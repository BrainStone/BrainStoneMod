package brainstonemod.network.packet.clientbound;

import java.util.Random;

import brainstonemod.common.block.BlockBrainLightSensor;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSmokeParticle implements IMessage {
	private int x, y, z;

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
			((BlockBrainLightSensor) player.worldObj.getBlockState(new BlockPos(message.x, message.y, message.z)).getBlock()).smoke(player.worldObj, message.x, message.y, message.z, new Random());
			return null;
		}
	}
}