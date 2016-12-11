package brainstonemod.common.block;

import javax.annotation.Nullable;

import brainstonemod.BrainStone;
import brainstonemod.network.BrainStoneGuiHandler;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockBrainStoneAnvil extends BlockAnvil {
	public BlockBrainStoneAnvil() {
		super();

		setHardness(5.0f);
		setSoundType(SoundType.ANVIL);
		setResistance(2000.0f);
		setCreativeTab(BrainStone.getCreativeTab(CreativeTabs.DECORATIONS));
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!worldIn.isRemote) {
			playerIn.openGui(BrainStone.MOD_ID, BrainStoneGuiHandler.ID_BRAIN_STONE_ANVIL, worldIn, pos.getX(),
					pos.getY(), pos.getZ());
		}

		return true;
	}
}
