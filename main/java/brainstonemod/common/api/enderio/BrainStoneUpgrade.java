package brainstonemod.common.api.enderio;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import brainstonemod.BrainStone;
import cpw.mods.fml.common.Optional;
import crazypants.enderio.item.darksteel.DarkSteelItems;
import crazypants.enderio.item.darksteel.upgrade.AbstractUpgrade;

@Optional.Interface(iface = "crazypants.enderio.item.darksteel.upgrade.IDarkSteelUpgrade", modid = "EnderIO")
public class BrainStoneUpgrade extends AbstractUpgrade {
	private static String UPGRADE_NAME = "brainstone";

	public static final BrainStoneUpgrade UPGRADE = new BrainStoneUpgrade(
			new ItemStack(BrainStone.brainStone()), 20);

	protected BrainStoneUpgrade(ItemStack upgradeItem, int levelCost) {
		super(UPGRADE_NAME, KEY_UPGRADE_PREFIX + UPGRADE_NAME, upgradeItem,
				levelCost);
	}

	public BrainStoneUpgrade(NBTTagCompound tag) {
		super(UPGRADE_NAME, tag);
	}

	@Override
	@Optional.Method(modid = "EnderIO")
	public boolean canAddToItem(ItemStack stack) {
		if (stack == null
				|| (stack.getItem() != DarkSteelItems.itemDarkSteelBoots
						&& stack.getItem() != DarkSteelItems.itemDarkSteelLeggings
						&& stack.getItem() != DarkSteelItems.itemDarkSteelChestplate && stack
						.getItem() != DarkSteelItems.itemDarkSteelHelmet)) {
			return false;
		}

		return !hasUpgrade(stack);
	}

	@Override
	@Optional.Method(modid = "EnderIO")
	public void writeUpgradeToNBT(NBTTagCompound upgradeRoot) {
	}

}
