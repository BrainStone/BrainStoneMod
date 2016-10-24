package brainstonemod.network.packet.serverbound;

import brainstonemod.common.helper.BSP;
import brainstonemod.common.tileentity.TileEntityBrainStoneTrigger;
import brainstonemod.network.packet.clientbound.PacketSyncSetMaxDelay;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSetMaxDelay implements IMessage {
	private int x;
	private short y;
	private int z;

	private byte maxDelay;

	public PacketSetMaxDelay() {
	}

	public PacketSetMaxDelay(TileEntity tileentity, byte maxDelay) {
		x=tileentity.getPos().getX();
		y=(short)tileentity.getPos().getY();
		z=tileentity.getPos().getZ();
		this.maxDelay=maxDelay;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		x=buf.readInt();
		y=buf.readShort();
		z=buf.readInt();
		maxDelay=buf.readByte();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeShort(y);
		buf.writeInt(z);
		buf.writeByte(maxDelay);
	}

	public static class Handler extends AbstractServerMessageHandler<PacketSetMaxDelay> {
		@Override
		public IMessage handleServerMessage(EntityPlayer player, PacketSetMaxDelay message, MessageContext ctx) {
			TileEntity te = player.worldObj.getTileEntity(new BlockPos(message.x, message.y, message.z));
			if(te instanceof TileEntityBrainStoneTrigger){
				((TileEntityBrainStoneTrigger) te).setMaxDelay(message.maxDelay);
				return new PacketSyncSetMaxDelay(te, message.maxDelay);
			}else{
				BSP.error("Tile Entity at "+message.x+", "+message.y+", "+message.z+" was "+te+" and not TileEntityBrainStoneTrigger.");
				return null;
			}
		}
	}
}