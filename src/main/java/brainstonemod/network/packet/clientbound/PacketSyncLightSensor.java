package brainstonemod.network.packet.clientbound;

import brainstonemod.common.helper.BSP;
import brainstonemod.common.tileentity.TileEntityBrainLightSensor;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncLightSensor implements IMessage {
	private int x;
	private short y;
	private int z;

	private int lightlevel;
	private boolean direction;

	public PacketSyncLightSensor() {
	}

	public PacketSyncLightSensor(TileEntity tileentity, int lightlevel, boolean direction) {
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

	public static class Handler extends AbstractClientMessageHandler<PacketSyncLightSensor> {
		@Override
		public IMessage handleClientMessage(EntityPlayer player, PacketSyncLightSensor message, MessageContext ctx) {
			TileEntity te = player.world.getTileEntity(new BlockPos(message.x, message.y, message.z));
			if (te instanceof TileEntityBrainLightSensor) {
				((TileEntityBrainLightSensor) te).setLightLevel(message.lightlevel);
				((TileEntityBrainLightSensor) te).setDirection(message.direction);
			} else {
				BSP.error("Tile Entity at " + message.x + ", " + message.y + ", " + message.z + " was " + te
						+ " and not TileEntityBrainLightSensor.");
			}
			return null;
		}
	}
}