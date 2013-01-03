package brainstone;

import ahx;
import amq;
import baz;
import bbb;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import org.lwjgl.opengl.GL11;
import yc;
import ym;

public class BrainStoneBlockPulsatingBrainStonerRenderClass
  implements ISimpleBlockRenderingHandler
{
  public void renderInventoryBlock(amq block, int metadata, int modelID, bbb renderer)
  {
    baz tessellator = baz.a;
    metadata = (int)(BrainStone.proxy.getClientWorld().K().g() / 2L) % 16;

    int k = block.g_(metadata);
    float f = (k >> 16 & 0xFF) / 255.0F;
    float f2 = (k >> 8 & 0xFF) / 255.0F;
    float f4 = (k & 0xFF) / 255.0F;
    GL11.glColor4f(f, f2, f4, 1.0F);

    block.f();
    GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
    tessellator.b();
    tessellator.b(0.0F, -1.0F, 0.0F);
    renderer.a(block, 0.0D, 0.0D, 0.0D, block.a(0, metadata));
    tessellator.a();

    int l = block.g_(metadata);
    float f1 = (l >> 16 & 0xFF) / 255.0F;
    float f3 = (l >> 8 & 0xFF) / 255.0F;
    float f5 = (l & 0xFF) / 255.0F;
    GL11.glColor4f(f1 * modelID, f3 * modelID, f5 * modelID, 1.0F);

    tessellator.b();
    tessellator.b(0.0F, 1.0F, 0.0F);
    renderer.b(block, 0.0D, 0.0D, 0.0D, block.a(1, metadata));
    tessellator.a();

    GL11.glColor4f(modelID, modelID, modelID, 1.0F);

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

  public boolean renderWorldBlock(ym world, int x, int y, int z, amq block, int modelId, bbb renderer)
  {
    int metadata = (int)(BrainStone.proxy.getClientWorld().K().g() / 2L + world.h(x, y, z)) % 16;

    int id = block.a(0, metadata);

    renderer.b(block, x, y, z, id);
    renderer.a(block, x, y, z, id);
    renderer.e(block, x, y, z, id);
    renderer.c(block, x, y, z, id);
    renderer.f(block, x, y, z, id);
    renderer.d(block, x, y, z, id);

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