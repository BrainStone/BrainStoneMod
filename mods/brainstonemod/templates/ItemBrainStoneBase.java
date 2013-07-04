package mods.brainstonemod.templates;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;

public class ItemBrainStoneBase extends Item {

	public ItemBrainStoneBase(int par1) {
		super(par1);
	}

	@Override
	public void registerIcons(IconRegister par1IconRegister) {
		itemIcon = par1IconRegister.registerIcon("BrainStoneMod:"
				+ this.getUnlocalizedName().replaceFirst("item.", ""));
	}
}