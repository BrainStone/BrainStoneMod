package brainstonemod.common.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import brainstonemod.BrainStone;
import brainstonemod.client.ClientProxy;
import brainstonemod.common.block.template.BlockBrainStoneContainerBase;
import brainstonemod.common.helper.BrainStoneDirection;
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

	@Override
	public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z,
			int side) {
		assert (side < 4) && (side >= -1);

		if (side == -1)
			return true;

		final TileEntityBlockBrainLogicBlock tileEntity = (TileEntityBlockBrainLogicBlock) world
				.getTileEntity(x, y, z);

		if (tileEntity == null)
			return false;

		return tileEntity.connectToRedstone(BrainStoneDirection
				.fromRedstoneConnectIndex(side));
	}

	@Override
	public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
		final TileEntityBlockBrainLogicBlock tileEntity = (TileEntityBlockBrainLogicBlock) world
				.getTileEntity(x, y, z);

		if ((tileEntity != null) && (tileEntity.currentRenderDirection != null))
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
	public IIcon getIcon(int side, int meta) {
		if (side >= 2)
			return textures[1 + side];

		return textures[2];
	}

	@Override
	public int getRenderType() {
		return ClientProxy.BrainLogicBlockRenderType;
	}

	@Override
	public int isProvidingStrongPower(IBlockAccess iblockaccess, int x, int y,
			int z, int side) {
		final TileEntityBlockBrainLogicBlock tileEntity = (TileEntityBlockBrainLogicBlock) iblockaccess
				.getTileEntity(x, y, z);

		if (tileEntity != null)
			return tileEntity.getPowerOutputLevel(BrainStoneDirection
					.fromArrayIndex(side));
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
			EntityPlayer player, int unknown, float px, float py, float pz) {
		final TileEntityBlockBrainLogicBlock tileEntity = (TileEntityBlockBrainLogicBlock) world
				.getTileEntity(x, y, z);

		if (tileEntity == null)
			return false;
		else {
			TileEntityBlockBrainLogicBlock.guiDirection = BrainStoneDirection
					.fromPlayerYaw(player.rotationYaw);

			player.openGui(BrainStone.instance, 2, world, x, y, z);
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
			EntityLivingBase player, ItemStack itemStack) {
		((TileEntityBlockBrainLogicBlock) world.getTileEntity(x, y, z))
				.changeGate(BrainStoneDirection
						.fromPlayerYaw(player.rotationYaw));
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
		final TileEntityBlockBrainLogicBlock tileEntity = (TileEntityBlockBrainLogicBlock) world
				.getTileEntity(x, y, z);

		if (tileEntity != null) {
			tileEntity.doTASKS();

			long time;

			if (tileEntity.shallDoUpdate(time = world.getWorldInfo()
					.getWorldTotalTime())) {

				tileEntity.tickGate(world, x, y, z, time);

				BrainStonePacketHelper.sendReRenderBlockAtPacket(
						world.provider.dimensionId, x, y, z, world);
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
		}
	}
}
