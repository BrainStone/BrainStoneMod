package mods.brainstone.guis;

import aab;
import ajv;
import ava;
import avy;
import awg;
import awv;
import bdw;
import bkd;
import bo;
import mods.brainstone.BrainStone;
import mods.brainstone.ClientProxy;
import mods.brainstone.containers.ContainerBlockBrainLightSensor;
import mods.brainstone.templates.GuiBrainStoneBase;
import mods.brainstone.tileentities.TileEntityBlockBrainLogicBlock;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class GuiBrainLogicBlock extends GuiBrainStoneBase
{
  private byte focused;
  private int globalX;
  private int globalY;
  private static final int b = 176;
  private static final int c = 166;
  private float factor;
  private final TileEntityBlockBrainLogicBlock tileentity;
  private final String username;
  private boolean help;
  private static final int helpYSize = 90;
  private static final int stringWidth = 156;
  private String HelpText;

  public GuiBrainLogicBlock(TileEntityBlockBrainLogicBlock tileentityblockbrainlogicblock)
  {
    super(new ContainerBlockBrainLightSensor());
    this.username = BrainStone.proxy.getPlayer().bS;
    this.tileentity = tileentityblockbrainlogicblock;
    this.tileentity.logIn(this.username);
    this.focused = this.tileentity.getFocused();
    this.help = false;
  }

  protected void a(awg guibutton)
  {
    if (guibutton.f == 0) {
      this.HelpText = bo.a("gui.brainstone.help.gate" + String.valueOf(this.tileentity.getMode()));

      this.help = true;

      this.tileentity.logOut(this.username);
    }
  }

  private void click() {
    this.f.B.a("random.click", 1.0F, 1.0F);
  }

  private void closeHelpGui() {
    click();
    this.help = false;
    this.tileentity.logIn(this.username);
  }

  public boolean f()
  {
    return false;
  }

  private void drawFocus(int i, int j) {
    int k = (int)(BrainStone.proxy.getClientWorld().L().g() & 1L) * 20;

    b(i, j, 196, k, 20, 20);
  }

  public void a(float f, int i, int j)
  {
    registerTexture();

    if (this.help) {
      int l = (this.g - 176) / 2;
      int i1 = (this.h - 90) / 2;
      b(l, i1, 0, 166, 176, 90);
      this.l.a(this.HelpText, l + 10, i1 + 10, 156, 15658734);
    }
    else {
      this.factor = 1.0F;
      int l = this.globalX = (this.g - 176) / 2;
      int i1 = this.globalY = (this.h - 166) / 2;
      b(l, i1, 0, 0, 176, 166);
      this.tileentity.drawBoxes(this, l + 104, i1 + 7);
      this.focused = this.tileentity.getFocused();

      if (this.focused != 0) {
        switch (this.focused) {
        case 1:
          drawFocus(l + 124, i1 + 7);
          break;
        case 2:
          drawFocus(l + 144, i1 + 27);
          break;
        case 3:
          drawFocus(l + 104, i1 + 27);
        }

      }

      GL11.glPushMatrix();
      GL11.glScalef(this.factor, this.factor, this.factor);
      this.tileentity.drawGates(this, l + 6, i1 + 20);
      drawString(bo.a("tile.brainLogicBlock.name"), l + 6, i1 + 6, 0);

      boolean[] aflag = this.tileentity.shallRender();

      if (aflag[0] != 0) {
        drawString(this.tileentity.getPin(0), 130, 50, this.tileentity.getPinColor(0), 2.0F);
      }

      if (aflag[1] != 0) {
        drawString(this.tileentity.getPin(1), 130, 10, this.tileentity.getPinColor(1), 2.0F);
      }

      if (aflag[2] != 0) {
        drawString(this.tileentity.getPin(2), 150, 30, this.tileentity.getPinColor(2), 2.0F);
      }

      if (aflag[3] != 0) {
        drawString(this.tileentity.getPin(3), 110, 30, this.tileentity.getPinColor(3), 2.0F);
      }

      GL11.glPopMatrix();
      A_();
    }
  }

  public void drawSplitString(String s, int i, int j, int k, int l) {
    this.l.a(s, (int)(i / this.factor), (int)(j / this.factor), l, k);
  }

  public void drawString(String s, int i, int j, int k)
  {
    this.l.b(s, (int)(i / this.factor), (int)(j / this.factor), k);
  }

  private void drawString(String s, int i, int j, int k, float f) {
    if (f != this.factor) {
      this.factor = f;
      GL11.glPopMatrix();
      GL11.glPushMatrix();

      if (this.factor == 2.0F) {
        GL11.glTranslatef(this.globalX - 1.0F, this.globalY, 0.0F);
      }

      GL11.glScalef(this.factor, this.factor, this.factor);
    }

    this.l.b(s, (int)(i / this.factor), (int)(j / this.factor), k);
  }

  private boolean inField(int i, int j, int k, int l, int i1, int j1) {
    return (i >= k) && (i <= i1) && (j >= l) && (j <= j1);
  }

  public void A_()
  {
  }

  protected void a(char c, int i)
  {
    if (this.help) {
      closeHelpGui();
    } else {
      if ((i == 1) || (i == this.f.z.N.d)) {
        quit();
      }

      if (i == 205) {
        swap(true);
      }

      if (i == 203) {
        swap(false);
      }

      if (i == 54) {
        rotate(true);
      }

      if (i == 42) {
        rotate(false);
      }

      if ((i == 42) || (i == 54) || (i == 203) || (i == 205))
        click();
    }
  }

  protected void a(int i, int j, int k)
  {
    boolean flag = this.help;

    super.a(i, j, k);

    if ((k != 0) || (flag != this.help)) {
      return;
    }
    if (this.help) {
      closeHelpGui();
    } else {
      i -= (this.g - 176) / 2;
      j -= (this.h - 166) / 2;

      if (inField(i, j, 168, 3, 172, 7)) {
        quit();

        return;
      }

      for (byte byte0 = 0; byte0 < TileEntityBlockBrainLogicBlock.numGates; byte0 = (byte)(byte0 + 1)) {
        int l = 12 * byte0;

        if (inField(i, j, 5, 18 + l, 75, 32 + l)) {
          this.tileentity.setMode(byte0);
        }
      }

      if (inField(i, j, 76, 68, 168, 90)) {
        this.tileentity.invertInvertOutput();
      }

      if (!this.tileentity.isSwapable()) {
        this.tileentity.setFocused(0);
      } else if (inField(i, j, 124, 7, 143, 26)) {
        this.focused = this.tileentity.getFocused();

        if (this.focused != 1)
          this.tileentity.setFocused(1);
        else
          this.tileentity.setFocused(0);
      }
      else if (inField(i, j, 144, 27, 163, 46)) {
        if (this.focused != 2)
          this.tileentity.setFocused(2);
        else
          this.tileentity.setFocused(0);
      }
      else if (inField(i, j, 104, 27, 123, 46)) {
        if (this.focused != 3)
          this.tileentity.setFocused(3);
        else
          this.tileentity.setFocused(0);
      }
      else {
        this.tileentity.setFocused(0);
      }
    }
  }

  private void quit() {
    click();
    this.tileentity.logOut(this.username);
    this.f.a(null);
    this.f.h();
  }

  private void rotate(boolean flag) {
    if (flag) {
      this.tileentity.swapPosition(1, 2);
      this.tileentity.swapPosition(1, 3);
      this.tileentity.addTASKS("setFocused", new String[] { "0" });
    } else {
      this.tileentity.swapPosition(1, 3);
      this.tileentity.swapPosition(1, 2);
      this.tileentity.addTASKS("setFocused", new String[] { "0" });
    }
  }

  private void swap(boolean flag) {
    this.focused = this.tileentity.getFocused();

    if (flag) {
      switch (this.focused) {
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
      switch (this.focused) {
      case 1:
        this.tileentity.swapPosition(1, 3);
        break;
      case 2:
        this.tileentity.swapPosition(2, 1);
        break;
      case 3:
        this.tileentity.swapPosition(3, 2);
      }
  }
}