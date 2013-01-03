package brainstone;

import tj;
import uo;
import uq;

public class ItemHoeBrainStone extends uo
{
  public ItemHoeBrainStone(int i, uq enumtoolmaterial)
  {
    super(BrainStone.getId(359 + i), enumtoolmaterial);

    a(tj.i);
  }

  public String getTextureFile()
  {
    return "/brainstone/BrainStoneTextures/textures.png";
  }
}