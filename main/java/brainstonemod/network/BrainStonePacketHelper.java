package brainstonemod.network;

import io.netty.channel.ChannelHandler.Sharable;
import net.minecraft.client.Minecraft;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import brainstonemod.BrainStone;
import brainstonemod.common.helper.BSP;
import brainstonemod.common.tileentity.template.TileEntityBrainStoneSyncBase;
import brainstonemod.network.packet.BrainStoneTriggerMobInformationPacket;

@Sharable
public class BrainStonePacketHelper {
	public static void sendBrainStoneTriggerMobInformationPacketToPlayer(
			EntityPlayer player) {
		BSP.debug("Sending BrainStoneTriggerMobInformation Packet...");

		BrainStone.packetPipeline.sendTo(
				new BrainStoneTriggerMobInformationPacket(BrainStone
						.getServerSideTiggerEntities()),
				(EntityPlayerMP) player);

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
	 * @param packet
	 *            The packet to send
	 */
	public static void sendPacketToClosestPlayers(int x, int y, int z,
			World world, final Packet packet) {
		final int radius = 256;

		world.getEntitiesWithinAABBExcludingEntity(null, AxisAlignedBB
				.getBoundingBox(x - radius, y - radius, z - radius, x + radius,
						y + radius, z + radius), new IEntitySelector() {

			@Override
			public boolean isEntityApplicable(Entity var1) {
				if (var1 instanceof EntityPlayerMP) {
					final EntityPlayerMP pl = (EntityPlayerMP) var1;
					pl.playerNetServerHandler.sendPacket(packet);
				}
				return false;
			}
		});

	}

	/**
	 * Sends a sync packet to the closest players.
	 * 
	 * @param te
	 *            The TileEntity. Needed for the coordinates and the dimension
	 * @param packet
	 *            The packet to send
	 */
	public static void sendPacketToClosestPlayers(TileEntity te, Packet packet) {
		sendPacketToClosestPlayers(te.xCoord, te.yCoord, te.zCoord,
				te.getWorldObj(), packet);

	}

	/**
	 * Sends a snyc packet to the server.
	 * 
	 * @param data
	 *            The data which contains the syncing information
	 */
	public static void sendPacketToServer(Packet packet) {
		try {
			Minecraft.getMinecraft().getNetHandler().addToSendQueue(packet);
		} catch (final NullPointerException e) {
			BSP.infoException(e);
		}
	}

	// DOCME
	public static void sendPlayerUpdateMovementPacket(EntityPlayer entity,
			double x, double y, double z) {
		entity.setVelocity(x, y, z);
		final S12PacketEntityVelocity packet = new S12PacketEntityVelocity(
				entity);

		sendPacketToClosestPlayers(entity.serverPosX, entity.serverPosY,
				entity.serverPosZ, entity.worldObj, packet);
	}

	public static void sendReRenderBlockAtPacket(int x, int y, int z,
			World world) {
		// TODO Auto-generated method stub
	}

	public static void sendUpdateOptions(TileEntityBrainStoneSyncBase tileentity) {
		tileentity.allowClientToServerUpdate();
	}
}
