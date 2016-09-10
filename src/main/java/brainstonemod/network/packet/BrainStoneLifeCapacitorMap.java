package brainstonemod.network.packet;

import java.io.IOException;

import brainstonemod.BrainStone;
import brainstonemod.network.packet.template.BrainStoneToClientBasePacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;

public class BrainStoneLifeCapacitorMap extends BrainStoneToClientBasePacket {

	@Override
	public void generatePacketData(ChannelHandlerContext ctx, ByteBuf data) {
		try {
			final PacketBuffer buffer = new PacketBuffer(data);

			buffer.writeNBTTagCompoundToBuffer(
					BrainStone.brainStoneLifeCapacitor().getPlayerCapacitorMapping().getMap());
		} catch (IOException e) {
			throw new RuntimeException(
					"Unexpected Exception \"" + e.getClass().getName() + ": " + e.getMessage() + "\"", e);
		}
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		// Do nothing
	}

	@Override
	public void processPacketData(ChannelHandlerContext ctx, ByteBuf data) {
		try {
			final PacketBuffer buffer = new PacketBuffer(data);

			BrainStone.brainStoneLifeCapacitor().getPlayerCapacitorMapping()
					.setMap(buffer.readNBTTagCompoundFromBuffer());
		} catch (final IOException e) {
			throw new RuntimeException(
					"Unexpected Exception \"" + e.getClass().getName() + ": " + e.getMessage() + "\"", e);
		}
	}

}
