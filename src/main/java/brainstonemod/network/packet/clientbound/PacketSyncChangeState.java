package brainstonemod.network.packet.clientbound;

import brainstonemod.common.helper.BSP;
import brainstonemod.common.tileentity.TileEntityBrainLightSensor;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncChangeState implements IMessage {
	private int x;
	private short y;
	private int z;

	private boolean state;

	public PacketSyncChangeState() {
	}

	public PacketSyncChangeState(TileEntity tileentity, boolean state) {
		x = tileentity.getPos().getX();
		y = (short) tileentity.getPos().getY();
		z = tileentity.getPos().getZ();

		this.state = state;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		x = buf.readInt();
		y = buf.readShort();
		z = buf.readInt();

		state = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeShort(y);
		buf.writeInt(z);

		buf.writeBoolean(state);
	}

	public static class Handler extends AbstractClientMessageHandler<PacketSyncChangeState> {
		@Override
		public IMessage handleClientMessage(EntityPlayer player, PacketSyncChangeState message, MessageContext ctx) {
			TileEntity te = player.worldObj.getTileEntity(new BlockPos(message.x, message.y, message.z));
			if (te instanceof TileEntityBrainLightSensor) {
				((TileEntityBrainLightSensor) te).setState(message.state);
			} else {
				BSP.error("Tile Entity at " + message.x + ", " + message.y + ", " + message.z + " was " + te
						+ " and not TileEntityBrainLightSensor.");
			}
			return null;
		}
	}
}