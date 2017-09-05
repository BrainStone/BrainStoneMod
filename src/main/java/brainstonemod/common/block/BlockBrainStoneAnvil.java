package brainstonemod.common.block;

import java.util.Random;

import brainstonemod.BrainStone;
import brainstonemod.BrainStoneBlocks;
import brainstonemod.common.config.BrainStoneConfigWrapper;
import brainstonemod.network.BrainStoneGuiHandler;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockBrainStoneAnvil extends BlockAnvil {
	private final boolean effect;

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
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			playerIn.openGui(BrainStone.MOD_ID, BrainStoneGuiHandler.ID_BRAIN_STONE_ANVIL, world, pos.getX(),
					pos.getY(), pos.getZ());
		}

		return true;
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		super.onBlockAdded(worldIn, pos, state);

		if (effect) {
			worldIn.scheduleUpdate(pos, this,
					(int) worldIn.getTotalWorldTime() % ((new Random()).nextInt(tickRate(worldIn)) + 1));
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
					&& (world.getBlockState(pos.down()).getBlock() != BrainStoneBlocks.stablePulsatingBrainStone())) {
				EntityFallingBlock entityfallingblock = new EntityFallingBlock(world, pos.getX() + 0.5D, pos.getY(),
						pos.getZ() + 0.5D, world.getBlockState(pos));
				onStartFalling(entityfallingblock);
				BlockPulsatingBrainStone.kickEntity(entityfallingblock, random, 1.0);
				world.spawnEntity(entityfallingblock);
			}

			world.scheduleUpdate(pos, this, random.nextInt(tickRate(world)));
		}
	}
}
