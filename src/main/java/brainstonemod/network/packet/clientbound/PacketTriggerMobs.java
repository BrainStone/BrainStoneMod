package brainstonemod.network.packet.clientbound;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

import brainstonemod.BrainStone;
import brainstonemod.common.helper.BSP;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketTriggerMobs implements IMessage {
	private LinkedHashMap<String, Class<?>[]> triggerEntities;

	public PacketTriggerMobs() {
	}

	public PacketTriggerMobs(LinkedHashMap<String, Class<?>[]> triggerEntities) {
		this.triggerEntities = triggerEntities;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		final int size = buf.readInt();
		int entitiesSize;
		int i, j;
		String key;
		Class<?>[] entities;

		triggerEntities = new LinkedHashMap<>(size);

		for (i = 0; i < size; i++) {
			key = ByteBufUtils.readUTF8String(buf);

			entitiesSize = buf.readInt();
			entities = new Class<?>[entitiesSize];

			for (j = 0; j < entitiesSize; j++) {
				try {
					entities[j] = Class.forName(ByteBufUtils.readUTF8String(buf));
				} catch (final ClassNotFoundException e) {
					BSP.warnException(e);
				}
			}

			triggerEntities.put(key, entities);
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		Class<?>[] entities;

		buf.writeInt(triggerEntities.size());

		for (final Entry<String, Class<?>[]> pair : triggerEntities.entrySet()) {
			entities = pair.getValue();

			ByteBufUtils.writeUTF8String(buf, pair.getKey());

			buf.writeInt(entities.length);

			for (final Class<?> entity : entities) {
				ByteBufUtils.writeUTF8String(buf, entity.getName());
			}
		}
	}

	public static class Handler extends AbstractClientMessageHandler<PacketTriggerMobs> {
		@Override
		public IMessage handleClientMessage(EntityPlayer player, PacketTriggerMobs message, MessageContext ctx) {
			BSP.debug("Handling BrainStoneTriggerMobInformation Packet...");
			BrainStone.setClientSideTiggerEntities(message.triggerEntities);
			BSP.debug("Done handling BrainStoneTriggerMobInformation Packet");
			return null;
		}
	}
}