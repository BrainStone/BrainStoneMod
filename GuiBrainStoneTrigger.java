import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class GuiBrainStoneTrigger extends gb
{
  private TileEntityBlockBrainStoneTrigger tileentity;
  private int j;
  private int k;
  private int mouse_pos;

  public GuiBrainStoneTrigger(aak par1InventoryPlayer, TileEntityBlockBrainStoneTrigger par2TileEntity)
  {
    super(new ContainerBlockBrainStoneTrigger(par1InventoryPlayer, par2TileEntity));
    this.tileentity = par2TileEntity;
    this.j = 0; this.k = 0; this.mouse_pos = -1;
  }

  protected void d()
  {
    this.u.b(cy.a("tile.brainStoneTrigger.name"), 8, 6, 4210752);
    this.u.b(cy.a("container.inventory"), 8, this.c - 96 + 2, 4210752);

    if (this.tileentity == null) {
      return;
    }
    this.u.b(cy.a("gui.brainstone.item"), 21, 20, this.tileentity.getMobTriggered(0) ? 413702 : 14696512);
    this.u.b(cy.a("gui.brainstone.animal"), 21, 38, this.tileentity.getMobTriggered(1) ? 413702 : 14696512);
    this.u.b(cy.a("gui.brainstone.monster"), 21, 56, this.tileentity.getMobTriggered(2) ? 413702 : 14696512);
    this.u.b(cy.a("gui.brainstone.nether"), 102, 20, this.tileentity.getMobTriggered(3) ? 413702 : 14696512);
    this.u.b(cy.a("gui.brainstone.player"), 102, 38, this.tileentity.getMobTriggered(4) ? 413702 : 14696512);
  }

  protected void a(float par1, int par2, int par3)
  {
    if (this.tileentity == null) {
      return;
    }
    int i = this.p.p.b("/BrainStone/GuiBrainStoneTrigger.png");
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    this.p.p.b(i);
    this.j = ((this.q - this.b) / 2);
    this.k = ((this.r - this.c) / 2);
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

  protected void b(int par1, int par2, int par3)
  {
    super.b(par1, par2, par3);

    par1 -= this.q / 2 - this.b / 2;
    par2 -= this.r / 2 - this.c / 2;

    this.mouse_pos = (-1 + (inField(par1, par2, 8, 20, 15, 27) ? 1 : 0) + (inField(par1, par2, 8, 38, 15, 45) ? 2 : 0) + (inField(par1, par2, 8, 56, 15, 63) ? 3 : 0) + (inField(par1, par2, 89, 20, 96, 27) ? 4 : 0) + (inField(par1, par2, 89, 38, 96, 45) ? 5 : 0));
  }

  protected void a(int par1, int par2, int par3)
  {
    super.a(par1, par2, par3);

    if (this.tileentity == null) {
      return;
    }
    par1 -= this.q / 2 - this.b / 2;
    par2 -= this.r / 2 - this.c / 2;

    if (par3 == 0)
    {
      if (inField(par1, par2, 167, 4, 171, 8)) {
        quit();
      }
      if (inField(par1, par2, 8, 20, 15, 27))
      {
        this.tileentity.invertMobTriggered(0);
        click();
      }

      if (inField(par1, par2, 8, 38, 15, 45))
      {
        this.tileentity.invertMobTriggered(1);
        click();
      }

      if (inField(par1, par2, 8, 56, 15, 63))
      {
        this.tileentity.invertMobTriggered(2);
        click();
      }

      if (inField(par1, par2, 89, 20, 96, 27))
      {
        this.tileentity.invertMobTriggered(3);
        click();
      }

      if (inField(par1, par2, 89, 38, 96, 45))
      {
        this.tileentity.invertMobTriggered(4);
        click();
      }
    }
  }

  protected void a(char par1, int par2)
  {
    if ((par2 == 1) || (par2 == this.p.A.s.d))
      quit();
  }

  private boolean inField(int varX, int varY, int minX, int minY, int maxX, int maxY)
  {
    return (varX >= minX) && (varX <= maxX) && (varY >= minY) && (varY <= maxY);
  }

  private void click()
  {
    this.p.C.a("random.click", 1.0F, 1.0F);
  }

  private void quit()
  {
    click();
    this.p.a(null);
    this.p.g();
  }
}