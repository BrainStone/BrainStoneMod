package brainstonemod.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import net.minecraft.network.PacketBuffer;
import brainstonemod.common.helper.BSP;

public class BrainStoneTriggerMobInformationPacketHelper {
	private LinkedHashMap<String, Class<?>[]> triggerEntities;
	private ByteBuf data;

	public BrainStoneTriggerMobInformationPacketHelper(ByteBuf data) {
		try {
			readPacketData(new PacketBuffer(data));
		} catch (final IOException e) {
			BSP.warnException(e);
		}
	}

	public BrainStoneTriggerMobInformationPacketHelper(
			LinkedHashMap<String, Class<?>[]> triggerEntities) {
		this.triggerEntities = triggerEntities;

		data = Unpooled.buffer(1024);
		try {
			generatePacketData(new PacketBuffer(data));
		} catch (final IOException e) {
			BSP.warnException(e);
		}
	}

	private void generatePacketData(PacketBuffer buffer) throws IOException {
		Class<?>[] entities;

		buffer.writeInt(triggerEntities.size());

		for (final Entry<String, Class<?>[]> pair : triggerEntities.entrySet()) {
			entities = pair.getValue();

			buffer.writeStringToBuffer(pair.getKey());

			buffer.writeInt(entities.length);

			for (final Class<?> entity : entities) {
				buffer.writeStringToBuffer(entity.getName());
			}
		}
	}

	public ByteBuf getData() {
		return data;
	}

	public LinkedHashMap<String, Class<?>[]> getTriggerEntities() {
		return triggerEntities;
	}

	private void readPacketData(PacketBuffer buffer) throws IOException {
		final int size = buffer.readInt();
		int entitiesSize;
		int i, j;
		String key;
		Class<?>[] entities;

		triggerEntities = new LinkedHashMap<String, Class<?>[]>(size);

		for (i = 0; i < size; i++) {
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
		}
	}
}