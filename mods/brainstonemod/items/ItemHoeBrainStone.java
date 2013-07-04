package mods.brainstonemod.items;

import mods.brainstonemod.BrainStone;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemHoe;

public class ItemHoeBrainStone extends ItemHoe {
	public ItemHoeBrainStone(int i, EnumToolMaterial enumtoolmaterial) {
		super(BrainStone.getId(BrainStone.startItemId + i), enumtoolmaterial);

		this.setCreativeTab(CreativeTabs.tabTools);
	}

	@Override
	public void registerIcons(IconRegister par1IconRegister) {
		itemIcon = par1IconRegister.registerIcon("brainstone:"
				+ this.getUnlocalizedName().replaceFirst("item.", ""));
	}
}
