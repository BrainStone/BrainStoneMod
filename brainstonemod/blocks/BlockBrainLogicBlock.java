package brainstonemod.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import brainstonemod.BrainStone;
import brainstonemod.ClientProxy;
import brainstonemod.handlers.BrainStonePacketHandler;
import brainstonemod.templates.BlockBrainStoneContainerBase;
import brainstonemod.tileentities.TileEntityBlockBrainLogicBlock;

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
				.getBlockTileEntity(x, y, z);

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
	@SuppressWarnings("unused")
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
	public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
		final TileEntityBlockBrainLogicBlock tileEntity = (TileEntityBlockBrainLogicBlock) world
				.getBlockTileEntity(x, y, z);

		if ((tileEntity != null) && (tileEntity.currentRenderDirection != -1))
			return tileEntity.getGateColor(tileEntity.currentRenderDirection);

		return 16777215;
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityBlockBrainLogicBlock();
	}

	@Override
	public Icon getBlockTexture(IBlockAccess iblockaccess, int x, int y, int z,
			int side) {
		if (side < 2)
			return textures[2];

		return textures[1];
	}

	@Override
	public Icon getIcon(int i, int meta) {
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
				.getBlockTileEntity(x, y, z);

		if (tileentityblockbrainlogicblock != null)
			return tileentityblockbrainlogicblock
					.getPowerOutputLevel((byte) (side ^ 1));
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
			TileEntityBlockBrainLogicBlock.guiDirection = (byte) ((MathHelper
					.floor_double(((entityplayer.rotationYaw * 4.0F) / 360.0F) + 0.5D) & 3) ^ 2);

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

	@Override
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4,
			EntityLivingBase par5EntityLiving, ItemStack par6ItemStack) {
		((TileEntityBlockBrainLogicBlock) par1World.getBlockTileEntity(par2,
				par3, par4))
				.changeGate(MathHelper
						.floor_double(((par5EntityLiving.rotationYaw * 4.0F) / 360.0F) + 0.5D) & 3);
	}

	@Override
	public void registerIcons(IconRegister IconReg) {
		textures = new Icon[] {
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
	public int tickRate(World par1World) {
		return 2;
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random random) {
		super.updateTick(world, x, y, z, random);
		final TileEntityBlockBrainLogicBlock tileentityblockbrainlogicblock = (TileEntityBlockBrainLogicBlock) world
				.getBlockTileEntity(x, y, z);

		if (tileentityblockbrainlogicblock != null) {
			tileentityblockbrainlogicblock.doTASKS();

			long time;

			if (tileentityblockbrainlogicblock.shallDoUpdate(time = world
					.getWorldInfo().getWorldTime())) {

				tileentityblockbrainlogicblock.tickGate(world, x, y, z, time);

				BrainStonePacketHandler.sendReRenderBlockAtPacket(x, y, z,
						world);
				world.notifyBlockChange(x, y, z, blockID);
				world.notifyBlocksOfNeighborChange(x, y, z, blockID);
				world.notifyBlocksOfNeighborChange(x - 1, y, z, blockID);
				world.notifyBlocksOfNeighborChange(x + 1, y, z, blockID);
				world.notifyBlocksOfNeighborChange(x, y - 1, z, blockID);
				world.notifyBlocksOfNeighborChange(x, y + 1, z, blockID);
				world.notifyBlocksOfNeighborChange(x, y, z - 1, blockID);
				world.notifyBlocksOfNeighborChange(x, y, z + 1, blockID);
				world.scheduleBlockUpdate(x, y, z, blockID,
						this.tickRate(world));
			}
		}
	}
}
