package brainstonemod.common.block;

import java.io.IOException;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import brainstonemod.BrainStone;
import brainstonemod.common.block.template.BlockBrainStoneContainerBase;
import brainstonemod.common.helper.BSP;
import brainstonemod.common.tileentity.TileEntityBlockBrainLightSensor;

public class BlockBrainLightSensor extends BlockBrainStoneContainerBase {
	public static IIcon[] textures;

	/**
	 * Constructor of the block. Registers all properties and sets the id and
	 * the material
	 * 
	 * @param i
	 *            The internal BrainStone id
	 */
	public BlockBrainLightSensor() {
		super(Material.rock);

		setHardness(2.4F);
		setResistance(0.5F);
		setBlockName("brainLightSensor");
		setCreativeTab(CreativeTabs.tabRedstone);

		blockParticleGravity = -0.2F;
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block,
			int meta) {
		world.removeTileEntity(x, y, z);
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

		return true;
	}

	@Override
	public boolean canProvidePower() {
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int par1) {
		return new TileEntityBlockBrainLightSensor();
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		if (side == 1)
			return textures[0];
		else if (side == 0)
			return textures[2];
		else
			return textures[1];
	}

	@Override
	public int isProvidingStrongPower(IBlockAccess iblockaccess, int x, int y,
			int z, int meta) {
		final TileEntityBlockBrainLightSensor tileentity = (TileEntityBlockBrainLightSensor) iblockaccess
				.getTileEntity(x, y, z);

		if (tileentity == null)
			return 0;

		if (tileentity.getState())
			return tileentity.getPowerOn() ? 15 : 0;
		else
			return tileentity.getPower();
	}

	@Override
	public int isProvidingWeakPower(IBlockAccess iblockaccess, int x, int y,
			int z, int meta) {
		return isProvidingStrongPower(iblockaccess, x, y, z, meta);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer entityplayer, int unknown, float px, float py, float pz) {
		final TileEntityBlockBrainLightSensor tileentity = (TileEntityBlockBrainLightSensor) world
				.getTileEntity(x, y, z);

		if (tileentity == null) {
			BSP.log("The TileEntity is null!");

			return false;
		} else {
			entityplayer.openGui(BrainStone.instance, 0, world, x, y, z);
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
				(int) world.getTotalWorldTime() % tickRate(world));
	}

	@Override
	public void registerBlockIcons(IIconRegister IconReg) {
		textures = new IIcon[] {
				IconReg.registerIcon("brainstonemod:brainLightSensor"),
				IconReg.registerIcon("furnace_side"),
				IconReg.registerIcon("furnace_top") };
	}

	private void smoke(World world, int x, int y, int z, Random random) {
		/*
		 * BrainStone.proxy.getClient().sndManager.playSoundFX("random.click",
		 * 1.0F, 1.0F); int randInt = random.nextInt(11) + 5;
		 * 
		 * int i; for (i = 0; i < randInt; i++) { world.spawnParticle( "smoke",
		 * (x + (random.nextFloat() * 1.3999999999999999D)) -
		 * 0.20000000000000001D, y + 0.80000000000000004D + (random.nextFloat()
		 * * 0.59999999999999998D), (z + (random.nextFloat() *
		 * 1.3999999999999999D)) - 0.20000000000000001D, 0.0D, 0.0D, 0.0D); }
		 * 
		 * randInt = random.nextInt(3);
		 * 
		 * for (i = 0; i < randInt; i++) { world.spawnParticle( "largesmoke", (x
		 * + (random.nextFloat() * 1.3999999999999999D)) - 0.20000000000000001D,
		 * y + 0.80000000000000004D + (random.nextFloat() *
		 * 0.59999999999999998D), (z + (random.nextFloat() *
		 * 1.3999999999999999D)) - 0.20000000000000001D, 0.0D, 0.0D, 0.0D); }
		 */
	}

	@Override
	public int tickRate(World par1World) {
		return 2;
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random random) {
		final TileEntityBlockBrainLightSensor tileentity = (TileEntityBlockBrainLightSensor) world
				.getTileEntity(x, y, z);

		if (tileentity != null) {
			if (tileentity.getState()) {
				final boolean power = tileentity.getPowerOn();
				final boolean direction = tileentity.getDirection();
				final int tmpWorldLight = tileentity.getCurLightLevel();
				final int worldLight = world.getBlockLightValue(x, y + 1, z);
				final int lightLevel = tileentity.getLightLevel();

				final boolean tmpPower = direction ? worldLight <= lightLevel
						: worldLight >= lightLevel;

				if ((tmpPower != power) || (tmpWorldLight != worldLight)) {
					smoke(world, x, y, z, random);

					tileentity.setPowerOn(tmpPower);
					tileentity.setCurLightLevel(worldLight);

					try {
						tileentity.update(false);
					} catch (final IOException e) {
						BSP.logException(e);
					}

					world.notifyBlocksOfNeighborChange(x, y, z, this);
					world.notifyBlocksOfNeighborChange(x - 1, y, z, this);
					world.notifyBlocksOfNeighborChange(x + 1, y, z, this);
					world.notifyBlocksOfNeighborChange(x, y - 1, z, this);
					world.notifyBlocksOfNeighborChange(x, y + 1, z, this);
					world.notifyBlocksOfNeighborChange(x, y, z - 1, this);
					world.notifyBlocksOfNeighborChange(x, y, z + 1, this);
				}
			} else {
				final int worldLight = world.getBlockLightValue(x, y + 1, z);
				final short tmpPower = tileentity.getPower();
				final short power = (short) (tileentity.getDirection() ? worldLight
						: 15 - worldLight);

				if (tmpPower != power) {
					tileentity.setPower(power);

					try {
						tileentity.update(false);
					} catch (final IOException e) {
						BSP.logException(e);
					}

					world.notifyBlocksOfNeighborChange(x, y, z, this);
					world.notifyBlocksOfNeighborChange(x - 1, y, z, this);
					world.notifyBlocksOfNeighborChange(x + 1, y, z, this);
					world.notifyBlocksOfNeighborChange(x, y - 1, z, this);
					world.notifyBlocksOfNeighborChange(x, y + 1, z, this);
					world.notifyBlocksOfNeighborChange(x, y, z - 1, this);
					world.notifyBlocksOfNeighborChange(x, y, z + 1, this);
				}
			}

			world.scheduleBlockUpdate(x, y, z, this, tickRate(world));
		} else {
			BSP.fatal("Die TileEntity fehlt!");
		}
	}
}