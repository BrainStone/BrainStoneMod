package mods.brainstone.containers;

import java.util.List;
import mods.brainstone.slots.SlotBlockBrainStoneTrigger;
import mods.brainstone.tileentities.TileEntityBlockBrainStoneTrigger;
import so;
import sq;
import tj;
import ul;
import wm;

public class ContainerBlockBrainStoneTrigger extends tj
{
  private final TileEntityBlockBrainStoneTrigger trigger;

  public ContainerBlockBrainStoneTrigger(so inventoryplayer, TileEntityBlockBrainStoneTrigger tileentityblockbrainstonetrigger)
  {
    this.trigger = tileentityblockbrainstonetrigger;

    a(new SlotBlockBrainStoneTrigger(this.trigger, 0, 148, 29));

    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 9; j++) {
        a(new ul(inventoryplayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
      }

    }

    for (i = 0; i < 9; i++)
      a(new ul(inventoryplayer, i, 8 + i * 18, 142));
  }

  public boolean a(sq entityplayer)
  {
    return this.trigger.a(entityplayer);
  }

  public wm b(sq par1EntityPlayer, int par2)
  {
    wm itemstack = null;
    ul slot = (ul)this.c.get(par2);

    if ((slot != null) && (slot.d())) {
      wm itemstack1 = slot.c();
      itemstack1.e();
      itemstack = itemstack1.m();

      if (par2 == 0) {
        if (!a(itemstack1, 1, 37, true)) {
          return null;
        }
        slot.a(itemstack1, itemstack);
      } else if ((par2 >= 1) && (par2 < 28)) {
        if (!a(itemstack1, 28, 37, false))
          return null;
      } else if ((par2 >= 28) && (par2 < 37)) {
        if (!a(itemstack1, 1, 28, false))
          return null;
      } else if (!a(itemstack1, 1, 37, false)) {
        return null;
      }
      if (itemstack1.a == 0)
        slot.c((wm)null);
      else {
        slot.e();
      }

      if (itemstack1.a == itemstack.a) {
        return null;
      }
      if (itemstack.a == 0) {
        return null;
      }
      slot.a(par1EntityPlayer, itemstack1);
    }

    return itemstack;
  }
}