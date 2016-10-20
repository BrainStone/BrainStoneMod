package brainstonemod.network.packet;

import brainstonemod.common.helper.BSP;
import brainstonemod.common.tileentity.TileEntityBrainStoneTrigger;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class PacketSetMaxDelay implements IMessage {
	private int x;
	private short y;
	private int z;

	private byte maxDelay;

	public PacketSetMaxDelay() {
	}

	public PacketSetMaxDelay(TileEntity tileentity, byte maxDelay) {
		x=tileentity.xCoord;
		y=(short)tileentity.yCoord;
		z=tileentity.zCoord;
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
			TileEntity te = player.worldObj.getTileEntity(message.x, message.y, message.z);
			if(te instanceof TileEntityBrainStoneTrigger){
				((TileEntityBrainStoneTrigger) te).setMaxDelay(message.maxDelay);
			}else{
				BSP.error("Tile Entity at "+message.x+", "+message.y+", "+message.z+" was "+te+" and not TileEntityBrainStoneTrigger.");
			}
			return null;
		}
	}
}