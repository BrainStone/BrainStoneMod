package brainstonemod.network.packet.clientbound;

import brainstonemod.common.helper.BSP;
import brainstonemod.common.tileentity.TileEntityBrainStoneTrigger;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class PacketSyncEnableMobs implements IMessage {
	private int x;
	private short y;
	private int z;

	public PacketSyncEnableMobs() {
	}

	public PacketSyncEnableMobs(TileEntity tileentity) {
		x=tileentity.xCoord;
		y=(short)tileentity.yCoord;
		z=tileentity.zCoord;
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

	public static class Handler extends AbstractClientMessageHandler<PacketSyncEnableMobs> {
		@Override
		public IMessage handleClientMessage(EntityPlayer player, PacketSyncEnableMobs message, MessageContext ctx) {
			TileEntity te = player.worldObj.getTileEntity(message.x, message.y, message.z);
			if(te instanceof TileEntityBrainStoneTrigger){
				((TileEntityBrainStoneTrigger) te).enableAllMobs();
			}else{
				BSP.error("Tile Entity at "+message.x+", "+message.y+", "+message.z+" was "+te+" and not TileEntityBrainStoneTrigger.");
			}
			return null;
		}
	}
}