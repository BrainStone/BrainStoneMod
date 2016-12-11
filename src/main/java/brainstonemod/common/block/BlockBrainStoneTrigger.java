package brainstonemod.common.block;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import brainstonemod.BrainStone;
import brainstonemod.common.block.property.UnlistedPropertyCopiedBlock;
import brainstonemod.common.block.template.BlockBrainStoneHiders;
import brainstonemod.common.helper.BSP;
import brainstonemod.common.tileentity.TileEntityBrainStoneTrigger;
import brainstonemod.network.BrainStoneGuiHandler;
import brainstonemod.network.BrainStonePacketHelper;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBrainStoneTrigger extends BlockBrainStoneHiders {
	public static final UnlistedPropertyCopiedBlock COPIEDBLOCK = new UnlistedPropertyCopiedBlock();

	public BlockBrainStoneTrigger() {
		super();

		setHardness(2.4F);
		setResistance(0.5F);
		setHarvestLevel("pickaxe", 1);

		blockParticleGravity = -0.2F;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		final TileEntityBrainStoneTrigger tileentityblockbrainstonetrigger = (TileEntityBrainStoneTrigger) world
				.getTileEntity(pos);

		if (tileentityblockbrainstonetrigger != null) {
			tileentityblockbrainstonetrigger.dropItems(world, pos.getX(), pos.getY(), pos.getZ());
		}

		world.removeTileEntity(pos);
		world.notifyNeighborsOfStateChange(pos, this);
		world.notifyNeighborsOfStateChange(pos.add(-1, 0, 0), this);
		world.notifyNeighborsOfStateChange(pos.add(1, 0, 0), this);
		world.notifyNeighborsOfStateChange(pos.down(), this);
		world.notifyNeighborsOfStateChange(pos.up(), this);
		world.notifyNeighborsOfStateChange(pos.add(0, 0, -1), this);
		world.notifyNeighborsOfStateChange(pos.add(0, 0, 1), this);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int par2) {
		return new TileEntityBrainStoneTrigger();
	}

	@Override
	public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		final TileEntityBrainStoneTrigger tileentityblockbrainstonetrigger = (TileEntityBrainStoneTrigger) blockAccess
				.getTileEntity(pos);
		return ((tileentityblockbrainstonetrigger != null) && (tileentityblockbrainstonetrigger.getDelay() > 0))
				? tileentityblockbrainstonetrigger.getOutputBuffered()
				: 0;
	}

	@Override
	public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		return getStrongPower(blockState, blockAccess, pos, side);
	}

	@Override
	public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return true;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand,
			@Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (world.isRemote)
			return true;

		final TileEntityBrainStoneTrigger tileentityblockbrainstonetrigger = (TileEntityBrainStoneTrigger) world
				.getTileEntity(pos);

		if (tileentityblockbrainstonetrigger != null) {
			playerIn.openGui(BrainStone.instance, BrainStoneGuiHandler.ID_BRAIN_STONE_TRIGGER, world, pos.getX(),
					pos.getY(), pos.getZ());
		}

		return true;
	}

	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
		world.setTileEntity(pos, createNewTileEntity(world, 0));
		world.notifyNeighborsOfStateChange(pos, this);
		world.notifyNeighborsOfStateChange(pos.add(-1, 0, 0), this);
		world.notifyNeighborsOfStateChange(pos.add(1, 0, 0), this);
		world.notifyNeighborsOfStateChange(pos.down(), this);
		world.notifyNeighborsOfStateChange(pos.up(), this);
		world.notifyNeighborsOfStateChange(pos.add(0, 0, -1), this);
		world.notifyNeighborsOfStateChange(pos.add(0, 0, 1), this);

		world.scheduleBlockUpdate(pos, this, tickRate(world) - ((int) world.getTotalWorldTime() % tickRate(world)), 0);// TODO:
																														// Check
																														// that
																														// priority
	}

	@Override
	public int tickRate(World par1World) {
		return 2;
	}

	/**
	 * Checks if there are mobs to trigger on top of this block.
	 *
	 * @param world
	 *            The world. Needed to access the blocks
	 * @param i
	 *            x-coordinate
	 * @param j
	 *            y-coordinate
	 * @param k
	 *            z-coordinate
	 * @return True if there is at least one mob to trigger on top of this
	 *         block, false if not.
	 */
	private byte triggerCorrectMob(World world, int i, int j, int k) {
		final List<?> list = world.getEntitiesWithinAABBExcludingEntity(null,
				new AxisAlignedBB(i, j + 1, k, i + 1, j + 2, k + 1));
		final TileEntityBrainStoneTrigger tileentityblockbrainstonetrigger = (TileEntityBrainStoneTrigger) world
				.getTileEntity(new BlockPos(i, j, k));

		if (tileentityblockbrainstonetrigger == null)
			return 0;

		byte count = 0;

		for (int l = 0; (l < list.size()) && (count < 15); l++) {
			final Class<?> entity = ((Entity) list.get(l)).getClass();

			if (entity == null) {
				BSP.fatal("Fehler! Die Entity ist nicht vorhanden!");
				continue;
			}

			final int length = BrainStone.getSidedTriggerEntities().size();
			final String[] keys = BrainStone.getSidedTriggerEntities().keySet().toArray(new String[length]);
			String key;
			Class<?>[] classes;

			for (int count1 = 0; (count1 < length) && (count < 15); count1++) {
				key = keys[count1];

				if (tileentityblockbrainstonetrigger.getMobTriggered(key)) {
					classes = BrainStone.getSidedTriggerEntities().get(key);

					for (final Class<?> classe : classes) {
						if (classe.isAssignableFrom(entity)) {
							count += tileentityblockbrainstonetrigger.getMobPower(key);

							break;
						}
					}
				}
			}
		}

		return (count > 15) ? 15 : count;
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
		final TileEntityBrainStoneTrigger tileEntityBlockBrainStoneTrigger = (TileEntityBrainStoneTrigger) world
				.getTileEntity(pos);

		if (tileEntityBlockBrainStoneTrigger == null) {
			world.scheduleBlockUpdate(pos, this, tickRate(world), 0);// TODO:
																		// Check
																		// that
																		// priority
			return;
		}

		tileEntityBlockBrainStoneTrigger.setOutput(triggerCorrectMob(world, pos.getX(), pos.getY(), pos.getZ()));
		if (tileEntityBlockBrainStoneTrigger.getOutput() > 0) {
			tileEntityBlockBrainStoneTrigger.setOutputBuffered(tileEntityBlockBrainStoneTrigger.getOutput());
		}

		tileEntityBlockBrainStoneTrigger.setDelay((byte) ((tileEntityBlockBrainStoneTrigger.getOutput() > 0)
				? tileEntityBlockBrainStoneTrigger.getMaxDelay()
				: tileEntityBlockBrainStoneTrigger.getDelay() <= 0 ? 0
						: tileEntityBlockBrainStoneTrigger.getDelay() - 1));

		if (tileEntityBlockBrainStoneTrigger.checkForSlotChange()) {
			BrainStonePacketHelper.sendReRenderBlockAtPacket(world.provider.getDimension(), pos.getX(), pos.getY(),
					pos.getZ(), world);
		}

		world.notifyNeighborsOfStateChange(pos, this);
		world.notifyNeighborsOfStateChange(pos.add(-1, 0, 0), this);
		world.notifyNeighborsOfStateChange(pos.add(1, 0, 0), this);
		world.notifyNeighborsOfStateChange(pos.down(), this);
		world.notifyNeighborsOfStateChange(pos.up(), this);
		world.notifyNeighborsOfStateChange(pos.add(0, 0, -1), this);
		world.notifyNeighborsOfStateChange(pos.add(0, 0, 1), this);

		world.scheduleBlockUpdate(pos, this, tickRate(world), 0);// TODO: Check
																	// that
																	// priority
	}

	@SideOnly(Side.CLIENT)
	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.SOLID;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return true;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState iBlockState) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public boolean isFullCube(IBlockState iBlockState) {
		return true;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected BlockStateContainer createBlockState() {
		IProperty[] listedProperties = new IProperty[0]; // no listed properties
		IUnlistedProperty[] unlistedProperties = new IUnlistedProperty[] { COPIEDBLOCK };
		return new ExtendedBlockState(this, listedProperties, unlistedProperties);
	}

	@SuppressWarnings("deprecation")
	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
		if (state instanceof IExtendedBlockState) { // avoid crash in case of
													// mismatch
			IExtendedBlockState retval = (IExtendedBlockState) state;
			if ((world.getTileEntity(pos) instanceof TileEntityBrainStoneTrigger)
					&& (((TileEntityBrainStoneTrigger) world.getTileEntity(pos)).getStackInSlot(0) != null)
					&& (((TileEntityBrainStoneTrigger) world.getTileEntity(pos)).getStackInSlot(0)
							.getItem() instanceof ItemBlock)) {
				IBlockState copiedBlock = Block
						.getBlockFromItem(
								((TileEntityBrainStoneTrigger) world.getTileEntity(pos)).getStackInSlot(0).getItem())
						.getStateFromMeta(((TileEntityBrainStoneTrigger) world.getTileEntity(pos)).getStackInSlot(0)
								.getMetadata());
				retval = retval.withProperty(COPIEDBLOCK, copiedBlock);
				return retval;
			} else {
				retval = retval.withProperty(COPIEDBLOCK, state);
				return retval;
			}
		}
		return state;
	}
}
