package brainstonemod.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import brainstonemod.common.slot.SlotBlockBrainStoneTrigger;
import brainstonemod.common.tileentity.TileEntityBlockBrainStoneTrigger;

public class ContainerBlockBrainStoneTrigger extends Container {
	/** Temporary storage of the TileEntity */
	private final TileEntityBlockBrainStoneTrigger trigger;

	/**
	 * Sets all the slots on the Gui.
	 * 
	 * @param inventoryplayer
	 *            The inventory of the player. Needed to display the player
	 *            inventory on the Gui
	 * @param tileentityblockbrainstonetrigger
	 *            The TileEntity
	 */
	public ContainerBlockBrainStoneTrigger(InventoryPlayer inventoryplayer,
			TileEntityBlockBrainStoneTrigger tileentityblockbrainstonetrigger) {
		trigger = tileentityblockbrainstonetrigger;
		int i;
		addSlotToContainer(new SlotBlockBrainStoneTrigger(trigger, 0, 148, 29));

		for (i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				addSlotToContainer(new Slot(inventoryplayer, j + (i * 9) + 9,
						8 + (j * 18), 84 + (i * 18)));
			}
		}

		for (i = 0; i < 9; ++i) {
			addSlotToContainer(new Slot(inventoryplayer, i, 8 + (i * 18), 142));
		}
	}

	/**
	 * Determines if the player can use this.
	 * 
	 * @param entityplayer
	 *            The player
	 * @return Depends on the distance of the player to the block
	 */
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return trigger.isUseableByPlayer(entityplayer);
	}

	/**
	 * Called when a player shift-clicks on a slot. You must override this or
	 * you will crash when someone does that.
	 */
	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
		ItemStack itemstack = null;
		final Slot slot = (Slot) inventorySlots.get(par2);

		if ((slot != null) && slot.getHasStack()) {
			final ItemStack itemstack1 = slot.getStack();
			itemstack1.getMaxStackSize();
			itemstack = itemstack1.copy();

			if (par2 == 0) {
				if (!mergeItemStack(itemstack1, 1, 37, true))
					return null;

				slot.onSlotChange(itemstack1, itemstack);
			} else if ((par2 >= 1) && (par2 < 28)) {
				if (!mergeItemStack(itemstack1, 28, 37, false))
					return null;
			} else if ((par2 >= 28) && (par2 < 37)) {
				if (!mergeItemStack(itemstack1, 1, 28, false))
					return null;
			} else if (!mergeItemStack(itemstack1, 1, 37, false))
				return null;

			if (itemstack1.stackSize == 0) {
				slot.putStack((ItemStack) null);
			} else {
				slot.onSlotChanged();
			}

			if (itemstack1.stackSize == itemstack.stackSize)
				return null;

			if (itemstack.stackSize == 0)
				return null;

			slot.onPickupFromSlot(par1EntityPlayer, itemstack1);
		}

		return itemstack;
	}
}
