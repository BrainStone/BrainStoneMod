package brainstonemod.network;

import brainstonemod.BrainStone;
import brainstonemod.network.packet.*;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

/**
 * @author coolAlias
 * @author The_Fireplace
 */
public class PacketDispatcher {
    private static byte packetId = 0;
    private static final SimpleNetworkWrapper dispatcher = NetworkRegistry.INSTANCE.newSimpleChannel(BrainStone.MOD_ID);

    public static final void registerPackets() {
        registerMessage(PacketSmokeParticle.Handler.class, PacketSmokeParticle.class, Side.CLIENT);
        registerMessage(PacketCapacitorData.Handler.class, PacketCapacitorData.class, Side.CLIENT);
        registerMessage(PacketRedoRender.Handler.class, PacketRedoRender.class, Side.CLIENT);
        registerMessage(PacketSyncNBT.Handler.class, PacketSyncNBT.class, Side.SERVER);
        registerMessage(PacketTriggerMobs.Handler.class, PacketTriggerMobs.class, Side.CLIENT);
    }

    /**
     * Registers a packet
     * @param handlerClass
     *  The packet recieved handler
     * @param messageClass
     *  The packet class
     * @param side
     *  The side the packet will be sent to
     */
    private static final void registerMessage(Class handlerClass, Class messageClass, Side side) {
        PacketDispatcher.dispatcher.registerMessage(handlerClass, messageClass, packetId++, side);
    }

    //Wrapper methods
    public static final void sendTo(IMessage message, EntityPlayerMP player) {
        PacketDispatcher.dispatcher.sendTo(message, player);
    }

    public static final void sendToAll(IMessage message){
        PacketDispatcher.dispatcher.sendToAll(message);
    }

    public static final void sendToAllAround(IMessage message, NetworkRegistry.TargetPoint point) {
        PacketDispatcher.dispatcher.sendToAllAround(message, point);
    }

    public static final void sendToAllAround(IMessage message, int dimension, double x, double y, double z, double range) {
        PacketDispatcher.dispatcher.sendToAllAround(message, new NetworkRegistry.TargetPoint(dimension, x, y, z, range));
    }

    public static final void sendToAllAround(IMessage message, EntityPlayer player, double range) {
        PacketDispatcher.dispatcher.sendToAllAround(message, new NetworkRegistry.TargetPoint(player.worldObj.provider.dimensionId, player.posX, player.posY, player.posZ, range));
    }

    public static final void sendToDimension(IMessage message, int dimensionId) {
        PacketDispatcher.dispatcher.sendToDimension(message, dimensionId);
    }

    public static final void sendToServer(IMessage message) {
        PacketDispatcher.dispatcher.sendToServer(message);
    }
}