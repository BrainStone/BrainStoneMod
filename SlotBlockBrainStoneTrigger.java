class SlotBlockBrainStoneTrigger extends yu
{
  public SlotBlockBrainStoneTrigger(TileEntityBlockBrainStoneTrigger par1Container, int par3, int par4, int par5)
  {
    super(par1Container, par3, par4, par5);
  }

  public boolean a(aan par1ItemStack)
  {
    int i = par1ItemStack.c;

    if ((i > 255) || (i <= 0)) {
      return false;
    }
    pb tmp = pb.m[i];
    int ID = tmp.bO;

    if (tmp == null) {
      return false;
    }
    if ((ID == mod_BrainStone.brainStoneTrigger.bO) || (ID == pb.K.bO)) {
      return false;
    }
    return tmp.a();
  }

  public int a()
  {
    return 1;
  }
}