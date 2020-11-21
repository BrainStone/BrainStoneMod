package brainstonemod.common.item;

import brainstonemod.BrainStone;
import brainstonemod.BrainStoneItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class ItemArmorBrainStone extends ItemArmor {
  public ItemArmorBrainStone(ArmorMaterial armorMaterial, EntityEquipmentSlot slot) {
    super(armorMaterial, -1, slot);

    setCreativeTab(BrainStone.getCreativeTab(CreativeTabs.COMBAT));
  }

  @Override
  public String getArmorTexture(
      ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
    if ((stack.getItem() == BrainStoneItems.brainStoneBoots())
        || (stack.getItem() == BrainStoneItems.brainStoneHelmet())
        || (stack.getItem() == BrainStoneItems.brainStonePlate()))
      return BrainStone.MOD_ID + ":textures/armor/brainstone_armor_1.png";
    else if (stack.getItem() == BrainStoneItems.brainStoneLeggings())
      return BrainStone.MOD_ID + ":textures/armor/brainstone_armor_2.png";
    else if ((stack.getItem() == BrainStoneItems.stablePulsatingBrainStoneBoots())
        || (stack.getItem() == BrainStoneItems.stablePulsatingBrainStoneHelmet())
        || (stack.getItem() == BrainStoneItems.stablePulsatingBrainStonePlate()))
      return BrainStone.MOD_ID + ":textures/armor/stablepulsatingbs_armor_1.png";
    else if (stack.getItem() == BrainStoneItems.stablePulsatingBrainStoneLeggings())
      return BrainStone.MOD_ID + ":textures/armor/stablepulsatingbs_armor_2.png";

    return null;
  }
}
