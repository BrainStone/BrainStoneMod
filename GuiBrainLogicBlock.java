import java.util.List;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class GuiBrainLogicBlock extends vp
{
  private byte focused;
  private int globalX;
  private int globalY;
  private final int xSize;
  private final int ySize;
  private float factor;
  private TileEntityBlockBrainLogicBlock tileentity;

  public GuiBrainLogicBlock(TileEntityBlockBrainLogicBlock tileentity)
  {
    this.tileentity = tileentity;
    this.xSize = 176;
    this.ySize = 166;
    this.focused = 0;
  }

  public void a(int par1, int par2, float par3)
  {
    this.factor = 1.0F;

    int t = this.p.p.b("/BrainStone/GuiBrainLogicBlock.png");
    this.p.p.b(t);
    int x = this.globalX = (this.q - this.xSize) / 2;
    int y = this.globalY = (this.r - this.ySize) / 2;
    b(x, y, 0, 0, this.xSize, this.ySize);

    this.tileentity.drawBoxes(this, x + 104, y + 7);

    this.focused = this.tileentity.getFocused();

    if (this.focused != 0)
    {
      switch (this.focused)
      {
      case 1:
        drawFocus(x + 124, y + 7);
        break;
      case 2:
        drawFocus(x + 144, y + 27);
        break;
      case 3:
        drawFocus(x + 104, y + 27);
      }

    }

    GL11.glPushMatrix();
    GL11.glScalef(this.factor, this.factor, this.factor);

    this.tileentity.drawGates(this, x + 6, y + 20);

    drawString(cy.a("tile.brainLogicBlock.name"), x + 6, y + 6, 0);

    boolean[] render = this.tileentity.shallRender();

    if (render[0] != 0)
      drawString(this.tileentity.getPin(0), 130, 50, this.tileentity.getPinColor(0), 2.0F);
    if (render[1] != 0)
      drawString(this.tileentity.getPin(1), 130, 10, this.tileentity.getPinColor(1), 2.0F);
    if (render[2] != 0)
      drawString(this.tileentity.getPin(2), 150, 30, this.tileentity.getPinColor(2), 2.0F);
    if (render[3] != 0) {
      drawString(this.tileentity.getPin(3), 110, 30, this.tileentity.getPinColor(3), 2.0F);
    }
    GL11.glPopMatrix();

    c();
    super.a(par1, par2, par3);
  }

  public void drawString(String str, int x, int y, int color)
  {
    this.u.b(str, (int)(x / this.factor), (int)(y / this.factor), color);
  }

  public void drawSplitString(String str, int x, int y, int color, int lenght)
  {
    this.u.a(str, (int)(x / this.factor), (int)(y / this.factor), lenght, color);
  }

  public void c()
  {
    this.s.clear();
    this.s.add(new abp(0, this.globalX + 10, this.globalY + 140, 156, 20, cy.a("gui.brainstone.help")));
  }

  public boolean b()
  {
    return false;
  }

  protected void a(abp guibutton)
  {
    if (guibutton.f == 0)
      ModLoader.openGUI(ModLoader.getMinecraftInstance().h, new GuiBrainLogicBlockHelp(cy.a("gui.brainstone.help.gate" + String.valueOf(this.tileentity.getMode())), this));
  }

  protected void a(char par1, int par2)
  {
    if ((par2 == 1) || (par2 == this.p.A.s.d)) {
      quit();
    }
    if (par1 == 'h') {
      this.tileentity.swapPosition(1, 2);
    }
    if (par2 == 205) {
      swap(true);
    }
    if (par2 == 203) {
      swap(false);
    }
    if (par2 == 54) {
      rotate(true);
    }
    if (par2 == 42) {
      rotate(false);
    }
    if ((par2 == 42) || (par2 == 54) || (par2 == 203) || (par2 == 205))
      click();
  }

  protected void a(int par1, int par2, int par3)
  {
    super.a(par1, par2, par3);

    if (par3 == 0)
    {
      par1 -= (this.q - this.xSize) / 2;
      par2 -= (this.r - this.ySize) / 2;

      if (inField(par1, par2, 168, 3, 172, 7)) {
        quit();
      }

      for (byte i = 0; i < TileEntityBlockBrainLogicBlock.numGates; i = (byte)(i + 1))
      {
        int tmp = 12 * i;

        if (inField(par1, par2, 5, 18 + tmp, 75, 32 + tmp)) {
          this.tileentity.setMode(i);
        }
      }
      if (inField(par1, par2, 76, 68, 168, 90)) {
        this.tileentity.invertInvertOutput();
      }
      if (!this.tileentity.isSwapable())
        this.tileentity.setFocused(0);
      else if (inField(par1, par2, 124, 7, 143, 26))
      {
        if (this.focused != 1)
          this.tileentity.setFocused(1);
        else
          this.tileentity.setFocused(0);
      }
      else if (inField(par1, par2, 144, 27, 163, 46))
      {
        if (this.focused != 2)
          this.tileentity.setFocused(2);
        else
          this.tileentity.setFocused(0);
      }
      else if (inField(par1, par2, 104, 27, 123, 46))
      {
        if (this.focused != 3)
          this.tileentity.setFocused(3);
        else
          this.tileentity.setFocused(0);
      }
      else
        this.tileentity.setFocused(0);
    }
  }

  private void click()
  {
    this.p.C.a("random.click", 1.0F, 1.0F);
  }

  private void drawFocus(int x, int y)
  {
    int tmp = (int)(ModLoader.getMinecraftInstance().f.B().f() & 1L) * 20;

    b(x, y, 196, tmp, 20, 20);
  }

  private void drawString(String str, int x, int y, int color, float fact)
  {
    if (fact != this.factor)
    {
      this.factor = fact;

      GL11.glPopMatrix();

      GL11.glPushMatrix();
      if (this.factor == 2.0F) {
        GL11.glTranslatef(this.globalX - 1.0F, this.globalY, 0.0F);
      }
      GL11.glScalef(this.factor, this.factor, this.factor);
    }

    this.u.b(str, (int)(x / this.factor), (int)(y / this.factor), color);
  }

  private void quit()
  {
    click();
    this.tileentity.setFocused(0);
    this.p.a(null);
    this.p.g();
  }

  private void rotate(boolean right)
  {
    if (right)
    {
      this.tileentity.swapPosition(1, 2);
      this.tileentity.swapPosition(1, 3);
      this.tileentity.addTASKS("setFocused", new String[] { "0" });
    }
    else
    {
      this.tileentity.swapPosition(1, 3);
      this.tileentity.swapPosition(1, 2);
      this.tileentity.addTASKS("setFocused", new String[] { "0" });
    }
  }

  private void swap(boolean right)
  {
    if (right)
    {
      switch (this.focused)
      {
      case 1:
        this.tileentity.swapPosition(1, 2);
        break;
      case 2:
        this.tileentity.swapPosition(2, 3);
        break;
      case 3:
        this.tileentity.swapPosition(3, 1);
      }

    }
    else
    {
      switch (this.focused)
      {
      case 1:
        this.tileentity.swapPosition(1, 3);
        this.focused = 3;

        break;
      case 2:
        this.tileentity.swapPosition(2, 1);
        this.focused = 1;

        break;
      case 3:
        this.tileentity.swapPosition(3, 2);
        this.focused = 2;
      }
    }
  }

  private boolean inField(int varX, int varY, int minX, int minY, int maxX, int maxY)
  {
    return (varX >= minX) && (varX <= maxX) && (varY >= minY) && (varY <= maxY);
  }
}