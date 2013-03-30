package mods.brainstone.templates;

import ly;
import wk;

public class ItemBrainStoneBase extends wk
{
  public ItemBrainStoneBase(int par1)
  {
    super(par1);
  }

  public void a(ly par1IconRegister)
  {
    this.ct = par1IconRegister.a("brainstone:" + a().replaceFirst("item.", ""));
  }
}