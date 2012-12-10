package net.braintonemod.src;

import agb;
import ahq;
import aju;
import alo;
import amj;
import anq;
import java.util.Random;
import ke;
import md;
import qx;
import th;
import xv;
import yf;

public class BlockBrainLogicBlock extends aju
{
  protected BlockBrainLogicBlock(int i)
  {
    super(BrainStone.getId(i), agb.q);

    c(3.0F);
    b(1.0F);
    b("brainLogicBlock");
    r();
    a(th.d);

    b(true);

    this.cA = 0.0F;
  }

  public String getTextureFile()
  {
    return "/BrainStoneTextures/textures.png";
  }

  public anq a(xv world)
  {
    return new TileEntityBlockBrainLogicBlock();
  }

  public void g(xv world, int i, int j, int k)
  {
    super.g(world, i, j, k);
    world.h(i, j, k, this.cm);
    world.h(i - 1, j, k, this.cm);
    world.h(i + 1, j, k, this.cm);
    world.h(i, j - 1, k, this.cm);
    world.h(i, j + 1, k, this.cm);
    world.h(i, j, k - 1, this.cm);
    world.h(i, j, k + 1, this.cm);
    world.a(i, j, k, this.cm, r_());
  }

  public void a(xv world, int i, int j, int k, int par5, int par6)
  {
    super.a(world, i, j, k, par5, par6);
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
    super.b(world, i, j, k, random);
    TileEntityBlockBrainLogicBlock tileentityblockbrainlogicblock = (TileEntityBlockBrainLogicBlock)world.q(i, j, k);

    if (tileentityblockbrainlogicblock != null)
    {
      tileentityblockbrainlogicblock.doTASKS();

      if (tileentityblockbrainlogicblock.shallDoUpdate(world.K().g()))
      {
        byte[] abyte0 = { -1, -1, -1 };

        for (byte byte0 = 1; byte0 < 4; byte0 = (byte)(byte0 + 1))
        {
          abyte0[(byte0 - 1)] = checkState(world, i, j, k, tileentityblockbrainlogicblock.reverseTransformDirection(byte0));
        }

        tileentityblockbrainlogicblock.setPinState(abyte0);
        BrainStonePacketHandler.sendReRenderBlockAtPacket(i, j, k, world);
        world.f(i, j, k, this.cm);
        world.h(i, j, k, this.cm);
        world.h(i - 1, j, k, this.cm);
        world.h(i + 1, j, k, this.cm);
        world.h(i, j - 1, k, this.cm);
        world.h(i, j + 1, k, this.cm);
        world.h(i, j, k - 1, this.cm);
        world.h(i, j, k + 1, this.cm);
        world.a(i, j, k, this.cm, r_());
      }
    }
  }

  public int a(int i)
  {
    i -= 2; switch (i)
    {
    case 0:
      return 19;
    case 1:
      return 35;
    case 2:
      return 19;
    case 3:
      return 35;
    }

    return 3;
  }

  public int d(yf iblockaccess, int i, int j, int k, int l)
  {
    if (l < 2)
    {
      return 3;
    }

    TileEntityBlockBrainLogicBlock tileentityblockbrainlogicblock = (TileEntityBlockBrainLogicBlock)iblockaccess.q(i, j, k);

    if (tileentityblockbrainlogicblock == null)
    {
      return 3;
    }

    return tileentityblockbrainlogicblock.getPinStateBasedTexture(tileentityblockbrainlogicblock.transformDirection(l - 2));
  }

  public void a(xv world, int i, int j, int k, md entityliving)
  {
    TileEntityBlockBrainLogicBlock tileentityblockbrainlogicblock = (TileEntityBlockBrainLogicBlock)world.q(i, j, k);

    if (tileentityblockbrainlogicblock != null)
    {
      tileentityblockbrainlogicblock.setDirection(ke.c(entityliving.z * 4.0F / 360.0F + 0.5D) & 0x3);
    }
  }

  public boolean c(yf iblockaccess, int i, int j, int k, int l)
  {
    TileEntityBlockBrainLogicBlock tileentityblockbrainlogicblock = (TileEntityBlockBrainLogicBlock)iblockaccess.q(i, j, k);

    if ((tileentityblockbrainlogicblock != null) && (tileentityblockbrainlogicblock.getDirection() == transformDirection(l - 2)))
    {
      return tileentityblockbrainlogicblock.getOutput();
    }

    return false;
  }

  public boolean b(yf iblockaccess, int i, int j, int k, int l)
  {
    return c(iblockaccess, i, j, k, l);
  }

  public boolean i()
  {
    return true;
  }

  public int r_()
  {
    return 2;
  }

  public boolean a(xv world, int i, int j, int k, qx entityplayer, int unknown, float px, float py, float pz)
  {
    TileEntityBlockBrainLogicBlock tileentityblockbrainlogicblock = (TileEntityBlockBrainLogicBlock)world.q(i, j, k);

    if (tileentityblockbrainlogicblock == null)
    {
      return false;
    }

    entityplayer.openGui(BrainStone.instance, 2, world, i, j, k);
    world.h(i, j, k, this.cm);
    return true;
  }

  private byte checkState(xv world, int i, int j, int k, byte byte0)
  {
    switch (byte0)
    {
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

    if (l == 0)
    {
      return -1;
    }

    byte byte1 = -1;
    amj block;
    if ((block = amj.p[l]).i())
    {
      if ((block instanceof alo))
      {
        byte1 = (byte)(world.h(i, j, k) <= 0 ? 0 : 1);
      }
      else
      {
        byte0 = (byte)(byte0 + 2);
        byte1 = (byte)((!block.b(world, i, j, k, byte0)) && (!block.c(world, i, j, k, byte0)) ? 0 : 1);
      }
    }
    else if (i(l))
    {
      byte1 = (byte)(world.B(i, j, k) ? 1 : 0);
    }

    return byte1;
  }

  private int transformDirection(int i)
  {
    switch (i)
    {
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
}