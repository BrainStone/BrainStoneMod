package mods.brainstone.items;

import ly;
import mods.brainstone.BrainStone;
import uy;
import wd;
import wf;

public class ItemHoeBrainStone extends wd
{
  public ItemHoeBrainStone(int i, wf enumtoolmaterial)
  {
    super(BrainStone.getId(359 + i), enumtoolmaterial);

    a(uy.i);
  }

  public void a(ly par1IconRegister)
  {
    this.ct = par1IconRegister.a("brainstone:" + a().replaceFirst("item.", ""));
  }
}