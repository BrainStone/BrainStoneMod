package brainstonemod.common.block;

import java.util.Random;

import javax.annotation.Nullable;

import brainstonemod.BrainStone;
import brainstonemod.common.config.BrainStoneConfigWrapper;
import brainstonemod.network.BrainStoneGuiHandler;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockBrainStoneAnvil extends BlockAnvil {
	private boolean effect;

	public BlockBrainStoneAnvil() {
		this(false);
	}

	public BlockBrainStoneAnvil(boolean effect) {
		super();

		this.effect = effect;

		setHardness(12.0f);
		setSoundType(SoundType.ANVIL);
		setResistance(2000.0f);
		setLightLevel(1.0f);
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

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		super.onBlockAdded(worldIn, pos, state);

		if (effect) {
			// TODO: Check that priority
			worldIn.scheduleBlockUpdate(pos, this, (int) worldIn.getTotalWorldTime() % tickRate(worldIn), 0);
		}
	}

	@Override
	public int tickRate(World par1World) {
		return 20;
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random random) {
		if (effect) {
			if (!world.isRemote && (random.nextInt(BrainStoneConfigWrapper.getPulsatingBrainStoneJumpChance()) == 0)
					&& (world.getBlockState(pos.down()).getBlock() != BrainStone.stablePulsatingBrainStone())) {
				EntityFallingBlock entityfallingblock = new EntityFallingBlock(world, pos.getX() + 0.5D, pos.getY(),
						pos.getZ() + 0.5D, world.getBlockState(pos));
				onStartFalling(entityfallingblock);
				BlockPulsatingBrainStone.kickEntity(entityfallingblock, random, 1.0);
				world.spawnEntityInWorld(entityfallingblock);
			}

			// TODO: Check that priority
			world.scheduleBlockUpdate(pos, this, tickRate(world), 0);
		}
	}
}
