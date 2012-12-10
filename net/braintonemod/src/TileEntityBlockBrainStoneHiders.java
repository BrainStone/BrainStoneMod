package net.braintonemod.src;

import bq;
import java.util.Random;
import la;
import px;
import qx;
import um;
import xv;

public abstract class TileEntityBlockBrainStoneHiders extends TileEntityBrainStoneSyncBase
  implements la
{
  protected um[] ItemStacks;

  public int k_()
  {
    return this.ItemStacks.length;
  }

  public um a(int i)
  {
    return this.ItemStacks[i];
  }

  public um a(int i, int j)
  {
    if (this.ItemStacks[i] != null)
    {
      if (this.ItemStacks[i].a <= j)
      {
        um itemstack = this.ItemStacks[i];
        this.ItemStacks[i] = null;
        return itemstack;
      }

      um itemstack1 = this.ItemStacks[i].a(j);

      if (this.ItemStacks[i].a == 0)
      {
        this.ItemStacks[i] = null;
      }

      return itemstack1;
    }

    return null;
  }

  public um a_(int i)
  {
    if (this.ItemStacks[i] != null)
    {
      um itemstack = this.ItemStacks[i];
      this.ItemStacks[i] = null;
      return itemstack;
    }

    return null;
  }

  public void a(int i, um itemstack)
  {
    this.ItemStacks[i] = itemstack;

    if ((itemstack != null) && (itemstack.a > c()))
    {
      itemstack.a = c();
    }
  }

  public int c()
  {
    return 1;
  }

  public boolean a_(qx entityplayer)
  {
    if (this.k.q(this.l, this.m, this.n) != this)
    {
      return false;
    }

    return entityplayer.e(this.l + 0.5D, this.m + 0.5D, this.n + 0.5D) <= 64.0D;
  }

  public void l_()
  {
  }

  public void f()
  {
  }

  public void dropItems(xv world, int i, int j, int k)
  {
    for (int l = 0; l < this.ItemStacks.length; l++)
    {
      um itemstack = this.ItemStacks[l];

      if (itemstack != null)
      {
        float f = 0.7F;
        double d = world.u.nextFloat() * f + (1.0F - f) * 0.5D;
        double d1 = world.u.nextFloat() * f + (1.0F - f) * 0.5D;
        double d2 = world.u.nextFloat() * f + (1.0F - f) * 0.5D;
        px entityitem = new px(world, i + d, j + d1, k + d2, itemstack);
        entityitem.c = 10;
        world.d(entityitem);
      }
    }
  }

  public void a(bq nbttagcompound)
  {
    super.a(nbttagcompound);
  }

  public void b(bq nbttagcompound)
  {
    super.b(nbttagcompound);
  }
}