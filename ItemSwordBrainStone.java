public class ItemSwordBrainStone extends yr
{
  private int weaponDamage;
  private final EnumToolMaterialBrainStone toolMaterial;

  public ItemSwordBrainStone(int par1, EnumToolMaterialBrainStone par2EnumToolMaterial)
  {
    super(par1);
    this.toolMaterial = par2EnumToolMaterial;
    this.bR = 1;
    g(par2EnumToolMaterial.getMaxUses());
    this.weaponDamage = (4 + par2EnumToolMaterial.getDamageVsEntity());
  }

  public float a(aan par1ItemStack, pb par2Block)
  {
    return par2Block.bO != pb.W.bO ? 1.5F : 15.0F;
  }

  public boolean a(aan par1ItemStack, acq par2EntityLiving, acq par3EntityLiving)
  {
    par1ItemStack.a(1, par3EntityLiving);
    return true;
  }

  public boolean a(aan par1ItemStack, int par2, int par3, int par4, int par5, acq par6EntityLiving)
  {
    par1ItemStack.a(2, par6EntityLiving);
    return true;
  }

  public int a(nn par1Entity)
  {
    return this.weaponDamage;
  }

  public boolean a()
  {
    return true;
  }

  public aaq c(aan par1ItemStack)
  {
    return aaq.d;
  }

  public int b(aan par1ItemStack)
  {
    return 72000;
  }

  public aan a(aan par1ItemStack, xd par2World, yw par3EntityPlayer)
  {
    par3EntityPlayer.c(par1ItemStack, b(par1ItemStack));
    return par1ItemStack;
  }

  public boolean a(pb par1Block)
  {
    return par1Block.bO == pb.W.bO;
  }

  public int b()
  {
    return this.toolMaterial.getEnchantability();
  }
}