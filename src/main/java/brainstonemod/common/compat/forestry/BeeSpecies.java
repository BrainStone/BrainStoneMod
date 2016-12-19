package brainstonemod.common.compat.forestry;

import java.awt.Color;
import java.util.Arrays;
import java.util.Locale;

import brainstonemod.BrainStone;
import forestry.api.apiculture.BeeManager;
import forestry.api.apiculture.EnumBeeChromosome;
import forestry.api.apiculture.EnumBeeType;
import forestry.api.apiculture.IAlleleBeeSpecies;
import forestry.api.apiculture.IAlleleBeeSpeciesBuilder;
import forestry.api.apiculture.IBee;
import forestry.api.apiculture.IBeeGenome;
import forestry.api.core.EnumHumidity;
import forestry.api.core.EnumTemperature;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IAllele;
import forestry.apiculture.PluginApiculture;
import forestry.apiculture.genetics.Bee;
import forestry.apiculture.genetics.IBeeDefinition;
import forestry.apiculture.items.EnumHoneyComb;
import forestry.core.genetics.IBranchDefinition;
import forestry.core.genetics.alleles.AlleleHelper;
import net.minecraft.item.ItemStack;

public enum BeeSpecies implements IBeeDefinition {
	BRAIN_STONE(BeeBranches.BRAIN_STONE, "Brain Stone", false, new Color(0x23B23B), new Color(0x33FF57)) {
		@Override
		protected void registerMutations() {
			BeeManager.beeMutationFactory.createMutation(IndustriousBee, DemonicBee, getTemplate(), 5);
		}

		@Override
		protected void setAlleles(IAllele[] template) {
			// Default values
		}

		@Override
		protected void setSpeciesProperties(IAlleleBeeSpeciesBuilder beeSpecies) {
			beeSpecies.addProduct(PluginApiculture.items.beeComb.get(EnumHoneyComb.SIMMERING, 1), 0.30f)
					.addSpecialty(new ItemStack(BrainStone.brainStoneDustTiny(), 1), 0.10f)
					.setTemperature(EnumTemperature.NORMAL).setHumidity(EnumHumidity.NORMAL);
		}
	};

	// Forestry bees
	private static IAlleleBeeSpecies IndustriousBee = (IAlleleBeeSpecies) AlleleManager.alleleRegistry
			.getAllele("forestry.speciesIndustrious");
	private static IAlleleBeeSpecies DemonicBee = (IAlleleBeeSpecies) AlleleManager.alleleRegistry
			.getAllele("forestry.speciesDemonic");

	// BrainStoneMod Bees
	// private static IAlleleBeeSpecies BrainStoneBee = (IAlleleBeeSpecies)
	// AlleleManager.alleleRegistry
	// .getAllele(BrainStone.MOD_ID + ".species_brain_stone");

	public static void initBees() {
		for (BeeSpecies bee : values()) {
			bee.init();
		}

		for (BeeSpecies bee : values()) {
			bee.registerMutations();
		}
	}

	private final IBranchDefinition branch;

	private final IAlleleBeeSpecies species;
	private IAllele[] template;

	private IBeeGenome genome;

	BeeSpecies(IBranchDefinition branch, String binomial, boolean dominant, Color primary, Color secondary) {
		String lowercaseName = toString().toLowerCase(Locale.ENGLISH);
		String species = "species_" + lowercaseName;

		String ID = BrainStone.MOD_ID + '.' + species;
		String name = "for.bees.species." + ID;
		String description = name + ".description";

		this.branch = branch;
		IAlleleBeeSpeciesBuilder speciesBuilder = BeeManager.beeFactory.createSpecies(ID, dominant, "The_BrainStone",
				name, description, branch.getBranch(), binomial, primary.getRGB(), secondary.getRGB());

		if (isSecret()) {
			speciesBuilder.setIsSecret();
		}

		setSpeciesProperties(speciesBuilder);
		this.species = speciesBuilder.build();
	}

	@Override
	public final IBeeGenome getGenome() {
		return genome;
	}

	@Override
	public final IBee getIndividual() {
		return new Bee(genome);
	}

	@Override
	public final ItemStack getMemberStack(EnumBeeType beeType) {
		IBee bee = getIndividual();
		return BeeManager.beeRoot.getMemberStack(bee, beeType);
	}

	@Override
	public final IAllele[] getTemplate() {
		return Arrays.copyOf(template, template.length);
	}

	private void init() {
		template = branch.getTemplate();
		AlleleHelper.instance.set(template, EnumBeeChromosome.SPECIES, species);
		setAlleles(template);

		genome = BeeManager.beeRoot.templateAsGenome(template);

		BeeManager.beeRoot.registerTemplate(template);
	}

	protected boolean isSecret() {
		return false;
	}

	protected abstract void registerMutations();

	protected abstract void setAlleles(IAllele[] template);

	protected abstract void setSpeciesProperties(IAlleleBeeSpeciesBuilder beeSpecies);
}
