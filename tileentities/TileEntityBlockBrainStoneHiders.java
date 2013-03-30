package mods.brainstone.tileentities;

import aab;
import bs;
import java.util.Random;
import lt;
import mods.brainstone.templates.TileEntityBrainStoneSyncBase;
import rh;
import sq;
import wm;

public abstract class TileEntityBlockBrainStoneHiders extends TileEntityBrainStoneSyncBase
  implements lt
{
  protected wm[] ItemStacks;

  public void g()
  {
  }

  public wm a(int i, int j)
  {
    if (this.ItemStacks[i] != null) {
      if (this.ItemStacks[i].a <= j) {
        wm itemstack = this.ItemStacks[i];
        this.ItemStacks[i] = null;
        return itemstack;
      }

      wm itemstack1 = this.ItemStacks[i].a(j);

      if (this.ItemStacks[i].a == 0) {
        this.ItemStacks[i] = null;
      }

      return itemstack1;
    }
    return null;
  }

  public void dropItems(aab world, int i, int j, int k) {
    for (int l = 0; l < this.ItemStacks.length; l++) {
      wm itemstack = this.ItemStacks[l];

      if (itemstack != null) {
        float f = 0.7F;
        double d = world.s.nextFloat() * 0.7F + 0.1500000059604645D;

        double d1 = world.s.nextFloat() * 0.7F + 0.1500000059604645D;

        double d2 = world.s.nextFloat() * 0.7F + 0.1500000059604645D;

        rh entityitem = new rh(world, i + d, j + d1, k + d2, itemstack);

        entityitem.b = 10;
        world.d(entityitem);
      }
    }
  }

  public int d()
  {
    return 1;
  }

  public int j_()
  {
    return this.ItemStacks.length;
  }

  public wm a(int i)
  {
    return this.ItemStacks[i];
  }

  public wm b(int i)
  {
    if (this.ItemStacks[i] != null) {
      wm itemstack = this.ItemStacks[i];
      this.ItemStacks[i] = null;
      return itemstack;
    }
    return null;
  }

  public boolean a(sq entityplayer)
  {
    if (this.k.r(this.l, this.m, this.n) != this) {
      return false;
    }
    return entityplayer.e(this.l + 0.5D, this.m + 0.5D, this.n + 0.5D) <= 64.0D;
  }

  public void f()
  {
  }

  public void a(bs nbttagcompound)
  {
    super.a(nbttagcompound);
  }

  public void a(int i, wm itemstack)
  {
    this.ItemStacks[i] = itemstack;

    if ((itemstack != null) && (itemstack.a > d()))
    {
      itemstack.a = d();
    }
  }

  public void b(bs nbttagcompound)
  {
    super.b(nbttagcompound);
  }
}