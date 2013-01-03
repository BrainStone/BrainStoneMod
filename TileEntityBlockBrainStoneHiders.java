package brainstone;

import bq;
import java.util.Random;
import la;
import px;
import qx;
import ur;
import yc;

public abstract class TileEntityBlockBrainStoneHiders extends TileEntityBrainStoneSyncBase
  implements la
{
  protected ur[] ItemStacks;

  public int k_()
  {
    return this.ItemStacks.length;
  }

  public ur a(int i)
  {
    return this.ItemStacks[i];
  }

  public ur a(int i, int j)
  {
    if (this.ItemStacks[i] != null)
    {
      if (this.ItemStacks[i].a <= j)
      {
        ur itemstack = this.ItemStacks[i];
        this.ItemStacks[i] = null;
        return itemstack;
      }

      ur itemstack1 = this.ItemStacks[i].a(j);

      if (this.ItemStacks[i].a == 0)
      {
        this.ItemStacks[i] = null;
      }

      return itemstack1;
    }

    return null;
  }

  public ur a_(int i)
  {
    if (this.ItemStacks[i] != null)
    {
      ur itemstack = this.ItemStacks[i];
      this.ItemStacks[i] = null;
      return itemstack;
    }

    return null;
  }

  public void a(int i, ur itemstack)
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

  public void dropItems(yc world, int i, int j, int k)
  {
    for (int l = 0; l < this.ItemStacks.length; l++)
    {
      ur itemstack = this.ItemStacks[l];

      if (itemstack != null)
      {
        float f = 0.7F;
        double d = world.t.nextFloat() * f + (1.0F - f) * 0.5D;
        double d1 = world.t.nextFloat() * f + (1.0F - f) * 0.5D;
        double d2 = world.t.nextFloat() * f + (1.0F - f) * 0.5D;
        px entityitem = new px(world, i + d, j + d1, k + d2, itemstack);
        entityitem.b = 10;
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