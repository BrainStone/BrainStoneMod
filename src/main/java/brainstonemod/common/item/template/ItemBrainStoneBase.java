package brainstonemod.common.item.template;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemBrainStoneBase extends Item {
  private final EnumRarity customRarity;

  public ItemBrainStoneBase(EnumRarity customRarity) {
    super();

    this.customRarity = customRarity;
  }

  @Override
  public EnumRarity getRarity(ItemStack stack) {
    if (customRarity == null) return super.getRarity(stack);
    else return customRarity;
  }
}
