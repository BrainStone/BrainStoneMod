package mods.brainstone.tileentities;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import mods.brainstone.handlers.BrainStonePacketHandler;
import mods.brainstone.templates.TileEntityBrainStoneSyncBase;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityBlockBrainLightSensor extends
		TileEntityBrainStoneSyncBase {
	private int lightLevel;
	private boolean direction;
	private boolean powerOn;
	private int curLightLevel;
	public boolean GUIopen;

	public TileEntityBlockBrainLightSensor() {
		lightLevel = 8;
		direction = true;
		powerOn = false;
		GUIopen = false;
		curLightLevel = lightLevel;
	}

	@Override
	protected void generateOutputStream(DataOutputStream outputStream)
			throws IOException {
		outputStream.writeInt(xCoord);
		outputStream.writeInt(yCoord);
		outputStream.writeInt(zCoord);

		outputStream.writeInt(lightLevel);
		outputStream.writeBoolean(direction);
		outputStream.writeBoolean(powerOn);
		outputStream.writeInt(curLightLevel);
		outputStream.writeBoolean(GUIopen);
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

	public boolean getPowerOn() {
		return powerOn;
	}

	@Override
	public void readFromInputStream(DataInputStream inputStream)
			throws IOException {
		lightLevel = inputStream.readInt();
		direction = inputStream.readBoolean();
		powerOn = inputStream.readBoolean();
		curLightLevel = inputStream.readInt();
		GUIopen = inputStream.readBoolean();
	}

	/**
	 * Reads a tile entity from NBT.
	 */
	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		lightLevel = nbttagcompound.getByte("TEBBLS_lightLevel");
		direction = nbttagcompound.getBoolean("TEBBLS_direction");
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

	public void setPowerOn(boolean flag) {
		powerOn = flag;
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
		nbttagcompound.setByte("TEBBLS_lightLevel", (byte) lightLevel);
		nbttagcompound.setBoolean("TEBBLS_direction", direction);
	}
}
