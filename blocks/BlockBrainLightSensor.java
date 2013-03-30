package mods.brainstone.blocks;

import aab;
import aak;
import aif;
import aqp;
import bkd;
import java.io.IOException;
import java.util.Random;
import lx;
import ly;
import mods.brainstone.BrainStone;
import mods.brainstone.ClientProxy;
import mods.brainstone.templates.BSP;
import mods.brainstone.templates.BlockBrainStoneContainerBase;
import mods.brainstone.tileentities.TileEntityBlockBrainLightSensor;
import net.minecraft.client.Minecraft;
import sq;
import ve;

public class BlockBrainLightSensor extends BlockBrainStoneContainerBase
{
  public static lx[] textures;

  public BlockBrainLightSensor(int i)
  {
    super(BrainStone.getId(i), aif.e);

    c(2.4F);
    b(0.5F);
    c("brainLightSensor");
    a(ve.d);

    this.cN = -0.2F;
  }

  public void a(aab world, int i, int j, int k, int par5, int par6)
  {
    world.s(i, j, k);
    world.f(i, j, k, this.cz);
    world.f(i - 1, j, k, this.cz);
    world.f(i + 1, j, k, this.cz);
    world.f(i, j - 1, k, this.cz);
    world.f(i, j + 1, k, this.cz);
    world.f(i, j, k - 1, this.cz);
    world.f(i, j, k + 1, this.cz);
  }

  public aqp b(aab world)
  {
    return new TileEntityBlockBrainLightSensor();
  }

  public lx a(int i, int meta)
  {
    if (i == 1)
      return textures[0];
    if (i == 0) {
      return textures[2];
    }
    return textures[1];
  }

  public int c(aak iblockaccess, int i, int j, int k, int l)
  {
    TileEntityBlockBrainLightSensor tileentityblockbrainlightsensor = (TileEntityBlockBrainLightSensor)iblockaccess.r(i, j, k);

    return (tileentityblockbrainlightsensor != null) && (tileentityblockbrainlightsensor.getPowerOn()) ? 15 : 0;
  }

  public int b(aak iblockaccess, int i, int j, int k, int l)
  {
    return c(iblockaccess, i, j, k, l);
  }

  public boolean a(aab world, int i, int j, int k, sq entityplayer, int unknown, float px, float py, float pz)
  {
    TileEntityBlockBrainLightSensor tileentityblockbrainlightsensor = (TileEntityBlockBrainLightSensor)world.r(i, j, k);

    if (tileentityblockbrainlightsensor == null) {
      BSP.println("The TileEntity is null!");

      return false;
    }
    entityplayer.openGui(BrainStone.instance, 0, world, i, j, k);
    return true;
  }

  public void a(aab world, int i, int j, int k)
  {
    world.a(i, j, k, b(world));
    world.f(i, j, k, this.cz);
    world.f(i - 1, j, k, this.cz);
    world.f(i + 1, j, k, this.cz);
    world.f(i, j - 1, k, this.cz);
    world.f(i, j + 1, k, this.cz);
    world.f(i, j, k - 1, this.cz);
    world.f(i, j, k + 1, this.cz);
    world.a(i, j, k, this.cz, (int)world.G() % a(world));
  }

  public void a(ly IconReg)
  {
    textures = new lx[] { IconReg.a("brainstone:brainLightSensor"), IconReg.a("furnace_side"), IconReg.a("furnace_top") };
  }

  public int a(aab par1World)
  {
    return 2;
  }

  public void a(aab world, int i, int j, int k, Random random)
  {
    TileEntityBlockBrainLightSensor tileentityblockbrainlightsensor = (TileEntityBlockBrainLightSensor)world.r(i, j, k);

    if (tileentityblockbrainlightsensor != null) {
      boolean flag = tileentityblockbrainlightsensor.getPowerOn();
      boolean flag1 = tileentityblockbrainlightsensor.getDirection();

      int l = world.n(i, j + 1, k);
      int i1 = tileentityblockbrainlightsensor.getLightLevel();

      tileentityblockbrainlightsensor.setCurLightLevel(l);

      boolean flag2 = l <= i1;

      if (flag2 != flag) {
        BrainStone.proxy.getClient().B.a("random.click", 1.0F, 1.0F);

        int j1 = random.nextInt(11) + 5;

        for (int k1 = 0; k1 < j1; k1++) {
          world.a("smoke", i + random.nextFloat() * 1.4D - 0.2D, j + 0.8D + random.nextFloat() * 0.6D, k + random.nextFloat() * 1.4D - 0.2D, 0.0D, 0.0D, 0.0D);
        }

        j1 = random.nextInt(3);

        for (int l1 = 0; l1 < j1; l1++) {
          world.a("largesmoke", i + random.nextFloat() * 1.4D - 0.2D, j + 0.8D + random.nextFloat() * 0.6D, k + random.nextFloat() * 1.4D - 0.2D, 0.0D, 0.0D, 0.0D);
        }

      }

      tileentityblockbrainlightsensor.setPowerOn(flag2);

      world.f(i, j, k, this.cz);
      world.f(i - 1, j, k, this.cz);
      world.f(i + 1, j, k, this.cz);
      world.f(i, j - 1, k, this.cz);
      world.f(i, j + 1, k, this.cz);
      world.f(i, j, k - 1, this.cz);
      world.f(i, j, k + 1, this.cz);
      try
      {
        tileentityblockbrainlightsensor.update(false);
      } catch (IOException e) {
        BSP.printException(e);
      }

      world.a(i, j, k, this.cz, a(world));
    } else {
      BSP.println("Die TileEntity fehlt!");
    }
  }
}