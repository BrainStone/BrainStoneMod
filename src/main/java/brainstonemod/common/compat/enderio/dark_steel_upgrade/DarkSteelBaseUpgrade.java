package brainstonemod.common.compat.enderio.dark_steel_upgrade;

import brainstonemod.BrainStone;
import crazypants.enderio.base.handler.darksteel.AbstractUpgrade;
import info.loenwind.autoconfig.factory.IValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.item.ItemStack;

public abstract class DarkSteelBaseUpgrade extends AbstractUpgrade {
  @Getter private final String id;
  @Getter private final ItemStack upgradeItem;

  protected DarkSteelBaseUpgrade(ItemStack upgradeItem, String upgradeName, int levelCost) {
    super(
        BrainStone.MOD_ID, upgradeName, KEY_UPGRADE_PREFIX + upgradeName, new IntValue(levelCost));

    this.id = BrainStone.MOD_ID + ':' + upgradeName;
    this.upgradeItem = upgradeItem;
  }

  @RequiredArgsConstructor
  private static class IntValue implements IValue<Integer> {
    private final int value;

    @Override
    public Integer get() {
      return value;
    }
  }
}
