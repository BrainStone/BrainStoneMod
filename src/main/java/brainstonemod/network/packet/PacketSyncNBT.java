package brainstonemod.network.packet;

import brainstonemod.common.tileentity.template.TileEntityBrainStoneSyncBase;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class PacketSyncNBT implements IMessage {
	private int x;
	private int y;
	private int z;
	private NBTTagCompound nbt;

	public PacketSyncNBT() {
	}

	public PacketSyncNBT(TileEntityBrainStoneSyncBase tileentity) {
		nbt = new NBTTagCompound();
		tileentity.writeToNBT(nbt);
		x=tileentity.xCoord;
		y=tileentity.yCoord;
		z=tileentity.zCoord;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		nbt=ByteBufUtils.readTag(buf);
		x=buf.readInt();
		y=buf.readShort();
		z=buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, nbt);
		buf.writeInt(x);
		buf.writeShort(y);
		buf.writeInt(z);
	}

	public static class Handler extends AbstractServerMessageHandler<PacketSyncNBT> {
		@Override
		public IMessage handleServerMessage(EntityPlayer player, PacketSyncNBT message, MessageContext ctx) {
			player.worldObj.getTileEntity(message.x, message.y, message.z).readFromNBT(message.nbt);
			return null;
		}
	}
}