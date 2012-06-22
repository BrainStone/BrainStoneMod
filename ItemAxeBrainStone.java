public class ItemAxeBrainStone extends ItemToolBrainStone
{
  private static pb[] blocksEffectiveAgainst = { pb.x, pb.an, pb.J, pb.au, pb.aj, pb.ak, pb.ba, pb.bf };

  protected ItemAxeBrainStone(int par1, EnumToolMaterialBrainStone par2EnumToolMaterial)
  {
    super(par1, 3, par2EnumToolMaterial, blocksEffectiveAgainst);
  }

  public float a(aan par1ItemStack, pb par2Block)
  {
    if ((par2Block != null) && (par2Block.cd == acn.d))
    {
      return this.efficiencyOnProperMaterial;
    }

    return super.a(par1ItemStack, par2Block);
  }
}