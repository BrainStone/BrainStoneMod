package brainstonemod.common.compat.forestry;

import java.util.Arrays;
import java.util.Locale;

import forestry.api.apiculture.BeeManager;
import forestry.api.apiculture.EnumBeeChromosome;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IAllele;
import forestry.api.genetics.IClassification;
import forestry.apiculture.genetics.alleles.AlleleEffects;
import forestry.core.genetics.IBranchDefinition;
import forestry.core.genetics.alleles.AlleleHelper;
import forestry.core.genetics.alleles.EnumAllele;
import lombok.Getter;

public enum BeeBranches implements IBranchDefinition {
	BRAIN_STONE("Cerebrum Lapidem") {
		@Override
		protected void setBranchProperties(IAllele[] alleles) {
			AlleleHelper.getInstance().set(defaultTemplate, EnumBeeChromosome.SPEED, EnumAllele.Speed.NORMAL);
			AlleleHelper.getInstance().set(alleles, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORT);
			AlleleHelper.getInstance().set(alleles, EnumBeeChromosome.TEMPERATURE_TOLERANCE,
					EnumAllele.Tolerance.BOTH_2);
			AlleleHelper.getInstance().set(alleles, EnumBeeChromosome.NEVER_SLEEPS, true);
			AlleleHelper.getInstance().set(alleles, EnumBeeChromosome.HUMIDITY_TOLERANCE, EnumAllele.Tolerance.BOTH_2);
			AlleleHelper.getInstance().set(alleles, EnumBeeChromosome.CAVE_DWELLING, true);
			AlleleHelper.getInstance().set(alleles, EnumBeeChromosome.FLOWER_PROVIDER, BeeGenes.flowerTypeBrainStone);
			AlleleHelper.getInstance().set(alleles, EnumBeeChromosome.TERRITORY, EnumAllele.Territory.LARGER);
		}
	};

	private static IAllele[] defaultTemplate;
	@Getter
	private final IClassification branch;

	private static IAllele[] getDefaultTemplate() {
		if (defaultTemplate == null) {
			defaultTemplate = new IAllele[EnumBeeChromosome.values().length];

			AlleleHelper.getInstance().set(defaultTemplate, EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOWEST);
			AlleleHelper.getInstance().set(defaultTemplate, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTER);
			AlleleHelper.getInstance().set(defaultTemplate, EnumBeeChromosome.FERTILITY, EnumAllele.Fertility.NORMAL);
			AlleleHelper.getInstance().set(defaultTemplate, EnumBeeChromosome.TEMPERATURE_TOLERANCE,
					EnumAllele.Tolerance.NONE);
			AlleleHelper.getInstance().set(defaultTemplate, EnumBeeChromosome.NEVER_SLEEPS, false);
			AlleleHelper.getInstance().set(defaultTemplate, EnumBeeChromosome.HUMIDITY_TOLERANCE,
					EnumAllele.Tolerance.NONE);
			AlleleHelper.getInstance().set(defaultTemplate, EnumBeeChromosome.TOLERATES_RAIN, false);
			AlleleHelper.getInstance().set(defaultTemplate, EnumBeeChromosome.CAVE_DWELLING, false);
			AlleleHelper.getInstance().set(defaultTemplate, EnumBeeChromosome.FLOWER_PROVIDER,
					EnumAllele.Flowers.VANILLA);
			AlleleHelper.getInstance().set(defaultTemplate, EnumBeeChromosome.FLOWERING, EnumAllele.Flowering.SLOWEST);
			AlleleHelper.getInstance().set(defaultTemplate, EnumBeeChromosome.TERRITORY, EnumAllele.Territory.AVERAGE);
			AlleleHelper.getInstance().set(defaultTemplate, EnumBeeChromosome.EFFECT, AlleleEffects.effectNone);
		}

		return Arrays.copyOf(defaultTemplate, defaultTemplate.length);
	}

	public static void initBranches() {
		IClassification apidae = AlleleManager.alleleRegistry.getClassification("family.apidae");

		for (BeeBranches beeBranch : values()) {
			apidae.addMemberGroup(beeBranch.getBranch());
		}
	}

	BeeBranches(String scientific) {
		branch = BeeManager.beeFactory.createBranch(name().toLowerCase(Locale.ENGLISH), scientific);
	}

	@Override
	public final IAllele[] getTemplate() {
		IAllele[] template = getDefaultTemplate();
		setBranchProperties(template);
		return template;
	}

	protected void setBranchProperties(IAllele[] template) {
		// No Overrides by default
	}
}
