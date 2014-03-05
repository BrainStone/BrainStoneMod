package brainstonemod.common.tileentity;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.nbt.NBTTagCompound;
import brainstonemod.common.tileentity.template.TileEntityBrainStoneSyncBase;
import brainstonemod.network.BrainStonePacketHelper;

public class TileEntityBlockBrainLightSensor extends
		TileEntityBrainStoneSyncBase {
	private int lightLevel;
	private boolean direction;
	private boolean powerOn;
	private boolean state;
	private int curLightLevel;
	public boolean GUIopen;
	private short power;

	public TileEntityBlockBrainLightSensor() {
		lightLevel = 8;
		direction = true;
		powerOn = false;
		GUIopen = false;
		curLightLevel = lightLevel;
		state = false;
	}

	public void changeState() {
		state = !state;
	}

	public int getCurLightLevel() {
		return curLightLevel;
	}

	public boolean getDirection() {
		return direction;
	}

	public int getLightLevel() {
		return lightLevel;
	}

	public short getPower() {
		return power;
	}

	public boolean getPowerOn() {
		return powerOn;
	}

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

	public void setCurLightLevel(int i) {
		curLightLevel = i;
	}

	public void setDirection(boolean flag) {
		direction = flag;
	}

	public void setLightLevel(int i) {
		lightLevel = i;
	}

	public void setPower(short Power) {
		if ((Power >= 0) && (Power <= 15)) {
			power = Power;
		}
	}

	public void setPowerOn(boolean flag) {
		powerOn = flag;
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
