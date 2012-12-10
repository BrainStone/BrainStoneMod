package net.braintonemod.src;

import agb;
import aju;
import anq;
import bek;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Random;
import net.minecraft.client.Minecraft;
import qx;
import th;
import xv;
import yf;

public class BlockBrainLightSensor extends aju
{
  protected BlockBrainLightSensor(int i)
  {
    super(BrainStone.getId(i), agb.q);

    c(2.4F);
    b(0.5F);
    b("brainLightSensor");
    a(th.d);

    this.cA = -0.2F;
  }

  public String getTextureFile()
  {
    return "/BrainStoneTextures/textures.png";
  }

  public anq a(xv world)
  {
    return new TileEntityBlockBrainLightSensor();
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
    world.r(i, j, k);
    world.h(i, j, k, this.cm);
    world.h(i - 1, j, k, this.cm);
    world.h(i + 1, j, k, this.cm);
    world.h(i, j - 1, k, this.cm);
    world.h(i, j + 1, k, this.cm);
    world.h(i, j, k - 1, this.cm);
    world.h(i, j, k + 1, this.cm);
  }

  public void b(xv world, int i, int j, int k, Random random)
  {
    TileEntityBlockBrainLightSensor tileentityblockbrainlightsensor = (TileEntityBlockBrainLightSensor)world.q(i, j, k);

    if (tileentityblockbrainlightsensor != null)
    {
      boolean flag = tileentityblockbrainlightsensor.getPowerOn();
      boolean flag1 = tileentityblockbrainlightsensor.getDirection();
      int l = world.m(i, j + 1, k);
      int i1 = tileentityblockbrainlightsensor.getLightLevel();

      tileentityblockbrainlightsensor.setCurLightLevel(l);

      boolean flag2 = l <= i1;

      if (flag2 != flag)
      {
        BrainStone.proxy.getClient().A.a("random.click", 1.0F, 1.0F);
        int j1 = random.nextInt(11) + 5;

        for (int k1 = 0; k1 < j1; k1++)
        {
          world.a("smoke", i + random.nextFloat() * 1.4D - 0.2D, j + 0.8D + random.nextFloat() * 0.6D, k + random.nextFloat() * 1.4D - 0.2D, 0.0D, 0.0D, 0.0D);
        }

        j1 = random.nextInt(3);

        for (int l1 = 0; l1 < j1; l1++)
        {
          world.a("largesmoke", i + random.nextFloat() * 1.4D - 0.2D, j + 0.8D + random.nextFloat() * 0.6D, k + random.nextFloat() * 1.4D - 0.2D, 0.0D, 0.0D, 0.0D);
        }
      }

      tileentityblockbrainlightsensor.setPowerOn(flag2);
      world.i(i, j, k);
      world.h(i, j, k, this.cm);
      world.h(i - 1, j, k, this.cm);
      world.h(i + 1, j, k, this.cm);
      world.h(i, j - 1, k, this.cm);
      world.h(i, j + 1, k, this.cm);
      world.h(i, j, k - 1, this.cm);
      world.h(i, j, k + 1, this.cm);
      try
      {
        tileentityblockbrainlightsensor.update(false);
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
    else
    {
      System.out.println("Die TileEntity fehlt!");
    }

    world.a(i, j, k, this.cm, r_());
  }

  public boolean i()
  {
    return true;
  }

  public boolean c(yf iblockaccess, int i, int j, int k, int l)
  {
    TileEntityBlockBrainLightSensor tileentityblockbrainlightsensor = (TileEntityBlockBrainLightSensor)iblockaccess.q(i, j, k);
    return (tileentityblockbrainlightsensor != null) && (tileentityblockbrainlightsensor.getPowerOn());
  }

  public boolean b(yf iblockaccess, int i, int j, int k, int l)
  {
    return c(iblockaccess, i, j, k, l);
  }

  public int a(int i)
  {
    if (i == 1)
    {
      return 1;
    }

    if (i == 0)
    {
      return 33;
    }

    return 17;
  }

  public int r_()
  {
    return 2;
  }

  public boolean a(xv world, int i, int j, int k, qx entityplayer, int unknown, float px, float py, float pz)
  {
    TileEntityBlockBrainLightSensor tileentityblockbrainlightsensor = (TileEntityBlockBrainLightSensor)world.q(i, j, k);

    if (tileentityblockbrainlightsensor == null)
    {
      System.out.println("The TileEntity is null!");

      return false;
    }

    entityplayer.openGui(BrainStone.instance, 0, world, i, j, k);
    return true;
  }
}