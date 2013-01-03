package brainstone;

import agi;
import amq;
import java.util.Random;
import tj;
import yc;

public class BlockBrainStone extends amq
{
  private final boolean powered;

  public BlockBrainStone(int i, boolean flag)
  {
    super(BrainStone.getId(i), flag ? 16 : 0, agi.e);
    c(3.0F);
    b(1.0F);

    if (flag)
    {
      a(0.0F);
      b("brainStoneOut");
    }
    else
    {
      a(1.0F);
      b("brainStone");
      a(tj.b);
    }

    this.powered = flag;
    this.cA = -0.2F;
  }

  public String getTextureFile()
  {
    return "/brainstone/BrainStoneTextures/textures.png";
  }

  public void g(yc world, int i, int j, int k)
  {
    if (!world.I)
    {
      if ((this.powered) && (!world.B(i, j, k)))
      {
        world.a(i, j, k, this.cm, 4);
      }
      else if ((!this.powered) && (world.B(i, j, k)))
      {
        world.e(i, j, k, BrainStone.brainStoneOut().cm);
      }
    }
  }

  public void a(yc world, int i, int j, int k, int l)
  {
    if (!world.I)
    {
      if ((this.powered) && (!world.B(i, j, k)))
      {
        world.a(i, j, k, this.cm, 4);
      }
      else if ((!this.powered) && (world.B(i, j, k)))
      {
        world.e(i, j, k, BrainStone.brainStoneOut().cm);
      }
    }
  }

  public void b(yc world, int i, int j, int k, Random random)
  {
    if ((!world.I) && (this.powered) && (!world.B(i, j, k)))
    {
      world.e(i, j, k, BrainStone.brainStone().cm);
    }
  }

  public int a(int i, Random random, int j)
  {
    return BrainStone.dirtyBrainStone().cm;
  }
}