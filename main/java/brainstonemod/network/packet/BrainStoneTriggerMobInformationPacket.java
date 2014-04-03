package brainstonemod.network.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import brainstonemod.BrainStone;
import brainstonemod.common.helper.BSP;
import brainstonemod.network.packet.template.BrainStoneToClientBasePacket;

public class BrainStoneTriggerMobInformationPacket extends BrainStoneToClientBasePacket {
	private LinkedHashMap<String, Class<?>[]> triggerEntities;

	public BrainStoneTriggerMobInformationPacket() {
	}

	public BrainStoneTriggerMobInformationPacket(
			LinkedHashMap<String, Class<?>[]> triggerEntities) {
		this.triggerEntities = triggerEntities;
	}

	@Override
	public void generatePacketData(ChannelHandlerContext ctx, ByteBuf data) {
		final PacketBuffer buffer = new PacketBuffer(data);

		Class<?>[] entities;

		buffer.writeInt(triggerEntities.size());

		for (final Entry<String, Class<?>[]> pair : triggerEntities.entrySet()) {
			try {
				entities = pair.getValue();

				buffer.writeStringToBuffer(pair.getKey());

				buffer.writeInt(entities.length);

				for (final Class<?> entity : entities) {
					buffer.writeStringToBuffer(entity.getName());
				}
			} catch (final IOException e) {
				BSP.warnException(e);
			}
		}
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		BSP.debug("Handling BrainStoneTriggerMobInformation Packet...");

		BrainStone.setClientSideTiggerEntities(triggerEntities);

		BSP.debug("Done handling BrainStoneTriggerMobInformation Packet");
	}

	@Override
	public void processPacketData(ChannelHandlerContext ctx, ByteBuf data) {
		final PacketBuffer buffer = new PacketBuffer(data);

		final int size = buffer.readInt();
		int entitiesSize;
		int i, j;
		String key;
		Class<?>[] entities;

		triggerEntities = new LinkedHashMap<String, Class<?>[]>(size);

		for (i = 0; i < size; i++) {
			try {
				key = buffer.readStringFromBuffer(1024);

				entitiesSize = buffer.readInt();
				entities = new Class<?>[entitiesSize];

				for (j = 0; j < entitiesSize; j++) {
					try {
						entities[j] = Class.forName(buffer
								.readStringFromBuffer(1024));
					} catch (final ClassNotFoundException e) {
						BSP.warnException(e);
					}
				}

				triggerEntities.put(key, entities);
			} catch (final IOException e) {
				BSP.warnException(e);
			}
		}
	}
}