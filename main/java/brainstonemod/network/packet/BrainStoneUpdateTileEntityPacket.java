package brainstonemod.network.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import brainstonemod.network.packet.template.BrainStoneToServerBasePacket;

public class BrainStoneUpdateTileEntityPacket extends
		BrainStoneToServerBasePacket {
	private int x;
	private int y;
	private int z;
	private NBTTagCompound nbt;

	public BrainStoneUpdateTileEntityPacket() {
	}

	public BrainStoneUpdateTileEntityPacket(S35PacketUpdateTileEntity packet) {
		x = packet.func_148856_c();
		y = packet.func_148855_d();
		z = packet.func_148854_e();
		nbt = packet.func_148857_g();
	}

	@Override
	public void generatePacketData(ChannelHandlerContext ctx, ByteBuf data) {
		try {
			final PacketBuffer buffer = new PacketBuffer(data);

			buffer.writeInt(x);
			buffer.writeShort(y);
			buffer.writeInt(z);
			buffer.writeNBTTagCompoundToBuffer(nbt);
		} catch (final IOException e) {
			throw new RuntimeException("Unexpected Exception \""
					+ e.getClass().getName() + ": " + e.getMessage() + "\"", e);
		}
	}

	@Override
	public void handleServerSide(EntityPlayer player) {
		player.worldObj.getTileEntity(x, y, z).onDataPacket(null,
				new S35PacketUpdateTileEntity(x, y, z, 0, nbt));
	}

	@Override
	public void processPacketData(ChannelHandlerContext ctx, ByteBuf data) {
		try {
			final PacketBuffer buffer = new PacketBuffer(data);

			x = buffer.readInt();
			y = buffer.readShort();
			z = buffer.readInt();
			nbt = buffer.readNBTTagCompoundFromBuffer();
		} catch (final IOException e) {
			throw new RuntimeException("Unexpected Exception \""
					+ e.getClass().getName() + ": " + e.getMessage() + "\"", e);
		}
	}
}