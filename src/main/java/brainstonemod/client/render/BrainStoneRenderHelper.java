package brainstonemod.client.render;

import brainstonemod.BrainStone;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.HashMap;
import java.util.Map;
import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

/**
 * Some of this code is taken from or inspired by DraconicEvolution!
 *
 * @author BrainStone
 */
@UtilityClass
public class BrainStoneRenderHelper {
  private static double pxl;
  private static Map<String, ResourceLocation> resourceCache = new HashMap<>();

  public static void drawTexturedRect(
      double x,
      double y,
      double width,
      double height,
      int u,
      int v,
      int uSize,
      int vSize,
      double zLevel) {
    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder vertexBuffer = tessellator.getBuffer();
    vertexBuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
    vertexBuffer.pos(x, y + height, zLevel).tex(u * pxl, (v + vSize) * pxl).endVertex();
    vertexBuffer
        .pos(x + width, y + height, zLevel)
        .tex((u + uSize) * pxl, (v + vSize) * pxl)
        .endVertex();
    vertexBuffer.pos(x + width, y, zLevel).tex((u + uSize) * pxl, v * pxl).endVertex();
    vertexBuffer.pos(x, y, zLevel).tex(u * pxl, v * pxl).endVertex();
    tessellator.draw();
  }

  @SuppressFBWarnings(
      value = "ICAST_IDIV_CAST_TO_DOUBLE",
      justification = "Integer truncation is intended here")
  public static void drawCenteredString(String text, float x, float y, int color) {
    FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;

    // Intended truncation
    fontRenderer.drawStringWithShadow(text, x - (fontRenderer.getStringWidth(text) / 2), y, color);
  }

  public static void drawString(String text, float x, float y, int color) {
    FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;

    fontRenderer.drawStringWithShadow(text, x, y, color);
  }

  public static void setTexture(String textureName, int size) {
    if (!resourceCache.containsKey(textureName)) {
      resourceCache.put(
          textureName, new ResourceLocation(BrainStone.RESOURCE_PACKAGE, textureName));
    }

    setTexture(resourceCache.get(textureName), size);
  }

  public static void setTexture(ResourceLocation texture, int size) {
    Minecraft.getMinecraft().renderEngine.bindTexture(texture);

    pxl = 1.0 / size;
  }
}
