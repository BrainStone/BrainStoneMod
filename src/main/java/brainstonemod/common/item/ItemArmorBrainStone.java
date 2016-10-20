package brainstonemod.common.item;

import brainstonemod.BrainStone;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class ItemArmorBrainStone extends ItemArmor {
	public ItemArmorBrainStone(ArmorMaterial armorMaterial, int par3, int slot) {
		super(armorMaterial, par3, slot);

		setCreativeTab(BrainStone.getCreativeTab(CreativeTabs.tabCombat));
	}

	@Override
	public String getArmorTexture(ItemStack itemstack, Entity entity, int slot, String type) {
		ItemArmor armor = (ItemArmor) itemstack.getItem();
		String path = BrainStone.RESOURCE_PREFIX + BrainStone.armorPath + armor.getArmorMaterial().name().toLowerCase();

		if (armor.armorType == 2)
			return path + "_armor_2.png";

		return path + "_armor_1.png";
	}

	@Override
	public boolean getIsRepairable(ItemStack tool, ItemStack material) {
		return Block.getBlockFromItem(material.getItem()) == BrainStone.brainStone();
	}

	@Override
	public void registerIcons(IIconRegister par1IconRegister) {
		itemIcon = par1IconRegister
				.registerIcon("brainstonemod:" + this.getUnlocalizedName().replaceFirst("item.", ""));
	}
}