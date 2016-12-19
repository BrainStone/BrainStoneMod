package brainstonemod.common.compat.forestry;

import brainstonemod.BrainStone;
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

		flowerRegistry.registerAcceptableFlower(BrainStone.brainStoneOre(), flowerBrainStone);
		flowerRegistry.registerAcceptableFlower(BrainStone.brainStone(), flowerBrainStone);
		flowerRegistry.registerAcceptableFlower(BrainStone.brainStoneOut(), flowerBrainStone);
		flowerRegistry.registerAcceptableFlower(BrainStone.dirtyBrainStone(), flowerBrainStone);
		flowerRegistry.registerAcceptableFlower(BrainStone.pulsatingBrainStone(), flowerBrainStone);
		flowerRegistry.registerAcceptableFlower(BrainStone.pulsatingBrainStoneEffect(), flowerBrainStone);
		flowerRegistry.registerAcceptableFlower(BrainStone.stablePulsatingBrainStone(), flowerBrainStone,
				flowerStablePulsatingBrainStone);
	}

	public static void intiGenes() {
		flowerTypeBrainStone = AlleleManager.alleleFactory.createFlowers(BrainStone.MOD_ID, "flowers", flowerBrainStone,
				getFlowerProvider(flowerBrainStone), false,
				new IChromosomeType[] { EnumBeeChromosome.FLOWER_PROVIDER });
		flowerTypeStablePulsatingBrainStone = AlleleManager.alleleFactory.createFlowers(BrainStone.MOD_ID, "flowers",
				flowerStablePulsatingBrainStone, getFlowerProvider(flowerStablePulsatingBrainStone), false,
				new IChromosomeType[] { EnumBeeChromosome.FLOWER_PROVIDER });
	}

	private static IFlowerProvider getFlowerProvider(String name) {
		return new FlowerProvider(name, "for.flowers." + BrainStone.MOD_ID + '.' + name);
	}
}
