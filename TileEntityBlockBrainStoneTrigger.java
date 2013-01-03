package brainstone;

import amq;
import bq;
import by;
import ce;
import ef;
import fg;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.Set;
import la;
import px;
import ur;
import yc;
import ym;

public class TileEntityBlockBrainStoneTrigger extends TileEntityBlockBrainStoneHiders
  implements la
{
  public static LinkedHashMap triggerEntities;
  public boolean power;
  private HashMap mobTriggered;
  private static final int mobTriggeredSize = 5;
  public boolean forceUpdate;
  public byte delay;
  public byte max_delay;

  public TileEntityBlockBrainStoneTrigger()
  {
    this.ItemStacks = new ur[1];
    this.mobTriggered = new HashMap();
    this.power = false;
    this.forceUpdate = false;
    this.delay = 0;
    this.max_delay = 4;

    int length = triggerEntities.size();
    String[] keys = (String[])triggerEntities.keySet().toArray(new String[length]);

    for (int i = 0; i < length; i++)
    {
      this.mobTriggered.put(keys[i], Boolean.valueOf(true));
    }
  }

  public String b()
  {
    return "container.brainstonetrigger";
  }

  public ef l()
  {
    bq tag = new bq();
    b(tag);
    return new fg(this.l, this.m, this.n, 1, tag);
  }

  public void onDataPacket(ce net, fg packet)
  {
    bq tag = packet.e;
    a(tag);
  }

  public int getTextureId(ym iblockaccess, int i, int j, int k)
  {
    ur itemstack = this.ItemStacks[0];

    if (itemstack == null)
    {
      return -1;
    }

    int l = itemstack.c;

    if ((l > amq.p.length) || (l <= 0))
    {
      return -1;
    }

    amq block = amq.p[l];

    if (block == null)
    {
      return -1;
    }

    return block.d(iblockaccess, i, j, k, 1);
  }

  public String getTextureFile()
  {
    ur itemstack = this.ItemStacks[0];

    if (itemstack == null)
    {
      return "/brainstone/BrainStoneTextures/textures.png";
    }

    int l = itemstack.c;

    if ((l > amq.p.length) || (l <= 0))
    {
      return "/brainstone/BrainStoneTextures/textures.png";
    }

    amq block = amq.p[l];

    if (block == null)
    {
      return "/brainstone/BrainStoneTextures/textures.png";
    }

    return block.getTextureFile();
  }

  public boolean getMobTriggered(String s)
  {
    if (this.mobTriggered.containsKey(s))
    {
      return ((Boolean)this.mobTriggered.get(s)).booleanValue();
    }

    return false;
  }

  public void setMobTriggered(String s, boolean flag)
  {
    if (this.mobTriggered.containsKey(s))
    {
      this.mobTriggered.put(s, Boolean.valueOf(flag));
    }
  }

  public void invertMobTriggered(String s)
  {
    if (this.mobTriggered.containsKey(s))
    {
      this.mobTriggered.put(s, Boolean.valueOf(!((Boolean)this.mobTriggered.get(s)).booleanValue()));
    }
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
    by nbttaglist = nbttagcompound.m("ItemsBrainStoneTrigger");
    this.ItemStacks = new ur[k_()];

    for (int i = 0; i < nbttaglist.c(); i++)
    {
      bq nbttagcompound1 = (bq)nbttaglist.b(i);
      byte byte0 = nbttagcompound1.c("SlotBrainStoneTrigger");

      if ((byte0 >= 0) && (byte0 < this.ItemStacks.length))
      {
        this.ItemStacks[byte0] = ur.a(nbttagcompound1);
      }
    }

    int length = nbttagcompound.e("TriggerSize");

    for (int i = 0; i < length; i++)
    {
      String trigger = "Trigger" + String.valueOf(i);

      this.mobTriggered.put(nbttagcompound.i(trigger + "Key"), Boolean.valueOf(nbttagcompound.n(trigger)));
    }

    this.delay = nbttagcompound.c("BrainStoneDelay");
    this.max_delay = nbttagcompound.c("BrainStoneMaxDelay");
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

    int length = triggerEntities.size();
    nbttagcompound.a("TriggerSize", length);
    String[] keys = (String[])triggerEntities.keySet().toArray(new String[length]);

    for (int i = 0; i < length; i++)
    {
      String key = keys[i];
      String trigger = "Trigger" + String.valueOf(i);

      nbttagcompound.a(trigger + "Key", key);
      nbttagcompound.a(trigger, ((Boolean)this.mobTriggered.get(key)).booleanValue());
    }

    nbttagcompound.a("BrainStoneDelay", this.delay);
    nbttagcompound.a("BrainStoneMaxDelay", this.max_delay);
  }

  public void readFromInputStream(DataInputStream inputStream)
    throws IOException
  {
    this.power = inputStream.readBoolean();
    this.forceUpdate = inputStream.readBoolean();
    this.delay = inputStream.readByte();
    this.max_delay = inputStream.readByte();

    int length = inputStream.readInt();

    for (int i = 0; i < length; i++)
    {
      this.mobTriggered.put(inputStream.readUTF(), Boolean.valueOf(inputStream.readBoolean()));
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
    outputStream.writeByte(this.max_delay);

    int length = triggerEntities.size();
    outputStream.writeInt(length);
    String[] keys = (String[])triggerEntities.keySet().toArray(new String[length]);

    for (int i = 0; i < length; i++)
    {
      String key = keys[i];
      String trigger = "Trigger" + String.valueOf(i);

      outputStream.writeUTF(key);
      outputStream.writeBoolean(((Boolean)this.mobTriggered.get(key)).booleanValue());
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