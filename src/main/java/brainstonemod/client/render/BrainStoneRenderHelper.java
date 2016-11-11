package brainstonemod.client.render;

import brainstonemod.BrainStone;
import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
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

	public static void drawTexturedRect(double x, double y, double width, double height, int u, int v, int uSize,
			int vSize, double zLevel) {
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer vertexBuffer = tessellator.getBuffer();
		vertexBuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		vertexBuffer.pos(x, y + height, zLevel).tex(u * pxl, (v + vSize) * pxl).endVertex();
		vertexBuffer.pos(x + width, y + height, zLevel).tex((u + uSize) * pxl, (v + vSize) * pxl).endVertex();
		vertexBuffer.pos(x + width, y, zLevel).tex((u + uSize) * pxl, v * pxl).endVertex();
		vertexBuffer.pos(x, y, zLevel).tex(u * pxl, v * pxl).endVertex();
		tessellator.draw();
	}

	public static void setTexture(String textureName, int size) {
		setTexture(new ResourceLocation(BrainStone.RESOURCE_PACKAGE, textureName), size);
	}

	public static void setTexture(ResourceLocation texture, int size) {
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);

		pxl = 1.0 / size;
	}
}
