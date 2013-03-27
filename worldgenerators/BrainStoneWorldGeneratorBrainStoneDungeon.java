package mods.brainstone.worldgenerators;

import add;
import ahz;
import aou;
import aps;
import java.util.Random;
import kx;
import mods.brainstone.BrainStone;
import mods.brainstone.templates.BSP;
import we;
import wg;
import yv;
import zv;

public class BrainStoneWorldGeneratorBrainStoneDungeon extends add
{
  private int x;
  private int y;
  private int z;
  private zv world;
  private Random random;
  private final int[] Options;

  public BrainStoneWorldGeneratorBrainStoneDungeon()
  {
    this.Options = new int[] { 2 };
  }

  private boolean canPlaceSecretRoomHere()
  {
    int height = this.Options[0] * 8 + 3;

    for (int i = 3; i < 10; i++) {
      for (int j = -1; j < 6; j++) {
        for (int k = height + 1; k > height - 8; k--) {
          if (!isSolid(this.x + i, this.y - k, this.z + j))
            return false;
        }
      }
    }
    return true;
  }

  private boolean canPlaceShackHere()
  {
    for (int i = 0; i < 6; i++) {
      for (int j = 0; j < 5; j++) {
        if (!isSolid(this.x + i, this.y - 1, this.z + j))
          return false;
      }
    }
    for (i = 0; i < 4; i++) {
      if (!isSolid(this.x + i, this.y - 1, this.z + 5))
        return false;
    }
    for (i = 0; i < 6; i++) {
      for (int j = 0; j < 5; j++) {
        for (int k = 0; k < 4; k++) {
          if (!isReplaceable(this.x + i, this.y + k, this.z + j))
            return false;
        }
      }
    }
    for (i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        if (!isReplaceable(this.x + i, this.y + j, this.z + 5))
          return false;
      }
    }
    if ((!isReplaceable(this.x + 4, this.y + 2, this.z + 5)) || (!isReplaceable(this.x + 5, this.y + 2, this.z + 5)))
    {
      return false;
    }
    for (i = -1; i < 5; i++) {
      if (!isReplaceable(this.x + i, this.y + 2, this.z + 6))
        return false;
    }
    for (i = -1; i < 6; i++) {
      if (!isReplaceable(this.x - 1, this.y + 2, this.z + i))
        return false;
    }
    for (i = 0; i < 7; i++) {
      if (!isReplaceable(this.x + i, this.y + 2, this.z - 1))
        return false;
    }
    for (i = 0; i < 6; i++) {
      if (!isReplaceable(this.x + 6, this.y + 2, this.z + i))
        return false;
    }
    for (i = 1; i < 4; i++) {
      for (int j = 1; j < 5; j++) {
        if (!isReplaceable(this.x + j, this.y + 4, this.z + i))
          return false;
      }
    }
    if ((!isReplaceable(this.x + 1, this.y + 4, this.z + 4)) || (!isReplaceable(this.x + 2, this.y + 4, this.z + 4)))
    {
      return false;
    }
    if ((!isReplaceable(this.x + 2, this.y + 5, this.z + 2)) || (!isReplaceable(this.x + 3, this.y + 5, this.z + 2)))
    {
      return false;
    }
    return true;
  }

  private boolean canPlaceStairsHere()
  {
    int height = this.Options[0] * 8 + 5;

    for (int i = -1; i < 6; i++) {
      for (int j = -1; j < 6; j++) {
        for (int k = 2; k < height; k++) {
          if (!isSolid(this.x + i, this.y - k, this.z + j))
            return false;
        }
      }
    }
    return true;
  }

  private boolean canPlaceStructHere(int structure, Object[] options) {
    switch (structure) {
    case 0:
      return canPlaceShackHere();
    case 1:
      return (canPlaceStairsHere()) && (canPlaceSecretRoomHere());
    }

    return false;
  }

  public boolean a(zv world, Random random, int x, int y, int z)
  {
    this.world = world;
    this.random = random;
    this.x = x;
    this.y = world.f(x, z);
    this.z = z;

    BSP.print("Trying at ");
    BSP.print(Integer.valueOf(x));
    BSP.print(", ");
    BSP.print(Integer.valueOf(this.y));
    BSP.print(", ");
    BSP.print(Integer.valueOf(z));
    BSP.println("!");

    int counter = 0;
    int direction = 0;
    int directionCounter = 0;
    int maxDirection = 1;
    while (true)
    {
      if (directionCounter >= maxDirection) {
        directionCounter = 0;
        maxDirection += direction % 2;
        direction = (direction + 1) % 4;
      }

      this.y = world.f(this.x, this.z);

      if (canPlaceStructHere(0, new Object[0])) {
        this.Options[0] = 3;

        if (canPlaceStructHere(1, new Object[0]))
        {
          break;
        }
        this.Options[0] = 2;

        if (canPlaceStructHere(1, new Object[0]))
        {
          break;
        }
        this.Options[0] = 4;

        if (canPlaceStructHere(1, new Object[0]))
        {
          break;
        }
      }
      switch (direction) {
      case 0:
        this.x += 1;
        break;
      case 1:
        this.z += 1;
        break;
      case 2:
        this.x -= 1;
        break;
      case 3:
        this.z -= 1;
      }

      if (counter >= 10000) {
        BSP.println("Failed");

        return false;
      }

      counter++;
      directionCounter++;
    }

    generateShack();

    generateStairs();

    generateSecretRoom();

    BSP.print("Placed at ");
    BSP.print(Integer.valueOf(this.x));
    BSP.print(", ");
    BSP.print(Integer.valueOf(this.y));
    BSP.print(", ");
    BSP.print(Integer.valueOf(this.z));
    BSP.println("!");

    return true;
  }

  private void generateSecretRoom()
  {
    int height = this.Options[0] * 8 + 3;

    for (int i = 4; i < 9; i++) {
      for (int j = 0; j < 5; j++) {
        for (int k = height; k > height - 7; k--) {
          setBlock(this.x + i, this.y - k, this.z + j, (i == 4) || (i == 8) || (j == 0) || (j == 4) || (k == height) || (k == height - 6) ? 4 : 0);
        }

      }

    }

    setBlock(this.x + 6, this.y - height + 6, this.z + 2, BrainStone.brainStone().cz);

    setBlock(this.x + 6, this.y - height, this.z + 2, BrainStone.brainStone().cz);

    setBlock(this.x + 7, this.y - height - 2, this.z + 2, BrainStone.pulsatingBrainStone().cz);

    height--;

    setBlock(this.x + 6, this.y - height, this.z + 1, 54);
    aps chest = (aps)this.world.r(this.x + 6, this.y - height, this.z + 1);

    int rand1 = this.random.nextInt(9) + 2;

    for (i = 0; i < rand1; i++) {
      chest.a(this.random.nextInt(chest.j_()), getLoot(1));
    }

    setBlock(this.x + 7, this.y - height, this.z + 1, 54);
    chest = (aps)this.world.r(this.x + 7, this.y - height, this.z + 1);

    rand1 = this.random.nextInt(9) + 2;
    int rand2 = this.random.nextInt(3);

    for (i = 0; i < rand1; i++) {
      chest.a(this.random.nextInt(chest.j_()), getLoot(rand2));
    }

    setBlock(this.x + 6, this.y - height, this.z + 3, 54);
    chest = (aps)this.world.r(this.x + 6, this.y - height, this.z + 3);

    rand1 = this.random.nextInt(9) + 2;
    rand2 = this.random.nextInt(3);

    for (i = 0; i < rand1; i++) {
      chest.a(this.random.nextInt(chest.j_()), getLoot(rand2));
    }

    setBlock(this.x + 7, this.y - height, this.z + 3, 54);
    chest = (aps)this.world.r(this.x + 7, this.y - height, this.z + 3);

    rand1 = this.random.nextInt(9) + 2;

    for (i = 0; i < rand1; i++)
      chest.a(this.random.nextInt(chest.j_()), getLoot(1));
  }

  private void generateShack()
  {
    for (int j = 0; j < 2; j++) {
      for (int i = 0; i < 6; i++) {
        setBlock(this.x + i, this.y + j, this.z, 5);
      }

      for (i = 0; i < 6; i++) {
        setBlock(this.x, this.y + j, this.z + i, 5);
      }

      for (i = 0; i < 4; i++) {
        setBlock(this.x + i, this.y + j, this.z + 5, 5);
      }

      for (i = 0; i < 5; i++) {
        setBlock(this.x + 5, this.y + j, this.z + i, 5);
      }

      for (i = 3; i < 6; i++) {
        setBlock(this.x + i, this.y + j, this.z + 4, 5);
      }

    }

    setBlock(this.x, this.y, this.z + 2, 0);
    setBlock(this.x, this.y + 1, this.z + 2, 0);
    setBlock(this.x + 2, this.y + 1, this.z, 0);
    setBlock(this.x + 5, this.y + 1, this.z + 2, 0);

    for (int i = 0; i < 6; i++) {
      for (j = 0; j < 6; j++) {
        setBlock(this.x + i, this.y + 2, this.z + j, 5);
      }

    }

    setBlockAndMetadata(this.x + 4, this.y + 2, this.z + 5, 53, 1);
    setBlockAndMetadata(this.x + 5, this.y + 2, this.z + 5, 53, 3);

    for (i = -1; i < 5; i++) {
      setBlockAndMetadata(this.x + i, this.y + 2, this.z + 6, 53, 3);
    }

    for (i = -1; i < 6; i++) {
      setBlockAndMetadata(this.x - 1, this.y + 2, this.z + i, 53, 0);
    }

    for (i = 0; i < 7; i++) {
      setBlockAndMetadata(this.x + i, this.y + 2, this.z - 1, 53, 2);
    }

    for (i = 0; i < 6; i++) {
      setBlockAndMetadata(this.x + 6, this.y + 2, this.z + i, 53, 1);
    }

    for (i = 1; i < 5; i++) {
      for (j = 1; j < 5; j++) {
        setBlock(this.x + i, this.y + 3, this.z + j, 5);
      }
    }

    setBlockAndMetadata(this.x + 3, this.y + 3, this.z + 4, 53, 1);
    setBlockAndMetadata(this.x + 4, this.y + 3, this.z + 4, 53, 3);

    for (i = 0; i < 4; i++) {
      setBlockAndMetadata(this.x + i, this.y + 3, this.z + 5, 53, 3);
    }

    for (i = 0; i < 5; i++) {
      setBlockAndMetadata(this.x, this.y + 3, this.z + i, 53, 0);
    }

    for (i = 1; i < 6; i++) {
      setBlockAndMetadata(this.x + i, this.y + 3, this.z, 53, 2);
    }

    for (i = 1; i < 5; i++) {
      setBlockAndMetadata(this.x + 5, this.y + 3, this.z + i, 53, 1);
    }

    int rand = this.random.nextInt(14);
    int chunkX = new int[] { 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 4, 4, 4 }[rand];

    int chunkZ = new int[] { 1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 1, 2, 3 }[rand];

    setBlock(this.x + chunkX, this.y + 3, this.z + chunkZ, 54);
    aps chest = (aps)this.world.r(this.x + chunkX, this.y + 3, this.z + chunkZ);

    rand = this.random.nextInt(9) + 2;

    for (i = 0; i < rand; i++) {
      chest.a(this.random.nextInt(chest.j_()), getLoot(0));
    }

    setBlock(this.x + 2, this.y + 4, this.z + 2, 5);
    setBlock(this.x + 3, this.y + 4, this.z + 2, 5);

    setBlockAndMetadata(this.x + 2, this.y + 4, this.z + 3, 53, 1);
    setBlockAndMetadata(this.x + 3, this.y + 4, this.z + 3, 53, 3);
    setBlockAndMetadata(this.x + 2, this.y + 4, this.z + 4, 53, 3);

    for (i = 2; i < 5; i++) {
      setBlockAndMetadata(this.x + 1, this.y + 4, this.z + i, 53, 0);
    }

    for (i = 1; i < 5; i++) {
      setBlockAndMetadata(this.x + i, this.y + 4, this.z + 1, 53, 2);
    }

    for (i = 2; i < 4; i++) {
      setBlockAndMetadata(this.x + 4, this.y + 4, this.z + i, 53, 1);
    }

    setBlock(this.x + 2, this.y + 5, this.z + 2, 126);
    setBlock(this.x + 3, this.y + 5, this.z + 2, 126);

    for (i = 1; i < 5; i++) {
      for (j = 1; j < 4; j++) {
        for (int k = 0; k < 2; k++) {
          setBlock(this.x + i, this.y + k, this.z + j, 0);
        }
      }
    }

    for (i = 1; i < 3; i++)
      for (j = 0; j < 2; j++)
        setBlock(this.x + i, this.y + j, this.z + 4, 0);
  }

  private void generateStairs()
  {
    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 5; j++) {
        setBlock(this.x + i, this.y - 2, this.z + j, 4);
      }
    }

    for (i = 1; i < 4; i++) {
      setBlock(this.x + 3, this.y - 2, this.z + i, 0);
    }

    setBlock(this.x + 3, this.y - 2, this.z + 2, 50);
    setBlockAndMetadata(this.x + 2, this.y - 2, this.z + 1, 67, 1);

    int[] tmpX = { 2, 3, 3, 3, 2, 1, 1, 1 };
    int[] tmpZ = { 1, 1, 2, 3, 3, 3, 2, 1 };

    for (int l = 0; l < this.Options[0]; l++) {
      int height = l * 8 + 3;

      for (i = 0; i < 5; i++) {
        for (int j = 0; j < 5; j++) {
          for (int k = 0; k < 8; k++) {
            setBlock(this.x + i, this.y - height - k, this.z + j, ((i == 1) || (i == 3) || (j == 1) || (j == 3)) && (i != 0) && (i != 4) && (j != 0) && (j != 4) ? 0 : 4);
          }

        }

      }

      for (i = 0; i < 8; i++) {
        setBlock(this.x + tmpX[i], this.y - height - i, this.z + tmpZ[i], 4);

        int tmp = (i + 1) % 8;
        int tmp2 = i / 2;
        setBlockAndMetadata(this.x + tmpX[tmp], this.y - height - i, this.z + tmpZ[tmp], 67, tmp2 == 2 ? 2 : tmp2 == 1 ? 0 : tmp2 == 0 ? 3 : 1);

        tmp = (i + 7) % 8;
        tmp2 = (i + 1) / 2 % 4;
        setBlockAndMetadata(this.x + tmpX[tmp], this.y - height - i, this.z + tmpZ[tmp], 67, tmp2 == 2 ? 6 : tmp2 == 1 ? 4 : tmp2 == 0 ? 7 : 5);

        if (i % 2 == 1) {
          tmp = (i + 3) % 8;
          tmp2 = i / 2;
          setBlockAndMetadata(this.x + tmpX[tmp], this.y - height - i, this.z + tmpZ[tmp], 50, tmp2 == 2 ? 4 : tmp2 == 1 ? 2 : tmp2 == 0 ? 3 : 1);
        }

      }

    }

    int height = this.Options[0] * 8 + 3;

    for (i = 0; i < 5; i++)
      for (int j = 0; j < 5; j++)
        setBlock(this.x + i, this.y - height, this.z + j, 4);
  }

  private wg getLoot(int lootId)
  {
    Object[][] loots = (Object[][])null;

    switch (lootId) {
    case 0:
      loots = new Object[][] { { new wg(BrainStone.brainStone()), Float.valueOf(0.1F), Integer.valueOf(1), Integer.valueOf(4), Integer.valueOf(5) }, { new wg(BrainStone.dirtyBrainStone()), Float.valueOf(1.0F), Integer.valueOf(1), Integer.valueOf(4), Integer.valueOf(5) }, { new wg(BrainStone.brainStoneDust()), Float.valueOf(2.0F), Integer.valueOf(1), Integer.valueOf(6), Integer.valueOf(7) }, { new wg(BrainStone.pulsatingBrainStone()), Float.valueOf(0.05F), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(2) }, { new wg(we.o), Float.valueOf(0.75F), Integer.valueOf(1), Integer.valueOf(4), Integer.valueOf(5) }, { new wg(we.bI), Float.valueOf(0.5F), Integer.valueOf(1), Integer.valueOf(4), Integer.valueOf(5) } };

      break;
    case 1:
      loots = new Object[][] { { new wg(BrainStone.brainStoneDust()), Float.valueOf(0.1F), Integer.valueOf(1), Integer.valueOf(5), Integer.valueOf(6) }, { new wg(we.aD), Float.valueOf(1.0F), Integer.valueOf(3), Integer.valueOf(5), Integer.valueOf(7) }, { new wg(aou.L), Float.valueOf(1.0F), Integer.valueOf(3), Integer.valueOf(2), Integer.valueOf(5) }, { new wg(we.aX, 1, 4), Float.valueOf(1.0F), Integer.valueOf(3), Integer.valueOf(5), Integer.valueOf(7) }, { new wg(aou.K), Float.valueOf(1.0F), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(4) }, { new wg(we.o), Float.valueOf(0.2F), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(2) }, { new wg(we.aX, 1, 3), Float.valueOf(1.0F), Integer.valueOf(3), Integer.valueOf(5), Integer.valueOf(7) }, { new wg(we.aB), Float.valueOf(0.2F), Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0) }, { new wg(we.au), Float.valueOf(0.1F), Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0) }, { new wg(we.au, 1, 1), Float.valueOf(0.01F), Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0) } };

      break;
    case 2:
      loots = new Object[][] { { new wg(aou.as), Float.valueOf(1.0F), Integer.valueOf(1), Integer.valueOf(19), Integer.valueOf(20) }, { new wg(aou.bh), Float.valueOf(1.0F), Integer.valueOf(1), Integer.valueOf(9), Integer.valueOf(10) }, { new wg(aou.bj), Float.valueOf(1.0F), Integer.valueOf(1), Integer.valueOf(9), Integer.valueOf(10) }, { new wg(aou.aX), Float.valueOf(1.0F), Integer.valueOf(1), Integer.valueOf(19), Integer.valueOf(20) }, { new wg(aou.bP), Float.valueOf(1.0F), Integer.valueOf(1), Integer.valueOf(9), Integer.valueOf(10) }, { new wg(aou.bO), Float.valueOf(0.01F), Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0) }, { new wg(we.bs), Float.valueOf(1.0F), Integer.valueOf(1), Integer.valueOf(9), Integer.valueOf(10) }, { new wg(we.aN), Float.valueOf(1.0F), Integer.valueOf(1), Integer.valueOf(9), Integer.valueOf(10) }, { new wg(we.aM), Float.valueOf(1.0F), Integer.valueOf(1), Integer.valueOf(9), Integer.valueOf(10) }, { new wg(we.bp), Float.valueOf(1.0F), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(2) }, { new wg(we.bo), Float.valueOf(1.0F), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(2) }, { new wg(BrainStone.brainStoneAxe(), 1, this.random.nextInt(5368)), Float.valueOf(0.1F), Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0) }, { new wg(BrainStone.brainStonePickaxe(), 1, this.random.nextInt(5368)), Float.valueOf(0.1F), Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0) }, { new wg(BrainStone.brainStoneShovel(), 1, this.random.nextInt(5368)), Float.valueOf(0.1F), Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0) }, { new wg(BrainStone.brainStoneHoe(), 1, this.random.nextInt(5368)), Float.valueOf(0.1F), Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0) }, { new wg(BrainStone.brainStoneSword(), 1, this.random.nextInt(5368)), Float.valueOf(0.1F), Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0) }, { new wg(BrainStone.brainStoneHelmet(), 1, this.random.nextInt(1824)), Float.valueOf(0.1F), Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0) }, { new wg(BrainStone.brainStonePlate(), 1, this.random.nextInt(1824)), Float.valueOf(0.1F), Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0) }, { new wg(BrainStone.brainStoneLeggings(), 1, this.random.nextInt(1824)), Float.valueOf(0.1F), Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0) }, { new wg(BrainStone.brainStoneBoots(), 1, this.random.nextInt(1824)), Float.valueOf(0.1F), Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0) }, { yv.a(this.random, new wg(BrainStone.brainStoneAxe(), 1, this.random.nextInt(5368)), 10), Float.valueOf(0.05F), Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0) }, { yv.a(this.random, new wg(BrainStone.brainStonePickaxe(), 1, this.random.nextInt(5368)), 10), Float.valueOf(0.05F), Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0) }, { yv.a(this.random, new wg(BrainStone.brainStoneShovel(), 1, this.random.nextInt(5368)), 10), Float.valueOf(0.05F), Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0) }, { yv.a(this.random, new wg(BrainStone.brainStoneSword(), 1, this.random.nextInt(5368)), 10), Float.valueOf(0.05F), Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0) }, { yv.a(this.random, new wg(BrainStone.brainStoneHelmet(), 1, this.random.nextInt(1824)), 10), Float.valueOf(0.05F), Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0) }, { yv.a(this.random, new wg(BrainStone.brainStonePlate(), 1, this.random.nextInt(1824)), 10), Float.valueOf(0.05F), Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0) }, { yv.a(this.random, new wg(BrainStone.brainStoneLeggings(), 1, this.random.nextInt(1824)), 10), Float.valueOf(0.05F), Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0) }, { yv.a(this.random, new wg(BrainStone.brainStoneBoots(), 1, this.random.nextInt(1824)), 10), Float.valueOf(0.05F), Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0) } };
    }

    if (loots == null) {
      return null;
    }
    float sum = 0.0F;

    wg loot = null;

    int size = loots.length;

    for (int i = 0; i < size; i++) {
      sum += ((Float)loots[i][1]).floatValue();
    }

    float rand = (float)kx.a(this.random, 0.0D, sum);

    for (i = 0; i < size; i++) {
      Object[] tmpLoot = loots[i];
      float tmpChance = ((Float)tmpLoot[1]).floatValue();

      if (rand < tmpChance) {
        loot = (wg)tmpLoot[0];
        loot.a = (((Integer)tmpLoot[2]).intValue() + this.random.nextInt(((Integer)tmpLoot[3]).intValue() + 1) + this.random.nextInt(((Integer)tmpLoot[4]).intValue() + 1));

        if (loot.c != aou.bO.cz) break;
        BSP.println("Dragon Egg!!!!"); break;
      }

      rand -= tmpChance;
    }

    return loot;
  }

  private boolean isReplaceable(int x, int y, int z) {
    return this.world.g(x, y, z).j();
  }

  private boolean isSolid(int x, int y, int z) {
    return this.world.g(x, y, z).a();
  }

  private void setBlock(int x, int y, int z, int blockId)
  {
    setBlockAndMetadata(x, y, z, blockId, 0);
  }

  private void setBlockAndMetadata(int x, int y, int z, int blockId, int metaData)
  {
    this.world.f(x, y, z, blockId, metaData, 2);
  }
}