import net.minecraft.client.Minecraft;

public class GuiBrainLightSensor extends vp
{
  private BlockBrainLightSensor Block;
  private int lightLevel;
  private static Minecraft p;
  private int x;
  private int y;
  private int z;
  private xd world;

  public GuiBrainLightSensor(BlockBrainLightSensor Block, xd world, int x, int y, int z)
  {
    this.x = x;
    this.y = y;
    this.z = z;
    this.world = world;

    this.Block = Block;
    this.lightLevel = Block.getLightLevel(world, x, y, z);

    p = ModLoader.getMinecraftInstance();
  }

  public void a(int par1, int par2, float par3)
  {
    int xSize = 160;
    int ySize = 84;
    int t = p.p.b("/BrainStone/GuiBrainLightSensor.png");
    p.p.b(t);
    int x = (this.q - xSize) / 2;
    int y = (this.r - ySize) / 2;
    b(x, y, 0, 0, xSize, ySize);

    String str = String.valueOf(this.lightLevel);

    if (this.lightLevel < 10)
    {
      this.u.b(str, this.q / 2 - 32, this.r / 2 - 2, 0);
    }
    else
    {
      this.u.b(str, this.q / 2 - 35, this.r / 2 - 2, 0);
    }
  }

  protected void a(char par1, int par2)
  {
    if ((par2 == 1) || (par2 == 18))
    {
      p.C.a("random.click", 1.0F, 1.0F);
      quit();
    }
    else if (par2 == 200)
    {
      p.C.a("random.click", 1.0F, 1.0F);
      count(true);
    }
    else if (par2 == 208)
    {
      p.C.a("random.click", 1.0F, 1.0F);
      count(false);
    }
  }

  protected void a(int par1, int par2, int par3)
  {
    par1 -= this.q / 2 - 80;
    par2 -= this.r / 2 - 42;

    if (par3 == 0)
    {
      if (inField(par1, par2, 92, 20, 119, 39))
      {
        p.C.a("random.click", 1.0F, 1.0F);
        count(true);
      }
      else if (inField(par1, par2, 92, 44, 119, 63))
      {
        p.C.a("random.click", 1.0F, 1.0F);
        count(false);
      }
      else if (inField(par1, par2, 128, 16, 147, 35))
      {
        p.C.a("random.click", 1.0F, 1.0F);
        quit();
      }
    }
  }

  private void quit()
  {
    this.Block.setLightLevel(this.lightLevel, this.world, this.x, this.y, this.z);
    p.a(null);
    p.g();
  }

  private boolean inField(int varX, int varY, int minX, int minY, int maxX, int maxY)
  {
    return (varX >= minX) && (varX <= maxX) && (varY >= minY) && (varY <= maxY);
  }

  private void count(boolean up)
  {
    if ((this.lightLevel < 15) && (up)) {
      this.lightLevel += 1;
    }
    if ((this.lightLevel > 1) && (!up))
      this.lightLevel -= 1;
  }
}