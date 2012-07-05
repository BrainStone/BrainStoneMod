import java.util.Random;
import java.util.logging.Logger;

public class mod_BrainStone extends BaseMod
{
  private static final Logger logger = Logger.getLogger("mod_BrainStone");
  public static pb brainStone;
  public static pb brainStoneOut;
  public static pb brainStoneOre;
  public static pb dirtyBrainStone;
  public static pb brainLightSensor;
  public static pb brainStoneTrigger;
  public static yr brainStoneDust;
  public static yr coalBriquette;
  public static yr brainStoneSword;
  public static yr brainStoneShovel;
  public static yr brainStonePickaxe;
  public static yr brainStoneAxe;
  public static yr brainStoneHoe;
  public static int brainStoneTexture;
  public static int brainStoneOutTexture;
  public static int brainStoneOreTexture;
  public static int dirtyBrainStoneTexture;
  public static int brainLightSensorTexture;
  public static int brainStoneTriggerTexture;
  public static int brainStoneSwordTexture;
  public static int brainStoneShovelTexture;
  public static int brainStonePickaxeTexture;
  public static int brainStoneAxeTexture;
  public static int brainStoneHoeTexture;
  public static aeb WTHIT;
  public static aeb itLives;
  public static aeb intelligentBlocks;
  public static aeb intelligentTools;
  private static String de;

  public void load()
  {
    brainStoneTexture = ModLoader.addOverride("/terrain.png", "/BrainStone/brainStone.png");
    brainStoneOutTexture = ModLoader.addOverride("/terrain.png", "/BrainStone/brainStoneOut.png");
    brainStoneOreTexture = ModLoader.addOverride("/terrain.png", "/BrainStone/brainStoneOre.png");
    dirtyBrainStoneTexture = ModLoader.addOverride("/terrain.png", "/BrainStone/dirtyBrainStone.png");
    brainLightSensorTexture = ModLoader.addOverride("/terrain.png", "/BrainStone/brainLightSensor.png");
    brainStoneTriggerTexture = ModLoader.addOverride("/terrain.png", "/BrainStone/brainStoneTrigger.png");
    brainStoneSwordTexture = ModLoader.addOverride("/gui/items.png", "/BrainStone/brainStoneSword.png");
    brainStoneShovelTexture = ModLoader.addOverride("/gui/items.png", "/BrainStone/brainStoneShovel.png");
    brainStonePickaxeTexture = ModLoader.addOverride("/gui/items.png", "/BrainStone/brainStonePickaxe.png");
    brainStoneAxeTexture = ModLoader.addOverride("/gui/items.png", "/BrainStone/brainStoneAxe.png");
    brainStoneHoeTexture = ModLoader.addOverride("/gui/items.png", "/BrainStone/brainStoneHoe.png");

    brainStone = new BlockBrainStone(210, brainStoneTexture, false).c(3.0F).a("brainStone").b(1.0F).a(1.0F);
    brainStoneOut = new BlockBrainStone(211, brainStoneOutTexture, true).c(3.0F).a("brainStoneOut").b(1.0F).a(0.0F);
    brainStoneOre = new BlockBrainStoneOre(212, brainStoneOreTexture).c(2.0F).a("brainStoneOre").b(0.25F).a(0.3F);
    dirtyBrainStone = new pb(213, dirtyBrainStoneTexture, acn.e).c(2.4F).a("dirtyBrainStone").b(0.5F).a(0.5F);
    brainLightSensor = new BlockBrainLightSensor(214, brainLightSensorTexture).c(2.4F).b(0.5F).a("brainLightSensor");
    brainStoneTrigger = new BlockBrainStoneTrigger(215, brainStoneTriggerTexture).c(2.4F).b(0.5F).a("brainStoneTrigger").k();

    brainStoneOre.cc = 0.2F;
    dirtyBrainStone.cc = -0.1F;

    brainStoneDust = new yr(1358).e(ModLoader.addOverride("/gui/items.png", "/BrainStone/brainStoneDust.png")).a("brainStoneDust");
    coalBriquette = new yr(1359).e(ModLoader.addOverride("/gui/items.png", "/BrainStone/coalBriquette.png")).a("coalBriquette");

    brainStoneSword = new ItemSwordBrainStone(1364, EnumToolMaterialBrainStone.BRAINSTONE).e(brainStoneSwordTexture).a("brainStoneSword");
    brainStoneShovel = new ItemSpadeBrainStone(1365, EnumToolMaterialBrainStone.BRAINSTONE).e(brainStoneShovelTexture).a("brainStoneShovel");
    brainStonePickaxe = new ItemPickaxeBrainStone(1366, EnumToolMaterialBrainStone.BRAINSTONE).e(brainStonePickaxeTexture).a("brainStonePickaxe");
    brainStoneAxe = new ItemAxeBrainStone(1367, EnumToolMaterialBrainStone.BRAINSTONE).e(brainStoneAxeTexture).a("brainStoneAxe");
    brainStoneHoe = new ItemHoeBrainStone(1368, EnumToolMaterialBrainStone.BRAINSTONE).e(brainStoneHoeTexture).a("brainStoneHoe");

    WTHIT = new aeb(1004, "What the Hell is that???", 8, 2, brainStoneDust, dp.o).d();
    itLives = new aeb(1005, "It lives!", 8, 4, brainStone, WTHIT).d();
    intelligentBlocks = new aeb(1006, "Intelligent Blocks!", 6, 5, brainLightSensor, itLives).d();
    intelligentTools = new aeb(1007, "Intelligent Tools!", 10, 5, brainStonePickaxe, itLives).d();

    de = "de_DE";

    ModLoader.registerTileEntity(TileEntityBlockBrainLightSensor.class, "TileEntityBlockBrainLightSensor");
    ModLoader.registerTileEntity(TileEntityBlockBrainStoneTrigger.class, "TileEntityBlockBrainStoneTrigger");

    ModLoader.registerBlock(brainStone);
    ModLoader.registerBlock(brainStoneOut);
    ModLoader.registerBlock(brainStoneOre);
    ModLoader.registerBlock(dirtyBrainStone);
    ModLoader.registerBlock(brainLightSensor);
    ModLoader.registerBlock(brainStoneTrigger);

    ModLoader.addName(brainStone, "Brain Stone");
    ModLoader.addName(brainStoneOut, "Brain Stone");
    ModLoader.addName(brainStoneOre, "Brain Stone Ore");
    ModLoader.addName(dirtyBrainStone, "Dirty Brain Stone");
    ModLoader.addName(brainLightSensor, "Brain Light Sensor");
    ModLoader.addName(brainStoneTrigger, "Brain Stone Trigger");

    ModLoader.addName(brainStoneDust, "Brain Stone Dust");
    ModLoader.addName(coalBriquette, "Coal Briquette");

    ModLoader.addName(brainStoneSword, "Brain Stone Sword");
    ModLoader.addName(brainStoneShovel, "Brain Stone Shovel");
    ModLoader.addName(brainStonePickaxe, "Brain Stone Pickaxe");
    ModLoader.addName(brainStoneAxe, "Brain Stone Axe");
    ModLoader.addName(brainStoneHoe, "Brain Stone Hoe");

    ModLoader.addAchievementDesc(WTHIT, "What the Hell is that???", "You have to find a strange green powder.");
    ModLoader.addAchievementDesc(itLives, "It lives!", "Crafting and a Smelting is the key!");
    ModLoader.addAchievementDesc(intelligentBlocks, "Intelligent Blocks!", "Make usefull intelligent Blocks out of this green stone!");
    ModLoader.addAchievementDesc(intelligentTools, "Intelligent Tools!", "Make Tools out of this green stone!");

    ModLoader.addLocalization("gui.brainstone.item", "Item/Object");
    ModLoader.addLocalization("gui.brainstone.animal", "Animal");
    ModLoader.addLocalization("gui.brainstone.monster", "Monster");
    ModLoader.addLocalization("gui.brainstone.nether", "Nether Mob");
    ModLoader.addLocalization("gui.brainstone.player", "Player");

    ModLoader.addName(brainStone, de, "Hirnstein");
    ModLoader.addName(brainStoneOut, de, "Hirnstein");
    ModLoader.addName(brainStoneOre, de, "Hirnsteinerz");
    ModLoader.addName(dirtyBrainStone, de, "Dreckiger Hirnstein");
    ModLoader.addName(brainLightSensor, de, "Hirnlichtsensor");
    ModLoader.addName(brainStoneTrigger, de, "Hirnsteinauslöser");

    ModLoader.addName(brainStoneDust, de, "Hirnsteinstaub");
    ModLoader.addName(coalBriquette, de, "Kohlebrikett");

    ModLoader.addName(brainStoneSword, de, "Hirnsteinschwert");
    ModLoader.addName(brainStoneShovel, de, "Hirnsteinschaufel");
    ModLoader.addName(brainStonePickaxe, de, "Hirnsteinspitzhacke");
    ModLoader.addName(brainStoneAxe, de, "Hirnsteinaxt");
    ModLoader.addName(brainStoneHoe, de, "Hirnsteinfeldhacke");

    addAchievementDesc(WTHIT, de, "Was zur Hölle ist das???", "Du must ein seltsames grünes Pulver finden.");
    addAchievementDesc(itLives, de, "Es lebt!", "Craften und Schmelzen ist die Lösung!");
    addAchievementDesc(intelligentBlocks, de, "Intilligente Blöcke!", "Stelle nützliche intilligente Blöcke aus diesem grünen Stein her!");
    addAchievementDesc(intelligentTools, de, "Intilligente Werkzeuge", "Stelle Werkzeuge aus diesem grünen Stein her!");

    ModLoader.addLocalization("gui.brainstone.item", de, "Item/Objekt");
    ModLoader.addLocalization("gui.brainstone.animal", de, "Tier");
    ModLoader.addLocalization("gui.brainstone.monster", de, "Monster");
    ModLoader.addLocalization("gui.brainstone.nether", de, "Nether Wesen");
    ModLoader.addLocalization("gui.brainstone.player", de, "Spieler");

    ModLoader.addRecipe(new aan(dirtyBrainStone, 1), new Object[] { "XX", "XX", Character.valueOf('X'), brainStoneDust });

    ModLoader.addRecipe(new aan(coalBriquette, 1), new Object[] { "XXX", "XXX", "XXX", Character.valueOf('X'), yr.m });

    ModLoader.addRecipe(new aan(brainLightSensor, 1), new Object[] { "XGX", "XBX", "XRX", Character.valueOf('X'), pb.t, Character.valueOf('G'), pb.M, Character.valueOf('B'), brainStone, Character.valueOf('R'), yr.aC });

    ModLoader.addRecipe(new aan(brainStoneTrigger, 1), new Object[] { "XXX", "RRR", "XBX", Character.valueOf('X'), pb.t, Character.valueOf('B'), brainStone, Character.valueOf('R'), yr.aC });

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

  public void onItemPickup(yw entityplayer, aan itemstack)
  {
    if (itemstack.c == brainStoneDust.bQ)
    {
      entityplayer.a(WTHIT, 1);
    }
  }

  public void takenFromFurnace(yw entityplayer, aan itemstack)
  {
    if (itemstack.c == brainStone.bO)
    {
      entityplayer.a(itLives, 1);
    }
  }

  public void takenFromCrafting(yw entityplayer, aan itemstack)
  {
    int ID = itemstack.c;

    if ((ID == brainLightSensor.bO) || (ID == brainStoneTrigger.bO))
    {
      entityplayer.a(intelligentBlocks, 1);
    }
    else if ((ID == brainStoneSword.bQ) || (ID == brainStoneShovel.bQ) || (ID == brainStonePickaxe.bQ) || (ID == brainStoneAxe.bQ) || (ID == brainStoneHoe.bQ))
    {
      entityplayer.a(intelligentTools, 1);
    }
  }

  public static void addAchievementDesc(aeb achievement, String lang, String s, String s1)
  {
    try
    {
      if (achievement.i().contains("."))
      {
        String[] as = achievement.i().split("\\.");

        if (as.length == 2)
        {
          String s2 = as[1];
          ModLoader.addLocalization("achievement." + s2, lang, s);
          ModLoader.addLocalization("achievement." + s2 + ".desc", lang, s1);
          ModLoader.setPrivateValue(ajw.class, achievement, 1, cy.a("achievement." + s2));
          ModLoader.setPrivateValue(aeb.class, achievement, 3, cy.a("achievement." + s2 + ".desc"));
        }
        else
        {
          ModLoader.setPrivateValue(ajw.class, achievement, 1, s);
          ModLoader.setPrivateValue(aeb.class, achievement, 3, s1);
        }
      }
      else
      {
        ModLoader.setPrivateValue(ajw.class, achievement, 1, s);
        ModLoader.setPrivateValue(aeb.class, achievement, 3, s1);
      }
    }
    catch (IllegalArgumentException illegalargumentexception)
    {
      logger.throwing("ModLoader", "AddAchievementDesc", illegalargumentexception);
      throwException(illegalargumentexception);
    }
    catch (SecurityException securityexception)
    {
      logger.throwing("ModLoader", "AddAchievementDesc", securityexception);
      throwException(securityexception);
    }
    catch (NoSuchFieldException nosuchfieldexception)
    {
      logger.throwing("ModLoader", "AddAchievementDesc", nosuchfieldexception);
      throwException(nosuchfieldexception);
    }
  }

  private static void throwException(Throwable throwable)
  {
    ModLoader.throwException("Exception occured in ModLoader", throwable);
  }

  public String getVersion()
  {
    return "1.8.2 BETA release";
  }
}