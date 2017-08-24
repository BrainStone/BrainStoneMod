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
	private static final float twoThirdPi = (float) ((Math.PI * 2.0) / 3.0);

	private boolean renderRainbow;

	private static int getRainbowColour() {
		return ColourHelper.getRainbowColour(1.0f, 1.0f, 1.0f, 0, twoThirdPi, twoThirdPi * 2.0f, 127.5f, 127.5f, 1000);
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
		final int greyTone = ColourHelper.RGB(greyScale, greyScale, greyScale);

		if (renderRainbow)
			return ColourHelper.blend(getRainbowColour(), greyTone);
		else
			return greyTone;
	}
}
