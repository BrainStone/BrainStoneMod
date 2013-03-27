package mods.brainstone.templates;

import ahz;
import akt;
import ly;
import zv;

public abstract class BlockBrainStoneContainerBase extends akt
{
  protected BlockBrainStoneContainerBase(int par1, ahz par2Material)
  {
    super(par1, par2Material);
  }

  public void a(ly par1IconRegister)
  {
    this.cQ = par1IconRegister.a("brainstone:" + a().substring(5));
  }

  public boolean isBlockNormalCube(zv world, int x, int y, int z)
  {
    return true;
  }
}