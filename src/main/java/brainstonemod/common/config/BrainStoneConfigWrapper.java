package brainstonemod.common.config;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import brainstonemod.common.api.BrainStoneModules;
import brainstonemod.common.helper.BSP;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@UtilityClass
public class BrainStoneConfigWrapper {
	public static final String CAT_MISC = "miscellaneous";
	public static final String CAT_BSLC = "brainstonelifecapacitor";
	public static final String CAT_GEN = "worldgen";

	private static Configuration configStorage;
	private static final boolean isClient = FMLCommonHandler.instance().getSide() == Side.CLIENT;

	@Getter
	private static boolean enableCreativeTab;
	@Getter
	private static boolean enableAchievementPage;
	@Getter
	private static double essenceOfLifeBaseChance;
	@Getter
	private static int[] brainStoneOreDims;
	@Getter
	private static int[] brainStoneHouseDims;
	@Getter
	@ServerOverride
	private static boolean BSLCallowStealing;
	@Getter
	@ServerOverride
	private static long BSLCRFperHalfHeart;
	/** The X positions of the achievements */
	@SideOnly(Side.CLIENT)
	private static Map<String, Integer> achievementXPositions;
	@SideOnly(Side.CLIENT)
	/** The Y positions of the achievements */
	private static Map<String, Integer> achievementYPositions;

	public static Map<String, Object> getOverrideValues() {
		Map<String, Object> values = new HashMap<>();

		for (Field field : BrainStoneConfigWrapper.class.getDeclaredFields()) {
			if (field.isAnnotationPresent(ServerOverride.class)) {
				try {
					values.put(field.getName(), field.get(null));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					BSP.fatalException(e);
				}
			}
		}

		return values;
	}

	public static void loadConfig() {
		loadMiscellaneousSettings();
		loadBrainStoneLifeCapacitorSettings();
		loadWorldgenSettings();

		// Server doesn't care where the Achievements are
		if (isClient)
			loadAchievementSettings();

		saveIfChanged();
	}

	public static void loadConfig(Configuration config) {
		configStorage = config;
		configStorage.load();

		loadConfig();
	}

	private static void loadMiscellaneousSettings() {
		enableCreativeTab = getBoolean(CAT_MISC, "EnableCreativeTab", true,
				"Do you want to have a custom Creative Tab for this mod?", true);
		enableAchievementPage = getBoolean(CAT_MISC, "EnableAchievementPage", true,
				"Do you want to have a custom Achievement Page for this mod?", true);
		essenceOfLifeBaseChance = ((getInt(CAT_MISC, "EssenceOfLifeDropChanceModifier", 0, -50, 75,
				"Modifies the drop chance of the Essence of Life. Value in percent.\n"
						+ "If the base chance is 50% (depends on what other mods you have loaded) and you set this value to 20 (%) the chance becomes 50% + (50% * 20%) = 60%.")
				/ 100.0) + 1.0) * (BrainStoneModules.draconicEvolution() ? 0.1 : 0.5);

		addCustomCategoryComment(CAT_MISC, "This set defines miscellaneous settings");
	}

	private static void loadBrainStoneLifeCapacitorSettings() {
		BSLCallowStealing = getBoolean(CAT_BSLC, "AllowStealing", false,
				"Do you want to allow the stealing of the BrainStoneLifeCapacitor?");
		BSLCRFperHalfHeart = getInt(CAT_BSLC, "RFperHalfHeart", 1000000, 100, Integer.MAX_VALUE,
				"How much energy half a heart should cost.");

		addCustomCategoryComment(CAT_BSLC, "This set defines the behavior of the BrainStoneLifeCapacitor");
	}

	private static void loadWorldgenSettings() {
		brainStoneOreDims = getIntList(CAT_GEN, "BrainStoneOreDimensionsWhitelist", new int[] { 0, 7, -100 },
				"In which dimensions should Brain Stone Ore be generated");
		brainStoneHouseDims = getIntList(CAT_GEN, "BrainStoneHouseDimensionsWhitelist", new int[] { 0 },
				"In which dimensions should the Brain Stone House be generated");

		addCustomCategoryComment(CAT_GEN, "This set defines world generation settings");
	}

	@SideOnly(Side.CLIENT)
	private static void loadAchievementSettings() {
		achievementXPositions = new LinkedHashMap<>();
		achievementYPositions = new LinkedHashMap<>();

		String curAch, curAchUp;
		String achievementPageType = (enableAchievementPage ? "custom" : "regular");
		String achievementPageName = achievementPageType + "achievementpage";
		String achievementPageDescX = "Choose a x coordinate on the " + achievementPageType
				+ " Achievement Page for the ";
		String achievementPageDescY = "Choose a y coordinate on the " + achievementPageType
				+ " Achievement Page for the ";
		String[] achievementNames = new String[] { "WTHIT", "itLives", "intelligentBlocks", "intelligentTools",
				"lifeCapacitor" };
		int[] xPos = enableAchievementPage ? new int[] { 0, 2, 3, 3, 5 } : new int[] { 2, 4, 5, 5, 7 };
		int[] yPos = enableAchievementPage ? new int[] { 0, 0, 2, -2, -4 } : new int[] { 8, 8, 10, 6, 8 };

		// Save the positions of the achievements
		for (int i = 0; i < achievementNames.length; i++) {
			curAch = achievementNames[i];
			curAchUp = Character.toUpperCase(curAch.charAt(0)) + curAch.substring(1);

			achievementXPositions.put(curAch, getInt(achievementPageName, "Xpos" + curAchUp, xPos[i], -20, 20,
					achievementPageDescX + curAch + " achievement"));
			achievementYPositions.put(curAch, getInt(achievementPageName, "Ypos" + curAchUp, yPos[i], -20, 20,
					achievementPageDescY + curAch + " achievement"));
		}

		if (enableAchievementPage)
			addCustomCategoryComment("customachievementpage",
					"This set defines the positions of the achievements on the custom Brain Stone Mod Achievement Page.\nOnly applies when \"B:EnableAchievementPage\" is set to true.");
		else
			addCustomCategoryComment("regularachievementpage",
					"This set defines the positions of the achievements on the default Minecraft Achievement Page.\nOnly applies when \"B:EnableAchievementPage\" is set to false.");
	}

	private static String getDefaultLangKey(String category, String name) {
		return "gui.brainstone.config.cat." + category.toLowerCase() + '.' + name.toLowerCase();
	}

	/**
	 * Creates a boolean property.
	 * 
	 * @param category
	 *            Category of the property.
	 * @param name
	 *            Name of the property.
	 * @param defaultValue
	 *            Default value of the property.
	 * @param comment
	 *            A brief description what the property does.
	 * @return The value of the new boolean property.
	 */
	private static boolean getBoolean(String category, String name, boolean defaultValue, String comment) {
		return getBoolean(category, name, defaultValue, comment, false);
	}

	/**
	 * Creates a boolean property.
	 * 
	 * @param category
	 *            Category of the property.
	 * @param name
	 *            Name of the property.
	 * @param defaultValue
	 *            Default value of the property.
	 * @param comment
	 *            A brief description what the property does.
	 * @param requiresRestart
	 *            Whether Minecraft requires a restart if this changed while
	 *            running.
	 * @return The value of the new boolean property.
	 */
	private static boolean getBoolean(String category, String name, boolean defaultValue, String comment,
			boolean requiresRestart) {
		Property prop = configStorage.get(category, name, defaultValue);
		prop.setLanguageKey(getDefaultLangKey(category, name));
		prop.setComment(comment + " [default: " + defaultValue + "]");
		prop.setRequiresMcRestart(requiresRestart);

		return prop.getBoolean(defaultValue);
	}

	/**
	 * Creates a integer property.
	 * 
	 * @param category
	 *            Category of the property.
	 * @param name
	 *            Name of the property.
	 * @param defaultValue
	 *            Default value of the property.
	 * @param minValue
	 *            Minimum value of the property.
	 * @param maxValue
	 *            Maximum value of the property.
	 * @param comment
	 *            A brief description what the property does.
	 * @return The value of the new integer property.
	 */
	private static int getInt(String category, String name, int defaultValue, int minValue, int maxValue,
			String comment) {
		Property prop = configStorage.get(category, name, defaultValue);
		prop.setLanguageKey(getDefaultLangKey(category, name));
		prop.setComment(comment + " [range: " + minValue + " ~ " + maxValue + ", default: " + defaultValue + "]");
		prop.setMinValue(minValue);
		prop.setMaxValue(maxValue);

		int readValue = prop.getInt(defaultValue);
		int cappedValue = Math.max(Math.min(readValue, maxValue), minValue);

		if (readValue != cappedValue)
			prop.set(cappedValue);

		return cappedValue;
	}

	/**
	 * Creates a integer property.
	 *
	 * @param category
	 *            Category of the property.
	 * @param name
	 *            Name of the property.
	 * @param defaultValues
	 *            Default value of the property.
	 * @param comment
	 *            A brief description what the property does.
	 * @return The value of the new integer list property.
	 */
	private static int[] getIntList(String category, String name, int[] defaultValues, String comment) {
		Property prop = configStorage.get(category, name, defaultValues);
		prop.setLanguageKey(getDefaultLangKey(category, name));
		prop.setComment(comment + " [default: " + Arrays.toString(defaultValues) + "]");

		return prop.getIntList();
	}

	/**
	 * Adds a comment to the specified ConfigCategory object
	 *
	 * @param category
	 *            the config category
	 * @param comment
	 *            a String comment
	 */
	private static void addCustomCategoryComment(String category, String comment) {
		configStorage.setCategoryComment(category, comment);
	}

	private static void saveIfChanged() {
		if (configStorage.hasChanged())
			configStorage.save();
	}

	public static int getAchievementXPosition(String achievement) {
		if (isClient)
			return achievementXPositions.get(achievement);
		else
			return 0;
	}

	public static int getAchievementYPosition(String achievement) {
		if (isClient)
			return achievementYPositions.get(achievement);
		else
			return 0;
	}

	@SideOnly(Side.CLIENT)
	public static List<IConfigElement> getMiscCategory() {
		return new ConfigElement(configStorage.getCategory(CAT_MISC)).getChildElements();
	}

	@SideOnly(Side.CLIENT)
	public static List<IConfigElement> getBrainStoneLifeCapacitorCategory() {
		return new ConfigElement(configStorage.getCategory(CAT_BSLC)).getChildElements();
	}

	@SideOnly(Side.CLIENT)
	public static List<IConfigElement> getWorldgenCategory() {
		return new ConfigElement(configStorage.getCategory(CAT_GEN)).getChildElements();
	}
}
