package brainstonemod.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockConnectedTextures extends Block {
	public static final PropertyBool CONNECTED_DOWN = PropertyBool.create("connected_down");
	public static final PropertyBool CONNECTED_UP = PropertyBool.create("connected_up");
	public static final PropertyBool CONNECTED_NORTH = PropertyBool.create("connected_north");
	public static final PropertyBool CONNECTED_SOUTH = PropertyBool.create("connected_south");
	public static final PropertyBool CONNECTED_WEST = PropertyBool.create("connected_west");
	public static final PropertyBool CONNECTED_EAST = PropertyBool.create("connected_east");

	public BlockConnectedTextures(Material materialIn) {
		super(materialIn);

		setDefaultState(
				blockState.getBaseState().withProperty(CONNECTED_DOWN, false).withProperty(CONNECTED_EAST, false)
						.withProperty(CONNECTED_NORTH, false).withProperty(CONNECTED_SOUTH, false)
						.withProperty(CONNECTED_UP, false).withProperty(CONNECTED_WEST, false));
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { CONNECTED_DOWN, CONNECTED_UP, CONNECTED_NORTH,
				CONNECTED_SOUTH, CONNECTED_WEST, CONNECTED_EAST });
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos position) {
		return state.withProperty(CONNECTED_DOWN, isSideConnectable(world, position, EnumFacing.DOWN))
				.withProperty(CONNECTED_EAST, isSideConnectable(world, position, EnumFacing.EAST))
				.withProperty(CONNECTED_NORTH, isSideConnectable(world, position, EnumFacing.NORTH))
				.withProperty(CONNECTED_SOUTH, isSideConnectable(world, position, EnumFacing.SOUTH))
				.withProperty(CONNECTED_UP, isSideConnectable(world, position, EnumFacing.UP))
				.withProperty(CONNECTED_WEST, isSideConnectable(world, position, EnumFacing.WEST));
	}

	/**
	 * Checks if a specific side of a block can connect to this block. For this
	 * example, a side is connectable if the block is the same block as this
	 * one.
	 *
	 * @param world
	 *            The world to run the check in.
	 * @param pos
	 *            The position of the block to check for.
	 * @param side
	 *            The side of the block to check.
	 * @return Whether or not the side is connectable.
	 */
	private boolean isSideConnectable(IBlockAccess world, BlockPos pos, EnumFacing side) {
		final IBlockState state = world.getBlockState(pos.offset(side));
		return (state == null) ? false : state.getBlock() == this;
	}
}
