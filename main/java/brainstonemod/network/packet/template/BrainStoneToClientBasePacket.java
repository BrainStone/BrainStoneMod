package brainstonemod.network.packet.template;

import brainstonemod.common.helper.BSP;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

public abstract class BrainStoneToClientBasePacket extends BrainStoneBasePacket {
	@Override
	public final void handleServerSide(EntityPlayer player) {
		BSP.throwException(new IllegalStateException(
				"The server should never handle this packet! (" + getClass().getName() + ")"));
	}
}