package brainstonemod.network.packet;

import brainstonemod.common.helper.BSP;
import brainstonemod.common.helper.BrainStoneDirection;
import brainstonemod.common.tileentity.TileEntityBrainLogicBlock;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class PacketChangeGate implements IMessage {
	private int x;
	private short y;
	private int z;

	private String gate;
	private int direction;

	public PacketChangeGate() {
	}

	public PacketChangeGate(TileEntity tileentity, String gate, int direction) {
		x=tileentity.xCoord;
		y=(short)tileentity.yCoord;
		z=tileentity.zCoord;
		this.gate=gate;
		this.direction=direction;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		x=buf.readInt();
		y=buf.readShort();
		z=buf.readInt();
		gate=ByteBufUtils.readUTF8String(buf);
		direction=buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeShort(y);
		buf.writeInt(z);
		ByteBufUtils.writeUTF8String(buf, gate);
		buf.writeInt(direction);
	}

	public static class Handler extends AbstractServerMessageHandler<PacketChangeGate> {
		@Override
		public IMessage handleServerMessage(EntityPlayer player, PacketChangeGate message, MessageContext ctx) {
			TileEntity te = player.worldObj.getTileEntity(message.x, message.y, message.z);
			if(te instanceof TileEntityBrainLogicBlock){
				((TileEntityBrainLogicBlock) te).changeGate(message.gate, BrainStoneDirection.values()[message.direction]);
			}else{
				BSP.error("Tile Entity at "+message.x+", "+message.y+", "+message.z+" was "+te+" and not TileEntityBrainLogicBlock.");
			}
			return null;
		}
	}
}