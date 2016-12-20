package brainstonemod.network.packet.serverbound;

import brainstonemod.network.BrainStonePacketHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketRequestOverrides implements IMessage {
	@Override
	public void fromBytes(ByteBuf buf) {
		// No data to send. Do nothing
	}

	@Override
	public void toBytes(ByteBuf buf) {
		// No data to send. Do nothing
	}

	public static class Handler extends AbstractServerMessageHandler<PacketRequestOverrides> {
		@Override
		public IMessage handleServerMessage(EntityPlayer player, PacketRequestOverrides message, MessageContext ctx) {
			BrainStonePacketHelper.sendServerOverridesPacket(player);

			return null;
		}
	}
}
