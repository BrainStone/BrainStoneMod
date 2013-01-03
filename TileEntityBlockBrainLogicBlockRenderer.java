package brainstone;

import any;
import atq;
import bdx;
import org.lwjgl.opengl.GL11;

public class TileEntityBlockBrainLogicBlockRenderer extends bdx
{
  public void a(any tileentity, double d, double d1, double d2, float f)
  {
    try
    {
      float f1 = 0.01F;
      float f2 = 0.125F;
      GL11.glPushMatrix();
      GL11.glTranslatef((float)d + 0.5625F, (float)d1 + 1.4375F, (float)d2 + 1.0F + f1);
      GL11.glScalef(f2, -f2, f2);
      GL11.glNormal3f(0.0F, 0.0F, -1.0F * f1);
      renderInOutPut(tileentity, (byte)1);
      GL11.glPopMatrix();
      GL11.glPushMatrix();
      GL11.glTranslatef((float)d + 1.0F + f1, (float)d1 + 1.4375F, (float)d2 + 0.4375F);
      GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
      GL11.glScalef(f2, -f2, f2);
      GL11.glNormal3f(0.0F, 0.0F, -1.0F * f1);
      renderInOutPut(tileentity, (byte)3);
      GL11.glPopMatrix();
      GL11.glPushMatrix();
      GL11.glTranslatef((float)d + 0.4375F, (float)d1 + 1.4375F, (float)d2 - f1);
      GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
      GL11.glScalef(f2, -f2, f2);
      GL11.glNormal3f(0.0F, 0.0F, 1.0F * f1);
      renderInOutPut(tileentity, (byte)0);
      GL11.glPopMatrix();
      GL11.glPushMatrix();
      GL11.glTranslatef((float)d - f1, (float)d1 + 1.4375F, (float)d2 + 0.5625F);
      GL11.glRotatef(270.0F, 0.0F, 1.0F, 0.0F);
      GL11.glScalef(f2, -f2, f2);
      GL11.glNormal3f(0.0F, 0.0F, -1.0F * f1);
      renderInOutPut(tileentity, (byte)2);
      GL11.glPopMatrix();
    }
    catch (Exception exception)
    {
      BSP.printException(exception, "renderTileEntityAt (TileEntityBlockBrainLogicBlockRenderer)");
    }
  }

  private void renderInOutPut(any tileentity, byte byte0)
  {
    if ((tileentity instanceof TileEntityBlockBrainLogicBlock))
    {
      TileEntityBlockBrainLogicBlock tileentityblockbrainlogicblock = (TileEntityBlockBrainLogicBlock)tileentity;
      atq fontrenderer = b();
      GL11.glDepthMask(false);
      tileentityblockbrainlogicblock.renderInOutPut(fontrenderer, byte0);
      GL11.glDepthMask(true);
    }
  }
}