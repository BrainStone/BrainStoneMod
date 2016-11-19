package brainstonemod.common.achievement;

import javax.annotation.Nullable;

import betterachievements.api.components.achievement.ICustomBackgroundColour;
import betterachievements.api.util.ColourHelper;
import brainstonemod.BrainStone;
import brainstonemod.common.compat.BrainStoneModules;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(iface = "betterachievements.api.components.achievement.ICustomBackgroundColour", modid = BrainStoneModules.BETTER_ACHIEVEMENTS_MODID)
public class BrainStoneAchievement extends Achievement implements ICustomBackgroundColour {
	private static final float twoThirdPi = (float) (Math.PI * 2.0 / 3.0);

	private boolean renderRainbow;

	public static int getRainbowColour() {
		return getRainbowColour(1.0f, 1.0f, 1.0f, 0, twoThirdPi, twoThirdPi * 2.0f, 127.5f, 127.5f, 1000);
	}

	/**
	 * Gives a colour based of {@link System#currentTimeMillis()} and given
	 * params.<br>
	 * Adopted and fixed from
	 * {@link ColourHelper#getRainbowColour(float, float, float, float, float, float, float, float, float)}.
	 *
	 * @param freqR
	 *            frequency of the reds
	 * @param freqG
	 *            frequency of the greens
	 * @param freqB
	 *            frequency of the blues
	 * @param phaseR
	 *            phase shift red
	 * @param phaseG
	 *            phase shift green
	 * @param phaseB
	 *            phase shift blue
	 * @param center
	 *            center value
	 * @param width
	 *            width of colour range
	 * @param length
	 *            change rate
	 * @return an int colour
	 */
	public static int getRainbowColour(float freqR, float freqG, float freqB, float phaseR, float phaseG, float phaseB,
			float center, float width, float length) {
		double i = (System.currentTimeMillis() * 2 * Math.PI) / (double) length;
		double r = Math.sin(freqR * i + phaseR) * width + center;
		double g = Math.sin(freqG * i + phaseG) * width + center;
		double b = Math.sin(freqB * i + phaseB) * width + center;

		return ColourHelper.RGB((int) r, (int) g, (int) b);
	}

	/**
	 * Create a new achievement in the BrainStoneMod.<br />
	 * <strong>The achievement will be registered automatically!</strong>
	 */
	public BrainStoneAchievement(String unlocalizedName, int column, int row, Block block,
			@Nullable Achievement parent) {
		this(unlocalizedName, column, row, new ItemStack(block), parent);
	}

	/**
	 * Create a new achievement in the BrainStoneMod.<br />
	 * <strong>The achievement will be registered automatically!</strong>
	 */
	public BrainStoneAchievement(String unlocalizedName, int column, int row, Item item, @Nullable Achievement parent) {
		this(unlocalizedName, column, row, new ItemStack(item), parent);
	}

	/**
	 * Create a new achievement in the BrainStoneMod.<br />
	 * <strong>The achievement will be registered automatically!</strong>
	 */
	public BrainStoneAchievement(String unlocalizedName, int column, int row, ItemStack itemStack,
			@Nullable Achievement parent) {
		super(BrainStone.RESOURCE_PACKAGE + "." + unlocalizedName, BrainStone.RESOURCE_PACKAGE + "." + unlocalizedName,
				column, row, itemStack, parent);
		renderRainbow = false;

		BrainStone.achievements.put(unlocalizedName, this);
		registerStat();
	}

	public BrainStoneAchievement setRainbow() {
		renderRainbow = true;

		return this;
	}

	@Override
	public int recolourBackground(float greyScale) {
		int grey = (int) (0xff * greyScale);
		int greyTone = ColourHelper.RGB(grey, grey, grey);

		if (renderRainbow)
			return ColourHelper.blend(greyTone, getRainbowColour());
		else
			return greyTone;
	}
}
