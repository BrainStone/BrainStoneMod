package brainstonemod;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.LinkedHashMap;

import org.apache.logging.log4j.Level;

import brainstonemod.client.gui.helper.BrainStoneModCreativeTab;
import brainstonemod.client.handler.BrainStoneClientEvents;
import brainstonemod.common.CommonProxy;
import brainstonemod.common.advancement.CriterionRegistry;
import brainstonemod.common.compat.BrainStoneModules;
import brainstonemod.common.config.BrainStoneConfigWrapper;
import brainstonemod.common.handler.BrainStoneEventHandler;
import brainstonemod.common.helper.BSP;
import brainstonemod.common.helper.BrainStoneJarUtils;
import brainstonemod.common.tileentity.TileEntityBrainLightSensor;
import brainstonemod.common.tileentity.TileEntityBrainStoneTrigger;
import brainstonemod.common.worldgenerator.BrainStoneHouseWorldGenerator;
import brainstonemod.common.worldgenerator.BrainStoneOreWorldGenerator;
import brainstonemod.network.BrainStoneGuiHandler;
import brainstonemod.network.BrainStonePacketHelper;
import brainstonemod.network.PacketDispatcher;
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
import net.minecraft.launchwrapper.Launch;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
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

	/**
	 * The proxy. Used to perform side dependent operation such as getting the
	 * local player<br>
	 * <br>
	 * - is client-proxy when this is the client<br>
	 * - is server-proxy when this is the server
	 */
	@SidedProxy(clientSide = "brainstonemod.client.ClientProxy", serverSide = "brainstonemod.common.CommonProxy")
	public static CommonProxy proxy;

	/**
	 * This Map maps the different mob types of the BrainStoneTrigger to the
	 * corresponding Classes
	 */
	private static final LinkedHashMap<Side, LinkedHashMap<String, Class<?>[]>> triggerEntities = new LinkedHashMap<>();
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
		if (BrainStoneJarUtils.RUNNING_FROM_JAR && BrainStoneJarUtils.SIGNED_JAR) {
			VALID_JAR = false;
		}
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

		BSP.setUpLogger(event.getModLog());
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

		BrainStoneBlocks.generateBlocks();
		BrainStoneItems.generateItems();

		BrainStoneModules.preInit(event);

		// Event Handlers
		MinecraftForge.EVENT_BUS.register(BrainStoneEventHandler.registrar());
		MinecraftForge.EVENT_BUS.register(BrainStoneBlocks.registrar());
		MinecraftForge.EVENT_BUS.register(BrainStoneItems.registrar());
		MinecraftForge.EVENT_BUS.register(BrainStoneRecipes.registrar());
		MinecraftForge.EVENT_BUS.register(BrainStoneSounds.registrar());

		if (event.getSide().isClient()) {
			MinecraftForge.EVENT_BUS.register(BrainStoneClientEvents.registrar());
			MinecraftForge.EVENT_BUS.register(proxy);
		}
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
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new BrainStoneGuiHandler());

		registerTileEntitys(); // TileEntitys
		BrainStoneRecipes.addRecipes(); // Recipes
		BrainStoneRecipes.addSmeltings(); // Smeltings
		// WorldGen
		GameRegistry.registerWorldGenerator(new BrainStoneHouseWorldGenerator(), 0);
		GameRegistry.registerWorldGenerator(new BrainStoneOreWorldGenerator(), 1);

		// Advancement stuff
		CriterionRegistry.init();

		BrainStoneModules.init(event);

		// Registering ore dict
		proxy.registerOreDict();
	}

	/**
	 * Postinitialization. Postinitializes the packet pipeline
	 *
	 * @param event
	 *            The MCForge PostInitializationEvent
	 */
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
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
		BrainStoneItems.brainStoneLifeCapacitor()
				.newPlayerCapacitorMapping(DimensionManager.getCurrentSaveRootDirectory());
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

		for (String line : lines) {
			player.sendMessage(new TextComponentString(line));
		}
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

			for (final ResourceLocation entry : EntityList.getEntityNameList()) {
				verifyTriggerEntity(tempTriggerEntities, EntityList.getTranslationName(entry),
						EntityList.getClass(entry));
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
	 * Registers all the TileEnties.
	 */
	@SuppressWarnings("deprecation")
	private static final void registerTileEntitys() {
		GameRegistry.registerTileEntity(TileEntityBrainLightSensor.class, "TileEntityBlockBrainLightSensor");
		GameRegistry.registerTileEntity(TileEntityBrainStoneTrigger.class, "TileEntityBlockBrainStoneTrigger");
	}

	public static final CreativeTabs getCreativeTab(CreativeTabs defaultTab) {
		if (BrainStoneConfigWrapper.getEnableCreativeTab()) {
			if (tabBrainStoneMod == null) {
				tabBrainStoneMod = new BrainStoneModCreativeTab();
			}

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
}
