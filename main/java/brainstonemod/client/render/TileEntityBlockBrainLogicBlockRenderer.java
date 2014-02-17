package brainstonemod.client.render;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import brainstonemod.common.helper.BSP;
import brainstonemod.common.tileentity.TileEntityBlockBrainLogicBlock;

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

				tileentityblockbrainlogicblock.renderGate(fontrenderer,
						(byte) pos);

				GL11.glDepthMask(true);
			}
		}
	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y,
			double z, float f) {
		try {
			final float f1 = 0.0005F;
			final float f2 = 0.125F;

			GL11.glPushMatrix();
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

			// Top Face
			GL11.glPushMatrix();
			GL11.glTranslatef((float) x + 0.5625F, (float) y + 1.0F + f1,
					(float) z - 0.4375F);
			GL11.glRotatef(-90F, 1.0F, 0.0F, 0.0F);
			GL11.glScalef(f2, -f2, f2);
			GL11.glNormal3f(0.0F, 0.0F, 1.0F * f1);

			renderGate(tileentity, 0);

			GL11.glPopMatrix();

			// Bottom Face
			GL11.glPushMatrix();
			GL11.glTranslatef((float) x + 0.4375F, (float) y - f1,
					(float) z - 0.4375F);
			GL11.glRotatef(-90F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
			GL11.glScalef(f2, -f2, f2);
			GL11.glNormal3f(0.0F, 0.0F, 1.0F * f1);

			renderGate(tileentity, 1);

			GL11.glPopMatrix();

			// North Face
			GL11.glPushMatrix();
			GL11.glTranslatef((float) x + 0.4375F, (float) y + 1.4375F,
					(float) z - f1);
			GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
			GL11.glScalef(f2, -f2, f2);
			GL11.glNormal3f(0.0F, 0.0F, 1.0F * f1);

			renderGate(tileentity, 2);

			GL11.glPopMatrix();

			// East Face
			GL11.glPushMatrix();
			GL11.glTranslatef((float) x + 1.0F + f1, (float) y + 1.4375F,
					(float) z + 0.4375F);
			GL11.glRotatef(90F, 0.0F, 1.0F, 0.0F);
			GL11.glScalef(f2, -f2, f2);
			GL11.glNormal3f(0.0F, 0.0F, -1F * f1);

			renderGate(tileentity, 3);

			GL11.glPopMatrix();

			// South Face
			GL11.glPushMatrix();
			GL11.glTranslatef((float) x + 0.5625F, (float) y + 1.4375F,
					(float) z + 1.0F + f1);
			GL11.glScalef(f2, -f2, f2);
			GL11.glNormal3f(0.0F, 0.0F, -1F * f1);

			renderGate(tileentity, 4);

			GL11.glPopMatrix();

			// West Face
			GL11.glPushMatrix();
			GL11.glTranslatef((float) x - f1, (float) y + 1.4375F,
					(float) z + 0.5625F);
			GL11.glRotatef(270F, 0.0F, 1.0F, 0.0F);
			GL11.glScalef(f2, -f2, f2);
			GL11.glNormal3f(0.0F, 0.0F, -1F * f1);

			renderGate(tileentity, 5);

			GL11.glPopMatrix();

			GL11.glPopMatrix();
		} catch (final Exception exception) {
			BSP.logException(exception,
					"renderTileEntityAt (TileEntityBlockBrainLogicBlockRenderer)");
		}
	}
}
