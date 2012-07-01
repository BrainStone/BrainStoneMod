import net.minecraft.client.Minecraft;

public class GuiBrainLightSensor extends vp
{
  private BlockBrainLightSensor Block;
  private int lightLevel;
  private boolean direction;
  private static Minecraft p;
  private int x;
  private int y;
  private int z;
  private xd world;
  int xSize;
  int ySize;

  public GuiBrainLightSensor(BlockBrainLightSensor Block, xd world, int x, int y, int z)
  {
    this.x = x;
    this.y = y;
    this.z = z;
    this.world = world;

    this.Block = Block;
    this.direction = Block.getDirection(world, x, y, z);
    setLightLevel(Block.getLightLevel(world, x, y, z));

    p = ModLoader.getMinecraftInstance();

    this.xSize = 128;
    this.ySize = 79;
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
      b(x + 9, y + 8, 9, 79, 7 * (this.lightLevel + 1), 64);
    }
    else
    {
      int redWidth = 7 * (16 - this.lightLevel);
      int versch = 112 - redWidth;

      b(x + 9 + versch, y + 8, 9 + versch, 79, redWidth, 64);
    }
  }

  protected void a(char par1, int par2)
  {
    if ((par2 == 1) || (par2 == 18))
    {
      click();
      quit();
    }
  }

  protected void a(int par1, int par2, int par3)
  {
    par1 -= this.q / 2 - this.xSize / 2;
    par2 -= this.r / 2 - this.ySize / 2;

    if (par3 == 0)
    {
      for (int i = 0; i < 16; i++)
      {
        int i7 = 7 * i;
        int i2 = 2 * i;

        if (inField(par1, par2, 9 + i7, 38 - i2, 12 + i7, 41 + i2))
        {
          click();
          setLightLevel(i);
        }
      }

      if (inField(par1, par2, 120, 3, 124, 7))
      {
        click();
        quit();
      }
    }
  }

  private void quit()
  {
    this.Block.setLightLevel(this.lightLevel, this.world, this.x, this.y, this.z);
    this.Block.setDirection(this.direction, this.world, this.x, this.y, this.z);
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

      return;
    }
    if ((!this.direction) && (lightLevel == 0))
    {
      this.direction = true;
      this.lightLevel = lightLevel;

      return;
    }

    this.lightLevel = lightLevel;
  }

  private void click()
  {
    p.C.a("random.click", 1.0F, 1.0F);
  }
}