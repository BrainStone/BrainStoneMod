import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class GuiBrainStoneTrigger extends gb
{
  private TileEntityBlockBrainStoneTrigger furnaceInventory;

  public GuiBrainStoneTrigger(aak par1InventoryPlayer, TileEntityBlockBrainStoneTrigger par2TileEntityFurnace)
  {
    super(new ContainerBlockBrainStoneTrigger(par1InventoryPlayer, par2TileEntityFurnace));
    this.furnaceInventory = par2TileEntityFurnace;
  }

  protected void d()
  {
    this.u.b(cy.a("tile.brainStoneTrigger.name"), 8, 6, 4210752);
    this.u.b(cy.a("container.inventory"), 8, this.c - 96 + 2, 4210752);
  }

  protected void a(float par1, int par2, int par3)
  {
    int i = this.p.p.b("/BrainStone/GuiBrainStoneTrigger.png");
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    this.p.p.b(i);
    int j = (this.q - this.b) / 2;
    int k = (this.r - this.c) / 2;
    b(j, k, 0, 0, this.b, this.c);
  }
}