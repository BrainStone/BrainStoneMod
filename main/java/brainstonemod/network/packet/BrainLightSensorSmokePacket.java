package brainstonemod.network.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import brainstonemod.common.block.BlockBrainLightSensor;
import brainstonemod.common.helper.BSP;
import brainstonemod.network.packet.template.BrainStoneBasePacket;

public class BrainLightSensorSmokePacket extends BrainStoneBasePacket {
	private int x, y, z;

	public BrainLightSensorSmokePacket() {
	}

	public BrainLightSensorSmokePacket(int x, int y, int z) {
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
		((BlockBrainLightSensor) player.worldObj.getBlock(x, y, z)).smoke(
				player.worldObj, x, y, z, new Random());
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