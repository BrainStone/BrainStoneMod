import java.util.Random;

public class BlockBrainLogicBlock extends agy
{
  public BlockBrainLogicBlock(int i, int j)
  {
    super(i, j, acn.p);

    this.cc = 0.0F;
    a(true);
  }

  public kw u_()
  {
    return new TileEntityBlockBrainLogicBlock();
  }

  public void a(xd par1World, int par2, int par3, int par4)
  {
    super.a(par1World, par2, par3, par4);
    par1World.j(par2, par3, par4, this.bO);
    par1World.j(par2 - 1, par3, par4, this.bO);
    par1World.j(par2 + 1, par3, par4, this.bO);
    par1World.j(par2, par3 - 1, par4, this.bO);
    par1World.j(par2, par3 + 1, par4, this.bO);
    par1World.j(par2, par3, par4 - 1, this.bO);
    par1World.j(par2, par3, par4 + 1, this.bO);

    par1World.a(par2, par3, par4, this.bO, e());
  }

  public void b_(xd par1World, int par2, int par3, int par4)
  {
    super.b_(par1World, par2, par3, par4);
    par1World.j(par2, par3, par4, this.bO);
    par1World.j(par2 - 1, par3, par4, this.bO);
    par1World.j(par2 + 1, par3, par4, this.bO);
    par1World.j(par2, par3 - 1, par4, this.bO);
    par1World.j(par2, par3 + 1, par4, this.bO);
    par1World.j(par2, par3, par4 - 1, this.bO);
    par1World.j(par2, par3, par4 + 1, this.bO);
  }

  public void a(xd world, int i, int j, int k, Random random)
  {
    super.a(world, i, j, k, random);

    TileEntityBlockBrainLogicBlock tileentity = (TileEntityBlockBrainLogicBlock)world.b(i, j, k);

    if (tileentity != null)
    {
      tileentity.doTASKS();

      if (tileentity.shallDoUpdate(world.B().f()))
      {
        byte[] tmp = { -1, -1, -1 };

        for (byte dir = 1; dir < 4; dir = (byte)(dir + 1))
        {
          tmp[(dir - 1)] = checkState(world, i, j, k, tileentity.reverseTransformDirection(dir));
        }

        tileentity.setPinState(tmp);

        world.h(i, j, k, this.bO);
        world.j(i, j, k, this.bO);
        world.j(i - 1, j, k, this.bO);
        world.j(i + 1, j, k, this.bO);
        world.j(i, j - 1, k, this.bO);
        world.j(i, j + 1, k, this.bO);
        world.j(i, j, k - 1, this.bO);
        world.j(i, j, k + 1, this.bO);

        world.a(i, j, k, this.bO, e());
      }
    }
  }

  public int a_(int par1)
  {
    par1 -= 2;

    switch (par1) {
    case 0:
      return mod_BrainStone.brainLogicBlockOnTexture;
    case 1:
      return mod_BrainStone.brainLogicBlockOnTexture;
    case 2:
      return mod_BrainStone.brainLogicBlockOffTexture;
    case 3:
      return mod_BrainStone.brainLogicBlockOffTexture;
    }

    return this.bN;
  }

  public int d(ali par1IBlockAccess, int par2, int par3, int par4, int par5)
  {
    if (par5 < 2) {
      return this.bN;
    }
    TileEntityBlockBrainLogicBlock tileentity = (TileEntityBlockBrainLogicBlock)par1IBlockAccess.b(par2, par3, par4);

    if (tileentity == null) {
      return this.bN;
    }
    return tileentity.getPinStateBasedTexture(tileentity.transformDirection(par5 - 2));
  }

  public void a(xd par1World, int par2, int par3, int par4, acq par5EntityLiving)
  {
    TileEntityBlockBrainLogicBlock tileentity = (TileEntityBlockBrainLogicBlock)par1World.b(par2, par3, par4);

    if (tileentity != null)
      tileentity.setDirection(gk.c(par5EntityLiving.u * 4.0F / 360.0F + 0.5D) & 0x3);
  }

  public boolean b(ali par1IBlockAccess, int par2, int par3, int par4, int side)
  {
    TileEntityBlockBrainLogicBlock tileentity = (TileEntityBlockBrainLogicBlock)par1IBlockAccess.b(par2, par3, par4);

    if ((tileentity != null) && (tileentity.getDirection() == transformDirection(side - 2))) {
      return tileentity.getOutput();
    }
    return false;
  }

  public boolean e(xd par1World, int par2, int par3, int par4, int side)
  {
    return b(par1World, par2, par3, par4, side);
  }

  public boolean g()
  {
    return true;
  }

  public int e()
  {
    return 2;
  }

  public boolean b(xd par1World, int par2, int par3, int par4, yw par5EntityPlayer)
  {
    if (par1World.F)
    {
      return true;
    }

    TileEntityBlockBrainLogicBlock tileentity = (TileEntityBlockBrainLogicBlock)par1World.b(par2, par3, par4);

    if (tileentity == null) {
      return false;
    }
    ModLoader.openGUI(par5EntityPlayer, new GuiBrainLogicBlock(tileentity));

    par1World.j(par2, par3, par4, this.bO);

    return true;
  }

  private byte checkState(xd world, int par2, int par3, int par4, byte direction)
  {
    switch (direction)
    {
    case 3:
      par2++;
      break;
    case 2:
      par2--;
      break;
    case 1:
      par4++;
      break;
    case 0:
      par4--;
    }

    int tmp = world.a(par2, par3, par4);

    if (tmp == 0) {
      return -1;
    }
    byte state = -1;
    pb block;
    if ((block = pb.m[tmp]).g())
    {
      if ((block instanceof ahi))
      {
        state = (byte)(world.e(par2, par3, par4) > 0 ? 1 : 0);
      }
      else
      {
        direction = (byte)(direction + 2);
        state = (byte)((block.b(world, par2, par3, par4, direction)) || (block.e(world, par2, par3, par4, direction)) ? 1 : 0);
      }

    }
    else if (g(tmp))
    {
      state = (byte)(world.x(par2, par3, par4) ? 1 : 0);
    }

    return state;
  }

  private byte switchDirection(byte direction)
  {
    switch (direction) {
    case 0:
      return 1;
    case 1:
      return 0;
    case 2:
      return 3;
    case 3:
      return 2;
    }

    return 0;
  }

  private int transformDirection(int direction)
  {
    switch (direction) {
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