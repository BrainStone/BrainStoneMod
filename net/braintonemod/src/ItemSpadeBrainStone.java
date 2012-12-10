package net.braintonemod.src;

import amj;

public class ItemSpadeBrainStone extends ItemToolBrainStone
{
  private static amj[] blocksEffectiveAgainst = { amj.x, amj.y, amj.H, amj.I, amj.aV, amj.aX, amj.aZ, amj.aD, amj.bf, amj.bB };

  public ItemSpadeBrainStone(int i, EnumToolMaterialBrainStone enumtoolmaterialbrainstone)
  {
    super(BrainStone.getId(359 + i), 1, enumtoolmaterialbrainstone, blocksEffectiveAgainst);
  }

  public String getTextureFile()
  {
    return "/BrainStoneTextures/textures.png";
  }

  public boolean a(amj block)
  {
    if (block == amj.aV)
    {
      return true;
    }

    return block == amj.aX;
  }
}