package brainstone;

import ays;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.MinecraftForgeClient;
import yc;

public class ClientProxy extends CommonProxy
{
  public void registerRenderInformation()
  {
    MinecraftForgeClient.preloadTexture("/brainstone/BrainStoneTextures/textures.png");
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBlockBrainLogicBlock.class, new TileEntityBlockBrainLogicBlockRenderer());
  }

  public yc getClientWorld()
  {
    return getClient().e;
  }

  public Minecraft getClient()
  {
    return FMLClientHandler.instance().getClient();
  }

  public ays getPlayer()
  {
    return getClient().g;
  }
}