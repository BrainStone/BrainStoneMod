package mods.brainstone.items;

import mods.brainstone.BrainStone;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.IArmorTextureProvider;

public class ItemArmorBrainStone extends ItemArmor implements
		IArmorTextureProvider {
	public ItemArmorBrainStone(int i, EnumArmorMaterial armorMaterial,
			int par3, int par4) {
		super(BrainStone.getId(BrainStone.startItemId + i), armorMaterial,
				par3, par4);
	}

	@Override
	public String getArmorTextureFile(ItemStack itemstack) {
		if (itemstack.itemID == BrainStone.brainStoneLeggings().itemID)
			return BrainStone.armorPath + "brainstone_armor_2.png";

		return BrainStone.armorPath + "brainstone_armor_1.png";
	}

	@Override
	public boolean getIsRepairable(ItemStack tool, ItemStack material) {
		return material.itemID == BrainStone.brainStone().blockID;
	}

	@Override
	public void registerIcons(IconRegister par1IconRegister) {
		itemIcon = par1IconRegister.registerIcon("brainstone:"
				+ this.getUnlocalizedName().replaceFirst("item.", ""));
	}
}