package brainstonemod.network;

import brainstonemod.BrainStone;
import brainstonemod.network.packet.clientbound.PacketCapacitorData;
import brainstonemod.network.packet.clientbound.PacketOverrideClientSettings;
import brainstonemod.network.packet.clientbound.PacketRedoRender;
import brainstonemod.network.packet.clientbound.PacketSmokeParticle;
import brainstonemod.network.packet.clientbound.PacketSyncChangeDirection;
import brainstonemod.network.packet.clientbound.PacketSyncChangeState;
import brainstonemod.network.packet.clientbound.PacketSyncDisableMobs;
import brainstonemod.network.packet.clientbound.PacketSyncEnableMobs;
import brainstonemod.network.packet.clientbound.PacketSyncInvertMobTriggered;
import brainstonemod.network.packet.clientbound.PacketSyncLightSensor;
import brainstonemod.network.packet.clientbound.PacketSyncSetMaxDelay;
import brainstonemod.network.packet.clientbound.PacketSyncSetMobTriggered;
import brainstonemod.network.packet.clientbound.PacketTriggerMobs;
import brainstonemod.network.packet.serverbound.PacketChangeDirection;
import brainstonemod.network.packet.serverbound.PacketChangeState;
import brainstonemod.network.packet.serverbound.PacketDisableMobs;
import brainstonemod.network.packet.serverbound.PacketEnableMobs;
import brainstonemod.network.packet.serverbound.PacketInvertMobTriggered;
import brainstonemod.network.packet.serverbound.PacketLightSensor;
import brainstonemod.network.packet.serverbound.PacketRequestOverrides;
import brainstonemod.network.packet.serverbound.PacketSetMaxDelay;
import brainstonemod.network.packet.serverbound.PacketSetMobTriggered;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author coolAlias
 * @author The_Fireplace
 */
@SuppressWarnings("unchecked")
public class PacketDispatcher {
	private static byte packetId = 0;
	private static final SimpleNetworkWrapper dispatcher = NetworkRegistry.INSTANCE.newSimpleChannel(BrainStone.MOD_ID);

	public static final void registerPackets() {
		registerMessage(PacketOverrideClientSettings.Handler.class, PacketOverrideClientSettings.class, Side.CLIENT);
		registerMessage(PacketRequestOverrides.Handler.class, PacketRequestOverrides.class, Side.SERVER);
		registerMessage(PacketSmokeParticle.Handler.class, PacketSmokeParticle.class, Side.CLIENT);
		registerMessage(PacketCapacitorData.Handler.class, PacketCapacitorData.class, Side.CLIENT);
		registerMessage(PacketRedoRender.Handler.class, PacketRedoRender.class, Side.CLIENT);
		registerMessage(PacketTriggerMobs.Handler.class, PacketTriggerMobs.class, Side.CLIENT);
		registerMessage(PacketLightSensor.Handler.class, PacketLightSensor.class, Side.SERVER);
		registerMessage(PacketSyncLightSensor.Handler.class, PacketSyncLightSensor.class, Side.CLIENT);
		registerMessage(PacketChangeDirection.Handler.class, PacketChangeDirection.class, Side.SERVER);
		registerMessage(PacketSyncChangeDirection.Handler.class, PacketSyncChangeDirection.class, Side.CLIENT);
		registerMessage(PacketChangeState.Handler.class, PacketChangeState.class, Side.SERVER);
		registerMessage(PacketSyncChangeState.Handler.class, PacketSyncChangeState.class, Side.CLIENT);
		registerMessage(PacketInvertMobTriggered.Handler.class, PacketInvertMobTriggered.class, Side.SERVER);
		registerMessage(PacketSyncInvertMobTriggered.Handler.class, PacketSyncInvertMobTriggered.class, Side.CLIENT);
		registerMessage(PacketSetMaxDelay.Handler.class, PacketSetMaxDelay.class, Side.SERVER);
		registerMessage(PacketSyncSetMaxDelay.Handler.class, PacketSyncSetMaxDelay.class, Side.CLIENT);
		registerMessage(PacketSetMobTriggered.Handler.class, PacketSetMobTriggered.class, Side.SERVER);
		registerMessage(PacketSyncSetMobTriggered.Handler.class, PacketSyncSetMobTriggered.class, Side.CLIENT);
		registerMessage(PacketDisableMobs.Handler.class, PacketDisableMobs.class, Side.SERVER);
		registerMessage(PacketSyncDisableMobs.Handler.class, PacketSyncDisableMobs.class, Side.CLIENT);
		registerMessage(PacketEnableMobs.Handler.class, PacketEnableMobs.class, Side.SERVER);
		registerMessage(PacketSyncEnableMobs.Handler.class, PacketSyncEnableMobs.class, Side.CLIENT);
	}

	/**
	 * Registers a packet
	 *
	 * @param handlerClass
	 *            The packet recieved handler
	 * @param messageClass
	 *            The packet class
	 * @param side
	 *            The side the packet will be sent to
	 */
	@SuppressWarnings("rawtypes")
	private static final void registerMessage(Class handlerClass, Class messageClass, Side side) {
		PacketDispatcher.dispatcher.registerMessage(handlerClass, messageClass, packetId++, side);
	}

	// Wrapper methods
	public static final void sendTo(IMessage message, EntityPlayerMP player) {
		PacketDispatcher.dispatcher.sendTo(message, player);
	}

	public static final void sendToAll(IMessage message) {
		PacketDispatcher.dispatcher.sendToAll(message);
	}

	public static final void sendToAllAround(IMessage message, NetworkRegistry.TargetPoint point) {
		PacketDispatcher.dispatcher.sendToAllAround(message, point);
	}

	public static final void sendToAllAround(IMessage message, int dimension, double x, double y, double z,
			double range) {
		PacketDispatcher.dispatcher.sendToAllAround(message,
				new NetworkRegistry.TargetPoint(dimension, x, y, z, range));
	}

	public static final void sendToAllAround(IMessage message, EntityPlayer player, double range) {
		PacketDispatcher.dispatcher.sendToAllAround(message, new NetworkRegistry.TargetPoint(
				player.world.provider.getDimension(), player.posX, player.posY, player.posZ, range));
	}

	public static final void sendToDimension(IMessage message, int dimensionId) {
		PacketDispatcher.dispatcher.sendToDimension(message, dimensionId);
	}

	public static final void sendToServer(IMessage message) {
		PacketDispatcher.dispatcher.sendToServer(message);
	}
}
