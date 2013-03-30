package mods.brainstone.templates;

import ans;
import ly;

public class BlockBrainStoneOreBase extends ans
{
  public BlockBrainStoneOreBase(int par1)
  {
    super(par1);
  }

  public void a(ly par1IconRegister)
  {
    this.cQ = par1IconRegister.a("brainstone:" + a().replaceFirst("tile.", ""));
  }
}