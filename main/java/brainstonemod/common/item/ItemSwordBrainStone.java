package brainstonemod.common.item;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import brainstonemod.BrainStone;

public class ItemSwordBrainStone extends ItemSword {
	public ItemSwordBrainStone(int i, EnumToolMaterial enumtoolmaterial) {
		super(BrainStone.getId(BrainStone.startItemId + i), enumtoolmaterial);
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