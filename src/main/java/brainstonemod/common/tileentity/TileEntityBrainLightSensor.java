package brainstonemod.common.tileentity;

import lombok.Getter;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityBrainLightSensor extends TileEntity {
  private boolean needsUpdate;
  @Getter private int lightLevel;
  private boolean classicDirection;
  private boolean simpleDirection;
  @Getter private boolean state;
  @Getter private boolean powerOn;
  @Getter private short power;

  public TileEntityBrainLightSensor() {
    needsUpdate = true;
    lightLevel = 8;
    classicDirection = true;
    simpleDirection = true;
    powerOn = false;
    state = false;
  }

  public void changeState() {
    needsUpdate = true;
    state = !state;
  }

  public boolean getNeedsUpdate() {
    if (needsUpdate) {
      needsUpdate = false;

      return true;
    } else return false;
  }

  public int getCurLightLevel() {
    if (world.isRemote) {
      world.calculateInitialSkylight();
    }

    return world.getLight(pos.up(), true);
  }

  public boolean getDirection() {
    return state ? classicDirection : simpleDirection;
  }

  /**
   * Determines in which state this light sensor is currently in.
   *
   * @return <tt>true</tt> when in Classsic Mode, <tt>false</tt> when in Simple Mode
   */
  public boolean isClassic() {
    return state;
  }

  /** Reads a tile entity from NBT. */
  @Override
  public void readFromNBT(NBTTagCompound nbttagcompound) {
    super.readFromNBT(nbttagcompound);
    state = nbttagcompound.getBoolean("TEBBLS_state");

    if (state) {
      lightLevel = nbttagcompound.getByte("TEBBLS_lightLevel");
      classicDirection = nbttagcompound.getBoolean("TEBBLS_direction");
    } else {
      simpleDirection = nbttagcompound.getBoolean("TEBBLS_direction");
    }
  }

  public void setLightLevel(int lightLevel) {
    if ((lightLevel >= 0) && (lightLevel <= 15)) {
      needsUpdate = needsUpdate || (this.lightLevel != lightLevel);
      this.lightLevel = lightLevel;
    }
  }

  public void setDirection(boolean direction) {
    if (state) {
      needsUpdate = needsUpdate || (classicDirection != direction);
      classicDirection = direction;
    } else {
      needsUpdate = needsUpdate || (simpleDirection != direction);
      simpleDirection = direction;
    }
  }

  public void setState(boolean state) {
    needsUpdate = needsUpdate || (this.state != state);
    this.state = state;
  }

  public void setPowerOn(boolean powerOn) {
    needsUpdate = needsUpdate || (this.powerOn != powerOn);
    this.powerOn = powerOn;
  }

  public void setPower(short power) {
    if ((power >= 0) && (power <= 15)) {
      needsUpdate = needsUpdate || (this.power != power);
      this.power = power;
    }
  }

  /** Writes a tile entity to NBT. */
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
    super.writeToNBT(nbttagcompound);
    nbttagcompound.setBoolean("TEBBLS_state", state);

    if (state) {
      nbttagcompound.setByte("TEBBLS_lightLevel", (byte) lightLevel);
      nbttagcompound.setBoolean("TEBBLS_direction", classicDirection);
    } else {
      nbttagcompound.setBoolean("TEBBLS_direction", simpleDirection);
    }
    return nbttagcompound;
  }

  @Override
  public SPacketUpdateTileEntity getUpdatePacket() {
    return new SPacketUpdateTileEntity(pos, getBlockMetadata(), getUpdateTag());
  }

  @Override
  public NBTTagCompound getUpdateTag() {
    return writeToNBT(new NBTTagCompound());
  }

  @Override
  public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
    readFromNBT(pkt.getNbtCompound());
  }
}
