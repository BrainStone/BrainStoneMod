package brainstonemod.network.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import brainstonemod.common.helper.BSP;
import brainstonemod.network.packet.template.BrainStoneBasePacket;

public class BrainStoneReRenderBlockAtPacket extends BrainStoneBasePacket {
	private int x, y, z;

	public BrainStoneReRenderBlockAtPacket() {
	}

	public BrainStoneReRenderBlockAtPacket(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void generatePacketData(ChannelHandlerContext ctx, ByteBuf buffer) {
		buffer.writeInt(x);
		buffer.writeShort(y);
		buffer.writeInt(z);
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		player.worldObj.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
	}

	@Override
	public void handleServerSide(EntityPlayer player) {
		BSP.throwException(new IllegalStateException(
				"The server should never handle this packet!"));
	}

	@Override
	public void processPacketData(ChannelHandlerContext ctx, ByteBuf buffer) {
		x = buffer.readInt();
		y = buffer.readShort();
		z = buffer.readInt();
	}
}
