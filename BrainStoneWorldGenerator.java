package net.braintonemod.src;

import aaj;
import abr;
import amj;
import cpw.mods.fml.common.IWorldGenerator;
import java.util.Random;
import xv;
import zp;

public class BrainStoneWorldGenerator
  implements IWorldGenerator
{
  private Random random;
  private int chunkX;
  private int chunkZ;
  private xv world;
  private zp chunkGenerator;
  private zp chunkProvider;

  public void generate(Random random, int chunkX, int chunkZ, xv world, zp chunkGenerator, zp chunkProvider)
  {
    this.random = random;
    this.chunkX = chunkX;
    this.chunkZ = chunkZ;
    this.world = world;
    this.chunkGenerator = chunkGenerator;
    this.chunkProvider = chunkProvider;

    switch (world.v.h)
    {
    case -1:
      break;
    case 0:
      genMinable(BrainStone.brainStoneOre().cm, 20, 1, 32);

      break;
    case 1:
    }
  }

  private void genMinable(int blockId, int size, int perChunk, int high, int low)
  {
    for (int i = 0; i < perChunk; i++)
    {
      int randPosX = this.random.nextInt(16) + this.chunkX;
      int randPosY = low + this.random.nextInt(high - low);
      int randPosZ = this.random.nextInt(16) + this.chunkZ;

      new abr(blockId, size).a(this.world, this.random, randPosX, randPosY, randPosZ);
    }
  }

  private void genMinable(int blockId, int size, int perChunk, int high)
  {
    genMinable(blockId, size, perChunk, high, 0);
  }
}