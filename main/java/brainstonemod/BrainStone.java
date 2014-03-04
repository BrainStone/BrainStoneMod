package brainstonemod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.command.ICommandSender;
import net.minecraft.creativetab.CreativeTabs;
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
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.EnumHelper;

import org.apache.logging.log4j.core.Logger;

import brainstonemod.common.CommonProxy;
import brainstonemod.common.block.BlockBrainLightSensor;
import brainstonemod.common.block.BlockBrainLogicBlock;
import brainstonemod.common.block.BlockBrainStone;
import brainstonemod.common.block.BlockBrainStoneOre;
import brainstonemod.common.block.BlockBrainStoneTrigger;
import brainstonemod.common.block.BlockPulsatingBrainStone;
import brainstonemod.common.block.template.BlockBrainStoneBase;
import brainstonemod.common.handler.BrainStoneEventHandler;
import brainstonemod.common.handler.BrainStoneGuiHandler;
import brainstonemod.common.helper.BSP;
import brainstonemod.common.item.ItemArmorBrainStone;
import brainstonemod.common.item.ItemHoeBrainStone;
import brainstonemod.common.item.ItemSwordBrainStone;
import brainstonemod.common.item.ItemToolBrainStone;
import brainstonemod.common.item.template.ItemBrainStoneBase;
import brainstonemod.common.logicgate.Gate;
import brainstonemod.common.tileentity.TileEntityBlockBrainLightSensor;
import brainstonemod.common.tileentity.TileEntityBlockBrainLogicBlock;
import brainstonemod.common.tileentity.TileEntityBlockBrainStoneTrigger;
import brainstonemod.common.worldgenerators.BrainStoneWorldGenerator;
import brainstonemod.network.BrainStonePacketHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

/**
 * The main file of the mod
 * 
 * @author Yannick Schinko (alias The_BrainStone)
 */
@Mod(modid = BrainStone.MOD_ID, name = BrainStone.NAME, version = BrainStone.VERSION)
public class BrainStone {
	public static final String MOD_ID = "BrainStoneMod";
	public static final String NAME = "Brain Stone Mod";
	public static final String VERSION = "v2.42.462 BETA DEV";

	public static final String packetID_BrainStoneTriggerMobInformation = MOD_ID
			+ "_TMI";

	/** States if the current mod version is a release version or not */
	public static final boolean release = VERSION.toLowerCase().contains(
			"release");
	/** States if the current mod version is a debug version or not */
	public static final boolean debug = VERSION.toLowerCase().contains("debug");
	/** States if the current mod version is a DEV version or not */
	public static final boolean DEV = VERSION.toLowerCase().contains("dev")
			|| VERSION.toLowerCase().contains("prerelease");

	/** A String with the English localization (en_EN) */
	private static final String en = "en_EN";
	/** A String with the German localization (de_DE) */
	private static final String de = "de_DE";

	private static String releaseVersion;
	private static String recommendedVersion;
	private static String latestVersion;

	/**
	 * <tt><table><tr><td>0:</td><td>release</td></tr><tr><td>1:</td><td>recommended</td></tr><tr><td>2:</td><td>latest</td></tr><tr><td>-1:</td><td><i>none</i></td></tr></table></tt>
	 */
	private static byte updateNotification;

	public static final String guiPath = "textures/gui/";
	public static final String armorPath = "textures/armor/";

	/** The instance of this mod */
	@Instance("BrainStoneMod")
	public static BrainStone instance;

	/**
	 * The proxy. Used to perform side dependent operation such as getting the
	 * local player<br>
	 * <br>
	 * - is client-proxy when this is the client<br>
	 * - is server-proxy when this is the server
	 */
	@SidedProxy(clientSide = "brainstonemod.client.ClientProxy", serverSide = "brainstonemod.common.CommonProxy")
	public static CommonProxy proxy;

	/** The BrainStone Tool Material */
	public static ToolMaterial toolBRAINSTONE = EnumHelper.addToolMaterial(
			"BRAINSTONE", 3, 5368, 6F, 5, 25);
	/** The BrainStone Armor Material */
	public static ArmorMaterial armorBRAINSTONE = EnumHelper.addArmorMaterial(
			"BRAINSTONE", 114, new int[] { 2, 6, 5, 2 }, 25);

	/**
	 * This Map maps the different mob types of the BrainStoneTrigger to the
	 * corresponding Classes
	 */
	private final static HashMap<Side, LinkedHashMap<String, Class<?>[]>> triggerEntities = new HashMap<Side, LinkedHashMap<String, Class<?>[]>>();
	/**
	 * A HashMap with the all blocks.<br>
	 * &emsp;<b>key:</b> The internal id<br>
	 * &emsp;<b>value:</b> The actual block
	 */
	private static HashMap<String, Block> blocks = new HashMap<String, Block>();
	/**
	 * A HashMap with the all items.<br>
	 * &emsp;<b>key:</b> The internal id<br>
	 * &emsp;<b>value:</b> The actual item
	 */
	private static HashMap<String, Item> items = new HashMap<String, Item>();
	/**
	 * A HashMap with the all items.<br>
	 * &emsp;<b>key:</b> The internal id<br>
	 * &emsp;<b>value:</b> The actual item
	 */
	private static HashMap<String, Achievement> achievements = new HashMap<String, Achievement>();

	/**
	 * Preinitialization. Reads the ids from the config file and fills the block
	 * and item HashMaps with the blocks and items.
	 * 
	 * @param event
	 *            The MCForge PreInitializationEvent
	 */
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		if ((Gate.Gates == null) || Gate.Gates.isEmpty()) {
			BSP.throwNullPointerException("Well, that should NOT have happenend! This IS a HUGE problem if you notice this please report it to yannick@tedworld.de.\nThanks!\n\nDeveloper Information:\nThe Map of the Gates is EMPTY!\nIs gates null: "
					+ (Gate.Gates == null));
		}

		BSP.setUpLogger((Logger) event.getModLog());

		loadConfig(event);
		retriveCurrentVersions();
		generateMcModInfoFile(event);
		generateBlocksAndItems();

		// Registering blocks and items.
		registerBlocks();
		registerItems();

		// Generating Achievements here because the blocks and items need to be
		// registered at this moment.
		generateAchievements();
	}

	/**
	 * Initialization. Registers the client render information, the GuiHandler,
	 * the blocks, the TileEntitys, adds the names, the recipes, smeltings,
	 * registers the FuelHandler, the WordGenerator and adds the localizations.
	 * 
	 * @param event
	 *            The MCForge InitializationEvent
	 */
	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.registerRenderInformation();
		NetworkRegistry.INSTANCE.registerGuiHandler(this,
				new BrainStoneGuiHandler());

		registerTileEntitys(); // TileEntitys
		addRecipes(); // Recipes
		addSmeltings(); // Smeltings
		// Ore Generation

		// TODO split into two classes and find good values for the world
		// generator
		GameRegistry.registerWorldGenerator(new BrainStoneWorldGenerator(), 0);
		// Event Handler
		FMLCommonHandler.instance().bus()
				.register(new BrainStoneEventHandler());
		// Register Packet
		NetworkRegistry.INSTANCE.newChannel(
				packetID_BrainStoneTriggerMobInformation,
				new BrainStonePacketHandler());

		proxy.registerOre();
	}

	/**
	 * Postinitialization. Fills the triggerEntity's for the BrainStoneTrigger
	 * 
	 * @param event
	 *            The MCForge PostInitializationEvent
	 * @throws Throwable
	 */
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) throws Throwable {
		fillTriggerEntities();
	}

	/**
	 * This method is client side called when a player joins the game. Both for
	 * a server or a single player world.
	 */
	public static void onPlayerJoinClient(EntityPlayer entity,
			PlayerLoggedInEvent event) {
		if (!latestVersion.equals("") && !recommendedVersion.equals("")
				&& !releaseVersion.equals("")) {
			switch (updateNotification) {
			case 0:
				if (isHigherVersion(VERSION, releaseVersion)) {
					sendToPlayer(
							entity,
							"§a A new Version of the BSM is available!\n§l§c========== §4"
									+ releaseVersion
									+ "§c ==========\n"
									+ "§1Download it at §ehttp://adf.ly/2002096/release§1\nor §ehttps://github.com/BrainStone/brainstone§1!");
				}

				break;
			case 1:
				if (isHigherVersion(VERSION, releaseVersion)
						&& !isHigherVersion(releaseVersion, recommendedVersion)) {
					sendToPlayer(
							entity,
							"§a A new Version of the BSM is available!\n§l§c========== §4"
									+ releaseVersion
									+ "§c ==========\n"
									+ "§1Download it at §ehttp://adf.ly/2002096/release§1\nor §ehttps://github.com/BrainStone/brainstone§1!");
				} else if (isHigherVersion(VERSION, recommendedVersion)) {
					sendToPlayer(
							entity,
							"§a A new recommended DEV Version of the BSM is available!\n§l§c========== §4"
									+ recommendedVersion
									+ "§c ==========\n"
									+ "§1Download it at §ehttp://adf.ly/2002096/recommended§1\nor §ehttps://github.com/BrainStone/brainstone§1!");
				}

				break;
			case 2:
				if (isHigherVersion(VERSION, releaseVersion)
						&& !isHigherVersion(releaseVersion, recommendedVersion)
						&& !isHigherVersion(releaseVersion, latestVersion)) {
					sendToPlayer(
							entity,
							"§a A new Version of the BSM is available!\n§l§c========== §4"
									+ releaseVersion
									+ "§c ==========\n"
									+ "§1Download it at §ehttp://adf.ly/2002096/release§1\nor §ehttps://github.com/BrainStone/brainstone§1!");
				} else if (isHigherVersion(VERSION, recommendedVersion)
						&& !isHigherVersion(recommendedVersion, latestVersion)) {
					sendToPlayer(
							entity,
							"§a A new recommended DEV Version of the BSM is available!\n§l§c========== §4"
									+ recommendedVersion
									+ "§c ==========\n"
									+ "§1Download it at §ehttp://adf.ly/2002096/recommended§1\nor §ehttps://github.com/BrainStone/brainstone§1!");
				} else if (isHigherVersion(VERSION, latestVersion)) {
					sendToPlayer(
							entity,
							"§a A new DEV Version of the BSM is available!\n§l§c========== §4"
									+ latestVersion
									+ "§c ==========\n"
									+ "§1Download it at §ehttp://adf.ly/2002096/latest§1\nor §ehttps://github.com/BrainStone/brainstone§1!");
				}

				break;
			}
		}
	}

	// DOCME
	public static void onPlayerJoinServer(EntityPlayer player,
			PlayerLoggedInEvent event) {
		BrainStonePacketHandler
				.sendBrainStoneTriggerMobInformationPacketToPlayer(player);
	}

	/**
	 * Sends a chat message to the current player. Only works client side
	 * 
	 * @param message
	 *            the message to be sent
	 */
	private static void sendToPlayer(EntityPlayer player, String message) {
		((ICommandSender) player)
				.addChatMessage(new ChatComponentText(message));
	}

	/**
	 * Checks if the new version is higher than the current one
	 * 
	 * @param currentVersion
	 *            The version which is considered current
	 * @param newVersion
	 *            The version which is considered new
	 * @return Whether the new version is higher than the current one or not
	 */
	private static boolean isHigherVersion(String currentVersion,
			String newVersion) {
		final int[] _current = splitVersion(currentVersion);
		final int[] _new = splitVersion(newVersion);

		return (_current[0] < _new[0])
				|| ((_current[0] == _new[0]) && (_current[1] < _new[1]))
				|| ((_current[0] == _new[0]) && (_current[1] == _new[1]) && (_current[2] < _new[2]));
	}

	/**
	 * Splits a version in its number components (Format ".\d+\.\d+\.\d+.*" )
	 * 
	 * @param Version
	 *            The version to be splitted (Format is important!
	 * @return The numeric version components as an integer array
	 */
	private static int[] splitVersion(String Version) {
		final String[] tmp = Version.substring(1).split(" ")[0].split("\\.");
		final int size = tmp.length;
		final int out[] = new int[size];

		for (int i = 0; i < size; i++) {
			out[i] = Integer.parseInt(tmp[i]);
		}

		return out;
	}

	/**
	 * Downloads the current versions of this mod from github.com
	 */
	private static void retriveCurrentVersions() {
		try {
			releaseVersion = get_content(new URL(
					"http://download.brainstonemod.tk/downloads/release/.version")
					.openConnection());

			recommendedVersion = get_content(new URL(
					"http://download.brainstonemod.tk/downloads/recommended/.version")
					.openConnection());

			latestVersion = get_content(new URL(
					"http://download.brainstonemod.tk/downloads/latest/.version")
					.openConnection());

		} catch (final MalformedURLException e) {
			BSP.warnException_noAddon(e,
					"The Versions will be empty. No internet connection!");

			releaseVersion = "";
			recommendedVersion = "";
			latestVersion = "";
		} catch (final IOException e) {
			BSP.warnException_noAddon(e,
					"The Versions will be empty. No internet connection!");

			releaseVersion = "";
			recommendedVersion = "";
			latestVersion = "";
		}
	}

	// DOCME
	private static String get_content(URLConnection con) throws IOException {
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
		event.getModMetadata().modId = MOD_ID;
		event.getModMetadata().name = NAME;
		event.getModMetadata().version = VERSION;
		event.getModMetadata().url = "http://minecraft.de/showthread.php?89926";
		event.getModMetadata().credits = "The_BrainStone(Code, Textures, Ideas), Herr_Kermit(Textures), Jobbel(Name)";
		event.getModMetadata().authorList = Arrays.asList(new String[] {
				"The_BrainStone", "Herr_Kermit" });
		event.getModMetadata().description = "The Brain Stone Mod adds a new block type. It is called Brain Stone. It is very rare but you can make many different intelligent sensor blocks! An example is the BrainStoneTrigger. It's a block that triggers if an entity is on top. All these intelligent blocks are highly adjustable! There are also tools. The are as fast as iron tools but you can havrest more than 5,368 blocks! (Diamond tools only 1,561). The latest feature is the PulsatingBrainStoneBlock. It is the craziest block you have ever seen! It will throw you and animals through the air or will add random potion effects! You acan make yourself immune to these effect by wearing the full set of the newly adden BrainStoneArmor.\nBut see by yourself and enjoy!\n\n\nAnd thanks for downloading and supporting this mod!\n\n\n\nIf you think this mod caused a game crash (what should not happen by the way XD) send an email with the error log to yannick@tedworld.de!\n\nThank you!"
				+ "\n\n\n\nCurrent Versions:\n    release:          "
				+ releaseVersion
				+ "\n    recommended:   "
				+ recommendedVersion
				+ "\n    latest:            " + latestVersion;
		event.getModMetadata().logoFile = "/assets/brainstonemod/textures/Logo_500x200.png";
		event.getModMetadata().updateUrl = (updateNotification == -1) ? ""
				: ("https://raw.github.com/BrainStone/brainstone/master/builds/"
						+ ((updateNotification == 0) ? "release"
								: ((updateNotification == 1) ? "recommended"
										: "latest")) + "/BrainStoneMod.zip");
		event.getModMetadata().parent = "";
		event.getModMetadata().screenshots = new String[] {};
	}

	/**
	 * Adds the recipes.
	 */
	private static void addRecipes() {
		GameRegistry.addRecipe(new ItemStack(dirtyBrainStone(), 1),
				new Object[] { "XX", "XX", 'X', brainStoneDust() });
		GameRegistry.addRecipe(new ItemStack(brainLightSensor(), 1),
				new Object[] { "XGX", "XBX", "XRX", 'X', Blocks.stone, 'G',
						Blocks.glass, 'B', brainStone(), 'R', Items.redstone });
		GameRegistry.addRecipe(new ItemStack(brainStoneTrigger(), 1),
				new Object[] { "XXX", "RRR", "XBX", 'X', Blocks.stone, 'B',
						brainStone(), 'R', Items.redstone });
		GameRegistry.addRecipe(new ItemStack(brainLogicBlock(), 1),
				new Object[] { "SRS", "RPR", "SRS", 'S', Blocks.stone, 'P',
						brainProcessor(), 'R', Items.redstone });
		GameRegistry.addRecipe(new ItemStack(pulsatingBrainStone(), 1),
				new Object[] { "dBd", "BDB", "dBd", 'd', brainStoneDust(), 'B',
						brainStone(), 'D', Items.diamond });
		GameRegistry.addRecipe(new ItemStack(brainStoneSword(), 1),
				new Object[] { "B", "B", "S", 'S', Items.stick, 'B',
						brainStone() });
		GameRegistry.addRecipe(new ItemStack(brainStoneShovel(), 1),
				new Object[] { "B", "S", "S", 'S', Items.stick, 'B',
						brainStone() });
		GameRegistry.addRecipe(new ItemStack(brainStonePickaxe(), 1),
				new Object[] { "BBB", " S ", " S ", 'S', Items.stick, 'B',
						brainStone() });
		GameRegistry.addRecipe(new ItemStack(brainStoneAxe(), 1), new Object[] {
				"BB", "BS", " S", 'S', Items.stick, 'B', brainStone() });
		GameRegistry.addRecipe(new ItemStack(brainStoneHoe(), 1), new Object[] {
				"BB", " S", " S", 'S', Items.stick, 'B', brainStone() });
		GameRegistry.addRecipe(new ItemStack(brainProcessor(), 4),
				new Object[] { "TRT", "SBS", "TRT", 'B', brainStone(), 'S',
						Items.redstone, 'T', Blocks.redstone_torch, 'R',
						Items.repeater });
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
		GameRegistry.addSmelting(dirtyBrainStone(), new ItemStack(brainStone(),
				1, 0), 3.0F);
	}

	// DOCME
	private static void fillTriggerEntities() throws Throwable {
		BSP.debug("Filling triggerEntities");

		LinkedHashMap<String, Class<?>[]> tempTriggerEntities = new LinkedHashMap<String, Class<?>[]>();

		tempTriggerEntities.put("gui.brainstone.player",
				new Class<?>[] { EntityPlayer.class });
		tempTriggerEntities.put("gui.brainstone.item",
				new Class<?>[] { EntityBoat.class, EntityFishHook.class,
						EntityItem.class, EntityMinecart.class,
						EntityTNTPrimed.class, EntityXPOrb.class });
		tempTriggerEntities.put("gui.brainstone.projectile", new Class[] {
				EntityArrow.class, EntityThrowable.class, EntityEnderEye.class,
				EntityFireball.class });

		final Map<?, ?> allEntities = EntityList.IDtoClassMapping;
		final int length = allEntities.size();
		final Object[] keys = allEntities.keySet().toArray(new Object[length]);
		Object key;
		Class<?> value;

		for (int i = 0; i < length; i++) {
			key = keys[i];
			value = (Class<?>) allEntities.get(key);

			if ((value != null) && (!Modifier.isAbstract(value.getModifiers()))
					&& (EntityLiving.class.isAssignableFrom(value))
					&& (!EntityLiving.class.equals(value))) {
				tempTriggerEntities.put("entity."
						+ EntityList.classToStringMapping.get(value) + ".name",
						new Class[] { value });
			}
		}

		triggerEntities.put(Side.SERVER, tempTriggerEntities);

		BSP.debug("Done filling triggerEntities");
	}

	/**
	 * Generates the blocks and items and puts them into the HasMaps.
	 */
	private static void generateBlocksAndItems() {
		// Blocks

		blocks.put("brainStone", new BlockBrainStone(false));
		blocks.put("brainStoneOut", new BlockBrainStone(true));
		blocks.put("brainStoneOre", new BlockBrainStoneOre());
		blocks.put("dirtyBrainStone", (new BlockBrainStoneBase(Material.rock))
				.setHardness(2.4F).setResistance(0.5F).setLightLevel(0.5F)
				.setCreativeTab(CreativeTabs.tabBlock));
		blocks.put("brainLightSensor", new BlockBrainLightSensor());
		blocks.put("brainStoneTrigger", new BlockBrainStoneTrigger());
		blocks.put("brainLogicBlock", new BlockBrainLogicBlock());
		blocks.put("pulsatingBrainStone", new BlockPulsatingBrainStone(false));
		blocks.put("pulsatingBrainStoneEffect", new BlockPulsatingBrainStone(
				true));

		blocks.get("dirtyBrainStone").blockParticleGravity = -0.1F;
		blocks.get("dirtyBrainStone").setHarvestLevel("pickaxe", 2);

		// Items

		items.put("brainStoneDust", (new ItemBrainStoneBase())
				.setCreativeTab(CreativeTabs.tabMaterials));
		items.put("brainProcessor",
				(new ItemBrainStoneBase()).setCreativeTab(CreativeTabs.tabMisc));
		items.put("brainStoneSword", (new ItemSwordBrainStone(toolBRAINSTONE)));
		items.put("brainStoneShovel", (new ItemToolBrainStone(toolBRAINSTONE,
				"spade")));
		items.put("brainStonePickaxe", (new ItemToolBrainStone(toolBRAINSTONE,
				"pickaxe")));
		items.put("brainStoneAxe", (new ItemToolBrainStone(toolBRAINSTONE,
				"axe")));
		items.put("brainStoneHoe", (new ItemHoeBrainStone(toolBRAINSTONE)));
		items.put("brainStoneHelmet", (new ItemArmorBrainStone(armorBRAINSTONE,
				proxy.addArmor("brainstone"), 0)));
		items.put("brainStonePlate", (new ItemArmorBrainStone(armorBRAINSTONE,
				proxy.addArmor("brainstone"), 1)));
		items.put("brainStoneLeggings", (new ItemArmorBrainStone(
				armorBRAINSTONE, proxy.addArmor("brainstone"), 2)));
		items.put("brainStoneBoots", (new ItemArmorBrainStone(armorBRAINSTONE,
				proxy.addArmor("brainstone"), 3)));
	}

	private static void generateAchievements() {
		// Achievements

		achievements.put("WTHIT", (new Achievement("WTHIT", "WTHIT", 8, 2,
				brainStoneDust(), AchievementList.buildBetterPickaxe))
				.registerStat());
		achievements.put("itLives", (new Achievement("itLives", "itLives", 8,
				4, brainStone(), WTHIT())).registerStat());
		achievements.put("intelligentBlocks", (new Achievement(
				"intelligentBlocks", "intelligentBlocks", 10, 5,
				brainLightSensor(), itLives())).registerStat());
		achievements.put("intelligentTools", (new Achievement(
				"intelligentTools", "intelligentTools", 6, 5,
				brainStonePickaxe(), itLives())).registerStat());
		achievements.put("logicBlock", (new Achievement("logicBlock",
				"logicBlock", 12, 7, brainProcessor(), intelligentBlocks()))
				.registerStat());
	}

	/**
	 * Reads the ids from the config file and saves them into "ids".
	 * 
	 * @param event
	 *            The MCForge PreInitializationEven. Needed to access the config
	 *            file
	 */
	private static void loadConfig(FMLPreInitializationEvent event) {
		final Configuration config = new Configuration(
				event.getSuggestedConfigurationFile());
		config.load();

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
		String key;
		Block block;

		for (Entry<String, Block> pair : blocks.entrySet()) {
			key = pair.getKey();
			block = pair.getValue();

			block.setBlockName(key);
			GameRegistry.registerBlock(block, key);
		}
	}

	/**
	 * Registers all the items.
	 */
	private static final void registerItems() {
		String key;
		Item item;

		for (Entry<String, Item> pair : items.entrySet()) {
			key = pair.getKey();
			item = pair.getValue();

			item.setUnlocalizedName(key);
			GameRegistry.registerItem(item, key);
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

	public static final LinkedHashMap<String, Class<?>[]> getClientSideTiggerEntities() {
		return triggerEntities.get(Side.CLIENT);
	}

	public static final void setClientSideTiggerEntities(
			LinkedHashMap<String, Class<?>[]> triggerEntities) {
		BSP.debug("Dumping triggerEntities in setClientSideTriggerEntities");

		for (String key : triggerEntities.keySet()) {
			BSP.debug(key + ":" + Arrays.toString(triggerEntities.get(key)));
		}

		BSP.debug("End of Dump");

		BrainStone.triggerEntities.put(Side.CLIENT, triggerEntities);
	}

	public static final LinkedHashMap<String, Class<?>[]> getServerSideTiggerEntities() {
		return triggerEntities.get(Side.SERVER);
	}

	public static final LinkedHashMap<String, Class<?>[]> getSidedTiggerEntities(
			Side side) {
		return triggerEntities.get(side);
	}

	public static final LinkedHashMap<String, Class<?>[]> getSidedTiggerEntities() {
		return triggerEntities.get(FMLCommonHandler.instance()
				.getEffectiveSide());
	}

	/**
	 * @return the instance of BrainStone
	 */
	public static final Block brainStone() {
		return blocks.get("brainStone");
	}

	/**
	 * @return the instance of BrainStoneOut
	 */
	public static final Block brainStoneOut() {
		return blocks.get("brainStoneOut");
	}

	/**
	 * @return the instance of BrainStoneOre
	 */
	public static final Block brainStoneOre() {
		return blocks.get("brainStoneOre");
	}

	/**
	 * @return the instance of DirtyBrainStone
	 */
	public static final Block dirtyBrainStone() {
		return blocks.get("dirtyBrainStone");
	}

	/**
	 * @return the instance of BrainLightSensor
	 */
	public static final Block brainLightSensor() {
		return blocks.get("brainLightSensor");
	}

	/**
	 * @return the instance of BrainStoneTrigger
	 */
	public static final Block brainStoneTrigger() {
		return blocks.get("brainStoneTrigger");
	}

	/**
	 * @return the instance of BrainLogicBlock
	 */
	public static final Block brainLogicBlock() {
		return blocks.get("brainLogicBlock");
	}

	/**
	 * @return the instance of BrainLogicBlock
	 */
	public static final Block pulsatingBrainStone() {
		return blocks.get("pulsatingBrainStone");
	}

	/**
	 * @return the instance of BrainLogicBlock
	 */
	public static final Block pulsatingBrainStoneEffect() {
		return blocks.get("pulsatingBrainStoneEffect");
	}

	/**
	 * @return the instance of BrainStoneDust
	 */
	public static final Item brainStoneDust() {
		return items.get("brainStoneDust");
	}

	/**
	 * @return the instance of BrainProcessor
	 */
	public static final Item brainProcessor() {
		return items.get("brainProcessor");
	}

	/**
	 * @return the instance of BrainStoneSword
	 */
	public static final Item brainStoneSword() {
		return items.get("brainStoneSword");
	}

	/**
	 * @return the instance of BrainStoneShovel
	 */
	public static final Item brainStoneShovel() {
		return items.get("brainStoneShovel");
	}

	/**
	 * @return the instance of BrainStonePickaxe
	 */
	public static final Item brainStonePickaxe() {
		return items.get("brainStonePickaxe");
	}

	/**
	 * @return the instance of BrainStoneAxe
	 */
	public static final Item brainStoneAxe() {
		return items.get("brainStoneAxe");
	}

	/**
	 * @return the instance of BrainStoneHoe
	 */
	public static final Item brainStoneHoe() {
		return items.get("brainStoneHoe");
	}

	/**
	 * @return the instance of BrainStoneHelmet
	 */
	public static final Item brainStoneHelmet() {
		return items.get("brainStoneHelmet");
	}

	/**
	 * @return the instance of BrainStonePlate
	 */
	public static final Item brainStonePlate() {
		return items.get("brainStonePlate");
	}

	/**
	 * @return the instance of BrainStoneLeggings
	 */
	public static final Item brainStoneLeggings() {
		return items.get("brainStoneLeggings");
	}

	/**
	 * @return the instance of BrainStoneBoots
	 */
	public static final Item brainStoneBoots() {
		return items.get("brainStoneBoots");
	}

	/**
	 * @return the instance of WTHIT
	 */
	public static final Achievement WTHIT() {
		return achievements.get("WTHIT");
	}

	/**
	 * @return the instance of itLives
	 */
	public static final Achievement itLives() {
		return achievements.get("itLives");
	}

	/**
	 * @return the instance of intelligentBlocks
	 */
	public static final Achievement intelligentBlocks() {
		return achievements.get("intelligentBlocks");
	}

	/**
	 * @return the instance of intelligentTools
	 */
	public static final Achievement intelligentTools() {
		return achievements.get("intelligentTools");
	}

	/**
	 * @return the instance of logicBlock
	 */
	public static final Achievement logicBlock() {
		return achievements.get("logicBlock");
	}
}