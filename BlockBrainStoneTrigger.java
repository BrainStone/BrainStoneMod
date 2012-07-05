import java.io.PrintStream;
import java.util.List;
import java.util.Random;

public class BlockBrainStoneTrigger extends agy
{
  public BlockBrainStoneTrigger(int i, int j)
  {
    super(i, j, acn.p);

    this.cc = -0.2F;
  }

  public kw u_()
  {
    return new TileEntityBlockBrainStoneTrigger();
  }

  public void a(xd par1World, int par2, int par3, int par4)
  {
    par1World.a(par2, par3, par4, u_());
    par1World.j(par2, par3, par4, this.bO);
    par1World.j(par2 - 1, par3, par4, this.bO);
    par1World.j(par2 + 1, par3, par4, this.bO);
    par1World.j(par2, par3 - 1, par4, this.bO);
    par1World.j(par2, par3 + 1, par4, this.bO);
    par1World.j(par2, par3, par4 - 1, this.bO);
    par1World.j(par2, par3, par4 + 1, this.bO);

    par1World.a(par2, par3, par4, this.bO, 0);
  }

  public void b_(xd par1World, int par2, int par3, int par4)
  {
    par1World.q(par2, par3, par4);
    par1World.j(par2, par3, par4, this.bO);
    par1World.j(par2 - 1, par3, par4, this.bO);
    par1World.j(par2 + 1, par3, par4, this.bO);
    par1World.j(par2, par3 - 1, par4, this.bO);
    par1World.j(par2, par3 + 1, par4, this.bO);
    par1World.j(par2, par3, par4 - 1, this.bO);
    par1World.j(par2, par3, par4 + 1, this.bO);
  }

  public boolean b(xd par1World, int par2, int par3, int par4, yw par5EntityPlayer)
  {
    if (par1World.F)
    {
      return true;
    }

    TileEntityBlockBrainStoneTrigger tileentity = (TileEntityBlockBrainStoneTrigger)par1World.b(par2, par3, par4);

    if (tileentity != null)
    {
      ModLoader.openGUI(par5EntityPlayer, new GuiBrainStoneTrigger(par5EntityPlayer.ap, tileentity));

      par1World.h(par2, par3, par4, this.bO);
    }

    return true;
  }

  public void a(xd par1World, int par2, int par3, int par4, Random random)
  {
    TileEntityBlockBrainStoneTrigger tileentity = (TileEntityBlockBrainStoneTrigger)par1World.b(par2, par3, par4);

    if (tileentity != null) {
      tileentity.power = triggerCorrectMob(par1World, par2, par3, par4);
    }
    par1World.h(par2, par3, par4, this.bO);
    par1World.j(par2, par3, par4, this.bO);
    par1World.j(par2 - 1, par3, par4, this.bO);
    par1World.j(par2 + 1, par3, par4, this.bO);
    par1World.j(par2, par3 - 1, par4, this.bO);
    par1World.j(par2, par3 + 1, par4, this.bO);
    par1World.j(par2, par3, par4 - 1, this.bO);
    par1World.j(par2, par3, par4 + 1, this.bO);

    par1World.a(par2, par3, par4, this.bO, e());
  }

  public int d(ali par1IBlockAccess, int par2, int par3, int par4, int par5)
  {
    if (par5 == 1)
    {
      TileEntityBlockBrainStoneTrigger tileentity = (TileEntityBlockBrainStoneTrigger)par1IBlockAccess.b(par2, par3, par4);

      if (tileentity == null) {
        return this.bN;
      }
      int tmp = tileentity.getTextureId(par1IBlockAccess, par2, par3, par4);

      return tmp == -1 ? this.bN : tmp;
    }
    if (par5 == 0)
    {
      return this.bN;
    }

    return 45;
  }

  public int c(ali par1IBlockAccess, int par2, int par3, int par4)
  {
    TileEntityBlockBrainStoneTrigger tileentity = (TileEntityBlockBrainStoneTrigger)par1IBlockAccess.b(par2, par3, par4);

    if (tileentity == null) {
      return 16777215;
    }
    aan tmpItemStack = tileentity.k_(0);

    if (tmpItemStack == null) {
      return 16777215;
    }
    int i = tmpItemStack.c;

    if ((i > 255) || (i <= 0)) {
      return 16777215;
    }
    pb tmp = pb.m[i];

    return tmp.c(par1IBlockAccess, par2, par3, par4);
  }

  public int a_(int par1)
  {
    if ((par1 == 1) || (par1 == 0))
    {
      return this.bN;
    }

    return 45;
  }

  public boolean g()
  {
    return true;
  }

  public boolean b(ali par1IBlockAccess, int par2, int par3, int par4, int par5)
  {
    TileEntityBlockBrainStoneTrigger tileentity = (TileEntityBlockBrainStoneTrigger)par1IBlockAccess.b(par2, par3, par4);

    return (tileentity != null) && (tileentity.power);
  }

  public boolean e(xd par1World, int par2, int par3, int par4, int par5)
  {
    return b(par1World, par2, par3, par4, par5);
  }

  public int e()
  {
    return 2;
  }

  private boolean triggerCorrectMob(xd par1World, int par2, int par3, int par4)
  {
    List list = par1World.b(null, wu.b(par2, par3 + 1, par4, par2 + 1, par3 + 2, par4 + 1));
    TileEntityBlockBrainStoneTrigger tileentity = (TileEntityBlockBrainStoneTrigger)par1World.b(par2, par3, par4);

    if (tileentity == null) {
      return false;
    }
    boolean flag = false;

    for (int i = 0; i < list.size(); i++)
    {
      nn tmp = (nn)list.get(i);

      if (tmp == null)
      {
        System.out.println("Fehler! Die Entity ist nicht vorhanden!");
      }
      else
      {
        if (tileentity.getMobTriggered(0))
          flag = (flag) || ((tmp instanceof nm)) || ((tmp instanceof ep)) || ((tmp instanceof va)) || ((tmp instanceof bt)) || ((tmp instanceof act)) || ((tmp instanceof fq)) || ((tmp instanceof ama)) || ((tmp instanceof av)) || ((tmp instanceof agg)) || ((tmp instanceof bz));
        if (tileentity.getMobTriggered(1))
          flag = (flag) || ((tmp instanceof bc)) || (((tmp instanceof aaa)) && (!(tmp instanceof yy)));
        if (tileentity.getMobTriggered(2))
          flag = (flag) || (((tmp instanceof yy)) && (!(tmp instanceof adg)) && (!(tmp instanceof ui)) && (!(tmp instanceof aic)) && (!(tmp instanceof alt)));
        if (tileentity.getMobTriggered(3))
          flag = (flag) || ((tmp instanceof adg)) || ((tmp instanceof ui)) || ((tmp instanceof aic)) || ((tmp instanceof alt));
        if (tileentity.getMobTriggered(4)) {
          flag = (flag) || ((tmp instanceof yw)) || ((tmp instanceof yw)) || ((tmp instanceof yw));
        }
      }
    }
    return flag;
  }
}