package mods.brainstone;

import bdp;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import mods.brainstone.tileentities.TileEntityBlockBrainLogicBlock;
import mods.brainstone.tileentities.TileEntityBlockBrainLogicBlockRenderer;
import net.minecraft.client.Minecraft;
import zv;

public class ClientProxy extends CommonProxy
{
  public Minecraft getClient()
  {
    return FMLClientHandler.instance().getClient();
  }

  public zv getClientWorld()
  {
    return getClient().e;
  }

  public bdp getPlayer()
  {
    return getClient().g;
  }

  public void registerRenderInformation()
  {
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBlockBrainLogicBlock.class, new TileEntityBlockBrainLogicBlockRenderer());
  }
}