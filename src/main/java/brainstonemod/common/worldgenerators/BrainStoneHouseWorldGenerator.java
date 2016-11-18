package brainstonemod.common.worldgenerators;

import java.util.Random;

import org.apache.commons.lang3.ArrayUtils;

import brainstonemod.common.config.BrainStoneConfigWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;

public class BrainStoneHouseWorldGenerator implements IWorldGenerator {
	/** Temporary storage of the random parameter in generate */
	private Random random;
	/** Temporary storage of the chunkX parameter in generate */
	private int chunkX;
	/** Temporary storage of the chunkZ parameter in generate */
	private int chunkZ;
	/** Temporary storage of the World parameter in generate */
	private World world;

	private void genBrainStoneDungeon() {
		if (random.nextInt(BrainStoneConfigWrapper.getBrainStoneHouseRarity()) == 0) {
			final BrainStoneWorldGeneratorBrainStoneDungeon name = new BrainStoneWorldGeneratorBrainStoneDungeon();

			name.generate(world, random, new BlockPos(chunkX, 0, chunkZ));
		}
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		this.random = world.getChunkFromChunkCoords(chunkX, chunkZ).getRandomWithSeed(-8708896470234868L);
		this.chunkX = chunkX * 16;
		this.chunkZ = chunkZ * 16;
		this.world = world;
		if (ArrayUtils.contains(BrainStoneConfigWrapper.getBrainStoneHouseDims(), this.world.provider.getDimension()))
			genBrainStoneDungeon();
	}
}
