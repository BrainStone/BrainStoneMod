package mods.brainstone.blocks;

import aab;
import apa;
import java.util.Random;
import kx;
import mods.brainstone.BrainStone;
import mods.brainstone.templates.BlockBrainStoneOreBase;
import ve;
import wk;

public class BlockBrainStoneOre extends BlockBrainStoneOreBase
{
  public BlockBrainStoneOre(int i)
  {
    super(BrainStone.getId(i));

    c(2.0F);
    c("brainStoneOre");
    b(0.25F);
    a(0.3F);
    a(ve.b);
    this.cN = 0.2F;
  }

  public void a(aab par1World, int par2, int par3, int par4, int par5, float par6, int par7)
  {
    super.a(par1World, par2, par3, par4, par5, par6, par7);

    if (a(par5, par1World.s, par7) != this.cz) {
      int var8 = 0;

      if (this.cz == BrainStone.brainStoneOre().cz) {
        var8 = kx.a(par1World.s, 10, 20);
      }

      j(par1World, par2, par3, par4, var8);
    }
  }

  public int a(int i, Random random, int j)
  {
    return BrainStone.brainStoneDust().cp;
  }

  public int a(Random random)
  {
    return random.nextInt(2);
  }

  public int a(int i, Random random)
  {
    return random.nextInt(2 + i);
  }
}