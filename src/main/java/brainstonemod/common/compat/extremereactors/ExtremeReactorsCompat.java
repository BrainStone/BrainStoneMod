package brainstonemod.common.compat.extremereactors;

import brainstonemod.common.compat.IModIntegration;
import brainstonemod.common.helper.BSP;
import erogenousbeef.bigreactors.api.data.ReactantData;
import erogenousbeef.bigreactors.api.data.ReactorReaction;
import erogenousbeef.bigreactors.api.registry.Reactants;
import erogenousbeef.bigreactors.api.registry.ReactorConversions;
import erogenousbeef.bigreactors.api.registry.ReactorInterior;
import erogenousbeef.bigreactors.api.registry.TurbineCoil;
import java.lang.reflect.Field;
import java.util.Map;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ExtremeReactorsCompat implements IModIntegration {
  private static final String REACTANT_ESSENCE_OF_LIFE = "essenceoflife";
  private static final String REACTANT_BRAINSTONE = "brainstone";

  private static final String MATERIAL_BRAINSTONE = "brainstone";
  private static final String MATERIAL_STABLE_PULSATING_BRAINSTONE = "stablepulsatingbrainstone";

  @SuppressWarnings({"deprecation", "unused"}) // Just logging the same way
  private static void registerCustomReactant(ReactantData reactant) {
    try {
      final Field fieldReactants = Reactants.class.getDeclaredField("_reactants");
      fieldReactants.setAccessible(true);

      @SuppressWarnings("unchecked") // I know what I'm doing!
      final Map<String, ReactantData> reactants =
          (Map<String, ReactantData>) fieldReactants.get(null);
      final String name = reactant.getName();

      if (reactants.containsKey(name)) {
        FMLLog.warning(
            "Overwriting data for reactant %s - someone may be altering BR game data or have duplicate reactant names!",
            name);
      }

      reactants.put(name, reactant);
    } catch (NoSuchFieldException
        | SecurityException
        | IllegalArgumentException
        | IllegalAccessException e) {
      BSP.errorException(e);
    }
  }

  @Override
  public void preInit(FMLPreInitializationEvent event) {
    // Do nothing
  }

  @Override
  public void init(FMLInitializationEvent event) {
    // Todo
    // registerCustomReactant(new EssenceOfLifeReactantData(REACTANT_ESSENCE_OF_LIFE, 0));
    Reactants.registerReactant(REACTANT_ESSENCE_OF_LIFE, 0, 0xe3237b);
    Reactants.registerReactant(REACTANT_BRAINSTONE, 1, 0x214d12);

    Reactants.registerSolid("essenceOfLife", REACTANT_ESSENCE_OF_LIFE, 250);
    Reactants.registerSolid("dustBrainstone", REACTANT_BRAINSTONE, 250);

    ReactorConversions.register(
        REACTANT_ESSENCE_OF_LIFE,
        REACTANT_BRAINSTONE,
        ReactorReaction.standardReactivity * 1.25F,
        ReactorReaction.standardFissionRate * 0.75F);

    TurbineCoil.registerBlock(MATERIAL_BRAINSTONE, 2.25F, 1.0F, 2.1F);
    TurbineCoil.registerBlock(MATERIAL_STABLE_PULSATING_BRAINSTONE, 4.0F, 1.5F, 4.0F);

    ReactorInterior.registerBlock(MATERIAL_BRAINSTONE, 0.61F, 0.9F, 2.1F, 2.3F);
    ReactorInterior.registerBlock(MATERIAL_STABLE_PULSATING_BRAINSTONE, 0.95F, 0.98F, 6.5F, 5.0F);
  }

  @Override
  public void postInit(FMLPostInitializationEvent event) {
    // Do nothing
  }
}
