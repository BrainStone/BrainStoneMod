package mods.brainstone.templates;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.tileentity.TileEntity;

public abstract class TileEntityBrainStoneSyncBase extends TileEntity {
	protected abstract void generateOutputStream(DataOutputStream outputStream)
			throws IOException;

	public abstract void readFromInputStream(DataInputStream inputStream)
			throws IOException;

	public abstract void update(boolean sendToServer) throws IOException;
}