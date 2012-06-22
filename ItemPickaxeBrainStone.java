public class ItemPickaxeBrainStone extends ItemToolBrainStone
{
  private static pb[] blocksEffectiveAgainst = { pb.w, pb.aj, pb.ak, pb.t, pb.Q, pb.ao, pb.H, pb.ai, pb.I, pb.ah, pb.G, pb.aw, pb.ax, pb.aT, pb.bb, pb.N, pb.O, pb.aN, pb.aO, pb.aG, pb.U, pb.T, mod_BrainStone.brainStone, mod_BrainStone.brainStoneOut, mod_BrainStone.brainStoneOre, mod_BrainStone.dirtyBrainStone, mod_BrainStone.brainLightSensor };

  protected ItemPickaxeBrainStone(int par1, EnumToolMaterialBrainStone par2EnumToolMaterial)
  {
    super(par1, 2, par2EnumToolMaterial, blocksEffectiveAgainst);
  }

  public boolean a(pb par1Block)
  {
    if (par1Block == pb.ap)
    {
      return this.toolMaterial.getHarvestLevel() == 3;
    }

    if ((par1Block == pb.ax) || (par1Block == pb.aw))
    {
      return this.toolMaterial.getHarvestLevel() >= 2;
    }

    if ((par1Block == pb.ah) || (par1Block == pb.G))
    {
      return this.toolMaterial.getHarvestLevel() >= 2;
    }

    if ((par1Block == pb.ai) || (par1Block == pb.H))
    {
      return this.toolMaterial.getHarvestLevel() >= 1;
    }

    if ((par1Block == pb.O) || (par1Block == pb.N))
    {
      return this.toolMaterial.getHarvestLevel() >= 1;
    }

    if ((par1Block == pb.aN) || (par1Block == pb.aO))
    {
      return this.toolMaterial.getHarvestLevel() >= 2;
    }

    if (par1Block.cd == acn.e)
    {
      return true;
    }

    return par1Block.cd == acn.f;
  }

  public float a(aan par1ItemStack, pb par2Block)
  {
    if ((par2Block != null) && ((par2Block.cd == acn.f) || (par2Block.cd == acn.e)))
    {
      return this.efficiencyOnProperMaterial;
    }

    return super.a(par1ItemStack, par2Block);
  }
}