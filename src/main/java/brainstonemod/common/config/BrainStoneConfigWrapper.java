package brainstonemod.common.config;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import brainstonemod.common.compat.BrainStoneModules;
import brainstonemod.common.compat.ModuleInformation;
import brainstonemod.common.helper.BSP;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@UtilityClass
public class BrainStoneConfigWrapper {
	public static final String CAT_MODULES = "modules";
	public static final String CAT_MISC = "miscellaneous";
	public static final String CAT_BSLC = "brainstonelifecapacitor";
	public static final String CAT_GEN = "worldgen";

	private static Configuration configStorage;

	@Getter
	@ServerOverride
	private static boolean enableJEIReloading;
	@Getter
	private static boolean enableCreativeTab;
	@Getter
	private static double essenceOfLifeBaseChance;
	@Getter
	private static int[] brainStoneOreDims;
	@Getter
	private static int brainStoneOreVeinCount;
	@Getter
	private static int brainStoneOreVeinSize;
	@Getter
	private static int[] brainStoneHouseDims;
	@Getter
	private static int brainStoneHouseRarity;
	@Getter
	@ServerOverride
	private static boolean BSLCallowStealing;
	@Getter
	@ServerOverride
	private static long BSLCRFperHalfHeart;

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
		loadModuleSettings();
		loadMiscellaneousSettings();
		loadBrainStoneLifeCapacitorSettings();
		loadWorldgenSettings();

		saveIfChanged();
	}

	public static void loadConfig(Configuration config) {
		configStorage = config;
		configStorage.load();

		loadConfig();
	}

	private static void loadModuleSettings() {
		for (ModuleInformation module : BrainStoneModules.getAllModules()) {
			if (getBoolean(CAT_MODULES, "Disable" + firstUpper(module.getModid()), false,
					"Manually disable the compatibility module for " + module.getName(), true)) {
				if (module.isActive())
					BSP.info("Module " + module.getName() + " manually disabled!");

				module.disable();
			}
		}

		enableJEIReloading = getBoolean(CAT_MODULES, "EnableJEIReloading", true, "Reload JEI when settings change.\n"
				+ "Big modpacks should disable this! If not on the client then on the server!\n"
				+ "This setting also doesn't break anything if disabled. It only prevents some incorrect tooltips in JEI when some server and client configs are different.");

		addCustomCategoryComment(CAT_MODULES,
				"This set allows you to manually disable certain compatibility modules for other mods.");
	}

	private static void loadMiscellaneousSettings() {
		enableCreativeTab = getBoolean(CAT_MISC, "EnableCreativeTab", true,
				"Do you want to have a custom Creative Tab for this mod?", true);
		essenceOfLifeBaseChance = ((getInt(CAT_MISC, "EssenceOfLifeDropChanceModifier", 0, -50, 75,
				"Modifies the drop chance of the Essence of Life. Value in percent.\n"
						+ "If the base chance is 50% (depends on what other mods you have loaded) and you set this value to 20 (%) the chance becomes 50% + (50% * 20%) = 60%.")
				/ 100.0) + 1.0) * (BrainStoneModules.draconicEvolution() ? 0.1 : 0.5);

		addCustomCategoryComment(CAT_MISC, "This set defines miscellaneous settings.");
	}

	private static void loadBrainStoneLifeCapacitorSettings() {
		BSLCallowStealing = getBoolean(CAT_BSLC, "AllowStealing", false,
				"Do you want to allow the stealing of the BrainStoneLifeCapacitor?");
		BSLCRFperHalfHeart = getInt(CAT_BSLC, "RFperHalfHeart", 1000000, 200, Integer.MAX_VALUE,
				"How much energy half a heart should cost?");

		addCustomCategoryComment(CAT_BSLC, "This set defines the behavior of the BrainStoneLifeCapacitor.");
	}

	private static void loadWorldgenSettings() {
		brainStoneOreDims = getIntList(CAT_GEN, "BrainStoneOreDimensionsWhitelist", new int[] { 0, 7, -100 },
				"In which dimensions should Brain Stone Ore be generated?");
		brainStoneOreVeinCount = getInt(CAT_GEN, "BrainStoneOreVeinCount", 1, 1, 20,
				"How many veins of BrainStoneOre per chunk should be generated?");
		brainStoneOreVeinSize = getInt(CAT_GEN, "BrainStoneOreVeinSize", 20, 1, 100,
				"How big should the BrainStoneOre veins be? (In blocks on average)");
		brainStoneHouseDims = getIntList(CAT_GEN, "BrainStoneHouseDimensionsWhitelist", new int[] { 0 },
				"In which dimensions should the Brain Stone House be generated?");
		brainStoneHouseRarity = getInt(CAT_GEN, "BrainStoneHouseRarity", 10000, 100, 1000000,
				"How rare should the BraiNStoneHouse be? (Generated once every n chunks)");

		addCustomCategoryComment(CAT_GEN, "This set defines world generation settings.");
	}

	private static String firstUpper(String str) {
		return Character.toUpperCase(str.charAt(0)) + str.substring(1);
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

	@SideOnly(Side.CLIENT)
	public static List<IConfigElement> getModulesCategory() {
		return new ConfigElement(configStorage.getCategory(CAT_MODULES)).getChildElements();
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
