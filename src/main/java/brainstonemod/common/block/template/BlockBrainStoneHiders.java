package brainstonemod.common.block.template;

import brainstonemod.BrainStone;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;

public abstract class BlockBrainStoneHiders extends BlockBrainStoneContainerBase {
	public BlockBrainStoneHiders() {
		super(Material.ROCK);

		setCreativeTab(BrainStone.getCreativeTab(CreativeTabs.REDSTONE));
	}

	@Override
	public boolean canProvidePower(IBlockState state) {
		return true;
	}
}