import java.util.Random;

public class BlockBrainStoneOre extends pb
{
  public BlockBrainStoneOre(int par1, int par2)
  {
    super(par1, par2, acn.e);
  }

  public int a(Random random)
  {
    return random.nextInt(2);
  }

  public int a(int i, Random random, int j)
  {
    return mod_BrainStone.brainStoneDust.bQ;
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