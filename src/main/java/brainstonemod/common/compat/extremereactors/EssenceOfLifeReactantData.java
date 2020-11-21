package brainstonemod.common.compat.extremereactors;

import erogenousbeef.bigreactors.api.data.ReactantData;
import java.awt.Color;

public class EssenceOfLifeReactantData extends ReactantData {
  public EssenceOfLifeReactantData(String name, int type) {
    this(name, ReactantData.TYPES[type]);
  }

  public EssenceOfLifeReactantData(String name, ReactantType type) {
    super(name, type);
  }

  @Override
  public int getColor() {
    return Color.HSBtoRGB(System.currentTimeMillis() / 12_000.0F, 1.0F, 1.0F);
  }
}
