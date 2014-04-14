package brainstonemod.common.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import brainstonemod.BrainStone;
import brainstonemod.client.ClientProxy;
import brainstonemod.common.block.template.BlockBrainStoneContainerBase;
import brainstonemod.common.tileentity.TileEntityBlockBrainLogicBlock;
import brainstonemod.network.BrainStonePacketHelper;

public class BlockBrainLogicBlock extends BlockBrainStoneContainerBase {
	public static IIcon[] textures;

	/**
	 * Constructor of the block. Registers all properties and sets the id and
	 * the material
	 * 
	 * @param i
	 *            The internal BrainStone id
	 */
	public BlockBrainLogicBlock() {
		super(Material.rock);

		setHardness(3.0F);
		setResistance(1.0F);
		setCreativeTab(CreativeTabs.tabRedstone);
		setHarvestLevel("pickaxe", 1);

		setTickRandomly(true);

		blockParticleGravity = 0.0F;
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block,
			int meta) {
		super.breakBlock(world, x, y, z, block, meta);
		world.notifyBlocksOfNeighborChange(x, y, z, this);
		world.notifyBlocksOfNeighborChange(x - 1, y, z, this);
		world.notifyBlocksOfNeighborChange(x + 1, y, z, this);
		world.notifyBlocksOfNeighborChange(x, y - 1, z, this);
		world.notifyBlocksOfNeighborChange(x, y + 1, z, this);
		world.notifyBlocksOfNeighborChange(x, y, z - 1, this);
		world.notifyBlocksOfNeighborChange(x, y, z + 1, this);
	}

	/**
	 * Determine if this block can make a redstone connection on the side
	 * provided, Useful to control which sides are inputs and outputs for
	 * redstone wires.<br>
	 * 
	 * Side:<br>
	 * -1: UP<br>
	 * 0: NORTH<br>
	 * 1: EAST<br>
	 * 2: SOUTH<br>
	 * 3: WEST
	 * 
	 * @param world
	 *            The current world
	 * @param x
	 *            X Position
	 * @param y
	 *            Y Position
	 * @param z
	 *            Z Position
	 * @param side
	 *            The side that is trying to make the connection
	 * @return True to make the connection
	 */
	@Override
	public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z,
			int side) {
		if (side == -1)
			return false;

		final TileEntityBlockBrainLogicBlock tileEntity = (TileEntityBlockBrainLogicBlock) world
				.getTileEntity(x, y, z);

		if (tileEntity == null)
			return false;

		return tileEntity.connectToRedstone(side);
	}

	// TODO See if this can be removed!
	/**
	 * Checks the current state of a pin.
	 * 
	 * @param world
	 *            The world. Needed to access the blocks
	 * @param x
	 *            x-coordinate
	 * @param y
	 *            y-coordinate
	 * @param z
	 *            z-coordinate
	 * @param pos
	 *            The position/direction of the pin
	 * @return The state at the requested pin
	 */
	private byte checkState(World world, int x, int y, int z, byte pos) {
		switch (pos) {
		case 3:
			x++;
			break;

		case 2:
			x--;
			break;

		case 1:
			z++;
			break;

		case 0:
			z--;
			break;
		}

		byte byte1 = -1;
		final Block block = world.getBlock(x, y, z);
		;

		if (block.canProvidePower()) {
			if (block instanceof BlockRedstoneWire) {
				byte1 = (byte) (world.getBlockMetadata(x, y, z) <= 0 ? 0 : 1);
			} else {
				pos += 2;
				byte1 = (byte) (((block.isProvidingWeakPower(world, x, y, z,
						pos) + block
						.isProvidingStrongPower(world, x, y, z, pos)) == 0) ? 0
						: 1);
			}
		} else if (block.isNormalCube()) {
			byte1 = (byte) (world.isBlockIndirectlyGettingPowered(x, y, z) ? 1
					: 0);
		}

		return byte1;
	}

	@Override
	public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
		final TileEntityBlockBrainLogicBlock tileEntity = (TileEntityBlockBrainLogicBlock) world
				.getTileEntity(x, y, z);

		if ((tileEntity != null) && (tileEntity.currentRenderDirection != -1))
			return tileEntity.getGateColor(tileEntity.currentRenderDirection);

		return 16777215;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int unknown_1) {
		return new TileEntityBlockBrainLogicBlock();
	}

	@Override
	public IIcon getIcon(IBlockAccess iblockaccess, int x, int y, int z,
			int side) {
		if (side < 2)
			return textures[2];

		return textures[1];
	}

	@Override
	public IIcon getIcon(int i, int meta) {
		if (i >= 2)
			return textures[1 + i];

		return textures[2];
	}

	@Override
	public int getRenderType() {
		return ClientProxy.BrainLogicBlockRenderType;
	}

	@Override
	public int isProvidingStrongPower(IBlockAccess iblockaccess, int x, int y,
			int z, int side) {
		final TileEntityBlockBrainLogicBlock tileentityblockbrainlogicblock = (TileEntityBlockBrainLogicBlock) iblockaccess
				.getTileEntity(x, y, z);

<<<<<<< HEAD
		if (tileentityblockbrainlogicblock != null)
			return tileentityblockbrainlogicblock
					.getPowerOutputLevel((byte) (side ^ 1));
=======
		if ((tileEntity != null)
				&& (tileEntity.getDirection() == transformDirection(side - 2)))
			return tileEntity.getOutput() ? 15 : 0;
>>>>>>> f3a966d... v2.42.1037 BETA prerelease
		else
			return 0;
	}

	@Override
	public int isProvidingWeakPower(IBlockAccess iblockaccess, int x, int y,
			int z, int side) {
		return isProvidingStrongPower(iblockaccess, x, y, z, side);
	}

	@Override
	public boolean isSideSolid(IBlockAccess world, int x, int y, int z,
			ForgeDirection side) {
		return true;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer entityplayer, int unknown, float px, float py, float pz) {
		final TileEntityBlockBrainLogicBlock tileentityblockbrainlogicblock = (TileEntityBlockBrainLogicBlock) world
				.getTileEntity(x, y, z);

		if (tileentityblockbrainlogicblock == null)
			return false;
		else {
			TileEntityBlockBrainLogicBlock.guiDirection = (byte) ((MathHelper
					.floor_double(((entityplayer.rotationYaw * 4.0F) / 360.0F) + 0.5D) & 3) ^ 2);

			entityplayer.openGui(BrainStone.instance, 2, world, x, y, z);
			world.notifyBlocksOfNeighborChange(x, y, z, this);
			return true;
		}
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		world.setTileEntity(x, y, z, createNewTileEntity(world, 0));
		world.notifyBlocksOfNeighborChange(x, y, z, this);
		world.notifyBlocksOfNeighborChange(x - 1, y, z, this);
		world.notifyBlocksOfNeighborChange(x + 1, y, z, this);
		world.notifyBlocksOfNeighborChange(x, y - 1, z, this);
		world.notifyBlocksOfNeighborChange(x, y + 1, z, this);
		world.notifyBlocksOfNeighborChange(x, y, z - 1, this);
		world.notifyBlocksOfNeighborChange(x, y, z + 1, this);

		world.scheduleBlockUpdate(x, y, z, this,
				tickRate(world)
						- ((int) world.getTotalWorldTime() % tickRate(world)));
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z,
			EntityLivingBase entity, ItemStack itemStack) {
		((TileEntityBlockBrainLogicBlock) world.getTileEntity(x, y, z))
				.changeGate(MathHelper
						.floor_double(((entity.rotationYaw * 4.0F) / 360.0F) + 0.5D) & 3);
	}

	@Override
	public void registerBlockIcons(IIconRegister IconReg) {
		textures = new IIcon[] {
				IconReg.registerIcon("brainstonemod:brainLogicBlockPin"),
				IconReg.registerIcon("furnace_side"),
				IconReg.registerIcon("furnace_top"),
				IconReg.registerIcon("brainstonemod:brainLogicBlockNotConnectedA"),
				IconReg.registerIcon("brainstonemod:brainLogicBlockOffC"),
				IconReg.registerIcon("brainstonemod:brainLogicBlockOnQ"),
				IconReg.registerIcon("brainstonemod:brainLogicBlockOnB") };
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public int tickRate(World world) {
		return 2;
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random random) {
		super.updateTick(world, x, y, z, random);
		final TileEntityBlockBrainLogicBlock tileentityblockbrainlogicblock = (TileEntityBlockBrainLogicBlock) world
				.getTileEntity(x, y, z);

		if (tileentityblockbrainlogicblock != null) {
			tileentityblockbrainlogicblock.doTASKS();

			long time;

<<<<<<< HEAD
			if (tileentityblockbrainlogicblock.shallDoUpdate(time = world
					.getWorldInfo().getWorldTime())) {

				tileentityblockbrainlogicblock.tickGate(world, x, y, z, time);

				BrainStonePacketHelper.sendReRenderBlockAtPacket(
						world.provider.dimensionId, x, y, z, world);
=======
				for (byte i = 1; i < 4; i++) {
					pinStates[i - 1] = checkState(world, x, y, z,
							tileEntity.reverseTransformDirection(i));
				}

				if (tileEntity.setPinState(pinStates)) {
					BrainStonePacketHelper
							.sendUpdateTileEntityPacket(tileEntity);
				}

>>>>>>> f3a966d... v2.42.1037 BETA prerelease
				world.notifyBlockChange(x, y, z, this);
				world.notifyBlocksOfNeighborChange(x, y, z, this);
				world.notifyBlocksOfNeighborChange(x - 1, y, z, this);
				world.notifyBlocksOfNeighborChange(x + 1, y, z, this);
				world.notifyBlocksOfNeighborChange(x, y - 1, z, this);
				world.notifyBlocksOfNeighborChange(x, y + 1, z, this);
				world.notifyBlocksOfNeighborChange(x, y, z - 1, this);
				world.notifyBlocksOfNeighborChange(x, y, z + 1, this);
				world.scheduleBlockUpdate(x, y, z, this, tickRate(world));
			}
<<<<<<< HEAD
=======

			world.scheduleBlockUpdate(x, y, z, this, tickRate(world));
>>>>>>> f3a966d... v2.42.1037 BETA prerelease
		}
	}
}
