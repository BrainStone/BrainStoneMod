import java.util.logging.Logger;
import org.lwjgl.opengl.GL11;

public class TileEntityBlockBrainLogicBlockRenderer extends aar
{
  public void a(kw tileentity, double d, double d1, double d2, float f)
  {
    try
    {
      float dist = 0.01F;
      float f4 = 0.125F;

      GL11.glPushMatrix();
      GL11.glTranslatef((float)d + 0.5625F, (float)d1 + 1.4375F, (float)d2 + 1.0F + dist);
      GL11.glScalef(f4, -f4, f4);
      GL11.glNormal3f(0.0F, 0.0F, -1.0F * dist);

      renderInOutPut(tileentity, (byte)1);
      GL11.glPopMatrix();

      GL11.glPushMatrix();
      GL11.glTranslatef((float)d + 1.0F + dist, (float)d1 + 1.4375F, (float)d2 + 0.4375F);
      GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
      GL11.glScalef(f4, -f4, f4);
      GL11.glNormal3f(0.0F, 0.0F, -1.0F * dist);

      renderInOutPut(tileentity, (byte)3);
      GL11.glPopMatrix();

      GL11.glPushMatrix();
      GL11.glTranslatef((float)d + 0.4375F, (float)d1 + 1.4375F, (float)d2 - dist);
      GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
      GL11.glScalef(f4, -f4, f4);
      GL11.glNormal3f(0.0F, 0.0F, 1.0F * dist);

      renderInOutPut(tileentity, (byte)0);
      GL11.glPopMatrix();

      GL11.glPushMatrix();
      GL11.glTranslatef((float)d - dist, (float)d1 + 1.4375F, (float)d2 + 0.5625F);
      GL11.glRotatef(270.0F, 0.0F, 1.0F, 0.0F);
      GL11.glScalef(f4, -f4, f4);
      GL11.glNormal3f(0.0F, 0.0F, -1.0F * dist);

      renderInOutPut(tileentity, (byte)2);
      GL11.glPopMatrix();
    }
    catch (Exception e)
    {
      mod_BrainStone.logger.throwing("BrainStone", "renderTileEntityAt (TileEntityBlockBrainLogicBlockRenderer)", e);
    }
  }

  private void renderInOutPut(kw tileentity, byte index)
  {
    if ((tileentity instanceof TileEntityBlockBrainLogicBlock))
    {
      TileEntityBlockBrainLogicBlock tmp = (TileEntityBlockBrainLogicBlock)tileentity;

      nl fontrenderer = a();
      GL11.glDepthMask(false);
      tmp.renderInOutPut(fontrenderer, index);
      GL11.glDepthMask(true);
    }
  }
}