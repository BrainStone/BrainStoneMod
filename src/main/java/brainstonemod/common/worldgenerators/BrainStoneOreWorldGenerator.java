package brainstonemod.common.worldgenerators;

import brainstonemod.BrainStone;
import brainstonemod.common.config.BrainStoneConfigWrapper;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Random;

public class BrainStoneOreWorldGenerator implements IWorldGenerator {
	/** Temporary storage of the random parameter in generate */
	private Random random;
	/** Temporary storage of the chunkX parameter in generate */
	private int chunkX;
	/** Temporary storage of the chunkZ parameter in generate */
	private int chunkZ;
	/** Temporary storage of the World parameter in generate */
	private World world;

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		this.random = random;
		this.chunkX = chunkX * 16;
		this.chunkZ = chunkZ * 16;
		this.world = world;
		if (ArrayUtils.contains(BrainStoneConfigWrapper.getBrainStoneOreDims(), this.world.provider.getDimension()))
			this.genMinable(BrainStone.brainStoneOre(), 20, 1, 32);
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

		WorldGenMinable generator = new WorldGenMinable(block.getDefaultState(), size);

		for (int i = 0; i < perChunk; i++) {
			randPosX = random.nextInt(16) + chunkX;
			randPosY = low + random.nextInt(high - low);
			randPosZ = random.nextInt(16) + chunkZ;

			generator.generate(world, random, new BlockPos(randPosX, randPosY, randPosZ));
		}
	}
}
