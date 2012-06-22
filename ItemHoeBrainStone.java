public class ItemHoeBrainStone extends yr
{
  public ItemHoeBrainStone(int par1, EnumToolMaterialBrainStone par2EnumToolMaterial)
  {
    super(par1);
    this.bR = 1;
    g(par2EnumToolMaterial.getMaxUses());
  }

  public boolean a(aan par1ItemStack, yw par2EntityPlayer, xd par3World, int par4, int par5, int par6, int par7)
  {
    if (!par2EntityPlayer.e(par4, par5, par6))
    {
      return false;
    }

    int i = par3World.a(par4, par5, par6);
    int j = par3World.a(par4, par5 + 1, par6);

    if (((par7 != 0) && (j == 0) && (i == pb.u.bO)) || (i == pb.v.bO))
    {
      pb block = pb.aA;
      par3World.a(par4 + 0.5F, par5 + 0.5F, par6 + 0.5F, block.cb.d(), (block.cb.b() + 1.0F) / 2.0F, block.cb.c() * 0.8F);

      if (par3World.F)
      {
        return true;
      }

      par3World.g(par4, par5, par6, block.bO);
      par1ItemStack.a(1, par2EntityPlayer);
      return true;
    }

    return false;
  }

  public boolean a()
  {
    return true;
  }
}