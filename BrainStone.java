package mods.brainstone;

import ModLoader;
import aab;
import aif;
import apa;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import ju;
import jv;
import mods.brainstone.blocks.BlockBrainLightSensor;
import mods.brainstone.blocks.BlockBrainLogicBlock;
import mods.brainstone.blocks.BlockBrainStone;
import mods.brainstone.blocks.BlockBrainStoneOre;
import mods.brainstone.blocks.BlockBrainStoneTrigger;
import mods.brainstone.blocks.BlockPulsatingBrainStone;
import mods.brainstone.handlers.BrainStoneCraftingHandler;
import mods.brainstone.handlers.BrainStoneFuelHandler;
import mods.brainstone.handlers.BrainStoneGuiHandler;
import mods.brainstone.handlers.BrainStonePacketHandler;
import mods.brainstone.handlers.BrainStonePickupNotifier;
import mods.brainstone.items.ItemArmorBrainStone;
import mods.brainstone.items.ItemHoeBrainStone;
import mods.brainstone.items.ItemSwordBrainStone;
import mods.brainstone.items.ItemToolBrainStone;
import mods.brainstone.templates.BSP;
import mods.brainstone.templates.BlockBrainStoneBase;
import mods.brainstone.templates.ItemBrainStoneBase;
import mods.brainstone.tileentities.TileEntityBlockBrainLightSensor;
import mods.brainstone.tileentities.TileEntityBlockBrainLogicBlock;
import mods.brainstone.tileentities.TileEntityBlockBrainStoneTrigger;
import mods.brainstone.worldgenerators.BrainStoneWorldGenerator;
import mp;
import mv;
import nc;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.EnumHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Property;
import ng;
import rf;
import rh;
import ri;
import rr;
import sq;
import ss;
import st;
import su;
import sw;
import tb;
import uq;
import ve;
import wk;
import wl;
import wm;

@Mod(modid="BrainStoneMod", name="Brain Stone Mod", version="v2.19.97 BETA release")
@NetworkMod(clientSideRequired=true, serverSideRequired=true, channels={"BSM", "BSM.TEBBSTS", "BSM.TEBBSTC", "BSM.TEBBLSS", "BSM.TEBBLSC", "BSM.TEBBLBS", "BSM.TEBBLBC", "BSM.UPAS", "BSM.RRBAC", "BSM.UPMC"}, packetHandler=BrainStonePacketHandler.class)
public class BrainStone
{
  private static Mod annotation = null;

  public static final boolean release = getModAnnotation().version().toLowerCase().contains("release");

  public static final boolean debug = getModAnnotation().version().toLowerCase().contains("debug");
  public static final int startBlockId = 1258;
  public static final int startItemId = 359;
  public static final int startAchievementId = 3698;
  private static final String de = "de_DE";
  public static final String basePath = "/mods/brainstone/";
  public static final String guiPath = "/mods/brainstone/textures/gui/";
  public static final String armorPath = "/mods/brainstone/textures/armor/";

  @Mod.Instance("BrainStoneMod")
  public static BrainStone instance;

  @SidedProxy(clientSide="mods.brainstone.ClientProxy", serverSide="mods.brainstone.CommonProxy")
  public static ClientProxy proxy;
  public static wl toolBRAINSTONE = EnumHelper.addToolMaterial("BRAINSTONE", 3, 5368, 6.0F, 5, 25);

  public static uq armorBRAINSTONE = EnumHelper.addArmorMaterial("BRAINSTONE", 114, new int[] { 2, 6, 5, 2 }, 25);

  private static HashMap ids = new HashMap();

  private static HashMap blocks = new HashMap();

  private static HashMap items = new HashMap();

  private static HashMap achievements = new HashMap();

  private static HashMap name_en = new HashMap();
  private static HashMap name_de;
  private static HashMap localizations_en;
  private static HashMap localizations_de;
  private static HashMap achievement_en;
  private static HashMap achievement_de;

  @Mod.PreInit
  public void preInit(FMLPreInitializationEvent event)
  {
    generateMcModInfoFile(event);

    getIds(event);

    generateObjects();

    proxy.registerTextures();
  }

  @Mod.Init
  public void init(FMLInitializationEvent event)
  {
    proxy.registerRenderInformation();
    NetworkRegistry.instance().registerGuiHandler(this, new BrainStoneGuiHandler());

    registerBlocks();
    registerTileEntitys();
    addNames();
    addRecipes();
    addSmeltings();

    GameRegistry.registerFuelHandler(new BrainStoneFuelHandler());

    GameRegistry.registerWorldGenerator(new BrainStoneWorldGenerator());

    GameRegistry.registerCraftingHandler(new BrainStoneCraftingHandler());

    GameRegistry.registerPickupHandler(new BrainStonePickupNotifier());
    addLocalizations();

    MinecraftForge.setBlockHarvestLevel(brainStone(), "pickaxe", 2);
    MinecraftForge.setBlockHarvestLevel(brainStoneOut(), "pickaxe", 2);
    MinecraftForge.setBlockHarvestLevel(dirtyBrainStone(), "pickaxe", 2);
    MinecraftForge.setBlockHarvestLevel(brainStoneOre(), "pickaxe", 2);

    proxy.registerOre();
  }

  @Mod.PostInit
  public void postInit(FMLPostInitializationEvent event)
    throws Throwable
  {
    fillTriggerEntities();
  }

  private static void generateMcModInfoFile(FMLPreInitializationEvent event)
  {
    event.getModMetadata().modId = getModAnnotation().modid();
    event.getModMetadata().name = getModAnnotation().name();
    event.getModMetadata().version = getModAnnotation().version();
    event.getModMetadata().url = "http://minecraft.de/showthread.php?89926";
    event.getModMetadata().credits = "The_BrainStone(Code, Textures, Ideas), Jobbel(Name), TheStarman(Textures)";
    event.getModMetadata().authorList = Arrays.asList(new String[] { "The_BrainStone" });
    event.getModMetadata().description = "The Brain Stone Mod adds a new block type. It is called Brain Stone. It is very rare but you can make many different intelligent sensor blocks! An example is the BrainStoneTrigger. It's a block that triggers if an entity is on top. All these intelligent blocks are highly adjustable! There are also tools. The are as fast as iron tools but you can havrest more than 5,368 blocks! (Diamond tools only 1,561). The latest feature is the PulsatingBrainStoneBlock. It is the craziest block you have ever seen! It will throw you and animals through the air or will add random potion effects! You acan make yourself immune to these effect by wearing the full set of the newly adden BrainStoneArmor.\nBut see by yourself and enjoy!\n\n\nAnd thanks for downloading and supporting this mod!\n\n\n\nIf you think this mod caused a game crash (what should not happen by the way XD) send an email with the error log to yannick@tedworld.de!\n\nThank you!";
    event.getModMetadata().logoFile = "";
    event.getModMetadata().updateUrl = "http://minecraft.de/showthread.php?89926";
    event.getModMetadata().parent = "";
    event.getModMetadata().screenshots = new String[0];
  }

  private static void addLocalizations()
  {
    int length = localizations_en.size();
    String[] keys = (String[])localizations_en.keySet().toArray(new String[length]);

    LanguageRegistry.instance();

    for (int i = 0; i < length; i++) {
      String key = keys[i];

      LanguageRegistry.instance().addStringLocalization(key, (String)localizations_en.get(key));

      LanguageRegistry.instance().addStringLocalization(key, "de_DE", (String)localizations_de.get(key));
    }
  }

  private static final void addNames()
  {
    HashMap objects = new HashMap(blocks);

    objects.putAll(items);
    objects.putAll(achievements);

    int length = objects.size();
    Integer[] keys = (Integer[])objects.keySet().toArray(new Integer[length]);

    for (int i = 0; i < length; i++) {
      int key = keys[i].intValue();
      Object obj = objects.get(Integer.valueOf(key));

      if (key < 3698) {
        LanguageRegistry.addName(obj, get_name_en(key));

        LanguageRegistry.instance().addNameForObject(obj, "de_DE", get_name_de(key));
      }
      else {
        int key2 = key - 3698;

        ju tmp = (ju)obj;
        LanguageRegistry.instance().addStringLocalization(tmp.i(), ((String[])achievement_en.get(Integer.valueOf(key2)))[0]);

        LanguageRegistry.instance().addStringLocalization(tmp.i() + ".desc", ((String[])achievement_en.get(Integer.valueOf(key2)))[1]);

        LanguageRegistry.instance().addStringLocalization(tmp.i(), "de_DE", ((String[])achievement_de.get(Integer.valueOf(key2)))[0]);

        LanguageRegistry.instance().addStringLocalization(tmp.i() + ".desc", "de_DE", ((String[])achievement_de.get(Integer.valueOf(key2)))[1]);
      }
    }
  }

  private static void addRecipes()
  {
    GameRegistry.addRecipe(new wm(dirtyBrainStone(), 1), new Object[] { "XX", "XX", Character.valueOf('X'), brainStoneDust() });

    for (int i = 0; i <= 9; i++) {
      GameRegistry.addShapelessRecipe(new wm(coalBriquette(), 1), new Object[] { new wm(wk.n, 1, i <= 0 ? 0 : 1), new wm(wk.n, 1, i <= 1 ? 0 : 1), new wm(wk.n, 1, i <= 2 ? 0 : 1), new wm(wk.n, 1, i <= 3 ? 0 : 1), new wm(wk.n, 1, i <= 4 ? 0 : 1), new wm(wk.n, 1, i <= 5 ? 0 : 1), new wm(wk.n, 1, i <= 6 ? 0 : 1), new wm(wk.n, 1, i <= 7 ? 0 : 1), new wm(wk.n, 1, i <= 8 ? 0 : 1) });
    }

    GameRegistry.addRecipe(new wm(brainLightSensor(), 1), new Object[] { "XGX", "XBX", "XRX", Character.valueOf('X'), apa.x, Character.valueOf('G'), apa.Q, Character.valueOf('B'), brainStone(), Character.valueOf('R'), wk.aD });

    GameRegistry.addRecipe(new wm(brainStoneTrigger(), 1), new Object[] { "XXX", "RRR", "XBX", Character.valueOf('X'), apa.x, Character.valueOf('B'), brainStone(), Character.valueOf('R'), wk.aD });

    GameRegistry.addRecipe(new wm(brainLogicBlock(), 1), new Object[] { "SRS", "RPR", "SRS", Character.valueOf('S'), apa.x, Character.valueOf('P'), brainProcessor(), Character.valueOf('R'), wk.aD });

    GameRegistry.addRecipe(new wm(pulsatingBrainStone(), 1), new Object[] { "dBd", "BDB", "dBd", Character.valueOf('d'), brainStoneDust(), Character.valueOf('B'), brainStone(), Character.valueOf('D'), wk.o });

    GameRegistry.addRecipe(new wm(brainStoneSword(), 1), new Object[] { "B", "B", "S", Character.valueOf('S'), wk.E, Character.valueOf('B'), brainStone() });

    GameRegistry.addRecipe(new wm(brainStoneShovel(), 1), new Object[] { "B", "S", "S", Character.valueOf('S'), wk.E, Character.valueOf('B'), brainStone() });

    GameRegistry.addRecipe(new wm(brainStonePickaxe(), 1), new Object[] { "BBB", " S ", " S ", Character.valueOf('S'), wk.E, Character.valueOf('B'), brainStone() });

    GameRegistry.addRecipe(new wm(brainStoneAxe(), 1), new Object[] { "BB", "BS", " S", Character.valueOf('S'), wk.E, Character.valueOf('B'), brainStone() });

    GameRegistry.addRecipe(new wm(brainStoneHoe(), 1), new Object[] { "BB", " S", " S", Character.valueOf('S'), wk.E, Character.valueOf('B'), brainStone() });

    GameRegistry.addRecipe(new wm(brainProcessor(), 4), new Object[] { "TRT", "SBS", "TRT", Character.valueOf('B'), brainStone(), Character.valueOf('S'), wk.aD, Character.valueOf('T'), apa.aU, Character.valueOf('R'), wk.bc });

    GameRegistry.addRecipe(new wm(brainStoneHelmet(), 1), new Object[] { "BBB", "B B", Character.valueOf('B'), brainStone() });

    GameRegistry.addRecipe(new wm(brainStonePlate(), 1), new Object[] { "B B", "BBB", "BBB", Character.valueOf('B'), brainStone() });

    GameRegistry.addRecipe(new wm(brainStoneLeggings(), 1), new Object[] { "BBB", "B B", "B B", Character.valueOf('B'), brainStone() });

    GameRegistry.addRecipe(new wm(brainStoneBoots(), 1), new Object[] { "B B", "B B", Character.valueOf('B'), brainStone() });
  }

  private static void addSmeltings()
  {
    GameRegistry.addSmelting(dirtyBrainStone().cz, new wm(brainStone(), 1, 0), 3.0F);
  }

  private static void fillTriggerEntities() throws Throwable
  {
    LinkedHashMap brainStoneTriggerEntities = new LinkedHashMap();

    brainStoneTriggerEntities.put("gui.brainstone.player", new Class[] { sq.class });

    brainStoneTriggerEntities.put("gui.brainstone.item", new Class[] { rf.class, sw.class, rh.class, ri.class, rr.class, nc.class });

    brainStoneTriggerEntities.put("gui.brainstone.projectile", new Class[] { ss.class, tb.class, st.class, su.class });

    Map allEntities = mv.d;
    int length = allEntities.size();
    Object[] keys = allEntities.keySet().toArray(new Object[length]);

    for (int i = 0; i < length; i++) {
      Object key = keys[i];
      Class value = (Class)allEntities.get(key);

      if ((value != null) && (ng.class.isAssignableFrom(value)) && (!ng.class.equals(value))) {
        try
        {
          brainStoneTriggerEntities.put("entity." + mv.b((mp)value.getDeclaredConstructor(new Class[] { aab.class }).newInstance(new Object[] { null })) + ".name", new Class[] { value });
        }
        catch (InstantiationException e)
        {
          BSP.debugOnly_printException_noAddon(e, "This is normal and is NOT a problem. (" + value.getName() + ")");
        }
        catch (IllegalArgumentException e)
        {
          BSP.force_printException(e, "This is very unexpected! Actually, it should never happen!\nIt could be bad programming in the BrainStoneMod (I don't think so...) or in the " + value.getName() + ".class. But just report that to me. I'll take care of everything!");

          BSP.throwException(e, "This is very unexpected! Actually, it should never happen!\nIt could be bad programming in the BrainStoneMod (I don't think so...) or in the " + value.getName() + ".class. But just report that to me. I'll take care of everything!");
        }
        catch (IllegalAccessException e)
        {
          BSP.force_printException_noAddon(e, "This is caused by bad programming of the programmer of the " + value.getName() + ".class. Please report this to them!\n\nAdditional Information:\nThis Mod (BrainStoneMod) tried to create a new instance of the " + value.getName() + ".class with the only parameter of the type \"World\" and was denied acces to that!");

          throw new IllegalAccessException(e.getMessage());
        } catch (NoSuchMethodException e) {
          BSP.force_printException_noAddon(e, "This is caused by bad programming of the programmer of the " + value.getName() + ".class. Please report this to them!\n\nAdditional Information:\nThis Mod (BrainStoneMod) tried to create a new instance of the " + value.getName() + ".class with the only parameter of the type \"World\" and there is no such constructor!");

          throw new NoSuchMethodException(e.getMessage());
        } catch (InvocationTargetException e) {
          BSP.force_printException_noAddon(e, "This is caused by bad programming of the programmer of the " + value.getName() + ".class. Please report this to them!");

          throw new InvocationTargetException(e);
        } catch (SecurityException e) {
          BSP.force_printException_noAddon(e, "This is caused by bad programming of the programmer of the " + value.getName() + ".class. Please report this to them!");

          throw new SecurityException(e);
        }
      }
    }

    BlockBrainStoneTrigger.triggerEntities = brainStoneTriggerEntities;
    mods.brainstone.guis.GuiBrainStoneTrigger.triggerEntities = brainStoneTriggerEntities;
    TileEntityBlockBrainStoneTrigger.triggerEntities = brainStoneTriggerEntities;
  }

  private static void generateObjects()
  {
    blocks.put(Integer.valueOf(0), new BlockBrainStone(0, false));
    blocks.put(Integer.valueOf(1), new BlockBrainStone(1, true));
    blocks.put(Integer.valueOf(2), new BlockBrainStoneOre(2));
    blocks.put(Integer.valueOf(3), new BlockBrainStoneBase(getId(3), aif.e).c(2.4F).c("dirtyBrainStone").b(0.5F).a(0.5F).a(ve.b));

    ((apa)blocks.get(Integer.valueOf(3))).cN = -0.1F;
    blocks.put(Integer.valueOf(4), new BlockBrainLightSensor(4));
    blocks.put(Integer.valueOf(5), new BlockBrainStoneTrigger(5));
    blocks.put(Integer.valueOf(6), new BlockBrainLogicBlock(6));
    blocks.put(Integer.valueOf(7), new BlockPulsatingBrainStone(7, false));
    blocks.put(Integer.valueOf(8), new BlockPulsatingBrainStone(8, true));

    items.put(Integer.valueOf(359), new ItemBrainStoneBase(getId(359)).b("brainStoneDust").a(ve.l));

    items.put(Integer.valueOf(360), new ItemBrainStoneBase(getId(360)).b("coalBriquette").a(ve.f));

    items.put(Integer.valueOf(361), new ItemBrainStoneBase(getId(361)).b("brainProcessor").a(ve.f));

    items.put(Integer.valueOf(362), new ItemSwordBrainStone(3, toolBRAINSTONE).b("brainStoneSword"));

    items.put(Integer.valueOf(363), new ItemToolBrainStone(4, toolBRAINSTONE, "spade").b("brainStoneShovel"));

    items.put(Integer.valueOf(364), new ItemToolBrainStone(5, toolBRAINSTONE, "pickaxe").b("brainStonePickaxe"));

    items.put(Integer.valueOf(365), new ItemToolBrainStone(6, toolBRAINSTONE, "axe").b("brainStoneAxe"));

    items.put(Integer.valueOf(366), new ItemHoeBrainStone(7, toolBRAINSTONE).b("brainStoneHoe"));

    items.put(Integer.valueOf(367), new ItemArmorBrainStone(8, armorBRAINSTONE, ModLoader.addArmor("brainstone"), 0).b("brainStoneHelmet"));

    items.put(Integer.valueOf(368), new ItemArmorBrainStone(9, armorBRAINSTONE, ModLoader.addArmor("brainstone"), 1).b("brainStonePlate"));

    items.put(Integer.valueOf(369), new ItemArmorBrainStone(10, armorBRAINSTONE, ModLoader.addArmor("brainstone"), 2).b("brainStoneLeggings"));

    items.put(Integer.valueOf(370), new ItemArmorBrainStone(11, armorBRAINSTONE, ModLoader.addArmor("brainstone"), 3).b("brainStoneBoots"));

    achievements.put(Integer.valueOf(3698), new ju(getId(3698), "What the Hell is that???", 8, 2, brainStoneDust(), jv.o).c());

    achievements.put(Integer.valueOf(3699), new ju(getId(3699), "It lives!", 8, 4, brainStone(), WTHIT()).c());

    achievements.put(Integer.valueOf(3700), new ju(getId(3700), "Intelligent Blocks!", 10, 5, brainLightSensor(), itLives()).c());

    achievements.put(Integer.valueOf(3701), new ju(getId(3701), "Intelligent Tools!", 6, 5, brainStonePickaxe(), itLives()).c());

    achievements.put(Integer.valueOf(3702), new ju(getId(3702), "Logic Block!", 12, 7, brainProcessor(), intelligentBlocks()).c());
  }

  private static final String get_name_de(int id)
  {
    return name_de.containsKey(Integer.valueOf(id)) ? (String)name_de.get(Integer.valueOf(id)) : get_name_en(id);
  }

  private static final String get_name_en(int id)
  {
    return name_en.containsKey(Integer.valueOf(id)) ? (String)name_en.get(Integer.valueOf(id)) : "";
  }

  private static void getIds(FMLPreInitializationEvent event)
  {
    Configuration config = new Configuration(event.getSuggestedConfigurationFile());

    config.load();

    int length = name_en.size();
    Integer[] keys = (Integer[])name_en.keySet().toArray(new Integer[length]);

    for (int i = 0; i < length; i++) {
      int key = keys[i].intValue();
      String name = get_name_en(key).replace(" ", "");

      ids.put(Integer.valueOf(key), Integer.valueOf(config.get(key >= 359 ? "Item" : key >= 3698 ? "Achievement" : "Block", name, 1258 + key).getInt()));
    }

    config.save();
  }

  private static final void registerBlocks()
  {
    int length = blocks.size();
    Integer[] keys = (Integer[])blocks.keySet().toArray(new Integer[length]);

    for (int i = 0; i < length; i++)
      GameRegistry.registerBlock((apa)blocks.get(keys[i]));
  }

  private static final void registerTileEntitys()
  {
    GameRegistry.registerTileEntity(TileEntityBlockBrainLightSensor.class, "TileEntityBlockBrainLightSensor");

    GameRegistry.registerTileEntity(TileEntityBlockBrainStoneTrigger.class, "TileEntityBlockBrainStoneTrigger");

    GameRegistry.registerTileEntity(TileEntityBlockBrainLogicBlock.class, "TileEntityBlockBrainLogicBlock");
  }

  private static Mod getModAnnotation()
  {
    if (annotation == null) {
      return BrainStone.annotation = (Mod)BrainStone.class.getAnnotation(Mod.class);
    }
    return annotation;
  }

  public static final apa brainLightSensor()
  {
    return (apa)blocks.get(Integer.valueOf(4));
  }

  public static final apa brainLogicBlock()
  {
    return (apa)blocks.get(Integer.valueOf(6));
  }

  public static final wk brainProcessor()
  {
    return (wk)items.get(Integer.valueOf(361));
  }

  public static final apa brainStone()
  {
    return (apa)blocks.get(Integer.valueOf(0));
  }

  public static final wk brainStoneAxe()
  {
    return (wk)items.get(Integer.valueOf(365));
  }

  public static final wk brainStoneBoots()
  {
    return (wk)items.get(Integer.valueOf(370));
  }

  public static final wk brainStoneDust()
  {
    return (wk)items.get(Integer.valueOf(359));
  }

  public static final wk brainStoneHelmet()
  {
    return (wk)items.get(Integer.valueOf(367));
  }

  public static final wk brainStoneHoe()
  {
    return (wk)items.get(Integer.valueOf(366));
  }

  public static final wk brainStoneLeggings()
  {
    return (wk)items.get(Integer.valueOf(369));
  }

  public static final apa brainStoneOre()
  {
    return (apa)blocks.get(Integer.valueOf(2));
  }

  public static final apa brainStoneOut()
  {
    return (apa)blocks.get(Integer.valueOf(1));
  }

  public static final wk brainStonePickaxe()
  {
    return (wk)items.get(Integer.valueOf(364));
  }

  public static final wk brainStonePlate()
  {
    return (wk)items.get(Integer.valueOf(368));
  }

  public static final wk brainStoneShovel()
  {
    return (wk)items.get(Integer.valueOf(363));
  }

  public static final wk brainStoneSword()
  {
    return (wk)items.get(Integer.valueOf(362));
  }

  public static final apa brainStoneTrigger()
  {
    return (apa)blocks.get(Integer.valueOf(5));
  }

  public static final wk coalBriquette()
  {
    return (wk)items.get(Integer.valueOf(360));
  }

  public static final apa dirtyBrainStone()
  {
    return (apa)blocks.get(Integer.valueOf(3));
  }

  public static final int getId(int id)
  {
    return ids.containsKey(Integer.valueOf(id)) ? ((Integer)ids.get(Integer.valueOf(id))).intValue() : 0;
  }

  public static final ju intelligentBlocks()
  {
    return (ju)achievements.get(Integer.valueOf(3700));
  }

  public static final ju intelligentTools()
  {
    return (ju)achievements.get(Integer.valueOf(3701));
  }

  public static final ju itLives()
  {
    return (ju)achievements.get(Integer.valueOf(3699));
  }

  public static final ju logicBlock()
  {
    return (ju)achievements.get(Integer.valueOf(3702));
  }

  public static final apa pulsatingBrainStone()
  {
    return (apa)blocks.get(Integer.valueOf(7));
  }

  public static final apa pulsatingBrainStoneEffect()
  {
    return (apa)blocks.get(Integer.valueOf(8));
  }

  public static final ju WTHIT()
  {
    return (ju)achievements.get(Integer.valueOf(3698));
  }

  static
  {
    name_en.put(Integer.valueOf(0), "Brain Stone");
    name_en.put(Integer.valueOf(1), "Brain Stone Out");
    name_en.put(Integer.valueOf(2), "Brain Stone Ore");
    name_en.put(Integer.valueOf(3), "Dirty Brain Stone");
    name_en.put(Integer.valueOf(4), "Brain Light Sensor");
    name_en.put(Integer.valueOf(5), "Brain Stone Trigger");
    name_en.put(Integer.valueOf(6), "Brain Logic Block");
    name_en.put(Integer.valueOf(7), "Pulsating Brain Stone");
    name_en.put(Integer.valueOf(8), "Pulsating Brain Stone Effect");

    name_en.put(Integer.valueOf(359), "Brain Stone Dust");
    name_en.put(Integer.valueOf(360), "Coal Briquette");
    name_en.put(Integer.valueOf(361), "Brain Processor");
    name_en.put(Integer.valueOf(362), "Brain Stone Sword");
    name_en.put(Integer.valueOf(363), "Brain Stone Shovel");
    name_en.put(Integer.valueOf(364), "Brain Stone Pickaxe");
    name_en.put(Integer.valueOf(365), "Brain Stone Axe");
    name_en.put(Integer.valueOf(366), "Brain Stone Hoe");
    name_en.put(Integer.valueOf(367), "Brain Stone Helmet");
    name_en.put(Integer.valueOf(368), "Brain Stone Chestplate");
    name_en.put(Integer.valueOf(369), "Brain Stone Leggings");
    name_en.put(Integer.valueOf(370), "Brain Stone Boots");

    name_en.put(Integer.valueOf(3698), "WTHIT");
    name_en.put(Integer.valueOf(3699), "itLives");
    name_en.put(Integer.valueOf(3700), "intelligentBlocks");
    name_en.put(Integer.valueOf(3701), "intelligentTools");
    name_en.put(Integer.valueOf(3702), "logicBlock");

    name_de = new HashMap();

    name_de.put(Integer.valueOf(0), "Hirnstein");
    name_de.put(Integer.valueOf(1), "Ausgeschalteter Hirnstein");
    name_de.put(Integer.valueOf(2), "Hirnsteinerz");
    name_de.put(Integer.valueOf(3), "Dreckiger Hirnstein");
    name_de.put(Integer.valueOf(4), "Hirnlichtsensor");
    name_de.put(Integer.valueOf(5), "Hirnsteinauslöser");
    name_de.put(Integer.valueOf(6), "Hirnlogiblock");
    name_de.put(Integer.valueOf(7), "Pulsierender Hirnstein");
    name_de.put(Integer.valueOf(8), "Pulsierender Hirnstein-Effekt");

    name_de.put(Integer.valueOf(359), "Hirnsteinstaub");
    name_de.put(Integer.valueOf(360), "Kohlebrikett");
    name_de.put(Integer.valueOf(361), "Hirnprozessor");
    name_de.put(Integer.valueOf(362), "Hirnsteinschwert");
    name_de.put(Integer.valueOf(363), "Hirnsteinschaufel");
    name_de.put(Integer.valueOf(364), "Hirnsteinspitzhacke");
    name_de.put(Integer.valueOf(365), "Hirnsteinaxt");
    name_de.put(Integer.valueOf(366), "Hirnsteinfeldhacke");
    name_de.put(Integer.valueOf(367), "Hirnsteinhelm");
    name_de.put(Integer.valueOf(368), "Hirnsteinbrustplatte");
    name_de.put(Integer.valueOf(369), "Hirnsteinhose");
    name_de.put(Integer.valueOf(370), "Hirsteinstiefel");

    localizations_en = new HashMap();

    localizations_en.put("gui.brainstone.item", "Item/Object");
    localizations_en.put("gui.brainstone.player", "Player");
    localizations_en.put("gui.brainstone.projectile", "Projectile");
    localizations_en.put("gui.brainstone.help", "Help");
    localizations_en.put("gui.brainstone.invertOutput", "Invert Output");
    localizations_en.put("gui.brainstone.warning1", "This gate will not operate until it is wired correctly!");

    localizations_en.put("gui.brainstone.warning2", "You should wire all inputs to make sure the gate works properly!");

    localizations_en.put("gui.brainstone.help.gate0", "---------AND-Gate---------\nA device where the output is on when all inputs are on.");

    localizations_en.put("gui.brainstone.help.gate1", "----------OR-Gate---------\nA device where the output is on when at least one of the inputs are on.");

    localizations_en.put("gui.brainstone.help.gate2", "---------XOR-Gate---------\nA device where the output switches for every input that is on.");

    localizations_en.put("gui.brainstone.help.gate3", "--------Implies-Gate-------\nReturns false only if the implication A -> B is false. That is, if the antecedent A is true, but the consequent B is false. It is often read \"if A then B.\" It is the logical equivalent of \"B or NOT A\".");

    localizations_en.put("gui.brainstone.help.gate4", "---------NOT-Gate---------\nA device that inverts the input, as such it is also called an \"Inverter\" Gate.");

    localizations_en.put("gui.brainstone.help.gate5", "-------RS-NOR-Latch-------\nA device where Q will stay on forever after input is received by S. Q can be turned off again by a signal received by R. S and R shuouldn't be on at the same time!");

    localizations_en.put("gui.brainstone.help.gate6", "--------D-Flip-Flop--------\nA D flip-flop, or \"data\" flip-flop, sets the output to D only when its clock (input C) is on.");

    localizations_en.put("gui.brainstone.help.gate7", "--------T-Flip-Flop--------\nT flip-flops are also known as \"toggles.\" Whenever T changes from OFF to ON, the output will toggle its state.");

    localizations_en.put("gui.brainstone.help.gate8", "--------JK-Flip-Flop-------\nIf the input J = 1 and the input K = 0, the output Q = 1. When J = 0 and K = 1, the output Q = 0. If both J and K are 0, then the JK flip-flop maintains its previous state. If both are 1, the output will complement itself.");

    localizations_de = new HashMap();

    localizations_de.put("gui.brainstone.item", "Item/Objekt");
    localizations_de.put("gui.brainstone.player", "Spieler");
    localizations_de.put("gui.brainstone.projectile", "Projektil");
    localizations_de.put("gui.brainstone.help", "Hilfe");
    localizations_de.put("gui.brainstone.invertOutput", "Ausgang invertieren");

    localizations_de.put("gui.brainstone.warning1", "Dieses Gate wird nicht arbeiten, bis es korrekt angeschlossen ist!");

    localizations_de.put("gui.brainstone.warning2", "Sie sollten alle Eingänge anschießen, damit das Gate richtig arbeiten kann!");

    localizations_de.put("gui.brainstone.help.gate0", "---------AND-Gate---------\nDas AND-Gate ist an, wenn alle Eingänge an sind.");

    localizations_de.put("gui.brainstone.help.gate1", "----------OR-Gate---------\nDas OR-Gate ist an, wenn mindestens ein Eingang an ist.");

    localizations_de.put("gui.brainstone.help.gate2", "---------XOR-Gate---------\nDer Ausgang schaltet für jeden aktiven Eingang um.");

    localizations_de.put("gui.brainstone.help.gate3", "--------Implies-Gate-------\nDer Ausgang ist nur falsch (aus), wenn der Schluss A -> B falsch ist. Das trifft zu, wenn die Bedingung A wahr ist, die Konsequenz B falsch. Man kann die Beziehung verstehen als \"Wenn A dann B\". Entspricht \"B oder nicht A\".");

    localizations_de.put("gui.brainstone.help.gate4", "---------NOT-Gate---------\nAuch bekannt als Umkehrer (in der Elektrotechnik üblicherweise \"Inverter\" genannt). Dieses Gate kehrt den Eingang um.");

    localizations_de.put("gui.brainstone.help.gate5", "-------RS-NOR-Latch-------\nDer Ausgang wird eingeschaltet, wenn \"S\" eingeschaltet wird (und danach wieder aus) ausgeschaltet, wenn \"R\" eingeschaltet wird. Beide sollten niemals gleichzeitig an sein!");

    localizations_de.put("gui.brainstone.help.gate6", "--------D-Flip-Flop--------\nEin D-Flip-Flop oder auch Daten-Flip-Flop setzt seinen Ausgang nur auf den Zustand seines Einganges D, wenn die Clock (Eingang C) an ist.");

    localizations_de.put("gui.brainstone.help.gate7", "--------T-Flip-Flop--------\nDer T-Flip-Flop ist ein Speicher, der umspringt, wenn das eingehende Signal (T) angeht.");

    localizations_de.put("gui.brainstone.help.gate8", "--------JK-Flip-Flop-------\nWenn der Eingang J = 1 and der Eingang K = 0, wird der Ausgang Q = 1. Wenn J = 0 and K = 1, dann wird Q = 0. Wenn J und K 0 sind, dann behält das Gate seinen Zustand. Wenn beide 1 sind, dann kehrt sich der Ausgang um.");

    achievement_en = new HashMap();

    achievement_en.put(Integer.valueOf(0), new String[] { "What the Hell is that???", "You have to find a strange green powder." });

    achievement_en.put(Integer.valueOf(1), new String[] { "It lives!", "Crafting and a Smelting is the key!" });

    achievement_en.put(Integer.valueOf(2), new String[] { "Intelligent Blocks", "Make usefull intelligent Blocks out of this green stone!" });

    achievement_en.put(Integer.valueOf(3), new String[] { "Intelligent Tools!", "Make Tools out of this green stone!" });

    achievement_en.put(Integer.valueOf(4), new String[] { "Logic Block", "First make a processor. Then a Logic Block!" });

    achievement_de = new HashMap();

    achievement_de.put(Integer.valueOf(0), new String[] { "Was zur Hölle ist das???", "Du must ein seltsames grünes Pulver finden." });

    achievement_de.put(Integer.valueOf(1), new String[] { "Es lebt!", "Craften und Schmelzen ist die Lösung!" });

    achievement_de.put(Integer.valueOf(2), new String[] { "Intelligente Blöcke", "Stelle nützliche intelligente Blöcke aus diesem grünen Stein her!" });

    achievement_de.put(Integer.valueOf(3), new String[] { "Intelligente Werkzeuge", "Stelle Werkzeuge aus diesem grünen Stein her!" });

    achievement_de.put(Integer.valueOf(4), new String[] { "Logikblock!", "Mache als erstes einen Prozessor. Dann einen Logikblock!" });
  }
}