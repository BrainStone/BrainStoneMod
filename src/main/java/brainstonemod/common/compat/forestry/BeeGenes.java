package brainstonemod.common.compat.forestry;

import brainstonemod.BrainStone;
import brainstonemod.BrainStoneBlocks;
import forestry.api.apiculture.EnumBeeChromosome;
import forestry.api.apiculture.FlowerManager;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IAlleleFlowers;
import forestry.api.genetics.IChromosomeType;
import forestry.api.genetics.IFlowerProvider;
import forestry.api.genetics.IFlowerRegistry;
import forestry.apiculture.flowers.FlowerProvider;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BeeGenes {
	public static final String flowerBrainStone = "brain_stone";
	public static final String flowerStablePulsatingBrainStone = "stable_pulsating_brain_stone";
	public static IAlleleFlowers flowerTypeBrainStone;
	public static IAlleleFlowers flowerTypeStablePulsatingBrainStone;

	public static void intiFlowers() {
		IFlowerRegistry flowerRegistry = FlowerManager.flowerRegistry;

		flowerRegistry.registerAcceptableFlower(BrainStoneBlocks.brainStoneOre(), flowerBrainStone);
		flowerRegistry.registerAcceptableFlower(BrainStoneBlocks.brainStone(), flowerBrainStone);
		flowerRegistry.registerAcceptableFlower(BrainStoneBlocks.brainStoneOut(), flowerBrainStone);
		flowerRegistry.registerAcceptableFlower(BrainStoneBlocks.dirtyBrainStone(), flowerBrainStone);
		flowerRegistry.registerAcceptableFlower(BrainStoneBlocks.pulsatingBrainStone(), flowerBrainStone);
		flowerRegistry.registerAcceptableFlower(BrainStoneBlocks.pulsatingBrainStoneEffect(), flowerBrainStone);
		flowerRegistry.registerAcceptableFlower(BrainStoneBlocks.stablePulsatingBrainStone(), flowerBrainStone,
				flowerStablePulsatingBrainStone);
	}

	public static void intiGenes() {
		flowerTypeBrainStone = AlleleManager.alleleFactory.createFlowers(BrainStone.MOD_ID, "flowers", flowerBrainStone,
				getFlowerProvider(flowerBrainStone), true, new IChromosomeType[] { EnumBeeChromosome.FLOWER_PROVIDER });
		flowerTypeStablePulsatingBrainStone = AlleleManager.alleleFactory.createFlowers(BrainStone.MOD_ID, "flowers",
				flowerStablePulsatingBrainStone, getFlowerProvider(flowerStablePulsatingBrainStone), true,
				new IChromosomeType[] { EnumBeeChromosome.FLOWER_PROVIDER });
	}

	private static IFlowerProvider getFlowerProvider(String name) {
		return new FlowerProvider(name, "for.flowers." + BrainStone.MOD_ID + '.' + name);
	}
}
