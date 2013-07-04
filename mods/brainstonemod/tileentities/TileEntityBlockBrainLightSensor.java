package mods.brainstonemod.tileentities;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import mods.brainstonemod.handlers.BrainStonePacketHandler;
import mods.brainstonemod.templates.TileEntityBrainStoneSyncBase;
import net.minecraft.nbt.NBTTagCompound;

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

	@Override
	protected void generateOutputStream(DataOutputStream outputStream)
			throws IOException {
		outputStream.writeInt(xCoord);
		outputStream.writeInt(yCoord);
		outputStream.writeInt(zCoord);

		outputStream.writeBoolean(state);

		if (state) {
			outputStream.writeInt(lightLevel);
			outputStream.writeBoolean(direction);
			outputStream.writeBoolean(powerOn);
			outputStream.writeInt(curLightLevel);
			outputStream.writeBoolean(GUIopen);
		} else {
			outputStream.writeBoolean(direction);
			outputStream.writeShort(power);
		}
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

	@Override
	public void readFromInputStream(DataInputStream inputStream)
			throws IOException {
		if (state = inputStream.readBoolean()) {
			lightLevel = inputStream.readInt();
			direction = inputStream.readBoolean();
			powerOn = inputStream.readBoolean();
			curLightLevel = inputStream.readInt();
			GUIopen = inputStream.readBoolean();
		} else {
			direction = inputStream.readBoolean();
			power = inputStream.readShort();
		}
	}

	/**
	 * Reads a tile entity from NBT.
	 */
	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		if (state = nbttagcompound.getBoolean("TEBBLS_state")) {
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

	@Override
	public void update(boolean sendToServer) throws IOException {
		final ByteArrayOutputStream bos = new ByteArrayOutputStream(0);
		final DataOutputStream outputStream = new DataOutputStream(bos);

		this.generateOutputStream(outputStream);

		if (sendToServer) {
			BrainStonePacketHandler.sendPacketToServer("BSM.TEBBLSS", bos);
		} else {
			BrainStonePacketHandler.sendPacketToClosestPlayers(this,
					"BSM.TEBBLSC", bos);
		}
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
