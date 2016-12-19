package brainstonemod.common.compat.forestry;

import java.util.Arrays;
import java.util.Locale;

import forestry.api.apiculture.BeeManager;
import forestry.api.apiculture.EnumBeeChromosome;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IAllele;
import forestry.api.genetics.IClassification;
import forestry.apiculture.genetics.alleles.AlleleEffect;
import forestry.core.genetics.IBranchDefinition;
import forestry.core.genetics.alleles.AlleleHelper;
import forestry.core.genetics.alleles.EnumAllele;

public enum BeeBranches implements IBranchDefinition {
	BRAIN_STONE("Cerebrum Lapidem") {
		@Override
		protected void setBranchProperties(IAllele[] alleles) {
			AlleleHelper.instance.set(alleles, EnumBeeChromosome.TEMPERATURE_TOLERANCE, EnumAllele.Tolerance.BOTH_2);
			AlleleHelper.instance.set(alleles, EnumBeeChromosome.NEVER_SLEEPS, true);
			AlleleHelper.instance.set(alleles, EnumBeeChromosome.HUMIDITY_TOLERANCE, EnumAllele.Tolerance.BOTH_2);
			AlleleHelper.instance.set(alleles, EnumBeeChromosome.CAVE_DWELLING, true);
			AlleleHelper.instance.set(alleles, EnumBeeChromosome.FLOWER_PROVIDER, EnumAllele.Flowers.VANILLA);
			AlleleHelper.instance.set(alleles, EnumBeeChromosome.TERRITORY, EnumAllele.Territory.LARGER);
		}
	};

	private static IAllele[] defaultTemplate;

	private static IAllele[] getDefaultTemplate() {
		if (defaultTemplate == null) {
			defaultTemplate = new IAllele[EnumBeeChromosome.values().length];

			AlleleHelper.instance.set(defaultTemplate, EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOWEST);
			AlleleHelper.instance.set(defaultTemplate, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTER);
			AlleleHelper.instance.set(defaultTemplate, EnumBeeChromosome.FERTILITY, EnumAllele.Fertility.NORMAL);
			AlleleHelper.instance.set(defaultTemplate, EnumBeeChromosome.TEMPERATURE_TOLERANCE,
					EnumAllele.Tolerance.NONE);
			AlleleHelper.instance.set(defaultTemplate, EnumBeeChromosome.NEVER_SLEEPS, false);
			AlleleHelper.instance.set(defaultTemplate, EnumBeeChromosome.HUMIDITY_TOLERANCE, EnumAllele.Tolerance.NONE);
			AlleleHelper.instance.set(defaultTemplate, EnumBeeChromosome.TOLERATES_RAIN, false);
			AlleleHelper.instance.set(defaultTemplate, EnumBeeChromosome.CAVE_DWELLING, false);
			AlleleHelper.instance.set(defaultTemplate, EnumBeeChromosome.FLOWER_PROVIDER, EnumAllele.Flowers.VANILLA);
			AlleleHelper.instance.set(defaultTemplate, EnumBeeChromosome.FLOWERING, EnumAllele.Flowering.SLOWEST);
			AlleleHelper.instance.set(defaultTemplate, EnumBeeChromosome.TERRITORY, EnumAllele.Territory.AVERAGE);
			AlleleHelper.instance.set(defaultTemplate, EnumBeeChromosome.EFFECT, AlleleEffect.effectNone);
		}

		return Arrays.copyOf(defaultTemplate, defaultTemplate.length);
	}

	public static void initBranches() {
		IClassification apidae = AlleleManager.alleleRegistry.getClassification("family.apidae");

		for (BeeBranches beeBranch : values()) {
			apidae.addMemberGroup(beeBranch.getBranch());
		}
	}

	private final IClassification branch;

	BeeBranches(String scientific) {
		branch = BeeManager.beeFactory.createBranch(name().toLowerCase(Locale.ENGLISH), scientific);
	}

	@Override
	public final IClassification getBranch() {
		return branch;
	}

	@Override
	public final IAllele[] getTemplate() {
		IAllele[] template = getDefaultTemplate();
		setBranchProperties(template);
		return template;
	}

	protected void setBranchProperties(IAllele[] template) {
	}
}
