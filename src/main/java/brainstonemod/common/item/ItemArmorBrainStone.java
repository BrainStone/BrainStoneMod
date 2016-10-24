package brainstonemod.common.item;

import brainstonemod.BrainStone;
import net.minecraft.block.Block;
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
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
		if (stack.getItem() == BrainStone.brainStoneBoots() || stack.getItem() == BrainStone.brainStoneHelmet() || stack.getItem() == BrainStone.brainStonePlate())
			return BrainStone.MOD_ID + ":textures/armor/brainstone_armor_1.png";
		else if (stack.getItem() == BrainStone.brainStoneLeggings())
			return BrainStone.MOD_ID + ":textures/armor/brainstone_armor_2.png";
		else if (stack.getItem() == BrainStone.stablePulsatingBrainStoneBoots() || stack.getItem() == BrainStone.stablePulsatingBrainStoneHelmet() || stack.getItem() == BrainStone.stablePulsatingBrainStonePlate())
			return BrainStone.MOD_ID + ":textures/armor/stablepulsatingbs_armor_1.png";
		else if (stack.getItem() == BrainStone.stablePulsatingBrainStoneLeggings())
			return BrainStone.MOD_ID + ":textures/armor/stablepulsatingbs_armor_2.png";

		return null;
	}

	@Override
	public boolean getIsRepairable(ItemStack tool, ItemStack material) {
		return Block.getBlockFromItem(material.getItem()) == BrainStone.brainStone();
	}
}