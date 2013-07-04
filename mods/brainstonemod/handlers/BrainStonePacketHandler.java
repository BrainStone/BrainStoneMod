package mods.brainstonemod.handlers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import mods.brainstonemod.BrainStone;
import mods.brainstonemod.templates.BSP;
import mods.brainstonemod.templates.TileEntityBrainStoneSyncBase;
import mods.brainstonemod.tileentities.TileEntityBlockBrainLogicBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class BrainStonePacketHandler implements IPacketHandler {
	/**
	 * Sends a sync packet to the closest players.
	 * 
	 * @param x
	 *            x-Coordinate
	 * @param y
	 *            y-Coordinate
	 * @param z
	 *            z-Coordinate
	 * @param world
	 *            The world. Needed for the dimension id
	 * @param channel
	 *            The channel on which the packet is going to be sent
	 * @param data
	 *            The data which contains the syncing information
	 */
	public static void sendPacketToClosestPlayers(int x, int y, int z,
			World world, String channel, ByteArrayOutputStream data) {
		final Packet250CustomPayload pkt = new Packet250CustomPayload();
		pkt.channel = channel;
		pkt.data = data.toByteArray();
		pkt.length = data.size();
		pkt.isChunkDataPacket = true;

		PacketDispatcher.sendPacketToAllAround(x, y, z, 256.0, world
				.getWorldInfo().getDimension(), pkt);

	}

	/**
	 * Sends a sync packet to the closest players.
	 * 
	 * @param te
	 *            The TileEntity. Needed for the coordinates and the dimension
	 * @param channel
	 *            The channel on which the packet is going to be sent
	 * @param data
	 *            The data which contains the syncing information
	 */
	public static void sendPacketToClosestPlayers(TileEntity te,
			String channel, ByteArrayOutputStream data) {
		final Packet250CustomPayload pkt = new Packet250CustomPayload();
		pkt.channel = channel;
		pkt.data = data.toByteArray();
		pkt.length = data.size();
		pkt.isChunkDataPacket = true;

		PacketDispatcher.sendPacketToAllAround(te.xCoord, te.yCoord, te.zCoord,
				256, te.worldObj.getWorldInfo().getDimension(), pkt);

	}

	/**
	 * Sends a snyc packet to the server.
	 * 
	 * @param channel
	 *            The channel on which the packet is going to be sent
	 * @param data
	 *            The data which contains the syncing information
	 */
	public static void sendPacketToServer(String channel,
			ByteArrayOutputStream data) {
		final Packet250CustomPayload pkt = new Packet250CustomPayload();
		pkt.channel = channel;
		pkt.data = data.toByteArray();
		pkt.length = data.size();
		pkt.isChunkDataPacket = true;

		PacketDispatcher.sendPacketToServer(pkt);
	}

	public static void sendPlayerUpdateMovementPacket(Player player, double x,
			double y, double z) {
		final ByteArrayOutputStream bos = new ByteArrayOutputStream(0);
		final DataOutputStream outputStream = new DataOutputStream(bos);

		try {
			outputStream.writeDouble(x);
			outputStream.writeDouble(y);
			outputStream.writeDouble(z);
		} catch (final IOException e) {
			BSP.printException(e);
		}

		final Packet250CustomPayload pkt = new Packet250CustomPayload();
		pkt.channel = "BSM.UPMC";
		pkt.data = bos.toByteArray();
		pkt.length = bos.size();
		pkt.isChunkDataPacket = true;

		PacketDispatcher.sendPacketToPlayer(pkt, player);
	}

	/**
	 * Sends a the server a packet that tell him to update the players
	 * TileEntity at the given location.
	 * 
	 * @param x
	 *            x-Coordinate
	 * @param y
	 *            y-Coordinate
	 * @param z
	 *            z-Coordinate
	 */
	public static void sendPlayerUpdatePacket(int x, int y, int z) {
		final ByteArrayOutputStream bos = new ByteArrayOutputStream(0);
		final DataOutputStream outputStream = new DataOutputStream(bos);

		try {
			outputStream.writeInt(x);
			outputStream.writeInt(y);
			outputStream.writeInt(z);
		} catch (final IOException e) {
			BSP.printException(e);
		}

		sendPacketToServer("BSM.PUAS", bos);
	}

	/**
	 * Sends a packet to all nearby players to rerender a block.
	 * 
	 * @param x
	 *            x-Coordinate
	 * @param y
	 *            y-Coordinate
	 * @param z
	 *            z-Coordinate
	 * @param world
	 *            The world. Needed for the dimension
	 */
	public static void sendReRenderBlockAtPacket(int x, int y, int z,
			World world) {
		final ByteArrayOutputStream bos = new ByteArrayOutputStream(0);
		final DataOutputStream outputStream = new DataOutputStream(bos);

		try {
			outputStream.writeInt(x);
			outputStream.writeInt(y);
			outputStream.writeInt(z);
		} catch (final IOException e) {
			BSP.printException(e);
		}

		sendPacketToClosestPlayers(x, y, z, world, "BSM.RRBAC", bos);
	}

	/**
	 * Syncs the TileEntity with the server (and all other players nearby).
	 * 
	 * @param te
	 *            The TileEntity to be synced
	 */
	public static void sendUpdateOptions(TileEntity te) {
		if (te instanceof TileEntityBrainStoneSyncBase) {
			try {
				((TileEntityBrainStoneSyncBase) te).update(true);
			} catch (final IOException e) {
				BSP.printException(e);
			}
		}
	}

	/** Temporary storage of the received channel */
	private String channel;

	/** Temporary storage of the packet */
	private Packet250CustomPayload packet;

	/** Temporary storage of the player */
	private Player player;

	/**
	 * Temporary storage of the inputStream. The data is read from this. Will be
	 * filled in any primary packet handle function
	 */
	private DataInputStream inputStream;

	/** Is the packet handled or not */
	private boolean handled;

	/**
	 * Temporary storage of the TileEntity. Will be filled in any primary packet
	 * handle function if needed
	 */
	private TileEntity tileEntity;

	/**
	 * Is called when a TileEntity update packet for a client arrives. Reads the
	 * Coordinates and sets the internal tileEntity to the TileEntity just at
	 * the given location.<br>
	 * <b><u>!!!The Packet is still marked as unhandled!!!</u></b>
	 */
	private void handleClientPacket() {
		inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));

		try {
			tileEntity = BrainStone.proxy.getClientWorld().getBlockTileEntity(
					inputStream.readInt(), inputStream.readInt(),
					inputStream.readInt());
		} catch (final IOException ex) {
			BSP.printException(ex);

			tileEntity = null;
		}
	}

	private void handled() {
		handled = true;
	}

	/**
	 * Marks a block in the client word for a render Update.
	 */
	private void handleReRenderBlockAtPacket() {
		inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));

		try {
			final int x = inputStream.readInt();
			final int y = inputStream.readInt();
			final int z = inputStream.readInt();

			BrainStone.proxy.getClientWorld().markBlockForRenderUpdate(x, y, z);
		} catch (final IOException ex) {
			BSP.printException(ex);
		}

		this.handled();
	}

	/**
	 * Is called when a TileEntity update packet for the server arrives. Reads
	 * the Coordinates and sets the internal tileEntity to the TileEntity just
	 * at the given location.<br>
	 * <b><u>!!!The Packet is still marked as unhandled!!!</u></b>
	 */
	private void handleServerPacket() {
		inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
		final EntityPlayer sender = (EntityPlayer) player;

		try {
			tileEntity = MinecraftServer
					.getServer()
					.worldServerForDimension(sender.dimension)
					.getBlockTileEntity(inputStream.readInt(),
							inputStream.readInt(), inputStream.readInt());
		} catch (final IOException ex) {
			BSP.printException(ex);

			tileEntity = null;
		}
	}

	/**
	 * This function is called, when a client added a task to the
	 * TileEntityBlockBrainLogicBlock. Adds the task to the server TileEntity
	 * and updates all nearby client TileEntitys.
	 */
	private void handleTileEntityBlockBrainLogicBlockPacket() {
		try {
			if (tileEntity instanceof TileEntityBlockBrainLogicBlock) {
				((TileEntityBlockBrainLogicBlock) tileEntity)
						.addTASKS(inputStream.readUTF());
			}
		} catch (final IOException ex) {
			BSP.printException(ex);

			return;
		}

		this.handled();
	}

	/**
	 * The standard TileEntity sync function. Marks the packet as handled.
	 * Called if nothing special is to do.
	 */
	private void handleTileEntityBrainStoneSyncBasePacket() {
		try {
			if (tileEntity instanceof TileEntityBrainStoneSyncBase) {
				((TileEntityBrainStoneSyncBase) tileEntity)
						.readFromInputStream(inputStream);
			}
		} catch (final IOException ex) {
			BSP.printException(ex);

			return;
		}

		this.handled();
	}

	private void handleUnknownPacket() {
		BSP.print(
				"The packet's channel \"" + channel + "\" was not regonized!",
				"Content of the packet:", packet.data);

		this.handled();
	}

	/**
	 * Changes the players Velocity
	 */
	private void handleUpdatePlayerMovementPacket() {
		inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));

		try {
			final double x = inputStream.readDouble();
			final double y = inputStream.readDouble();
			final double z = inputStream.readDouble();

			((EntityPlayer) player).addVelocity(x, y, z);
		} catch (final IOException ex) {
			BSP.printException(ex);
		}

		this.handled();
	}

	private boolean isNotHandled() {
		return !handled;
	}

	@Override
	public void onPacketData(INetworkManager manager,
			Packet250CustomPayload packet, Player player) {
		this.unhandled();

		channel = packet.channel;

		if (channel.equals("BSM")) // generic Packet
		{
			;
		} else if (channel.startsWith("BSM")) {
			this.packet = packet;
			this.player = player;
			final String subchannel = channel.substring(4);

			if (channel.endsWith("S")) // Server Packet
			{
				if (subchannel.equals("TEBBSTS") || // TileEntityBloockBrainStoneTrigger
													// Server Packet
						subchannel.equals("TEBBLSS")) // TileEntityBlockBrainLightSensor
														// Server Packet
				{
					this.handleServerPacket();

					this.handleTileEntityBrainStoneSyncBasePacket();

					this.sendPlayerUpdatePacket();
				} else if (subchannel.equals("TEBBLBS")) // TileEntityBlockBrainLogicBlock
															// Server Packet
				{
					this.handleServerPacket();

					this.handleTileEntityBlockBrainLogicBlockPacket();
				} else if (subchannel.equals("UPAS")) // UpdatePlayerAt Server
														// Packet
				{
					this.handleServerPacket();

					this.sendPlayerUpdatePacket();
				}
			} else // Client Packet
			{
				if (subchannel.equals("TEBBSTC") || // TileEntityBloockBrainStoneTrigger
													// Client Packet
						subchannel.equals("TEBBLSC") || // TileEntityBlockBrainLightSensor
														// Client Packet
						subchannel.equals("TEBBLBC")) // TileEntityBlockBrainLogicBlock
														// Client Packet
				{
					this.handleClientPacket();

					this.handleTileEntityBrainStoneSyncBasePacket();
				} else if (subchannel.equals("RRBAC")) // ReRenderBlockAt Client
														// Packet
				{
					this.handleReRenderBlockAtPacket();
				} else if (subchannel.equals("UPMC")) // UpdatePlayerMovement
														// Client Packet
				{
					this.handleUpdatePlayerMovementPacket();
				}
			}
		}

		if (this.isNotHandled()) {
			this.handleUnknownPacket();
		}

	}

	/**
	 * Sends a player a update packet. The save TileEntity is used to determine
	 * what to send. (Only used by handler functions!)
	 */
	private void sendPlayerUpdatePacket() {
		try {
			((TileEntityBrainStoneSyncBase) tileEntity).update(false);
		} catch (final IOException e) {
			BSP.printException(e);
		} catch (final NullPointerException e) {
			BSP.finer(e, "I guess this block just got removed!");
		}
	}

	private void unhandled() {
		handled = false;
	}
}
