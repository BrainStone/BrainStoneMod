package mods.brainstone.tileentities;

import mods.brainstone.templates.BSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

public class TileEntityBlockBrainLogicBlockRenderer extends
		TileEntitySpecialRenderer {
	public TileEntityBlockBrainLogicBlockRenderer() {
	}

	private void renderGate(TileEntity tileentity, int pos) {
		if (tileentity instanceof TileEntityBlockBrainLogicBlock) {
			final TileEntityBlockBrainLogicBlock tileentityblockbrainlogicblock = (TileEntityBlockBrainLogicBlock) tileentity;

			if (tileentityblockbrainlogicblock != null) {
				final FontRenderer fontrenderer = this.getFontRenderer();
				GL11.glDepthMask(false);

				tileentityblockbrainlogicblock.renderGate(fontrenderer, pos);

				GL11.glDepthMask(true);
			}
		}
	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d, double d1,
			double d2, float f) {
		try {
			final float f1 = 0.01F;
			final float f2 = 0.125F;
			GL11.glPushMatrix();
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glTranslatef((float) d + 0.5625F, (float) d1 + 1.4375F,
					(float) d2 + 1.0F + f1);
			GL11.glScalef(f2, -f2, f2);
			GL11.glNormal3f(0.0F, 0.0F, -1F * f1);
			this.renderGate(tileentity, 1);
			GL11.glPopMatrix();

			GL11.glPushMatrix();
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glTranslatef((float) d + 1.0F + f1, (float) d1 + 1.4375F,
					(float) d2 + 0.4375F);
			GL11.glRotatef(90F, 0.0F, 1.0F, 0.0F);
			GL11.glScalef(f2, -f2, f2);
			GL11.glNormal3f(0.0F, 0.0F, -1F * f1);
			this.renderGate(tileentity, 3);
			GL11.glPopMatrix();

			GL11.glPushMatrix();
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glTranslatef((float) d + 0.4375F, (float) d1 + 1.4375F,
					(float) d2 - f1);
			GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
			GL11.glScalef(f2, -f2, f2);
			GL11.glNormal3f(0.0F, 0.0F, 1.0F * f1);
			this.renderGate(tileentity, 0);
			GL11.glPopMatrix();

			GL11.glPushMatrix();
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glTranslatef((float) d - f1, (float) d1 + 1.4375F,
					(float) d2 + 0.5625F);
			GL11.glRotatef(270F, 0.0F, 1.0F, 0.0F);
			GL11.glScalef(f2, -f2, f2);
			GL11.glNormal3f(0.0F, 0.0F, -1F * f1);
			this.renderGate(tileentity, 2);
			GL11.glPopMatrix();
		} catch (final Exception exception) {
			BSP.printException(exception,
					"renderTileEntityAt (TileEntityBlockBrainLogicBlockRenderer)");
		}
	}
}
