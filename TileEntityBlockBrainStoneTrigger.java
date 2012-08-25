import java.util.Random;

public class TileEntityBlockBrainStoneTrigger extends TileEntityBlockBrainStoneHiders
  implements io
{
  public boolean power;
  private boolean[] mobTriggered;
  private static final int mobTriggeredSize = 5;
  public boolean forceUpdate;
  public byte delay;

  public TileEntityBlockBrainStoneTrigger()
  {
    this.ItemStacks = new aan[1];
    this.mobTriggered = new boolean[5];
    this.power = false;
    this.forceUpdate = false;
    this.delay = 0;

    for (int i = 0; i < 5; i++)
    {
      this.mobTriggered[i] = true;
    }
  }

  public String c()
  {
    return "container.brainstonetrigger";
  }

  public int getTextureId(ali par1IBlockAccess, int par2, int par3, int par4)
  {
    aan tmpItemStack = this.ItemStacks[0];

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

  public int getIndex(String type)
  {
    if (type == "item")
      return 0;
    if (type == "animal")
      return 1;
    if (type == "monster")
      return 2;
    if (type == "nether")
      return 3;
    if (type == "player") {
      return 4;
    }
    return -1;
  }

  public String getType(int index)
  {
    if (index == 0)
      return "item";
    if (index == 1)
      return "animal";
    if (index == 2)
      return "monster";
    if (index == 3)
      return "nether";
    if (index == 4) {
      return "player";
    }
    return "";
  }

  public boolean getMobTriggered(String type)
  {
    return getMobTriggered(getIndex(type));
  }

  public boolean getMobTriggered(int index)
  {
    if ((index >= 5) || (index < 0)) {
      return false;
    }
    return this.mobTriggered[index];
  }

  public void setMobTriggered(String type, boolean flag)
  {
    setMobTriggered(getIndex(type), flag);
  }

  public void setMobTriggered(int index, boolean flag)
  {
    if ((index >= 5) || (index < 0)) {
      return;
    }
    this.mobTriggered[index] = flag;
  }

  public void invertMobTriggered(int index)
  {
    if ((index >= 5) || (index < 0)) {
      return;
    }
    this.mobTriggered[index] = (this.mobTriggered[index] == 0 ? 1 : false);
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

    no nbttaglist = par1NBTTagCompound.n("ItemsBrainStoneTrigger");
    this.ItemStacks = new aan[a()];

    for (int i = 0; i < nbttaglist.d(); i++)
    {
      ady nbttagcompound = (ady)nbttaglist.a(i);
      byte byte0 = nbttagcompound.d("SlotBrainStoneTrigger");

      if ((byte0 >= 0) && (byte0 < this.ItemStacks.length))
      {
        this.ItemStacks[byte0] = aan.a(nbttagcompound);
      }
    }

    for (int i = 0; i < 5; i++)
    {
      this.mobTriggered[i] = par1NBTTagCompound.o(getType(i));
    }

    this.delay = par1NBTTagCompound.d("BrainStoneDelay");
  }

  public void b(ady par1NBTTagCompound)
  {
    super.b(par1NBTTagCompound);

    no nbttaglist = new no();

    for (int i = 0; i < this.ItemStacks.length; i++)
    {
      if (this.ItemStacks[i] != null)
      {
        ady nbttagcompound = new ady();
        nbttagcompound.a("SlotBrainStoneTrigger", (byte)i);
        this.ItemStacks[i].b(nbttagcompound);
        nbttaglist.a(nbttagcompound);
      }
    }

    par1NBTTagCompound.a("ItemsBrainStoneTrigger", nbttaglist);

    for (int i = 0; i < 5; i++)
    {
      par1NBTTagCompound.a(getType(i), this.mobTriggered[i]);
    }

    par1NBTTagCompound.a("BrainStoneDelay", this.delay);
  }
}