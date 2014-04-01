package brainstonemod.common.block;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import brainstonemod.BrainStone;
import brainstonemod.handlers.BrainStonePacketHandler;
import brainstonemod.templates.BSP;
import brainstonemod.tileentities.TileEntityBlockBrainStoneTrigger;

public class BlockBrainStoneTrigger extends BlockBrainStoneHiders {
	public static LinkedHashMap<String, Class[]> triggerEntities;
	public static Icon[] textures;

	/**
	 * Constructor of the block. Registers all properties and sets the id and
	 * the material
	 * 
	 * @param i
	 *            The internal BrainStone id
	 */
	public BlockBrainStoneTrigger(int i) {
		super(i);

		this.setHardness(2.4F);
		this.setResistance(0.5F);
		this.setUnlocalizedName("brainStoneTrigger");
		// setRequiresSelfNotify();

		blockParticleGravity = -0.2F;
	}

	@Override
	public void breakBlock(World world, int i, int j, int k, int par5, int par6) {
		final TileEntityBlockBrainStoneTrigger tileentityblockbrainstonetrigger = (TileEntityBlockBrainStoneTrigger) world
				.getBlockTileEntity(i, j, k);

		if (tileentityblockbrainstonetrigger != null) {
			tileentityblockbrainstonetrigger.dropItems(world, i, j, k);
		}

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
		return new TileEntityBlockBrainStoneTrigger();
	}

	@Override
	public Icon getBlockTexture(IBlockAccess iblockaccess, int i, int j, int k,
			int l) {
		if (l == 1) {
			final TileEntityBlockBrainStoneTrigger tileentityblockbrainstonetrigger = (TileEntityBlockBrainStoneTrigger) iblockaccess
					.getBlockTileEntity(i, j, k);

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
	public Icon getIcon(int i, int meta) {
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
				.getBlockTileEntity(i, j, k);
		return ((tileentityblockbrainstonetrigger != null) && (tileentityblockbrainstonetrigger.delay > 0)) ? tileentityblockbrainstonetrigger.output_buffered
				: 0;
	}

	@Override
	public int isProvidingWeakPower(IBlockAccess iblockaccess, int i, int j,
			int k, int l) {
		return this.isProvidingStrongPower(iblockaccess, i, j, k, l);
	}

	@Override
	public boolean onBlockActivated(World world, int i, int j, int k,
			EntityPlayer entityplayer, int par6, float par7, float par8,
			float par9) {
		if (world.isRemote)
			return true;

		final TileEntityBlockBrainStoneTrigger tileentityblockbrainstonetrigger = (TileEntityBlockBrainStoneTrigger) world
				.getBlockTileEntity(i, j, k);

		if (tileentityblockbrainstonetrigger != null) {
			entityplayer.openGui(BrainStone.instance, 1, world, i, j, k);
		}

		return true;
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
		final List list = world.getEntitiesWithinAABBExcludingEntity(null,
				AxisAlignedBB.getBoundingBox(i, j + 1, k, i + 1, j + 2, k + 1));
		final TileEntityBlockBrainStoneTrigger tileentityblockbrainstonetrigger = (TileEntityBlockBrainStoneTrigger) world
				.getBlockTileEntity(i, j, k);

		if (tileentityblockbrainstonetrigger == null)
			return 0;

		byte count = 0;

		for (int l = 0; (l < list.size()) && (count < 15); l++) {
			final Class entity = ((Entity) list.get(l)).getClass();

			if (entity == null) {
				BSP.severe("Fehler! Die Entity ist nicht vorhanden!");
				continue;
			}

			final int length = triggerEntities.size();
			final String[] keys = triggerEntities.keySet().toArray(
					new String[length]);
			String key;
			Class[] classes;

			for (int count1 = 0; (count1 < length) && (count < 15); count1++) {
				key = keys[count1];

				if (tileentityblockbrainstonetrigger.getMobTriggered(key)) {
					classes = triggerEntities.get(key);

					for (int a = 0; a < classes.length; a++) {
						if (classes[a].isAssignableFrom(entity)) {
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
	public void updateTick(World world, int i, int j, int k, Random random) {
		final TileEntityBlockBrainStoneTrigger tileentityblockbrainstonetrigger = (TileEntityBlockBrainStoneTrigger) world
				.getBlockTileEntity(i, j, k);

		if (tileentityblockbrainstonetrigger == null) {
			world.scheduleBlockUpdate(i, j, k, blockID, this.tickRate(world));
			return;
		}

		tileentityblockbrainstonetrigger.output = this.triggerCorrectMob(world,
				i, j, k);
		if (tileentityblockbrainstonetrigger.output > 0) {
			tileentityblockbrainstonetrigger.output_buffered = tileentityblockbrainstonetrigger.output;
		}

		tileentityblockbrainstonetrigger.delay = (byte) ((tileentityblockbrainstonetrigger.output > 0) ? tileentityblockbrainstonetrigger.max_delay
				: tileentityblockbrainstonetrigger.delay <= 0 ? 0
						: tileentityblockbrainstonetrigger.delay - 1);

		if (tileentityblockbrainstonetrigger.checkForSlotChange()) {
			world.markBlockForUpdate(i, j, k);
			BrainStonePacketHandler.sendReRenderBlockAtPacket(i, j, k, world);
		}

		world.notifyBlocksOfNeighborChange(i, j, k, blockID);
		world.notifyBlocksOfNeighborChange(i - 1, j, k, blockID);
		world.notifyBlocksOfNeighborChange(i + 1, j, k, blockID);
		world.notifyBlocksOfNeighborChange(i, j - 1, k, blockID);
		world.notifyBlocksOfNeighborChange(i, j + 1, k, blockID);
		world.notifyBlocksOfNeighborChange(i, j, k - 1, blockID);
		world.notifyBlocksOfNeighborChange(i, j, k + 1, blockID);

		world.scheduleBlockUpdate(i, j, k, blockID, this.tickRate(world));
	}
}
