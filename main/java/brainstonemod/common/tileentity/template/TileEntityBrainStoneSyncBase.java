package brainstonemod.common.tileentity.template;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import brainstonemod.BrainStone;
import brainstonemod.network.BrainStonePacketHelper;
import brainstonemod.network.BrainStonePacketPipeline;
import brainstonemod.network.packet.BrainStoneUpdateTileEntityPacket;
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
	private int lastUpdate = 0;
	@SideOnly(value = Side.CLIENT)
	private boolean sendToServer = false;

	@Override
	public void onDataPacket(NetworkManager net,
			S35PacketUpdateTileEntity packet) {
		readFromNBT(packet.func_148857_g());
	}

	@Override
	public void updateEntity() {
		if (lastUpdate-- <= 0) {
			lastUpdate = 20;

			if (!worldObj.isRemote) {
				BrainStonePacketHelper.sendPacketToClosestPlayers(this,
						getDescriptionPacket());
			} else if (sendToServer) {
				sendToServer = false;

				BrainStone.packetPipeline
						.sendToServer(new BrainStoneUpdateTileEntityPacket(
								(S35PacketUpdateTileEntity) getDescriptionPacket()));
			}
		}
	}
	
	public void allowClientToServerUpdate() {
		sendToServer = true;
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);

		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord,
				this.zCoord, 0, nbt);
	}
}