package mods.brainstone.items;

import ahz;
import aou;
import ly;
import mods.brainstone.BrainStone;
import mods.brainstone.templates.BSP;
import vl;
import wc;
import wf;
import wg;
import wo;
import wz;

public class ItemToolBrainStone extends vl
{
  private final int typeId;

  private static final aou[] getBlocksEffectiveAgainstForToolsType(String type)
  {
    type = type.toLowerCase();

    if (type.contains("spade"))
      return wz.c;
    if (type.contains("pickaxe"))
      return wo.c;
    if (type.contains("axe")) {
      return wc.c;
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

  public ItemToolBrainStone(int i, wf enumtoolmaterial, String type)
  {
    super(BrainStone.getId(359 + i), 0, enumtoolmaterial, getBlocksEffectiveAgainstForToolsType(type));

    this.typeId = getTypeId(type);
  }

  public boolean a(aou par1Block)
  {
    switch (this.typeId) {
    case 0:
      return par1Block == aou.aW;
    case 1:
      return this.b.d() == 3;
    }

    return super.a(par1Block);
  }

  public void a(ly par1IconRegister)
  {
    this.ct = par1IconRegister.a("brainstone:" + a().replaceFirst("item.", ""));
  }

  public boolean a(wg tool, wg material)
  {
    return material.c == BrainStone.brainStone().cz;
  }

  public float a(wg par1ItemStack, aou par2Block)
  {
    switch (this.typeId) {
    case 1:
      return (par2Block != null) && ((par2Block.cO == ahz.f) || (par2Block.cO == ahz.g) || (par2Block.cO == ahz.e)) ? this.a : super.a(par1ItemStack, par2Block);
    case 2:
      return (par2Block != null) && ((par2Block.cO == ahz.d) || (par2Block.cO == ahz.k) || (par2Block.cO == ahz.l)) ? this.a : super.a(par1ItemStack, par2Block);
    }

    return super.a(par1ItemStack, par2Block);
  }
}