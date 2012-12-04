package net.braintonemod.src;

public enum EnumToolMaterialBrainStone
{
  BRAINSTONE(3, 5368, 6.0F, 5, 25);

  private final int harvestLevel;
  private final int maxUses;
  private final float efficiencyOnProperMaterial;
  private final int damageVsEntity;
  private final int enchantability;

  private EnumToolMaterialBrainStone(int j, int k, float f, int l, int i1)
  {
    this.harvestLevel = j;
    this.maxUses = k;
    this.efficiencyOnProperMaterial = f;
    this.damageVsEntity = l;
    this.enchantability = i1;
  }

  public int getMaxUses()
  {
    return this.maxUses;
  }

  public float getEfficiencyOnProperMaterial()
  {
    return this.efficiencyOnProperMaterial;
  }

  public int getDamageVsEntity()
  {
    return this.damageVsEntity;
  }

  public int getHarvestLevel()
  {
    return this.harvestLevel;
  }

  public int getEnchantability()
  {
    return this.enchantability;
  }
}