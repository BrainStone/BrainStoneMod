package brainstonemod.network.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.lang.reflect.Field;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import brainstonemod.common.helper.BSP;
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
		// Extremely dirty hack.
		try {
			Field field = packet.getClass().getDeclaredField("field_148863_a");
			field.setAccessible(true);
			
			x = field.getInt(packet);
			
			field = packet.getClass().getDeclaredField("field_148861_b");
			field.setAccessible(true);
			
			y = field.getInt(packet);
			
			field = packet.getClass().getDeclaredField("field_148862_c");
			field.setAccessible(true);
			
			z = field.getInt(packet);
			
			field = packet.getClass().getDeclaredField("field_148860_e");
			field.setAccessible(true);
			
			nbt = (NBTTagCompound) field.get(packet);
		} catch (NoSuchFieldException e) {
			BSP.infoException(e);
		} catch (SecurityException e) {
			BSP.infoException(e);
		} catch (IllegalArgumentException e) {
			BSP.infoException(e);
		} catch (IllegalAccessException e) {
			BSP.infoException(e);
		}
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