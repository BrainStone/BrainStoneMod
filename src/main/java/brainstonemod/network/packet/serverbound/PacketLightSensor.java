package brainstonemod.network.packet.serverbound;

import brainstonemod.common.helper.BSP;
import brainstonemod.common.tileentity.TileEntityBrainLightSensor;
import brainstonemod.network.packet.clientbound.PacketSyncLightSensor;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketLightSensor implements IMessage {
	private int x;
	private short y;
	private int z;

	private int lightlevel;
	private boolean direction;

	public PacketLightSensor() {
	}

	public PacketLightSensor(TileEntity tileentity, int lightlevel, boolean direction) {
		x = tileentity.getPos().getX();
		y = (short) tileentity.getPos().getY();
		z = tileentity.getPos().getZ();
		this.lightlevel = lightlevel;
		this.direction = direction;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		x = buf.readInt();
		y = buf.readShort();
		z = buf.readInt();
		lightlevel = buf.readInt();
		direction = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeShort(y);
		buf.writeInt(z);
		buf.writeInt(lightlevel);
		buf.writeBoolean(direction);
	}

	public static class Handler extends AbstractServerMessageHandler<PacketLightSensor> {
		@Override
		public IMessage handleServerMessage(EntityPlayer player, PacketLightSensor message, MessageContext ctx) {
			TileEntity te = player.worldObj.getTileEntity(new BlockPos(message.x, message.y, message.z));
			if (te instanceof TileEntityBrainLightSensor) {
				((TileEntityBrainLightSensor) te).setLightLevel(message.lightlevel);
				((TileEntityBrainLightSensor) te).setDirection(message.direction);
				return new PacketSyncLightSensor(te, message.lightlevel, message.direction);
			} else {
				BSP.error("Tile Entity at " + message.x + ", " + message.y + ", " + message.z + " was " + te
						+ " and not TileEntityBrainLightSensor.");
				return null;
			}
		}
	}
}