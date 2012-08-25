import net.minecraft.client.Minecraft;

public class GuiBrainLightSensor extends vp
{
  private int lightLevel;
  private int xSize;
  private int ySize;
  private int curLightLevel;
  private boolean direction;
  private static Minecraft p;
  private TileEntityBlockBrainLightSensor tileentity;

  public GuiBrainLightSensor(TileEntityBlockBrainLightSensor tileentity)
  {
    this.tileentity = tileentity;

    this.direction = tileentity.getDirection();
    setLightLevel(tileentity.getLightLevel());

    p = ModLoader.getMinecraftInstance();

    this.xSize = 128;
    this.ySize = 84;

    tileentity.GUIopen = true;
  }

  public void a(int par1, int par2, float par3)
  {
    int t = p.p.b("/BrainStone/GuiBrainLightSensor.png");
    p.p.b(t);
    int x = (this.q - this.xSize) / 2;
    int y = (this.r - this.ySize) / 2;
    b(x, y, 0, 0, this.xSize, this.ySize);

    if (this.direction)
    {
      b(x + 9, y + 13, 9, 84, 7 * (this.lightLevel + 1), 64);
    }
    else
    {
      int redWidth = 7 * (16 - this.lightLevel);
      int versch = 112 - redWidth;

      b(x + 9 + versch, y + 13, 9 + versch, 84, redWidth, 64);
    }

    this.curLightLevel = this.tileentity.getCurLightLevel();

    b(x + this.curLightLevel * 7 + 8, y + 12, 8 + 7 * this.curLightLevel, 148, 6, 66);

    this.u.b(cy.a("tile.brainLightSensor.name"), x + 6, y + 6, 4210752);
  }

  protected void a(char par1, int par2)
  {
    if ((par2 == 1) || (par2 == p.A.s.d)) {
      quit();
    }
    if (((par2 == 200) || (par2 == 205)) && (this.lightLevel != 15)) {
      setLightLevel(this.lightLevel + 1);
    }
    if (((par2 == 208) || (par2 == 203)) && (this.lightLevel != 0)) {
      setLightLevel(this.lightLevel - 1);
    }
    if ((par2 == 208) || (par2 == 203) || (par2 == 200) || (par2 == 205))
      click();
  }

  protected void a(int par1, int par2, int par3)
  {
    if (par3 == 0)
    {
      par1 -= (this.q - this.xSize) / 2;
      par2 -= (this.r - this.ySize) / 2;

      for (int i = 0; i < 16; i++)
      {
        int i7 = 7 * i;
        int i2 = 2 * i;

        if (inField(par1, par2, 9 + i7, 43 - i2, 12 + i7, 46 + i2))
        {
          click();
          setLightLevel(i);
        }
      }

      if (inField(par1, par2, 120, 3, 124, 7))
        quit();
    }
  }

  private void quit()
  {
    this.tileentity.GUIopen = false;
    click();
    p.a(null);
    p.g();
  }

  private boolean inField(int varX, int varY, int minX, int minY, int maxX, int maxY)
  {
    return (varX >= minX) && (varX <= maxX) && (varY >= minY) && (varY <= maxY);
  }

  private void setLightLevel(int lightLevel)
  {
    lightLevel &= 15;

    if ((this.direction) && (lightLevel == 15))
    {
      this.direction = false;
      this.lightLevel = lightLevel;
    }
    else if ((!this.direction) && (lightLevel == 0))
    {
      this.direction = true;
      this.lightLevel = lightLevel;
    }
    else
    {
      this.lightLevel = lightLevel;
    }

    this.tileentity.setLightLevel(this.lightLevel);
    this.tileentity.setDirection(this.direction);
  }

  private void click()
  {
    p.C.a("random.click", 1.0F, 1.0F);
  }

  public boolean b()
  {
    return false;
  }
}