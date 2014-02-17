package brainstonemod.common.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemHoe;
import brainstonemod.BrainStone;

public class ItemHoeBrainStone extends ItemHoe {
	public ItemHoeBrainStone(int i, EnumToolMaterial enumtoolmaterial) {
		super(BrainStone.getId(BrainStone.startItemId + i), enumtoolmaterial);

		setCreativeTab(CreativeTabs.tabTools);
	}

	@Override
	public void registerIcons(IconRegister par1IconRegister) {
		itemIcon = par1IconRegister.registerIcon("brainstonemod:"
				+ this.getUnlocalizedName().replaceFirst("item.", ""));
	}
}
