package net.braintonemod.src;

import agb;
import amj;
import java.util.Random;
import th;
import xv;

public class BlockBrainStone extends amj
{
  private final boolean powered;

  public BlockBrainStone(int i, boolean flag)
  {
    super(BrainStone.getId(i), flag ? 16 : 0, agb.e);
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
      a(th.b);
    }

    this.powered = flag;
    this.cA = -0.2F;
  }

  public String getTextureFile()
  {
    return "/BrainStone/textures.png";
  }

  public void g(xv world, int i, int j, int k)
  {
    if (!world.J)
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

  public void a(xv world, int i, int j, int k, int l)
  {
    if (!world.J)
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

  public void b(xv world, int i, int j, int k, Random random)
  {
    if ((!world.J) && (this.powered) && (!world.B(i, j, k)))
    {
      world.e(i, j, k, BrainStone.brainStone().cm);
    }
  }

  public int a(int i, Random random, int j)
  {
    return BrainStone.dirtyBrainStone().cm;
  }
}