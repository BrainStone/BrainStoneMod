package brainstonemod;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Logger;

import brainstonemod.client.gui.helper.BrainStoneModCreativeTab;
import brainstonemod.client.handler.BrainStoneClientEvents;
import brainstonemod.client.render.BSTriggerModel;
import brainstonemod.common.CommonProxy;
import brainstonemod.common.achievement.BrainStoneAchievement;
import brainstonemod.common.block.BlockBrainLightSensor;
import brainstonemod.common.block.BlockBrainStone;
import brainstonemod.common.block.BlockBrainStoneOre;
import brainstonemod.common.block.BlockBrainStoneTrigger;
import brainstonemod.common.block.BlockPulsatingBrainStone;
import brainstonemod.common.block.template.BlockBrainStoneBase;
import brainstonemod.common.compat.BrainStoneModules;
import brainstonemod.common.compat.draconicevolution.DraconicEvolutionItems;
import brainstonemod.common.compat.enderio.EnderIOItems;
import brainstonemod.common.config.BrainStoneConfigWrapper;
import brainstonemod.common.handler.BrainStoneEventHandler;
import brainstonemod.common.helper.BSP;
import brainstonemod.common.helper.BrainStoneJarUtils;
import brainstonemod.common.helper.BrainStoneLifeCapacitorUpgrade;
import brainstonemod.common.item.ItemArmorBrainStone;
import brainstonemod.common.item.ItemBrainStoneLifeCapacitor;
import brainstonemod.common.item.ItemEssenceOfLife;
import brainstonemod.common.item.ItemHoeBrainStone;
import brainstonemod.common.item.ItemSwordBrainStone;
import brainstonemod.common.item.ItemToolBrainStone;
import brainstonemod.common.item.template.ItemBrainStoneBase;
import brainstonemod.common.tileentity.TileEntityBrainLightSensor;
import brainstonemod.common.tileentity.TileEntityBrainStoneTrigger;
import brainstonemod.common.worldgenerators.BrainStoneHouseWorldGenerator;
import brainstonemod.common.worldgenerators.BrainStoneOreWorldGenerator;
import brainstonemod.network.BrainStoneGuiHandler;
import brainstonemod.network.BrainStonePacketHelper;
import brainstonemod.network.PacketDispatcher;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
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
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;

/**
 * The main file of the mod
 * 
 * @author Yannick Schinko (alias The_BrainStone)
 * @author The_Fireplace
 */
@Mod(modid = BrainStone.MOD_ID, name = BrainStone.NAME, version = BrainStone.VERSION, dependencies = BrainStoneModules.DEPENDENCIES, certificateFingerprint = BrainStone.FINGERPRINT, guiFactory = BrainStone.GUI_FACTORY, updateJSON = BrainStone.UPDATE_JSON)
public class BrainStone {
	public static final String MOD_ID = "brainstonemod";
	public static final String RESOURCE_PACKAGE = MOD_ID;
	public static final String RESOURCE_PREFIX = RESOURCE_PACKAGE + ":";
	public static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation(RESOURCE_PACKAGE);
	public static final String NAME = "Brain Stone Mod";
	public static final String VERSION = "${version}";
	public static final String FINGERPRINT = "2238d4a92d81ab407741a2fdb741cebddfeacba6";
	public static final String GUI_FACTORY = "brainstonemod.client.gui.config.BrainStoneGuiFactory";
	public static final String BASE_URL = "http://download.brainstonemod.com/";
	public static final String UPDATE_JSON = BASE_URL + "update.json";

	/** The instance of this mod */
	@Instance(MOD_ID)
	public static BrainStone instance;

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

	public static final String guiPath = "textures/gui/";

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

	/** The Stable Pulsating BrainStone Tool Material */
	public static ToolMaterial toolSTABLEPULSATINGBS;
	/** The Stable Pulsating BrainStone Armor Material */
	public static ArmorMaterial armorSTABLEPULSATINGBS;

	/**
	 * This Map maps the different mob types of the BrainStoneTrigger to the
	 * corresponding Classes
	 */
	private static final LinkedHashMap<Side, LinkedHashMap<String, Class<?>[]>> triggerEntities = new LinkedHashMap<>();
	/**
	 * A HashMap with the all blocks.<br>
	 * &emsp;<b>key:</b> The internal id<br>
	 * &emsp;<b>value:</b> The actual block
	 */
	public static final LinkedHashMap<String, Block> blocks = new LinkedHashMap<>();
	/**
	 * A HashMap with the all items.<br>
	 * &emsp;<b>key:</b> The internal id<br>
	 * &emsp;<b>value:</b> The actual item
	 */
	public static final LinkedHashMap<String, Item> items = new LinkedHashMap<>();
	/**
	 * A HashMap with the all items.<br>
	 * &emsp;<b>key:</b> The internal id<br>
	 * &emsp;<b>value:</b> The actual item
	 */
	public static final LinkedHashMap<String, BrainStoneAchievement> achievements = new LinkedHashMap<>();
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
	public static final void onInvalidCertificate(FMLFingerprintViolationEvent event) {
		if (BrainStoneJarUtils.RUNNING_FROM_JAR && BrainStoneJarUtils.SIGNED_JAR)
			VALID_JAR = false;
	}

	/**
	 * Preinitialization. Reads the ids from the config file and fills the block
	 * and item HashMaps with the blocks and items.
	 * 
	 * @param event
	 *            The MCForge PreInitializationEvent
	 */
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		PacketDispatcher.registerPackets();
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

		BrainStoneConfigWrapper.loadConfig(new Configuration(event.getSuggestedConfigurationFile()));
		BrainStoneModules.detectActiveModules();

		generateMcModInfoFile(event);
		createEnums();
		generateBlocksAndItems();

		BrainStoneModules.preInit(event);

		// Registering blocks and items.
		registerBlocks();
		registerItems();

		// Generating Achievements here because the blocks and items need to be
		// registered at this moment.
		generateAchievements();

		if (event.getSide().isClient())
			clPreInit();
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
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new BrainStoneGuiHandler());

		proxy.registerOre();

		registerTileEntitys(); // TileEntitys
		addRecipes(); // Recipes
		addSmeltings(); // Smeltings
		// WorldGen
		GameRegistry.registerWorldGenerator(new BrainStoneHouseWorldGenerator(), 0);
		GameRegistry.registerWorldGenerator(new BrainStoneOreWorldGenerator(), 1);
		// Event Handler
		MinecraftForge.EVENT_BUS.register(eventHandler);

		BrainStoneModules.init(event);
	}

	/**
	 * Postinitialization. Postinitializes the packet pipeline
	 * 
	 * @param event
	 *            The MCForge PostInitializationEvent
	 */
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		/*
		 * if (BrainStoneModules.thaumcraft()) { AspectCreator.initAspects(); }
		 */

		/*
		 * if (BrainStoneModules.MFR()) {
		 * MFRBrainstoneConfig.registerMFRProperties(); }
		 */

		BrainStoneModules.postInit(event);
	}

	/**
	 * Fills the triggerEntity's for the BrainStoneTrigger after the server is
	 * starting.
	 * 
	 * @param event
	 *            The MCForge ServerAboutToStartEvent
	 */
	@EventHandler
	public static final void onServerAboutToStart(FMLServerAboutToStartEvent event) {
		fillTriggerEntities();
	}

	/**
	 * Loads the brainStoneLifeCapacitor mapping
	 * 
	 * @param event
	 *            The MCForge ServerStartingEvent
	 */
	@EventHandler
	public static final void onServerStarting(FMLServerStartingEvent event) {
		brainStoneLifeCapacitor().newPlayerCapacitorMapping(DimensionManager.getCurrentSaveRootDirectory());
	}

	/**
	 * This method is client side called when a player joins the game. Both for
	 * a server or a single player world.
	 */
	public static final void onPlayerJoinClient(EntityPlayer player, ClientConnectedToServerEvent event) {
		if (!VALID_JAR) {
			sendToPlayer(player,
					TextFormatting.DARK_RED + "The .jar file of the BrainStoneMod appears to be corrupted\n"
							+ TextFormatting.DARK_RED + "or modified!\n" + TextFormatting.DARK_RED
							+ "Please DO NOT use it as it may cause harm to your computer!\n" + TextFormatting.YELLOW
							+ "You can download a fresh .jar file from here\n" + TextFormatting.DARK_BLUE
							+ "https://download.brainstonemod.com " + TextFormatting.YELLOW + "!");
		} else if (!BrainStoneJarUtils.SIGNED_JAR && !DEV_ENV) {
			sendToPlayer(player,
					TextFormatting.DARK_RED + "The .jar file of the BrainStoneMod is not signed!\n"
							+ TextFormatting.YELLOW + "If you did not create this version yourself download a\n"
							+ TextFormatting.YELLOW + "fresh .jar file from here " + TextFormatting.DARK_BLUE
							+ "https://download.brainstonemod.com " + TextFormatting.YELLOW + "!");
		}
	}

	/**
	 * This method is server side called when a player joins the game. Both for
	 * a server or a single player world.
	 */
	public static final void onPlayerJoinServer(EntityPlayer player, PlayerLoggedInEvent event) {
		BrainStonePacketHelper.sendBrainStoneTriggerMobInformationPacketToPlayer(player);
		BrainStonePacketHelper.sendServerOverridesPacket(player);
	}

	/**
	 * Sends a chat message to the current player. Only works client side
	 * 
	 * @param message
	 *            the message to be sent
	 */
	public static final void sendToPlayer(EntityPlayer player, String message) {
		String[] lines = message.split("\n");

		for (String line : lines)
			player.addChatMessage(new TextComponentString(line));
	}

	private static final void createEnums() {
		toolBRAINSTONE = EnumHelper.addToolMaterial("BRAINSTONE", 5, 5368, 6.0f, 5.0f, 25);
		armorBRAINSTONE = EnumHelper.addArmorMaterial("BRAINSTONE", "BRAINSTONE", 114, new int[] { 2, 6, 5, 2 }, 25,
				SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0.0f);

		toolSTABLEPULSATINGBS = EnumHelper.addToolMaterial("STABLEPULSATINGBS", 9, 21472, 18.0f, 20.0f, 50);
		armorSTABLEPULSATINGBS = EnumHelper.addArmorMaterial("STABLEPULSATINGBS", "STABLEPULSATINGBS", 456,
				new int[] { 12, 32, 24, 12 }, 50, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0.0f);
	}

	/**
	 * Generates the mcmod.info file.
	 */
	private static final void generateMcModInfoFile(FMLPreInitializationEvent event) {
		event.getModMetadata().modId = MOD_ID;
		event.getModMetadata().name = NAME;
		event.getModMetadata().version = VERSION;
		event.getModMetadata().url = "https://minecraft.curseforge.com/projects/brain-stone-mod";
		event.getModMetadata().credits = "The_BrainStone(Code, Textures, Ideas), The_Fireplace(Code), Herr_Kermit(Textures), Jobbel(Name)";
		event.getModMetadata().authorList = Arrays.asList("The_BrainStone", "The_Fireplace", "Herr_Kermit");
		event.getModMetadata().description = "This mod adds the mysterious block BrainStone. You can craft almost magical things from it.\nBut see yourself!\n\n\nThanks for downloading and supporting this mod!";
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

		switch (currentLanguage) {
		case de:
			// German logo

			logoFile += "de";
			break;
		case en:
			// English logo

			logoFile += "en";
			break;
		default:
			// Unsupported language =>
			// English logo

			logoFile += "en";
			break;
		}

		event.getModMetadata().logoFile = logoFile + ".png";
	}

	/**
	 * Adds the recipes.
	 */
	private static final void addRecipes() {
		addRecipe(new ItemStack(dirtyBrainStone(), 1), "XX", "XX", 'X', "dustBrainstone");
		addRecipe(new ItemStack(brainLightSensor(), 1), "XGX", "XBX", "XPX", 'X', "stone", 'G', "blockGlass", 'B',
				"brainstone", 'P', brainProcessor());
		addRecipe(new ItemStack(brainStoneTrigger(), 1), "XXX", "RPR", "XBX", 'X', "stone", 'B', "brainstone", 'R',
				"dustRedstone", 'P', brainProcessor());
		addRecipe(new ItemStack(pulsatingBrainStone(), 1), "dBd", "BDB", "dBd", 'd', "dustBrainstone", 'B',
				"brainstone", 'D', "gemDiamond");
		addRecipe(new ItemStack(pulsatingBrainStone(), 1), "BdB", "dDd", "BdB", 'd', "dustBrainstone", 'B',
				"brainstone", 'D', "gemDiamond");
		addRecipe(new ItemStack(stablePulsatingBrainStone(), 1), "EPE", "PSP", "EPE", 'E', essenceOfLife(), 'P',
				pulsatingBrainStone(), 'S', Items.NETHER_STAR);
		addRecipe(new ItemStack(stablePulsatingBrainStone(), 1), "PEP", "ESE", "PEP", 'E', essenceOfLife(), 'P',
				pulsatingBrainStone(), 'S', Items.NETHER_STAR);
		addRecipe(new ItemStack(brainStoneSword(), 1), "B", "B", "S", 'S', "stickWood", 'B', "brainstone");
		addRecipe(new ItemStack(brainStoneShovel(), 1), "B", "S", "S", 'S', "stickWood", 'B', "brainstone");
		addRecipe(new ItemStack(brainStonePickaxe(), 1), "BBB", " S ", " S ", 'S', "stickWood", 'B', "brainstone");
		addRecipe(new ItemStack(brainStoneAxe(), 1), "BB", "BS", " S", 'S', "stickWood", 'B', "brainstone");
		addRecipe(new ItemStack(brainStoneHoe(), 1), "BB", " S", " S", 'S', "stickWood", 'B', "brainstone");
		addRecipe(new ItemStack(brainStoneHelmet(), 1), "BBB", "B B", 'B', "brainstone");
		addRecipe(new ItemStack(brainStonePlate(), 1), "B B", "BBB", "BBB", 'B', "brainstone");
		addRecipe(new ItemStack(brainStoneLeggings(), 1), "BBB", "B B", "B B", 'B', "brainstone");
		addRecipe(new ItemStack(brainStoneBoots(), 1), "B B", "B B", 'B', "brainstone");
		addRecipe(new ItemStack(brainProcessor(), 4), "TRT", "SBS", "TRT", 'B', "brainstone", 'S', "dustRedstone", 'T',
				Blocks.REDSTONE_TORCH, 'R', Items.REPEATER);
		addRecipe(new ItemStack(stablePulsatingBrainStoneSword(), 1), "B", "B", "S", 'S', "blockRedstone", 'B',
				stablePulsatingBrainStone());
		addRecipe(new ItemStack(stablePulsatingBrainStoneShovel(), 1), "B", "S", "S", 'S', "blockRedstone", 'B',
				stablePulsatingBrainStone());
		addRecipe(new ItemStack(stablePulsatingBrainStonePickaxe(), 1), "BBB", " S ", " S ", 'S', "blockRedstone", 'B',
				stablePulsatingBrainStone());
		addRecipe(new ItemStack(stablePulsatingBrainStoneAxe(), 1), "BB", "BS", " S", 'S', "blockRedstone", 'B',
				stablePulsatingBrainStone());
		addRecipe(new ItemStack(stablePulsatingBrainStoneHoe(), 1), "BB", " S", " S", 'S', "blockRedstone", 'B',
				stablePulsatingBrainStone());
		addRecipe(new ItemStack(stablePulsatingBrainStoneHelmet(), 1), "BBB", "B B", 'B', stablePulsatingBrainStone());
		addRecipe(new ItemStack(stablePulsatingBrainStonePlate(), 1), "B B", "BBB", "BBB", 'B',
				stablePulsatingBrainStone());
		addRecipe(new ItemStack(stablePulsatingBrainStoneLeggings(), 1), "BBB", "B B", "B B", 'B',
				stablePulsatingBrainStone());
		addRecipe(new ItemStack(stablePulsatingBrainStoneBoots(), 1), "B B", "B B", 'B', stablePulsatingBrainStone());

		// Capacitor Recipes
		GameRegistry.addRecipe(new BrainStoneLifeCapacitorUpgrade(BrainStoneLifeCapacitorUpgrade.Upgrade.CAPACITY));
		GameRegistry.addRecipe(new BrainStoneLifeCapacitorUpgrade(BrainStoneLifeCapacitorUpgrade.Upgrade.CHARGING));

		Object craftingS = (BrainStoneModules.enderIO()) ? EnderIOItems.getSentientEnder()
				: new ItemStack(Items.SKULL, 1, 1);
		Object craftingX = (BrainStoneModules.enderIO()) ? EnderIOItems.getXPRod() : Items.BLAZE_ROD;
		Object craftingC = (BrainStoneModules.enderIO()) ? EnderIOItems.getOctadicCapacitor() : "dustRedstone";
		Object craftingH = (BrainStoneModules.draconicEvolution()) ? DraconicEvolutionItems.getDragonHeart()
				: new ItemStack(Items.GOLDEN_APPLE, 1, 1);

		BrainStone.addRecipe(new ItemStack(brainStoneLifeCapacitor(), 1), "SBX", "CHC", " P ", 'S', craftingS, 'B',
				brainProcessor(), 'X', craftingX, 'C', craftingC, 'H', craftingH, 'P',
				stablePulsatingBrainStonePlate());
	}

	@SideOnly(Side.CLIENT)
	public void clPreInit() {
		StateMapperBase ignoreState = new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState iBlockState) {
				return BSTriggerModel.variantTag;
			}
		};
		ModelLoader.setCustomStateMapper(brainStoneTrigger(), ignoreState);

		MinecraftForge.EVENT_BUS.register(new BrainStoneClientEvents());

		ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation(MOD_ID + ":brainStoneTrigger",
				"inventory");
		final int DEFAULT_ITEM_SUBTYPE = 0;
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(brainStoneTrigger()), DEFAULT_ITEM_SUBTYPE,
				itemModelResourceLocation);
	}

	public static final void addRecipe(ItemStack stack, Object... args) {
		GameRegistry.addRecipe(new ShapedOreRecipe(stack, args));
	}

	/**
	 * Adds the smeltings.
	 */
	private static final void addSmeltings() {
		GameRegistry.addSmelting(dirtyBrainStone(), new ItemStack(brainStone(), 1, 0), 3.0F);
	}

	// DOCME
	private static final void fillTriggerEntities() {
		if ((getServerSideTriggerEntities() == null) || (getServerSideTriggerEntities().size() == 0)) {
			BSP.debug("Filling triggerEntities");

			final LinkedHashMap<String, Class<?>[]> tempTriggerEntities = new LinkedHashMap<>();

			tempTriggerEntities.put("gui.brainstone.player", new Class<?>[] { EntityPlayer.class });
			tempTriggerEntities.put("gui.brainstone.item", new Class<?>[] { EntityBoat.class, EntityFishHook.class,
					EntityItem.class, EntityMinecart.class, EntityTNTPrimed.class, EntityXPOrb.class });
			tempTriggerEntities.put("gui.brainstone.projectile", new Class[] { EntityArrow.class, EntityThrowable.class,
					EntityEnderEye.class, EntityFireball.class });

			for (final Entry<String, Class<? extends Entity>> entry : EntityList.NAME_TO_CLASS.entrySet()) {
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
	private static final void verifyTriggerEntity(LinkedHashMap<String, Class<?>[]> tempTriggerEntities, String name,
			Class<?> entityClass) {
		if ((entityClass != null) && (!Modifier.isAbstract(entityClass.getModifiers()))
				&& (EntityLiving.class.isAssignableFrom(entityClass))) {
			tempTriggerEntities.put("entity." + name + ".name", new Class[] { entityClass });
		}
	}

	/**
	 * Generates the blocks and items and puts them into the HasMaps.
	 */
	private static final void generateBlocksAndItems() {
		// Blocks
		blocks.put("brainStone", new BlockBrainStone(false));
		blocks.put("brainStoneOut", new BlockBrainStone(true));
		blocks.put("brainStoneOre", new BlockBrainStoneOre());
		blocks.put("dirtyBrainStone", (new BlockBrainStoneBase(Material.ROCK)).setHardness(2.4F).setResistance(0.5F)
				.setLightLevel(0.5F).setCreativeTab(BrainStone.getCreativeTab(CreativeTabs.BUILDING_BLOCKS)));
		blocks.put("brainLightSensor", new BlockBrainLightSensor());
		blocks.put("brainStoneTrigger", new BlockBrainStoneTrigger());
		blocks.put("pulsatingBrainStone", new BlockPulsatingBrainStone(false));
		blocks.put("pulsatingBrainStoneEffect", new BlockPulsatingBrainStone(true));
		blocks.put("stablePulsatingBrainStone",
				(new BlockBrainStoneBase(Material.ROCK)).setHardness(4.0F).setResistance(1.5F).setLightLevel(1.0F)
						.setCreativeTab(BrainStone.getCreativeTab(CreativeTabs.BUILDING_BLOCKS)));

		dirtyBrainStone().blockParticleGravity = -0.1F;
		dirtyBrainStone().setHarvestLevel("pickaxe", 2);

		stablePulsatingBrainStone().blockParticleGravity = -0.2F;
		stablePulsatingBrainStone().setHarvestLevel("pickaxe", 4);

		// Items
		items.put("brainStoneDust",
				(new ItemBrainStoneBase()).setCreativeTab(BrainStone.getCreativeTab(CreativeTabs.MATERIALS)));
		items.put("brainProcessor",
				(new ItemBrainStoneBase()).setCreativeTab(BrainStone.getCreativeTab(CreativeTabs.MISC)));
		items.put("brainStoneSword", new ItemSwordBrainStone(toolBRAINSTONE));
		items.put("brainStoneShovel", new ItemToolBrainStone(toolBRAINSTONE, "spade"));
		items.put("brainStonePickaxe", new ItemToolBrainStone(toolBRAINSTONE, "pickaxe"));
		items.put("brainStoneAxe", new ItemToolBrainStone(toolBRAINSTONE, "axe"));
		items.put("brainStoneHoe", new ItemHoeBrainStone(toolBRAINSTONE));
		items.put("brainStoneHelmet", new ItemArmorBrainStone(armorBRAINSTONE, EntityEquipmentSlot.HEAD));
		items.put("brainStonePlate", new ItemArmorBrainStone(armorBRAINSTONE, EntityEquipmentSlot.CHEST));
		items.put("brainStoneLeggings", new ItemArmorBrainStone(armorBRAINSTONE, EntityEquipmentSlot.LEGS));
		items.put("brainStoneBoots", new ItemArmorBrainStone(armorBRAINSTONE, EntityEquipmentSlot.FEET));

		items.put("essenceOfLife", new ItemEssenceOfLife());

		items.put("stablePulsatingBrainStoneSword", new ItemSwordBrainStone(toolSTABLEPULSATINGBS));
		items.put("stablePulsatingBrainStoneShovel", new ItemToolBrainStone(toolSTABLEPULSATINGBS, "spade"));
		items.put("stablePulsatingBrainStonePickaxe", new ItemToolBrainStone(toolSTABLEPULSATINGBS, "pickaxe"));
		items.put("stablePulsatingBrainStoneAxe", new ItemToolBrainStone(toolSTABLEPULSATINGBS, "axe"));
		items.put("stablePulsatingBrainStoneHoe", new ItemHoeBrainStone(toolSTABLEPULSATINGBS));
		items.put("stablePulsatingBrainStoneHelmet",
				new ItemArmorBrainStone(armorSTABLEPULSATINGBS, EntityEquipmentSlot.HEAD));
		items.put("stablePulsatingBrainStonePlate",
				new ItemArmorBrainStone(armorSTABLEPULSATINGBS, EntityEquipmentSlot.CHEST));
		items.put("stablePulsatingBrainStoneLeggings",
				new ItemArmorBrainStone(armorSTABLEPULSATINGBS, EntityEquipmentSlot.LEGS));
		items.put("stablePulsatingBrainStoneBoots",
				new ItemArmorBrainStone(armorSTABLEPULSATINGBS, EntityEquipmentSlot.FEET));
		items.put("brainStoneLifeCapacitor", (new ItemBrainStoneLifeCapacitor()));
	}

	private static final void generateAchievements() {
		// Achievements
		new BrainStoneAchievement("WTHIT", 0, 0, brainStoneDust(), AchievementList.BUILD_BETTER_PICKAXE);
		new BrainStoneAchievement("itLives", 2, 0, brainStone(), WTHIT());
		new BrainStoneAchievement("intelligentBlocks", 3, 2, brainLightSensor(), itLives());
		new BrainStoneAchievement("intelligentTools", 3, -2, brainStonePickaxe(), itLives());
		new BrainStoneAchievement("lifeCapacitor", 5, -4, brainStoneLifeCapacitor(), intelligentTools()).setSpecial();

		BrainStoneModules.addAchievement();

		AchievementPage.registerAchievementPage(new AchievementPage("Brain Stone Mod",
				achievements.values().toArray(new BrainStoneAchievement[achievements.size()])));
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

			block.setUnlocalizedName(key);
			block.setRegistryName(key);
			GameRegistry.register(block);
			GameRegistry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
			proxy.rmm(block);
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
			item.setRegistryName(key);
			GameRegistry.register(item);
			proxy.rmm(item);
		}
	}

	/**
	 * Registers all the TileEntiys.
	 */
	private static final void registerTileEntitys() {
		GameRegistry.registerTileEntity(TileEntityBrainLightSensor.class, "TileEntityBlockBrainLightSensor");
		GameRegistry.registerTileEntity(TileEntityBrainStoneTrigger.class, "TileEntityBlockBrainStoneTrigger");
	}

	public static final CreativeTabs getCreativeTab(CreativeTabs defaultTab) {
		if (BrainStoneConfigWrapper.getEnableCreativeTab()) {
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
		TileEntityBrainStoneTrigger.retryFailedTileEntities();
	}

	public static final LinkedHashMap<String, Class<?>[]> getServerSideTriggerEntities() {
		return triggerEntities.get(Side.SERVER);
	}

	public static final LinkedHashMap<String, Class<?>[]> getSidedTriggerEntities() {
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
	 * @return the instance of Brain Stone Life Capacitor
	 */
	public static final ItemBrainStoneLifeCapacitor brainStoneLifeCapacitor() {
		return (ItemBrainStoneLifeCapacitor) BrainStone.items.get("brainStoneLifeCapacitor");
	}

	/**
	 * @return the instance of WTHIT
	 */
	public static final BrainStoneAchievement WTHIT() {
		return achievements.get("WTHIT");
	}

	/**
	 * @return the instance of itLives
	 */
	public static final BrainStoneAchievement itLives() {
		return achievements.get("itLives");
	}

	/**
	 * @return the instance of intelligentBlocks
	 */
	public static final BrainStoneAchievement intelligentBlocks() {
		return achievements.get("intelligentBlocks");
	}

	/**
	 * @return the instance of intelligentTools
	 */
	public static final BrainStoneAchievement intelligentTools() {
		return achievements.get("intelligentTools");
	}

	/**
	 * @return the instance of lifeCapacitor
	 */
	public static final BrainStoneAchievement lifeCapacitor() {
		return achievements.get("lifeCapacitor");
	}
}