package brainstonemod.common.item;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import brainstonemod.BrainStone;

public class ItemSwordBrainStone extends ItemSword {
	public ItemSwordBrainStone(ToolMaterial enumtoolmaterial) {
		super(enumtoolmaterial);

		setCreativeTab(BrainStone.getCreativeTab(CreativeTabs.tabCombat));
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