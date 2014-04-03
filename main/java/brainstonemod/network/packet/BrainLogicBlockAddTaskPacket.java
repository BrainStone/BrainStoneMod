package brainstonemod.network.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import brainstonemod.common.tileentity.TileEntityBlockBrainLogicBlock;
import brainstonemod.network.packet.template.BrainStoneToServerBasePacket;

public class BrainLogicBlockAddTaskPacket extends BrainStoneToServerBasePacket {
	private int x, y, z;
	private String task;

	public BrainLogicBlockAddTaskPacket(
			TileEntityBlockBrainLogicBlock tileEntity, String task) {
		x = tileEntity.xCoord;
		y = tileEntity.yCoord;
		z = tileEntity.zCoord;
		this.task = task;
	}

	public BrainLogicBlockAddTaskPacket() {
	}

	@Override
	public void generatePacketData(ChannelHandlerContext ctx, ByteBuf data) {
		try {
			final PacketBuffer buffer = new PacketBuffer(data);

			buffer.writeInt(x);
			buffer.writeShort(y);
			buffer.writeInt(z);
			buffer.writeStringToBuffer(task);
		} catch (IOException e) {
			throw new RuntimeException("Unexpected Exception \""
					+ e.getClass().getName() + ": " + e.getMessage() + "\"", e);
		}
	}

	@Override
	public void handleServerSide(EntityPlayer player) {
		((TileEntityBlockBrainLogicBlock) player.worldObj
				.getTileEntity(x, y, z)).addTASK(task);
	}

	@Override
	public void processPacketData(ChannelHandlerContext ctx, ByteBuf data) {
		try {
			final PacketBuffer buffer = new PacketBuffer(data);

			x = buffer.readInt();
			y = buffer.readShort();
			z = buffer.readInt();
			task = buffer.readStringFromBuffer(0xffff);
		} catch (final IOException e) {
			throw new RuntimeException("Unexpected Exception \""
					+ e.getClass().getName() + ": " + e.getMessage() + "\"", e);
		}
	}
}