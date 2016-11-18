package brainstonemod.common.worldgenerators;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import brainstonemod.BrainStone;
import brainstonemod.common.helper.BSP;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

public class BrainStoneWorldGeneratorBrainStoneDungeon extends WorldGenerator {
	@Deprecated
	private int x, y, z;
	private BlockPos pos;
	private World world;
	private Random random;
	private final int[] Options;
	private MinecraftServer minecraftServer;
	private TemplateManager templateManager;
	private Rotation rotation;

	private final static ResourceLocation REPLACEMENT_WOOD = new ResourceLocation(BrainStone.RESOURCE_PACKAGE,
			"house/brainstone_house_replacement_wood");
	private final static ResourceLocation SHACK = new ResourceLocation(BrainStone.RESOURCE_PACKAGE,
			"house/brainstone_house_shack");

	public BrainStoneWorldGeneratorBrainStoneDungeon() {
		// height of the stairs

		Options = new int[] { 2 };
	}

	private boolean canPlaceSecretRoomHere() {
		int i, j, k;
		final int height = (Options[0] * 8) + 3;

		for (i = 3; i < 10; i++) {
			for (j = -1; j < 6; j++) {
				for (k = height + 1; k > (height - 8); k--)
					if (isNotSolid(x + i, y - k, z + j))
						return false;
			}
		}

		return true;
	}

	private boolean canPlaceShackHere() {
		int i, j, k;

		for (i = 0; i < 6; i++) {
			for (j = 0; j < 5; j++)
				if (isNotSolid(x + i, y - 1, z + j))
					return false;
		}

		for (i = 0; i < 4; i++)
			if (isNotSolid(x + i, y - 1, z + 5))
				return false;

		for (i = 0; i < 6; i++) {
			for (j = 0; j < 5; j++) {
				for (k = 0; k < 4; k++)
					if (!isReplaceable(x + i, y + k, z + j))
						return false;
			}
		}

		for (i = 0; i < 4; i++) {
			for (j = 0; j < 4; j++)
				if (!isReplaceable(x + i, y + j, z + 5))
					return false;
		}

		if (!(isReplaceable(x + 4, y + 2, z + 5) && isReplaceable(x + 5, y + 2, z + 5)))
			return false;

		for (i = -1; i < 5; i++)
			if (!isReplaceable(x + i, y + 2, z + 6))
				return false;

		for (i = -1; i < 6; i++)
			if (!isReplaceable(x - 1, y + 2, z + i))
				return false;

		for (i = 0; i < 7; i++)
			if (!isReplaceable(x + i, y + 2, z - 1))
				return false;

		for (i = 0; i < 6; i++)
			if (!isReplaceable(x + 6, y + 2, z + i))
				return false;

		for (i = 1; i < 4; i++) {
			for (j = 1; j < 5; j++)
				if (!isReplaceable(x + j, y + 4, z + i))
					return false;
		}

		return (isReplaceable(x + 1, y + 4, z + 4) && isReplaceable(x + 2, y + 4, z + 4))
				&& isReplaceable(x + 2, y + 5, z + 2) && isReplaceable(x + 3, y + 5, z + 2);

	}

	private boolean canPlaceStairsHere() {
		int i, j, k;

		final int height = (Options[0] * 8) + 5;

		for (i = -1; i < 6; i++) {
			for (j = -1; j < 6; j++) {
				for (k = 2; k < height; k++)
					if (isNotSolid(x + i, y - k, z + j))
						return false;
			}
		}

		return true;
	}

	private boolean canPlaceStructHere(int structure) {
		switch (structure) {
		case 0:
			return canPlaceShackHere();
		case 1:
			return canPlaceStairsHere() && canPlaceSecretRoomHere();
		default:
			return false;
		}
	}

	@Override
	public boolean generate(World world, Random random, BlockPos pos) {
		this.world = world;
		this.random = random;
		x = pos.getX();
		y = world.getHeight(pos).getY();
		z = pos.getZ();
		this.pos = new BlockPos(x, y, z);
		minecraftServer = world.getMinecraftServer();
		templateManager = world.getSaveHandler().getStructureTemplateManager();
		Rotation[] allRotations = Rotation.values();
		rotation = allRotations[random.nextInt(allRotations.length)];

		BSP.info("Trying at " + x + ", " + y + ", " + z + "!");

		int counter = 0;
		int direction = 0;
		int directionCounter = 0;
		int maxDirection = 1;

		while (true) {
			if (directionCounter >= maxDirection) {
				directionCounter = 0;
				maxDirection += direction % 2;
				direction = (direction + 1) % 4;
			}

			y = world.getHeight(pos).getY();

			if (canPlaceStructHere(0)) {
				Options[0] = 3;

				if (canPlaceStructHere(1)) {
					break;
				}

				Options[0] = 2;

				if (canPlaceStructHere(1)) {
					break;
				}

				Options[0] = 4;

				if (canPlaceStructHere(1)) {
					break;
				}
			}

			switch (direction) {
			case 0:
				this.x++;
				break;
			case 1:
				this.z++;
				break;
			case 2:
				this.x--;
				break;
			case 3:
				this.z--;
				break;
			default:
				// Shouldn't be here
				break;
			}

			if (counter >= 10000) {
				BSP.info("Failed");

				return false;
			}

			counter++;
			directionCounter++;
		}

		generateShack();

		// generateStairs();

		// generateSecretRoom();

		BSP.info("Placed at " + x + ", " + y + ", " + z + "!");

		return true;
	}

	private void generateSecretRoom() {
		int i, j, k;
		int height = (Options[0] * 8) + 3;
		final ResourceLocation lootTable = new ResourceLocation(BrainStone.RESOURCE_PACKAGE,
				"chests/brainstone_house_bottom");

		for (i = 4; i < 9; i++) {
			for (j = 0; j < 5; j++) {
				for (k = height; k > (height - 7); k--) {
					setBlock(x + i, y - k, z + j,
							((i == 4) || (i == 8) || (j == 0) || (j == 4) || (k == height) || (k == (height - 6)))
									? Blocks.COBBLESTONE
									: Blocks.AIR);
				}
			}
		}

		setBlock(x + 6, (y - height) + 6, z + 2, BrainStone.brainStone());
		setBlock(x + 6, y - height, z + 2, BrainStone.brainStone());

		setBlock(x + 7, y - height - 2, z + 2, BrainStone.pulsatingBrainStone());

		height -= 1;

		// Chests

		setBlock(x + 6, y - height, z + 1, Blocks.CHEST);
		TileEntityChest chest = (TileEntityChest) world.getTileEntity(new BlockPos(x + 6, y - height, z + 1));
		chest.setLootTable(lootTable, random.nextInt());

		setBlock(x + 7, y - height, z + 1, Blocks.CHEST);
		chest = (TileEntityChest) world.getTileEntity(new BlockPos(x + 7, y - height, z + 1));
		chest.setLootTable(lootTable, random.nextInt());

		setBlock(x + 6, y - height, z + 3, Blocks.CHEST);
		chest = (TileEntityChest) world.getTileEntity(new BlockPos(x + 6, y - height, z + 3));
		chest.setLootTable(lootTable, random.nextInt());

		setBlock(x + 7, y - height, z + 3, Blocks.CHEST);
		chest = (TileEntityChest) world.getTileEntity(new BlockPos(x + 7, y - height, z + 3));
		chest.setLootTable(lootTable, random.nextInt());
	}

	private void generateShack() {
		Template templateShack = templateManager.getTemplate(minecraftServer, SHACK);
		Template templateReplacementWood = templateManager.getTemplate(minecraftServer, REPLACEMENT_WOOD);
		PlacementSettings placementSettings = (new PlacementSettings()).setRotation(rotation);
		List<BlockPos> dataBlocks = templateShack.getDataBlocks(pos, placementSettings).entrySet().stream()
				.filter(entry -> entry.getValue().equals("chest")).map(entry -> entry.getKey())
				.collect(Collectors.toList());
		BlockPos chestPos = dataBlocks.remove(random.nextInt(dataBlocks.size()));

		templateShack.addBlocksToWorld(world, pos, placementSettings);

		world.setBlockState(chestPos, Blocks.CHEST.getDefaultState().withRotation(rotation));
		TileEntityChest chestTE = (TileEntityChest) world.getTileEntity(chestPos);
		chestTE.setLootTable(new ResourceLocation(BrainStone.RESOURCE_PACKAGE, "chests/brainstone_house_top"),
				random.nextLong());

		for (BlockPos woodPos : dataBlocks) {
			templateReplacementWood.addBlocksToWorld(world, woodPos, placementSettings);
		}

		// Testing
		world.setBlockState(pos, Blocks.COBBLESTONE.getDefaultState());
		world.setBlockState(pos.add(templateShack.getSize()), Blocks.COBBLESTONE.getDefaultState());
	}

	private void generateStairs() {
		int i, j, k, l;

		// Top Layer

		for (i = 0; i < 5; i++) {
			for (j = 0; j < 5; j++) {
				setBlock(x + i, y - 2, z + j, Blocks.COBBLESTONE);
			}
		}

		for (i = 1; i < 4; i++) {
			setBlock(x + 3, y - 2, z + i, Blocks.AIR);
		}

		setBlock(x + 3, y - 2, z + 2, Blocks.TORCH);
		setBlockAndMetadata(x + 2, y - 2, z + 1, Blocks.STONE_STAIRS, 1);

		// Stairs Down

		final int[] tmpX = new int[] { 2, 3, 3, 3, 2, 1, 1, 1 };
		final int[] tmpZ = new int[] { 1, 1, 2, 3, 3, 3, 2, 1 };
		int tmp, tmp2, height;

		for (l = 0; l < Options[0]; l++) {
			height = (l * 8) + 3;

			// Walls

			for (i = 0; i < 5; i++) {
				for (j = 0; j < 5; j++) {
					for (k = 0; k < 8; k++) {
						setBlock(x + i, y - height - k, z + j,
								(((i == 1) || (i == 3) || (j == 1) || (j == 3))
										&& !((i == 0) || (i == 4) || (j == 0) || (j == 4))) ? Blocks.AIR
												: Blocks.COBBLESTONE);
					}
				}
			}

			// Actual Stairs

			for (i = 0; i < 8; i++) {
				setBlock(x + tmpX[i], y - height - i, z + tmpZ[i], Blocks.COBBLESTONE);

				tmp = (i + 1) % 8;
				tmp2 = i / 2;
				setBlockAndMetadata(x + tmpX[tmp], y - height - i, z + tmpZ[tmp], Blocks.STONE_STAIRS,
						(tmp2 == 0) ? 3 : (tmp2 == 1) ? 0 : (tmp2 == 2) ? 2 : 1);

				tmp = (i + 7) % 8;
				tmp2 = ((i + 1) / 2) % 4;
				setBlockAndMetadata(x + tmpX[tmp], y - height - i, z + tmpZ[tmp], Blocks.STONE_STAIRS,
						(tmp2 == 0) ? 7 : (tmp2 == 1) ? 4 : (tmp2 == 2) ? 6 : 5);

				if ((i % 2) == 1) {
					tmp = (i + 3) % 8;
					tmp2 = i / 2;
					setBlockAndMetadata(x + tmpX[tmp], y - height - i, z + tmpZ[tmp], Blocks.TORCH,
							(tmp2 == 0) ? 3 : (tmp2 == 1) ? 2 : (tmp2 == 2) ? 4 : 1);
				}

			}
		}

		height = (Options[0] * 8) + 3;

		for (i = 0; i < 5; i++) {
			for (j = 0; j < 5; j++) {
				setBlock(x + i, y - height, z + j, Blocks.COBBLESTONE);
			}
		}
	}

	private boolean isReplaceable(int x, int y, int z) {
		return world.getBlockState(new BlockPos(x, y, z)).getMaterial().isReplaceable();
	}

	private boolean isNotSolid(int x, int y, int z) {
		return !world.getBlockState(new BlockPos(x, y, z)).getMaterial().isSolid();
	}

	/**
	 * Sets a Block at the specified Spot
	 */
	@Deprecated
	private void setBlock(int x, int y, int z, Block blockId) {
		setBlockAndMetadata(x, y, z, blockId, 0);
	}

	/**
	 * Sets a Block with MetaData at the specified Spot
	 */
	@Deprecated
	private void setBlockAndMetadata(int x, int y, int z, Block blockId, int metaData) {
		world.setBlockState(new BlockPos(x, y, z), blockId.getStateFromMeta(metaData), 2);
	}
}
