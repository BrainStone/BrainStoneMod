package net.braintonemod.src;

import amj;
import bao;
import baq;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import org.lwjgl.opengl.GL11;
import um;
import yf;

public class BrainStoneBlockBrainStoneTriggerRenderClass
  implements ISimpleBlockRenderingHandler
{
  public void renderInventoryBlock(amj block, int metadata, int modelID, baq renderer)
  {
    bao tessellator = bao.a;

    if (renderer.c)
    {
      int k = block.g_(metadata);
      float f = (k >> 16 & 0xFF) / 255.0F;
      float f2 = (k >> 8 & 0xFF) / 255.0F;
      float f4 = (k & 0xFF) / 255.0F;
      GL11.glColor4f(f, f2, f4, 1.0F);
    }

    block.f();
    GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
    tessellator.b();
    tessellator.b(0.0F, -1.0F, 0.0F);
    renderer.a(block, 0.0D, 0.0D, 0.0D, block.a(0, metadata));
    tessellator.a();

    if (renderer.c)
    {
      int l = block.g_(metadata);
      float f1 = (l >> 16 & 0xFF) / 255.0F;
      float f3 = (l >> 8 & 0xFF) / 255.0F;
      float f5 = (l & 0xFF) / 255.0F;
      GL11.glColor4f(f1 * modelID, f3 * modelID, f5 * modelID, 1.0F);
    }

    tessellator.b();
    tessellator.b(0.0F, 1.0F, 0.0F);
    renderer.b(block, 0.0D, 0.0D, 0.0D, block.a(1, metadata));
    tessellator.a();

    if (renderer.c)
    {
      GL11.glColor4f(modelID, modelID, modelID, 1.0F);
    }

    tessellator.b();
    tessellator.b(0.0F, 0.0F, -1.0F);
    renderer.c(block, 0.0D, 0.0D, 0.0D, block.a(2, metadata));
    tessellator.a();
    tessellator.b();
    tessellator.b(0.0F, 0.0F, 1.0F);
    renderer.d(block, 0.0D, 0.0D, 0.0D, block.a(3, metadata));
    tessellator.a();
    tessellator.b();
    tessellator.b(-1.0F, 0.0F, 0.0F);
    renderer.e(block, 0.0D, 0.0D, 0.0D, block.a(4, metadata));
    tessellator.a();
    tessellator.b();
    tessellator.b(1.0F, 0.0F, 0.0F);
    renderer.f(block, 0.0D, 0.0D, 0.0D, block.a(5, metadata));
    tessellator.a();
    GL11.glTranslatef(0.5F, 0.5F, 0.5F);
  }

  public boolean renderWorldBlock(yf world, int x, int y, int z, amj block, int modelId, baq renderer)
  {
    if (modelId == 56)
    {
      return renderBrainStoneSensors(block, x, y, z, renderer, world);
    }

    return false;
  }

  private boolean renderBrainStoneSensors(amj block, int i, int j, int k, baq renderblocks, yf iblockaccess)
  {
    block.setTextureFile("/BrainStoneTextures/textures.png");

    double x = i;
    double y = j;
    double z = k;

    block.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    renderblocks.a(block, x, y, z, 34);
    renderblocks.e(block, x, y, z, 18);
    renderblocks.c(block, x, y, z, 18);
    renderblocks.f(block, x, y, z, 18);
    renderblocks.d(block, x, y, z, 18);

    TileEntityBlockBrainStoneTrigger tileentity = (TileEntityBlockBrainStoneTrigger)iblockaccess.q(i, j, k);
    um item = tileentity.a(0);

    if ((tileentity != null) && (item != null) && (item.a != 0))
    {
      block.setTextureFile(tileentity.getTextureFile());
      renderblocks.b(block, x, y, z, tileentity.getTextureId(iblockaccess, i, j, k));
    }
    else
    {
      renderblocks.b(block, x, y, z, 2);
    }

    return true;
  }

  public boolean shouldRender3DInInventory()
  {
    return true;
  }

  public int getRenderId()
  {
    return 56;
  }
}