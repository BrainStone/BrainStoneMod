package net.braintonemod.src;

import agb;
import amj;
import um;

public class ItemPickaxeBrainStone extends ItemToolBrainStone
{
  private static amj[] blocksEffectiveAgainst = { amj.z, amj.w, amj.T, amj.ar, amj.K, amj.al, amj.L, amj.ak, amj.J, amj.az, amj.aA, amj.aW, amj.be, amj.Q, amj.R, amj.aQ, amj.aR, amj.aJ, amj.X, amj.W, BrainStone.brainStone(), BrainStone.brainStoneOut(), BrainStone.brainStoneOre(), BrainStone.dirtyBrainStone(), BrainStone.brainLightSensor() };

  protected ItemPickaxeBrainStone(int i, EnumToolMaterialBrainStone enumtoolmaterialbrainstone)
  {
    super(BrainStone.getId(359 + i), 2, enumtoolmaterialbrainstone, blocksEffectiveAgainst);
  }

  public String getTextureFile()
  {
    return "/BrainStoneTextures/textures.png";
  }

  public boolean a(amj block)
  {
    if (block == amj.as)
    {
      return this.b.getHarvestLevel() == 3;
    }

    if ((block == amj.aA) || (block == amj.az))
    {
      return this.b.getHarvestLevel() >= 2;
    }

    if ((block == amj.ak) || (block == amj.J))
    {
      return this.b.getHarvestLevel() >= 2;
    }

    if ((block == amj.al) || (block == amj.K))
    {
      return this.b.getHarvestLevel() >= 1;
    }

    if ((block == amj.R) || (block == amj.Q))
    {
      return this.b.getHarvestLevel() >= 1;
    }

    if ((block == amj.aQ) || (block == amj.aR))
    {
      return this.b.getHarvestLevel() >= 2;
    }

    if (block.cB == agb.e)
    {
      return true;
    }

    return block.cB == agb.f;
  }

  public float a(um itemstack, amj block)
  {
    if ((block != null) && ((block.cB == agb.f) || (block.cB == agb.e)))
    {
      return this.a;
    }

    return super.a(itemstack, block);
  }
}