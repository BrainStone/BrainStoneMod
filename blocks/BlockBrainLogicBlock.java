package mods.brainstone.blocks;

import aae;
import ahz;
import ajp;
import any;
import aou;
import aqj;
import java.util.Random;
import kx;
import lx;
import ly;
import mods.brainstone.BrainStone;
import mods.brainstone.handlers.BrainStonePacketHandler;
import mods.brainstone.templates.BlockBrainStoneContainerBase;
import mods.brainstone.tileentities.TileEntityBlockBrainLogicBlock;
import ng;
import sk;
import uy;
import wg;
import zv;

public class BlockBrainLogicBlock extends BlockBrainStoneContainerBase
{
  public static lx[] textures;

  public BlockBrainLogicBlock(int i)
  {
    super(BrainStone.getId(i), ahz.e);

    c(3.0F);
    b(1.0F);
    c("brainLogicBlock");
    a(uy.d);

    b(true);

    this.cN = 0.0F;
  }

  public void a(zv world, int i, int j, int k, int par5, int par6)
  {
    super.a(world, i, j, k, par5, par6);
    world.f(i, j, k, this.cz);
    world.f(i - 1, j, k, this.cz);
    world.f(i + 1, j, k, this.cz);
    world.f(i, j - 1, k, this.cz);
    world.f(i, j + 1, k, this.cz);
    world.f(i, j, k - 1, this.cz);
    world.f(i, j, k + 1, this.cz);
  }

  public boolean f()
  {
    return true;
  }

  private byte checkState(zv world, int i, int j, int k, byte byte0)
  {
    switch (byte0) {
    case 3:
      i++;
      break;
    case 2:
      i--;
      break;
    case 1:
      k++;
      break;
    case 0:
      k--;
    }

    int l = world.a(i, j, k);

    if (l == 0) {
      return -1;
    }
    byte byte1 = -1;
    aou block;
    if ((block = aou.r[l]).f()) {
      if ((block instanceof any)) {
        byte1 = (byte)(world.h(i, j, k) <= 0 ? 0 : 1);
      } else {
        byte0 = (byte)(byte0 + 2);
        byte1 = (byte)(block.b(world, i, j, k, byte0) + block.c(world, i, j, k, byte0) == 0 ? 0 : 1);
      }

    }
    else if (l(l)) {
      byte1 = (byte)(world.C(i, j, k) ? 1 : 0);
    }

    return byte1;
  }

  public aqj b(zv world)
  {
    return new TileEntityBlockBrainLogicBlock();
  }

  public void a(ly IconReg)
  {
    textures = new lx[] { IconReg.a("brainstone:brainLogicBlockNotConnected"), IconReg.a("brainstone:brainLogicBlockOff"), IconReg.a("brainstone:brainLogicBlockOn"), IconReg.a("furnace_side"), IconReg.a("furnace_top"), IconReg.a("brainstone:brainLogicBlockNotConnectedA"), IconReg.a("brainstone:brainLogicBlockOffC"), IconReg.a("brainstone:brainLogicBlockOnQ"), IconReg.a("brainstone:brainLogicBlockOnB") };
  }

  public lx b_(aae iblockaccess, int i, int j, int k, int l)
  {
    if (l < 2) {
      return textures[4];
    }
    TileEntityBlockBrainLogicBlock tileentityblockbrainlogicblock = (TileEntityBlockBrainLogicBlock)iblockaccess.r(i, j, k);

    if (tileentityblockbrainlogicblock == null) {
      return textures[4];
    }
    return textures[tileentityblockbrainlogicblock.getPinStateBasedTextureIndex(tileentityblockbrainlogicblock.transformDirection(l - 2))];
  }

  public lx a(int i, int meta)
  {
    if (i >= 2) {
      return textures[(3 + i)];
    }
    return textures[4];
  }

  public int c(aae iblockaccess, int i, int j, int k, int l)
  {
    TileEntityBlockBrainLogicBlock tileentityblockbrainlogicblock = (TileEntityBlockBrainLogicBlock)iblockaccess.r(i, j, k);

    if ((tileentityblockbrainlogicblock != null) && (tileentityblockbrainlogicblock.getDirection() == transformDirection(l - 2)))
    {
      return tileentityblockbrainlogicblock.getOutput() ? 15 : 0;
    }
    return 0;
  }

  public int b(aae iblockaccess, int i, int j, int k, int l)
  {
    return c(iblockaccess, i, j, k, l);
  }

  public boolean a(zv world, int i, int j, int k, sk entityplayer, int unknown, float px, float py, float pz)
  {
    TileEntityBlockBrainLogicBlock tileentityblockbrainlogicblock = (TileEntityBlockBrainLogicBlock)world.r(i, j, k);

    if (tileentityblockbrainlogicblock == null) {
      return false;
    }
    entityplayer.openGui(BrainStone.instance, 2, world, i, j, k);
    world.f(i, j, k, this.cz);
    return true;
  }

  public void a(zv world, int i, int j, int k)
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

  public void a(zv par1World, int par2, int par3, int par4, ng par5EntityLiving, wg par6ItemStack)
  {
    ((TileEntityBlockBrainLogicBlock)par1World.r(par2, par3, par4)).setDirection((byte)kx.c(par5EntityLiving.A * 4.0F / 360.0F + 0.5D) & 0x3);
  }

  public int a(zv par1World)
  {
    return 2;
  }

  private int transformDirection(int i)
  {
    switch (i) {
    case 0:
      return 2;
    case 1:
      return 0;
    case 2:
      return 1;
    case 3:
      return 3;
    }

    return 0;
  }

  public void a(zv world, int i, int j, int k, Random random)
  {
    super.a(world, i, j, k, random);
    TileEntityBlockBrainLogicBlock tileentityblockbrainlogicblock = (TileEntityBlockBrainLogicBlock)world.r(i, j, k);

    if (tileentityblockbrainlogicblock != null) {
      tileentityblockbrainlogicblock.doTASKS();

      if (tileentityblockbrainlogicblock.shallDoUpdate(world.L().g()))
      {
        byte[] abyte0 = { -1, -1, -1 };

        for (byte byte0 = 1; byte0 < 4; byte0 = (byte)(byte0 + 1)) {
          abyte0[(byte0 - 1)] = checkState(world, i, j, k, tileentityblockbrainlogicblock.reverseTransformDirection(byte0));
        }

        tileentityblockbrainlogicblock.setPinState(abyte0);
        BrainStonePacketHandler.sendReRenderBlockAtPacket(i, j, k, world);

        world.d(i, j, k, this.cz);
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
  }
}