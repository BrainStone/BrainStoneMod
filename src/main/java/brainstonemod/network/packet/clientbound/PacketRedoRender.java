package brainstonemod.network.packet.clientbound;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class PacketRedoRender implements IMessage {
	private int x, y, z;

	public PacketRedoRender() {
	}

	public PacketRedoRender(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		x=buf.readInt();
		y=buf.readShort();
		z=buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeShort(y);
		buf.writeInt(z);
	}

	public static class Handler extends AbstractClientMessageHandler<PacketRedoRender> {
		@Override
		public IMessage handleClientMessage(EntityPlayer player, PacketRedoRender message, MessageContext ctx) {
			player.worldObj.markBlockRangeForRenderUpdate(message.x, message.y, message.z, message.x, message.y, message.z);
			return null;
		}
	}
}
