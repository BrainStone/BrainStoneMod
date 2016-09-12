package brainstonemod;

import brainstonemod.client.gui.helper.BrainStoneModCreativeTab;
import brainstonemod.common.CommonProxy;
import brainstonemod.common.api.BrainStoneModules;
import brainstonemod.common.api.cofh.mfr.MFRBrainstoneConfig;
import brainstonemod.common.api.enderio.EnderIOItems;
import brainstonemod.common.api.enderio.EnderIORecipies;
import brainstonemod.common.api.tconstruct.TinkersConstructItems;
import brainstonemod.common.api.tconstruct.TinkersContructMaterialBrainStone;
import brainstonemod.common.api.thaumcraft.AspectCreator;
import brainstonemod.common.block.*;
import brainstonemod.common.block.template.BlockBrainStoneBase;
import brainstonemod.common.handler.BrainStoneEventHandler;
import brainstonemod.common.handler.BrainStoneGuiHandler;
import brainstonemod.common.helper.*;
import brainstonemod.common.item.*;
import brainstonemod.common.item.template.ItemBrainStoneBase;
import brainstonemod.common.logicgate.Gate;
import brainstonemod.common.tileentity.TileEntityBlockBrainLightSensor;
import brainstonemod.common.tileentity.TileEntityBlockBrainLogicBlock;
import brainstonemod.common.tileentity.TileEntityBlockBrainStoneTrigger;
import brainstonemod.common.worldgenerators.BrainStoneHouseWorldGenerator;
import brainstonemod.common.worldgenerators.BrainStoneOreWorldGenerator;
import brainstonemod.network.BrainStonePacketHelper;
import brainstonemod.network.BrainStonePacketPipeline;
import brainstonemod.network.packet.BrainStoneLifeCapacitorMap;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.versioning.ComparableVersion;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.command.ICommandSender;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.*;
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
import net.minecraft.launchwrapper.Launch;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.EnumHelper;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Logger;
import tconstruct.armor.TinkerArmor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;

/**
 * The main file of the mod
 * 
 * @author Yannick Schinko (alias The_BrainStone)
 */
@Mod(modid = BrainStone.MOD_ID, name = BrainStone.NAME, version = BrainStone.VERSION, dependencies = BrainStone.DEPENDENCIES, canBeDeactivated = true, certificateFingerprint = BrainStone.FINGERPRINT, guiFactory = BrainStone.GUI_FACTORY)
public class BrainStone {
	public static final String MOD_ID = "BrainStoneMod";
	public static final String RESOURCE_PACKAGE = MOD_ID.toLowerCase();
	public static final String RESOURCE_PREFIX = RESOURCE_PACKAGE + ":";
	public static final String NAME = "Brain Stone Mod";
	public static final String VERSION = "${version}";
	public static final String DEPENDENCIES = "after:EnderIO;after:MineFactoryReloaded;after:Thaumcraft;after:TConstruct";
	public static final String FINGERPRINT = "2238d4a92d81ab407741a2fdb741cebddfeacba6";
	public static final String GUI_FACTORY = "brainstonemod.client.gui.config.BrainStoneGuiFactory";
	public static final String BASE_URL = "http://download.brainstonemod.com/";

	/** The instance of this mod */
	@Instance(MOD_ID)
	public static BrainStone instance;

	public static BrainStonePacketPipeline packetPipeline;

	/** States if the current mod version is a release version or not */
	public static final boolean release = VERSION.toLowerCase().contains("release");
	/** States if the current mod version is a debug version or not */
	public static final boolean debug = VERSION.toLowerCase().contains("debug");
	/** States if the current mod version is a DEV version or not */
	public static final boolean DEV = VERSION.toLowerCase().contains("dev")
			|| VERSION.toLowerCase().contains("prerelease");
	/** States if this is the eclipse working environment or not */
	public static boolean DEV_ENV = (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
	/** States if this jar is valid or not. */
	public static boolean VALID_JAR = true;

	/** A String with the English localization (en_EN) */
	private static final String en = "en_EN";
	/** A String with the German localization (de_DE) */
	private static final String de = "de_DE";

	private static String releaseVersion;
	private static String recommendedVersion;
	private static String latestVersion;

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

	/** The Stable Pulsating BrainStone Tool Material */
	public static ToolMaterial toolSTABLEPULSATINGBS;
	/** The Stable Pulsating BrainStone Armor Material */
	public static ArmorMaterial armorSTABLEPULSATINGBS;
	/** The Stable Pulsating BrainStone Armor Material render index */
	public static int armorSTABLEPULSATINGBS_RenderIndex;

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
	 * Called when the signature does not match the expected hash. Marks the jar
	 * invalid in that case.<br>
	 * Does nothing in case the mod is not running from a jar
	 * 
	 * @param event
	 *            he MCForge FingerprintViolationEvent
	 */
	@EventHandler
	public static void onInvalidCertificate(FMLFingerprintViolationEvent event) {
		if (BrainStoneJarUtils.RUNNING_FROM_JAR && BrainStoneJarUtils.SIGNED_JAR)
			VALID_JAR = false;
	}

	/**
	 * Preinitialization. Reads the ids from the config file and fills the block
	 * and item HashMaps with the blocks and items.
	 * 
	 * @param event
	 *            The MCForge PreInitializationEvent
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Level logLevel = DEV_ENV ? Level.INFO : Level.DEBUG;

		BSP.setUpLogger((Logger) event.getModLog());
		BSP.log(logLevel, "Is release version: " + release, "Is DEV version: " + DEV, "Is DEV environment: " + DEV_ENV,
				"Is running from jar: " + BrainStoneJarUtils.RUNNING_FROM_JAR);

		if (VALID_JAR && BrainStoneJarUtils.RUNNING_FROM_JAR) {
			try {
				BrainStoneJarUtils.verifyJar();
			} catch (SecurityException e) {
				BSP.warn(e.getMessage());
				VALID_JAR = false;
			}
		}

		BSP.log(VALID_JAR ? logLevel : Level.WARN, "Jar is " + (VALID_JAR ? "" : "not ") + "valid!",
				"Jar is " + (BrainStoneJarUtils.SIGNED_JAR ? "" : "not ") + "signed!");

		checkForModules();

		packetPipeline = new BrainStonePacketPipeline();

		if ((Gate.Gates == null) || Gate.Gates.isEmpty()) {
			BSP.throwNullPointerException(
					"Well, that should NOT have happenend! This IS a HUGE problem if you notice this please report it to yannick@tedworld.de.\nThanks!\n\nDeveloper Information:\nThe Map of the Gates is EMPTY!\nIs gates null: "
							+ (Gate.Gates == null));
		}

		BrainStoneConfigHelper.loadConfig(new Configuration(event.getSuggestedConfigurationFile()));
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
		BrainStoneEventHandler eventHandler = new BrainStoneEventHandler();

		proxy.registerRenderInformation();
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new BrainStoneGuiHandler());

		registerTileEntitys(); // TileEntitys
		addRecipes(); // Recipes
		addSmeltings(); // Smeltings
		// WorldGen
		GameRegistry.registerWorldGenerator(new BrainStoneHouseWorldGenerator(), 0);
		GameRegistry.registerWorldGenerator(new BrainStoneOreWorldGenerator(), 1);
		// Event Handler
		FMLCommonHandler.instance().bus().register(eventHandler);
		MinecraftForge.EVENT_BUS.register(eventHandler);
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

		if (BrainStoneModules.thaumcraft()) {
			AspectCreator.initAspects();
		}

		if (BrainStoneModules.MFR()) {
			MFRBrainstoneConfig.registerMFRProperties();
		}

		if (BrainStoneModules.enderIO()) {
			EnderIORecipies.registerEnderIORecipies();
		}

		if (BrainStoneModules.tinkersConstruct()) {
			TinkersContructMaterialBrainStone.initToolMaterials();
		}
	}

	/**
	 * Fills the triggerEntity's for the BrainStoneTrigger after the server is
	 * starting.
	 * 
	 * @param event
	 *            The MCForge ServerStartingEvent
	 */
	@EventHandler
	public static void onServerStarting(FMLServerStartingEvent event) {
		fillTriggerEntities();

		if (BrainStoneModules.energy()) {
			brainStoneLifeCapacitor().newPlayerCapacitorMapping(DimensionManager.getCurrentSaveRootDirectory());
		}
	}

	/**
	 * This method is client side called when a player joins the game. Both for
	 * a server or a single player world.
	 */
	public static void onPlayerJoinClient(EntityPlayer player, ClientConnectedToServerEvent event) {
		if (!VALID_JAR) {
			sendToPlayer(player,
					EnumChatFormatting.DARK_RED + "The .jar file of the BrainStoneMod appears to be corrupted\n"
							+ EnumChatFormatting.DARK_RED + "or modified!\n" + EnumChatFormatting.DARK_RED
							+ "Please DO NOT use it as it may cause harm to your computer!\n"
							+ EnumChatFormatting.YELLOW + "You can download a fresh .jar file from here\n"
							+ EnumChatFormatting.DARK_BLUE + "https://download.brainstonemod.com "
							+ EnumChatFormatting.YELLOW + "!");
		} else if (!BrainStoneJarUtils.SIGNED_JAR && !DEV_ENV) {
			sendToPlayer(player,
					EnumChatFormatting.DARK_RED + "The .jar file of the BrainStoneMod is not signed!\n"
							+ EnumChatFormatting.YELLOW + "If you did not create this version yourself download a\n"
							+ EnumChatFormatting.YELLOW + "fresh .jar file from here " + EnumChatFormatting.DARK_BLUE
							+ "https://download.brainstonemod.com " + EnumChatFormatting.YELLOW + "!");
		}

		if (!VERSION.equals("${ver" + "sion}") && !latestVersion.equals("") && !recommendedVersion.equals("")
				&& !releaseVersion.equals("")) {
			switch (BrainStoneConfigHelper.updateNotification()) {
			case 0:
				if (isHigherVersion(VERSION, releaseVersion)) {
					sendToPlayer(player,
							EnumChatFormatting.GREEN + "A new Version of the BSM is available!\n"
									+ EnumChatFormatting.BOLD + EnumChatFormatting.RED + "========== "
									+ EnumChatFormatting.DARK_RED + latestVersion + EnumChatFormatting.RED
									+ " ==========\n" + EnumChatFormatting.DARK_BLUE + "Download it at "
									+ EnumChatFormatting.YELLOW + "http://adf.ly/2002096/release\n"
									+ EnumChatFormatting.DARK_BLUE + "or " + EnumChatFormatting.YELLOW
									+ "https://download.brainstonemod.com " + EnumChatFormatting.DARK_BLUE + "!");
				}

				break;
			case 1:
				if (isHigherVersion(VERSION, releaseVersion) && !isHigherVersion(releaseVersion, recommendedVersion)) {
					sendToPlayer(player,
							EnumChatFormatting.GREEN + "A new Version of the BSM is available!\n"
									+ EnumChatFormatting.BOLD + EnumChatFormatting.RED + "========== "
									+ EnumChatFormatting.DARK_RED + latestVersion + EnumChatFormatting.RED
									+ " ==========\n" + EnumChatFormatting.DARK_BLUE + "Download it at "
									+ EnumChatFormatting.YELLOW + "http://adf.ly/2002096/release\n"
									+ EnumChatFormatting.DARK_BLUE + "or " + EnumChatFormatting.YELLOW
									+ "https://download.brainstonemod.com " + EnumChatFormatting.DARK_BLUE + "!");
				} else if (isHigherVersion(VERSION, recommendedVersion)) {
					sendToPlayer(player,
							EnumChatFormatting.GREEN + "A new recommended DEV Version of the BSM is available!\n"
									+ EnumChatFormatting.BOLD + EnumChatFormatting.RED + "========== "
									+ EnumChatFormatting.DARK_RED + latestVersion + EnumChatFormatting.RED
									+ " ==========\n" + EnumChatFormatting.DARK_BLUE + "Download it at "
									+ EnumChatFormatting.YELLOW + "http://adf.ly/2002096/recommended\n"
									+ EnumChatFormatting.DARK_BLUE + "or " + EnumChatFormatting.YELLOW
									+ "https://download.brainstonemod.com " + EnumChatFormatting.DARK_BLUE + "!");
				}

				break;
			case 2:
				if (isHigherVersion(VERSION, releaseVersion) && !isHigherVersion(releaseVersion, recommendedVersion)
						&& !isHigherVersion(releaseVersion, latestVersion)) {
					sendToPlayer(player,
							EnumChatFormatting.GREEN + "A new Version of the BSM is available!\n"
									+ EnumChatFormatting.BOLD + EnumChatFormatting.RED + "========== "
									+ EnumChatFormatting.DARK_RED + latestVersion + EnumChatFormatting.RED
									+ " ==========\n" + EnumChatFormatting.DARK_BLUE + "Download it at "
									+ EnumChatFormatting.YELLOW + "http://adf.ly/2002096/release\n"
									+ EnumChatFormatting.DARK_BLUE + "or " + EnumChatFormatting.YELLOW
									+ "https://download.brainstonemod.com " + EnumChatFormatting.DARK_BLUE + "!");
				} else if (isHigherVersion(VERSION, recommendedVersion)
						&& !isHigherVersion(recommendedVersion, latestVersion)) {
					sendToPlayer(player,
							EnumChatFormatting.GREEN + "A new recommended DEV Version of the BSM is available!\n"
									+ EnumChatFormatting.BOLD + EnumChatFormatting.RED + "========== "
									+ EnumChatFormatting.DARK_RED + latestVersion + EnumChatFormatting.RED
									+ " ==========\n" + EnumChatFormatting.DARK_BLUE + "Download it at "
									+ EnumChatFormatting.YELLOW + "http://adf.ly/2002096/recommended\n"
									+ EnumChatFormatting.DARK_BLUE + "or " + EnumChatFormatting.YELLOW
									+ "https://download.brainstonemod.com " + EnumChatFormatting.DARK_BLUE + "!");
				} else if (isHigherVersion(VERSION, latestVersion)) {
					sendToPlayer(player,
							EnumChatFormatting.GREEN + "A new DEV Version of the BSM is available!\n"
									+ EnumChatFormatting.BOLD + EnumChatFormatting.RED + "========== "
									+ EnumChatFormatting.DARK_RED + latestVersion + EnumChatFormatting.RED
									+ " ==========\n" + EnumChatFormatting.DARK_BLUE + "Download it at "
									+ EnumChatFormatting.YELLOW + "http://adf.ly/2002096/latest\n"
									+ EnumChatFormatting.DARK_BLUE + "or " + EnumChatFormatting.YELLOW
									+ "https://download.brainstonemod.com " + EnumChatFormatting.DARK_BLUE + "!");
				}

				break;
			}
		}
	}

	/**
	 * This method is server side called when a player joins the game. Both for
	 * a server or a single player world.
	 */
	public static void onPlayerJoinServer(EntityPlayer player, PlayerLoggedInEvent event) {
		BrainStonePacketHelper.sendBrainStoneTriggerMobInformationPacketToPlayer(player);

		if (BrainStoneModules.energy()) {
			brainStoneLifeCapacitor().getPlayerCapacitorMapping().updateName(player.getUniqueID(), false);
			BrainStone.packetPipeline.sendToAll(new BrainStoneLifeCapacitorMap());
		}
	}

	/**
	 * Sends a chat message to the current player. Only works client side
	 * 
	 * @param message
	 *            the message to be sent
	 */
	public static void sendToPlayer(EntityPlayer player, String message) {
		String[] lines = message.split("\n");

		for (String line : lines)
			((ICommandSender) player).addChatMessage(new ChatComponentText(line));
	}

	private static void checkForModules()
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		BSP.info("Checking available modules:");

		for (Method method : BrainStoneModules.class.getMethods()) {
			Module module = method.getAnnotation(Module.class);

			if (module != null) {
				if ((Boolean) method.invoke(null)) {
					BSP.info("\t" + module.value());
				}
			}
		}

		BSP.debug("Finished checking available modules!");
	}

	private static void createEnums() {
		toolBRAINSTONE = EnumHelper.addToolMaterial("BRAINSTONE", 4, 5368, 6F, 5, 25);
		armorBRAINSTONE = EnumHelper.addArmorMaterial("BRAINSTONE", 114, new int[] { 2, 6, 5, 2 }, 25);

		armorBRAINSTONE_RenderIndex = proxy.addArmor("brainstone");

		toolSTABLEPULSATINGBS = EnumHelper.addToolMaterial("STABLEPULSATINGBS", 5, 21472, 18F, 20, 50);
		armorSTABLEPULSATINGBS = EnumHelper.addArmorMaterial("STABLEPULSATINGBS", 456, new int[] { 12, 32, 24, 12 },
				50);

		armorSTABLEPULSATINGBS_RenderIndex = proxy.addArmor("stablepulsatingbs");
	}

	/**
	 * Checks if the new version is higher than the current one
	 * 
	 * @param currentVersionStr
	 *            The version which is considered current
	 * @param newVersionStr
	 *            The version which is considered new
	 * @return Whether the new version is higher than the current one or not
	 */
	private static boolean isHigherVersion(String currentVersionStr, String newVersionStr) {
		final ComparableVersion currentVersion = new ComparableVersion(currentVersionStr);
		final ComparableVersion newVersion = new ComparableVersion(newVersionStr);

		return currentVersion.compareTo(newVersion) < 0;
	}

	/**
	 * Downloads the current versions of this mod from github.com
	 */
	private static void retriveCurrentVersions() {
		try {
			releaseVersion = get_content("release/.version");

			recommendedVersion = get_content("recommended/.version");

			latestVersion = get_content("latest/.version");

		} catch (final MalformedURLException e) {
			BSP.warnException_noAddon(e, "The Versions will be empty. No internet connection!");

			releaseVersion = "";
			recommendedVersion = "";
			latestVersion = "";
		} catch (final IOException e) {
			BSP.warnException_noAddon(e, "The Versions will be empty. No internet connection!");

			releaseVersion = "";
			recommendedVersion = "";
			latestVersion = "";
		}
	}

	// DOCME
	private static String get_content(String url) throws IOException {
		String output = "";

		try {
			URLConnection con = new URL(BASE_URL + url).openConnection();

			if (con != null) {
				con.setConnectTimeout(500);
				con.setReadTimeout(500);
				con.connect();

				final BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

				String input;

				while ((input = br.readLine()) != null) {
					output = output + input;
				}
				br.close();
			}
		} catch (SocketTimeoutException e) {
			BSP.warnException_noAddon(e);
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
		event.getModMetadata().authorList = Arrays.asList(new String[] { "The_BrainStone", "Herr_Kermit" });
		event.getModMetadata().description = "This mod adds the mysterious block BrainStone. You can craft almost magical things from it.\nBut see yourself!\n\n\nThanks for downloading and supporting this mod!"
				+ "\n\n\n\nCurrent Versions:\n    release:          " + releaseVersion + "\n    recommended:   "
				+ recommendedVersion + "\n    latest:            " + latestVersion;
		event.getModMetadata().updateUrl = (BrainStoneConfigHelper.updateNotification() == -1) ? ""
				: ("http://adf.ly/2002096/" + ((BrainStoneConfigHelper.updateNotification() == 0) ? "release"
						: ((BrainStoneConfigHelper.updateNotification() == 1) ? "recommended" : "latest")));
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
			BSP.warnException(e,
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
		GameRegistry.addRecipe(new ItemStack(dirtyBrainStone(), 1), "XX", "XX", 'X', brainStoneDust());
		GameRegistry.addRecipe(new ItemStack(brainLightSensor(), 1), "XGX", "XBX", "XRX", 'X', Blocks.stone, 'G',
				Blocks.glass, 'B', brainStone(), 'R', Items.redstone);
		GameRegistry.addRecipe(new ItemStack(brainStoneTrigger(), 1), "XXX", "RRR", "XBX", 'X', Blocks.stone, 'B',
				brainStone(), 'R', Items.redstone);
		GameRegistry.addRecipe(new ItemStack(brainLogicBlock(), 1), "SRS", "RPR", "SRS", 'S', Blocks.stone, 'P',
				brainProcessor(), 'R', Items.redstone);
		GameRegistry.addRecipe(new ItemStack(pulsatingBrainStone(), 1), "dBd", "BDB", "dBd", 'd', brainStoneDust(), 'B',
				brainStone(), 'D', Items.diamond);
		GameRegistry.addRecipe(new ItemStack(pulsatingBrainStone(), 1), "BdB", "dDd", "BdB", 'd', brainStoneDust(), 'B',
				brainStone(), 'D', Items.diamond);
		GameRegistry.addRecipe(new ItemStack(stablePulsatingBrainStone(), 1), "EPE", "PSP", "EPE", 'E', essenceOfLife(),
				'P', pulsatingBrainStone(), 'S', Items.nether_star);
		GameRegistry.addRecipe(new ItemStack(stablePulsatingBrainStone(), 1), "PEP", "ESE", "PEP", 'E', essenceOfLife(),
				'P', pulsatingBrainStone(), 'S', Items.nether_star);
		GameRegistry.addRecipe(new ItemStack(brainStoneSword(), 1), "B", "B", "S", 'S', Items.stick, 'B', brainStone());
		GameRegistry.addRecipe(new ItemStack(brainStoneShovel(), 1), "B", "S", "S", 'S', Items.stick, 'B',
				brainStone());
		GameRegistry.addRecipe(new ItemStack(brainStonePickaxe(), 1), "BBB", " S ", " S ", 'S', Items.stick, 'B',
				brainStone());
		GameRegistry.addRecipe(new ItemStack(brainStoneAxe(), 1), "BB", "BS", " S", 'S', Items.stick, 'B',
				brainStone());
		GameRegistry.addRecipe(new ItemStack(brainStoneHoe(), 1), "BB", " S", " S", 'S', Items.stick, 'B',
				brainStone());
		GameRegistry.addRecipe(new ItemStack(brainStoneHelmet(), 1), "BBB", "B B", 'B', brainStone());
		GameRegistry.addRecipe(new ItemStack(brainStonePlate(), 1), "B B", "BBB", "BBB", 'B', brainStone());
		GameRegistry.addRecipe(new ItemStack(brainStoneLeggings(), 1), "BBB", "B B", "B B", 'B', brainStone());
		GameRegistry.addRecipe(new ItemStack(brainStoneBoots(), 1), "B B", "B B", 'B', brainStone());
		GameRegistry.addRecipe(new ItemStack(brainProcessor(), 4), "TRT", "SBS", "TRT", 'B', brainStone(), 'S',
				Items.redstone, 'T', Blocks.redstone_torch, 'R', Items.repeater);
		GameRegistry.addRecipe(new ItemStack(stablePulsatingBrainStoneSword(), 1), "B", "B", "S", 'S',
				Blocks.redstone_block, 'B', stablePulsatingBrainStone());
		GameRegistry.addRecipe(new ItemStack(stablePulsatingBrainStoneShovel(), 1), "B", "S", "S", 'S',
				Blocks.redstone_block, 'B', stablePulsatingBrainStone());
		GameRegistry.addRecipe(new ItemStack(stablePulsatingBrainStonePickaxe(), 1), "BBB", " S ", " S ", 'S',
				Blocks.redstone_block, 'B', stablePulsatingBrainStone());
		GameRegistry.addRecipe(new ItemStack(stablePulsatingBrainStoneAxe(), 1), "BB", "BS", " S", 'S',
				Blocks.redstone_block, 'B', stablePulsatingBrainStone());
		GameRegistry.addRecipe(new ItemStack(stablePulsatingBrainStoneHoe(), 1), "BB", " S", " S", 'S',
				Blocks.redstone_block, 'B', stablePulsatingBrainStone());
		GameRegistry.addRecipe(new ItemStack(stablePulsatingBrainStoneHelmet(), 1), "BBB", "B B", 'B',
				stablePulsatingBrainStone());
		GameRegistry.addRecipe(new ItemStack(stablePulsatingBrainStonePlate(), 1), "B B", "BBB", "BBB", 'B',
				stablePulsatingBrainStone());
		GameRegistry.addRecipe(new ItemStack(stablePulsatingBrainStoneLeggings(), 1), "BBB", "B B", "B B", 'B',
				stablePulsatingBrainStone());
		GameRegistry.addRecipe(new ItemStack(stablePulsatingBrainStoneBoots(), 1), "B B", "B B", 'B',
				stablePulsatingBrainStone());

		if (BrainStoneModules.tinkersConstruct()) {
			GameRegistry.addShapelessRecipe(new ItemStack(TinkerArmor.heartCanister, 1, 5),
					new ItemStack(pulsatingBrainStone(), 1), new ItemStack(essenceOfLife(), 1),
					new ItemStack(TinkerArmor.heartCanister, 1, 3));
			GameRegistry.addShapelessRecipe(new ItemStack(TinkerArmor.heartCanister, 1, 6),
					new ItemStack(TinkerArmor.heartCanister, 1, 4), new ItemStack(TinkerArmor.heartCanister, 1, 5),
					new ItemStack(pulsatingBrainStone(), 1), new ItemStack(essenceOfLife(), 1),
					new ItemStack(TinkerArmor.diamondApple, 1), new ItemStack(Items.golden_apple, 1, 1));
		}

		if (BrainStoneModules.energy()) {
			GameRegistry.addRecipe(new BrainStoneLifeCapacitorUpgrade(BrainStoneLifeCapacitorUpgrade.Upgrade.CAPACITY));
			GameRegistry.addRecipe(new BrainStoneLifeCapacitorUpgrade(BrainStoneLifeCapacitorUpgrade.Upgrade.CHARGING));

			Object craftingS = (BrainStoneModules.enderIO()) ? EnderIOItems.getSentientEnder()
					: new ItemStack(Items.skull, 1, 1);
			Object craftingX = (BrainStoneModules.enderIO()) ? EnderIOItems.getXPRod() : Items.blaze_rod;
			Object craftingC = (BrainStoneModules.enderIO()) ? EnderIOItems.getOctadicCapacitor() : Items.redstone;
			Object craftingH = (BrainStoneModules.tinkersConstruct()) ? TinkersConstructItems.getGreenHeartCanister()
					: new ItemStack(Items.golden_apple, 1, 1);

			GameRegistry.addRecipe(new ItemStack(brainStoneLifeCapacitor(), 1), "SBX", "CHC", " P ", 'S', craftingS,
					'B', brainProcessor(), 'X', craftingX, 'C', craftingC, 'H', craftingH, 'P',
					stablePulsatingBrainStonePlate());
		}
	}

	/**
	 * Adds the smeltings.
	 */
	private static void addSmeltings() {
		GameRegistry.addSmelting(dirtyBrainStone(), new ItemStack(brainStone(), 1, 0), 3.0F);
	}

	// DOCME
	@SuppressWarnings("unchecked")
	private static void fillTriggerEntities() {
		if ((getServerSideTiggerEntities() == null) || (getServerSideTiggerEntities().size() == 0)) {
			BSP.debug("Filling triggerEntities");

			final LinkedHashMap<String, Class<?>[]> tempTriggerEntities = new LinkedHashMap<String, Class<?>[]>();

			tempTriggerEntities.put("gui.brainstone.player", new Class<?>[] { EntityPlayer.class });
			tempTriggerEntities.put("gui.brainstone.item", new Class<?>[] { EntityBoat.class, EntityFishHook.class,
					EntityItem.class, EntityMinecart.class, EntityTNTPrimed.class, EntityXPOrb.class });
			tempTriggerEntities.put("gui.brainstone.projectile", new Class[] { EntityArrow.class, EntityThrowable.class,
					EntityEnderEye.class, EntityFireball.class });

			for (final Entry<String, Class<?>> entry : (Set<Entry<String, Class<?>>>) EntityList.stringToClassMapping
					.entrySet()) {
				verifyTriggerEntity(tempTriggerEntities, entry.getKey(), entry.getValue());
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
	private static void verifyTriggerEntity(LinkedHashMap<String, Class<?>[]> tempTriggerEntities, String name,
			Class<?> entityClass) {
		if ((entityClass != null) && (!Modifier.isAbstract(entityClass.getModifiers()))
				&& (EntityLiving.class.isAssignableFrom(entityClass))) {
			tempTriggerEntities.put("entity." + name + ".name", new Class[] { entityClass });
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
		blocks.put("dirtyBrainStone", (new BlockBrainStoneBase(Material.rock)).setHardness(2.4F).setResistance(0.5F)
				.setLightLevel(0.5F).setCreativeTab(BrainStone.getCreativeTab(CreativeTabs.tabBlock)));
		blocks.put("brainLightSensor", new BlockBrainLightSensor());
		blocks.put("brainStoneTrigger", new BlockBrainStoneTrigger());
		blocks.put("brainLogicBlock", new BlockBrainLogicBlock());
		blocks.put("pulsatingBrainStone", new BlockPulsatingBrainStone(false));
		blocks.put("pulsatingBrainStoneEffect", new BlockPulsatingBrainStone(true));
		blocks.put("stablePulsatingBrainStone",
				(new BlockBrainStoneBase(Material.rock)).setHardness(4.0F).setResistance(1.5F).setLightLevel(1.0F)
						.setCreativeTab(BrainStone.getCreativeTab(CreativeTabs.tabBlock)));

		blocks.get("dirtyBrainStone").blockParticleGravity = -0.1F;
		blocks.get("dirtyBrainStone").setHarvestLevel("pickaxe", 2);

		blocks.get("stablePulsatingBrainStone").blockParticleGravity = -0.2F;
		blocks.get("stablePulsatingBrainStone").setHarvestLevel("pickaxe", 4);

		// Items

		items.put("brainStoneDust",
				(new ItemBrainStoneBase()).setCreativeTab(BrainStone.getCreativeTab(CreativeTabs.tabMaterials)));
		items.put("brainProcessor",
				(new ItemBrainStoneBase()).setCreativeTab(BrainStone.getCreativeTab(CreativeTabs.tabMisc)));
		items.put("brainStoneSword", new ItemSwordBrainStone(toolBRAINSTONE));
		items.put("brainStoneShovel", new ItemToolBrainStone(toolBRAINSTONE, "spade"));
		items.put("brainStonePickaxe", new ItemToolBrainStone(toolBRAINSTONE, "pickaxe"));
		items.put("brainStoneAxe", new ItemToolBrainStone(toolBRAINSTONE, "axe"));
		items.put("brainStoneHoe", new ItemHoeBrainStone(toolBRAINSTONE));
		items.put("brainStoneHelmet", new ItemArmorBrainStone(armorBRAINSTONE, armorBRAINSTONE_RenderIndex, 0));
		items.put("brainStonePlate", new ItemArmorBrainStone(armorBRAINSTONE, armorBRAINSTONE_RenderIndex, 1));
		items.put("brainStoneLeggings", new ItemArmorBrainStone(armorBRAINSTONE, armorBRAINSTONE_RenderIndex, 2));
		items.put("brainStoneBoots", new ItemArmorBrainStone(armorBRAINSTONE, armorBRAINSTONE_RenderIndex, 3));

		items.put("essenceOfLife", new ItemEssenceOfLife());

		items.put("stablePulsatingBrainStoneSword", new ItemSwordBrainStone(toolSTABLEPULSATINGBS));
		items.put("stablePulsatingBrainStoneShovel", new ItemToolBrainStone(toolSTABLEPULSATINGBS, "spade"));
		items.put("stablePulsatingBrainStonePickaxe", new ItemToolBrainStone(toolSTABLEPULSATINGBS, "pickaxe"));
		items.put("stablePulsatingBrainStoneAxe", new ItemToolBrainStone(toolSTABLEPULSATINGBS, "axe"));
		items.put("stablePulsatingBrainStoneHoe", new ItemHoeBrainStone(toolSTABLEPULSATINGBS));
		items.put("stablePulsatingBrainStoneHelmet",
				new ItemArmorBrainStone(armorSTABLEPULSATINGBS, armorSTABLEPULSATINGBS_RenderIndex, 0));
		items.put("stablePulsatingBrainStonePlate",
				new ItemArmorBrainStone(armorSTABLEPULSATINGBS, armorSTABLEPULSATINGBS_RenderIndex, 1));
		items.put("stablePulsatingBrainStoneLeggings",
				new ItemArmorBrainStone(armorSTABLEPULSATINGBS, armorSTABLEPULSATINGBS_RenderIndex, 2));
		items.put("stablePulsatingBrainStoneBoots",
				new ItemArmorBrainStone(armorSTABLEPULSATINGBS, armorSTABLEPULSATINGBS_RenderIndex, 3));

		if (BrainStoneModules.energy()) {
			items.put("brainStoneLifeCapacitor", (new ItemBrainStoneLifeCapacitor()));
		}
	}

	private static void generateAchievements() {
		// Achievements

		String curAch;

		achievements.put(curAch = "WTHIT",
				(new Achievement(curAch, curAch, BrainStoneConfigHelper.getAchievementYPosition(curAch),
						BrainStoneConfigHelper.getAchievementXPosition(curAch), brainStoneDust(),
						AchievementList.buildBetterPickaxe)).registerStat());
		achievements
				.put(curAch = "itLives",
						(new Achievement(curAch, curAch, BrainStoneConfigHelper.getAchievementYPosition(curAch),
								BrainStoneConfigHelper.getAchievementXPosition(curAch), brainStone(), WTHIT()))
										.registerStat());
		achievements.put(curAch = "intelligentBlocks",
				(new Achievement(curAch, curAch, BrainStoneConfigHelper.getAchievementYPosition(curAch),
						BrainStoneConfigHelper.getAchievementXPosition(curAch), brainLightSensor(), itLives()))
								.registerStat());
		achievements.put(curAch = "intelligentTools",
				(new Achievement(curAch, curAch, BrainStoneConfigHelper.getAchievementYPosition(curAch),
						BrainStoneConfigHelper.getAchievementXPosition(curAch), brainStonePickaxe(), itLives()))
								.registerStat());
		achievements.put(curAch = "logicBlock",
				(new Achievement(curAch, curAch, BrainStoneConfigHelper.getAchievementYPosition(curAch),
						BrainStoneConfigHelper.getAchievementXPosition(curAch), brainProcessor(), intelligentBlocks()))
								.registerStat());

		if (BrainStoneConfigHelper.enableAchievementPage()) {
			AchievementPage.registerAchievementPage(new AchievementPage("Brain Stone Mod",
					achievements.values().toArray(new Achievement[achievements.size()])));
		}
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
		GameRegistry.registerTileEntity(TileEntityBlockBrainLightSensor.class, "TileEntityBlockBrainLightSensor");
		GameRegistry.registerTileEntity(TileEntityBlockBrainStoneTrigger.class, "TileEntityBlockBrainStoneTrigger");
		GameRegistry.registerTileEntity(TileEntityBlockBrainLogicBlock.class, "TileEntityBlockBrainLogicBlock");
	}

	public static final CreativeTabs getCreativeTab(CreativeTabs defaultTab) {
		if (BrainStoneConfigHelper.enableCreativeTab()) {
			if (tabBrainStoneMod == null)
				tabBrainStoneMod = new BrainStoneModCreativeTab();

			return tabBrainStoneMod;
		} else
			return defaultTab;
	}

	public static final LinkedHashMap<String, Class<?>[]> getClientSideTiggerEntities() {
		return triggerEntities.get(Side.CLIENT);
	}

	public static final void setClientSideTiggerEntities(LinkedHashMap<String, Class<?>[]> triggerEntities) {
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

	public static final LinkedHashMap<String, Class<?>[]> getSidedTiggerEntities(Side side) {
		return triggerEntities.get(side);
	}

	public static final LinkedHashMap<String, Class<?>[]> getSidedTiggerEntities() {
		return triggerEntities.get(FMLCommonHandler.instance().getEffectiveSide());
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
	 * @return the instance of PulsatingBrainStone
	 */
	public static final Block pulsatingBrainStone() {
		return blocks.get("pulsatingBrainStone");
	}

	/**
	 * @return the instance of PulsatingBrainStoneEffect
	 */
	public static final Block pulsatingBrainStoneEffect() {
		return blocks.get("pulsatingBrainStoneEffect");
	}

	/**
	 * @return the instance of StablePulsatingBrainStone
	 */
	public static final Block stablePulsatingBrainStone() {
		return blocks.get("stablePulsatingBrainStone");
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
	 * @return the instance of Essence Of Live
	 */
	public static final Item essenceOfLife() {
		return items.get("essenceOfLife");
	}

	/**
	 * @return the instance of Stable Pulsating Brain Stone Sword
	 */
	public static final Item stablePulsatingBrainStoneSword() {
		return items.get("stablePulsatingBrainStoneSword");
	}

	/**
	 * @return the instance of Stable Pulsating Brain Stone Shovel
	 */
	public static final Item stablePulsatingBrainStoneShovel() {
		return items.get("stablePulsatingBrainStoneShovel");
	}

	/**
	 * @return the instance of Stable Pulsating Brain Stone Pickaxe
	 */
	public static final Item stablePulsatingBrainStonePickaxe() {
		return items.get("stablePulsatingBrainStonePickaxe");
	}

	/**
	 * @return the instance of Stable Pulsating Brain Stone Axe
	 */
	public static final Item stablePulsatingBrainStoneAxe() {
		return items.get("stablePulsatingBrainStoneAxe");
	}

	/**
	 * @return the instance of Stable Pulsating Brain Stone Hoe
	 */
	public static final Item stablePulsatingBrainStoneHoe() {
		return items.get("stablePulsatingBrainStoneHoe");
	}

	/**
	 * @return the instance of Stable Pulsating Brain Stone Helmet
	 */
	public static final Item stablePulsatingBrainStoneHelmet() {
		return items.get("stablePulsatingBrainStoneHelmet");
	}

	/**
	 * @return the instance of Stable Pulsating Brain Stone Chestplate
	 */
	public final static Item stablePulsatingBrainStonePlate() {
		return items.get("stablePulsatingBrainStonePlate");
	}

	/**
	 * @return the instance of Stable Pulsating Brain Stone Leggings
	 */
	public final static Item stablePulsatingBrainStoneLeggings() {
		return items.get("stablePulsatingBrainStoneLeggings");
	}

	/**
	 * @return the instance of Stable Pulsating Brain Stone Leggings
	 */
	public final static Item stablePulsatingBrainStoneBoots() {
		return items.get("stablePulsatingBrainStoneBoots");
	}

	/**
	 * @return the instance of Brain Stone Live Capacitor
	 */
	public static final ItemBrainStoneLifeCapacitor brainStoneLifeCapacitor() {
		return (ItemBrainStoneLifeCapacitor) items.get("brainStoneLifeCapacitor");
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