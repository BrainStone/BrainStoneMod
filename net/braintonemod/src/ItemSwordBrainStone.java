package net.braintonemod.src;

import amj;
import lq;
import md;
import qx;
import ul;
import um;
import vn;
import vp;
import xv;

public class ItemSwordBrainStone extends vp
{
  private int weaponDamage;
  private final EnumToolMaterialBrainStone toolMaterial;

  public ItemSwordBrainStone(int i, EnumToolMaterialBrainStone enumtoolmaterialbrainstone)
  {
    super(BrainStone.getId(359 + i), ul.e);
    this.toolMaterial = enumtoolmaterialbrainstone;
    this.ch = 1;
    e(enumtoolmaterialbrainstone.getMaxUses());
    this.weaponDamage = (4 + enumtoolmaterialbrainstone.getDamageVsEntity());
  }

  public String getTextureFile()
  {
    return "/BrainStoneTextures/textures.png";
  }

  public float a(um itemstack, amj block)
  {
    return block.cm == amj.Z.cm ? 15.0F : 1.5F;
  }

  public boolean a(um itemstack, md entityliving, md entityliving1)
  {
    itemstack.a(1, entityliving1);
    return true;
  }

  public boolean onBlockDestroyed(um itemstack, int i, int j, int k, int l, md entityliving)
  {
    itemstack.a(2, entityliving);
    return true;
  }

  public int a(lq entity)
  {
    return this.weaponDamage;
  }

  public boolean n_()
  {
    return true;
  }

  public vn d_(um itemstack)
  {
    return vn.d;
  }

  public int a(um itemstack)
  {
    return 72000;
  }

  public um a(um itemstack, xv world, qx entityplayer)
  {
    entityplayer.a(itemstack, a(itemstack));
    return itemstack;
  }

  public boolean a(amj block)
  {
    return block.cm == amj.Z.cm;
  }

  public int c()
  {
    return this.toolMaterial.getEnchantability();
  }
}