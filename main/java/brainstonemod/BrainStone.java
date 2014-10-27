package brainstonemod;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.bind.DatatypeConverter;

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

import scala.tools.nsc.settings.MutableSettings.EnableSettings;
import brainstonemod.client.gui.helper.BrainStoneModCreativeTab;
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
import brainstonemod.common.helper.BrainStoneClassFinder;
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
import brainstonemod.network.BrainStonePacketHelper;
import brainstonemod.network.BrainStonePacketPipeline;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLModDisabledEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.CoreModManager;
import cpw.mods.fml.relauncher.Side;

/**
 * The main file of the mod
 * 
 * @author Yannick Schinko (alias The_BrainStone)
 */
@Mod(modid = BrainStone.MOD_ID, name = BrainStone.NAME, version = BrainStone.VERSION)
public class BrainStone {
	public static final String MOD_ID = "BrainStoneMod";
	public static final String RESOURCE_PACKAGE = MOD_ID.toLowerCase();
	public static final String RESOURCE_PREFIX = RESOURCE_PACKAGE + ":";
	public static final String NAME = "Brain Stone Mod";
	public static final String VERSION = "v2.49.160 BETA DEV";

	/** The instance of this mod */
	@Instance(MOD_ID)
	public static BrainStone instance;

	public static BrainStonePacketPipeline packetPipeline;

	/** States if the current mod version is a release version or not */
	public static final boolean release = VERSION.toLowerCase().contains(
			"release");
	/** States if the current mod version is a debug version or not */
	public static final boolean debug = VERSION.toLowerCase().contains("debug");
	/** States if the current mod version is a DEV version or not */
	public static final boolean DEV = VERSION.toLowerCase().contains("dev")
			|| VERSION.toLowerCase().contains("prerelease");
	/** States if this is the eclipse working environment or not */
	public static boolean DEV_ENV;
	/** States if this jar is valid or not. */
	public static boolean VALID_JAR = false;

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
	/** Should the custom Creative tab be used? */
	private static boolean enableCreativeTab;

	public static final String guiPath = "textures/gui/";
	public static final String armorPath = "textures/armor/";

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
	public static ToolMaterial toolBRAINSTONE;
	/** The BrainStone Armor Material */
	public static ArmorMaterial armorBRAINSTONE;
	/** The BrainStone Armor Material render index */
	public static int armorBRAINSTONE_RenderIndex;

	/**
	 * This Map maps the different mob types of the BrainStoneTrigger to the
	 * corresponding Classes
	 */
	private static final LinkedHashMap<Side, LinkedHashMap<String, Class<?>[]>> triggerEntities = new LinkedHashMap<Side, LinkedHashMap<String, Class<?>[]>>();
	/**
	 * A HashMap with the all blocks.<br>
	 * &emsp;<b>key:</b> The internal id<br>
	 * &emsp;<b>value:</b> The actual block
	 */
	private static final LinkedHashMap<String, Block> blocks = new LinkedHashMap<String, Block>();
	/**
	 * A HashMap with the all items.<br>
	 * &emsp;<b>key:</b> The internal id<br>
	 * &emsp;<b>value:</b> The actual item
	 */
	private static final LinkedHashMap<String, Item> items = new LinkedHashMap<String, Item>();
	/**
	 * A HashMap with the all items.<br>
	 * &emsp;<b>key:</b> The internal id<br>
	 * &emsp;<b>value:</b> The actual item
	 */
	private static final LinkedHashMap<String, Achievement> achievements = new LinkedHashMap<String, Achievement>();
	/** The custom creative tab */
	private static BrainStoneModCreativeTab tabBrainStoneMod = null;

	/**
	 * Preinitialization. Reads the ids from the config file and fills the block
	 * and item HashMaps with the blocks and items.
	 * 
	 * @param event
	 *            The MCForge PreInitializationEvent
	 */
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		BSP.setUpLogger((Logger) event.getModLog());
		DEV_ENV = isDevEnv();
		BSP.info(release, DEV, DEV_ENV);

		VALID_JAR = DEV_ENV || ((!release && !DEV) || validateJarFile());

		packetPipeline = new BrainStonePacketPipeline();

		if ((Gate.Gates == null) || Gate.Gates.isEmpty()) {
			BSP.throwNullPointerException("Well, that should NOT have happenend! This IS a HUGE problem if you notice this please report it to yannick@tedworld.de.\nThanks!\n\nDeveloper Information:\nThe Map of the Gates is EMPTY!\nIs gates null: "
					+ (Gate.Gates == null));
		}

		loadConfig(event);
		retriveCurrentVersions();
		generateMcModInfoFile(event);
		createEnums();
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
		// Register PacketPipeline
		packetPipeline.initialise();

		proxy.registerOre();
	}

	/**
	 * Postinitialization. Postinitializes the packet pipeline
	 * 
	 * @param event
	 *            The MCForge PostInitializationEvent
	 */
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		// Post initializing the pipeline
		packetPipeline.postInitialise();
	}

	/**
	 * Fills the triggerEntity's for the BrainStoneTrigger after the server is
	 * starting.
	 * 
	 * @param event
	 *            The MCForge ServerStartingEvent
	 */
	@EventHandler
	public void onServerStarting(FMLServerStartingEvent event) {
		fillTriggerEntities();
	}

	/**
	 * Disabeling the mod. Will unload and unregister everything registered
	 * before
	 * 
	 * @param event
	 *            The MCForge ModDisabledEvent
	 */
	@EventHandler
	public void disableMod(FMLModDisabledEvent event) {
		packetPipeline = null;

		triggerEntities.clear();
		blocks.clear();
		items.clear();
		achievements.clear();

		// TODO What do we do here?

		BSP.warn("This mod is not properly unloaded! There might be troubles!");
	}

	/**
	 * This method is client side called when a player joins the game. Both for
	 * a server or a single player world.
	 */
	public static void onPlayerJoinClient(EntityPlayer player,
			ClientConnectedToServerEvent event) {
		if (!VALID_JAR) {
			sendToPlayer(
					player,
					"§4The .jar file of the BrainStoneMod appears to be corrupted\n§4or modified!\n§4Please DO NOT use it as it may cause harm to your computer!\n§eYou can download a fresh .jar file from here\n§1http://download.brainstonemod.tk §e!");
		}

		if (!latestVersion.equals("") && !recommendedVersion.equals("")
				&& !releaseVersion.equals("")) {
			switch (updateNotification) {
			case 0:
				if (isHigherVersion(VERSION, releaseVersion)) {
					sendToPlayer(
							player,
							"§a A new Version of the BSM is available!\n§l§c========== §4"
									+ releaseVersion
									+ "§c ==========\n"
									+ "§1Download it at §ehttp://adf.ly/2002096/release§1\nor §ehttp://download.brainstonemod.tk §1!");
				}

				break;
			case 1:
				if (isHigherVersion(VERSION, releaseVersion)
						&& !isHigherVersion(releaseVersion, recommendedVersion)) {
					sendToPlayer(
							player,
							"§a A new Version of the BSM is available!\n§l§c========== §4"
									+ releaseVersion
									+ "§c ==========\n"
									+ "§1Download it at §ehttp://adf.ly/2002096/release§1\nor §ehttp://download.brainstonemod.tk §1!");
				} else if (isHigherVersion(VERSION, recommendedVersion)) {
					sendToPlayer(
							player,
							"§a A new recommended DEV Version of the BSM is available!\n§l§c========== §4"
									+ recommendedVersion
									+ "§c ==========\n"
									+ "§1Download it at §ehttp://adf.ly/2002096/recommended§1\nor §ehttp://download.brainstonemod.tk §1!");
				}

				break;
			case 2:
				if (isHigherVersion(VERSION, releaseVersion)
						&& !isHigherVersion(releaseVersion, recommendedVersion)
						&& !isHigherVersion(releaseVersion, latestVersion)) {
					sendToPlayer(
							player,
							"§a A new Version of the BSM is available!\n§l§c========== §4"
									+ releaseVersion
									+ "§c ==========\n"
									+ "§1Download it at §ehttp://adf.ly/2002096/release§1\nor §ehttp://download.brainstonemod.tk §1!");
				} else if (isHigherVersion(VERSION, recommendedVersion)
						&& !isHigherVersion(recommendedVersion, latestVersion)) {
					sendToPlayer(
							player,
							"§a A new recommended DEV Version of the BSM is available!\n§l§c========== §4"
									+ recommendedVersion
									+ "§c ==========\n"
									+ "§1Download it at §ehttp://adf.ly/2002096/recommended§1\nor §ehttp://download.brainstonemod.tk §1!");
				} else if (isHigherVersion(VERSION, latestVersion)) {
					sendToPlayer(
							player,
							"§a A new DEV Version of the BSM is available!\n§l§c========== §4"
									+ latestVersion
									+ "§c ==========\n"
									+ "§1Download it at §ehttp://adf.ly/2002096/latest§1\nor §ehttp://download.brainstonemod.tk §1!");
				}

				break;
			}
		}
	}

	/**
	 * This method is server side called when a player joins the game. Both for
	 * a server or a single player world.
	 */
	public static void onPlayerJoinServer(EntityPlayer player,
			PlayerLoggedInEvent event) {
		BrainStonePacketHelper
				.sendBrainStoneTriggerMobInformationPacketToPlayer(player);
	}

	private static boolean isDevEnv() {
		try {
			final Field f = CoreModManager.class
					.getDeclaredField("deobfuscatedEnvironment");
			final boolean accessible = f.isAccessible();
			f.setAccessible(true);

			final boolean ouput = f.getBoolean(null);

			f.setAccessible(accessible);

			return ouput;
		} catch (final NoSuchFieldException e) {
			BSP.warnException(e);
		} catch (final SecurityException e) {
			BSP.warnException(e);
		} catch (final IllegalArgumentException e) {
			BSP.warnException(e);
		} catch (final IllegalAccessException e) {
			BSP.warnException(e);
		}

		return false;
	}

	private static boolean validateJarFile() {
		try {
			final String hash_online = get_content(new URL(
					"http://download.brainstonemod.tk/"
							+ VERSION.substring(1).replace(" BETA ", "_")
							+ "/sha512.hash").openConnection());
			final String jarHash = getJarHash();

			BSP.info("Jar Hash: " + jarHash, "Online Hash: " + hash_online);

			return hash_online.equalsIgnoreCase(jarHash);
		} catch (final MalformedURLException e) {
			BSP.infoException_noAddon(e);
		} catch (final IOException e) {
			BSP.infoException_noAddon(e);
		}

		return true;
	}

	private static String getJarHash() {
		try {
			final InputStream fis = new FileInputStream(
					BrainStoneClassFinder.findPathJar(null));

			final byte[] buffer = new byte[1024];
			final MessageDigest complete = MessageDigest.getInstance("SHA-512");
			int numRead;

			do {
				numRead = fis.read(buffer);

				if (numRead > 0) {
					complete.update(buffer, 0, numRead);
				}
			} while (numRead != -1);

			fis.close();

			return DatatypeConverter.printHexBinary(complete.digest());
		} catch (final FileNotFoundException e) {
			BSP.errorException(e);
		} catch (final IOException e) {
			BSP.errorException(e);
		} catch (final NoSuchAlgorithmException e) {
			BSP.errorException(
					e,
					"This is something reallyy bad! You don't have the SHA-512 algorithm! You should have that!");
		}

		return "";
	}

	private static void createEnums() {
		toolBRAINSTONE = EnumHelper.addToolMaterial("BRAINSTONE", 3, 5368, 6F,
				5, 25);
		armorBRAINSTONE = EnumHelper.addArmorMaterial("BRAINSTONE", 114,
				new int[] { 2, 6, 5, 2 }, 25);

		armorBRAINSTONE_RenderIndex = proxy.addArmor("brainstone");
	}

	/**
	 * Sends a chat message to the current player. Only works client side
	 * 
	 * @param message
	 *            the message to be sent
	 */
	private static void sendToPlayer(EntityPlayer player, String message) {
		String[] lines = message.split("\n");

		for (String line : lines)
			((ICommandSender) player)
					.addChatMessage(new ChatComponentText(line));
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
					"http://download.brainstonemod.tk/release/.version")
					.openConnection());

			recommendedVersion = get_content(new URL(
					"http://download.brainstonemod.tk/recommended/.version")
					.openConnection());

			latestVersion = get_content(new URL(
					"http://download.brainstonemod.tk/latest/.version")
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
	 * Generates the mcmod.info file.
	 */
	private static void generateMcModInfoFile(FMLPreInitializationEvent event) {
		event.getModMetadata().modId = MOD_ID;
		event.getModMetadata().name = NAME;
		event.getModMetadata().version = VERSION;
		event.getModMetadata().url = "http://www.planetminecraft.com/mod/125sspwip-brainstonemod-v14827-beta-release/";
		event.getModMetadata().credits = "The_BrainStone(Code, Textures, Ideas), Herr_Kermit(Textures), Jobbel(Name)";
		event.getModMetadata().authorList = Arrays.asList(new String[] {
				"The_BrainStone", "Herr_Kermit" });
		event.getModMetadata().description = "This mod adds the mysterious block BrainStone. You can craft almost magical things from it.\nBut see yourself!\n\n\nThanks for downloading and supporting this mod!"
				+ "\n\n\n\nCurrent Versions:\n    release:          "
				+ releaseVersion
				+ "\n    recommended:   "
				+ recommendedVersion
				+ "\n    latest:            " + latestVersion;
		event.getModMetadata().updateUrl = (updateNotification == -1) ? ""
				: ("http://adf.ly/2002096/" + ((updateNotification == 0) ? "release"
						: ((updateNotification == 1) ? "recommended" : "latest")));
		event.getModMetadata().parent = "";
		event.getModMetadata().screenshots = new String[] {};

		String logoFile = "/assets/brainstonemod/textures/logo/Logo_";
		String currentLanguage;

		try {
			// To prevent game crash when getCurrentLanguage() throws a
			// NullPointerException

			currentLanguage = FMLCommonHandler.instance().getCurrentLanguage();
		} catch (final NullPointerException e) {
			// Default to English, print the exception and add a warning to the
			// mod description!

			currentLanguage = en;
			BSP.warnException(
					e,
					"This is nothing really to worry about. But it is interesting.\nIf you see this please report it anyway. It will help me get rid of this!");
			event.getModMetadata().description += "\n\nSomething went wrong while trying to detect your language at start up. Please check the current Forge log and report the stack trace.\nThank you!";
		}

		if (currentLanguage.equals(de)) {
			// German logo

			logoFile += "de";
		} else if (currentLanguage.equals(en)) {
			// English logo

			logoFile += "en";
		} else {
			// Unsupported language =>
			// English logo

			logoFile += "en";
		}

		event.getModMetadata().logoFile = logoFile + ".png";
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
	private static void fillTriggerEntities() {
		if ((getServerSideTiggerEntities() == null)
				|| (getServerSideTiggerEntities().size() == 0)) {
			BSP.debug("Filling triggerEntities");

			final LinkedHashMap<String, Class<?>[]> tempTriggerEntities = new LinkedHashMap<String, Class<?>[]>();

			tempTriggerEntities.put("gui.brainstone.player",
					new Class<?>[] { EntityPlayer.class });
			tempTriggerEntities.put("gui.brainstone.item", new Class<?>[] {
					EntityBoat.class, EntityFishHook.class, EntityItem.class,
					EntityMinecart.class, EntityTNTPrimed.class,
					EntityXPOrb.class });
			tempTriggerEntities.put("gui.brainstone.projectile", new Class[] {
					EntityArrow.class, EntityThrowable.class,
					EntityEnderEye.class, EntityFireball.class });

			for (final Entry<String, Class<?>> entry : (Set<Entry<String, Class<?>>>) EntityList.stringToClassMapping
					.entrySet()) {
				verifyTriggerEntity(tempTriggerEntities, entry.getKey(),
						entry.getValue());
			}

			triggerEntities.put(Side.SERVER, tempTriggerEntities);

			BSP.debug("Done filling triggerEntities");
		}
	}

	/**
	 * Verifies and adds (if verification was successful) it to the passed map.
	 * 
	 * @param tempTriggerEntities
	 *            The map to append the class to
	 * @param name
	 *            The name it will be registered with
	 * @param entityClass
	 *            The class it refers to
	 */
	private static void verifyTriggerEntity(
			LinkedHashMap<String, Class<?>[]> tempTriggerEntities, String name,
			Class<?> entityClass) {
		if ((entityClass != null)
				&& (!Modifier.isAbstract(entityClass.getModifiers()))
				&& (EntityLiving.class.isAssignableFrom(entityClass))) {
			tempTriggerEntities.put("entity." + name + ".name",
					new Class[] { entityClass });
		}
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
				armorBRAINSTONE_RenderIndex, 0)));
		items.put("brainStonePlate", (new ItemArmorBrainStone(armorBRAINSTONE,
				armorBRAINSTONE_RenderIndex, 1)));
		items.put("brainStoneLeggings", (new ItemArmorBrainStone(
				armorBRAINSTONE, armorBRAINSTONE_RenderIndex, 2)));
		items.put("brainStoneBoots", (new ItemArmorBrainStone(armorBRAINSTONE,
				armorBRAINSTONE_RenderIndex, 3)));
		items.put("essenceOfLive", (new ItemBrainStoneBase())
				.setCreativeTab(CreativeTabs.tabMaterials));
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

		final String str = config
				.getString(
						"DisplayUpdates",
						"Display",
						DEV ? "recommended" : (release ? "release" : "latest"),
						"What update notifications do you want to recieve?\nValues are: release, recommended, latest");
		updateNotification = (byte) ((str.equals("none") || str.equals("off")) ? -1
				: (str.equals("recommended") ? 1 : (str.equals("latest") ? 2
						: 0)));

		enableCreativeTab = config
				.getBoolean("EnableCreativeTab", "Display", true,
						"Do you want to have a custom Creative Tab for this mod?");

		if (enableCreativeTab) {
			tabBrainStoneMod = new BrainStoneModCreativeTab();
		}

		config.save();
	}

	/**
	 * Registers all the blocks.
	 */
	private static final void registerBlocks() {
		String key;
		Block block;

		for (final Entry<String, Block> pair : blocks.entrySet()) {
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

		for (final Entry<String, Item> pair : items.entrySet()) {
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

	public static final CreativeTabs getCreativeTab(CreativeTabs defaultTab) {
		return (enableCreativeTab) ? tabBrainStoneMod : defaultTab;
	}

	public static final LinkedHashMap<String, Class<?>[]> getClientSideTiggerEntities() {
		return triggerEntities.get(Side.CLIENT);
	}

	public static final void setClientSideTiggerEntities(
			LinkedHashMap<String, Class<?>[]> triggerEntities) {
		BSP.debug("Dumping triggerEntities in setClientSideTriggerEntities");

		for (final String key : triggerEntities.keySet()) {
			BSP.debug(key + ":" + Arrays.toString(triggerEntities.get(key)));
		}

		BSP.debug("End of Dump");

		BrainStone.triggerEntities.put(Side.CLIENT, triggerEntities);

		// Make sure all TileEntityBlockBrainStoneTrigger objects are
		// initialized correctly
		TileEntityBlockBrainStoneTrigger.retryFailedTileEntities();
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