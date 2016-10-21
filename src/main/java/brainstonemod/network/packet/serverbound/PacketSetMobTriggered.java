package brainstonemod.network.packet.serverbound;

import brainstonemod.common.helper.BSP;
import brainstonemod.common.tileentity.TileEntityBrainStoneTrigger;
import brainstonemod.network.packet.clientbound.PacketSyncSetMobTriggered;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class PacketSetMobTriggered implements IMessage {
	private int x;
	private short y;
	private int z;

	private String mob;
	private int power;

	public PacketSetMobTriggered() {
	}

	public PacketSetMobTriggered(TileEntity tileentity, String mob, int power) {
		x=tileentity.xCoord;
		y=(short)tileentity.yCoord;
		z=tileentity.zCoord;
		this.mob=mob;
		this.power=power;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		x=buf.readInt();
		y=buf.readShort();
		z=buf.readInt();
		mob=ByteBufUtils.readUTF8String(buf);
		power=buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeShort(y);
		buf.writeInt(z);
		ByteBufUtils.writeUTF8String(buf, mob);
		buf.writeInt(power);
	}

	public static class Handler extends AbstractServerMessageHandler<PacketSetMobTriggered> {
		@Override
		public IMessage handleServerMessage(EntityPlayer player, PacketSetMobTriggered message, MessageContext ctx) {
			TileEntity te = player.worldObj.getTileEntity(message.x, message.y, message.z);
			if(te instanceof TileEntityBrainStoneTrigger){
				((TileEntityBrainStoneTrigger) te).setMobTriggered(message.mob, message.power);
				return new PacketSyncSetMobTriggered(te, message.mob, message.power);
			}else{
				BSP.error("Tile Entity at "+message.x+", "+message.y+", "+message.z+" was "+te+" and not TileEntityBrainStoneTrigger.");
				return null;
			}
		}
	}
}