package brainstonemod.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import brainstonemod.common.slot.SlotBlockBrainStoneTrigger;
import brainstonemod.common.tileentity.TileEntityBrainStoneTrigger;

public class ContainerBrainStoneTrigger extends Container {
	/** Temporary storage of the TileEntity */
	private final TileEntityBrainStoneTrigger trigger;

	public ContainerBrainStoneTrigger(InventoryPlayer inventoryplayer, TileEntityBrainStoneTrigger tileentityblockbrainstonetrigger) {
		trigger = tileentityblockbrainstonetrigger;
		int i;
		addSlotToContainer(new SlotBlockBrainStoneTrigger(trigger, 0, 148, 29));

		for (i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				addSlotToContainer(new Slot(inventoryplayer, j + (i * 9) + 9, 8 + (j * 18), 84 + (i * 18)));
			}
		}

		for (i = 0; i < 9; ++i) {
			addSlotToContainer(new Slot(inventoryplayer, i, 8 + (i * 18), 142));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return trigger.isUseableByPlayer(entityplayer);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int i) {
		Slot slot = getSlot(i);
		if (slot != null && slot.getHasStack()) {
			ItemStack is = slot.getStack();
			ItemStack result = is.copy();

			if (i >= 36) {
				if (!mergeItemStack(is, 0, 36, false)) {
					return null;
				}
			} else if (!mergeItemStack(is, 36, 36 + trigger.getSizeInventory(), false)) {
				return null;
			}
			if (is.stackSize == 0) {
				slot.putStack(null);
			} else {
				slot.onSlotChanged();
			}
			slot.onPickupFromSlot(player, is);
			return result;
		}
		return null;
	}
}
