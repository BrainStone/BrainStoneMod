package brainstonemod.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import brainstonemod.BrainStone;
import brainstonemod.common.helper.BSP;
import brainstonemod.common.helper.BrainStoneClassFinder;
import brainstonemod.network.packet.template.BrainStoneBasePacket;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Packet pipeline class. Directs all registered packet data to be handled by
 * the packets themselves.
 * 
 * @author <b>BrainStone</b>, adapted code from <b>sirgingalot</b> and some code
 *         from <b>cpw</b>
 */
@Sharable
public class BrainStonePacketPipeline extends
		MessageToMessageCodec<FMLProxyPacket, BrainStoneBasePacket> {
	private EnumMap<Side, FMLEmbeddedChannel> channels;
	private final LinkedList<Class<? extends BrainStoneBasePacket>> packets = new LinkedList<Class<? extends BrainStoneBasePacket>>();
	private boolean isPostInitialised = false;
	private final boolean autoRegister;

	public BrainStonePacketPipeline() {
		this(true);
	}

	public BrainStonePacketPipeline(boolean autoRegister) {
		this.autoRegister = autoRegister;
	}

	/**
	 * This method automatically registers all packets found in
	 * brainstonemod.network.packets
	 */
	private void autoRegisterPackets() {
		try {
			for (final Class clazz : BrainStoneClassFinder
					.getClassesForPackage(getClass().getPackage().getName()
							+ ".packet")) {
				BSP.info(clazz.getName());

				registerPacket(clazz, true);
			}
		} catch (final ClassNotFoundException e) {
			BSP.throwException(new RuntimeException(
					"Unexpechted failure while trying to automatically register all packets.",
					e));
		}
	}

	/** In line decoding and handling of the packet */
	@Override
	protected void decode(ChannelHandlerContext ctx, FMLProxyPacket msg,
			List<Object> out) throws Exception {
		final ByteBuf payload = msg.payload();
		final byte discriminator = payload.readByte();
		final Class<? extends BrainStoneBasePacket> clazz = packets
				.get(discriminator);
		if (clazz == null)
			throw new NullPointerException(
					"No packet registered for discriminator: " + discriminator);

		final BrainStoneBasePacket pkt = clazz.newInstance();
		pkt.decodeInto(ctx, payload.slice());

		EntityPlayer player;
		switch (FMLCommonHandler.instance().getEffectiveSide()) {
		case CLIENT:
			player = getClientPlayer();
			pkt.handleClientSide(player);
			break;

		case SERVER:
			final INetHandler netHandler = ctx.channel()
					.attr(NetworkRegistry.NET_HANDLER).get();
			player = ((NetHandlerPlayServer) netHandler).playerEntity;
			pkt.handleServerSide(player);
			break;

		default:
		}

		out.add(pkt);
	}

	/** In line encoding of the packet, including discriminator setting */
	@Override
	protected void encode(ChannelHandlerContext ctx, BrainStoneBasePacket msg,
			List<Object> out) throws Exception {
		final ByteBuf buffer = Unpooled.buffer();
		final Class<? extends BrainStoneBasePacket> clazz = msg.getClass();
		if (!packets.contains(msg.getClass()))
			throw new NullPointerException("No Packet Registered for: "
					+ msg.getClass().getCanonicalName());

		final byte discriminator = (byte) packets.indexOf(clazz);
		buffer.writeByte(discriminator);
		msg.encodeInto(ctx, buffer);
		final FMLProxyPacket proxyPacket = new FMLProxyPacket(buffer.copy(),
				ctx.channel().attr(NetworkRegistry.FML_CHANNEL).get());
		out.add(proxyPacket);
	}

	@SideOnly(Side.CLIENT)
	private EntityPlayer getClientPlayer() {
		return Minecraft.getMinecraft().thePlayer;
	}

	/** Method to call from FMLInitializationEvent */
	public void initialise() {
		channels = NetworkRegistry.INSTANCE.newChannel(BrainStone.MOD_ID, this);
	}

	/**
	 * Method to call from FMLPostInitializationEvent Ensures that packet
	 * discriminators are common between server and client by using logical
	 * sorting
	 */
	public void postInitialise() {
		if (isPostInitialised)
			return;

		if (autoRegister) {
			autoRegisterPackets();
		}

		isPostInitialised = true;

		Collections.sort(packets,
				new Comparator<Class<? extends BrainStoneBasePacket>>() {

					@Override
					public int compare(
							Class<? extends BrainStoneBasePacket> clazz1,
							Class<? extends BrainStoneBasePacket> clazz2) {
						int com = String.CASE_INSENSITIVE_ORDER.compare(
								clazz1.getCanonicalName(),
								clazz2.getCanonicalName());
						if (com == 0) {
							com = clazz1.getCanonicalName().compareTo(
									clazz2.getCanonicalName());
						}

						return com;
					}
				});
	}

	/**
	 * Register your packet with the pipeline. Discriminators are automatically
	 * set.
	 * 
	 * @param clazz
	 *            the class to register
	 * 
	 * @return whether registration was successful. Failure may occur if 256
	 *         packets have been registered or if the registry already contains
	 *         this packet
	 */
	public boolean registerPacket(Class<? extends BrainStoneBasePacket> clazz) {
		return registerPacket(clazz, false);
	}

	/**
	 * Register your packet with the pipeline. Discriminators are automatically
	 * set.<br>
	 * This method registers the packet if it is registered internally or if
	 * auto regsiter mode ist off.
	 * 
	 * @param clazz
	 *            the class to register
	 * @param internalCall
	 *            Whether this is a internal call or not
	 * 
	 * @return whether registration was successful. Failure may occur if 256
	 *         packets have been registered or if the registry already contains
	 *         this packet
	 */
	private boolean registerPacket(Class<? extends BrainStoneBasePacket> clazz,
			boolean internalCall) {
		if (!internalCall && autoRegister) {
			BSP.fatal("This pipeline is in auto register mode.");
			BSP.info("You are not allowed to manually register packets!");

			return false;
		}

		if (packets.size() > 256) {
			BSP.fatal("Too many packets registered!");

			return false;
		}

		if (packets.contains(clazz)) {
			BSP.error("Packet \"" + clazz.getName() + "\" already registered!");

			return false;
		}

		if (isPostInitialised) {
			BSP.fatal("This pipeline is already post initialized!");
			BSP.info("Register your packets before you post initialize your pipeline.");

			return false;
		}

		packets.add(clazz);
		return true;
	}

	/**
	 * Send this message to the specified player. <br>
	 * Adapted from CPW's code in
	 * cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
	 * 
	 * @param message
	 *            The message to send
	 * @param player
	 *            The player to send it to
	 */
	public void sendTo(BrainStoneBasePacket message, EntityPlayerMP player) {
		channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET)
				.set(FMLOutboundHandler.OutboundTarget.PLAYER);
		channels.get(Side.SERVER)
				.attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
		channels.get(Side.SERVER).writeAndFlush(message);
	}

	/**
	 * Send this message to everyone. <br>
	 * Adapted from CPW's code in
	 * cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
	 * 
	 * @param message
	 *            The message to send
	 */
	public void sendToAll(BrainStoneBasePacket message) {
		channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET)
				.set(FMLOutboundHandler.OutboundTarget.ALL);
		channels.get(Side.SERVER).writeAndFlush(message);
	}

	/**
	 * Send this message to everyone within a certain range of a point. <br>
	 * Adapted from CPW's code in
	 * cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
	 * 
	 * @param message
	 *            The message to send
	 * @param point
	 *            The
	 *            {@link cpw.mods.fml.common.network.NetworkRegistry.TargetPoint}
	 *            around which to send
	 */
	public void sendToAllAround(BrainStoneBasePacket message,
			NetworkRegistry.TargetPoint point) {
		channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET)
				.set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
		channels.get(Side.SERVER)
				.attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(point);
		channels.get(Side.SERVER).writeAndFlush(message);
	}

	/**
	 * Send this message to everyone within the supplied dimension. <br>
	 * Adapted from CPW's code in
	 * cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
	 * 
	 * @param message
	 *            The message to send
	 * @param dimensionId
	 *            The dimension id to target
	 */
	public void sendToDimension(BrainStoneBasePacket message, int dimensionId) {
		channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET)
				.set(FMLOutboundHandler.OutboundTarget.DIMENSION);
		channels.get(Side.SERVER)
				.attr(FMLOutboundHandler.FML_MESSAGETARGETARGS)
				.set(dimensionId);
		channels.get(Side.SERVER).writeAndFlush(message);
	}

	/**
	 * Send this message to the server. <br>
	 * Adapted from CPW's code in
	 * cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
	 * 
	 * @param message
	 *            The message to send
	 */
	public void sendToServer(BrainStoneBasePacket message) {
		channels.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET)
				.set(FMLOutboundHandler.OutboundTarget.TOSERVER);
		channels.get(Side.CLIENT).writeAndFlush(message);
	}
}
