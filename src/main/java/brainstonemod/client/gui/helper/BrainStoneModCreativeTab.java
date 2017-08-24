package brainstonemod.client.gui.helper;

import brainstonemod.BrainStone;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BrainStoneModCreativeTab extends CreativeTabs {
	private ItemStack iconItem = null;
	
	public BrainStoneModCreativeTab() {
		super("tab" + BrainStone.MOD_ID);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ItemStack getTabIconItem() {
		if(iconItem == null) {
			iconItem = new ItemStack(BrainStone.brainStone());
		}
		
		return iconItem;
	}
}
