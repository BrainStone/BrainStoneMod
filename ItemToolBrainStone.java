package net.braintonemod.src;

import amj;
import lq;
import md;
import tu;
import ul;
import um;

public class ItemToolBrainStone extends tu
{
  private amj[] blocksEffectiveAgainst;
  protected float a;
  private int cl;
  protected EnumToolMaterialBrainStone b;

  protected ItemToolBrainStone(int i, int j, EnumToolMaterialBrainStone enumtoolmaterialbrainstone, amj[] ablock)
  {
    super(i, j, ul.e, ablock);
    this.a = 4.0F;
    this.b = enumtoolmaterialbrainstone;
    this.blocksEffectiveAgainst = ablock;
    this.ch = 1;
    e(enumtoolmaterialbrainstone.getMaxUses());
    this.a = enumtoolmaterialbrainstone.getEfficiencyOnProperMaterial();
    this.cl = (j + enumtoolmaterialbrainstone.getDamageVsEntity());
  }

  public float a(um itemstack, amj block)
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

  public boolean a(um itemstack, md entityliving, md entityliving1)
  {
    itemstack.a(2, entityliving1);
    return true;
  }

  public boolean onBlockDestroyed(um itemstack, int i, int j, int k, int l, md entityliving)
  {
    itemstack.a(1, entityliving);
    return true;
  }

  public int a(lq entity)
  {
    return this.cl;
  }

  public boolean n_()
  {
    return true;
  }

  public int c()
  {
    return this.b.getEnchantability();
  }
}