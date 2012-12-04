package net.braintonemod.src;

import agb;
import amj;
import um;

public class ItemAxeBrainStone extends ItemToolBrainStone
{
  private static amj[] blocksEffectiveAgainst = { amj.A, amj.aq, amj.M, amj.ax, amj.bd, amj.bi };

  protected ItemAxeBrainStone(int i, EnumToolMaterialBrainStone enumtoolmaterialbrainstone)
  {
    super(BrainStone.getId(359 + i), 3, enumtoolmaterialbrainstone, blocksEffectiveAgainst);
  }

  public String getTextureFile()
  {
    return "/BrainStone/textures.png";
  }

  public float a(um itemstack, amj block)
  {
    if ((block != null) && (block.cB == agb.d))
    {
      return this.a;
    }

    return super.a(itemstack, block);
  }
}