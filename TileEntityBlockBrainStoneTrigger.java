package net.braintonemod.src;

import amj;
import bq;
import by;
import ce;
import eg;
import fh;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;
import la;
import px;
import um;
import xv;
import yf;

public class TileEntityBlockBrainStoneTrigger extends TileEntityBlockBrainStoneHiders
  implements la
{
  public boolean power;
  private boolean[] mobTriggered;
  private static final int mobTriggeredSize = 5;
  public boolean forceUpdate;
  public byte delay;

  public TileEntityBlockBrainStoneTrigger()
  {
    this.ItemStacks = new um[1];
    this.mobTriggered = new boolean[5];
    this.power = false;
    this.forceUpdate = false;
    this.delay = 0;

    for (int i = 0; i < 5; i++)
    {
      this.mobTriggered[i] = true;
    }
  }

  public String b()
  {
    return "container.brainstonetrigger";
  }

  public eg l()
  {
    bq tag = new bq();
    b(tag);
    return new fh(this.l, this.m, this.n, 1, tag);
  }

  public void onDataPacket(ce net, fh packet)
  {
    bq tag = packet.e;
    a(tag);
  }

  public int getTextureId(yf iblockaccess, int i, int j, int k)
  {
    um itemstack = this.ItemStacks[0];

    if (itemstack == null)
    {
      return -1;
    }

    int l = itemstack.c;

    if ((l > amj.p.length) || (l <= 0))
    {
      return -1;
    }

    amj block = amj.p[l];

    if (block == null)
    {
      return -1;
    }

    return block.d(iblockaccess, i, j, k, 1);
  }

  public String getTextureFile()
  {
    um itemstack = this.ItemStacks[0];

    if (itemstack == null)
    {
      return "";
    }

    int l = itemstack.c;

    if ((l > amj.p.length) || (l <= 0))
    {
      return "";
    }

    amj block = amj.p[l];

    if (block == null)
    {
      return "";
    }

    return block.getTextureFile();
  }

  public int getIndex(String s)
  {
    if (s == "item")
    {
      return 0;
    }

    if (s == "animal")
    {
      return 1;
    }

    if (s == "monster")
    {
      return 2;
    }

    if (s == "nether")
    {
      return 3;
    }

    return s != "player" ? -1 : 4;
  }

  public String getType(int i)
  {
    if (i == 0)
    {
      return "item";
    }

    if (i == 1)
    {
      return "animal";
    }

    if (i == 2)
    {
      return "monster";
    }

    if (i == 3)
    {
      return "nether";
    }

    if (i == 4)
    {
      return "player";
    }

    return "";
  }

  public boolean getMobTriggered(String s)
  {
    return getMobTriggered(getIndex(s));
  }

  public boolean getMobTriggered(int i)
  {
    if ((i >= 5) || (i < 0))
    {
      return false;
    }

    return this.mobTriggered[i];
  }

  public void setMobTriggered(String s, boolean flag)
  {
    setMobTriggered(getIndex(s), flag);
  }

  public void setMobTriggered(int i, boolean flag)
  {
    if ((i >= 5) || (i < 0))
    {
      return;
    }

    this.mobTriggered[i] = flag;
  }

  public void invertMobTriggered(int i)
  {
    if ((i >= 5) || (i < 0))
    {
      return;
    }

    this.mobTriggered[i] = (this.mobTriggered[i] == 0 ? 1 : false);
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
    by nbttaglist = nbttagcompound.m("ItemsBrainStoneTrigger");
    this.ItemStacks = new um[k_()];

    for (int i = 0; i < nbttaglist.c(); i++)
    {
      bq nbttagcompound1 = (bq)nbttaglist.b(i);
      byte byte0 = nbttagcompound1.c("SlotBrainStoneTrigger");

      if ((byte0 >= 0) && (byte0 < this.ItemStacks.length))
      {
        this.ItemStacks[byte0] = um.a(nbttagcompound1);
      }
    }

    for (int j = 0; j < 5; j++)
    {
      this.mobTriggered[j] = nbttagcompound.n(getType(j));
    }

    this.delay = nbttagcompound.c("BrainStoneDelay");
  }

  public void b(bq nbttagcompound)
  {
    super.b(nbttagcompound);
    by nbttaglist = new by();

    for (int i = 0; i < this.ItemStacks.length; i++)
    {
      if (this.ItemStacks[i] != null)
      {
        bq nbttagcompound1 = new bq();
        nbttagcompound1.a("SlotBrainStoneTrigger", (byte)i);
        this.ItemStacks[i].b(nbttagcompound1);
        nbttaglist.a(nbttagcompound1);
      }
    }

    nbttagcompound.a("ItemsBrainStoneTrigger", nbttaglist);

    for (int j = 0; j < 5; j++)
    {
      nbttagcompound.a(getType(j), this.mobTriggered[j]);
    }

    nbttagcompound.a("BrainStoneDelay", this.delay);
  }

  public void readFromInputStream(DataInputStream inputStream)
    throws IOException
  {
    this.power = inputStream.readBoolean();
    this.forceUpdate = inputStream.readBoolean();
    this.delay = inputStream.readByte();

    for (int i = 0; i < 5; i++)
    {
      this.mobTriggered[i] = inputStream.readBoolean();
    }
  }

  protected void generateOutputStream(DataOutputStream outputStream)
    throws IOException
  {
    outputStream.writeInt(this.l);
    outputStream.writeInt(this.m);
    outputStream.writeInt(this.n);

    outputStream.writeBoolean(this.power);
    outputStream.writeBoolean(this.forceUpdate);
    outputStream.writeByte(this.delay);

    for (int i = 0; i < 5; i++)
    {
      outputStream.writeBoolean(this.mobTriggered[i]);
    }
  }

  public void update(boolean sendToServer)
    throws IOException
  {
    ByteArrayOutputStream bos = new ByteArrayOutputStream(0);
    DataOutputStream outputStream = new DataOutputStream(bos);

    generateOutputStream(outputStream);

    if (sendToServer)
    {
      BrainStonePacketHandler.sendPacketToServer("BSM.TEBBSTS", bos);
    }
    else
    {
      BrainStonePacketHandler.sendPacketToClosestPlayers(this, "BSM.TEBBSTC", bos);
    }
  }
}