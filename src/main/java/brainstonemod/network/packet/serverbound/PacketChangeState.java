package brainstonemod.network.packet.serverbound;

import brainstonemod.common.helper.BSP;
import brainstonemod.common.tileentity.TileEntityBrainLightSensor;
import brainstonemod.network.packet.clientbound.PacketSyncChangeState;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketChangeState implements IMessage {
  private int x;
  private short y;
  private int z;

  public PacketChangeState() {}

  public PacketChangeState(TileEntity tileentity) {
    x = tileentity.getPos().getX();
    y = (short) tileentity.getPos().getY();
    z = tileentity.getPos().getZ();
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    x = buf.readInt();
    y = buf.readShort();
    z = buf.readInt();
  }

  @Override
  public void toBytes(ByteBuf buf) {
    buf.writeInt(x);
    buf.writeShort(y);
    buf.writeInt(z);
  }

  public static class Handler extends AbstractServerMessageHandler<PacketChangeState> {
    @Override
    public IMessage handleServerMessage(
        EntityPlayer player, PacketChangeState message, MessageContext ctx) {
      TileEntity te = player.world.getTileEntity(new BlockPos(message.x, message.y, message.z));
      if (te instanceof TileEntityBrainLightSensor) {
        ((TileEntityBrainLightSensor) te).changeState();
        return new PacketSyncChangeState(te, ((TileEntityBrainLightSensor) te).getState());
      } else {
        BSP.error(
            "Tile Entity at "
                + message.x
                + ", "
                + message.y
                + ", "
                + message.z
                + " was "
                + te
                + " and not TileEntityBrainLightSensor.");
        return null;
      }
    }
  }
}
