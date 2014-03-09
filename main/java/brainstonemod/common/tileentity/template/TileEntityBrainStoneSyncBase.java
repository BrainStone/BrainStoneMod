package brainstonemod.common.tileentity.template;

import java.lang.reflect.Field;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import brainstonemod.common.helper.BSP;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public abstract class TileEntityBrainStoneSyncBase extends TileEntity {
	protected boolean inventorySaving = true;

	@Override
	public boolean canUpdate() {
		return false;
	}

	public void disableInventorySaving() {
		inventorySaving = false;
	}

	public void enableInventorySaving() {
		inventorySaving = true;
	}

	@Override
	public Packet getDescriptionPacket() {
		final NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		enableInventorySaving();

		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net,
			S35PacketUpdateTileEntity packet) {
		NBTTagCompound nbt = null;

		try {
			nbt = packet.func_148857_g();
		} catch (final NoSuchMethodError e) {
			for (final Field field : S35PacketUpdateTileEntity.class
					.getDeclaredFields()) {
				if (field.getType().equals(NBTTagCompound.class)) {
					final boolean accesible = field.isAccessible();
					field.setAccessible(true);

					try {
						nbt = (NBTTagCompound) field.get(packet);
					} catch (final IllegalArgumentException ex) {
						BSP.fatalException(ex,
								"I have no idea what went wrong, but this should not have happened!");
					} catch (final IllegalAccessException ex) {
						BSP.fatalException(ex,
								"I have no idea what went wrong, but this should not have happened!");
					}

					field.setAccessible(accesible);

					break;
				}
			}
		}

		readFromNBT(nbt);

		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			disableInventorySaving();

			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
	}
}