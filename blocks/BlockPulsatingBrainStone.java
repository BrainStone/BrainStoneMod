package mods.brainstone.blocks;

import aab;
import aif;
import ajv;
import aqx;
import ara;
import cpw.mods.fml.common.network.Player;
import java.util.List;
import java.util.Random;
import kx;
import mk;
import ml;
import mods.brainstone.BrainStone;
import mods.brainstone.handlers.BrainStonePacketHandler;
import mods.brainstone.templates.BSP;
import mods.brainstone.templates.BlockBrainStoneBase;
import ng;
import so;
import sq;
import ve;
import wk;
import wm;

public class BlockPulsatingBrainStone extends BlockBrainStoneBase
{
  private final boolean effect;
  private static int hasEffectId;
  private static int hasNoEffectId;

  public BlockPulsatingBrainStone(int i, boolean effect)
  {
    super(BrainStone.getId(i), aif.e);

    this.effect = effect;

    c(3.0F);
    b(1.0F);
    a(1.0F);

    if (effect) {
      c("pulsatingBrainStoneEffect");
      hasEffectId = this.cz;
    } else {
      c("pulsatingBrainStone");
      a(ve.b);
      hasNoEffectId = this.cz;
    }

    this.cN = ((float)kx.a(new Random(), 3.0D, -3.0D));
  }

  protected boolean r_()
  {
    return false;
  }

  public wm getPickBlock(ara target, aab world, int x, int y, int z)
  {
    return new wm(BrainStone.pulsatingBrainStone(), 1);
  }

  private int getRandomPotion(Random random)
  {
    mk potion;
    do
      potion = mk.a[random.nextInt(32)];
    while (potion == null);

    return potion.H;
  }

  public int a(int i, Random random, int j)
  {
    return hasNoEffectId;
  }

  public void a(aab world, int i, int j, int k)
  {
    super.a(world, i, j, k);
    world.a(i, j, k, this.cz, (int)world.G() % a(world));
  }

  public int a(aab par1World)
  {
    return 2;
  }

  public void a(aab world, int x, int y, int z, Random random)
  {
    int metaData = (int)(world.L().g() / a(world) % 16L);

    if (metaData >= 15) {
      if (this.effect) {
        if (random.nextInt(2) == 0) {
          world.f(x, y, z, hasNoEffectId, 0, 2);
        }
      }
      else if (random.nextInt(4) == 0) {
        world.f(x, y, z, hasEffectId, 0, 2);
      }
    }
    else if ((metaData == 8) && (this.effect)) {
      BSP.debugOnly_println("Effect Time!");

      List list = world.b(null, aqx.a(x - 10, y - 10, z - 10, x + 11, y + 11, z + 11));

      int size = list.size();

      for (int i = 0; i < size; i++) {
        Object tmpEntity = list.get(i);

        BSP.debugOnly_println(tmpEntity.getClass().getName());

        if ((tmpEntity instanceof sq)) {
          wm[] playerArmor = ((sq)tmpEntity).bK.b;

          if ((playerArmor[3] != null) && (playerArmor[2] != null) && (playerArmor[1] != null) && (playerArmor[0] != null) && (playerArmor[3].c == BrainStone.brainStoneHelmet().cp) && (playerArmor[2].c == BrainStone.brainStonePlate().cp) && (playerArmor[1].c == BrainStone.brainStoneLeggings().cp) && (playerArmor[0].c == BrainStone.brainStoneBoots().cp))
          {
            BSP.debugOnly_println("Player wears armor! No effect!");

            continue;
          }
        }

        if ((tmpEntity instanceof ng)) {
          ng entity = (ng)tmpEntity;

          double radius = kx.a(random, 2.0D, 10.0D);

          if (entity.f(x + 0.5D, y + 0.5D, z + 0.5D) <= radius) {
            int taskRand = random.nextInt(10);

            if ((taskRand >= 0) && (taskRand < 6)) {
              BSP.debugOnly_println("Potion Effect");

              entity.d(new ml(getRandomPotion(random), random.nextInt(5980) + 20, random.nextInt(4)));
            }
            else if ((taskRand >= 6) && (taskRand < 10)) {
              BSP.debugOnly_println("Kick");

              double x1 = kx.a(random, -1.5D, 1.5D);

              double y1 = kx.a(random, 0.0D, 3.0D);

              double z1 = kx.a(random, -1.5D, 1.5D);

              if ((tmpEntity instanceof sq)) {
                BrainStonePacketHandler.sendPlayerUpdateMovementPacket((Player)entity, x1, y1, z1);
              }
              else
              {
                entity.g(x1, y1, z1);
              }
            }
          }
        }
      }
    }

    world.a(x, y, z, this.cz, a(world));
    BrainStonePacketHandler.sendReRenderBlockAtPacket(x, y, z, world);
  }
}