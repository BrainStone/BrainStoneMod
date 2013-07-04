package mods.brainstonemod.worldgenerators;

import java.util.Random;

import mods.brainstonemod.BrainStone;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import cpw.mods.fml.common.IWorldGenerator;

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
	public void generate(Random random, int chunkX, int chunkZ, World world,
			IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		this.random = random;
		this.chunkX = chunkX * 16;
		this.chunkZ = chunkZ * 16;
		this.world = world;
		switch (world.provider.dimensionId) {
		case -1: // Nether
			break;
		case 0: // Overworld
			this.genMinable(BrainStone.brainStoneOre().blockID, 20, 1, 32);

			this.genBrainStoneDungeon();

			break;
		case 1: // End
			break;
		}
	}

	/**
	 * Generates a vein. The minimum height is 0.
	 * 
	 * @param blockId
	 *            The blockIdd of the ore
	 * @param size
	 *            The size of the vein
	 * @param perChunk
	 *            How many veins per chunk
	 * @param high
	 *            The maximum height
	 */
	private void genMinable(int blockId, int size, int perChunk, int high) {
		this.genMinable(blockId, size, perChunk, high, 0);
	}

	/**
	 * Generates a vein.
	 * 
	 * @param blockId
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
	private void genMinable(int blockId, int size, int perChunk, int high,
			int low) {
		for (int i = 0; i < perChunk; i++) {
			final int randPosX = random.nextInt(16) + chunkX;
			final int randPosY = low + random.nextInt(high - low);
			final int randPosZ = random.nextInt(16) + chunkZ;

			new WorldGenMinable(blockId, size).generate(world, random,
					randPosX, randPosY, randPosZ);
		}
	}
}
