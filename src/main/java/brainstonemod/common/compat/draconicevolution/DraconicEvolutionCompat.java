package brainstonemod.common.compat.draconicevolution;

import brainstonemod.common.compat.IModIntegration;
import brainstonemod.common.helper.BrainStoneLifeCapacitorUpgrade;
import com.brandon3055.draconicevolution.api.fusioncrafting.FusionRecipeAPI;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class DraconicEvolutionCompat implements IModIntegration {
  @Override
  public void preInit(FMLPreInitializationEvent event) {
    // Do nothing
  }

  @Override
  public void init(FMLInitializationEvent event) {
    BrainStoneLifeCapacitorFusionUpgrade upgradeCapacity =
        new BrainStoneLifeCapacitorFusionUpgrade(BrainStoneLifeCapacitorUpgrade.Upgrade.CAPACITY);
    BrainStoneLifeCapacitorFusionUpgrade upgradeCharging =
        new BrainStoneLifeCapacitorFusionUpgrade(BrainStoneLifeCapacitorUpgrade.Upgrade.CHARGING);

    // Capacitor Recipes
    FusionRecipeAPI.addRecipe(upgradeCapacity);
    FusionRecipeAPI.addRecipe(upgradeCharging);

    // For advancements
    MinecraftForge.EVENT_BUS.register(upgradeCapacity);
    MinecraftForge.EVENT_BUS.register(upgradeCharging);
  }

  @Override
  public void postInit(FMLPostInitializationEvent event) {
    // Do nothing
  }
}
