package brainstone;

import agi;
import ahx;
import amq;
import aoe;
import aoh;
import cpw.mods.fml.common.network.Player;
import java.util.List;
import java.util.Random;
import ke;
import ll;
import lm;
import md;
import qw;
import qx;
import tj;
import up;
import ur;
import yc;

public class BlockPulsatingBrainStone extends amq
{
  private final boolean effect;
  private static int hasEffectId;
  private static int hasNoEffectId;

  public BlockPulsatingBrainStone(int i, boolean effect)
  {
    super(BrainStone.getId(i), agi.e);

    this.effect = effect;

    c(3.0F);
    b(1.0F);
    a(1.0F);

    if (effect)
    {
      b("pulsatingBrainStoneEffect");
      hasEffectId = this.cm;
    }
    else
    {
      b("pulsatingBrainStone");
      a(tj.b);
      hasNoEffectId = this.cm;
    }

    this.cA = ((float)ke.a(new Random(), 0.3D, -0.3D));
  }

  public String getTextureFile()
  {
    return "/brainstone/BrainStoneTextures/textures.png";
  }

  public void g(yc world, int i, int j, int k)
  {
    super.g(world, i, j, k);
    world.d(i, j, k, (int)(world.K().g() / 2L) % 16);
    world.a(i, j, k, this.cm, 0);
  }

  public int r_()
  {
    return 2;
  }

  public int a(int i, int metaData)
  {
    return metaData + (this.effect ? 240 : 112);
  }

  public void b(yc world, int x, int y, int z, Random random)
  {
    int realMetaData = world.h(x, y, z);
    int metaData = (int)(world.K().g() / 2L + realMetaData) % 16;

    if (metaData >= 15)
    {
      if (this.effect)
      {
        if (random.nextInt(2) == 0)
        {
          world.d(x, y, z, hasNoEffectId, realMetaData);
        }

      }
      else if (random.nextInt(4) == 0)
      {
        world.d(x, y, z, hasEffectId, realMetaData);
      }

    }
    else if ((metaData == 8) && (this.effect))
    {
      BSP.println("Effect Time!");

      List list = world.b(null, aoe.a(x - 10, y - 10, z - 10, x + 11, y + 11, z + 11));

      int size = list.size();

      for (int i = 0; i < size; i++)
      {
        Object tmpEntity = list.get(i);

        BSP.println(tmpEntity.getClass().getName());

        if ((tmpEntity instanceof qx))
        {
          ur[] playerArmor = ((qx)tmpEntity).bJ.b;

          if ((playerArmor[3] != null) && (playerArmor[2] != null) && (playerArmor[1] != null) && (playerArmor[0] != null) && (playerArmor[3].c == BrainStone.brainStoneHelmet().cj) && (playerArmor[2].c == BrainStone.brainStonePlate().cj) && (playerArmor[1].c == BrainStone.brainStoneLeggings().cj) && (playerArmor[0].c == BrainStone.brainStoneBoots().cj))
          {
            BSP.println("Player waers armor! No effect!");

            continue;
          }
        }

        if ((tmpEntity instanceof md))
        {
          md entity = (md)tmpEntity;

          double radius = ke.a(random, 2.0D, 10.0D);

          if (entity.f(x + 0.5D, y + 0.5D, z + 0.5D) <= radius)
          {
            int taskRand = random.nextInt(10);

            if ((taskRand >= 0) && (taskRand < 6))
            {
              BSP.println("Potion Effect");

              entity.d(new lm(getRandomPotion(random), random.nextInt(5980) + 20, random.nextInt(4)));
            }
            else if ((taskRand >= 6) && (taskRand < 10))
            {
              BSP.println("Kick");

              double x1 = ke.a(random, -1.5D, 1.5D);
              double y1 = ke.a(random, 0.0D, 3.0D);
              double z1 = ke.a(random, -1.5D, 1.5D);

              if ((tmpEntity instanceof qx))
              {
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

    world.a(x, y, z, this.cm, r_());
    BrainStonePacketHandler.sendReRenderBlockAtPacket(x, y, z, world);
  }

  public int a(int i, Random random, int j)
  {
    return hasNoEffectId;
  }

  public boolean c()
  {
    return true;
  }

  public boolean b()
  {
    return false;
  }

  public int d()
  {
    return 56;
  }

  public ur getPickBlock(aoh target, yc world, int x, int y, int z)
  {
    return new ur(BrainStone.pulsatingBrainStone(), 1);
  }

  protected boolean s_()
  {
    return false;
  }

  private int getRandomPotion(Random random)
  {
    ll potion;
    do {
      potion = ll.a[random.nextInt(32)];
    }
    while (potion == null);

    return potion.H;
  }
}