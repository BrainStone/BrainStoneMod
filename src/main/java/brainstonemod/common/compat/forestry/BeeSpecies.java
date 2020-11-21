package brainstonemod.common.compat.forestry;

import brainstonemod.BrainStone;
import brainstonemod.BrainStoneItems;
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
import forestry.apiculture.genetics.Bee;
import forestry.apiculture.genetics.IBeeDefinition;
import forestry.core.genetics.IBranchDefinition;
import forestry.core.genetics.alleles.AlleleHelper;
import forestry.core.genetics.alleles.EnumAllele;
import java.awt.Color;
import java.util.Arrays;
import java.util.Locale;
import lombok.Getter;
import net.minecraft.item.ItemStack;

public enum BeeSpecies implements IBeeDefinition {
  BRAIN_STONE(
      BeeBranches.BRAIN_STONE, "mundanis", false, new Color(0x23B23B), new Color(0x33FF57)) {
    @Override
    protected void registerMutations() {
      BeeManager.beeMutationFactory.createMutation(industriousBee, demonicBee, getTemplate(), 15);
    }

    @Override
    protected void setAlleles(IAllele[] template) {
      AlleleHelper.getInstance()
          .set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.NORMAL);
      AlleleHelper.getInstance()
          .set(template, EnumBeeChromosome.TEMPERATURE_TOLERANCE, EnumAllele.Tolerance.BOTH_1);
      AlleleHelper.getInstance()
          .set(template, EnumBeeChromosome.HUMIDITY_TOLERANCE, EnumAllele.Tolerance.BOTH_1);
      AlleleHelper.getInstance()
          .set(template, EnumBeeChromosome.TERRITORY, EnumAllele.Territory.LARGE);
    }

    @Override
    protected void setSpeciesProperties(IAlleleBeeSpeciesBuilder beeSpecies) {
      beeSpecies
          .addProduct(new ItemStack(BrainStoneItems.brainStoneComb()), 0.30f)
          .setTemperature(EnumTemperature.NORMAL)
          .setHumidity(EnumHumidity.NORMAL);
    }
  },
  PULSATING_BRAIN_STONE(
      BeeBranches.BRAIN_STONE, "vibrantis", false, new Color(0x33FF57), new Color(0x23B23B)) {
    @Override
    protected void registerMutations() {
      BeeManager.beeMutationFactory.createMutation(
          BRAIN_STONE.species, imperialBee, getTemplate(), 10);
    }

    @Override
    protected void setAlleles(IAllele[] template) {
      AlleleHelper.getInstance().set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.FAST);
      AlleleHelper.getInstance()
          .set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTER);
      AlleleHelper.getInstance()
          .set(template, EnumBeeChromosome.FLOWERING, EnumAllele.Flowering.SLOWER);
      AlleleHelper.getInstance()
          .set(template, EnumBeeChromosome.FERTILITY, EnumAllele.Fertility.HIGH);
    }

    @Override
    protected void setSpeciesProperties(IAlleleBeeSpeciesBuilder beeSpecies) {
      beeSpecies
          .addProduct(new ItemStack(BrainStoneItems.brainStoneComb()), 0.50f)
          .setTemperature(EnumTemperature.NORMAL)
          .setHumidity(EnumHumidity.NORMAL);
    }
  },
  STABLE_PULSATING_BRAIN_STONE(
      BeeBranches.BRAIN_STONE,
      "vibrantis stabilis",
      false,
      new Color(0x33FF57),
      new Color(0x7FFF94)) {
    @Override
    protected void registerMutations() {
      BeeManager.beeMutationFactory.createMutation(
          PULSATING_BRAIN_STONE.species, phantasmalBee, getTemplate(), 5);
    }

    @Override
    protected void setAlleles(IAllele[] template) {
      AlleleHelper.getInstance().set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOW);
      AlleleHelper.getInstance()
          .set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.LONGEST);
      AlleleHelper.getInstance().set(template, EnumBeeChromosome.TOLERATES_RAIN, true);
      AlleleHelper.getInstance()
          .set(
              template,
              EnumBeeChromosome.FLOWER_PROVIDER,
              BeeGenes.flowerTypeStablePulsatingBrainStone);
      AlleleHelper.getInstance()
          .set(template, EnumBeeChromosome.TERRITORY, EnumAllele.Territory.LARGEST);
    }

    @Override
    protected void setSpeciesProperties(IAlleleBeeSpeciesBuilder beeSpecies) {
      beeSpecies
          .addProduct(new ItemStack(BrainStoneItems.brainStoneComb()), 0.50f)
          .addProduct(new ItemStack(BrainStoneItems.essenceOfLifeDust(), 1), 0.10f)
          .setTemperature(EnumTemperature.NORMAL)
          .setHumidity(EnumHumidity.NORMAL)
          .setHasEffect();
    }
  };

  // Forestry bees
  private static final IAlleleBeeSpecies demonicBee =
      (IAlleleBeeSpecies) AlleleManager.alleleRegistry.getAllele("forestry.speciesDemonic");
  private static final IAlleleBeeSpecies industriousBee =
      (IAlleleBeeSpecies) AlleleManager.alleleRegistry.getAllele("forestry.speciesIndustrious");
  private static final IAlleleBeeSpecies imperialBee =
      (IAlleleBeeSpecies) AlleleManager.alleleRegistry.getAllele("forestry.speciesImperial");
  private static final IAlleleBeeSpecies phantasmalBee =
      (IAlleleBeeSpecies) AlleleManager.alleleRegistry.getAllele("forestry.speciesPhantasmal");

  private final IBranchDefinition branch;
  private final IAlleleBeeSpecies species;
  private IAllele[] template;
  @Getter private IBeeGenome genome;

  public static void initBees() {
    for (BeeSpecies bee : values()) {
      bee.init();
    }

    for (BeeSpecies bee : values()) {
      bee.registerMutations();
    }
  }

  BeeSpecies(
      IBranchDefinition branch, String binomial, boolean dominant, Color primary, Color secondary) {
    String lowercaseName = toString().toLowerCase(Locale.ENGLISH);
    String species = "species_" + lowercaseName;

    String ID = BrainStone.MOD_ID + '.' + species;
    String name = "for.bees.species." + ID;
    String description = name + ".description";

    this.branch = branch;
    IAlleleBeeSpeciesBuilder speciesBuilder =
        BeeManager.beeFactory.createSpecies(
            BrainStone.MOD_ID,
            ID,
            dominant,
            "The_BrainStone",
            name,
            description,
            branch.getBranch(),
            binomial,
            primary.getRGB(),
            secondary.getRGB());

    if (isSecret()) {
      speciesBuilder.setIsSecret();
    }

    setSpeciesProperties(speciesBuilder);
    this.species = speciesBuilder.build();
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
    AlleleHelper.getInstance().set(template, EnumBeeChromosome.SPECIES, species);
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
