package net.braintonemod.src;

import ale;
import amj;
import java.util.Random;
import ke;
import th;
import uk;
import xv;

public class BlockBrainStoneOre extends ale
{
  public BlockBrainStoneOre(int i)
  {
    super(BrainStone.getId(i), 32);

    c(2.0F);
    b("brainStoneOre");
    b(0.25F);
    a(0.3F);
    a(th.b);
    this.cA = 0.2F;
  }

  public String getTextureFile()
  {
    return "/BrainStoneTextures/textures.png";
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

  public void a(xv par1World, int par2, int par3, int par4, int par5, float par6, int par7)
  {
    super.a(par1World, par2, par3, par4, par5, par6, par7);

    if (a(par5, par1World.u, par7) != this.cm)
    {
      int var8 = 0;

      if (this.cm == BrainStone.brainStoneOre().cm)
      {
        var8 = ke.a(par1World.u, 10, 20);
      }

      f(par1World, par2, par3, par4, var8);
    }
  }
}