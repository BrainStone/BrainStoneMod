package brainstonemod.client.gui.helper;

import brainstonemod.BrainStone;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
