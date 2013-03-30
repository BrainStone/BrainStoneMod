package mods.brainstone.tileentities;

import aab;
import aak;
import apa;
import bs;
import ca;
import cg;
import ei;
import fn;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.Set;
import lt;
import lx;
import mods.brainstone.handlers.BrainStonePacketHandler;
import mods.brainstone.slots.SlotBlockBrainStoneTrigger;
import rh;
import wm;

public class TileEntityBlockBrainStoneTrigger extends TileEntityBlockBrainStoneHiders
  implements lt
{
  public static LinkedHashMap triggerEntities;
  public boolean power;
  private final HashMap mobTriggered;
  public byte delay;
  public byte max_delay;
  private wm oldStack;

  public TileEntityBlockBrainStoneTrigger()
  {
    this.ItemStacks = new wm[1];
    this.mobTriggered = new HashMap();
    this.power = false;
    this.delay = 0;
    this.max_delay = 4;

    int length = triggerEntities.size();
    String[] keys = (String[])triggerEntities.keySet().toArray(new String[length]);

    for (int i = 0; i < length; i++)
      this.mobTriggered.put(keys[i], Boolean.valueOf(true));
  }

  public boolean checkForSlotChange()
  {
    return this.oldStack != (this.oldStack = a(0));
  }

  public void dropItems(aab world, int i, int j, int k)
  {
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

  protected void generateOutputStream(DataOutputStream outputStream)
    throws IOException
  {
    outputStream.writeInt(this.l);
    outputStream.writeInt(this.m);
    outputStream.writeInt(this.n);
    outputStream.writeInt(this.oldStack == null ? 0 : this.oldStack.c);

    outputStream.writeBoolean(this.power);
    outputStream.writeByte(this.delay);
    outputStream.writeByte(this.max_delay);

    int length = triggerEntities.size();
    outputStream.writeInt(length);
    String[] keys = (String[])triggerEntities.keySet().toArray(new String[length]);

    for (int i = 0; i < length; i++) {
      String key = keys[i];

      outputStream.writeUTF(key);
      outputStream.writeBoolean(((Boolean)this.mobTriggered.get(key)).booleanValue());
    }
  }

  public ei m()
  {
    bs tag = new bs();
    b(tag);
    return new fn(this.l, this.m, this.n, 1, tag);
  }

  public String b()
  {
    return "container.brainstonetrigger";
  }

  public boolean getMobTriggered(String s) {
    if (this.mobTriggered.containsKey(s)) {
      return ((Boolean)this.mobTriggered.get(s)).booleanValue();
    }
    return false;
  }

  public lx getTextureId(aak iblockaccess, int i, int j, int k) {
    wm itemstack = this.ItemStacks[0];

    if (itemstack == null) {
      return mods.brainstone.blocks.BlockBrainStoneTrigger.textures[0];
    }
    int l = itemstack.c;

    if ((l > apa.r.length) || (l <= 0)) {
      return mods.brainstone.blocks.BlockBrainStoneTrigger.textures[0];
    }
    apa block = apa.r[l];

    if (block == null) {
      return mods.brainstone.blocks.BlockBrainStoneTrigger.textures[0];
    }
    return block.b_(iblockaccess, i, j, k, 1);
  }

  public void invertMobTriggered(String s) {
    if (this.mobTriggered.containsKey(s))
      this.mobTriggered.put(s, Boolean.valueOf(!((Boolean)this.mobTriggered.get(s)).booleanValue()));
  }

  public boolean c()
  {
    return false;
  }

  public boolean b(int i, wm itemstack)
  {
    return SlotBlockBrainStoneTrigger.staticIsItemValid(itemstack);
  }

  public void onDataPacket(cg net, fn packet)
  {
    bs tag = packet.e;
    a(tag);
  }

  public void readFromInputStream(DataInputStream inputStream)
    throws IOException
  {
    this.oldStack = new wm(inputStream.readInt(), 1, 0);

    this.power = inputStream.readBoolean();
    this.delay = inputStream.readByte();
    this.max_delay = inputStream.readByte();

    int length = inputStream.readInt();

    for (int i = 0; i < length; i++)
      this.mobTriggered.put(inputStream.readUTF(), Boolean.valueOf(inputStream.readBoolean()));
  }

  public void a(bs nbttagcompound)
  {
    super.a(nbttagcompound);
    ca nbttaglist = nbttagcompound.m("ItemsBrainStoneTrigger");

    this.ItemStacks = new wm[j_()];

    for (int i = 0; i < nbttaglist.c(); i++) {
      bs nbttagcompound1 = (bs)nbttaglist.b(i);

      byte byte0 = nbttagcompound1.c("SlotBrainStoneTrigger");

      if ((byte0 >= 0) && (byte0 < this.ItemStacks.length)) {
        this.ItemStacks[byte0] = wm.a(nbttagcompound1);
      }

    }

    int length = nbttagcompound.e("TriggerSize");

    for (int i = 0; i < length; i++) {
      String trigger = "Trigger" + String.valueOf(i);

      this.mobTriggered.put(nbttagcompound.i(trigger + "Key"), Boolean.valueOf(nbttagcompound.n(trigger)));
    }

    this.delay = nbttagcompound.c("BrainStoneDelay");
    this.max_delay = nbttagcompound.c("BrainStoneMaxDelay");
  }

  public void setMobTriggered(String s, boolean flag) {
    if (this.mobTriggered.containsKey(s))
      this.mobTriggered.put(s, Boolean.valueOf(flag));
  }

  public void update(boolean sendToServer)
    throws IOException
  {
    ByteArrayOutputStream bos = new ByteArrayOutputStream(0);
    DataOutputStream outputStream = new DataOutputStream(bos);

    generateOutputStream(outputStream);

    if (sendToServer)
      BrainStonePacketHandler.sendPacketToServer("BSM.TEBBSTS", bos);
    else
      BrainStonePacketHandler.sendPacketToClosestPlayers(this, "BSM.TEBBSTC", bos);
  }

  public void b(bs nbttagcompound)
  {
    super.b(nbttagcompound);
    ca nbttaglist = new ca();

    for (int i = 0; i < this.ItemStacks.length; i++) {
      if (this.ItemStacks[i] != null) {
        bs nbttagcompound1 = new bs();
        nbttagcompound1.a("SlotBrainStoneTrigger", (byte)i);
        this.ItemStacks[i].b(nbttagcompound1);
        nbttaglist.a(nbttagcompound1);
      }
    }

    nbttagcompound.a("ItemsBrainStoneTrigger", nbttaglist);

    int length = triggerEntities.size();
    nbttagcompound.a("TriggerSize", length);
    String[] keys = (String[])triggerEntities.keySet().toArray(new String[length]);

    for (int i = 0; i < length; i++) {
      String key = keys[i];
      String trigger = "Trigger" + String.valueOf(i);

      nbttagcompound.a(trigger + "Key", key);
      nbttagcompound.a(trigger, ((Boolean)this.mobTriggered.get(key)).booleanValue());
    }

    nbttagcompound.a("BrainStoneDelay", this.delay);
    nbttagcompound.a("BrainStoneMaxDelay", this.max_delay);
  }
}