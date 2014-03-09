package brainstonemod.common.block;

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
import net.minecraftforge.common.util.ForgeDirection;
import brainstonemod.BrainStone;
import brainstonemod.common.block.template.BlockBrainStoneContainerBase;
import brainstonemod.common.helper.BSP;
import brainstonemod.common.tileentity.TileEntityBlockBrainLightSensor;
import brainstonemod.network.BrainStonePacketHelper;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

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
		setCreativeTab(CreativeTabs.tabRedstone);
		setHarvestLevel("pickaxe", 1);

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
	public boolean isSideSolid(IBlockAccess world, int x, int y, int z,
			ForgeDirection side) {
		return true;
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
				tickRate(world)
						- ((int) world.getTotalWorldTime() % tickRate(world)));
	}

	@Override
	public void registerBlockIcons(IIconRegister IconReg) {
		textures = new IIcon[] {
				IconReg.registerIcon("brainstonemod:brainLightSensor"),
				IconReg.registerIcon("furnace_side"),
				IconReg.registerIcon("furnace_top") };
	}

	public void smoke(World world, int x, int y, int z, Random random) {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
			world.playSound(x, y, z, "random.click", 1.0F, 1.0F, true);
			int randInt = random.nextInt(5) + random.nextInt(6) + 5;

			int i;
			for (i = 0; i < randInt; i++) {
				world.spawnParticle("smoke",
						(x + (random.nextDouble() * 1.4)) - 0.2, y + 0.8
								+ (random.nextDouble() * 0.6),
						(z + (random.nextDouble() * 1.4)) - 0.2, 0.0D, 0.0D,
						0.0D);
			}

			randInt = random.nextInt(3);

			for (i = 0; i < randInt; i++) {
				world.spawnParticle("largesmoke",
						(x + (random.nextDouble() * 1.4)) - 0.2, y + 0.8
								+ (random.nextDouble() * 0.6),
						(z + (random.nextDouble() * 1.4)) - 0.2, 0.0D, 0.0D,
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
	public void updateTick(World world, int x, int y, int z, Random random) {
		final TileEntityBlockBrainLightSensor tileentity = (TileEntityBlockBrainLightSensor) world
				.getTileEntity(x, y, z);

		if (tileentity != null) {
			if (tileentity.getState()) {
				final boolean power = tileentity.getPowerOn();
				final boolean direction = tileentity.getDirection();
				final int oldWorldLight = tileentity.getOldCurLightLevel();
				final int worldLight = tileentity.getCurLightLevel();
				final int lightLevel = tileentity.getLightLevel();

				final boolean tmpPower = direction ? worldLight <= lightLevel
						: worldLight >= lightLevel;

				if ((tmpPower != power) || (oldWorldLight != worldLight)) {
					BrainStonePacketHelper.sendBrainLightSensorSmokePacket(
							world.provider.dimensionId, x, y, z);

					tileentity.setPowerOn(tmpPower);

					world.markBlockForUpdate(x, y, z);
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
					BrainStonePacketHelper.sendBrainLightSensorSmokePacket(
							world.provider.dimensionId, x, y, z);

					tileentity.setPower(power);

					world.markBlockForUpdate(x, y, z);
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