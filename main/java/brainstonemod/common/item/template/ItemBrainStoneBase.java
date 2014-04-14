package brainstonemod.common.item.template;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;

public class ItemBrainStoneBase extends Item {
	@Override
	public void registerIcons(IIconRegister par1IconRegister) {
		itemIcon = par1IconRegister.registerIcon("brainstonemod:"
				+ this.getUnlocalizedName().replaceFirst("item.", ""));
	}
}