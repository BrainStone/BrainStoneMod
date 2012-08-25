public class ItemToolBrainStone extends da
{
  private pb[] blocksEffectiveAgainst;
  protected float a;
  private int damageVsEntity;
  protected EnumToolMaterialBrainStone b;

  protected ItemToolBrainStone(int i, int j, EnumToolMaterialBrainStone enumtoolmaterial, pb[] ablock)
  {
    super(i, j, uk.e, ablock);
    this.a = 4.0F;
    this.b = enumtoolmaterial;
    this.blocksEffectiveAgainst = ablock;
    this.bR = 1;
    g(enumtoolmaterial.getMaxUses());
    this.a = enumtoolmaterial.getEfficiencyOnProperMaterial();
    this.damageVsEntity = (j + enumtoolmaterial.getDamageVsEntity());
  }

  public float a(aan itemstack, pb block)
  {
    for (int i = 0; i < this.blocksEffectiveAgainst.length; i++)
    {
      if (this.blocksEffectiveAgainst[i] == block)
      {
        return this.a;
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
    return this.b.getEnchantability();
  }
}