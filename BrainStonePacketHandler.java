package net.braintonemod.src;

import ahq;
import anq;
import ce;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import dj;
import in;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import net.minecraft.server.MinecraftServer;
import qx;
import xv;

public class BrainStonePacketHandler
  implements IPacketHandler
{
  private String channel;
  private dj packet;
  private Player player;
  private DataInputStream inputStream;
  private boolean handled;
  private anq tileEntity;

  public void onPacketData(ce manager, dj packet, Player player)
  {
    unhandled();

    this.channel = packet.a;

    if (!this.channel.equals("BSM"))
    {
      if (this.channel.startsWith("BSM"))
      {
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
          }
          else if (subchannel.equals("TEBBLBS"))
          {
            handleServerPacket();

            handleTileEntityBlockBrainLogicBlockPacket();
          }
          else if (subchannel.equals("UPAS"))
          {
            handleServerPacket();

            sendPlayerUpdatePacket();
          }

        }
        else if ((subchannel.equals("TEBBSTC")) || (subchannel.equals("TEBBLSC")) || (subchannel.equals("TEBBLBC")))
        {
          handleClientPacket();

          handleTileEntityBrainStoneSyncBasePacket();
        }
        else if (subchannel.equals("RRBAC"))
        {
          handleReRenderBlockAtPacket();
        }
      }
    }

    if (isNotHandled())
    {
      handleUnknownPacket();
    }
  }

  public static void sendPacketToClosestPlayers(anq te, String channel, ByteArrayOutputStream data)
  {
    dj pkt = new dj();
    pkt.a = channel;
    pkt.c = data.toByteArray();
    pkt.b = data.size();
    pkt.r = true;

    PacketDispatcher.sendPacketToAllAround(te.l, te.m, te.n, 256.0D, te.k.K().j(), pkt);
  }

  public static void sendPacketToClosestPlayers(int x, int y, int z, xv world, String channel, ByteArrayOutputStream data)
  {
    dj pkt = new dj();
    pkt.a = channel;
    pkt.c = data.toByteArray();
    pkt.b = data.size();
    pkt.r = true;

    PacketDispatcher.sendPacketToAllAround(x, y, z, 256.0D, world.K().j(), pkt);
  }

  public static void sendPacketToServer(String channel, ByteArrayOutputStream data)
  {
    dj pkt = new dj();
    pkt.a = channel;
    pkt.c = data.toByteArray();
    pkt.b = data.size();
    pkt.r = true;

    PacketDispatcher.sendPacketToServer(pkt);
  }

  public static void sendUpdateOptions(anq te)
  {
    if ((te instanceof TileEntityBrainStoneSyncBase))
    {
      try
      {
        ((TileEntityBrainStoneSyncBase)te).update(true);
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
  }

  public static void sendReRenderBlockAtPacket(int x, int y, int z, xv world)
  {
    ByteArrayOutputStream bos = new ByteArrayOutputStream(0);
    DataOutputStream outputStream = new DataOutputStream(bos);
    try
    {
      outputStream.writeInt(x);
      outputStream.writeInt(y);
      outputStream.writeInt(z);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }

    sendPacketToClosestPlayers(x, y, z, world, "BSM.RRBAC", bos);
  }

  private void sendPlayerUpdatePacket()
  {
    try
    {
      ((TileEntityBrainStoneSyncBase)this.tileEntity).update(false);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
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
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }

    sendPacketToServer("BSM.PUAS", bos);
  }

  private void handleClientPacket()
  {
    this.inputStream = new DataInputStream(new ByteArrayInputStream(this.packet.c));
    try
    {
      this.tileEntity = BrainStone.proxy.getClientWorld().q(this.inputStream.readInt(), this.inputStream.readInt(), this.inputStream.readInt());
    }
    catch (IOException ex)
    {
      ex.printStackTrace();

      this.tileEntity = null;
    }
  }

  private void handleServerPacket()
  {
    this.inputStream = new DataInputStream(new ByteArrayInputStream(this.packet.c));
    qx sender = (qx)this.player;
    try
    {
      this.tileEntity = MinecraftServer.D().a(sender.ap).q(this.inputStream.readInt(), this.inputStream.readInt(), this.inputStream.readInt());
    }
    catch (IOException ex)
    {
      ex.printStackTrace();

      this.tileEntity = null;
    }
  }

  private void handleTileEntityBrainStoneSyncBasePacket()
  {
    try
    {
      if ((this.tileEntity instanceof TileEntityBrainStoneSyncBase))
      {
        ((TileEntityBrainStoneSyncBase)this.tileEntity).readFromInputStream(this.inputStream);
      }
    }
    catch (IOException ex)
    {
      ex.printStackTrace();

      return;
    }

    handled();
  }

  private void handleTileEntityBlockBrainLogicBlockPacket()
  {
    try
    {
      if ((this.tileEntity instanceof TileEntityBlockBrainLogicBlock))
      {
        ((TileEntityBlockBrainLogicBlock)this.tileEntity).addTASKS(this.inputStream.readUTF());
      }
    }
    catch (IOException ex)
    {
      ex.printStackTrace();

      return;
    }

    handled();
  }

  private void handleReRenderBlockAtPacket()
  {
    this.inputStream = new DataInputStream(new ByteArrayInputStream(this.packet.c));
    try
    {
      int x = this.inputStream.readInt();
      int y = this.inputStream.readInt();
      int z = this.inputStream.readInt();

      BrainStone.proxy.getClientWorld().o(x, y, z);
    }
    catch (IOException ex)
    {
      ex.printStackTrace();
    }

    handled();
  }

  private void handleUnknownPacket()
  {
    System.out.println("The packet's channel \"" + this.channel + "\" was not regonized!");
    System.out.println("Content of the packet:");
    System.out.println(this.packet.c);

    handled();
  }

  private void unhandled()
  {
    this.handled = false;
  }

  private void handled()
  {
    this.handled = true;
  }

  private boolean isHandled()
  {
    return this.handled;
  }

  private boolean isNotHandled()
  {
    return !this.handled;
  }
}