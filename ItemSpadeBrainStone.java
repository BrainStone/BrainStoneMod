public class ItemSpadeBrainStone extends ItemToolBrainStone
{
  private static pb[] blocksEffectiveAgainst = { pb.u, pb.v, pb.E, pb.F, pb.aS, pb.aU, pb.aW, pb.aA, pb.bc, pb.by };

  public ItemSpadeBrainStone(int par1, EnumToolMaterialBrainStone par2EnumToolMaterial)
  {
    super(par1, 1, par2EnumToolMaterial, blocksEffectiveAgainst);
  }

  public boolean a(pb par1Block)
  {
    if (par1Block == pb.aS)
    {
      return true;
    }

    return par1Block == pb.aU;
  }
}