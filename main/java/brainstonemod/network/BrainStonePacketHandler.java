package brainstonemod.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import brainstonemod.BrainStone;
import brainstonemod.common.helper.BSP;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;

@Sharable
public class BrainStonePacketHandler extends
		SimpleChannelInboundHandler<FMLProxyPacket> {
	public static void handleBrainStoneTriggerMobInformationPacket(
			ByteBuf buffer) {
		BSP.debug("Processing BrainStoneTriggerMobInformation Packet...");

		BrainStone
				.setClientSideTiggerEntities(new BrainStoneTriggerMobInformationPacketHelper(
						buffer).getTriggerEntities());

		BSP.debug("Done processing BrainStoneTriggerMobInformation Packet");
	}

	public static void sendBrainStoneTriggerMobInformationPacketToPlayer(
			EntityPlayer player) {
		BSP.debug("Sending BrainStoneTriggerMobInformation Packet...");

		((EntityPlayerMP) player).playerNetServerHandler
				.sendPacket(new C17PacketCustomPayload(
						BrainStone.packetID_BrainStoneTriggerMobInformation,
						(new BrainStoneTriggerMobInformationPacketHelper(
								BrainStone.getServerSideTiggerEntities()))
								.getData()));

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
		// TODO
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

	public static void sendUpdateOptions(TileEntity tileentity) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FMLProxyPacket packet)
			throws Exception {
		if (packet.channel().equals(
				BrainStone.packetID_BrainStoneTriggerMobInformation)) {
			handleBrainStoneTriggerMobInformationPacket(packet.payload());
		}
	}
}
