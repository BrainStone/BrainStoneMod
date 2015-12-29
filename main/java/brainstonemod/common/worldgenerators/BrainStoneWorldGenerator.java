package brainstonemod.common.worldgenerators;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import brainstonemod.BrainStone;
import cpw.mods.fml.common.IWorldGenerator;

// TODO split into 2 classes for separate generation of the ore(s) and the dungeon
public class BrainStoneWorldGenerator implements IWorldGenerator {
	/** Temporary storage of the random parameter in generate */
	private Random random;
	/** Temporary storage of the chunkX parameter in generate */
	private int chunkX;
	/** Temporary storage of the chunkZ parameter in generate */
	private int chunkZ;
	/** Temporary storage of the World parameter in generate */
	private World world;

	private void genBrainStoneDungeon() {
		if (random.nextInt(1000) == 0) {
			final BrainStoneWorldGeneratorBrainStoneDungeon name = new BrainStoneWorldGeneratorBrainStoneDungeon();

			name.generate(world, random, chunkX, 0, chunkZ);
		}
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator,
			IChunkProvider chunkProvider) {
		this.random = random;
		this.chunkX = chunkX * 16;
		this.chunkZ = chunkZ * 16;
		this.world = world;
		switch (this.world.provider.dimensionId) {
		case -1: // Nether
			break;
		case 0: // Overworld
			this.genMinable(BrainStone.brainStoneOre(), 20, 1, 32);

			genBrainStoneDungeon();

			break;
		case 1: // End
			break;
		// TODO: Move to config
		case 7: // Twilight Forest
		case -100: // Deep Dark
			this.genMinable(BrainStone.brainStoneOre(), 20, 1, 32);

			break;
		}
	}

	/**
	 * Generates a vein. The minimum height is 0.
	 * 
	 * @param block
	 *            The blockId of the ore
	 * @param size
	 *            The size of the vein
	 * @param perChunk
	 *            How many veins per chunk
	 * @param high
	 *            The maximum height
	 */
	private void genMinable(Block block, int size, int perChunk, int high) {
		this.genMinable(block, size, perChunk, high, 0);
	}

	/**
	 * Generates a vein.
	 * 
	 * @param block
	 *            The blockIdd of the ore
	 * @param size
	 *            The size of the vein
	 * @param perChunk
	 *            How many veins per chunk
	 * @param high
	 *            The maximum height
	 * @param low
	 *            The minimum height
	 */
	private void genMinable(Block block, int size, int perChunk, int high, int low) {
		int randPosX;
		int randPosY;
		int randPosZ;

		WorldGenMinable generator = new WorldGenMinable(block, size);

		for (int i = 0; i < perChunk; i++) {
			randPosX = random.nextInt(16) + chunkX;
			randPosY = low + random.nextInt(high - low);
			randPosZ = random.nextInt(16) + chunkZ;

			generator.generate(world, random, randPosX, randPosY, randPosZ);
		}
	}
}
