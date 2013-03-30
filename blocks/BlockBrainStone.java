package mods.brainstone.blocks;

import aab;
import aif;
import apa;
import java.util.Random;
import mods.brainstone.BrainStone;
import mods.brainstone.templates.BlockBrainStoneBase;
import ve;

public class BlockBrainStone extends BlockBrainStoneBase
{
  private final boolean powered;

  public BlockBrainStone(int i, boolean flag)
  {
    super(BrainStone.getId(i), aif.e);
    c(3.0F);
    b(1.0F);

    if (flag) {
      a(0.0F);
      c("brainStoneOut");
    } else {
      a(1.0F);
      c("brainStone");
      a(ve.b);
    }

    this.powered = flag;
    this.cN = -0.2F;
  }

  public int a(int i, Random random, int j)
  {
    return BrainStone.dirtyBrainStone().cz;
  }

  public void a(aab world, int i, int j, int k)
  {
    if (!world.I)
      if ((this.powered) && (!world.C(i, j, k)))
        world.f(i, j, k, BrainStone.brainStone().cz, 0, 2);
      else if ((!this.powered) && (world.C(i, j, k)))
      {
        world.f(i, j, k, BrainStone.brainStoneOut().cz, 0, 2);
      }
  }

  public void a(aab world, int i, int j, int k, int l)
  {
    if (!world.I)
      if ((this.powered) && (!world.C(i, j, k)))
        world.f(i, j, k, BrainStone.brainStone().cz, 0, 2);
      else if ((!this.powered) && (world.C(i, j, k)))
      {
        world.f(i, j, k, BrainStone.brainStoneOut().cz, 0, 2);
      }
  }
}