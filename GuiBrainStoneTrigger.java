package net.braintonemod.src;

import arn;
import asl;
import atj;
import auy;
import bap;
import bek;
import bm;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import qw;

public class GuiBrainStoneTrigger extends auy
{
  private TileEntityBlockBrainStoneTrigger tileentity;
  private int j;
  private int k;
  private int mouse_pos;

  public GuiBrainStoneTrigger(qw inventoryplayer, TileEntityBlockBrainStoneTrigger tileentityblockbrainstonetrigger)
  {
    super(new ContainerBlockBrainStoneTrigger(inventoryplayer, tileentityblockbrainstonetrigger));
    this.tileentity = tileentityblockbrainstonetrigger;
    this.j = 0;
    this.k = 0;
    this.mouse_pos = -1;
  }

  protected void b(int par1, int par2)
  {
    this.l.b(bm.a("tile.brainStoneTrigger.name"), 6, 6, 4210752);
    this.l.b(bm.a("container.inventory"), 8, this.c - 96 + 2, 4210752);

    if (this.tileentity == null)
    {
      return;
    }

    this.l.b(bm.a("gui.brainstone.item"), 21, 20, this.tileentity.getMobTriggered(0) ? 413702 : 14696512);
    this.l.b(bm.a("gui.brainstone.animal"), 21, 38, this.tileentity.getMobTriggered(1) ? 413702 : 14696512);
    this.l.b(bm.a("gui.brainstone.monster"), 21, 56, this.tileentity.getMobTriggered(2) ? 413702 : 14696512);
    this.l.b(bm.a("gui.brainstone.nether"), 102, 20, this.tileentity.getMobTriggered(3) ? 413702 : 14696512);
    this.l.b(bm.a("gui.brainstone.player"), 102, 38, this.tileentity.getMobTriggered(4) ? 413702 : 14696512);
  }

  protected void a(float f, int i, int l)
  {
    if (this.tileentity == null)
    {
      return;
    }

    int i1 = this.f.o.b("/BrainStone/GuiBrainStoneTrigger.png");
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    this.f.o.b(i1);
    this.j = ((this.g - this.b) / 2);
    this.k = ((this.h - this.c) / 2);
    b(this.j, this.k, 0, 0, this.b, this.c);

    if (this.mouse_pos == 0)
    {
      b(this.j + 8, this.k + 20, 0, 166, 8, 8);
    }

    if (this.mouse_pos == 1)
    {
      b(this.j + 8, this.k + 38, 0, 166, 8, 8);
    }

    if (this.mouse_pos == 2)
    {
      b(this.j + 8, this.k + 56, 0, 166, 8, 8);
    }

    if (this.mouse_pos == 3)
    {
      b(this.j + 89, this.k + 20, 0, 166, 8, 8);
    }

    if (this.mouse_pos == 4)
    {
      b(this.j + 89, this.k + 38, 0, 166, 8, 8);
    }

    if (this.tileentity.getMobTriggered(0))
    {
      b(this.j + 7, this.k + 20, 8, 166, 10, 8);
    }

    if (this.tileentity.getMobTriggered(1))
    {
      b(this.j + 7, this.k + 38, 8, 166, 10, 8);
    }

    if (this.tileentity.getMobTriggered(2))
    {
      b(this.j + 7, this.k + 56, 8, 166, 10, 8);
    }

    if (this.tileentity.getMobTriggered(3))
    {
      b(this.j + 88, this.k + 20, 8, 166, 10, 8);
    }

    if (this.tileentity.getMobTriggered(4))
    {
      b(this.j + 88, this.k + 38, 8, 166, 10, 8);
    }
  }

  protected void b(int i, int l, int i1)
  {
    i -= this.g / 2 - this.b / 2;
    l -= this.h / 2 - this.c / 2;
    this.mouse_pos = (-1 + (inField(i, l, 8, 20, 15, 27) ? 1 : 0) + (inField(i, l, 8, 38, 15, 45) ? 2 : 0) + (inField(i, l, 8, 56, 15, 63) ? 3 : 0) + (inField(i, l, 89, 20, 96, 27) ? 4 : 0) + (inField(i, l, 89, 38, 96, 45) ? 5 : 0));
  }

  protected void a(int i, int l, int i1)
  {
    super.a(i, l, i1);

    if (this.tileentity == null)
    {
      return;
    }

    if (i1 == 0)
    {
      i -= (this.g - this.b) / 2;
      l -= (this.h - this.c) / 2;

      if (inField(i, l, 167, 4, 171, 8))
      {
        quit();
      }

      if (inField(i, l, 8, 20, 15, 27))
      {
        this.tileentity.invertMobTriggered(0);
        click();
      }

      if (inField(i, l, 8, 38, 15, 45))
      {
        this.tileentity.invertMobTriggered(1);
        click();
      }

      if (inField(i, l, 8, 56, 15, 63))
      {
        this.tileentity.invertMobTriggered(2);
        click();
      }

      if (inField(i, l, 89, 20, 96, 27))
      {
        this.tileentity.invertMobTriggered(3);
        click();
      }

      if (inField(i, l, 89, 38, 96, 45))
      {
        this.tileentity.invertMobTriggered(4);
        click();
      }

      this.tileentity.forceUpdate = true;
      BrainStonePacketHandler.sendUpdateOptions(this.tileentity);
    }
  }

  protected void a(char c, int i)
  {
    if ((i == 1) || (i == this.f.y.G.d))
    {
      quit();
    }
  }

  private boolean inField(int i, int l, int i1, int j1, int k1, int l1)
  {
    return (i >= i1) && (i <= k1) && (l >= j1) && (l <= l1);
  }

  private void click()
  {
    this.f.A.a("random.click", 1.0F, 1.0F);
  }

  private void quit()
  {
    click();
    this.f.a(null);
    this.f.h();
  }
}