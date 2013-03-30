package mods.brainstone.blocks;

import aab;
import aak;
import aqp;
import aqx;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;
import lx;
import ly;
import mods.brainstone.BrainStone;
import mods.brainstone.handlers.BrainStonePacketHandler;
import mods.brainstone.templates.BSP;
import mods.brainstone.tileentities.TileEntityBlockBrainStoneTrigger;
import mp;
import sq;

public class BlockBrainStoneTrigger extends BlockBrainStoneHiders
{
  public static LinkedHashMap triggerEntities;
  public static lx[] textures;

  public BlockBrainStoneTrigger(int i)
  {
    super(i);

    c(2.4F);
    b(0.5F);
    c("brainStoneTrigger");

    this.cN = -0.2F;
  }

  public void a(aab world, int i, int j, int k, int par5, int par6)
  {
    TileEntityBlockBrainStoneTrigger tileentityblockbrainstonetrigger = (TileEntityBlockBrainStoneTrigger)world.r(i, j, k);

    if (tileentityblockbrainstonetrigger != null) {
      tileentityblockbrainstonetrigger.dropItems(world, i, j, k);
    }

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
    return new TileEntityBlockBrainStoneTrigger();
  }

  public lx b_(aak iblockaccess, int i, int j, int k, int l)
  {
    if (l == 1) {
      TileEntityBlockBrainStoneTrigger tileentityblockbrainstonetrigger = (TileEntityBlockBrainStoneTrigger)iblockaccess.r(i, j, k);

      if (tileentityblockbrainstonetrigger == null) {
        return textures[0];
      }
      return tileentityblockbrainstonetrigger.getTextureId(iblockaccess, i, j, k);
    }

    if (l == 0) {
      return textures[2];
    }
    return textures[1];
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
    TileEntityBlockBrainStoneTrigger tileentityblockbrainstonetrigger = (TileEntityBlockBrainStoneTrigger)iblockaccess.r(i, j, k);

    return (tileentityblockbrainstonetrigger != null) && (tileentityblockbrainstonetrigger.delay > 0) ? 15 : 0;
  }

  public int b(aak iblockaccess, int i, int j, int k, int l)
  {
    return c(iblockaccess, i, j, k, l);
  }

  public boolean a(aab world, int i, int j, int k, sq entityplayer, int par6, float par7, float par8, float par9)
  {
    if (world.I) {
      return true;
    }
    TileEntityBlockBrainStoneTrigger tileentityblockbrainstonetrigger = (TileEntityBlockBrainStoneTrigger)world.r(i, j, k);

    if (tileentityblockbrainstonetrigger != null) {
      entityplayer.openGui(BrainStone.instance, 1, world, i, j, k);
    }

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
    textures = new lx[] { IconReg.a("brainstone:brainStoneTrigger"), IconReg.a("furnace_side"), IconReg.a("furnace_top") };
  }

  public int a(aab par1World)
  {
    return 2;
  }

  private boolean triggerCorrectMob(aab world, int i, int j, int k)
  {
    List list = world.b(null, aqx.a(i, j + 1, k, i + 1, j + 2, k + 1));

    TileEntityBlockBrainStoneTrigger tileentityblockbrainstonetrigger = (TileEntityBlockBrainStoneTrigger)world.r(i, j, k);

    if (tileentityblockbrainstonetrigger == null) {
      return false;
    }
    boolean flag = false;

    for (int l = 0; (l < list.size()) && (!flag); l++) {
      Class entity = ((mp)list.get(l)).getClass();

      if (entity == null) {
        BSP.println("Fehler! Die Entity ist nicht vorhanden!");
      }
      else
      {
        int length = triggerEntities.size();
        String[] keys = (String[])triggerEntities.keySet().toArray(new String[length]);

        for (int count = 0; (count < length) && (!flag); count++) {
          String key = keys[count];

          if (tileentityblockbrainstonetrigger.getMobTriggered(key)) {
            Class[] classes = (Class[])triggerEntities.get(key);

            for (int a = 0; a < classes.length; a++) {
              if (classes[a].isAssignableFrom(entity)) {
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

  public void a(aab world, int i, int j, int k, Random random)
  {
    TileEntityBlockBrainStoneTrigger tileentityblockbrainstonetrigger = (TileEntityBlockBrainStoneTrigger)world.r(i, j, k);

    if (tileentityblockbrainstonetrigger == null) {
      world.a(i, j, k, this.cz, a(world));
      return;
    }

    tileentityblockbrainstonetrigger.power = triggerCorrectMob(world, i, j, k);

    tileentityblockbrainstonetrigger.delay = ((byte)(tileentityblockbrainstonetrigger.delay <= 0 ? 0 : tileentityblockbrainstonetrigger.power ? tileentityblockbrainstonetrigger.max_delay : tileentityblockbrainstonetrigger.delay - 1));

    if (tileentityblockbrainstonetrigger.checkForSlotChange()) {
      world.j(i, j, k);
      BrainStonePacketHandler.sendReRenderBlockAtPacket(i, j, k, world);
    }

    world.f(i, j, k, this.cz);
    world.f(i - 1, j, k, this.cz);
    world.f(i + 1, j, k, this.cz);
    world.f(i, j - 1, k, this.cz);
    world.f(i, j + 1, k, this.cz);
    world.f(i, j, k - 1, this.cz);
    world.f(i, j, k + 1, this.cz);

    world.a(i, j, k, this.cz, a(world));
  }
}