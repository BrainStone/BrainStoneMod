public class ItemToolBrainStone extends yr
{
  private pb[] blocksEffectiveAgainst;
  protected float efficiencyOnProperMaterial;
  private int damageVsEntity;
  protected EnumToolMaterialBrainStone toolMaterial;

  protected ItemToolBrainStone(int i, int j, EnumToolMaterialBrainStone enumtoolmaterial, pb[] ablock)
  {
    super(i);
    this.efficiencyOnProperMaterial = 4.0F;
    this.toolMaterial = enumtoolmaterial;
    this.blocksEffectiveAgainst = ablock;
    this.bR = 1;
    g(enumtoolmaterial.getMaxUses());
    this.efficiencyOnProperMaterial = enumtoolmaterial.getEfficiencyOnProperMaterial();
    this.damageVsEntity = (j + enumtoolmaterial.getDamageVsEntity());
  }

  public float a(aan itemstack, pb block)
  {
    for (int i = 0; i < this.blocksEffectiveAgainst.length; i++)
    {
      if (this.blocksEffectiveAgainst[i] == block)
      {
        return this.efficiencyOnProperMaterial;
      }
    }

    return 1.0F;
  }

  public boolean a(aan itemstack, acq entityliving, acq entityliving1)
  {
    itemstack.a(2, entityliving1);
    return true;
  }

  public boolean a(aan itemstack, int i, int j, int k, int l, acq entityliving)
  {
    itemstack.a(1, entityliving);
    return true;
  }

  public int a(nn entity)
  {
    return this.damageVsEntity;
  }

  public boolean a()
  {
    return true;
  }

  public int b()
  {
    return this.toolMaterial.getEnchantability();
  }
}