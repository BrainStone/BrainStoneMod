package brainstonemod.common.item.template;

import net.minecraft.item.Item;

public class ItemBrainStoneBase extends Item {

	public ItemBrainStoneBase(int par1) {
		super(par1);
	}

	@Override
	public void registerIcons(IconRegister par1IconRegister) {
		itemIcon = par1IconRegister.registerIcon("brainstonemod:"
				+ this.getUnlocalizedName().replaceFirst("item.", ""));
	}
}