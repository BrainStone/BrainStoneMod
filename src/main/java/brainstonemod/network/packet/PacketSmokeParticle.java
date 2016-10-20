package brainstonemod.network.packet;

import brainstonemod.common.block.BlockBrainLightSensor;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Random;

public class PacketSmokeParticle implements IMessage {
	private int x, y, z;

	private final String separator = "n";

	public PacketSmokeParticle() {
	}

	public PacketSmokeParticle(int x, int y, int z) {
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

	public static class Handler extends AbstractClientMessageHandler<PacketSmokeParticle> {
		@Override
		public IMessage handleClientMessage(EntityPlayer player, PacketSmokeParticle message, MessageContext ctx) {
			((BlockBrainLightSensor) player.worldObj.getBlock(message.x, message.y, message.z)).smoke(player.worldObj, message.x, message.y, message.z, new Random());
			return null;
		}
	}
}