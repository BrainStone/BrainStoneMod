package mods.brainstone.templates;

import aif;
import apa;
import ly;

public class BlockBrainStoneBase extends apa
{
  public BlockBrainStoneBase(int par1, aif par2Material)
  {
    super(par1, par2Material);
  }

  public void a(ly par1IconRegister)
  {
    this.cQ = par1IconRegister.a("brainstone:" + a().replaceFirst("tile.", ""));
  }
}