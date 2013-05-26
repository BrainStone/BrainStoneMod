package mods.brainstone;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import mods.brainstone.blocks.BlockBrainLightSensor;
import mods.brainstone.blocks.BlockBrainLogicBlock;
import mods.brainstone.blocks.BlockBrainStone;
import mods.brainstone.blocks.BlockBrainStoneOre;
import mods.brainstone.blocks.BlockBrainStoneTrigger;
import mods.brainstone.blocks.BlockPulsatingBrainStone;
import mods.brainstone.guis.GuiBrainStoneTrigger;
import mods.brainstone.handlers.BrainStoneCraftingHandler;
import mods.brainstone.handlers.BrainStoneFuelHandler;
import mods.brainstone.handlers.BrainStoneGuiHandler;
import mods.brainstone.handlers.BrainStonePacketHandler;
import mods.brainstone.handlers.BrainStonePickupNotifier;
import mods.brainstone.handlers.BrainStoneTickHandler;
import mods.brainstone.items.ItemArmorBrainStone;
import mods.brainstone.items.ItemHoeBrainStone;
import mods.brainstone.items.ItemSwordBrainStone;
import mods.brainstone.items.ItemToolBrainStone;
import mods.brainstone.logicgates.Gate;
import mods.brainstone.templates.BSP;
import mods.brainstone.templates.BlockBrainStoneBase;
import mods.brainstone.templates.ItemBrainStoneBase;
import mods.brainstone.tileentities.TileEntityBlockBrainLightSensor;
import mods.brainstone.tileentities.TileEntityBlockBrainLogicBlock;
import mods.brainstone.tileentities.TileEntityBlockBrainStoneTrigger;
import mods.brainstone.worldgenerators.BrainStoneWorldGenerator;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.EnumHelper;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
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
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * The main file of the mod
 * 
 * @author Yannick Schinko (alias The_BrainStone)
 */
@Mod(modid = "BrainStoneMod", name = "Brain Stone Mod", version = "v2.28.27 BETA")
@NetworkMod(clientSideRequired = true, serverSideRequired = true, channels = {
		"BSM", // generic Packet
		"BSM.TEBBSTS", // TileEntityBlockBrainStoneTrigger Server Packet
		"BSM.TEBBSTC", // TileEntityBlockBrainStoneTrigger Client Packet
		"BSM.TEBBLSS", // TileEntityBlockBrainLightSensor Server Packet
		"BSM.TEBBLSC", // TileEntityBlockBrainLightSensor Client Packet
		"BSM.TEBBLBS", // TileEntityBlockBrainLogicBlock Server Packet
		"BSM.TEBBLBC", // TileEntityBlockBrainLogicBlock Client Packet
		"BSM.UPAS", // UpdatePlayerAt Server Packet
		"BSM.RRBAC", // ReRenderBlockAt Client Packet
		"BSM.UPMC" // UpdatePlayerMovement Client Packet
}, packetHandler = BrainStonePacketHandler.class)
public class BrainStone {
	/** The "Mod" annotation */
	private static Mod annotation = null;

	/** States if the current mod version is a release version or not */
	public static final boolean release = getModAnnotation().version()
			.toLowerCase().contains("release");
	/** States if the current mod version is a debug version or not */
	public static final boolean debug = getModAnnotation().version()
			.toLowerCase().contains("debug");
	/** States if the current mod version is a DEV version or not */
	public static final boolean DEV = getModAnnotation().version()
			.toLowerCase().contains("dev")
			|| getModAnnotation().version().toLowerCase()
					.contains("prerelease");

	/** The standard id blocks start with */
	public static final int startBlockId = 1258;
	/** The standard id items start with */
	public static final int startItemId = 359;
	/** The standard id achievements start with */
	public static final int startAchievementId = 3698;
	/** A String with the German localization (de_DE) */
	private static final String de = "de_DE";

	private static String releaseVersion;
	private static String recommendedVersion;
	private static String latestVersion;

	/**
	 * <tt><table><tr><td>0:</td><td>release</td></tr><tr><td>1:</td><td>recommended</td></tr><tr><td>2:</td><td>latest</td></tr><tr><td>-1:</td><td><i>none</i></td></tr></table></tt>
	 */
	private static byte updateNotification;

	public static final String basePath = "/mods/brainstone/";
	public static final String guiPath = basePath + "textures/gui/";
	public static final String armorPath = basePath + "textures/armor/";

	/** The instance of this mod */
	@Instance("BrainStoneMod")
	public static BrainStone instance;

	/** The client proxy */
	@SidedProxy(clientSide = "mods.brainstone.ClientProxy", serverSide = "mods.brainstone.CommonProxy")
	public static CommonProxy proxy;

	/** The BrainStone Tool Material */
	public static EnumToolMaterial toolBRAINSTONE = EnumHelper.addToolMaterial(
			"BRAINSTONE", 3, 5368, 6F, 5, 25);
	/** The BrainStone Tool Material */
	public static EnumArmorMaterial armorBRAINSTONE = EnumHelper
			.addArmorMaterial("BRAINSTONE", 114, new int[] { 2, 6, 5, 2 }, 25);

	@SideOnly(Side.CLIENT)
	public static boolean called_onPlayerJoin;

	/**
	 * A HashMap with the ids of the blocks and items.<br>
	 * &emsp;<b>key:</b> The internal id<br>
	 * &emsp;<b>value:</b> The real block/item id
	 */
	private static HashMap<Integer, Integer> ids = new HashMap<Integer, Integer>();
	/**
	 * A HashMap with the all blocks.<br>
	 * &emsp;<b>key:</b> The internal id<br>
	 * &emsp;<b>value:</b> The actual block
	 */
	private static HashMap<Integer, Block> blocks = new HashMap<Integer, Block>();
	/**
	 * A HashMap with the all items.<br>
	 * &emsp;<b>key:</b> The internal id<br>
	 * &emsp;<b>value:</b> The actual item
	 */
	private static HashMap<Integer, Item> items = new HashMap<Integer, Item>();
	/**
	 * A HashMap with the all items.<br>
	 * &emsp;<b>key:</b> The internal id<br>
	 * &emsp;<b>value:</b> The actual item
	 */
	private static HashMap<Integer, Achievement> achievements = new HashMap<Integer, Achievement>();

	/**
	 * A HashMap with the all block and item names in English.<br>
	 * &emsp;<b>key:</b> The internal id<br>
	 * &emsp;<b>value:</b> The English block or item name
	 */
	private static HashMap<Integer, String> name_en = new HashMap<Integer, String>();
	static {
		// Blocks

		name_en.put(0, "Brain Stone");
		name_en.put(1, "Brain Stone Out");
		name_en.put(2, "Brain Stone Ore");
		name_en.put(3, "Dirty Brain Stone");
		name_en.put(4, "Brain Light Sensor");
		name_en.put(5, "Brain Stone Trigger");
		name_en.put(6, "Brain Logic Block");
		name_en.put(7, "Pulsating Brain Stone");
		name_en.put(8, "Pulsating Brain Stone Effect");

		// Items

		name_en.put(startItemId + 0, "Brain Stone Dust");
		name_en.put(startItemId + 1, "Coal Briquette");
		name_en.put(startItemId + 2, "Brain Processor");
		name_en.put(startItemId + 3, "Brain Stone Sword");
		name_en.put(startItemId + 4, "Brain Stone Shovel");
		name_en.put(startItemId + 5, "Brain Stone Pickaxe");
		name_en.put(startItemId + 6, "Brain Stone Axe");
		name_en.put(startItemId + 7, "Brain Stone Hoe");
		name_en.put(startItemId + 8, "Brain Stone Helmet");
		name_en.put(startItemId + 9, "Brain Stone Chestplate");
		name_en.put(startItemId + 10, "Brain Stone Leggings");
		name_en.put(startItemId + 11, "Brain Stone Boots");

		// Achievements

		name_en.put(startAchievementId + 0, "WTHIT");
		name_en.put(startAchievementId + 1, "itLives");
		name_en.put(startAchievementId + 2, "intelligentBlocks");
		name_en.put(startAchievementId + 3, "intelligentTools");
		name_en.put(startAchievementId + 4, "logicBlock");
	}

	/**
	 * A HashMap with the all block and item names in German.<br>
	 * &emsp;<b>key:</b> The internal id<br>
	 * &emsp;<b>value:</b> The German block or item name
	 */
	private static HashMap<Integer, String> name_de = new HashMap<Integer, String>();
	static {
		// Blocks

		name_de.put(0, "Hirnstein");
		name_de.put(1, "Ausgeschalteter Hirnstein");
		name_de.put(2, "Hirnsteinerz");
		name_de.put(3, "Dreckiger Hirnstein");
		name_de.put(4, "Hirnlichtsensor");
		name_de.put(5, "Hirnsteinausl\u00F6ser");
		name_de.put(6, "Hirnlogikblock");
		name_de.put(7, "Pulsierender Hirnstein");
		name_de.put(8, "Pulsierender Hirnstein-Effekt");

		// Items

		name_de.put(startItemId + 0, "Hirnsteinstaub");
		name_de.put(startItemId + 1, "Kohlebrikett");
		name_de.put(startItemId + 2, "Hirnprozessor");
		name_de.put(startItemId + 3, "Hirnsteinschwert");
		name_de.put(startItemId + 4, "Hirnsteinschaufel");
		name_de.put(startItemId + 5, "Hirnsteinspitzhacke");
		name_de.put(startItemId + 6, "Hirnsteinaxt");
		name_de.put(startItemId + 7, "Hirnsteinfeldhacke");
		name_de.put(startItemId + 8, "Hirnsteinhelm");
		name_de.put(startItemId + 9, "Hirnsteinbrustplatte");
		name_de.put(startItemId + 10, "Hirnsteinhose");
		name_de.put(startItemId + 11, "Hirsteinstiefel");
	}

	/**
	 * A HashMap with the all localizations in English.<br>
	 * &emsp;<b>key:</b> The localizations path<br>
	 * &emsp;<b>value:</b> The English localizations
	 */
	private static HashMap<String, String> localizations_en = new HashMap<String, String>();
	static {
		localizations_en.put("gui.brainstone.item", "Item/Object");
		localizations_en.put("gui.brainstone.player", "Player");
		localizations_en.put("gui.brainstone.projectile", "Projectile");
		localizations_en.put("gui.brainstone.help", "Help");
		localizations_en.put("gui.brainstone.invertOutput", "Invert Output");
		localizations_en.put("gui.brainstone.warning1",
				"This gate will not operate until it is wired correctly!");
		localizations_en
				.put("gui.brainstone.warning2",
						"You should wire all inputs to make sure the gate works properly!");
		localizations_en
				.put("gui.brainstone.help.gate0",
						"-------------AND-Gate-------------\nA device where the output is on when all inputs are on.");
		localizations_en
				.put("gui.brainstone.help.gate1",
						"----------OR-Gate---------\nA device where the output is on when at least one of the inputs are on.");
		localizations_en
				.put("gui.brainstone.help.gate2",
						"---------XOR-Gate---------\nA device where the output switches for every input that is on.");
		localizations_en
				.put("gui.brainstone.help.gate3",
						"--------Implies-Gate-------\nReturns false only if the implication A -> B is false. That is, if the antecedent A is true, but the consequent B is false. It is often read \"if A then B.\" It is the logical equivalent of \"B or NOT A\".");
		localizations_en
				.put("gui.brainstone.help.gate4",
						"---------NOT-Gate---------\nA device that inverts the input, as such it is also called an \"Inverter\" Gate.");
		localizations_en
				.put("gui.brainstone.help.gate5",
						"-------RS-NOR-Latch-------\nA device where Q will stay on forever after input is received by S. Q can be turned off again by a signal received by R. S and R shuouldn't be on at the same time!");
		localizations_en
				.put("gui.brainstone.help.gate6",
						"--------D-Flip-Flop--------\nA D flip-flop, or \"data\" flip-flop, sets the output to D only when its clock (input C) is on.");
		localizations_en
				.put("gui.brainstone.help.gate7",
						"--------T-Flip-Flop--------\nT flip-flops are also known as \"toggles.\" Whenever T changes from OFF to ON, the output will toggle its state.");
		localizations_en
				.put("gui.brainstone.help.gate8",
						"--------JK-Flip-Flop-------\nIf the input J = 1 and the input K = 0, the output Q = 1. When J = 0 and K = 1, the output Q = 0. If both J and K are 0, then the JK flip-flop maintains its previous state. If both are 1, the output will complement itself.");
		localizations_en.put("gui.brainstone.classic", "Classic");
		localizations_en.put("gui.brainstone.simple", "Simple");
		localizations_en.put("gui.brainstone.proportional", "Proportional");
		localizations_en.put("gui.brainstone.inverted", "Inverted");
	}

	/**
	 * A HashMap with the all localizations in German.<br>
	 * &emsp;<b>key:</b> The localizations path<br>
	 * &emsp;<b>value:</b> The German localizations
	 */
	private static HashMap<String, String> localizations_de = new HashMap<String, String>();
	static {
		localizations_de.put("gui.brainstone.item", "Item/Objekt");
		localizations_de.put("gui.brainstone.player", "Spieler");
		localizations_de.put("gui.brainstone.projectile", "Projektil");
		localizations_de.put("gui.brainstone.help", "Hilfe");
		localizations_de.put("gui.brainstone.invertOutput",
				"Ausgang invertieren");
		localizations_de
				.put("gui.brainstone.warning1",
						"Dieses Gate wird nicht arbeiten, bis es korrekt angeschlossen ist!");
		localizations_de
				.put("gui.brainstone.warning2",
						"Sie sollten alle Eing\u00E4nge anschie\u00dfen, damit das Gate richtig arbeiten kann!");
		localizations_de
				.put("gui.brainstone.help.gate0",
						"---------AND-Gate---------\nDas AND-Gate ist an, wenn alle Eing\u00E4nge an sind.");
		localizations_de
				.put("gui.brainstone.help.gate1",
						"----------OR-Gate---------\nDas OR-Gate ist an, wenn mindestens ein Eingang an ist.");
		localizations_de
				.put("gui.brainstone.help.gate2",
						"---------XOR-Gate---------\nDer Ausgang schaltet f\u00FCr jeden aktiven Eingang um.");
		localizations_de
				.put("gui.brainstone.help.gate3",
						"--------Implies-Gate-------\nDer Ausgang ist nur falsch (aus), wenn der Schluss A -> B falsch ist. Das trifft zu, wenn die Bedingung A wahr ist, die Konsequenz B falsch. Man kann die Beziehung verstehen als \"Wenn A dann B\". Entspricht \"B oder nicht A\".");
		localizations_de
				.put("gui.brainstone.help.gate4",
						"---------NOT-Gate---------\nAuch bekannt als Umkehrer (in der Elektrotechnik \u00FCblicherweise \"Inverter\" genannt). Dieses Gate kehrt den Eingang um.");
		localizations_de
				.put("gui.brainstone.help.gate5",
						"-------RS-NOR-Latch-------\nDer Ausgang wird eingeschaltet, wenn \"S\" eingeschaltet wird (und danach wieder aus) ausgeschaltet, wenn \"R\" eingeschaltet wird. Beide sollten niemals gleichzeitig an sein!");
		localizations_de
				.put("gui.brainstone.help.gate6",
						"--------D-Flip-Flop--------\nEin D-Flip-Flop oder auch Daten-Flip-Flop setzt seinen Ausgang nur auf den Zustand seines Einganges D, wenn die Clock (Eingang C) an ist.");
		localizations_de
				.put("gui.brainstone.help.gate7",
						"--------T-Flip-Flop--------\nDer T-Flip-Flop ist ein Speicher, der umspringt, wenn das eingehende Signal (T) angeht.");
		localizations_de
				.put("gui.brainstone.help.gate8",
						"--------JK-Flip-Flop-------\nWenn der Eingang J = 1 and der Eingang K = 0, wird der Ausgang Q = 1. Wenn J = 0 and K = 1, dann wird Q = 0. Wenn J und K 0 sind, dann beh\u00E4lt das Gate seinen Zustand. Wenn beide 1 sind, dann kehrt sich der Ausgang um.");
		localizations_de.put("gui.brainstone.classic", "Klassik");
		localizations_de.put("gui.brainstone.simple", "Einfach");
		localizations_de.put("gui.brainstone.proportional", "Proportional");
		localizations_de.put("gui.brainstone.inverted", "Invertiert");
	}

	/**
	 * A HashMap with the all the description for Achievements in English.<br>
	 * &emsp;<b>key:</b> The Achievement id<br>
	 * &emsp;<b>value:</b> The English titles
	 */
	private static HashMap<Integer, String[]> achievement_en = new HashMap<Integer, String[]>();
	static {
		achievement_en.put(0, new String[] { "What the Hell is that???",
				"You have to find a strange green powder." });
		achievement_en.put(1, new String[] { "It lives!",
				"Crafting and a Smelting is the key!" });
		achievement_en.put(2, new String[] { "Intelligent Blocks",
				"Make usefull intelligent Blocks out of this green stone!" });
		achievement_en.put(3, new String[] { "Intelligent Tools!",
				"Make Tools out of this green stone!" });
		achievement_en.put(4, new String[] { "Logic Block",
				"First make a processor. Then a Logic Block!" });
	}

	/**
	 * A HashMap with the all the description for Achievements in German.<br>
	 * &emsp;<b>key:</b> The Achievement id<br>
	 * &emsp;<b>value:</b> The German titles
	 */
	private static HashMap<Integer, String[]> achievement_de = new HashMap<Integer, String[]>();
	static {
		achievement_de.put(0, new String[] { "Was zur H\u00F6lle ist das???",
				"Du must ein seltsames gr\u00FCnes Pulver finden." });
		achievement_de.put(1, new String[] { "Es lebt!",
				"Craften und Schmelzen ist die L\u00F6sung!" });
		achievement_de
				.put(2,
						new String[] {
								"Intelligente Bl\u00F6cke",
								"Stelle n\u00FCtzliche intelligente Bl\u00F6cke aus diesem gr\u00FCnen Stein her!" });
		achievement_de.put(3, new String[] { "Intelligente Werkzeuge",
				"Stelle Werkzeuge aus diesem gr\u00FCnen Stein her!" });
		achievement_de.put(4, new String[] { "Logikblock!",
				"Mache als erstes einen Prozessor. Dann einen Logikblock!" });
	}

	/**
	 * Preinitialization. Reads the ids from the config file and fills the block
	 * and item HashMaps with the blocks and items.
	 * 
	 * @param event
	 *            The MCForge PreInitializationEvent
	 */
	@PreInit
	public void preInit(FMLPreInitializationEvent event) {
		if ((Gate.Gates == null) || Gate.Gates.isEmpty()) {
			BSP.throwNullPointerException("Well, that should NOT have happenend! This IS a HUGE problem if you notice this please report it to yannick@tedworld.de.\nThanks!\n\nDeveloper Information:\nThe Map of the Gates is EMPTY!\nIs gates null: "
					+ (Gate.Gates == null));
		}

		BSP.setUpLogger(event.getModLog());

		getIds(event);
		retriveCurrentVersions();
		generateMcModInfoFile(event);
		generateObjects();
	}

	/**
	 * Initialization. Registers the client render information, the GuiHandler,
	 * the blocks, the TileEntitys, adds the names, the recipes, smeltings,
	 * registers the FuelHandler, the WordGenerator and adds the localizations.
	 * 
	 * @param event
	 *            The MCForge InitializationEvent
	 */
	@Init
	public void init(FMLInitializationEvent event) {
		proxy.registerRenderInformation();
		NetworkRegistry.instance().registerGuiHandler(this,
				new BrainStoneGuiHandler());

		registerBlocks(); // Blocks
		registerTileEntitys(); // TileEntitys
		addNames(); // Names
		addRecipes(); // Recipes
		addSmeltings(); // Smeltings
		// Fuels
		GameRegistry.registerFuelHandler(new BrainStoneFuelHandler());
		// Ore Generation
		GameRegistry.registerWorldGenerator(new BrainStoneWorldGenerator());
		// Crafting Handler
		GameRegistry.registerCraftingHandler(new BrainStoneCraftingHandler());
		// Pickup Handler
		GameRegistry.registerPickupHandler(new BrainStonePickupNotifier());
		// Tick Handler
		TickRegistry.registerTickHandler(new BrainStoneTickHandler(),
				Side.CLIENT);
		addLocalizations();

		MinecraftForge.setBlockHarvestLevel(brainStone(), "pickaxe", 2);
		MinecraftForge.setBlockHarvestLevel(brainStoneOut(), "pickaxe", 2);
		MinecraftForge.setBlockHarvestLevel(dirtyBrainStone(), "pickaxe", 2);
		MinecraftForge.setBlockHarvestLevel(brainStoneOre(), "pickaxe", 2);

		proxy.registerOre();
	}

	/**
	 * Postinitialization. Fills the triggerEntity's for the BrainStoneTrigger
	 * 
	 * @param event
	 *            The MCForge PostInitializationEvent
	 * @throws Throwable
	 */
	@PostInit
	public void postInit(FMLPostInitializationEvent event) throws Throwable {
		fillTriggerEntities();
	}

	@SideOnly(Side.CLIENT)
	public static void onPlayerJoin() {
		final String version = getModAnnotation().version();

		switch (updateNotification) {
		case 0:
			if (isHigherVersion(version, releaseVersion)) {
				sendToPlayer("§aA new Version of the BSM is available!\n§l§c========== &4"
						+ releaseVersion
						+ "§c =========="
						+ "§9Download it at §1http://adf.ly/2002096/release §9or §1https://github.com/BrainStone/brainstone§9!");
			}

			break;
		case 1:
			if (isHigherVersion(version, releaseVersion)
					&& !isHigherVersion(releaseVersion, recommendedVersion)) {
				sendToPlayer("§aA new Version of the BSM is available!\n§l§c========== &4"
						+ releaseVersion
						+ "§c =========="
						+ "§9Download it at §1http://adf.ly/2002096/release §9or §1https://github.com/BrainStone/brainstone§9!");
			} else if (isHigherVersion(version, recommendedVersion)) {
				sendToPlayer("§aA new recommended DEV Version of the BSM is available!\n§l§c========== &4"
						+ recommendedVersion
						+ "§c =========="
						+ "§9Download it at §1http://adf.ly/2002096/recommended §9or §1https://github.com/BrainStone/brainstone§9!");
			}

			break;
		case 2:
			if (isHigherVersion(version, releaseVersion)
					&& !isHigherVersion(releaseVersion, recommendedVersion)
					&& !isHigherVersion(releaseVersion, latestVersion)) {
				sendToPlayer("§aA new Version of the BSM is available!\n§l§c========== &4"
						+ releaseVersion
						+ "§c =========="
						+ "§9Download it at §1http://adf.ly/2002096/release §9or §1https://github.com/BrainStone/brainstone§9!");
			} else if (isHigherVersion(version, recommendedVersion)
					&& !isHigherVersion(recommendedVersion, latestVersion)) {
				sendToPlayer("§aA new recommended DEV Version of the BSM is available!\n§l§c========== &4"
						+ recommendedVersion
						+ "§c =========="
						+ "§9Download it at §1http://adf.ly/2002096/recommended §9or §1https://github.com/BrainStone/brainstone§9!");
			} else if (isHigherVersion(version, latestVersion)) {
				sendToPlayer("§aA new DEV Version of the BSM is available!\n§l§c========== &4"
						+ latestVersion
						+ "§c =========="
						+ "§9Download it at §1http://adf.ly/2002096/latest §9or §1https://github.com/BrainStone/brainstone§9!");
			}

			break;
		}
	}

	private static void sendToPlayer(String message) {
		proxy.getPlayer().sendChatToPlayer(message);
	}

	private static boolean isHigherVersion(String currentVersion,
			String newVersion) {
		final int[] _current = splitVersion(currentVersion);
		final int[] _new = splitVersion(newVersion);

		return (_current[0] < _new[0])
				|| ((_current[0] == _new[0]) && (_current[1] < _new[1]))
				|| ((_current[0] == _new[0]) && (_current[1] == _new[1]) && (_current[2] < _new[2]));
	}

	private static int[] splitVersion(String Version) {
		final String[] tmp = Version.substring(1).split(" ")[0].split("\\.");
		final int size = tmp.length;
		final int out[] = new int[size];

		for (int i = 0; i < size; i++) {
			out[i] = Integer.parseInt(tmp[i]);
		}

		return out;
	}

	private static void retriveCurrentVersions() {
		try {
			releaseVersion = get_content((HttpsURLConnection) new URL(
					"https://raw.github.com/BrainStone/brainstone/master/builds/release/.version")
					.openConnection());

			recommendedVersion = get_content((HttpsURLConnection) new URL(
					"https://raw.github.com/BrainStone/brainstone/master/builds/recommended/.version")
					.openConnection());

			latestVersion = get_content((HttpsURLConnection) new URL(
					"https://raw.github.com/BrainStone/brainstone/master/builds/latest/.version")
					.openConnection());

		} catch (final MalformedURLException e) {
			BSP.warningException_noAddon(e,
					"The Versions will be empty. No internet connection!");

			releaseVersion = "";
			recommendedVersion = "";
			latestVersion = "";
		} catch (final IOException e) {
			BSP.warningException_noAddon(e,
					"The Versions will be empty. No internet connection!");

			releaseVersion = "";
			recommendedVersion = "";
			latestVersion = "";
		}
	}

	private static String get_content(HttpsURLConnection con)
			throws IOException {
		String output = "";

		if (con != null) {
			final BufferedReader br = new BufferedReader(new InputStreamReader(
					con.getInputStream()));

			String input;

			while ((input = br.readLine()) != null) {
				output = output + input;
			}
			br.close();
		}

		return output;
	}

	/**
	 * Generates the mcmod.info file. Uses the "Mod" annotation to detect some
	 * values. Others are fixed
	 */
	private static void generateMcModInfoFile(FMLPreInitializationEvent event) {
		event.getModMetadata().modId = getModAnnotation().modid();
		event.getModMetadata().name = getModAnnotation().name();
		event.getModMetadata().version = getModAnnotation().version();
		event.getModMetadata().url = "http://minecraft.de/showthread.php?89926";
		event.getModMetadata().credits = "The_BrainStone(Code, Textures, Ideas), Jobbel(Name), TheStarman(Textures)";
		event.getModMetadata().authorList = Arrays
				.asList(new String[] { "The_BrainStone" });
		event.getModMetadata().description = "The Brain Stone Mod adds a new block type. It is called Brain Stone. It is very rare but you can make many different intelligent sensor blocks! An example is the BrainStoneTrigger. It's a block that triggers if an entity is on top. All these intelligent blocks are highly adjustable! There are also tools. The are as fast as iron tools but you can havrest more than 5,368 blocks! (Diamond tools only 1,561). The latest feature is the PulsatingBrainStoneBlock. It is the craziest block you have ever seen! It will throw you and animals through the air or will add random potion effects! You acan make yourself immune to these effect by wearing the full set of the newly adden BrainStoneArmor.\nBut see by yourself and enjoy!\n\n\nAnd thanks for downloading and supporting this mod!\n\n\n\nIf you think this mod caused a game crash (what should not happen by the way XD) send an email with the error log to yannick@tedworld.de!\n\nThank you!"
				+ "\n\n\n\nCurrent Versions:\n    release:          "
				+ releaseVersion
				+ "\n    recommended:   "
				+ recommendedVersion
				+ "\n    latest:            " + latestVersion;
		event.getModMetadata().logoFile = "";
		event.getModMetadata().updateUrl = (updateNotification == -1) ? ""
				: ("https://raw.github.com/BrainStone/brainstone/master/builds/"
						+ ((updateNotification == 0) ? "release"
								: ((updateNotification == 1) ? "recommended"
										: "latest")) + "/BrainStoneMod.zip");
		event.getModMetadata().parent = "";
		event.getModMetadata().screenshots = new String[] {};
	}

	/**
	 * Adds all the localizations (in English AND in German).
	 */
	private static void addLocalizations() {
		final int length = localizations_en.size();
		final String[] keys = localizations_en.keySet().toArray(
				new String[length]);
		String key;
		LanguageRegistry.instance();

		for (int i = 0; i < length; i++) {
			key = keys[i];

			LanguageRegistry.instance().addStringLocalization(key,
					localizations_en.get(key));
			LanguageRegistry.instance().addStringLocalization(key, de,
					localizations_de.get(key));
		}
	}

	/**
	 * Adds the names to the blocks and items (in English AND in German).
	 */
	private static final void addNames() {
		final HashMap<Integer, Object> objects = new HashMap<Integer, Object>(
				blocks);
		objects.putAll(items);
		objects.putAll(achievements);

		final int length = objects.size();
		final Integer[] keys = objects.keySet().toArray(new Integer[length]);
		int key, key2;
		Object obj;

		for (int i = 0; i < length; i++) {
			key = keys[i];
			obj = objects.get(key);

			if (key < startAchievementId) {
				LanguageRegistry.addName(obj, get_name_en(key));

				LanguageRegistry.instance().addNameForObject(obj, de,
						get_name_de(key));
			} else if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
				key2 = key - startAchievementId;

				final Achievement tmp = (Achievement) obj;
				LanguageRegistry.instance().addStringLocalization(
						tmp.getName(), achievement_en.get(key2)[0]);
				LanguageRegistry.instance().addStringLocalization(
						tmp.getName() + ".desc", achievement_en.get(key2)[1]);

				LanguageRegistry.instance().addStringLocalization(
						tmp.getName(), de, achievement_de.get(key2)[0]);
				LanguageRegistry.instance().addStringLocalization(
						tmp.getName() + ".desc", de,
						achievement_de.get(key2)[1]);
			}
		}
	}

	/**
	 * Adds the recipes.
	 */
	private static void addRecipes() {
		GameRegistry.addRecipe(new ItemStack(dirtyBrainStone(), 1),
				new Object[] { "XX", "XX", 'X', brainStoneDust() });

		for (int i = 0; i <= 9; i++) {
			GameRegistry.addShapelessRecipe(new ItemStack(coalBriquette(), 1),
					new Object[] { new ItemStack(Item.coal, 1, i <= 0 ? 0 : 1),
							new ItemStack(Item.coal, 1, i <= 1 ? 0 : 1),
							new ItemStack(Item.coal, 1, i <= 2 ? 0 : 1),
							new ItemStack(Item.coal, 1, i <= 3 ? 0 : 1),
							new ItemStack(Item.coal, 1, i <= 4 ? 0 : 1),
							new ItemStack(Item.coal, 1, i <= 5 ? 0 : 1),
							new ItemStack(Item.coal, 1, i <= 6 ? 0 : 1),
							new ItemStack(Item.coal, 1, i <= 7 ? 0 : 1),
							new ItemStack(Item.coal, 1, i <= 8 ? 0 : 1) });
		}

		GameRegistry.addRecipe(new ItemStack(brainLightSensor(), 1),
				new Object[] { "XGX", "XBX", "XRX", 'X', Block.stone, 'G',
						Block.glass, 'B', brainStone(), 'R', Item.redstone });
		GameRegistry.addRecipe(new ItemStack(brainStoneTrigger(), 1),
				new Object[] { "XXX", "RRR", "XBX", 'X', Block.stone, 'B',
						brainStone(), 'R', Item.redstone });
		GameRegistry.addRecipe(new ItemStack(brainLogicBlock(), 1),
				new Object[] { "SRS", "RPR", "SRS", 'S', Block.stone, 'P',
						brainProcessor(), 'R', Item.redstone });
		GameRegistry.addRecipe(new ItemStack(pulsatingBrainStone(), 1),
				new Object[] { "dBd", "BDB", "dBd", 'd', brainStoneDust(), 'B',
						brainStone(), 'D', Item.diamond });
		GameRegistry.addRecipe(new ItemStack(brainStoneSword(), 1),
				new Object[] { "B", "B", "S", 'S', Item.stick, 'B',
						brainStone() });
		GameRegistry.addRecipe(new ItemStack(brainStoneShovel(), 1),
				new Object[] { "B", "S", "S", 'S', Item.stick, 'B',
						brainStone() });
		GameRegistry.addRecipe(new ItemStack(brainStonePickaxe(), 1),
				new Object[] { "BBB", " S ", " S ", 'S', Item.stick, 'B',
						brainStone() });
		GameRegistry.addRecipe(new ItemStack(brainStoneAxe(), 1), new Object[] {
				"BB", "BS", " S", 'S', Item.stick, 'B', brainStone() });
		GameRegistry.addRecipe(new ItemStack(brainStoneHoe(), 1), new Object[] {
				"BB", " S", " S", 'S', Item.stick, 'B', brainStone() });
		GameRegistry.addRecipe(new ItemStack(brainProcessor(), 4),
				new Object[] { "TRT", "SBS", "TRT", 'B', brainStone(), 'S',
						Item.redstone, 'T', Block.torchRedstoneActive, 'R',
						Item.redstoneRepeater });
		GameRegistry.addRecipe(new ItemStack(brainStoneHelmet(), 1),
				new Object[] { "BBB", "B B", 'B', brainStone() });
		GameRegistry.addRecipe(new ItemStack(brainStonePlate(), 1),
				new Object[] { "B B", "BBB", "BBB", 'B', brainStone() });
		GameRegistry.addRecipe(new ItemStack(brainStoneLeggings(), 1),
				new Object[] { "BBB", "B B", "B B", 'B', brainStone() });
		GameRegistry.addRecipe(new ItemStack(brainStoneBoots(), 1),
				new Object[] { "B B", "B B", 'B', brainStone() });
	}

	/**
	 * Adds the smeltings.
	 */
	private static void addSmeltings() {
		GameRegistry.addSmelting(dirtyBrainStone().blockID, new ItemStack(
				brainStone(), 1, 0), 3.0F);
	}

	private static void fillTriggerEntities() throws Throwable {
		final LinkedHashMap<String, Class[]> brainStoneTriggerEntities = new LinkedHashMap<String, Class[]>();

		brainStoneTriggerEntities.put("gui.brainstone.player",
				new Class[] { EntityPlayer.class });
		brainStoneTriggerEntities.put("gui.brainstone.item",
				new Class[] { EntityBoat.class, EntityFishHook.class,
						EntityItem.class, EntityMinecart.class,
						EntityTNTPrimed.class, EntityXPOrb.class });
		brainStoneTriggerEntities.put("gui.brainstone.projectile", new Class[] {
				EntityArrow.class, EntityThrowable.class, EntityEnderEye.class,
				EntityFireball.class });

		final Map allEntities = EntityList.IDtoClassMapping;
		final int length = allEntities.size();
		final Object[] keys = allEntities.keySet().toArray(new Object[length]);
		Object key;
		Class value;

		for (int i = 0; i < length; i++) {
			key = keys[i];
			value = (Class) allEntities.get(key);

			if ((value != null) && (EntityLiving.class.isAssignableFrom(value))
					&& (!EntityLiving.class.equals(value))) {
				try {
					brainStoneTriggerEntities
							.put("entity."
									+ EntityList
											.getEntityString(((Entity) value
													.getDeclaredConstructor(
															new Class[] { World.class })
													.newInstance(
															new Object[] { null })))
									+ ".name", new Class[] { value });
				} catch (final InstantiationException e) {
					BSP.finestException_noAddon(
							e,
							"This is normal and is NOT a problem. ("
									+ value.getName() + ")");
				} catch (final IllegalArgumentException e) {
					BSP.severeException(
							e,
							"This is very unexpected! Actually, it should never happen!\nIt could be bad programming in the BrainStoneMod (I don't think so...) or in the "
									+ value.getName()
									+ ".class. But just report that to me. I'll take care of everything!");

					BSP.throwException(
							e,
							"This is very unexpected! Actually, it should never happen!\nIt could be bad programming in the BrainStoneMod (I don't think so...) or in the "
									+ value.getName()
									+ ".class. But just report that to me. I'll take care of everything!");
				} catch (final IllegalAccessException e) {
					BSP.severeException_noAddon(
							e,
							"This is caused by bad programming of the programmer of the "
									+ value.getName()
									+ ".class. Please report this to them!\n\nAdditional Information:\nThis Mod (BrainStoneMod) tried to create a new instance of the "
									+ value.getName()
									+ ".class with the only parameter of the type \"World\" and was denied acces to that!");

					throw new IllegalAccessException(e.getMessage());
				} catch (final NoSuchMethodException e) {
					BSP.severeException_noAddon(
							e,
							"This is caused by bad programming of the programmer of the "
									+ value.getName()
									+ ".class. Please report this to them!\n\nAdditional Information:\nThis Mod (BrainStoneMod) tried to create a new instance of the "
									+ value.getName()
									+ ".class with the only parameter of the type \"World\" and there is no such constructor!");

					throw new NoSuchMethodException(e.getMessage());
				} catch (final InvocationTargetException e) {
					BSP.severeException_noAddon(e,
							"This is caused by bad programming of the programmer of the "
									+ value.getName()
									+ ".class. Please report this to them!");

					throw new InvocationTargetException(e);
				} catch (final SecurityException e) {
					BSP.severeException_noAddon(e,
							"This is caused by bad programming of the programmer of the "
									+ value.getName()
									+ ".class. Please report this to them!");

					throw new SecurityException(e);
				}
			}
		}

		BlockBrainStoneTrigger.triggerEntities = brainStoneTriggerEntities;
		TileEntityBlockBrainStoneTrigger.triggerEntities = brainStoneTriggerEntities;

		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
			GuiBrainStoneTrigger.triggerEntities = brainStoneTriggerEntities;
		}
	}

	// Items

	/**
	 * Generates the blocks and items and puts them into the HasMaps.
	 */
	private static void generateObjects() {
		// Blocks

		blocks.put(0, new BlockBrainStone(0, false));
		blocks.put(1, new BlockBrainStone(1, true));
		blocks.put(2, new BlockBrainStoneOre(2));
		blocks.put(
				3,
				(new BlockBrainStoneBase(getId(3), Material.rock))
						.setHardness(2.4F)
						.setUnlocalizedName("dirtyBrainStone")
						.setResistance(0.5F).setLightValue(0.5F)
						.setCreativeTab(CreativeTabs.tabBlock));
		blocks.get(3).blockParticleGravity = -0.1F;
		blocks.put(4, new BlockBrainLightSensor(4));
		blocks.put(5, new BlockBrainStoneTrigger(5));
		blocks.put(6, new BlockBrainLogicBlock(6));
		blocks.put(7, new BlockPulsatingBrainStone(7, false));
		blocks.put(8, new BlockPulsatingBrainStone(8, true));

		// Items

		items.put(
				startItemId + 0,
				(new ItemBrainStoneBase(getId(startItemId)))
						.setUnlocalizedName("brainStoneDust").setCreativeTab(
								CreativeTabs.tabMaterials));
		items.put(startItemId + 1, (new ItemBrainStoneBase(
				getId(startItemId + 1))).setUnlocalizedName("coalBriquette")
				.setCreativeTab(CreativeTabs.tabMisc));
		items.put(startItemId + 2, (new ItemBrainStoneBase(
				getId(startItemId + 2))).setUnlocalizedName("brainProcessor")
				.setCreativeTab(CreativeTabs.tabMisc));
		items.put(startItemId + 3, (new ItemSwordBrainStone(3, toolBRAINSTONE))
				.setUnlocalizedName("brainStoneSword"));
		items.put(startItemId + 4, (new ItemToolBrainStone(4, toolBRAINSTONE,
				"spade")).setUnlocalizedName("brainStoneShovel"));
		items.put(startItemId + 5, (new ItemToolBrainStone(5, toolBRAINSTONE,
				"pickaxe")).setUnlocalizedName("brainStonePickaxe"));
		items.put(startItemId + 6, (new ItemToolBrainStone(6, toolBRAINSTONE,
				"axe")).setUnlocalizedName("brainStoneAxe"));
		items.put(startItemId + 7, (new ItemHoeBrainStone(7, toolBRAINSTONE))
				.setUnlocalizedName("brainStoneHoe"));
		items.put(startItemId + 8, (new ItemArmorBrainStone(8, armorBRAINSTONE,
				proxy.addArmor("brainstone"), 0))
				.setUnlocalizedName("brainStoneHelmet"));
		items.put(startItemId + 9, (new ItemArmorBrainStone(9, armorBRAINSTONE,
				proxy.addArmor("brainstone"), 1))
				.setUnlocalizedName("brainStonePlate"));
		items.put(startItemId + 10, (new ItemArmorBrainStone(10,
				armorBRAINSTONE, proxy.addArmor("brainstone"), 2))
				.setUnlocalizedName("brainStoneLeggings"));
		items.put(startItemId + 11, (new ItemArmorBrainStone(11,
				armorBRAINSTONE, proxy.addArmor("brainstone"), 3))
				.setUnlocalizedName("brainStoneBoots"));

		// Achievements

		achievements.put(startAchievementId + 0, (new Achievement(
				getId(startAchievementId + 0), "What the Hell is that???", 8,
				2, brainStoneDust(), AchievementList.buildBetterPickaxe))
				.registerAchievement());
		achievements.put(startAchievementId + 1, (new Achievement(
				getId(startAchievementId + 1), "It lives!", 8, 4, brainStone(),
				WTHIT())).registerAchievement());
		achievements.put(startAchievementId + 2, (new Achievement(
				getId(startAchievementId + 2), "Intelligent Blocks!", 10, 5,
				brainLightSensor(), itLives())).registerAchievement());
		achievements.put(startAchievementId + 3, (new Achievement(
				getId(startAchievementId + 3), "Intelligent Tools!", 6, 5,
				brainStonePickaxe(), itLives())).registerAchievement());
		achievements.put(startAchievementId + 4, (new Achievement(
				getId(startAchievementId + 4), "Logic Block!", 12, 7,
				brainProcessor(), intelligentBlocks())).registerAchievement());
	}

	/**
	 * Returns the German name of a block or item.
	 * 
	 * @param id
	 *            The internal id
	 * @return The German name of a block or item if existing. If not the
	 *         English name<br>
	 *         (calls get_name_en(id))
	 */
	private static final String get_name_de(int id) {
		return (name_de.containsKey(id)) ? name_de.get(id) : get_name_en(id);
	}

	/**
	 * Returns the English name of a block or item.
	 * 
	 * @param id
	 *            The internal id
	 * @return The English name of a block or item if existing. If not empty
	 *         String
	 */
	private static final String get_name_en(int id) {
		return (name_en.containsKey(id)) ? name_en.get(id) : "";
	}

	/**
	 * Reads the ids from the config file and saves them into "ids".
	 * 
	 * @param event
	 *            The MCForge PreInitializationEven. Needed to access the config
	 *            file
	 */
	private static void getIds(FMLPreInitializationEvent event) {
		final Configuration config = new Configuration(
				event.getSuggestedConfigurationFile());
		config.load();

		final int length = name_en.size();
		final Integer[] keys = name_en.keySet().toArray(new Integer[length]);
		int key;
		String name;

		for (int i = 0; i < length; i++) {
			key = keys[i];
			name = get_name_en(key).replace(" ", "");

			ids.put(key,
					config.get(
							(key >= startAchievementId) ? "Achievement"
									: (key >= startItemId) ? "Item" : "Block",
							name, startBlockId + key).getInt());
		}

		final String str = config.get("DisplayUpdates", "DisplayUpdates",
				DEV ? "recommended" : (release ? "release" : "latest"))
				.getString();
		updateNotification = (byte) ((str.equals("none") || str.equals("off")) ? -1
				: (str.equals("recommended") ? 1 : (str.equals("latest") ? 2
						: 0)));

		config.save();
	}

	/**
	 * Registers all the blocks.
	 */
	private static final void registerBlocks() {
		final int length = blocks.size();
		final Integer[] keys = blocks.keySet().toArray(new Integer[length]);

		for (int i = 0; i < length; i++) {
			GameRegistry.registerBlock(blocks.get(keys[i]), ItemBlock.class,
					null);
		}
	}

	/**
	 * Registers all the TileEntiys.
	 */
	private static final void registerTileEntitys() {
		GameRegistry.registerTileEntity(TileEntityBlockBrainLightSensor.class,
				"TileEntityBlockBrainLightSensor");
		GameRegistry.registerTileEntity(TileEntityBlockBrainStoneTrigger.class,
				"TileEntityBlockBrainStoneTrigger");
		GameRegistry.registerTileEntity(TileEntityBlockBrainLogicBlock.class,
				"TileEntityBlockBrainLogicBlock");
	}

	private static Mod getModAnnotation() {
		if (annotation == null)
			return (annotation = BrainStone.class.getAnnotation(Mod.class));

		return annotation;
	}

	/**
	 * Returns the ingame id of block or a items from this mod.
	 * 
	 * @param id
	 *            The internal id
	 * @return The ingame id of the block or the item
	 */
	public static final int getId(int id) {
		return (ids.containsKey(id)) ? ids.get(id) : 0;
	}

	/**
	 * @return the instance of BrainStone
	 */
	public static final Block brainStone() {
		return blocks.get(0);
	}

	/**
	 * @return the instance of BrainStoneOut
	 */
	public static final Block brainStoneOut() {
		return blocks.get(1);
	}

	/**
	 * @return the instance of BrainStoneOre
	 */
	public static final Block brainStoneOre() {
		return blocks.get(2);
	}

	/**
	 * @return the instance of DirtyBrainStone
	 */
	public static final Block dirtyBrainStone() {
		return blocks.get(3);
	}

	/**
	 * @return the instance of BrainLightSensor
	 */
	public static final Block brainLightSensor() {
		return blocks.get(4);
	}

	/**
	 * @return the instance of BrainStoneTrigger
	 */
	public static final Block brainStoneTrigger() {
		return blocks.get(5);
	}

	/**
	 * @return the instance of BrainLogicBlock
	 */
	public static final Block brainLogicBlock() {
		return blocks.get(6);
	}

	/**
	 * @return the instance of BrainLogicBlock
	 */
	public static final Block pulsatingBrainStone() {
		return blocks.get(7);
	}

	/**
	 * @return the instance of BrainLogicBlock
	 */
	public static final Block pulsatingBrainStoneEffect() {
		return blocks.get(8);
	}

	/**
	 * @return the instance of BrainStoneDust
	 */
	public static final Item brainStoneDust() {
		return items.get(startItemId);
	}

	/**
	 * @return the instance of CoalBriquette
	 */
	public static final Item coalBriquette() {
		return items.get(startItemId + 1);
	}

	/**
	 * @return the instance of BrainProcessor
	 */
	public static final Item brainProcessor() {
		return items.get(startItemId + 2);
	}

	/**
	 * @return the instance of BrainStoneSword
	 */
	public static final Item brainStoneSword() {
		return items.get(startItemId + 3);
	}

	/**
	 * @return the instance of BrainStoneShovel
	 */
	public static final Item brainStoneShovel() {
		return items.get(startItemId + 4);
	}

	/**
	 * @return the instance of BrainStonePickaxe
	 */
	public static final Item brainStonePickaxe() {
		return items.get(startItemId + 5);
	}

	/**
	 * @return the instance of BrainStoneAxe
	 */
	public static final Item brainStoneAxe() {
		return items.get(startItemId + 6);
	}

	/**
	 * @return the instance of BrainStoneHoe
	 */
	public static final Item brainStoneHoe() {
		return items.get(startItemId + 7);
	}

	/**
	 * @return the instance of BrainStoneHelmet
	 */
	public static final Item brainStoneHelmet() {
		return items.get(startItemId + 8);
	}

	/**
	 * @return the instance of BrainStonePlate
	 */
	public static final Item brainStonePlate() {
		return items.get(startItemId + 9);
	}

	/**
	 * @return the instance of BrainStoneLeggings
	 */
	public static final Item brainStoneLeggings() {
		return items.get(startItemId + 10);
	}

	/**
	 * @return the instance of BrainStoneBoots
	 */
	public static final Item brainStoneBoots() {
		return items.get(startItemId + 11);
	}

	/**
	 * @return the instance of WTHIT
	 */
	public static final Achievement WTHIT() {
		return achievements.get(startAchievementId);
	}

	/**
	 * @return the instance of itLives
	 */
	public static final Achievement itLives() {
		return achievements.get(startAchievementId + 1);
	}

	/**
	 * @return the instance of intelligentBlocks
	 */
	public static final Achievement intelligentBlocks() {
		return achievements.get(startAchievementId + 2);
	}

	/**
	 * @return the instance of intelligentTools
	 */
	public static final Achievement intelligentTools() {
		return achievements.get(startAchievementId + 3);
	}

	/**
	 * @return the instance of logicBlock
	 */
	public static final Achievement logicBlock() {
		return achievements.get(startAchievementId + 4);
	}
}