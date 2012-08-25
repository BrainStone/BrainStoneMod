import java.util.Random;

public abstract class TileEntityBlockBrainStoneHiders extends kw
  implements io
{
  protected aan[] ItemStacks;

  public int a()
  {
    return this.ItemStacks.length;
  }

  public aan k_(int par1)
  {
    return this.ItemStacks[par1];
  }

  public aan a(int par1, int par2)
  {
    if (this.ItemStacks[par1] != null)
    {
      if (this.ItemStacks[par1].a <= par2)
      {
        aan itemstack = this.ItemStacks[par1];
        this.ItemStacks[par1] = null;
        return itemstack;
      }

      aan itemstack1 = this.ItemStacks[par1].a(par2);

      if (this.ItemStacks[par1].a == 0)
      {
        this.ItemStacks[par1] = null;
      }

      return itemstack1;
    }

    return null;
  }

  public aan b(int par1)
  {
    if (this.ItemStacks[par1] != null)
    {
      aan itemstack = this.ItemStacks[par1];
      this.ItemStacks[par1] = null;

      return itemstack;
    }

    return null;
  }

  public void a(int par1, aan par2ItemStack)
  {
    this.ItemStacks[par1] = par2ItemStack;

    if ((par2ItemStack != null) && (par2ItemStack.a > d()))
    {
      par2ItemStack.a = d();
    }
  }

  public int d()
  {
    return 1;
  }

  public boolean a_(yw par1EntityPlayer)
  {
    if (this.i.b(this.j, this.k, this.l) != this)
    {
      return false;
    }

    return par1EntityPlayer.f(this.j + 0.5D, this.k + 0.5D, this.l + 0.5D) <= 64.0D;
  }

  public void e()
  {
  }

  public void f()
  {
  }

  public void dropItems(xd par1World, int par2, int par3, int par4)
  {
    for (int i = 0; i < this.ItemStacks.length; i++)
    {
      aan itemstack = this.ItemStacks[i];

      if (itemstack != null)
      {
        float f = 0.7F;
        double d = par1World.r.nextFloat() * f + (1.0F - f) * 0.5D;
        double d1 = par1World.r.nextFloat() * f + (1.0F - f) * 0.5D;
        double d2 = par1World.r.nextFloat() * f + (1.0F - f) * 0.5D;
        fq entityitem = new fq(par1World, par2 + d, par3 + d1, par4 + d2, itemstack);
        entityitem.c = 10;
        par1World.a(entityitem);
      }
    }
  }

  public void a(ady par1NBTTagCompound)
  {
    super.a(par1NBTTagCompound);
  }

  public void b(ady par1NBTTagCompound)
  {
    super.b(par1NBTTagCompound);
  }
}