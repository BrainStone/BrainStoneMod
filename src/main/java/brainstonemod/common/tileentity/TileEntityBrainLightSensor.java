package brainstonemod.common.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import brainstonemod.common.tileentity.template.TileEntityBrainStoneSyncBase;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class TileEntityBrainLightSensor extends
		TileEntityBrainStoneSyncBase {
	private int lightLevel;
	private boolean classicDirection;
	private boolean simpleDirection;
	private boolean powerOn;
	private boolean classicState;
	private boolean state;
	private int curLightLevel;
	private short power;

	public TileEntityBrainLightSensor() {
		lightLevel = 8;
		classicDirection = true;
		simpleDirection = true;
		powerOn = false;
		curLightLevel = lightLevel;
		state = false;
	}

	public void changeState() {
		state = !state;
	}

	public int getCurLightLevel() {
		if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			worldObj.calculateInitialSkylight();
		}

		curLightLevel = worldObj.getBlockLightValue_do(xCoord, yCoord + 1,
				zCoord, false);

		return curLightLevel;
	}

	public boolean getDirection() {
		return state ? classicDirection : simpleDirection;
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
	public boolean isClassic() {
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
			classicDirection = nbttagcompound.getBoolean("TEBBLS_direction");
		} else {
			simpleDirection = nbttagcompound.getBoolean("TEBBLS_direction");
		}
	}

	public void setDirection(boolean direction) {
		if (state) {
			classicDirection = direction;
		} else {
			simpleDirection = direction;
		}
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
			nbttagcompound.setBoolean("TEBBLS_direction", classicDirection);
		} else {
			nbttagcompound.setBoolean("TEBBLS_direction", simpleDirection);
		}
	}
}
