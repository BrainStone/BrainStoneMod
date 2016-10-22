package brainstonemod.common.helper;

import brainstonemod.BrainStone;
import brainstonemod.common.api.BrainStoneModules;
import cpw.mods.fml.client.config.IConfigElement;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lombok.experimental.UtilityClass;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
public class BrainStoneConfigHelper {
	public static final String CAT_DISPLAY = "display";
	public static final String CAT_BSLC = "brainstonelivecapacitor";

	private static Configuration configStorage;

	private static byte updateNotification;
	private static boolean enableCreativeTab;
	private static boolean enableAchievementPage;
	private static boolean BSLC_AllowStealing;
	private static long BSLC_RFperHalfHeart;
	//TODO: Add 2 integer arrays, one for Brainstone Ore dimension IDs, one for Brainstone House dimension IDs
	/** The X positions of the achievements */
	private static Map<String, Integer> achievementXPositions = new LinkedHashMap<>();
	/** The X positions of the achievements */
	private static Map<String, Integer> achievementYPositions = new LinkedHashMap<>();

	public static void loadConfig() {
		loadConfig(configStorage);
	}

	public static void loadConfig(Configuration config) {
		configStorage = config;

		config.load();

		final String str = config.getString("DisplayUpdates", CAT_DISPLAY,
				BrainStone.DEV ? "recommended" : (BrainStone.release ? "release" : "latest"),
				"What update notifications do you want to recieve?\nValues are: release, recommended, latest, none (or off)");
		updateNotification = (byte) ((str.equals("none") || str.equals("off")) ? -1
				: (str.equals("recommended") ? 1 : (str.equals("latest") ? 2 : 0)));

		enableCreativeTab = config.getBoolean("EnableCreativeTab", CAT_DISPLAY, true,
				"Do you want to have a custom Creative Tab for this mod?");
		enableAchievementPage = config.getBoolean("EnableAchievementPage", CAT_DISPLAY, true,
				"Do you want to have a custom Achievement Page for this mod?");

		String curAch, curAchUp;
		String achievementPageType = (enableAchievementPage ? "custom" : "regular");
		String achievementPageName = achievementPageType + "achievementpage";
		String achievementPageDescX = "Choose a x coordinate on the " + achievementPageType
				+ " Achievement Page for the ";
		String achievementPageDescY = "Choose a y coordinate on the " + achievementPageType
				+ " Achievement Page for the ";
		String[] achievementNames = new String[] { "WTHIT", "itLives", "intelligentBlocks", "intelligentTools",
				"logicBlock" };
		int[] xPos = enableAchievementPage ? new int[] { 0, 2, 3, 3, 5 } : new int[] { 2, 4, 5, 5, 7 };
		int[] yPos = enableAchievementPage ? new int[] { 0, 0, 2, -2, 4 } : new int[] { 8, 8, 10, 6, 12 };

		// Save the positions of the achievements
		for (int i = 0; i < achievementNames.length; i++) {
			curAch = achievementNames[i];
			curAchUp = Character.toUpperCase(curAch.charAt(0)) + curAch.substring(1);

			achievementXPositions.put(curAch, config.getInt("Xpos" + curAchUp, achievementPageName, xPos[i], -20, 20,
					achievementPageDescX + curAch + " achievement"));
			achievementYPositions.put(curAch, config.getInt("Ypos" + curAchUp, achievementPageName, yPos[i], -20, 20,
					achievementPageDescY + curAch + " achievement"));
		}

		if (BrainStoneModules.energy()) {
			BSLC_AllowStealing = config.getBoolean("AllowStealing", CAT_BSLC, false,
					"Do you want to allow the stealing of the BrainStoneLifeCapacitor?");
			BSLC_RFperHalfHeart = config.getInt("RFperHalfHeart", CAT_BSLC, 1000000, 1, Integer.MAX_VALUE,
					"How much energy half a heart should cost.");

			config.addCustomCategoryComment(CAT_BSLC, "This set defines the behavior of the BrainStoneLiveCapacitor");
		}

		config.addCustomCategoryComment(CAT_DISPLAY, "This set defines some basic ingame display settings");

		if (enableAchievementPage)
			config.addCustomCategoryComment("customachievementpage",
					"This set defines the positions of the achievements on the custom Brain Stone Mod Achievement Page.\nOnly applies when \"B:EnableAchievementPage\" is set to true.");
		else
			config.addCustomCategoryComment("regularachievementpage",
					"This set defines the positions of the achievements on the default Minecraft Achievement Page.\nOnly applies when \"B:EnableAchievementPage\" is set to false.");

		config.save();
	}

	/**
	 * <tt><table><tr><td>0:</td><td>release</td></tr><tr><td>1:</td><td>recommended</td></tr><tr><td>2:</td><td>latest</td></tr><tr><td>-1:</td><td><i>none</i></td></tr></table></tt>
	 */
	public static byte updateNotification() {
		return updateNotification;
	}

	public static boolean enableCreativeTab() {
		return enableCreativeTab;
	}

	public static boolean enableAchievementPage() {
		return enableAchievementPage;
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
	public static List<IConfigElement> getDisplayCategory() {
		return new ConfigElement(configStorage.getCategory(CAT_DISPLAY)).getChildElements();
	}

	@SideOnly(Side.CLIENT)
	public static List<IConfigElement> getBrainStoneLiveCapacitorCategory() {
		return new ConfigElement(configStorage.getCategory(CAT_BSLC)).getChildElements();
	}
}
