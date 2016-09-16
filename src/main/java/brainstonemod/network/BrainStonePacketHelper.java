package brainstonemod.network;

import brainstonemod.BrainStone;
import brainstonemod.common.helper.BSP;
import brainstonemod.common.tileentity.template.TileEntityBrainStoneSyncBase;
import brainstonemod.network.packet.PacketRedoRender;
import brainstonemod.network.packet.PacketSmokeParticle;
import brainstonemod.network.packet.PacketTriggerMobs;
import brainstonemod.network.packet.PacketSyncNBT;
import cpw.mods.fml.common.network.NetworkRegistry;
import io.netty.channel.ChannelHandler.Sharable;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

@Sharable
public class BrainStonePacketHelper {
	public static void sendBrainLightSensorSmokePacket(int dimension, int x, int y, int z) {
		PacketDispatcher.sendToAllAround(
				new PacketSmokeParticle(x, y, z),
				new NetworkRegistry.TargetPoint(dimension, x, y, z, 100));
		//Is 100 the radius in blocks? If so, isn't that a bit excessive, given how far away you need to see particles?
	}

	public static void sendBrainStoneTriggerMobInformationPacketToPlayer(
			EntityPlayer player) {
		BSP.debug("Sending BrainStoneTriggerMobInformation Packet...");

		PacketDispatcher.sendTo(
				new PacketTriggerMobs(BrainStone
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

	// DOCME
	public static void sendPlayerUpdateMovementPacket(EntityPlayer entity,
			double x, double y, double z) {
		final S12PacketEntityVelocity packet = new S12PacketEntityVelocity(
				entity.getEntityId(), x, y, z);

		sendPacketToClosestPlayers((int) entity.posX, (int) entity.posY,
				(int) entity.posZ, entity.worldObj, packet);
	}

	// DOCME
	public static void sendReRenderBlockAtPacket(int dimension, int x, int y, int z, World world) {
		PacketDispatcher.sendToAllAround(
				new PacketRedoRender(x, y, z),
				new NetworkRegistry.TargetPoint(dimension, x, y, z, 100));
	}

	// DOCME
	public static void sendUpdateTileEntityPacket(TileEntityBrainStoneSyncBase tileentity) {
		PacketDispatcher.sendToServer(new PacketSyncNBT(tileentity));
	}
}
