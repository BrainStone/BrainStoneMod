import java.util.Random;

public class mod_BrainStone extends BaseMod
{
  public static final pb brainStone;
  public static final pb brainStoneOut;
  public static final pb brainStoneOre;
  public static final pb dirtyBrainStone;
  public static final pb brainLightSensor;
  public static final yr brainStoneDust = new yr(1358).e(ModLoader.addOverride("/gui/items.png", "/BrainStone/brainStoneDust.png")).a("brainStoneDust");
  public static final yr coalBriquette = new yr(1359).e(ModLoader.addOverride("/gui/items.png", "/BrainStone/coalBriquette.png")).a("coalBriquette");

  public static final yr brainStoneSword = new ItemSwordBrainStone(1364, EnumToolMaterialBrainStone.BRAINSTONE).e(brainStoneSwordTexture).a("brainStoneSword");
  public static final yr brainStoneShovel = new ItemSpadeBrainStone(1365, EnumToolMaterialBrainStone.BRAINSTONE).e(brainStoneShovelTexture).a("brainStoneShovel");
  public static final yr brainStonePickaxe = new ItemPickaxeBrainStone(1366, EnumToolMaterialBrainStone.BRAINSTONE).e(brainStonePickaxeTexture).a("brainStonePickaxe");
  public static final yr brainStoneAxe = new ItemAxeBrainStone(1367, EnumToolMaterialBrainStone.BRAINSTONE).e(brainStoneAxeTexture).a("brainStoneAxe");
  public static final yr brainStoneHoe = new ItemHoeBrainStone(1368, EnumToolMaterialBrainStone.BRAINSTONE).e(brainStoneHoeTexture).a("brainStoneHoe");

  public static final int brainStoneTexture = ModLoader.addOverride("/terrain.png", "/BrainStone/brainStone.png");
  public static final int brainStoneOutTexture = ModLoader.addOverride("/terrain.png", "/BrainStone/brainStoneOut.png");
  public static final int brainStoneOreTexture = ModLoader.addOverride("/terrain.png", "/BrainStone/brainStoneOre.png");
  public static final int dirtyBrainStoneTexture = ModLoader.addOverride("/terrain.png", "/BrainStone/dirtyBrainStone.png");
  public static final int brainLightSensorTexture = ModLoader.addOverride("/terrain.png", "/BrainStone/brainLightSensor.png");
  public static final int brainStoneSwordTexture = ModLoader.addOverride("/gui/items.png", "/BrainStone/brainStoneSword.png");
  public static final int brainStoneShovelTexture = ModLoader.addOverride("/gui/items.png", "/BrainStone/brainStoneShovel.png");
  public static final int brainStonePickaxeTexture = ModLoader.addOverride("/gui/items.png", "/BrainStone/brainStonePickaxe.png");
  public static final int brainStoneAxeTexture = ModLoader.addOverride("/gui/items.png", "/BrainStone/brainStoneAxe.png");
  public static final int brainStoneHoeTexture = ModLoader.addOverride("/gui/items.png", "/BrainStone/brainStoneHoe.png");

  private static final String de = "de_DE";

  public void load()
  {
    ModLoader.setInGameHook(this, true, false);

    ModLoader.registerBlock(brainStone);
    ModLoader.registerBlock(brainStoneOut);
    ModLoader.registerBlock(brainStoneOre);
    ModLoader.registerBlock(dirtyBrainStone);
    ModLoader.registerBlock(brainLightSensor);

    ModLoader.addName(brainStone, "Brain Stone");
    ModLoader.addName(brainStoneOut, "Brain Stone");
    ModLoader.addName(brainStoneOre, "Brain Stone Ore");
    ModLoader.addName(dirtyBrainStone, "Dirty Brain Stone");
    ModLoader.addName(brainLightSensor, "Brain Light Sensor");

    ModLoader.addName(brainStoneDust, "Brain Stone Dust");
    ModLoader.addName(coalBriquette, "Coal Briquette");

    ModLoader.addName(brainStoneSword, "Brain Stone Sword");
    ModLoader.addName(brainStoneShovel, "Brain Stone Shovel");
    ModLoader.addName(brainStonePickaxe, "Brain Stone Pickaxe");
    ModLoader.addName(brainStoneAxe, "Brain Stone Axe");
    ModLoader.addName(brainStoneHoe, "Brain Stone Hoe");

    ModLoader.addName(brainStone, de, "Hirnstein");
    ModLoader.addName(brainStoneOut, de, "Hirnstein");
    ModLoader.addName(brainStoneOre, de, "Hirnsteinerz");
    ModLoader.addName(dirtyBrainStone, de, "Dreckiger Hirnstein");
    ModLoader.addName(brainLightSensor, de, "Hirnlichtsensor");

    ModLoader.addName(brainStoneDust, de, "Hirnsteinstaub");
    ModLoader.addName(coalBriquette, de, "Kohlebrikett");

    ModLoader.addName(brainStoneSword, de, "Hirnsteinschwert");
    ModLoader.addName(brainStoneShovel, de, "Hirnsteinschaufel");
    ModLoader.addName(brainStonePickaxe, de, "Hirnsteinspitzhacke");
    ModLoader.addName(brainStoneAxe, de, "Hirnsteinaxt");
    ModLoader.addName(brainStoneHoe, de, "Hirnsteinfeldhacke");

    ModLoader.addRecipe(new aan(dirtyBrainStone, 1), new Object[] { "XX", "XX", Character.valueOf('X'), brainStoneDust });

    ModLoader.addRecipe(new aan(coalBriquette, 1), new Object[] { "XXX", "XXX", "XXX", Character.valueOf('X'), yr.m });

    ModLoader.addRecipe(new aan(brainLightSensor, 1), new Object[] { "XGX", "XBX", "XRX", Character.valueOf('X'), pb.t, Character.valueOf('G'), pb.M, Character.valueOf('B'), brainStone, Character.valueOf('R'), yr.aC });

    ModLoader.addRecipe(new aan(brainStoneSword, 1), new Object[] { "B", "B", "S", Character.valueOf('S'), yr.D, Character.valueOf('B'), brainStone });

    ModLoader.addRecipe(new aan(brainStoneShovel, 1), new Object[] { "B", "S", "S", Character.valueOf('S'), yr.D, Character.valueOf('B'), brainStone });

    ModLoader.addRecipe(new aan(brainStonePickaxe, 1), new Object[] { "BBB", " S ", " S ", Character.valueOf('S'), yr.D, Character.valueOf('B'), brainStone });

    ModLoader.addRecipe(new aan(brainStoneAxe, 1), new Object[] { "BB", "BS", " S", Character.valueOf('S'), yr.D, Character.valueOf('B'), brainStone });

    ModLoader.addRecipe(new aan(brainStoneAxe, 1), new Object[] { "BB", "SB", "S ", Character.valueOf('S'), yr.D, Character.valueOf('B'), brainStone });

    ModLoader.addRecipe(new aan(brainStoneHoe, 1), new Object[] { "BB", " S", " S", Character.valueOf('S'), yr.D, Character.valueOf('B'), brainStone });

    ModLoader.addRecipe(new aan(brainStoneHoe, 1), new Object[] { "BB", "S ", "S ", Character.valueOf('S'), yr.D, Character.valueOf('B'), brainStone });

    ModLoader.addSmelting(dirtyBrainStone.bO, new aan(brainStone, 1, 0));
  }

  public int addFuel(int i, int j)
  {
    if (i == coalBriquette.bQ)
    {
      return 16000;
    }

    return 0;
  }

  public void generateSurface(xd world, Random rand, int chunkX, int chunkZ)
  {
    for (int i = 0; i < 1; i++)
    {
      int randPosX = chunkX + rand.nextInt(16);
      int randPosY = rand.nextInt(32);
      int randPosZ = chunkZ + rand.nextInt(16);

      new eh(brainStoneOre.bO, 20).a(world, rand, randPosX, randPosY, randPosZ);
    }
  }

  public String getVersion()
  {
    return "1.4.3 BETA release";
  }

  static
  {
    brainStone = new BlockBrainStone(210, brainStoneTexture, false).c(3.0F).a("brainStone").b(1.0F).a(1.0F);
    brainStoneOut = new BlockBrainStone(211, brainStoneOutTexture, true).c(3.0F).a("brainStoneOut").b(1.0F).a(0.0F);
    brainStoneOre = new BlockBrainStoneOre(212, brainStoneOreTexture).c(2.0F).a("brainStoneOre").b(0.25F).a(0.3F);
    dirtyBrainStone = new pb(213, dirtyBrainStoneTexture, acn.e).c(2.4F).a("dirtyBrainStone").b(0.5F).a(0.5F);
    brainLightSensor = new BlockBrainLightSensor(214, brainLightSensorTexture).c(2.4F).b(0.5F).a("brainLightSensor");

    brainStoneOre.cc = 0.2F;
    dirtyBrainStone.cc = -0.1F;
  }
}