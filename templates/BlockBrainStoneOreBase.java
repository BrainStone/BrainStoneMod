package mods.brainstone.templates;

import anm;
import ly;

public class BlockBrainStoneOreBase extends anm
{
  public BlockBrainStoneOreBase(int par1)
  {
    super(par1);
  }

  public void a(ly par1IconRegister)
  {
    this.cQ = par1IconRegister.a("brainstone:" + a().substring(5));
  }
}