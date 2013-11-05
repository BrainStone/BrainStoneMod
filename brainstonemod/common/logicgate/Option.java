package brainstonemod.common.logicgate;

import net.minecraft.nbt.NBTTagCompound;

public abstract class Option {
	public Object[] Data;
	public boolean Active;

	public abstract void draw(int x, int y);

	public abstract boolean onClick(int x, int y, int mouseButton);

	public abstract void readFromNBT(NBTTagCompound nbttagcompound);

	public abstract void saveToNBT(NBTTagCompound nbttagcompound);
}
