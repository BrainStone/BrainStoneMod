package mods.brainstone.handlers;

import aab;
import ajv;
import aqp;
import cg;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import dk;
import iz;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import mods.brainstone.BrainStone;
import mods.brainstone.ClientProxy;
import mods.brainstone.templates.BSP;
import mods.brainstone.templates.TileEntityBrainStoneSyncBase;
import mods.brainstone.tileentities.TileEntityBlockBrainLogicBlock;
import net.minecraft.server.MinecraftServer;
import sq;

public class BrainStonePacketHandler
  implements IPacketHandler
{
  private String channel;
  private dk packet;
  private Player player;
  private DataInputStream inputStream;
  private boolean handled;
  private aqp tileEntity;

  public static void sendPacketToClosestPlayers(int x, int y, int z, aab world, String channel, ByteArrayOutputStream data)
  {
    dk pkt = new dk();
    pkt.a = channel;
    pkt.c = data.toByteArray();
    pkt.b = data.size();
    pkt.s = true;

    PacketDispatcher.sendPacketToAllAround(x, y, z, 256.0D, world.L().j(), pkt);
  }

  public static void sendPacketToClosestPlayers(aqp te, String channel, ByteArrayOutputStream data)
  {
    dk pkt = new dk();
    pkt.a = channel;
    pkt.c = data.toByteArray();
    pkt.b = data.size();
    pkt.s = true;

    PacketDispatcher.sendPacketToAllAround(te.l, te.m, te.n, 256.0D, te.k.L().j(), pkt);
  }

  public static void sendPacketToServer(String channel, ByteArrayOutputStream data)
  {
    dk pkt = new dk();
    pkt.a = channel;
    pkt.c = data.toByteArray();
    pkt.b = data.size();
    pkt.s = true;

    PacketDispatcher.sendPacketToServer(pkt);
  }

  public static void sendPlayerUpdateMovementPacket(Player player, double x, double y, double z)
  {
    ByteArrayOutputStream bos = new ByteArrayOutputStream(0);
    DataOutputStream outputStream = new DataOutputStream(bos);
    try
    {
      outputStream.writeDouble(x);
      outputStream.writeDouble(y);
      outputStream.writeDouble(z);
    } catch (IOException e) {
      BSP.printException(e);
    }

    dk pkt = new dk();
    pkt.a = "BSM.UPMC";
    pkt.c = bos.toByteArray();
    pkt.b = bos.size();
    pkt.s = true;

    PacketDispatcher.sendPacketToPlayer(pkt, player);
  }

  public static void sendPlayerUpdatePacket(int x, int y, int z)
  {
    ByteArrayOutputStream bos = new ByteArrayOutputStream(0);
    DataOutputStream outputStream = new DataOutputStream(bos);
    try
    {
      outputStream.writeInt(x);
      outputStream.writeInt(y);
      outputStream.writeInt(z);
    } catch (IOException e) {
      BSP.printException(e);
    }

    sendPacketToServer("BSM.PUAS", bos);
  }

  public static void sendReRenderBlockAtPacket(int x, int y, int z, aab world)
  {
    ByteArrayOutputStream bos = new ByteArrayOutputStream(0);
    DataOutputStream outputStream = new DataOutputStream(bos);
    try
    {
      outputStream.writeInt(x);
      outputStream.writeInt(y);
      outputStream.writeInt(z);
    } catch (IOException e) {
      BSP.printException(e);
    }

    sendPacketToClosestPlayers(x, y, z, world, "BSM.RRBAC", bos);
  }

  public static void sendUpdateOptions(aqp te)
  {
    if ((te instanceof TileEntityBrainStoneSyncBase))
      try {
        ((TileEntityBrainStoneSyncBase)te).update(true);
      } catch (IOException e) {
        BSP.printException(e);
      }
  }

  private void handleClientPacket()
  {
    this.inputStream = new DataInputStream(new ByteArrayInputStream(this.packet.c));
    try
    {
      this.tileEntity = BrainStone.proxy.getClientWorld().r(this.inputStream.readInt(), this.inputStream.readInt(), this.inputStream.readInt());
    }
    catch (IOException ex)
    {
      BSP.printException(ex);

      this.tileEntity = null;
    }
  }

  private void handled() {
    this.handled = true;
  }

  private void handleReRenderBlockAtPacket()
  {
    this.inputStream = new DataInputStream(new ByteArrayInputStream(this.packet.c));
    try
    {
      int x = this.inputStream.readInt();
      int y = this.inputStream.readInt();
      int z = this.inputStream.readInt();

      BrainStone.proxy.getClientWorld().p(x, y, z);
    } catch (IOException ex) {
      BSP.printException(ex);
    }

    handled();
  }

  private void handleServerPacket()
  {
    this.inputStream = new DataInputStream(new ByteArrayInputStream(this.packet.c));
    sq sender = (sq)this.player;
    try
    {
      this.tileEntity = MinecraftServer.D().a(sender.ar).r(this.inputStream.readInt(), this.inputStream.readInt(), this.inputStream.readInt());
    }
    catch (IOException ex)
    {
      BSP.printException(ex);

      this.tileEntity = null;
    }
  }

  private void handleTileEntityBlockBrainLogicBlockPacket()
  {
    try
    {
      if ((this.tileEntity instanceof TileEntityBlockBrainLogicBlock))
        ((TileEntityBlockBrainLogicBlock)this.tileEntity).addTASKS(this.inputStream.readUTF());
    }
    catch (IOException ex)
    {
      BSP.printException(ex);

      return;
    }

    handled();
  }

  private void handleTileEntityBrainStoneSyncBasePacket()
  {
    try
    {
      if ((this.tileEntity instanceof TileEntityBrainStoneSyncBase))
        ((TileEntityBrainStoneSyncBase)this.tileEntity).readFromInputStream(this.inputStream);
    }
    catch (IOException ex)
    {
      BSP.printException(ex);

      return;
    }

    handled();
  }

  private void handleUnknownPacket() {
    BSP.println("The packet's channel \"" + this.channel + "\" was not regonized!");

    BSP.println("Content of the packet:");
    BSP.println(this.packet.c);

    handled();
  }

  private void handleUpdatePlayerMovementPacket()
  {
    this.inputStream = new DataInputStream(new ByteArrayInputStream(this.packet.c));
    try
    {
      double x = this.inputStream.readDouble();
      double y = this.inputStream.readDouble();
      double z = this.inputStream.readDouble();

      ((sq)this.player).g(x, y, z);
    } catch (IOException ex) {
      BSP.printException(ex);
    }

    handled();
  }

  private boolean isNotHandled() {
    return !this.handled;
  }

  public void onPacketData(cg manager, dk packet, Player player)
  {
    unhandled();

    this.channel = packet.a;

    if (!this.channel.equals("BSM"))
    {
      if (this.channel.startsWith("BSM")) {
        this.packet = packet;
        this.player = player;
        String subchannel = this.channel.substring(4);

        if (this.channel.endsWith("S"))
        {
          if ((subchannel.equals("TEBBSTS")) || (subchannel.equals("TEBBLSS")))
          {
            handleServerPacket();

            handleTileEntityBrainStoneSyncBasePacket();

            sendPlayerUpdatePacket();
          } else if (subchannel.equals("TEBBLBS"))
          {
            handleServerPacket();

            handleTileEntityBlockBrainLogicBlockPacket();
          } else if (subchannel.equals("UPAS"))
          {
            handleServerPacket();

            sendPlayerUpdatePacket();
          }

        }
        else if ((subchannel.equals("TEBBSTC")) || (subchannel.equals("TEBBLSC")) || (subchannel.equals("TEBBLBC")))
        {
          handleClientPacket();

          handleTileEntityBrainStoneSyncBasePacket();
        } else if (subchannel.equals("RRBAC"))
        {
          handleReRenderBlockAtPacket();
        } else if (subchannel.equals("UPMC"))
        {
          handleUpdatePlayerMovementPacket();
        }
      }
    }

    if (isNotHandled())
      handleUnknownPacket();
  }

  private void sendPlayerUpdatePacket()
  {
    try
    {
      ((TileEntityBrainStoneSyncBase)this.tileEntity).update(false);
    } catch (IOException e) {
      BSP.printException(e);
    }
  }

  private void unhandled() {
    this.handled = false;
  }
}