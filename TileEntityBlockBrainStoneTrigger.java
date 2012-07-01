public class TileEntityBlockBrainStoneTrigger extends kw
  implements io
{
  private aan[] triggerItemStacks;
  public boolean mob;

  public TileEntityBlockBrainStoneTrigger()
  {
    this.triggerItemStacks = new aan[1];
    this.mob = false;
  }

  public int a()
  {
    return this.triggerItemStacks.length;
  }

  public aan k_(int par1)
  {
    return this.triggerItemStacks[par1];
  }

  public aan a(int par1, int par2)
  {
    if (this.triggerItemStacks[par1] != null)
    {
      if (this.triggerItemStacks[par1].a <= par2)
      {
        aan itemstack = this.triggerItemStacks[par1];
        this.triggerItemStacks[par1] = null;
        return itemstack;
      }

      aan itemstack1 = this.triggerItemStacks[par1].a(par2);

      if (this.triggerItemStacks[par1].a == 0)
      {
        this.triggerItemStacks[par1] = null;
      }

      return itemstack1;
    }

    return null;
  }

  public aan b(int par1)
  {
    if (this.triggerItemStacks[par1] != null)
    {
      aan itemstack = this.triggerItemStacks[par1];
      this.triggerItemStacks[par1] = null;

      return itemstack;
    }

    return null;
  }

  public void a(int par1, aan par2ItemStack)
  {
    this.triggerItemStacks[par1] = par2ItemStack;

    if ((par2ItemStack != null) && (par2ItemStack.a > d()))
    {
      par2ItemStack.a = d();
    }
  }

  public String c()
  {
    return "container.brainstonetrigger";
  }

  public void a(ady par1NBTTagCompound)
  {
    super.a(par1NBTTagCompound);

    no nbttaglist = par1NBTTagCompound.n("ItemsBrainStoneTrigger");
    this.triggerItemStacks = new aan[a()];

    for (int i = 0; i < nbttaglist.d(); i++)
    {
      ady nbttagcompound = (ady)nbttaglist.a(i);
      byte byte0 = nbttagcompound.d("SlotBrainStoneTrigger");

      if ((byte0 >= 0) && (byte0 < this.triggerItemStacks.length))
      {
        this.triggerItemStacks[byte0] = aan.a(nbttagcompound);
      }
    }
  }

  public void b(ady par1NBTTagCompound)
  {
    super.b(par1NBTTagCompound);

    no nbttaglist = new no();

    for (int i = 0; i < this.triggerItemStacks.length; i++)
    {
      if (this.triggerItemStacks[i] != null)
      {
        ady nbttagcompound = new ady();
        nbttagcompound.a("SlotBrainStoneTrigger", (byte)i);
        this.triggerItemStacks[i].b(nbttagcompound);
        nbttaglist.a(nbttagcompound);
      }
    }

    par1NBTTagCompound.a("ItemsBrainStoneTrigger", nbttaglist);
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

  public int getTextureId(ali par1IBlockAccess, int par2, int par3, int par4)
  {
    aan tmpItemStack = this.triggerItemStacks[0];

    if (tmpItemStack == null) {
      return -1;
    }
    int i = tmpItemStack.c;

    if ((i > 255) || (i <= 0)) {
      return -1;
    }
    pb tmp = pb.m[i];

    if (tmp == null) {
      return -1;
    }
    return tmp.d(par1IBlockAccess, par2, par3, par4, 1);
  }
}