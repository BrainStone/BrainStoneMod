package brainstonemod.client.gui.helper;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import brainstonemod.BrainStone;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class BrainStoneModCreativeTab extends CreativeTabs {
	public BrainStoneModCreativeTab() {
		super("tab" + BrainStone.MOD_ID);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getTabIconItem() {
		return Item.getItemFromBlock(BrainStone.brainStone());
	}
}
