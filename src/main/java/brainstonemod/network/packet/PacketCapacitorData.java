package brainstonemod.network.packet;

import brainstonemod.BrainStone;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class PacketCapacitorData implements IMessage {

	private NBTTagCompound data;

	public PacketCapacitorData(){}

	public PacketCapacitorData(NBTTagCompound tagCompound){
		data=tagCompound;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		data=ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, data);
	}

	public static class Handler extends AbstractClientMessageHandler<PacketCapacitorData> {
		@Override
		public IMessage handleClientMessage(EntityPlayer player, PacketCapacitorData message, MessageContext ctx) {
			BrainStone.brainStoneLifeCapacitor().getPlayerCapacitorMapping().setMap(message.data);
			return null;
		}
	}
}
