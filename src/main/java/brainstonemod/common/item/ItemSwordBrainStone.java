package brainstonemod.common.item;

import brainstonemod.BrainStone;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class ItemSwordBrainStone extends ItemSword {
	public ItemSwordBrainStone(ToolMaterial enumtoolmaterial) {
		super(enumtoolmaterial);

		setCreativeTab(BrainStone.getCreativeTab(CreativeTabs.COMBAT));
	}

	@Override
	public boolean getIsRepairable(ItemStack tool, ItemStack material) {
		return Block.getBlockFromItem(material.getItem()) == BrainStone.brainStone();
	}
}