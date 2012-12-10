package net.braintonemod.src;

import anq;
import anw;
import java.io.PrintStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;
import lq;
import qx;
import xv;
import yf;

public class BlockBrainStoneTrigger extends BlockBrainStoneHiders
{
  public static LinkedHashMap triggerEntities;

  public BlockBrainStoneTrigger(int i)
  {
    super(i);

    c(2.4F);
    b(0.5F);
    b("brainStoneTrigger");
    r();
    setTextureFile("/BrainStoneTextures/textures.png");

    this.cA = -0.2F;
  }

  public anq a(xv world)
  {
    return new TileEntityBlockBrainStoneTrigger();
  }

  public void g(xv world, int i, int j, int k)
  {
    world.a(i, j, k, a(world));
    world.h(i, j, k, this.cm);
    world.h(i - 1, j, k, this.cm);
    world.h(i + 1, j, k, this.cm);
    world.h(i, j - 1, k, this.cm);
    world.h(i, j + 1, k, this.cm);
    world.h(i, j, k - 1, this.cm);
    world.h(i, j, k + 1, this.cm);
    world.a(i, j, k, this.cm, 0);
  }

  public void a(xv world, int i, int j, int k, int par5, int par6)
  {
    TileEntityBlockBrainStoneTrigger tileentityblockbrainstonetrigger = (TileEntityBlockBrainStoneTrigger)world.q(i, j, k);

    if (tileentityblockbrainstonetrigger != null)
    {
      tileentityblockbrainstonetrigger.dropItems(world, i, j, k);
    }

    world.r(i, j, k);
    world.h(i, j, k, this.cm);
    world.h(i - 1, j, k, this.cm);
    world.h(i + 1, j, k, this.cm);
    world.h(i, j - 1, k, this.cm);
    world.h(i, j + 1, k, this.cm);
    world.h(i, j, k - 1, this.cm);
    world.h(i, j, k + 1, this.cm);
  }

  public boolean a(xv world, int i, int j, int k, qx entityplayer, int par6, float par7, float par8, float par9)
  {
    if (world.J)
    {
      return true;
    }

    TileEntityBlockBrainStoneTrigger tileentityblockbrainstonetrigger = (TileEntityBlockBrainStoneTrigger)world.q(i, j, k);

    if (tileentityblockbrainstonetrigger != null)
    {
      entityplayer.openGui(BrainStone.instance, 1, world, i, j, k);
    }

    return true;
  }

  public void b(xv world, int i, int j, int k, Random random)
  {
    TileEntityBlockBrainStoneTrigger tileentityblockbrainstonetrigger = (TileEntityBlockBrainStoneTrigger)world.q(i, j, k);

    if (tileentityblockbrainstonetrigger == null)
    {
      world.a(i, j, k, this.cm, r_());
      return;
    }

    boolean flag = tileentityblockbrainstonetrigger.power;
    tileentityblockbrainstonetrigger.power = triggerCorrectMob(world, i, j, k);
    tileentityblockbrainstonetrigger.delay = ((byte)(tileentityblockbrainstonetrigger.delay <= 0 ? 0 : tileentityblockbrainstonetrigger.power ? tileentityblockbrainstonetrigger.max_delay : tileentityblockbrainstonetrigger.delay - 1));

    if (((!flag) && (tileentityblockbrainstonetrigger.power)) || (tileentityblockbrainstonetrigger.forceUpdate))
    {
      BrainStonePacketHandler.sendReRenderBlockAtPacket(i, j, k, world);

      tileentityblockbrainstonetrigger.forceUpdate = false;
    }

    world.d(i, j, k, this.cm, 0);
    world.h(i, j, k, this.cm);
    world.h(i - 1, j, k, this.cm);
    world.h(i + 1, j, k, this.cm);
    world.h(i, j - 1, k, this.cm);
    world.h(i, j + 1, k, this.cm);
    world.h(i, j, k - 1, this.cm);
    world.h(i, j, k + 1, this.cm);

    world.a(i, j, k, this.cm, r_());
  }

  public int d(yf iblockaccess, int i, int j, int k, int l)
  {
    if (l == 1)
    {
      TileEntityBlockBrainStoneTrigger tileentityblockbrainstonetrigger = (TileEntityBlockBrainStoneTrigger)iblockaccess.q(i, j, k);

      if (tileentityblockbrainstonetrigger == null)
      {
        return 3;
      }

      int i1 = tileentityblockbrainstonetrigger.getTextureId(iblockaccess, i, j, k);

      return i1 != -1 ? i1 : 2;
    }

    if (l == 0)
    {
      return 34;
    }

    return 18;
  }

  public int a(int i)
  {
    if (i == 1)
    {
      return 2;
    }
    if (i == 0)
    {
      return 34;
    }

    return 18;
  }

  public boolean c(yf iblockaccess, int i, int j, int k, int l)
  {
    TileEntityBlockBrainStoneTrigger tileentityblockbrainstonetrigger = (TileEntityBlockBrainStoneTrigger)iblockaccess.q(i, j, k);
    return (tileentityblockbrainstonetrigger != null) && (tileentityblockbrainstonetrigger.delay > 0);
  }

  public boolean b(yf iblockaccess, int i, int j, int k, int l)
  {
    return c(iblockaccess, i, j, k, l);
  }

  public int r_()
  {
    return 2;
  }

  private boolean triggerCorrectMob(xv world, int i, int j, int k)
  {
    List list = world.b(null, anw.a(i, j + 1, k, i + 1, j + 2, k + 1));
    TileEntityBlockBrainStoneTrigger tileentityblockbrainstonetrigger = (TileEntityBlockBrainStoneTrigger)world.q(i, j, k);

    if (tileentityblockbrainstonetrigger == null)
    {
      return false;
    }

    boolean flag = false;

    for (int l = 0; (l < list.size()) && (!flag); l++)
    {
      Class entity = ((lq)list.get(l)).getClass();

      if (entity == null)
      {
        System.out.println("Fehler! Die Entity ist nicht vorhanden!");
      }
      else
      {
        int length = triggerEntities.size();
        String[] keys = (String[])triggerEntities.keySet().toArray(new String[length]);

        for (int count = 0; (count < length) && (!flag); count++)
        {
          String key = keys[count];

          if (tileentityblockbrainstonetrigger.getMobTriggered(key))
          {
            Class[] classes = (Class[])triggerEntities.get(key);

            for (int a = 0; a < classes.length; a++)
            {
              if (classes[a].isAssignableFrom(entity))
              {
                flag = true;

                break;
              }
            }
          }
        }
      }
    }
    return flag;
  }
}