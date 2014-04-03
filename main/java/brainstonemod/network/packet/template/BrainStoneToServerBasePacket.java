package brainstonemod.network.packet.template;

import brainstonemod.common.helper.BSP;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

public abstract class BrainStoneToServerBasePacket extends BrainStoneBasePacket {
	@Override
	public final void handleClientSide(EntityPlayer player) {
		BSP.throwException(new IllegalStateException(
				"The client should never handle this packet! (" + getClass().getName() + ")"));
	}
}