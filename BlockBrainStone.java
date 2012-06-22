import java.util.Random;

public class BlockBrainStone extends pb
{
  private final boolean powered;

  public BlockBrainStone(int i, int j, boolean powered)
  {
    super(i, j, acn.e);

    this.powered = powered;
    this.cc = -0.2F;
  }

  public void a(xd par1World, int par2, int par3, int par4)
  {
    if (!par1World.F)
    {
      if ((this.powered) && (!par1World.x(par2, par3, par4)))
      {
        par1World.a(par2, par3, par4, this.bO, 4);
      }
      else if ((!this.powered) && (par1World.x(par2, par3, par4)))
      {
        par1World.g(par2, par3, par4, mod_BrainStone.brainStoneOut.bO);
      }
    }
  }

  public void a(xd par1World, int par2, int par3, int par4, int par5)
  {
    if (!par1World.F)
    {
      if ((this.powered) && (!par1World.x(par2, par3, par4)))
      {
        par1World.a(par2, par3, par4, this.bO, 4);
      }
      else if ((!this.powered) && (par1World.x(par2, par3, par4)))
      {
        par1World.g(par2, par3, par4, mod_BrainStone.brainStoneOut.bO);
      }
    }
  }

  public void a(xd par1World, int par2, int par3, int par4, Random par5Random)
  {
    if ((!par1World.F) && (this.powered) && (!par1World.x(par2, par3, par4)))
    {
      par1World.g(par2, par3, par4, mod_BrainStone.brainStone.bO);
    }
  }

  public int a(int i, Random random, int j)
  {
    return mod_BrainStone.dirtyBrainStone.bO;
  }
}