import net.minecraft.client.Minecraft;

public class GuiBrainLogicBlockHelp extends vp
{
  private final int xSize;
  private final int ySize;
  private final int superYSize;
  private final int stringWidth;
  private String HelpText;
  private GuiBrainLogicBlock SuperGui;

  public GuiBrainLogicBlockHelp(String HelpText, GuiBrainLogicBlock SuperGui)
  {
    this.HelpText = HelpText;
    this.SuperGui = SuperGui;
    this.xSize = 176;
    this.superYSize = 166;
    this.ySize = (256 - this.superYSize);
    this.stringWidth = (this.xSize - 20);
  }

  public void a(int par1, int par2, float par3)
  {
    int t = this.p.p.b("/BrainStone/GuiBrainLogicBlock.png");
    this.p.p.b(t);
    int x = (this.q - this.xSize) / 2;
    int y = (this.r - this.ySize) / 2;

    b(x, y, 0, this.superYSize, this.xSize, this.ySize);

    this.u.a(this.HelpText, x + 10, y + 10, this.stringWidth, 15658734);
  }

  public boolean b()
  {
    return false;
  }

  protected void a(int par1, int par2, int par3)
  {
    if (par3 == 0)
      quit();
  }

  protected void a(char par1, int par2)
  {
    quit();
  }

  private void quit()
  {
    this.p.C.a("random.click", 1.0F, 1.0F);
    this.p.a(this.SuperGui);
  }
}