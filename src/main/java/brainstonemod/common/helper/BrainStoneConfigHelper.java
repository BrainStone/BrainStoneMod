package brainstonemod.common.helper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import brainstonemod.common.api.BrainStoneModules;
import lombok.experimental.UtilityClass;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@UtilityClass
public class BrainStoneConfigHelper {
	public static final String CAT_MISC = "miscellaneous";
	public static final String CAT_BSLC = "brainstonelivecapacitor";
	public static final String CAT_GEN = "worldgen";

	private static Configuration configStorage;

	private static boolean enableCreativeTab;
	private static boolean enableAchievementPage;
	private static double essenceOfLifeBaseChance;
	private static int[] brainStoneOreDims;
	private static int[] brainStoneHouseDims;
	private static boolean BSLC_AllowStealing;
	private static long BSLC_RFperHalfHeart;
	/** The X positions of the achievements */
	private static Map<String, Integer> achievementXPositions = new LinkedHashMap<>();
	/** The X positions of the achievements */
	private static Map<String, Integer> achievementYPositions = new LinkedHashMap<>();

	public static void loadConfig() {
		loadConfig(null);
	}

	public static void loadConfig(Configuration config) {
		if (config == null) {
			config = configStorage;
		} else {
			configStorage = config;
			config.load();
		}

		enableCreativeTab = config.getBoolean("EnableCreativeTab", CAT_MISC, true,
				"Do you want to have a custom Creative Tab for this mod?");
		enableAchievementPage = config.getBoolean("EnableAchievementPage", CAT_MISC, true,
				"Do you want to have a custom Achievement Page for this mod?");
		essenceOfLifeBaseChance = ((config.getInt("EssenceOfLifeDropChanceModifier", CAT_MISC, 0, -50, 75,
				"Modifies the drop chance of the Essence of Life. Value in percent. If the chance is 50% (depends on what other mods you have loaded) and you set this value to 20 (%) the chance becomes 50% + (50% * 20%) = 60%.")
				/ 100.0) + 1.0) * (BrainStoneModules.draconicEvolution() ? 0.1 : 0.5);

		BSLC_AllowStealing = config.getBoolean("AllowStealing", CAT_BSLC, false,
				"Do you want to allow the stealing of the BrainStoneLifeCapacitor?");
		BSLC_RFperHalfHeart = config.getInt("RFperHalfHeart", CAT_BSLC, 1000000, 1, Integer.MAX_VALUE,
				"How much energy half a heart should cost.");

		brainStoneOreDims = config.get(CAT_GEN, "BrainStoneOreDimensionsWhitelist", new int[] { 0, 7, -100 },
				"In which dimensions should Brain Stone Ore be generated").getIntList();
		brainStoneHouseDims = config.get(CAT_GEN, "BrainStoneHouseDimensionsWhitelist", new int[] { 0 },
				"In which dimensions should the Brain Stone House be generated").getIntList();

		String curAch, curAchUp;
		String achievementPageType = (enableAchievementPage ? "custom" : "regular");
		String achievementPageName = achievementPageType + "achievementpage";
		String achievementPageDescX = "Choose a x coordinate on the " + achievementPageType
				+ " Achievement Page for the ";
		String achievementPageDescY = "Choose a y coordinate on the " + achievementPageType
				+ " Achievement Page for the ";
		String[] achievementNames = new String[] { "WTHIT", "ItLives", "IntelligentBlocks", "IntelligentTools",
				"lifeCapacitor" };
		int[] xPos = enableAchievementPage ? new int[] { 0, 2, 3, 3, 5 } : new int[] { 2, 4, 5, 5, 7 };
		int[] yPos = enableAchievementPage ? new int[] { 0, 0, 2, -2, -4 } : new int[] { 8, 8, 10, 6, 8 };

		// Save the positions of the achievements
		for (int i = 0; i < achievementNames.length; i++) {
			curAch = achievementNames[i];
			curAchUp = Character.toUpperCase(curAch.charAt(0)) + curAch.substring(1);

			achievementXPositions.put(curAch, config.getInt("Xpos" + curAchUp, achievementPageName, xPos[i], -20, 20,
					achievementPageDescX + curAch + " achievement"));
			achievementYPositions.put(curAch, config.getInt("Ypos" + curAchUp, achievementPageName, yPos[i], -20, 20,
					achievementPageDescY + curAch + " achievement"));
		}

		config.addCustomCategoryComment(CAT_MISC, "This set defines miscellaneous settings");
		config.addCustomCategoryComment(CAT_BSLC, "This set defines the behavior of the BrainStoneLiveCapacitor");
		config.addCustomCategoryComment(CAT_GEN, "This set defines world generation settings");

		if (enableAchievementPage)
			config.addCustomCategoryComment("customachievementpage",
					"This set defines the positions of the achievements on the custom Brain Stone Mod Achievement Page.\nOnly applies when \"B:EnableAchievementPage\" is set to true.");
		else
			config.addCustomCategoryComment("regularachievementpage",
					"This set defines the positions of the achievements on the default Minecraft Achievement Page.\nOnly applies when \"B:EnableAchievementPage\" is set to false.");

		config.save();
	}

	public static boolean enableCreativeTab() {
		return enableCreativeTab;
	}

	public static boolean enableAchievementPage() {
		return enableAchievementPage;
	}

	public static double getEssenceOfLifeBaseChance() {
		return essenceOfLifeBaseChance;
	}

	public static int[] getBrainStoneOreDims() {
		return brainStoneOreDims;
	}

	public static int[] getBrainStoneHouseDims() {
		return brainStoneHouseDims;
	}

	public static boolean BSLC_allowStealing() {
		return BSLC_AllowStealing;
	}

	public static long BSLC_RFperHalfHeart() {
		return BSLC_RFperHalfHeart;
	}

	public static int getAchievementXPosition(String achievement) {
		return achievementXPositions.get(achievement);
	}

	public static int getAchievementYPosition(String achievement) {
		return achievementYPositions.get(achievement);
	}

	@SideOnly(Side.CLIENT)
	public static List<IConfigElement> getMiscCategory() {
		return new ConfigElement(configStorage.getCategory(CAT_MISC)).getChildElements();
	}

	@SideOnly(Side.CLIENT)
	public static List<IConfigElement> getBrainStoneLiveCapacitorCategory() {
		return new ConfigElement(configStorage.getCategory(CAT_BSLC)).getChildElements();
	}

	@SideOnly(Side.CLIENT)
	public static List<IConfigElement> getWorldgenCategory() {
		return new ConfigElement(configStorage.getCategory(CAT_GEN)).getChildElements();
	}
}
