package brainstonemod.common.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import brainstonemod.common.tileentity.template.TileEntityBrainStoneSyncBase;

public class TileEntityBlockBrainLightSensor extends
		TileEntityBrainStoneSyncBase {
	private int lightLevel;
	private boolean direction;
	private boolean powerOn;
	private boolean state;
	private int curLightLevel;
	private short power;

	public TileEntityBlockBrainLightSensor() {
		lightLevel = 8;
		direction = true;
		powerOn = false;
		curLightLevel = lightLevel;
		state = false;
	}

	public void changeState() {
		state = !state;
	}

	public int getCurLightLevel() {
		curLightLevel = worldObj.getBlockLightValue(xCoord, yCoord + 1, zCoord);

		return curLightLevel;
	}

	public boolean getDirection() {
		return direction;
	}

	public int getLightLevel() {
		return lightLevel;
	}

	public int getOldCurLightLevel() {
		return curLightLevel;
	}

	public short getPower() {
		return power;
	}

	public boolean getPowerOn() {
		return powerOn;
	}

	/**
	 * Determines in which state this light sensor is currently in.
	 * 
	 * @return <tt>true</tt> when in Classsic Mode, <tt>false</tt> when in
	 *         Simple Mode
	 */
	public boolean getState() {
		return state;
	}

	/**
	 * Reads a tile entity from NBT.
	 */
	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		state = nbttagcompound.getBoolean("TEBBLS_state");

		if (state) {
			lightLevel = nbttagcompound.getByte("TEBBLS_lightLevel");
			direction = nbttagcompound.getBoolean("TEBBLS_direction");
		} else {
			direction = nbttagcompound.getBoolean("TEBBLS_direction");
		}
	}

	public void setDirection(boolean direction) {
		this.direction = direction;
	}

	public void setLightLevel(int lightLevel) {
		this.lightLevel = lightLevel;
	}

	public void setPower(short Power) {
		if ((Power >= 0) && (Power <= 15)) {
			power = Power;
		}
	}

	public void setPowerOn(boolean power) {
		powerOn = power;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	/**
	 * Writes a tile entity to NBT.
	 */
	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setBoolean("TEBBLS_state", state);

		if (state) {
			nbttagcompound.setByte("TEBBLS_lightLevel", (byte) lightLevel);
			nbttagcompound.setBoolean("TEBBLS_direction", direction);
			nbttagcompound.setBoolean("TEBBLS_state", state);
		} else {
			nbttagcompound.setBoolean("TEBBLS_direction", direction);
		}
	}
}
