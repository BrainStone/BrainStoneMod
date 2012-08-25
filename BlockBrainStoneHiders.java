public abstract class BlockBrainStoneHiders extends agy
{
  protected boolean renderGrass;

  public BlockBrainStoneHiders(int i, int j)
  {
    super(i, j, acn.p);
  }

  public BlockBrainStoneHiders(int i, int j, acn m)
  {
    super(i, j, m);
    this.renderGrass = false;
  }

  public boolean a()
  {
    return true;
  }

  public boolean b()
  {
    return true;
  }

  public boolean g()
  {
    return true;
  }

  public int d()
  {
    return mod_BrainStone.renderBrainStoneSensorsID;
  }

  public int c(ali par1IBlockAccess, int par2, int par3, int par4)
  {
    TileEntityBlockBrainStoneHiders tileentity = (TileEntityBlockBrainStoneHiders)par1IBlockAccess.b(par2, par3, par4);

    if (tileentity == null) {
      return 16777215;
    }
    aan itemstack = tileentity.k_(0);

    if (itemstack == null) {
      return 16777215;
    }
    int i = itemstack.c;

    if ((i > 255) || (i <= 0) || ((itemstack.c == pb.u.bO) && (!this.renderGrass))) {
      return 16777215;
    }
    pb tmp = pb.m[i];

    if (tmp == null) {
      return 16777215;
    }
    this.renderGrass = false;
    return tmp.c(par1IBlockAccess, par2, par3, par4);
  }

  public boolean isGrass(ali par1IBlockAccess, int par2, int par3, int par4)
  {
    TileEntityBlockBrainStoneHiders tileentity = (TileEntityBlockBrainStoneHiders)par1IBlockAccess.b(par2, par3, par4);

    if (tileentity == null) {
      return false;
    }
    aan itemstack = tileentity.k_(0);

    if (itemstack == null) {
      return false;
    }
    return this.renderGrass = itemstack.c == pb.u.bO ? 1 : 0;
  }
}