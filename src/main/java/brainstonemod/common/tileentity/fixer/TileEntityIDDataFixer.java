package brainstonemod.common.tileentity.fixer;

import brainstonemod.BrainStone;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class TileEntityIDDataFixer implements IFixableData {
  public static final int VERSION = 1;
  private final Map<String, String> idsToFix;

  public TileEntityIDDataFixer() {
    idsToFix =
        ImmutableMap.<String, String>builder()
            .put(
                "minecraft:tileentityblockbrainlightsensor",
                BrainStone.RESOURCE_PREFIX + "brain_light_sensor")
            .put(
                "minecraft:tileentityblockbrainstonetrigger",
                BrainStone.RESOURCE_PREFIX + "brain_stone_trigger")
            .build();
  }

  @Override
  public NBTTagCompound fixTagCompound(NBTTagCompound tag) {
    String tileEntityID = tag.getString("id");

    // only change value if tileEntityID is in the map
    if (idsToFix.containsKey(tileEntityID)) tag.setString("id", idsToFix.get(tileEntityID));

    return tag;
  }

  @Override
  public int getFixVersion() {
    return VERSION;
  }
}
