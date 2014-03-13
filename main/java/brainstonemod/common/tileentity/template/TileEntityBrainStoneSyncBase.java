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

	@Override
	public Packet getDescriptionPacket() {
		return getDescriptionPacket(true);
	}

	// DOCME
	public Packet getDescriptionPacket(boolean enableInventorySaving) {
		try {
			inventorySaving = enableInventorySaving;
			final NBTTagCompound nbt = new NBTTagCompound();
			nbt.setBoolean("inventorySaving", inventorySaving);

			writeToNBT(nbt);

			return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, nbt);
		} finally {
			// Just making sure inventorySaving is reset!
			inventorySaving = true;
		}
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
					final boolean accessible = field.isAccessible();
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

					field.setAccessible(accessible);

					break;
				}
			}
		}

		try {
			inventorySaving = nbt.getBoolean("inventorySaving");
			readFromNBT(nbt);
		} finally {
			// Just making sure inventorySaving is reset!
			inventorySaving = true;
		}

		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}

		markDirty();
	}
}