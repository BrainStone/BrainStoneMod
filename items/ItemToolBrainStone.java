package mods.brainstone.items;

import aif;
import apa;
import ly;
import mods.brainstone.BrainStone;
import mods.brainstone.templates.BSP;
import vr;
import wi;
import wl;
import wm;
import wu;
import xf;

public class ItemToolBrainStone extends vr
{
  private final int typeId;

  private static final apa[] getBlocksEffectiveAgainstForToolsType(String type)
  {
    type = type.toLowerCase();

    if (type.contains("spade"))
      return xf.c;
    if (type.contains("pickaxe"))
      return wu.c;
    if (type.contains("axe")) {
      return wi.c;
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

  public ItemToolBrainStone(int i, wl enumtoolmaterial, String type)
  {
    super(BrainStone.getId(359 + i), 0, enumtoolmaterial, getBlocksEffectiveAgainstForToolsType(type));

    this.typeId = getTypeId(type);
  }

  public boolean a(apa par1Block)
  {
    switch (this.typeId) {
    case 0:
      return par1Block == apa.aW;
    case 1:
      return this.b.d() == 3;
    }

    return super.a(par1Block);
  }

  public boolean a(wm tool, wm material)
  {
    return material.c == BrainStone.brainStone().cz;
  }

  public float a(wm par1ItemStack, apa par2Block)
  {
    switch (this.typeId) {
    case 1:
      return (par2Block != null) && ((par2Block.cO == aif.f) || (par2Block.cO == aif.g) || (par2Block.cO == aif.e)) ? this.a : super.a(par1ItemStack, par2Block);
    case 2:
      return (par2Block != null) && ((par2Block.cO == aif.d) || (par2Block.cO == aif.k) || (par2Block.cO == aif.l)) ? this.a : super.a(par1ItemStack, par2Block);
    }

    return super.a(par1ItemStack, par2Block);
  }

  public void a(ly par1IconRegister)
  {
    this.ct = par1IconRegister.a("brainstone:" + a().replaceFirst("item.", ""));
  }
}