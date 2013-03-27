package mods.brainstone.worldgenerators;

import abn;
import ach;
import adp;
import aou;
import cpw.mods.fml.common.IWorldGenerator;
import java.util.Random;
import mods.brainstone.BrainStone;
import zv;

public class BrainStoneWorldGenerator
  implements IWorldGenerator
{
  private Random random;
  private int chunkX;
  private int chunkZ;
  private zv world;

  private void genBrainStoneDungeon()
  {
    if (this.random.nextInt(1000) == 0) {
      BrainStoneWorldGeneratorBrainStoneDungeon name = new BrainStoneWorldGeneratorBrainStoneDungeon();

      name.a(this.world, this.random, this.chunkX, 0, this.chunkZ);
    }
  }

  public void generate(Random random, int chunkX, int chunkZ, zv world, abn chunkGenerator, abn chunkProvider)
  {
    this.random = random;
    this.chunkX = (chunkX * 16);
    this.chunkZ = (chunkZ * 16);
    this.world = world;
    switch (world.t.h) {
    case -1:
      break;
    case 0:
      genMinable(BrainStone.brainStoneOre().cz, 20, 1, 32);

      genBrainStoneDungeon();

      break;
    case 1:
    }
  }

  private void genMinable(int blockId, int size, int perChunk, int high)
  {
    genMinable(blockId, size, perChunk, high, 0);
  }

  private void genMinable(int blockId, int size, int perChunk, int high, int low)
  {
    for (int i = 0; i < perChunk; i++) {
      int randPosX = this.random.nextInt(16) + this.chunkX;
      int randPosY = low + this.random.nextInt(high - low);
      int randPosZ = this.random.nextInt(16) + this.chunkZ;

      new adp(blockId, size).a(this.world, this.random, randPosX, randPosY, randPosZ);
    }
  }
}