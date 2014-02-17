package brainstonemod.common.item;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import brainstonemod.BrainStone;

public class ItemArmorBrainStone extends ItemArmor {
	public ItemArmorBrainStone(int i, EnumArmorMaterial armorMaterial,
			int par3, int par4) {
		super(BrainStone.getId(BrainStone.startItemId + i), armorMaterial,
				par3, par4);
	}

	@Override
	public String getArmorTexture(ItemStack itemstack, Entity entity, int slot,
			int layer) {
		if (itemstack.itemID == BrainStone.brainStoneLeggings().itemID)
			return "brainstonemod:" + BrainStone.armorPath
					+ "brainstone_armor_2.png";

		return "brainstonemod:" + BrainStone.armorPath
				+ "brainstone_armor_1.png";
	}

	@Override
	public boolean getIsRepairable(ItemStack tool, ItemStack material) {
		return material.itemID == BrainStone.brainStone().blockID;
	}

	@Override
	public void registerIcons(IconRegister par1IconRegister) {
		itemIcon = par1IconRegister.registerIcon("brainstonemod:"
				+ this.getUnlocalizedName().replaceFirst("item.", ""));
	}
}