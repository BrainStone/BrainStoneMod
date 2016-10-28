package brainstonemod.common.item.energy;

import net.minecraft.item.ItemStack;

/**
 * This code is mostly taken from <a href=
 * "https://github.com/SleepyTrousers/EnderIO/blob/1.10/src/main/java/crazypants/enderio/power/IInternalPoweredItem.java">
 * https://github.com/SleepyTrousers/EnderIO/blob/1.10/src/main/java/crazypants/
 * enderio/power/IInternalPoweredItem.java</a>. All credit to the original
 * author
 * 
 * @author BrainStone
 */
public interface IEnergyContainerItem {
	long getMaxEnergyStored(ItemStack stack);

	long getEnergyStored(ItemStack stack);

	void setEnergyStored(ItemStack container, long energy);

	long getMaxInput(ItemStack stack);

	long getMaxOutput(ItemStack stack);
}
