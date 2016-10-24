package brainstonemod.common.item;

import brainstonemod.BrainStone;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemHoe;

public class ItemHoeBrainStone extends ItemHoe {
	public ItemHoeBrainStone(ToolMaterial enumtoolmaterial) {
		super(enumtoolmaterial);

		setCreativeTab(BrainStone.getCreativeTab(CreativeTabs.TOOLS));
	}
}
