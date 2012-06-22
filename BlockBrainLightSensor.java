import java.util.HashMap;
import java.util.Random;
import net.minecraft.client.Minecraft;

public class BlockBrainLightSensor extends pb
{
  private static int textureIdUp;
  private static Minecraft mc;
  private static BlockBrainLightSensor.StateContainer state;

  protected BlockBrainLightSensor(int i, int j)
  {
    super(i, 45, acn.p);

    state = new BlockBrainLightSensor.StateContainer(null);

    textureIdUp = j;
    this.cc = -0.2F;

    mc = ModLoader.getMinecraftInstance();
  }

  public void a(xd par1World, int par2, int par3, int par4)
  {
    setLightLevel(8, par1World, par2, par3, par4);

    state.put(new BlockBrainLightSensor.Coord(par2, par3, par4), false);

    if (par1World.e(par2, par3, par4) == 0)
    {
      super.a(par1World, par2, par3, par4);
    }

    par1World.j(par2, par3, par4, this.bO);
    par1World.a(par2, par3, par4, this.bO, 0);
  }

  public void b_(xd par1World, int par2, int par3, int par4)
  {
    par1World.j(par2, par3, par4, this.bO);

    setLightLevel(0, par1World, par2, par3, par4);

    state.remove(new BlockBrainLightSensor.Coord(par2, par3, par4));
  }

  public void a(xd par1World, int par2, int par3, int par4, Random random)
  {
    BlockBrainLightSensor.Coord coords = new BlockBrainLightSensor.Coord(par2, par3, par4);

    if (!state.containsKey(coords)) {
      state.put(coords, false);
    }
    boolean tmp = state.get(coords);
    state.put(coords, par1World.o(par2, par3 + 1, par4) > getLightLevel(par1World, par2, par3, par4));

    if (tmp != state.get(coords)) {
      mc.C.a("random.click", 1.0F, 1.0F);
    }
    par1World.j(par2, par3, par4, this.bO);
    par1World.a(par2, par3, par4, this.bO, e());
  }

  public boolean g()
  {
    return true;
  }

  public boolean b(ali par1IBlockAccess, int par2, int par3, int par4, int par5)
  {
    int i = par1IBlockAccess.e(par2, par3, par4);
    BlockBrainLightSensor.Coord coords = new BlockBrainLightSensor.Coord(par2, par3, par4);

    if (!state.containsKey(coords)) {
      return false;
    }
    if (!state.get(coords)) {
      return false;
    }
    if ((i == 5) && (par5 == 1))
    {
      return false;
    }

    if ((i == 3) && (par5 == 3))
    {
      return false;
    }

    if ((i == 4) && (par5 == 2))
    {
      return false;
    }

    if ((i == 1) && (par5 == 5))
    {
      return false;
    }

    return (i != 2) || (par5 != 4);
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

    return true;
  }

  public void setLightLevel(int lightLevel, xd world, int x, int y, int z)
  {
    world.f(x, y, z, lightLevel);
  }

  public int getLightLevel(xd world, int x, int y, int z)
  {
    return world.e(x, y, z);
  }

  private class StateContainer
  {
    private HashMap state = new HashMap();

    private StateContainer() {
    }
    public void put(BlockBrainLightSensor.Coord coords, boolean flag) { HashMap depth2 = new HashMap();
      depth2.put(Integer.valueOf(coords.z), Boolean.valueOf(flag));
      HashMap depth1 = new HashMap();
      depth1.put(Integer.valueOf(coords.y), depth2);

      this.state.put(Integer.valueOf(coords.x), depth1);
    }

    public boolean get(BlockBrainLightSensor.Coord coords)
    {
      return ((Boolean)((HashMap)((HashMap)this.state.get(Integer.valueOf(coords.x))).get(Integer.valueOf(coords.y))).get(Integer.valueOf(coords.z))).booleanValue();
    }

    public boolean containsKey(BlockBrainLightSensor.Coord coords)
    {
      if (!this.state.containsKey(Integer.valueOf(coords.x))) {
        return false;
      }
      HashMap depth1 = (HashMap)this.state.get(Integer.valueOf(coords.x));

      if (!depth1.containsKey(Integer.valueOf(coords.y))) {
        return false;
      }
      HashMap depth2 = (HashMap)depth1.get(Integer.valueOf(coords.y));

      return depth2.containsKey(Integer.valueOf(coords.z));
    }

    public void remove(BlockBrainLightSensor.Coord coords)
    {
      if (!containsKey(coords)) {
        return;
      }
      ((HashMap)((HashMap)this.state.get(Integer.valueOf(coords.x))).get(Integer.valueOf(coords.y))).remove(Integer.valueOf(coords.z));
    }
  }

  private class Coord
  {
    public int x;
    public int y;
    public int z;

    public Coord(int x, int y, int z)
    {
      this.x = x;
      this.y = y;
      this.z = z;
    }
  }
}