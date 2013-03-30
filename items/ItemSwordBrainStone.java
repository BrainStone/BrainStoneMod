package mods.brainstone.items;

import apa;
import ly;
import mods.brainstone.BrainStone;
import wl;
import wm;
import xr;

public class ItemSwordBrainStone extends xr
{
  public ItemSwordBrainStone(int i, wl enumtoolmaterial)
  {
    super(BrainStone.getId(359 + i), enumtoolmaterial);
  }

  public boolean a(wm tool, wm material)
  {
    return material.c == BrainStone.brainStone().cz;
  }

  public void a(ly par1IconRegister)
  {
    this.ct = par1IconRegister.a("brainstone:" + a().replaceFirst("item.", ""));
  }
}