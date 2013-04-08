package mods.brainstone.blocks;

import java.util.Random;

import mods.brainstone.BrainStone;
import mods.brainstone.handlers.BrainStonePacketHandler;
import mods.brainstone.templates.BlockBrainStoneContainerBase;
import mods.brainstone.tileentities.TileEntityBlockBrainLogicBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBrainLogicBlock extends BlockBrainStoneContainerBase {
	public static Icon[] textures;

	/**
	 * Constructor of the block. Registers all properties and sets the id and
	 * the material
	 * 
	 * @param i
	 *            The internal BrainStone id
	 */
	public BlockBrainLogicBlock(int i) {
		super(BrainStone.getId(i), Material.rock);

		this.setHardness(3.0F);
		this.setResistance(1.0F);
		this.setUnlocalizedName("brainLogicBlock");
		this.setCreativeTab(CreativeTabs.tabRedstone);

		this.setTickRandomly(true);

		blockParticleGravity = 0.0F;
	}

	@Override
	public void breakBlock(World world, int i, int j, int k, int par5, int par6) {
		super.breakBlock(world, i, j, k, par5, par6);
		world.notifyBlocksOfNeighborChange(i, j, k, blockID);
		world.notifyBlocksOfNeighborChange(i - 1, j, k, blockID);
		world.notifyBlocksOfNeighborChange(i + 1, j, k, blockID);
		world.notifyBlocksOfNeighborChange(i, j - 1, k, blockID);
		world.notifyBlocksOfNeighborChange(i, j + 1, k, blockID);
		world.notifyBlocksOfNeighborChange(i, j, k - 1, blockID);
		world.notifyBlocksOfNeighborChange(i, j, k + 1, blockID);
	}

	/**
	 * Determine if this block can make a redstone connection on the side
	 * provided, Useful to control which sides are inputs and outputs for
	 * redstone wires.
	 * 
	 * Side: -1: UP 0: NORTH 1: EAST 2: SOUTH 3: WEST
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

		final TileEntityBlockBrainLogicBlock tileentityblockbrainlogicblock = (TileEntityBlockBrainLogicBlock) world
				.getBlockTileEntity(x, y, z);

		if (tileentityblockbrainlogicblock == null)
			return false;

		return tileentityblockbrainlogicblock.connectToRedstone(side);
	}

	/**
	 * Checks the current state of a pin.
	 * 
	 * @param world
	 *            The world. Needed to access the blocks
	 * @param i
	 *            x-coordinate
	 * @param j
	 *            y-coordinate
	 * @param k
	 *            z-coordinate
	 * @param byte0
	 *            The position/direction of the pin
	 * @return The state at the requested pin
	 */
	private byte checkState(World world, int i, int j, int k, byte byte0) {
		switch (byte0) {
		case 3:
			i++;
			break;

		case 2:
			i--;
			break;

		case 1:
			k++;
			break;

		case 0:
			k--;
			break;
		}

		final int l = world.getBlockId(i, j, k);

		if (l == 0)
			return -1;

		byte byte1 = -1;
		Block block;

		if ((block = Block.blocksList[l]).canProvidePower()) {
			if (block instanceof BlockRedstoneWire) {
				byte1 = (byte) (world.getBlockMetadata(i, j, k) <= 0 ? 0 : 1);
			} else {
				byte0 += 2;
				byte1 = (byte) (((block.isProvidingWeakPower(world, i, j, k,
						byte0) + block.isProvidingStrongPower(world, i, j, k,
						byte0)) == 0) ? 0 : 1);
			}
		} else if (isNormalCube(l)) {
			byte1 = (byte) (world.isBlockIndirectlyGettingPowered(i, j, k) ? 1
					: 0);
		}

		return byte1;
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityBlockBrainLogicBlock();
	}

	@Override
	public Icon getBlockTexture(IBlockAccess iblockaccess, int i, int j, int k,
			int l) {

		if (l < 2)
			return textures[4];

		final TileEntityBlockBrainLogicBlock tileentityblockbrainlogicblock = (TileEntityBlockBrainLogicBlock) iblockaccess
				.getBlockTileEntity(i, j, k);

		if (tileentityblockbrainlogicblock == null)
			return textures[4];
		else
			return textures[tileentityblockbrainlogicblock
					.getPinStateBasedTextureIndex(tileentityblockbrainlogicblock
							.transformDirection(l - 2))];
	}

	@Override
	public Icon getBlockTextureFromSideAndMetadata(int i, int meta) {

		if (i >= 2)
			return textures[3 + i];

		return textures[4];
	}

	@Override
	public int isProvidingStrongPower(IBlockAccess iblockaccess, int i, int j,
			int k, int l) {
		final TileEntityBlockBrainLogicBlock tileentityblockbrainlogicblock = (TileEntityBlockBrainLogicBlock) iblockaccess
				.getBlockTileEntity(i, j, k);

		if ((tileentityblockbrainlogicblock != null)
				&& (tileentityblockbrainlogicblock.getDirection() == this
						.transformDirection(l - 2)))
			return tileentityblockbrainlogicblock.getOutput() ? 15 : 0;
		else
			return 0;
	}

	@Override
	public int isProvidingWeakPower(IBlockAccess iblockaccess, int i, int j,
			int k, int l) {
		return this.isProvidingStrongPower(iblockaccess, i, j, k, l);
	}

	@Override
	public boolean onBlockActivated(World world, int i, int j, int k,
			EntityPlayer entityplayer, int unknown, float px, float py, float pz) {
		final TileEntityBlockBrainLogicBlock tileentityblockbrainlogicblock = (TileEntityBlockBrainLogicBlock) world
				.getBlockTileEntity(i, j, k);

		if (tileentityblockbrainlogicblock == null)
			return false;
		else {
			entityplayer.openGui(BrainStone.instance, 2, world, i, j, k);
			world.notifyBlocksOfNeighborChange(i, j, k, blockID);
			return true;
		}
	}

	@Override
	public void onBlockAdded(World world, int i, int j, int k) {
		world.setBlockTileEntity(i, j, k, this.createNewTileEntity(world));
		world.notifyBlocksOfNeighborChange(i, j, k, blockID);
		world.notifyBlocksOfNeighborChange(i - 1, j, k, blockID);
		world.notifyBlocksOfNeighborChange(i + 1, j, k, blockID);
		world.notifyBlocksOfNeighborChange(i, j - 1, k, blockID);
		world.notifyBlocksOfNeighborChange(i, j + 1, k, blockID);
		world.notifyBlocksOfNeighborChange(i, j, k - 1, blockID);
		world.notifyBlocksOfNeighborChange(i, j, k + 1, blockID);
		world.scheduleBlockUpdate(i, j, k, blockID,
				(int) world.getTotalWorldTime() % this.tickRate(world));
	}

	/**
	 * Called when the block is placed in the world.
	 */
	@Override
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4,
			EntityLiving par5EntityLiving, ItemStack par6ItemStack) {
		((TileEntityBlockBrainLogicBlock) par1World.getBlockTileEntity(par2,
				par3, par4))
				.setDirection((byte) MathHelper
						.floor_double(((par5EntityLiving.rotationYaw * 4.0F) / 360.0F) + 0.5D) & 3);
	}

	@Override
	public void registerIcons(IconRegister IconReg) {
		textures = new Icon[] {
				IconReg.registerIcon("brainstone:brainLogicBlockNotConnected"),
				IconReg.registerIcon("brainstone:brainLogicBlockOff"),
				IconReg.registerIcon("brainstone:brainLogicBlockOn"),
				IconReg.registerIcon("furnace_side"),
				IconReg.registerIcon("furnace_top"),
				IconReg.registerIcon("brainstone:brainLogicBlockNotConnectedA"),
				IconReg.registerIcon("brainstone:brainLogicBlockOffC"),
				IconReg.registerIcon("brainstone:brainLogicBlockOnQ"),
				IconReg.registerIcon("brainstone:brainLogicBlockOnB") };
	}

	@Override
	public int tickRate(World par1World) {
		return 2;
	}

	/**
	 * Transforms directions from Minecraft directions (need to be decreased by
	 * 2) to TileEntityBlockBrainLogicBlock directions
	 * 
	 * @param i
	 *            Minecraft direction
	 * @return TileEntityBlockBrainLogicBlock direction
	 */
	private int transformDirection(int i) {
		switch (i) {
		case 0:
			return 2;

		case 1:
			return 0;

		case 2:
			return 1;

		case 3:
			return 3;
		}

		return 0;
	}

	@Override
	public void updateTick(World world, int i, int j, int k, Random random) {
		super.updateTick(world, i, j, k, random);
		final TileEntityBlockBrainLogicBlock tileentityblockbrainlogicblock = (TileEntityBlockBrainLogicBlock) world
				.getBlockTileEntity(i, j, k);

		if (tileentityblockbrainlogicblock != null) {
			tileentityblockbrainlogicblock.doTASKS();

			if (tileentityblockbrainlogicblock.shallDoUpdate(world
					.getWorldInfo().getWorldTime())) {
				final byte abyte0[] = { -1, -1, -1 };

				for (byte byte0 = 1; byte0 < 4; byte0++) {
					abyte0[byte0 - 1] = this.checkState(world, i, j, k,
							tileentityblockbrainlogicblock
									.reverseTransformDirection(byte0));
				}

				tileentityblockbrainlogicblock.setPinState(abyte0);
				BrainStonePacketHandler.sendReRenderBlockAtPacket(i, j, k,
						world);
				world.notifyBlockChange(i, j, k, blockID);
				world.notifyBlocksOfNeighborChange(i, j, k, blockID);
				world.notifyBlocksOfNeighborChange(i - 1, j, k, blockID);
				world.notifyBlocksOfNeighborChange(i + 1, j, k, blockID);
				world.notifyBlocksOfNeighborChange(i, j - 1, k, blockID);
				world.notifyBlocksOfNeighborChange(i, j + 1, k, blockID);
				world.notifyBlocksOfNeighborChange(i, j, k - 1, blockID);
				world.notifyBlocksOfNeighborChange(i, j, k + 1, blockID);
				world.scheduleBlockUpdate(i, j, k, blockID,
						this.tickRate(world));
			}
		}
	}
}
