package net.braintonemod.src;

import arn;
import asl;
import atj;
import auy;
import bap;
import bek;
import bm;
import net.minecraft.client.Minecraft;

public class GuiBrainLightSensor extends auy
{
  private int lightLevel;
  private static final int b = 128;
  private static final int c = 84;
  private int curLightLevel;
  private boolean direction;
  private TileEntityBlockBrainLightSensor tileentity;

  public GuiBrainLightSensor(TileEntityBlockBrainLightSensor tileentityblockbrainlightsensor)
  {
    super(new ContainerBlockBrainLightSensor());
    this.tileentity = tileentityblockbrainlightsensor;
    this.direction = this.tileentity.getDirection();
    setLightLevel(this.tileentity.getLightLevel());
    this.tileentity.GUIopen = true;

    BrainStonePacketHandler.sendUpdateOptions(this.tileentity);
  }

  protected void a(float f, int i, int j)
  {
    int k = this.f.o.b("/BrainStoneTextures/GuiBrainLightSensor.png");
    this.f.o.b(k);
    int l = (this.g - 128) / 2;
    int i1 = (this.h - 84) / 2;
    b(l, i1, 0, 0, 128, 84);

    if (this.direction)
    {
      b(l + 9, i1 + 13, 9, 84, 7 * (this.lightLevel + 1), 64);
    }
    else
    {
      int j1 = 7 * (16 - this.lightLevel);
      int k1 = 112 - j1;
      b(l + 9 + k1, i1 + 13, 9 + k1, 84, j1, 64);
    }

    this.curLightLevel = this.tileentity.getCurLightLevel();
    b(l + this.curLightLevel * 7 + 8, i1 + 12, 8 + 7 * this.curLightLevel, 148, 6, 66);
    this.l.b(bm.a("tile.brainLightSensor.name"), l + 6, i1 + 6, 4210752);
  }

  protected void a(char c, int i)
  {
    if ((i == 1) || (i == this.f.y.G.d))
    {
      quit();
    }

    if (((i == 200) || (i == 205)) && (this.lightLevel != 15))
    {
      setLightLevel(this.lightLevel + 1);
    }

    if (((i == 208) || (i == 203)) && (this.lightLevel != 0))
    {
      setLightLevel(this.lightLevel - 1);
    }

    if ((i == 208) || (i == 203) || (i == 200) || (i == 205))
    {
      click();
    }
  }

  protected void a(int i, int j, int k)
  {
    if (k == 0)
    {
      i -= (this.g - 128) / 2;
      j -= (this.h - 84) / 2;

      for (int l = 0; l < 16; l++)
      {
        int i1 = 7 * l;
        int j1 = 2 * l;

        if (inField(i, j, 9 + i1, 43 - j1, 12 + i1, 46 + j1))
        {
          click();
          setLightLevel(l);
        }
      }

      if (inField(i, j, 120, 3, 124, 7))
      {
        quit();
      }
    }
  }

  private void quit()
  {
    this.tileentity.GUIopen = false;
    BrainStonePacketHandler.sendUpdateOptions(this.tileentity);
    click();
    this.f.a(null);
    this.f.h();
  }

  private boolean inField(int x, int y, int xmin, int ymin, int xmax, int ymax)
  {
    return (x >= xmin) && (x <= xmax) && (y >= ymin) && (y <= ymax);
  }

  private void setLightLevel(int i)
  {
    i &= 15;

    if ((this.direction) && (i == 15))
    {
      this.direction = false;
      this.lightLevel = i;
    }
    else if ((!this.direction) && (i == 0))
    {
      this.direction = true;
      this.lightLevel = i;
    }
    else
    {
      this.lightLevel = i;
    }

    this.tileentity.setLightLevel(this.lightLevel);
    this.tileentity.setDirection(this.direction);

    BrainStonePacketHandler.sendUpdateOptions(this.tileentity);
  }

  private void click()
  {
    this.f.A.a("random.click", 1.0F, 1.0F);
  }
}