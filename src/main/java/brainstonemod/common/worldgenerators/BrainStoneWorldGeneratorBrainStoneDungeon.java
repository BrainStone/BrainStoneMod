package brainstonemod.common.worldgenerators;

import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.stream.Collectors;

import brainstonemod.BrainStone;
import brainstonemod.common.helper.BSP;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
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
	private BlockPos basePos;
	private BlockPos baseSize;
	private World world;
	private Random random;
	private final int[] Options;
	private MinecraftServer minecraftServer;
	private TemplateManager templateManager;
	private Rotation[] allRotations;

	private final static ResourceLocation LOOT_TABLE_TOP = new ResourceLocation(BrainStone.RESOURCE_PACKAGE,
			"chests/house/top");
	private final static ResourceLocation LOOT_TABLE_BOTTOM = new ResourceLocation(BrainStone.RESOURCE_PACKAGE,
			"chests/house/bottom");

	private final static ResourceLocation CHAMBER = new ResourceLocation(BrainStone.RESOURCE_PACKAGE, "house/chamber");
	private final static ResourceLocation REPLACEMENT_STONE = new ResourceLocation(BrainStone.RESOURCE_PACKAGE,
			"house/replacement_stone");
	private final static ResourceLocation REPLACEMENT_WOOD = new ResourceLocation(BrainStone.RESOURCE_PACKAGE,
			"house/replacement_wood");
	private final static ResourceLocation SHACK = new ResourceLocation(BrainStone.RESOURCE_PACKAGE, "house/shack");
	private final static ResourceLocation STARIS = new ResourceLocation(BrainStone.RESOURCE_PACKAGE, "house/stairs");
	private final static ResourceLocation STARIS_END = new ResourceLocation(BrainStone.RESOURCE_PACKAGE,
			"house/stairs_end");

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
		basePos = world.getHeight(pos);
		x = basePos.getX();
		y = basePos.getY();
		z = basePos.getZ();
		minecraftServer = world.getMinecraftServer();
		templateManager = world.getSaveHandler().getStructureTemplateManager();
		allRotations = Rotation.values();

		BSP.trace("Trying at " + x + ", " + y + ", " + z + "!");

		int counter = 0;
		int direction = 0;
		int directionCounter = 0;
		int maxDirection = 1;

		while (true) {
			// TODO: New check for placability!
			if (Math.sqrt(4) == 2)
				break;

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
				BSP.debug("Failed");

				return false;
			}

			counter++;
			directionCounter++;
		}

		generateShack();
		generateStairCase();

		BSP.trace("Placed at " + x + ", " + y + ", " + z + "!");

		return true;
	}

	private void generateShack() {
		Rotation rotation = allRotations[random.nextInt(allRotations.length)];
		Template templateShack = templateManager.getTemplate(minecraftServer, SHACK);
		Template templateReplacementWood = templateManager.getTemplate(minecraftServer, REPLACEMENT_WOOD);
		PlacementSettings placementSettings = (new PlacementSettings()).setRotation(rotation);
		BlockPos housePos = templateShack.getZeroPositionWithTransform(basePos, null, rotation);
		baseSize = templateShack.transformedSize(rotation);
		List<BlockPos> dataBlocks = templateShack.getDataBlocks(housePos, placementSettings).entrySet().stream()
				.filter(entry -> entry.getValue().equals("chest")).map(entry -> entry.getKey())
				.collect(Collectors.toList());
		BlockPos chestPos = dataBlocks.remove(random.nextInt(dataBlocks.size()));

		templateShack.addBlocksToWorld(world, housePos, placementSettings);

		world.setBlockState(chestPos, Blocks.CHEST.getDefaultState().withRotation(rotation));
		TileEntityChest chestTE = (TileEntityChest) world.getTileEntity(chestPos);
		chestTE.setLootTable(LOOT_TABLE_TOP, random.nextLong());

		for (BlockPos woodPos : dataBlocks) {
			templateReplacementWood.addBlocksToWorld(world, woodPos, placementSettings);
		}
	}

	private void generateStairCase() {
		Rotation rotation = allRotations[random.nextInt(allRotations.length)];
		IBlockState chestDefaultState = Blocks.CHEST.getDefaultState();
		IBlockState chestState;
		Template templateChamber = templateManager.getTemplate(minecraftServer, CHAMBER);
		Template templateStairs = templateManager.getTemplate(minecraftServer, STARIS);
		Template templateStairsEnd = templateManager.getTemplate(minecraftServer, STARIS_END);
		Template templateReplacementStone = templateManager.getTemplate(minecraftServer, REPLACEMENT_STONE);
		PlacementSettings placementSettings = (new PlacementSettings()).setRotation(rotation);
		BlockPos stairSize = templateStairs.transformedSize(rotation);
		BlockPos stairEndSize = templateStairsEnd.transformedSize(rotation);
		BlockPos chestPos;
		BlockPos stairBase = templateStairs.getDataBlocks(new BlockPos(0, 0, 0), placementSettings).entrySet()
				.iterator().next().getKey();
		BlockPos stairPos = basePos.subtract(stairBase).add(random.nextInt(baseSize.getX() - 2) + 1, -1,
				random.nextInt(baseSize.getZ() - 2) + 1);
		TileEntityChest chestTE;
		int max = random.nextInt(3);

		for (int i = -1; i <= max; i++) {
			stairPos = stairPos.add(0, -stairSize.getY(), 0);

			templateStairs.addBlocksToWorld(world, stairPos, placementSettings);
			templateReplacementStone.addBlocksToWorld(world, stairPos.add(stairBase), placementSettings);
		}

		stairPos = stairPos.add(0, -stairEndSize.getY(), 0);

		templateStairsEnd.addBlocksToWorld(world, stairPos, placementSettings);

		stairPos = stairPos.offset(rotation.rotate(EnumFacing.EAST),
				((rotation == Rotation.NONE) || (rotation == Rotation.CLOCKWISE_180)) ? stairEndSize.getZ()
						: stairEndSize.getX())
				.add(0, -2, 0);

		templateChamber.addBlocksToWorld(world, stairPos, placementSettings);

		for (Entry<BlockPos, String> dataBlock : templateChamber.getDataBlocks(stairPos, placementSettings)
				.entrySet()) {
			if (dataBlock.getValue().equals("chest_north")) {
				chestState = chestDefaultState.withRotation(rotation);
			} else if (dataBlock.getValue().equals("chest_south")) {
				chestState = chestDefaultState.withRotation(rotation.add(Rotation.CLOCKWISE_180));
			} else {
				continue;
			}

			chestPos = dataBlock.getKey();

			world.setBlockState(chestPos, chestState);
			chestTE = (TileEntityChest) world.getTileEntity(chestPos);
			chestTE.setLootTable(LOOT_TABLE_BOTTOM, random.nextLong());
		}
	}

	private boolean isReplaceable(int x, int y, int z) {
		return world.getBlockState(new BlockPos(x, y, z)).getMaterial().isReplaceable();
	}

	private boolean isNotSolid(int x, int y, int z) {
		return !world.getBlockState(new BlockPos(x, y, z)).getMaterial().isSolid();
	}
}
