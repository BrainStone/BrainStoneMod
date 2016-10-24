package brainstonemod.network;

import brainstonemod.BrainStone;
import brainstonemod.common.helper.BSP;
import brainstonemod.network.packet.clientbound.PacketRedoRender;
import brainstonemod.network.packet.clientbound.PacketSmokeParticle;
import brainstonemod.network.packet.clientbound.PacketTriggerMobs;
import io.netty.channel.ChannelHandler.Sharable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@Sharable
public class BrainStonePacketHelper {
	public static void sendBrainLightSensorSmokePacket(int dimension, int x, int y, int z) {
		PacketDispatcher.sendToAllAround(
				new PacketSmokeParticle(x, y, z),
				new NetworkRegistry.TargetPoint(dimension, x, y, z, 100));
		//TODO Is 100 the radius in blocks? If so, isn't that a bit excessive, given how far away you need to see particles?
	}

	public static void sendBrainStoneTriggerMobInformationPacketToPlayer(
			EntityPlayer player) {
		BSP.debug("Sending BrainStoneTriggerMobInformation Packet...");

		PacketDispatcher.sendTo(
				new PacketTriggerMobs(BrainStone
						.getServerSideTriggerEntities()),
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

		for(Entity pl:world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(x - radius, y - radius, z - radius, x + radius,
				y + radius, z + radius))){
			if(pl instanceof EntityPlayerMP){
				((EntityPlayerMP)pl).connection.sendPacket(packet);
			}
		}

	}

	// DOCME
	public static void sendPlayerUpdateMovementPacket(EntityPlayer entity,
			double x, double y, double z) {
		final SPacketEntityVelocity packet = new SPacketEntityVelocity(
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
}
