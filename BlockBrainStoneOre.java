package net.braintonemod.src;

import agb;
import amj;
import java.util.Random;
import th;
import uk;

public class BlockBrainStoneOre extends amj
{
  public BlockBrainStoneOre(int i)
  {
    super(BrainStone.getId(i), 32, agb.e);

    c(2.0F);
    b("brainStoneOre");
    b(0.25F);
    a(0.3F);
    a(th.b);
    this.cA = 0.2F;
  }

  public String getTextureFile()
  {
    return "/BrainStone/textures.png";
  }

  public int a(Random random)
  {
    return random.nextInt(2);
  }

  public int a(int i, Random random, int j)
  {
    return BrainStone.brainStoneDust().cg;
  }

  public int a(int i, Random random)
  {
    int j = random.nextInt(i + 2) - 1;

    if (j < 0)
    {
      j = 0;
    }

    return a(random) * (j + 1);
  }
}