package mods.brainstone;

import aab;
import bdw;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import mods.brainstone.tileentities.TileEntityBlockBrainLogicBlock;
import mods.brainstone.tileentities.TileEntityBlockBrainLogicBlockRenderer;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends CommonProxy
{
  public Minecraft getClient()
  {
    return FMLClientHandler.instance().getClient();
  }

  public aab getClientWorld()
  {
    return getClient().e;
  }

  public bdw getPlayer()
  {
    return getClient().g;
  }

  public void registerRenderInformation()
  {
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBlockBrainLogicBlock.class, new TileEntityBlockBrainLogicBlockRenderer());
  }

  public void registerTextures()
  {
    MinecraftForgeClient.preloadTexture("/mods/brainstone/textures/armor/brainstone_armor_1.png");

    MinecraftForgeClient.preloadTexture("/mods/brainstone/textures/armor/brainstone_armor_2.png");

    MinecraftForgeClient.preloadTexture("/mods/brainstone/textures/gui/GuiBrainLightSensor.png");

    MinecraftForgeClient.preloadTexture("/mods/brainstone/textures/gui/GuiBrainLogicBlock.png");

    MinecraftForgeClient.preloadTexture("/mods/brainstone/textures/gui/GuiBrainStoneTrigger.png");
  }
}