package mods.brainstone.items;

import ly;
import mods.brainstone.BrainStone;
import wf;
import xl;

public class ItemSwordBrainStone extends xl
{
  public ItemSwordBrainStone(int i, wf enumtoolmaterial)
  {
    super(BrainStone.getId(359 + i), enumtoolmaterial);
  }

  public void a(ly par1IconRegister)
  {
    this.ct = par1IconRegister.a("brainstone:" + a().replaceFirst("item.", ""));
  }
}