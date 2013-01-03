package brainstone;

import uq;
import vu;

public class ItemSwordBrainStone extends vu
{
  public ItemSwordBrainStone(int i, uq enumtoolmaterial)
  {
    super(BrainStone.getId(359 + i), enumtoolmaterial);
  }

  public String getTextureFile()
  {
    return "/brainstone/BrainStoneTextures/textures.png";
  }
}