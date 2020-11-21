package brainstonemod.common.container;

import brainstonemod.common.slot.SlotBlockBrainStoneTrigger;
import brainstonemod.common.tileentity.TileEntityBrainStoneTrigger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerBrainStoneTrigger extends Container {
  /** Temporary storage of the TileEntity */
  private final TileEntityBrainStoneTrigger trigger;

  public ContainerBrainStoneTrigger(
      InventoryPlayer inventoryplayer,
      TileEntityBrainStoneTrigger tileentityblockbrainstonetrigger) {
    trigger = tileentityblockbrainstonetrigger;
    int i;
    addSlotToContainer(new SlotBlockBrainStoneTrigger(trigger, 0, 152, 33));

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
    return trigger.isUsableByPlayer(entityplayer);
  }

  @Override
  public ItemStack transferStackInSlot(EntityPlayer player, int index) {
    ItemStack itemstack = ItemStack.EMPTY;
    Slot slot = inventorySlots.get(index);
    final int startIndex = trigger.getSizeInventory();
    final int endIndex = startIndex + 36;

    if ((slot != null) && slot.getHasStack()) {
      ItemStack itemstack_linked = slot.getStack();
      itemstack = itemstack_linked.copy();

      if (index < startIndex) {
        if (!mergeItemStack(itemstack_linked, startIndex, endIndex, true)) return ItemStack.EMPTY;
      } else {
        boolean allSlotsFull = true;

        for (int i = 0; i < startIndex; ++i) {
          if (inventorySlots.get(0).getHasStack()
              || !inventorySlots.get(0).isItemValid(itemstack_linked)) {
            continue;
          }

          if (!itemstack_linked.isEmpty()) {
            ItemStack result = itemstack_linked.copy();
            result.setCount(1);

            inventorySlots.get(0).putStack(result);
            itemstack_linked.shrink(1);

            allSlotsFull = false;
            break;
          }
        }

        if (allSlotsFull) return ItemStack.EMPTY;
      }

      if (itemstack_linked.isEmpty()) {
        slot.putStack(ItemStack.EMPTY);
      } else {
        slot.onSlotChanged();
      }

      if (itemstack_linked.getCount() == itemstack.getCount()) return ItemStack.EMPTY;

      slot.onTake(player, itemstack_linked);
    }

    return itemstack;
  }
}
