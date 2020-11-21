package brainstonemod.common.compat.enderio;

import brainstonemod.common.compat.IModIntegration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class EnderIOCompat implements IModIntegration {
  final EnderIORecipes recipes = new EnderIORecipes();

  @Override
  public void preInit(FMLPreInitializationEvent event) {
    MinecraftForge.EVENT_BUS.register(recipes);
  }

  @Override
  public void init(FMLInitializationEvent event) {
    recipes.registerEnderIORecipies();
  }

  @Override
  public void postInit(FMLPostInitializationEvent event) {
    // Do nothing
  }
}
