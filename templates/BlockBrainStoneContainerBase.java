package mods.brainstone.templates;

import aab;
import aif;
import akz;
import ly;
import net.minecraftforge.common.ForgeDirection;

public abstract class BlockBrainStoneContainerBase extends akz
{
  protected BlockBrainStoneContainerBase(int par1, aif par2Material)
  {
    super(par1, par2Material);
  }

  public boolean f()
  {
    return true;
  }

  public boolean isBlockSolidOnSide(aab world, int x, int y, int z, ForgeDirection side)
  {
    return true;
  }

  public void a(ly par1IconRegister)
  {
    this.cQ = par1IconRegister.a("brainstone:" + a().replaceFirst("tile.", ""));
  }
}