package brainstonemod.common.tileentity.template;

import brainstonemod.network.BrainStonePacketHelper;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

public abstract class TileEntityBrainStoneSyncBase extends TileEntity {
	@Override
	public void onDataPacket(NetworkManager net,
			S35PacketUpdateTileEntity packet) {
		readFromNBT(packet.func_148857_g());
	}

	@Override
	public void updateEntity() {
		if (worldObj.isRemote) {
			BrainStonePacketHelper.sendPacketToClosestPlayers(this,
					getDescriptionPacket());
		} else {
			BrainStonePacketHelper.sendPacketToServer(getDescriptionPacket());
		}
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);

		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord,
				this.zCoord, this.blockMetadata, nbt);
	}
}