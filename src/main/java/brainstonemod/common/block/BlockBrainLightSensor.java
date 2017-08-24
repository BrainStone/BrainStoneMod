package brainstonemod.common.block;

import java.util.Random;

import brainstonemod.BrainStone;
import brainstonemod.common.block.template.BlockBrainStoneContainerBase;
import brainstonemod.common.helper.BSP;
import brainstonemod.common.tileentity.TileEntityBrainLightSensor;
import brainstonemod.network.BrainStoneGuiHandler;
import brainstonemod.network.BrainStonePacketHelper;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBrainLightSensor extends BlockBrainStoneContainerBase {
	public BlockBrainLightSensor() {
		super(Material.ROCK);

		setHardness(2.4F);
		setResistance(0.5F);
		setCreativeTab(BrainStone.getCreativeTab(CreativeTabs.REDSTONE));
		setHarvestLevel("pickaxe", 1);

		blockParticleGravity = -0.2F;
	}

	@Override
	public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return side != EnumFacing.UP;
	}

	@Override
	public boolean canProvidePower(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int par1) {
		return new TileEntityBrainLightSensor();
	}

	@Override
	public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		final TileEntityBrainLightSensor tileentity = (TileEntityBrainLightSensor) blockAccess.getTileEntity(pos);

		if (tileentity == null)
			return 0;

		if (tileentity.isClassic())
			return tileentity.getPowerOn() ? 15 : 0;
		else
			return tileentity.getPower();
	}

	@Override
	public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		return getStrongPower(blockState, blockAccess, pos, side);
	}

	@Override
	public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return side != EnumFacing.UP;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		final TileEntityBrainLightSensor tileentity = (TileEntityBrainLightSensor) world.getTileEntity(pos);

		if (tileentity == null) {
			BSP.log("The TileEntity is null!");

			return false;
		} else {
			playerIn.openGui(BrainStone.instance, BrainStoneGuiHandler.ID_BRAIN_LIGHT_SENSOR, world, pos.getX(),
					pos.getY(), pos.getZ());
			return true;
		}
	}

	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
		super.onBlockAdded(world, pos, state);

		world.scheduleUpdate(pos, this, tickRate(world) - ((int) world.getTotalWorldTime() % tickRate(world)));
	}

	public void smoke(World world, int x, int y, int z, Random random) {
		if (world.isRemote) {
			world.playSound(x, y, z, SoundEvents.UI_BUTTON_CLICK, SoundCategory.BLOCKS, 1.0F, 1.0F, true);
			int randInt = random.nextInt(5) + random.nextInt(6) + 5;

			int i;
			for (i = 0; i < randInt; i++) {
				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, (x + (random.nextDouble() * 1.4)) - 0.2,
						y + 0.8 + (random.nextDouble() * 0.6), (z + (random.nextDouble() * 1.4)) - 0.2, 0.0D, 0.0D,
						0.0D);
			}

			randInt = random.nextInt(3);

			for (i = 0; i < randInt; i++) {
				world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, (x + (random.nextDouble() * 1.4)) - 0.2,
						y + 0.8 + (random.nextDouble() * 0.6), (z + (random.nextDouble() * 1.4)) - 0.2, 0.0D, 0.0D,
						0.0D);
			}
		} else {
			BSP.warn("The server should not call this!");
		}
	}

	@Override
	public int tickRate(World par1World) {
		return 2;
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
		final TileEntityBrainLightSensor tileEntity = (TileEntityBrainLightSensor) world.getTileEntity(pos);

		if (tileEntity != null) {
			final int worldLight = tileEntity.getCurLightLevel();

			if (tileEntity.isClassic()) {
				final int lightLevel = tileEntity.getLightLevel();

				tileEntity.setPowerOn(tileEntity.getDirection() ? worldLight <= lightLevel : worldLight >= lightLevel);
			} else {
				tileEntity.setPower((short) (tileEntity.getDirection() ? worldLight : 15 - worldLight));
			}

			if (tileEntity.getNeedsUpdate()) {
				if (tileEntity.isClassic()) {
					BrainStonePacketHelper.sendBrainLightSensorSmokePacket(world.provider.getDimension(), pos.getX(),
							pos.getY(), pos.getZ());
				}

				world.notifyNeighborsOfStateChange(pos, this, true);
				world.notifyNeighborsOfStateChange(pos.add(-1, 0, 0), this, true);
				world.notifyNeighborsOfStateChange(pos.add(1, 0, 0), this, true);
				world.notifyNeighborsOfStateChange(pos.down(), this, true);
				world.notifyNeighborsOfStateChange(pos.up(), this, true);
				world.notifyNeighborsOfStateChange(pos.add(0, 0, -1), this, true);
				world.notifyNeighborsOfStateChange(pos.add(0, 0, 1), this, true);
			}

			world.scheduleUpdate(pos, this, tickRate(world));
		} else {
			BSP.fatal("TileEntity missing!");
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}
}
