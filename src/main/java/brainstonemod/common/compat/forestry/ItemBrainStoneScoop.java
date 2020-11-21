package brainstonemod.common.compat.forestry;

import brainstonemod.BrainStone;
import forestry.api.core.Tabs;
import forestry.core.items.ItemScoop;

public class ItemBrainStoneScoop extends ItemScoop {
  public ItemBrainStoneScoop(ToolMaterial toolMaterial) {
    setHarvestLevel("scoop", toolMaterial.getHarvestLevel());
    setMaxDamage(toolMaterial.getMaxUses() / 100);
    setEfficiencyOnProperMaterial(toolMaterial.getEfficiency());

    setCreativeTab(BrainStone.getCreativeTab(Tabs.tabApiculture));
  }
}
