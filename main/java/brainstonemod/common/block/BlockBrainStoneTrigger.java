package brainstonemod.common.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import brainstonemod.BrainStone;
import brainstonemod.common.block.template.BlockBrainStoneHiders;
import brainstonemod.common.helper.BSP;
import brainstonemod.common.tileentity.TileEntityBlockBrainStoneTrigger;
import brainstonemod.network.BrainStonePacketHelper;

public class BlockBrainStoneTrigger extends BlockBrainStoneHiders {
	public static IIcon[] textures;

	/**
	 * Constructor of the block. Registers all properties and sets the id and
	 * the material
	 * 
	 * @param i
	 *            The internal BrainStone id
	 */
	public BlockBrainStoneTrigger() {
		super();

		setHardness(2.4F);
		setResistance(0.5F);
		setHarvestLevel("pickaxe", 1);
		// setRequiresSelfNotify();

		blockParticleGravity = -0.2F;
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block,
			int meta) {
		final TileEntityBlockBrainStoneTrigger tileentityblockbrainstonetrigger = (TileEntityBlockBrainStoneTrigger) world
				.getTileEntity(x, y, z);

		if (tileentityblockbrainstonetrigger != null) {
			tileentityblockbrainstonetrigger.dropItems(world, x, y, z);
		}

		world.removeTileEntity(x, y, z);
		world.notifyBlocksOfNeighborChange(x, y, z, this);
		world.notifyBlocksOfNeighborChange(x - 1, y, z, this);
		world.notifyBlocksOfNeighborChange(x + 1, y, z, this);
		world.notifyBlocksOfNeighborChange(x, y - 1, z, this);
		world.notifyBlocksOfNeighborChange(x, y + 1, z, this);
		world.notifyBlocksOfNeighborChange(x, y, z - 1, this);
		world.notifyBlocksOfNeighborChange(x, y, z + 1, this);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int par2) {
		return new TileEntityBlockBrainStoneTrigger();
	}

	@Override
	public IIcon getIcon(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		if (l == 1) {
			final TileEntityBlockBrainStoneTrigger tileentityblockbrainstonetrigger = (TileEntityBlockBrainStoneTrigger) iblockaccess
					.getTileEntity(i, j, k);

			if (tileentityblockbrainstonetrigger == null)
				return textures[0];
			else
				return tileentityblockbrainstonetrigger.getTextureId(
						iblockaccess, i, j, k);
		}

		if (l == 0)
			return textures[2];
		else
			return textures[1];
	}

	@Override
	public IIcon getIcon(int i, int meta) {
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
		final TileEntityBlockBrainStoneTrigger tileentityblockbrainstonetrigger = (TileEntityBlockBrainStoneTrigger) iblockaccess
				.getTileEntity(i, j, k);
		return ((tileentityblockbrainstonetrigger != null) && (tileentityblockbrainstonetrigger.delay > 0)) ? tileentityblockbrainstonetrigger.output_buffered
				: 0;
	}

	@Override
	public int isProvidingWeakPower(IBlockAccess iblockaccess, int i, int j,
			int k, int l) {
		return isProvidingStrongPower(iblockaccess, i, j, k, l);
	}

	@Override
	public boolean isSideSolid(IBlockAccess world, int x, int y, int z,
			ForgeDirection side) {
		return true;
	}

	@Override
	public boolean onBlockActivated(World world, int i, int j, int k,
			EntityPlayer entityplayer, int par6, float par7, float par8,
			float par9) {
		if (world.isRemote)
			return true;

		final TileEntityBlockBrainStoneTrigger tileentityblockbrainstonetrigger = (TileEntityBlockBrainStoneTrigger) world
				.getTileEntity(i, j, k);

		if (tileentityblockbrainstonetrigger != null) {
			entityplayer.openGui(BrainStone.instance, 1, world, i, j, k);
		}

		return true;
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
				IconReg.registerIcon("brainstonemod:brainStoneTrigger"),
				IconReg.registerIcon("furnace_side"),
				IconReg.registerIcon("furnace_top") };
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
				AxisAlignedBB.getBoundingBox(i, j + 1, k, i + 1, j + 2, k + 1));
		final TileEntityBlockBrainStoneTrigger tileentityblockbrainstonetrigger = (TileEntityBlockBrainStoneTrigger) world
				.getTileEntity(i, j, k);

		if (tileentityblockbrainstonetrigger == null)
			return 0;

		byte count = 0;

		for (int l = 0; (l < list.size()) && (count < 15); l++) {
			final Class<?> entity = ((Entity) list.get(l)).getClass();

			if (entity == null) {
				BSP.fatal("Fehler! Die Entity ist nicht vorhanden!");
				continue;
			}

			final int length = BrainStone.getSidedTiggerEntities().size();
			final String[] keys = BrainStone.getSidedTiggerEntities().keySet()
					.toArray(new String[length]);
			String key;
			Class<?>[] classes;

			for (int count1 = 0; (count1 < length) && (count < 15); count1++) {
				key = keys[count1];

				if (tileentityblockbrainstonetrigger.getMobTriggered(key)) {
					classes = BrainStone.getSidedTiggerEntities().get(key);

					for (final Class<?> classe : classes) {
						if (classe.isAssignableFrom(entity)) {
							count += tileentityblockbrainstonetrigger
									.getMobPower(key);

							break;
						}
					}
				}
			}
		}

		return (count > 15) ? 15 : count;
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random random) {
		final TileEntityBlockBrainStoneTrigger tileEntityBlockBrainStoneTrigger = (TileEntityBlockBrainStoneTrigger) world
				.getTileEntity(x, y, z);

		if (tileEntityBlockBrainStoneTrigger == null) {
			world.scheduleBlockUpdate(x, y, z, this, tickRate(world));
			return;
		}

		tileEntityBlockBrainStoneTrigger.output = triggerCorrectMob(world, x,
				y, z);
		if (tileEntityBlockBrainStoneTrigger.output > 0) {
			tileEntityBlockBrainStoneTrigger.output_buffered = tileEntityBlockBrainStoneTrigger.output;
		}

		tileEntityBlockBrainStoneTrigger.delay = (byte) ((tileEntityBlockBrainStoneTrigger.output > 0) ? tileEntityBlockBrainStoneTrigger.max_delay
				: tileEntityBlockBrainStoneTrigger.delay <= 0 ? 0
						: tileEntityBlockBrainStoneTrigger.delay - 1);

		if (tileEntityBlockBrainStoneTrigger.checkForSlotChange()) {
			BrainStonePacketHelper.sendReRenderBlockAtPacket(
					world.provider.dimensionId, x, y, z, world);
		}

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
