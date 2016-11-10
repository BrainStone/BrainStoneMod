package brainstonemod.common.api;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import brainstonemod.common.api.betterachievements.BetterAchievementsCompat;
import brainstonemod.common.api.enderio.EIOCompat;
import brainstonemod.common.api.jeresources.JEResourcesCompat;
import brainstonemod.common.api.overlord.OverlordCompat;
import brainstonemod.common.api.tconstruct.TiConCompat;
import brainstonemod.common.helper.BSP;
import lombok.experimental.UtilityClass;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@UtilityClass
public final class BrainStoneModules {
	public static final String BAUBLES_MODID = "Baubles";
	public static final String BETTER_ACHIEVEMENTS_MODID = "BetterAchievements";
	public static final String DRACONIC_EVOLUTION_MODID = "draconicevolution";
	public static final String ENDER_IO_MODID = "EnderIO";
	public static final String JEI_MODID = "JEI";
	public static final String JER_MODID = "jeresources";
	public static final String MFR_MODID = "MineFactoryReloaded";
	public static final String OVERLORD_MODID = "overlord";
	public static final String THAUMCRAFT_MODID = "Thaumcraft";
	public static final String TINKERS_CONSTRUCT_MODID = "tconstruct";

	public static final String DEPENDENCIES = "after:" + BAUBLES_MODID + ";after:" + BETTER_ACHIEVEMENTS_MODID
			+ ";after:" + DRACONIC_EVOLUTION_MODID + ";after:" + ENDER_IO_MODID + ";after:" + JEI_MODID + ";after:"
			+ JER_MODID + ";after:" + MFR_MODID + ";after:" + OVERLORD_MODID + ";after:" + THAUMCRAFT_MODID + ";after:"
			+ TINKERS_CONSTRUCT_MODID;

	private static Boolean BAUBLES;
	private static Boolean BETTER_ACHIEVEMENTS;
	private static Boolean DRACONIC_EVOLUTION;
	private static Boolean ENDER_IO;
	private static Boolean JEI;
	private static Boolean JER;
	private static Boolean MFR;
	private static Boolean OVERLORD;
	private static Boolean THAUMCRAFT;
	private static Boolean TINKERS_CONSTRUCT;

	private static List<IModIntegration> activeModules = new LinkedList<>();

	@Module(message = "Baubles detected! Enabling brainStoneLiveCapacitor in the BELT slot.")
	public static boolean baubles() {
		if (BAUBLES == null) {
			BAUBLES = Loader.isModLoaded(BAUBLES_MODID);
		}

		return BAUBLES;
	}

	@Module(message = "Better Achievements detected! Setting custom achievement icon.", integration = BetterAchievementsCompat.class)
	public static boolean betterAchievements() {
		if (BETTER_ACHIEVEMENTS == null) {
			BETTER_ACHIEVEMENTS = Loader.isModLoaded(BETTER_ACHIEVEMENTS_MODID);
		}

		return BETTER_ACHIEVEMENTS;
	}

	@Module(message = "Draconic Eveolution detected! Balancing and adding recipes.")
	public static boolean draconicEvolution() {
		if (DRACONIC_EVOLUTION == null) {
			DRACONIC_EVOLUTION = Loader.isModLoaded(DRACONIC_EVOLUTION_MODID);
		}

		return DRACONIC_EVOLUTION;
	}

	@Module(message = "EnderIO detected! Enabling recipes and armor upgrade.", integration = EIOCompat.class)
	public static boolean enderIO() {
		if (ENDER_IO == null) {
			ENDER_IO = Loader.isModLoaded(ENDER_IO_MODID);
		}

		return ENDER_IO;
	}

	@Module(message = "Just Enough Items (JEI) detected! Adding recipes to JEI.")
	public static boolean JEI() {
		if (JEI == null) {
			JEI = Loader.isModLoaded(JEI_MODID);
		}

		return JEI;
	}

	@Module(message = "Just Enough Resources (JER) detected! Adding drop and orgen information.", integration = JEResourcesCompat.class)
	public static boolean JER() {
		if (JER == null) {
			JER = Loader.isModLoaded(JER_MODID);
		}

		return JER;
	}

	@Module(message = "MineFactory Reloaded detected! Adding BrainStoneOre to the MiningLaser.")
	public static boolean MFR() {
		if (MFR == null) {
			MFR = Loader.isModLoaded(MFR_MODID);
		}

		return MFR;
	}

	@Module(message = "Overlord detected! Adding Pulsating BrainStone Augment.", integration = OverlordCompat.class)
	public static boolean overlord() {
		if (OVERLORD == null) {
			OVERLORD = Loader.isModLoaded(OVERLORD_MODID);
		}

		return OVERLORD;
	}

	@Module(message = "Thaumcraft detected! Adding aspect and fitting aspects to items.")
	public static boolean thaumcraft() {
		if (THAUMCRAFT == null) {
			THAUMCRAFT = Loader.isModLoaded(THAUMCRAFT_MODID);
		}

		return THAUMCRAFT;
	}

	@Module(message = "Tinker's Construct detected! Registering BrainStone materials as tool materials.", integration = TiConCompat.class)
	public static boolean tinkersConstruct() {
		if (TINKERS_CONSTRUCT == null) {
			TINKERS_CONSTRUCT = Loader.isModLoaded(TINKERS_CONSTRUCT_MODID);
		}

		return TINKERS_CONSTRUCT;
	}

	public static void detectModules() {
		BSP.info("Checking available modules:");

		for (Method method : BrainStoneModules.class.getMethods()) {
			Module module = method.getAnnotation(Module.class);

			if (module != null) {
				try {
					if ((Boolean) method.invoke(null)) {
						BSP.info("\t" + module.message());

						if (module.integration() != IModIntegration.class)
							activeModules.add(module.integration().newInstance());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		BSP.debug("Finished checking available modules!");
	}

	public static void preInit(FMLPreInitializationEvent event) {
		activeModules.forEach(module -> module.preInit(event));
	}

	public static void init(FMLInitializationEvent event) {
		activeModules.forEach(module -> module.init(event));
	}

	public static void postInit(FMLPostInitializationEvent event) {
		activeModules.forEach(module -> module.postInit(event));
	}

	public static void addAchievement() {
		activeModules.forEach(module -> module.addAchievement());
	}
}
