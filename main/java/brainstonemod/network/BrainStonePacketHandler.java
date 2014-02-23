package brainstonemod.network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import brainstonemod.BrainStone;
import brainstonemod.common.helper.BSP;
import brainstonemod.common.tileentity.TileEntityBlockBrainLogicBlock;
import brainstonemod.common.tileentity.template.TileEntityBrainStoneSyncBase;

public class BrainStonePacketHandler implements IPacketHandler {
	public static void sendBrainStoneTriggerMobInformationPacketToPlayer(
			EntityPlayer player2) {
		BSP.debug("Sending BrainStoneTriggerMobInformation Packet");

		final ByteArrayOutputStream data = new ByteArrayOutputStream(0);
		final DataOutputStream output = new DataOutputStream(data);

		final int size = BrainStone.getSidedTiggerEntities().size();
		Class<?>[] value;

		try {
			output.writeInt(size);

			for (final String key : BrainStone.getSidedTiggerEntities()
					.keySet()) {
				value = BrainStone.getSidedTiggerEntities().get(key);

				output.writeUTF(key);
				output.writeInt(value.length);

				for (final Class<?> tmp : value) {
					output.writeUTF(tmp.getName());
				}
			}

		} catch (final IOException ex) {
			BSP.warnException(ex);
		}

		final Packet250CustomPayload pkt = new Packet250CustomPayload();
		pkt.channel = "BSM.BSTMI";
		pkt.data = data.toByteArray();
		pkt.length = data.size();

		PacketDispatcher.sendPacketToPlayer(pkt, player2);

		BSP.debug("Done sending BrainStoneTriggerMobInformation Packet");
	}

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

		PacketDispatcher.sendPacketToAllAround(x, y, z, 256.0,
				world.provider.dimensionId, pkt);

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

		PacketDispatcher.sendPacketToAllAround(te.xCoord, te.yCoord, te.zCoord,
				256, te.worldObj.provider.dimensionId, pkt);

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

		PacketDispatcher.sendPacketToServer(pkt);
	}

	public static void sendPlayerUpdateMovementPacket(EntityPlayer player,
			double x, double y, double z) {
		final ByteArrayOutputStream bos = new ByteArrayOutputStream(0);
		final DataOutputStream outputStream = new DataOutputStream(bos);

		try {
			outputStream.writeDouble(x);
			outputStream.writeDouble(y);
			outputStream.writeDouble(z);
		} catch (final IOException e) {
			BSP.logException(e);
		}

		final Packet250CustomPayload pkt = new Packet250CustomPayload();
		pkt.channel = "BSM.UPMC";
		pkt.data = bos.toByteArray();
		pkt.length = bos.size();

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
			BSP.logException(e);
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
			BSP.logException(e);
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
				BSP.logException(e);
			}
		}
	}

	/** Temporary storage of the received channel */
	private String channel;

	/** Temporary storage of the packet */
	private Packet250CustomPayload packet;

	/** Temporary storage of the player */
	private EntityPlayer player;

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

	private void handleBrainStoneTriggerMobInformationPacket() {
		BSP.debug("Handling BrainStoneTriggerMobInformation Packet");

		inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));

		try {
			int size, sizeTmp, i, j;
			String key;
			Class<?>[] value;

			size = inputStream.readInt();

			final LinkedHashMap<String, Class<?>[]> tmpTriggerEntities = new LinkedHashMap<String, Class<?>[]>(
					size);

			for (i = 0; i < size; i++) {
				key = inputStream.readUTF();
				sizeTmp = inputStream.readInt();

				value = new Class[sizeTmp];

				for (j = 0; j < sizeTmp; j++) {
					value[j] = Class.forName(inputStream.readUTF());
				}

				tmpTriggerEntities.put(key, value);
			}

			BrainStone.setClientSideTiggerEntities(tmpTriggerEntities);

			BSP.debug("Dumping tmpTriggerEntities in handleBrainStoneTriggerMobInformationPacket");

			for (final String key2 : tmpTriggerEntities.keySet()) {
				BSP.debug(key2 + ":"
						+ Arrays.toString(tmpTriggerEntities.get(key2)));
			}

			BSP.debug("End of Dump");

			handled();
		} catch (final IOException ex) {
			BSP.warnException(ex);
		} catch (final ClassNotFoundException ex) {
			BSP.fatalException(ex,
					"There seems to be some mods missing on the client side!\n"
							+ "This a bug.\nPlease report it!");
		}

		BSP.debug("Done handling BrainStoneTriggerMobInformation Packet");
	}

	/**
	 * Is called when a TileEntity update packet for a client arrives. Reads the
	 * Coordinates and sets the internal tileEntity to the TileEntity just at
	 * the given location.<br>
	 * <b><u>!!!The Packet is still marked as unhandled!!!</u></b>
	 */
	private void handleClientPacket() {
		inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));

		try {
			tileEntity = BrainStone.proxy.getClientWorld().getTileEntity(
					inputStream.readInt(), inputStream.readInt(),
					inputStream.readInt());
		} catch (final IOException ex) {
			BSP.logException(ex);

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
			BSP.logException(ex);
		}

		handled();
	}

	/**
	 * Is called when a TileEntity update packet for the server arrives. Reads
	 * the Coordinates and sets the internal tileEntity to the TileEntity just
	 * at the given location.<br>
	 * <b><u>!!!The Packet is still marked as unhandled!!!</u></b>
	 */
	private void handleServerPacket() {
		inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
		final EntityPlayer sender = player;

		try {
			tileEntity = MinecraftServer
					.getServer()
					.worldServerForDimension(sender.dimension)
					.getTileEntity(inputStream.readInt(),
							inputStream.readInt(), inputStream.readInt());
		} catch (final IOException ex) {
			BSP.logException(ex);

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
			BSP.logException(ex);

			return;
		}

		handled();
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
			BSP.logException(ex);

			return;
		}

		handled();
	}

	private void handleUnknownPacket() {
		BSP.warning("The packet's channel \"" + channel
				+ "\" was not regonized!", "Content of the packet:",
				packet.data);

		handled();
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

			player.addVelocity(x, y, z);
		} catch (final IOException ex) {
			BSP.logException(ex);
		}

		handled();
	}

	private boolean isNotHandled() {
		return !handled;
	}

	@Override
	public void onPacketData(INetworkManager manager,
			Packet250CustomPayload packet, EntityPlayer player) {
		unhandled();

		channel = packet.channel;

		// generic Packet
		if (channel.equals("BSM")) {
			;
		} else if (channel.startsWith("BSM")) {
			this.packet = packet;
			this.player = player;
			final String subchannel = channel.substring(4);

			// Server Packet
			if (channel.endsWith("S")) {

				// TileEntityBloockBrainStoneTrigger Server Packet
				// TileEntityBlockBrainLightSensor Server Packet
				if (subchannel.equals("TEBBSTS")
						|| subchannel.equals("TEBBLSS")) {
					handleServerPacket();

					handleTileEntityBrainStoneSyncBasePacket();

					this.sendPlayerUpdatePacket();

					// TileEntityBlockBrainLogicBlock Server Packet
				} else if (subchannel.equals("TEBBLBS")) {
					handleServerPacket();

					handleTileEntityBlockBrainLogicBlockPacket();

					// UpdatePlayerAt Server Packet
				} else if (subchannel.equals("UPAS")) {
					handleServerPacket();

					this.sendPlayerUpdatePacket();
				}

				// Client Packet
			} else if (channel.endsWith("C")) {

				// TileEntityBloockBrainStoneTrigger Client Packet
				// TileEntityBlockBrainLightSensor Client Packet
				// TileEntityBlockBrainLogicBlock Client Packet
				if (subchannel.equals("TEBBSTC")
						|| subchannel.equals("TEBBLSC")
						|| subchannel.equals("TEBBLBC")) {
					handleClientPacket();

					handleTileEntityBrainStoneSyncBasePacket();

					// ReRenderBlockAt Client Packet
				} else if (subchannel.equals("RRBAC")) {
					handleReRenderBlockAtPacket();

					// UpdatePlayerMovement Client Packet
				} else if (subchannel.equals("UPMC")) {
					handleUpdatePlayerMovementPacket();
				}
			} else {

				// BrainStoneTriggerMobInformation Packet
				if (subchannel.equals("BSTMI")) {
					handleBrainStoneTriggerMobInformationPacket();
				}
			}
		}

		// Unknown Packet
		if (isNotHandled()) {
			handleUnknownPacket();
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
			BSP.logException(e);
		} catch (final NullPointerException e) {
			BSP.debug(e, "I guess this block just got removed!");
		}
	}

	private void unhandled() {
		handled = false;
	}
}
