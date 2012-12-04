package net.braintonemod.src;

import agb;
import aju;
import th;

public abstract class BlockBrainStoneHiders extends aju
{
  public BlockBrainStoneHiders(int i)
  {
    super(BrainStone.getId(i), agb.q);

    a(th.d);
    setTextureFile("/BrainStone/textures.png");
  }

  public boolean c()
  {
    return true;
  }

  public boolean b()
  {
    return true;
  }

  public boolean i()
  {
    return true;
  }
}