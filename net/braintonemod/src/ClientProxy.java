package net.braintonemod.src;

import ayk;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.MinecraftForgeClient;
import xv;

public class ClientProxy extends CommonProxy
{
  public void registerRenderInformation()
  {
    MinecraftForgeClient.preloadTexture("/BrainStoneTextures/textures.png");
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBlockBrainLogicBlock.class, new TileEntityBlockBrainLogicBlockRenderer());
  }

  public xv getClientWorld()
  {
    return getClient().e;
  }

  public Minecraft getClient()
  {
    return FMLClientHandler.instance().getClient();
  }

  public ayk getPlayer()
  {
    return getClient().g;
  }
}