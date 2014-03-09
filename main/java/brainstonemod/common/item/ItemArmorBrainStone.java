package brainstonemod.common.item;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import brainstonemod.BrainStone;

public class ItemArmorBrainStone extends ItemArmor {
	public ItemArmorBrainStone(ArmorMaterial armorMaterial, int par3, int par4) {
		super(armorMaterial, par3, par4);
	}

	@Override
	public String getArmorTexture(ItemStack itemstack, Entity entity, int slot,
			String type) {
		if (itemstack.getItem() == BrainStone.brainStoneLeggings())
			return "brainstonemod:" + BrainStone.armorPath
					+ "brainstone_armor_2.png";

		return "brainstonemod:" + BrainStone.armorPath
				+ "brainstone_armor_1.png";
	}

	@Override
	public boolean getIsRepairable(ItemStack tool, ItemStack material) {
		return Block.getBlockFromItem(material.getItem()) == BrainStone
				.brainStone();
	}

	@Override
	public void registerIcons(IIconRegister par1IconRegister) {
		itemIcon = par1IconRegister.registerIcon("brainstonemod:"
				+ this.getUnlocalizedName().replaceFirst("item.", ""));
	}
}