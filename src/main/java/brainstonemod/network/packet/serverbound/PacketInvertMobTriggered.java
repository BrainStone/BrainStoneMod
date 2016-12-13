package brainstonemod.network.packet.serverbound;

import brainstonemod.common.helper.BSP;
import brainstonemod.common.tileentity.TileEntityBrainStoneTrigger;
import brainstonemod.network.packet.clientbound.PacketSyncInvertMobTriggered;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketInvertMobTriggered implements IMessage {
	private int x;
	private short y;
	private int z;

	private String mob;

	public PacketInvertMobTriggered() {
	}

	public PacketInvertMobTriggered(TileEntity tileentity, String mob) {
		x = tileentity.getPos().getX();
		y = (short) tileentity.getPos().getY();
		z = tileentity.getPos().getZ();
		this.mob = mob;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		x = buf.readInt();
		y = buf.readShort();
		z = buf.readInt();
		mob = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeShort(y);
		buf.writeInt(z);
		ByteBufUtils.writeUTF8String(buf, mob);
	}

	public static class Handler extends AbstractServerMessageHandler<PacketInvertMobTriggered> {
		@Override
		public IMessage handleServerMessage(EntityPlayer player, PacketInvertMobTriggered message, MessageContext ctx) {
			TileEntity te = player.world.getTileEntity(new BlockPos(message.x, message.y, message.z));
			if (te instanceof TileEntityBrainStoneTrigger) {
				((TileEntityBrainStoneTrigger) te).invertMobTriggered(message.mob);
				return new PacketSyncInvertMobTriggered(te, message.mob);
			} else {
				BSP.error("Tile Entity at " + message.x + ", " + message.y + ", " + message.z + " was " + te
						+ " and not TileEntityBrainStoneTrigger.");
				return null;
			}
		}
	}
}