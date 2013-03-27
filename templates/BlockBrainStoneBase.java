package mods.brainstone.templates;

import ahz;
import aou;
import ly;

public class BlockBrainStoneBase extends aou
{
  public BlockBrainStoneBase(int par1, ahz par2Material)
  {
    super(par1, par2Material);
  }

  public void a(ly par1IconRegister)
  {
    this.cQ = par1IconRegister.a("brainstone:" + a().replaceFirst("tile.", ""));
  }
}