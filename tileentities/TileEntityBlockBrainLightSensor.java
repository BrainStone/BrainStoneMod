package mods.brainstone.tileentities;

import bs;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import mods.brainstone.handlers.BrainStonePacketHandler;
import mods.brainstone.templates.TileEntityBrainStoneSyncBase;

public class TileEntityBlockBrainLightSensor extends TileEntityBrainStoneSyncBase
{
  private int lightLevel;
  private boolean direction;
  private boolean powerOn;
  private int curLightLevel;
  public boolean GUIopen;

  public TileEntityBlockBrainLightSensor()
  {
    this.lightLevel = 8;
    this.direction = true;
    this.powerOn = false;
    this.GUIopen = false;
    this.curLightLevel = this.lightLevel;
  }

  protected void generateOutputStream(DataOutputStream outputStream)
    throws IOException
  {
    outputStream.writeInt(this.l);
    outputStream.writeInt(this.m);
    outputStream.writeInt(this.n);

    outputStream.writeInt(this.lightLevel);
    outputStream.writeBoolean(this.direction);
    outputStream.writeBoolean(this.powerOn);
    outputStream.writeInt(this.curLightLevel);
    outputStream.writeBoolean(this.GUIopen);
  }

  public int getCurLightLevel() {
    return this.curLightLevel;
  }

  public boolean getDirection() {
    return this.direction;
  }

  public int getLightLevel() {
    return this.lightLevel;
  }

  public boolean getPowerOn() {
    return this.powerOn;
  }

  public void readFromInputStream(DataInputStream inputStream)
    throws IOException
  {
    this.lightLevel = inputStream.readInt();
    this.direction = inputStream.readBoolean();
    this.powerOn = inputStream.readBoolean();
    this.curLightLevel = inputStream.readInt();
    this.GUIopen = inputStream.readBoolean();
  }

  public void a(bs nbttagcompound)
  {
    super.a(nbttagcompound);
    this.lightLevel = nbttagcompound.c("TEBBLS_lightLevel");
    this.direction = nbttagcompound.n("TEBBLS_direction");
  }

  public void setCurLightLevel(int i) {
    this.curLightLevel = i;
  }

  public void setDirection(boolean flag) {
    this.direction = flag;
  }

  public void setLightLevel(int i) {
    this.lightLevel = i;
  }

  public void setPowerOn(boolean flag) {
    this.powerOn = flag;
  }

  public void update(boolean sendToServer) throws IOException
  {
    ByteArrayOutputStream bos = new ByteArrayOutputStream(0);
    DataOutputStream outputStream = new DataOutputStream(bos);

    generateOutputStream(outputStream);

    if (sendToServer)
      BrainStonePacketHandler.sendPacketToServer("BSM.TEBBLSS", bos);
    else
      BrainStonePacketHandler.sendPacketToClosestPlayers(this, "BSM.TEBBLSC", bos);
  }

  public void b(bs nbttagcompound)
  {
    super.b(nbttagcompound);
    nbttagcompound.a("TEBBLS_lightLevel", (byte)this.lightLevel);
    nbttagcompound.a("TEBBLS_direction", this.direction);
  }
}