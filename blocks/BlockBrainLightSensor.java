package mods.brainstone.blocks;

import java.io.IOException;
import java.util.Random;

import mods.brainstone.BrainStone;
import mods.brainstone.templates.BSP;
import mods.brainstone.templates.BlockBrainStoneContainerBase;
import mods.brainstone.tileentities.TileEntityBlockBrainLightSensor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBrainLightSensor extends BlockBrainStoneContainerBase {
	public static Icon[] textures;

	/**
	 * Constructor of the block. Registers all properties and sets the id and
	 * the material
	 * 
	 * @param i
	 *            The internal BrainStone id
	 */
	public BlockBrainLightSensor(int i) {
		super(BrainStone.getId(i), Material.rock);

		this.setHardness(2.4F);
		this.setResistance(0.5F);
		this.setUnlocalizedName("brainLightSensor");
		this.setCreativeTab(CreativeTabs.tabRedstone);

		blockParticleGravity = -0.2F;
	}

	@Override
	public void breakBlock(World world, int i, int j, int k, int par5, int par6) {
		world.removeBlockTileEntity(i, j, k);
		world.notifyBlocksOfNeighborChange(i, j, k, blockID);
		world.notifyBlocksOfNeighborChange(i - 1, j, k, blockID);
		world.notifyBlocksOfNeighborChange(i + 1, j, k, blockID);
		world.notifyBlocksOfNeighborChange(i, j - 1, k, blockID);
		world.notifyBlocksOfNeighborChange(i, j + 1, k, blockID);
		world.notifyBlocksOfNeighborChange(i, j, k - 1, blockID);
		world.notifyBlocksOfNeighborChange(i, j, k + 1, blockID);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityBlockBrainLightSensor();
	}

	@Override
	public Icon getBlockTextureFromSideAndMetadata(int i, int meta) {
		if (i == 1)
			return textures[0];
		else if (i == 0)
			return textures[2];
		else
			return textures[1];
	}

	@Override
	public int isProvidingStrongPower(IBlockAccess iblockaccess, int i, int j,
			int k, int l) {
		final TileEntityBlockBrainLightSensor tileentityblockbrainlightsensor = (TileEntityBlockBrainLightSensor) iblockaccess
				.getBlockTileEntity(i, j, k);
		return ((tileentityblockbrainlightsensor != null) && tileentityblockbrainlightsensor
				.getPowerOn()) ? 15 : 0;
	}

	@Override
	public int isProvidingWeakPower(IBlockAccess iblockaccess, int i, int j,
			int k, int l) {
		return this.isProvidingStrongPower(iblockaccess, i, j, k, l);
	}

	@Override
	public boolean onBlockActivated(World world, int i, int j, int k,
			EntityPlayer entityplayer, int unknown, float px, float py, float pz) {
		final TileEntityBlockBrainLightSensor tileentityblockbrainlightsensor = (TileEntityBlockBrainLightSensor) world
				.getBlockTileEntity(i, j, k);

		if (tileentityblockbrainlightsensor == null) {
			BSP.println("The TileEntity is null!");

			return false;
		} else {
			entityplayer.openGui(BrainStone.instance, 0, world, i, j, k);
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
	public void registerIcons(IconRegister IconReg) {
		textures = new Icon[] {
				IconReg.registerIcon("brainstone:brainLightSensor"),
				IconReg.registerIcon("furnace_side"),
				IconReg.registerIcon("furnace_top") };
	}

	@Override
	public int tickRate(World par1World) {
		return 2;
	}

	@Override
	public void updateTick(World world, int i, int j, int k, Random random) {
		final TileEntityBlockBrainLightSensor tileentityblockbrainlightsensor = (TileEntityBlockBrainLightSensor) world
				.getBlockTileEntity(i, j, k);

		if (tileentityblockbrainlightsensor != null) {
			final boolean flag = tileentityblockbrainlightsensor.getPowerOn();
			final boolean flag1 = tileentityblockbrainlightsensor
					.getDirection();
			final int l = world.getBlockLightValue(i, j + 1, k);
			final int i1 = tileentityblockbrainlightsensor.getLightLevel();

			tileentityblockbrainlightsensor.setCurLightLevel(l);

			final boolean flag2 = flag1 ? l <= i1 : l >= i1;

			if (flag2 != flag) {
				BrainStone.proxy.getClient().sndManager.playSoundFX(
						"random.click", 1.0F, 1.0F);
				int j1 = random.nextInt(11) + 5;

				for (int k1 = 0; k1 < j1; k1++) {
					world.spawnParticle(
							"smoke",
							(i + (random.nextFloat() * 1.3999999999999999D)) - 0.20000000000000001D,
							j
									+ 0.80000000000000004D
									+ (random.nextFloat() * 0.59999999999999998D),
							(k + (random.nextFloat() * 1.3999999999999999D)) - 0.20000000000000001D,
							0.0D, 0.0D, 0.0D);
				}

				j1 = random.nextInt(3);

				for (int l1 = 0; l1 < j1; l1++) {
					world.spawnParticle(
							"largesmoke",
							(i + (random.nextFloat() * 1.3999999999999999D)) - 0.20000000000000001D,
							j
									+ 0.80000000000000004D
									+ (random.nextFloat() * 0.59999999999999998D),
							(k + (random.nextFloat() * 1.3999999999999999D)) - 0.20000000000000001D,
							0.0D, 0.0D, 0.0D);
				}
			}

			tileentityblockbrainlightsensor.setPowerOn(flag2);
			// world.markBlockForUpdate(i, j, k);
			world.notifyBlocksOfNeighborChange(i, j, k, blockID);
			world.notifyBlocksOfNeighborChange(i - 1, j, k, blockID);
			world.notifyBlocksOfNeighborChange(i + 1, j, k, blockID);
			world.notifyBlocksOfNeighborChange(i, j - 1, k, blockID);
			world.notifyBlocksOfNeighborChange(i, j + 1, k, blockID);
			world.notifyBlocksOfNeighborChange(i, j, k - 1, blockID);
			world.notifyBlocksOfNeighborChange(i, j, k + 1, blockID);

			try {
				tileentityblockbrainlightsensor.update(false);
			} catch (final IOException e) {
				BSP.printException(e);
			}

			world.scheduleBlockUpdate(i, j, k, blockID, this.tickRate(world));
		} else {
			BSP.println("Die TileEntity fehlt!");
		}
	}
}