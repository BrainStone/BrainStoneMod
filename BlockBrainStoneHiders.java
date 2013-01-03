package brainstone;

import agi;
import akb;
import tj;

public abstract class BlockBrainStoneHiders extends akb
{
  public BlockBrainStoneHiders(int i)
  {
    super(BrainStone.getId(i), agi.q);

    a(tj.d);
    setTextureFile("/brainstone/BrainStoneTextures/textures.png");
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