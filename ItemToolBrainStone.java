package brainstone;

import agi;
import amq;
import tw;
import un;
import uq;
import ur;
import uy;
import vj;

public class ItemToolBrainStone extends tw
{
  private final int typeId;

  public ItemToolBrainStone(int i, uq enumtoolmaterial, String type)
  {
    super(BrainStone.getId(359 + i), 0, enumtoolmaterial, getBlocksEffectiveAgainstForToolsType(type));

    this.typeId = getTypeId(type);
  }

  public String getTextureFile()
  {
    return "/brainstone/BrainStoneTextures/textures.png";
  }

  public boolean a(amq par1Block)
  {
    switch (this.typeId)
    {
    case 0:
      return par1Block == amq.aV;
    case 1:
      return this.b.d() == 3;
    }
    return super.a(par1Block);
  }

  public float a(ur par1ItemStack, amq par2Block)
  {
    switch (this.typeId)
    {
    case 1:
      return (par2Block != null) && ((par2Block.cB == agi.f) || (par2Block.cB == agi.g) || (par2Block.cB == agi.e)) ? this.a : super.a(par1ItemStack, par2Block);
    case 2:
      return (par2Block != null) && ((par2Block.cB == agi.d) || (par2Block.cB == agi.k) || (par2Block.cB == agi.l)) ? this.a : super.a(par1ItemStack, par2Block);
    }
    return super.a(par1ItemStack, par2Block);
  }

  public boolean a(ur tool, ur material)
  {
    return material.c == BrainStone.brainStone().cm;
  }

  private static final amq[] getBlocksEffectiveAgainstForToolsType(String type)
  {
    type = type.toLowerCase();

    if (type.contains("spade"))
      return vj.c;
    if (type.contains("pickaxe"))
      return uy.c;
    if (type.contains("axe")) {
      return un.c;
    }
    BSP.throwIllegalArgumentException("The tool type \"" + type + "\" was not regonized!\nPlease choose from \"spade\", \"pickaxe\" or \"axe\"!");

    return null;
  }

  private static final int getTypeId(String type)
  {
    String tmpType = type.toLowerCase();

    if (tmpType.contains("spade"))
      return 0;
    if (tmpType.contains("pickaxe"))
      return 1;
    if (tmpType.contains("axe")) {
      return 2;
    }
    BSP.throwIllegalArgumentException("The tool type \"" + type + "\" was not regonized!\nPlease choose from \"spade\", \"pickaxe\" or \"axe\"!");

    return -1;
  }
}