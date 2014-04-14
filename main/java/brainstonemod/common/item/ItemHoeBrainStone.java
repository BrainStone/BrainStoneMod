package brainstonemod.common.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemHoe;

public class ItemHoeBrainStone extends ItemHoe {
	public ItemHoeBrainStone(ToolMaterial enumtoolmaterial) {
		super(enumtoolmaterial);

		setCreativeTab(CreativeTabs.tabTools);
	}

	@Override
	public void registerIcons(IIconRegister par1IconRegister) {
		itemIcon = par1IconRegister.registerIcon("brainstonemod:"
				+ this.getUnlocalizedName().replaceFirst("item.", ""));
	}
}
