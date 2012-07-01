import java.io.PrintStream;
import java.util.Random;
import net.minecraft.client.Minecraft;

public class BlockBrainLightSensor extends agy
{
  private static int textureIdUp;
  private static Minecraft mc;

  protected BlockBrainLightSensor(int i, int j)
  {
    super(i, 45, acn.p);

    textureIdUp = j;
    this.cc = -0.2F;

    mc = ModLoader.getMinecraftInstance();
  }

  public kw u_()
  {
    return new TileEntityBlockBrainLightSensor();
  }

  public void a(xd par1World, int par2, int par3, int par4)
  {
    par1World.a(par2, par3, par4, u_());
    par1World.j(par2, par3, par4, this.bO);
    par1World.j(par2 - 1, par3, par4, this.bO);
    par1World.j(par2 + 1, par3, par4, this.bO);
    par1World.j(par2, par3 - 1, par4, this.bO);
    par1World.j(par2, par3 + 1, par4, this.bO);
    par1World.j(par2, par3, par4 - 1, this.bO);
    par1World.j(par2, par3, par4 + 1, this.bO);

    par1World.a(par2, par3, par4, this.bO, 0);
  }

  public void b_(xd par1World, int par2, int par3, int par4)
  {
    par1World.q(par2, par3, par4);
    par1World.j(par2, par3, par4, this.bO);
    par1World.j(par2 - 1, par3, par4, this.bO);
    par1World.j(par2 + 1, par3, par4, this.bO);
    par1World.j(par2, par3 - 1, par4, this.bO);
    par1World.j(par2, par3 + 1, par4, this.bO);
    par1World.j(par2, par3, par4 - 1, this.bO);
    par1World.j(par2, par3, par4 + 1, this.bO);
  }

  public void a(xd par1World, int par2, int par3, int par4, Random random)
  {
    TileEntityBlockBrainLightSensor tileentity = (TileEntityBlockBrainLightSensor)par1World.b(par2, par3, par4);

    if (tileentity != null)
    {
      boolean tmp = tileentity.getPowerOn();
      boolean direction = tileentity.getDirection();

      int lightLevel = par1World.o(par2, par3 + 1, par4);
      int intern_lightLevel = tileentity.getLightLevel();

      boolean powerOn = lightLevel <= intern_lightLevel;

      if (powerOn != tmp) {
        mc.C.a("random.click", 1.0F, 1.0F);
      }
      tileentity.setPowerOn(powerOn);

      par1World.j(par2, par3, par4, this.bO);
      par1World.j(par2 - 1, par3, par4, this.bO);
      par1World.j(par2 + 1, par3, par4, this.bO);
      par1World.j(par2, par3 - 1, par4, this.bO);
      par1World.j(par2, par3 + 1, par4, this.bO);
      par1World.j(par2, par3, par4 - 1, this.bO);
      par1World.j(par2, par3, par4 + 1, this.bO);
    }
    else
    {
      System.out.println("Die TileEntity fehlt!");
    }

    par1World.a(par2, par3, par4, this.bO, e());
  }

  public boolean g()
  {
    return true;
  }

  public boolean b(ali par1IBlockAccess, int par2, int par3, int par4, int par5)
  {
    int i = par1IBlockAccess.e(par2, par3, par4);
    TileEntityBlockBrainLightSensor tileentity = (TileEntityBlockBrainLightSensor)par1IBlockAccess.b(par2, par3, par4);

    return (tileentity != null) && (tileentity.getPowerOn());
  }

  public boolean e(xd par1World, int par2, int par3, int par4, int par5)
  {
    return b(par1World, par2, par3, par4, par5);
  }

  public int a_(int i)
  {
    if (i == 1)
    {
      return textureIdUp;
    }
    if (i == 0)
    {
      return 62;
    }

    return this.bN;
  }

  public int e()
  {
    return 2;
  }

  public boolean b(xd par1World, int par2, int par3, int par4, yw par5EntityPlayer)
  {
    if (par1World.F)
    {
      return true;
    }

    ModLoader.openGUI(par5EntityPlayer, new GuiBrainLightSensor(this, par1World, par2, par3, par4));

    par1World.j(par2, par3, par4, this.bO);

    return true;
  }

  public void setLightLevel(int lightLevel, xd world, int x, int y, int z)
  {
    TileEntityBlockBrainLightSensor tileentity = (TileEntityBlockBrainLightSensor)world.b(x, y, z);

    if (tileentity == null)
    {
      return;
    }

    tileentity.setLightLevel(lightLevel);
  }

  public void setDirection(boolean direction, xd world, int x, int y, int z)
  {
    TileEntityBlockBrainLightSensor tileentity = (TileEntityBlockBrainLightSensor)world.b(x, y, z);

    if (tileentity == null)
    {
      return;
    }

    tileentity.setDirection(direction);
  }

  public int getLightLevel(xd world, int x, int y, int z)
  {
    TileEntityBlockBrainLightSensor tileentity = (TileEntityBlockBrainLightSensor)world.b(x, y, z);

    if (tileentity == null)
    {
      return 8;
    }

    return tileentity.getLightLevel();
  }

  public boolean getDirection(xd world, int x, int y, int z)
  {
    TileEntityBlockBrainLightSensor tileentity = (TileEntityBlockBrainLightSensor)world.b(x, y, z);

    if (tileentity == null)
    {
      return true;
    }

    return tileentity.getDirection();
  }
}