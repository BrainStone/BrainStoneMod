package brainstonemod.common.worldgenerator;

import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;

import brainstonemod.BrainStone;
import brainstonemod.common.config.BrainStoneConfigWrapper;
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
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraftforge.fml.common.IWorldGenerator;

public class BrainStoneHouseWorldGenerator implements IWorldGenerator {
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

	private BlockPos basePos;
	private BlockPos baseSize;
	private World world;
	private Random random;
	private MinecraftServer minecraftServer;
	private TemplateManager templateManager;
	private Rotation[] allRotations;

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		this.random = random;
		basePos = world.getHeight(new BlockPos(chunkX * 16, 0, chunkZ * 16));
		this.world = world;

		if (ArrayUtils.contains(BrainStoneConfigWrapper.getBrainStoneHouseDims(), this.world.provider.getDimension())
				&& (this.random.nextInt(BrainStoneConfigWrapper.getBrainStoneHouseRarity()) == 0)) {
			generate();
		}
	}

	private void generate() {
		minecraftServer = world.getMinecraftServer();
		templateManager = world.getSaveHandler().getStructureTemplateManager();
		allRotations = Rotation.values();
		Rotation rotation = allRotations[random.nextInt(allRotations.length)];
		Template templateShack = templateManager.getTemplate(minecraftServer, SHACK);
		PlacementSettings placementSettings = (new PlacementSettings()).setRotation(rotation);
		BlockPos housePos;
		baseSize = templateShack.transformedSize(rotation);
		int counter = 0;
		int direction = 0;
		int directionCounter = 0;
		int maxDirection = 1;
		int x;
		int y;
		int z;
		boolean locationOk;

		BSP.trace("Trying at " + basePos.getX() + ", " + basePos.getY() + ", " + basePos.getZ() + "!");

		// Find valid location
		while (true) {
			if (directionCounter >= maxDirection) {
				directionCounter = 0;
				maxDirection += direction % 2;
				direction = (direction + 1) % 4;
			}

			basePos = world.getHeight(basePos);
			locationOk = true;

			for (x = 0; locationOk && (x < baseSize.getX()); x++) {
				for (z = 0; locationOk && (z < baseSize.getZ()); z++) {
					locationOk = isSolid(basePos.add(x, -1, z));

					for (y = 0; locationOk && (y < baseSize.getY()); y++) {
						locationOk = !isSolid(basePos.add(x, y, z));
					}
				}
			}

			if (locationOk) {
				housePos = templateShack.getZeroPositionWithTransform(basePos, null, rotation);

				break;
			}

			switch (direction) {
			case 0:
				basePos = basePos.north();
				break;
			case 1:
				basePos = basePos.east();
				break;
			case 2:
				basePos = basePos.south();
				break;
			case 3:
				basePos = basePos.west();
				break;
			default:
				// Shouldn't be here
				break;
			}

			if (counter >= 100000) {
				BSP.debug("Failed");

				return;
			}

			counter++;
			directionCounter++;
		}

		// Generate Shack
		Template templateReplacementWood = templateManager.getTemplate(minecraftServer, REPLACEMENT_WOOD);
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

		generateStairCase();

		BSP.trace("Placed at " + basePos.getX() + ", " + basePos.getY() + ", " + basePos.getZ() + "!");
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

		stairPos = stairPos.add(0, -2, 0).offset(rotation.rotate(EnumFacing.EAST),
				((rotation == Rotation.NONE) || (rotation == Rotation.CLOCKWISE_180)) ? stairEndSize.getZ()
						: stairEndSize.getX());

		templateChamber.addBlocksToWorld(world, stairPos, placementSettings);

		for (Entry<BlockPos, String> dataBlock : templateChamber.getDataBlocks(stairPos, placementSettings)
				.entrySet()) {
			if (dataBlock.getValue().equals("chest_north")) {
				chestState = chestDefaultState.withRotation(rotation);
			} else if (dataBlock.getValue().equals("chest_east")) {
				chestState = chestDefaultState.withRotation(rotation.add(Rotation.CLOCKWISE_90));
			} else if (dataBlock.getValue().equals("chest_south")) {
				chestState = chestDefaultState.withRotation(rotation.add(Rotation.CLOCKWISE_180));
			} else if (dataBlock.getValue().equals("chest_west")) {
				chestState = chestDefaultState.withRotation(rotation.add(Rotation.COUNTERCLOCKWISE_90));
			} else {
				continue;
			}

			chestPos = dataBlock.getKey();

			world.setBlockState(chestPos, chestState);
			chestTE = (TileEntityChest) world.getTileEntity(chestPos);
			if (chestTE != null) {
				chestTE.setLootTable(LOOT_TABLE_BOTTOM, random.nextLong());
			} else {
				BSP.warn("Chest TE was null at " + chestPos + " in " + world.toString());
			}
		}
	}

	private boolean isSolid(BlockPos blockPos) {
		return world.getBlockState(blockPos).getMaterial().isSolid();
	}
}
