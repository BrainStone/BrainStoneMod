package brainstonemod.network;

import brainstonemod.BrainStone;
import brainstonemod.common.helper.BSP;
import brainstonemod.network.packet.clientbound.PacketRedoRender;
import brainstonemod.network.packet.clientbound.PacketSmokeParticle;
import brainstonemod.network.packet.clientbound.PacketTriggerMobs;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.channel.ChannelHandler.Sharable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.world.World;

@Sharable
public class BrainStonePacketHelper {
	public static void sendBrainLightSensorSmokePacket(int dimension, int x, int y, int z) {
		PacketDispatcher.sendToAllAround(new PacketSmokeParticle(x, y, z),
				new NetworkRegistry.TargetPoint(dimension, x, y, z, 100));
		// Is 100 the radius in blocks? If so, isn't that a bit excessive, given
		// how far away you need to see particles?
	}

	public static void sendBrainStoneTriggerMobInformationPacketToPlayer(EntityPlayer player) {
		BSP.debug("Sending BrainStoneTriggerMobInformation Packet...");

		PacketDispatcher.sendTo(new PacketTriggerMobs(BrainStone.getServerSideTiggerEntities()),
				(EntityPlayerMP) player);

		BSP.debug("Done sending BrainStoneTriggerMobInformation Packet");
	}

	// DOCME
	public static void sendPlayerUpdateMovementPacket(EntityPlayer entity, double x, double y, double z) {
		final S12PacketEntityVelocity packet = new S12PacketEntityVelocity(entity.getEntityId(), x, y, z);

		PacketDispatcher.sendToAll((IMessage) packet);
	}

	// DOCME
	public static void sendReRenderBlockAtPacket(int dimension, int x, int y, int z, World world) {
		PacketDispatcher.sendToAllAround(new PacketRedoRender(x, y, z),
				new NetworkRegistry.TargetPoint(dimension, x, y, z, 100));
	}
}
