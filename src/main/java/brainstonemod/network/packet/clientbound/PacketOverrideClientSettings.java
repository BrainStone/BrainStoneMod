package brainstonemod.network.packet.clientbound;

import brainstonemod.client.config.BrainStoneClientConfigWrapper;
import io.netty.buffer.ByteBuf;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketOverrideClientSettings implements IMessage {
  private Map<String, Object> values;

  public PacketOverrideClientSettings() {}

  public PacketOverrideClientSettings(Map<String, Object> values) {
    this.values = values;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    NBTTagCompound tag = ByteBufUtils.readTag(buf);
    String type;
    values = new HashMap<>();

    for (String name : tag.getKeySet()) {
      type = NBTBase.NBT_TYPES[tag.getTag(name).getId()];

      if ("BYTE".equals(type)) {
        values.put(name, tag.getBoolean(name));
      } else if ("SHORT".equals(type)) {
        values.put(name, tag.getShort(name));
      } else if ("INT".equals(type)) {
        values.put(name, tag.getInteger(name));
      } else if ("LONG".equals(type)) {
        values.put(name, tag.getLong(name));
      } else if ("FLOAT".equals(type)) {
        values.put(name, tag.getFloat(name));
      } else if ("DOUBLE".equals(type)) {
        values.put(name, tag.getDouble(name));
      } else if ("STRING".equals(type)) {
        values.put(name, tag.getString(name));
      } else if ("BYTE[]".equals(type)) {
        values.put(name, tag.getByteArray(name));
      } else if ("INT[]".equals(type)) {
        values.put(name, tag.getIntArray(name));
      } else
        throw new IllegalArgumentException(
            "Unsupported type: \"" + type + "\" while trying to parse NBT");
    }
  }

  @Override
  public void toBytes(ByteBuf buf) {
    NBTTagCompound tag = new NBTTagCompound();
    String name;
    Object obj;

    for (Entry<String, Object> value : values.entrySet()) {
      name = value.getKey();
      obj = value.getValue();

      if (obj instanceof Boolean) {
        tag.setBoolean(name, (Boolean) obj);
      } else if (obj instanceof Short) {
        tag.setShort(name, (Short) obj);
      } else if (obj instanceof Integer) {
        tag.setInteger(name, (Integer) obj);
      } else if (obj instanceof Long) {
        tag.setLong(name, (Long) obj);
      } else if (obj instanceof Float) {
        tag.setFloat(name, (Float) obj);
      } else if (obj instanceof Double) {
        tag.setDouble(name, (Double) obj);
      } else if (obj instanceof String) {
        tag.setString(name, (String) obj);
      } else if (obj instanceof byte[]) {
        tag.setByteArray(name, (byte[]) obj);
      } else if (obj instanceof int[]) {
        tag.setIntArray(name, (int[]) obj);
      } else
        throw new IllegalArgumentException(
            "Unsupported type: \""
                + obj.getClass().getName()
                + "\" while trying to add value to NBT");
    }

    ByteBufUtils.writeTag(buf, tag);
  }

  public static class Handler extends AbstractClientMessageHandler<PacketOverrideClientSettings> {
    @Override
    public IMessage handleClientMessage(
        EntityPlayer player, PacketOverrideClientSettings message, MessageContext ctx) {
      BrainStoneClientConfigWrapper.setOverrideValues(message.values);

      return null;
    }
  }
}
