import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class mod_BrainStone extends BaseMod
{
  public static final Logger logger = Logger.getLogger("mod_BrainStone");
  public static int renderBrainStoneSensorsID;
  public static pb brainStone;
  public static pb brainStoneOut;
  public static pb brainStoneOre;
  public static pb dirtyBrainStone;
  public static pb brainLightSensor;
  public static pb brainStoneTrigger;
  public static pb brainLogicBlock;
  public static yr brainStoneDust;
  public static yr coalBriquette;
  public static yr brainProcessor;
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
  public static int brainLogicBlockOnTexture;
  public static int brainLogicBlockOffTexture;
  public static int brainLogicBlockNotConnectedTexture;
  public static int brainStoneSwordTexture;
  public static int brainStoneShovelTexture;
  public static int brainStonePickaxeTexture;
  public static int brainStoneAxeTexture;
  public static int brainStoneHoeTexture;
  public static int brainProcessorTexture;
  private static aan[] creavtivContainer;
  public static aeb WTHIT;
  public static aeb itLives;
  public static aeb intelligentBlocks;
  public static aeb intelligentTools;
  public static aeb logicBlock;
  private static String de;

  public void load()
  {
    ModLoader.setInGameHook(this, true, true);
    ModLoader.setInGUIHook(this, true, true);

    renderBrainStoneSensorsID = ModLoader.getUniqueBlockModelID(this, true);

    brainStoneTexture = ModLoader.addOverride("/terrain.png", "/BrainStone/brainStone.png");
    brainStoneOutTexture = ModLoader.addOverride("/terrain.png", "/BrainStone/brainStoneOut.png");
    brainStoneOreTexture = ModLoader.addOverride("/terrain.png", "/BrainStone/brainStoneOre.png");
    dirtyBrainStoneTexture = ModLoader.addOverride("/terrain.png", "/BrainStone/dirtyBrainStone.png");
    brainLightSensorTexture = ModLoader.addOverride("/terrain.png", "/BrainStone/brainLightSensor.png");
    brainStoneTriggerTexture = ModLoader.addOverride("/terrain.png", "/BrainStone/brainStoneTrigger.png");

    brainLogicBlockOnTexture = ModLoader.addOverride("/terrain.png", "/BrainStone/brainLogicBlockOn.png");
    brainLogicBlockOffTexture = ModLoader.addOverride("/terrain.png", "/BrainStone/brainLogicBlockOff.png");
    brainLogicBlockNotConnectedTexture = ModLoader.addOverride("/terrain.png", "/BrainStone/brainLogicBlockNotConnected.png");
    brainStoneSwordTexture = ModLoader.addOverride("/gui/items.png", "/BrainStone/brainStoneSword.png");
    brainStoneShovelTexture = ModLoader.addOverride("/gui/items.png", "/BrainStone/brainStoneShovel.png");
    brainStonePickaxeTexture = ModLoader.addOverride("/gui/items.png", "/BrainStone/brainStonePickaxe.png");
    brainStoneAxeTexture = ModLoader.addOverride("/gui/items.png", "/BrainStone/brainStoneAxe.png");
    brainStoneHoeTexture = ModLoader.addOverride("/gui/items.png", "/BrainStone/brainStoneHoe.png");
    brainProcessorTexture = ModLoader.addOverride("/gui/items.png", "/BrainStone/brainProcessor.png");

    brainStone = new BlockBrainStone(210, brainStoneTexture, false).c(3.0F).a("brainStone").b(1.0F).a(1.0F);
    brainStoneOut = new BlockBrainStone(211, brainStoneOutTexture, true).c(3.0F).a("brainStoneOut").b(1.0F).a(0.0F);
    brainStoneOre = new BlockBrainStoneOre(212, brainStoneOreTexture).c(2.0F).a("brainStoneOre").b(0.25F).a(0.3F);
    dirtyBrainStone = new pb(213, dirtyBrainStoneTexture, acn.e).c(2.4F).a("dirtyBrainStone").b(0.5F).a(0.5F);
    brainLightSensor = new BlockBrainLightSensor(214, brainLightSensorTexture).c(2.4F).b(0.5F).a("brainLightSensor");
    brainStoneTrigger = new BlockBrainStoneTrigger(215, brainStoneTriggerTexture).c(2.4F).b(0.5F).a("brainStoneTrigger");

    brainLogicBlock = new BlockBrainLogicBlock(217, brainStoneTriggerTexture).c(3.0F).a("brainLogicBlock").b(1.0F).k();

    brainStoneOre.cc = 0.2F;
    dirtyBrainStone.cc = -0.1F;

    creavtivContainer = new aan[] { new aan(brainStone), new aan(brainStoneOre), new aan(dirtyBrainStone), new aan(brainLightSensor), new aan(brainStoneTrigger), new aan(brainLogicBlock) };

    brainStoneDust = new yr(1358).e(ModLoader.addOverride("/gui/items.png", "/BrainStone/brainStoneDust.png")).a("brainStoneDust");
    coalBriquette = new yr(1359).e(ModLoader.addOverride("/gui/items.png", "/BrainStone/coalBriquette.png")).a("coalBriquette");
    brainProcessor = new yr(1369).e(brainProcessorTexture).a("brainProcessor");

    brainStoneSword = new ItemSwordBrainStone(1364, EnumToolMaterialBrainStone.BRAINSTONE).e(brainStoneSwordTexture).a("brainStoneSword");
    brainStoneShovel = new ItemSpadeBrainStone(1365, EnumToolMaterialBrainStone.BRAINSTONE).e(brainStoneShovelTexture).a("brainStoneShovel");
    brainStonePickaxe = new ItemPickaxeBrainStone(1366, EnumToolMaterialBrainStone.BRAINSTONE).e(brainStonePickaxeTexture).a("brainStonePickaxe");
    brainStoneAxe = new ItemAxeBrainStone(1367, EnumToolMaterialBrainStone.BRAINSTONE).e(brainStoneAxeTexture).a("brainStoneAxe");
    brainStoneHoe = new ItemHoeBrainStone(1368, EnumToolMaterialBrainStone.BRAINSTONE).e(brainStoneHoeTexture).a("brainStoneHoe");

    WTHIT = new aeb(1004, "What the Hell is that???", 8, 2, brainStoneDust, dp.o).d();
    itLives = new aeb(1005, "It lives!", 8, 4, brainStone, WTHIT).d();
    intelligentBlocks = new aeb(1006, "Intelligent Blocks!", 10, 5, brainLightSensor, itLives).d();
    intelligentTools = new aeb(1007, "Intelligent Tools!", 6, 5, brainStonePickaxe, itLives).d();
    logicBlock = new aeb(1008, "Logic Block!", 12, 7, brainProcessor, intelligentBlocks).d();

    de = "de_DE";

    ModLoader.registerTileEntity(TileEntityBlockBrainLightSensor.class, "TileEntityBlockBrainLightSensor");
    ModLoader.registerTileEntity(TileEntityBlockBrainStoneTrigger.class, "TileEntityBlockBrainStoneTrigger");
    ModLoader.registerTileEntity(TileEntityBlockBrainLogicBlock.class, "TileEntityBlockBrainLogicBlock");

    ModLoader.registerTileEntity(TileEntityBlockBrainLogicBlock.class, "TileEntityBlockBrainLogicBlock", new TileEntityBlockBrainLogicBlockRenderer());

    ModLoader.registerBlock(brainStone);
    ModLoader.registerBlock(brainStoneOut);
    ModLoader.registerBlock(brainStoneOre);
    ModLoader.registerBlock(dirtyBrainStone);
    ModLoader.registerBlock(brainLightSensor);
    ModLoader.registerBlock(brainStoneTrigger);

    ModLoader.registerBlock(brainLogicBlock);

    ModLoader.addName(brainStone, "Brain Stone");
    ModLoader.addName(brainStoneOut, "Brain Stone");
    ModLoader.addName(brainStoneOre, "Brain Stone Ore");
    ModLoader.addName(dirtyBrainStone, "Dirty Brain Stone");
    ModLoader.addName(brainLightSensor, "Brain Light Sensor");
    ModLoader.addName(brainStoneTrigger, "Brain Stone Trigger");

    ModLoader.addName(brainLogicBlock, "Brain Logic Block");

    ModLoader.addName(brainStoneDust, "Brain Stone Dust");
    ModLoader.addName(coalBriquette, "Coal Briquette");
    ModLoader.addName(brainProcessor, "Brain Processor");

    ModLoader.addName(brainStoneSword, "Brain Stone Sword");
    ModLoader.addName(brainStoneShovel, "Brain Stone Shovel");
    ModLoader.addName(brainStonePickaxe, "Brain Stone Pickaxe");
    ModLoader.addName(brainStoneAxe, "Brain Stone Axe");
    ModLoader.addName(brainStoneHoe, "Brain Stone Hoe");

    ModLoader.addAchievementDesc(WTHIT, "What the Hell is that???", "You have to find a strange green powder.");
    ModLoader.addAchievementDesc(itLives, "It lives!", "Crafting and a Smelting is the key!");
    ModLoader.addAchievementDesc(intelligentBlocks, "Intelligent Blocks!", "Make usefull intelligent Blocks out of this green stone!");
    ModLoader.addAchievementDesc(intelligentTools, "Intelligent Tools!", "Make Tools out of this green stone!");
    ModLoader.addAchievementDesc(logicBlock, "Logic Block!", "First make a processor. Then a Logic Block!");

    ModLoader.addLocalization("gui.brainstone.item", "Item/Object");
    ModLoader.addLocalization("gui.brainstone.animal", "Animal");
    ModLoader.addLocalization("gui.brainstone.monster", "Monster");
    ModLoader.addLocalization("gui.brainstone.nether", "Nether Mob");
    ModLoader.addLocalization("gui.brainstone.player", "Player");
    ModLoader.addLocalization("gui.brainstone.help", "Help");
    ModLoader.addLocalization("gui.brainstone.invertOutput", "Invert Output");
    ModLoader.addLocalization("gui.brainstone.warning1", "This gate will not operate until it is wired correctly!");
    ModLoader.addLocalization("gui.brainstone.warning2", "You should wire all inputs to make sure the gate works properly!");
    ModLoader.addLocalization("gui.brainstone.help.gate0", "---------AND-Gate---------\nA device where the output is on when all inputs are on.");
    ModLoader.addLocalization("gui.brainstone.help.gate1", "----------OR-Gate---------\nA device where the output is on when at least one of the inputs are on.");
    ModLoader.addLocalization("gui.brainstone.help.gate2", "---------XOR-Gate---------\nA device where the output switches for every input that is on.");
    ModLoader.addLocalization("gui.brainstone.help.gate3", "--------Implies-Gate-------\nReturns false only if the implication A -> B is false. That is, if the antecedent A is true, but the consequent B is false. It is often read \"if A then B.\" It is the logical equivalent of \"B or NOT A\".");
    ModLoader.addLocalization("gui.brainstone.help.gate4", "---------NOT-Gate---------\nA device that inverts the input, as such it is also called an \"Inverter\" Gate.");
    ModLoader.addLocalization("gui.brainstone.help.gate5", "-------RS-NOR-Latch-------\nA device where Q will stay on forever after input is received by S. Q can be turned off again by a signal received by R. S and R shuouldn't be on at the same time!");
    ModLoader.addLocalization("gui.brainstone.help.gate6", "--------D-Flip-Flop--------\nA D flip-flop, or \"data\" flip-flop, sets the output to D only when its clock (input C) is on.");
    ModLoader.addLocalization("gui.brainstone.help.gate7", "--------T-Flip-Flop--------\nT flip-flops are also known as \"toggles.\" Whenever T changes from OFF to ON, the output will toggle its state.");
    ModLoader.addLocalization("gui.brainstone.help.gate8", "--------JK-Flip-Flop-------\nIf the input J = 1 and the input K = 0, the output Q = 1. When J = 0 and K = 1, the output Q = 0. If both J and K are 0, then the JK flip-flop maintains its previous state. If both are 1, the output will complement itself.");

    ModLoader.addName(brainStone, de, "Hirnstein");
    ModLoader.addName(brainStoneOut, de, "Hirnstein");
    ModLoader.addName(brainStoneOre, de, "Hirnsteinerz");
    ModLoader.addName(dirtyBrainStone, de, "Dreckiger Hirnstein");
    ModLoader.addName(brainLightSensor, de, "Hirnlichtsensor");
    ModLoader.addName(brainStoneTrigger, de, "Hirnsteinausl√∂ser");

    ModLoader.addName(brainLogicBlock, de, "Hirnlogikblock");

    ModLoader.addName(brainStoneDust, de, "Hirnsteinstaub");
    ModLoader.addName(coalBriquette, de, "Kohlebrikett");
    ModLoader.addName(brainProcessor, de, "Hirnprozessor");

    ModLoader.addName(brainStoneSword, de, "Hirnsteinschwert");
    ModLoader.addName(brainStoneShovel, de, "Hirnsteinschaufel");
    ModLoader.addName(brainStonePickaxe, de, "Hirnsteinspitzhacke");
    ModLoader.addName(brainStoneAxe, de, "Hirnsteinaxt");
    ModLoader.addName(brainStoneHoe, de, "Hirnsteinfeldhacke");

    addAchievementDesc(WTHIT, de, "Was zur H√∂lle ist das???", "Du must ein seltsames gr¬∏nes Pulver finden.");
    addAchievementDesc(itLives, de, "Es lebt!", "Craften und Schmelzen ist die L√∂sung!");
    addAchievementDesc(intelligentBlocks, de, "Intelligente Bl√∂cke!", "Stelle n√ºtzliche intelligente Bl√∂cke aus diesem gr√ºnen Stein her!");
    addAchievementDesc(intelligentTools, de, "Intelligente Werkzeuge", "Stelle Werkzeuge aus diesem gr√ºnen Stein her!");
    addAchievementDesc(logicBlock, de, "Logikblock!", "Mache als erstes einen Prozessor. Dann einen Logikblock!");

    ModLoader.addLocalization("gui.brainstone.item", de, "Item/Objekt");
    ModLoader.addLocalization("gui.brainstone.animal", de, "Tier");
    ModLoader.addLocalization("gui.brainstone.monster", de, "Monster");
    ModLoader.addLocalization("gui.brainstone.nether", de, "Nether Wesen");
    ModLoader.addLocalization("gui.brainstone.player", de, "Spieler");
    ModLoader.addLocalization("gui.brainstone.help", de, "Hilfe");
    ModLoader.addLocalization("gui.brainstone.invertOutput", de, "Ausgang invertieren");
    ModLoader.addLocalization("gui.brainstone.warning1", de, "Dieses Gate wird nicht arbeiten, bis es korrekt angeschlossen ist!");
    ModLoader.addLocalization("gui.brainstone.warning2", de, "Sie sollten alle Eing√§nge anschie√üen, damit das Gate richtig arbeiten kann!");
    ModLoader.addLocalization("gui.brainstone.help.gate0", de, "---------AND-Gate---------\nDas AND-Gate ist an, wenn alle Eing√§nge an sind.");
    ModLoader.addLocalization("gui.brainstone.help.gate1", de, "----------OR-Gate---------\nDas OR-Gate ist an, wenn mindestens ein Eingang an ist.");
    ModLoader.addLocalization("gui.brainstone.help.gate2", de, "---------XOR-Gate---------\nDer Ausgang schaltet f√ºr jeden aktiven Eingang um.");
    ModLoader.addLocalization("gui.brainstone.help.gate3", de, "--------Implies-Gate-------\nDer Ausgang ist nur falsch (aus), wenn der Schluss A -> B falsch ist. Das trifft zu, wenn die Bedingung A wahr ist, die Konsequenz B falsch. Man kann die Beziehung verstehen als \"Wenn A dann B\". Entspricht \"B oder nicht A\".");
    ModLoader.addLocalization("gui.brainstone.help.gate4", de, "---------NOT-Gate---------\nAuch bekannt als Umkehrer (in der Elektrotechnik √ºblicherweise \"Inverter\" genannt). Dieses Gate kehrt den Eingang um.");
    ModLoader.addLocalization("gui.brainstone.help.gate5", de, "-------RS-NOR-Latch-------\nDer Ausgang wird eingeschaltet, wenn \"S\" eingeschaltet wird (und danach wieder aus) ausgeschaltet, wenn \"R\" eingeschaltet wird. Beide sollten niemals gleichzeitig an sein!");
    ModLoader.addLocalization("gui.brainstone.help.gate6", de, "--------D-Flip-Flop--------\nEin D-Flip-Flop oder auch Daten-Flip-Flop setzt seinen Ausgang nur auf den Zustand seines Einganges D, wenn die Clock (Eingang C) an ist.");
    ModLoader.addLocalization("gui.brainstone.help.gate7", de, "--------T-Flip-Flop--------\nDer T-Flip-Flop ist ein Speicher, der umspringt, wenn das eingehende Signal (T) angeht.");
    ModLoader.addLocalization("gui.brainstone.help.gate8", de, "--------JK-Flip-Flop-------\nWenn der Eingang J = 1 and der Eingang K = 0, wird der Ausgang Q = 1. Wenn J = 0 and K = 1, dann wird Q = 0. Wenn J und K 0 sind, dann beh√§lt das Gate seinen Zustand. Wenn beide 1 sind, dann kehrt sich der Ausgang um.");

    ModLoader.addRecipe(new aan(dirtyBrainStone, 1), new Object[] { "XX", "XX", Character.valueOf('X'), brainStoneDust });

    for (int i = 0; i <= 9; i++)
    {
      ModLoader.addShapelessRecipe(new aan(coalBriquette, 1), new Object[] { new aan(yr.m, 1, i > 0 ? 1 : 0), new aan(yr.m, 1, i > 1 ? 1 : 0), new aan(yr.m, 1, i > 2 ? 1 : 0), new aan(yr.m, 1, i > 3 ? 1 : 0), new aan(yr.m, 1, i > 4 ? 1 : 0), new aan(yr.m, 1, i > 5 ? 1 : 0), new aan(yr.m, 1, i > 6 ? 1 : 0), new aan(yr.m, 1, i > 7 ? 1 : 0), new aan(yr.m, 1, i > 8 ? 1 : 0) });
    }

    ModLoader.addRecipe(new aan(brainLightSensor, 1), new Object[] { "XGX", "XBX", "XRX", Character.valueOf('X'), pb.t, Character.valueOf('G'), pb.M, Character.valueOf('B'), brainStone, Character.valueOf('R'), yr.aC });

    ModLoader.addRecipe(new aan(brainStoneTrigger, 1), new Object[] { "XXX", "RRR", "XBX", Character.valueOf('X'), pb.t, Character.valueOf('B'), brainStone, Character.valueOf('R'), yr.aC });

    ModLoader.addRecipe(new aan(brainLogicBlock, 1), new Object[] { "SRS", "RPR", "SRS", Character.valueOf('S'), pb.t, Character.valueOf('P'), brainProcessor, Character.valueOf('R'), yr.aC });

    ModLoader.addRecipe(new aan(brainStoneSword, 1), new Object[] { "B", "B", "S", Character.valueOf('S'), yr.D, Character.valueOf('B'), brainStone });

    ModLoader.addRecipe(new aan(brainStoneShovel, 1), new Object[] { "B", "S", "S", Character.valueOf('S'), yr.D, Character.valueOf('B'), brainStone });

    ModLoader.addRecipe(new aan(brainStonePickaxe, 1), new Object[] { "BBB", " S ", " S ", Character.valueOf('S'), yr.D, Character.valueOf('B'), brainStone });

    ModLoader.addRecipe(new aan(brainStoneAxe, 1), new Object[] { "BB", "BS", " S", Character.valueOf('S'), yr.D, Character.valueOf('B'), brainStone });

    ModLoader.addRecipe(new aan(brainStoneAxe, 1), new Object[] { "BB", "SB", "S ", Character.valueOf('S'), yr.D, Character.valueOf('B'), brainStone });

    ModLoader.addRecipe(new aan(brainStoneHoe, 1), new Object[] { "BB", " S", " S", Character.valueOf('S'), yr.D, Character.valueOf('B'), brainStone });

    ModLoader.addRecipe(new aan(brainStoneHoe, 1), new Object[] { "BB", "S ", "S ", Character.valueOf('S'), yr.D, Character.valueOf('B'), brainStone });

    ModLoader.addRecipe(new aan(brainProcessor, 4), new Object[] { "TRT", "SBS", "TRT", Character.valueOf('B'), brainStone, Character.valueOf('S'), yr.aC, Character.valueOf('T'), pb.aQ, Character.valueOf('R'), yr.bb });

    ModLoader.addSmelting(dirtyBrainStone.bO, new aan(brainStone, 1, 0));

    if (getVersion().indexOf("release") == -1)
    {
      ModLoader.addRecipe(new aan(brainStoneOre, 1), new Object[] { "X", Character.valueOf('X'), pb.v });

      ModLoader.addRecipe(new aan(brainStone, 1), new Object[] { "X", Character.valueOf('X'), pb.t });

      ModLoader.addRecipe(new aan(brainLightSensor, 1), new Object[] { "X", Character.valueOf('X'), pb.E });

      ModLoader.addRecipe(new aan(dirtyBrainStone, 1), new Object[] { "X", Character.valueOf('X'), pb.w });

      ModLoader.addRecipe(new aan(brainStoneTrigger, 1), new Object[] { "X", Character.valueOf('X'), pb.u });
    }
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

  public void takenFromCrafting(yw entityplayer, aan itemstack, io iinventory)
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
    else if (ID == brainLogicBlock.bO)
    {
      entityplayer.a(logicBlock, 1);
    }
  }

  public boolean onTickInGame(float f, Minecraft mc)
  {
    aan itemstack = mc.h.ap.b[3];

    if (itemstack != null)
    {
      if (itemstack.c == yr.V.bQ)
      {
        if (mc.h.z)
        {
          mc.h.r *= 1.2D;
          mc.h.s *= 1.2D;
          mc.h.t *= 1.2D;
        }
      }
    }

    return true;
  }

  public boolean onTickInGUI(float f, Minecraft mc, vp gui)
  {
    if ((gui instanceof sr))
    {
      dd contain = ((gb)gui).d;
      List list = ((os)contain).a;

      if (!list.contains(creavtivContainer[0]))
      {
        int size = list.size(); int index = size;
        aan tmp = null;

        for (int i = 0; (index == size) && (i < size); i++)
        {
          tmp = (aan)list.get(i);

          if ((tmp != null) && (tmp.c == pb.bL.bO)) {
            index = i + 1;
          }
        }
        for (int i = creavtivContainer.length - 1; i >= 0; i--)
        {
          list.add(index, creavtivContainer[i]);
        }
      }
    }

    return true;
  }

  public boolean renderWorldBlock(vl renderblocks, ali iblockaccess, int i, int j, int k, pb block, int l)
  {
    if (l == renderBrainStoneSensorsID)
    {
      return renderBrainStoneSensors(block, i, j, k, renderblocks, iblockaccess);
    }

    return false;
  }

  public boolean renderBrainStoneSensors(pb block, int i, int j, int k, vl renderblocks, ali iblockaccess)
  {
    block.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    renderblocks.o(block, i, j, k);

    BlockBrainStoneHiders block_conv = (BlockBrainStoneHiders)block;

    if (block_conv != null)
    {
      if (block_conv.isGrass(iblockaccess, i, j, k))
      {
        block.a(0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        renderblocks.o(block, i, j, k);

        block.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      }
    }

    return true;
  }

  public void renderInvBlock(vl renderblocks, pb par1Block, int par2, int par3)
  {
    if (par3 == renderBrainStoneSensorsID)
    {
      renderBrainStoneSensorsInv(renderblocks, par1Block, par2, par3);
    }
  }

  public void renderBrainStoneSensorsInv(vl renderblocks, pb par1Block, int par2, int par3)
  {
    adz tessellator = adz.a;

    if (renderblocks.c)
    {
      int i = par1Block.d(par2);

      float f = (i >> 16 & 0xFF) / 255.0F;
      float f2 = (i >> 8 & 0xFF) / 255.0F;
      float f6 = (i & 0xFF) / 255.0F;
      GL11.glColor4f(f, f2, f6, 1.0F);
    }

    par1Block.h();
    GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
    tessellator.b();
    tessellator.b(0.0F, -1.0F, 0.0F);
    renderblocks.a(par1Block, 0.0D, 0.0D, 0.0D, par1Block.a(0, par2));
    tessellator.a();

    if (renderblocks.c)
    {
      int k1 = par1Block.d(par2);
      float f5 = (k1 >> 16 & 0xFF) / 255.0F;
      float f7 = (k1 >> 8 & 0xFF) / 255.0F;
      float f8 = (k1 & 0xFF) / 255.0F;
      GL11.glColor4f(f5 * par3, f7 * par3, f8 * par3, 1.0F);
    }

    tessellator.b();
    tessellator.b(0.0F, 1.0F, 0.0F);
    renderblocks.b(par1Block, 0.0D, 0.0D, 0.0D, par1Block.a(1, par2));
    tessellator.a();

    if (renderblocks.c)
    {
      GL11.glColor4f(par3, par3, par3, 1.0F);
    }

    tessellator.b();
    tessellator.b(0.0F, 0.0F, -1.0F);
    renderblocks.c(par1Block, 0.0D, 0.0D, 0.0D, par1Block.a(2, par2));
    tessellator.a();
    tessellator.b();
    tessellator.b(0.0F, 0.0F, 1.0F);
    renderblocks.d(par1Block, 0.0D, 0.0D, 0.0D, par1Block.a(3, par2));
    tessellator.a();
    tessellator.b();
    tessellator.b(-1.0F, 0.0F, 0.0F);
    renderblocks.e(par1Block, 0.0D, 0.0D, 0.0D, par1Block.a(4, par2));
    tessellator.a();
    tessellator.b();
    tessellator.b(1.0F, 0.0F, 0.0F);
    renderblocks.f(par1Block, 0.0D, 0.0D, 0.0D, par1Block.a(5, par2));
    tessellator.a();
    GL11.glTranslatef(0.5F, 0.5F, 0.5F);
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
      logger.throwing("BrainStone", "AddAchievementDesc", illegalargumentexception);
      throwException(illegalargumentexception);
    }
    catch (SecurityException securityexception)
    {
      logger.throwing("BrainStone", "AddAchievementDesc", securityexception);
      throwException(securityexception);
    }
    catch (NoSuchFieldException nosuchfieldexception)
    {
      logger.throwing("BrainStone", "AddAchievementDesc", nosuchfieldexception);
      throwException(nosuchfieldexception);
    }
  }

  private static void throwException(Throwable throwable)
  {
    ModLoader.throwException("Exception occured in BrainStone", throwable);
  }

  public String getVersion()
  {
    return "1.48.27 BETA release";
  }

  public String toString()
  {
    return "BrainStoneMod v" + getVersion();
  }
}