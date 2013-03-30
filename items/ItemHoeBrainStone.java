package mods.brainstone.items;

import ly;
import mods.brainstone.BrainStone;
import ve;
import wj;
import wl;

public class ItemHoeBrainStone extends wj
{
  public ItemHoeBrainStone(int i, wl enumtoolmaterial)
  {
    super(BrainStone.getId(359 + i), enumtoolmaterial);

    a(ve.i);
  }

  public void a(ly par1IconRegister)
  {
    this.ct = par1IconRegister.a("brainstone:" + a().replaceFirst("item.", ""));
  }
}