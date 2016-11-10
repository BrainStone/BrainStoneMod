package brainstonemod.common.compat;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import brainstonemod.common.compat.betterachievements.BetterAchievementsCompat;
import brainstonemod.common.compat.enderio.EIOCompat;
import brainstonemod.common.compat.jeresources.JEResourcesCompat;
import brainstonemod.common.compat.overlord.OverlordCompat;
import brainstonemod.common.compat.tconstruct.TiConCompat;
import brainstonemod.common.helper.BSP;
import lombok.Getter;
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

	@Getter
	private static final Set<ModuleInformation> allModules = detectAllModules();
	@Getter
	private static Set<ModuleInformation> activeModules = new TreeSet<>();

	@Module(modid = BAUBLES_MODID, name = "Baubles", message = "Enabling brainStoneLiveCapacitor in the BELT slot.")
	public static boolean baubles() {
		if (BAUBLES == null) {
			BAUBLES = Loader.isModLoaded(BAUBLES_MODID);
		}

		return BAUBLES;
	}

	@Module(modid = BETTER_ACHIEVEMENTS_MODID, name = "Better Achievements", message = "Setting custom achievement icon.", integration = BetterAchievementsCompat.class)
	public static boolean betterAchievements() {
		if (BETTER_ACHIEVEMENTS == null) {
			BETTER_ACHIEVEMENTS = Loader.isModLoaded(BETTER_ACHIEVEMENTS_MODID);
		}

		return BETTER_ACHIEVEMENTS;
	}

	@Module(modid = DRACONIC_EVOLUTION_MODID, name = "Draconic Eveolution", message = "Balancing and adding recipes.")
	public static boolean draconicEvolution() {
		if (DRACONIC_EVOLUTION == null) {
			DRACONIC_EVOLUTION = Loader.isModLoaded(DRACONIC_EVOLUTION_MODID);
		}

		return DRACONIC_EVOLUTION;
	}

	@Module(modid = ENDER_IO_MODID, name = "EnderIO", message = "Enabling recipes and armor upgrade.", integration = EIOCompat.class)
	public static boolean enderIO() {
		if (ENDER_IO == null) {
			ENDER_IO = Loader.isModLoaded(ENDER_IO_MODID);
		}

		return ENDER_IO;
	}

	@Module(modid = JEI_MODID, name = "Just Enough Items (JEI)", message = "Adding recipes to JEI.")
	public static boolean JEI() {
		if (JEI == null) {
			JEI = Loader.isModLoaded(JEI_MODID);
		}

		return JEI;
	}

	@Module(modid = JER_MODID, name = "Just Enough Resources (JER)", message = "Adding drop and orgen information.", integration = JEResourcesCompat.class)
	public static boolean JER() {
		if (JER == null) {
			JER = Loader.isModLoaded(JER_MODID);
		}

		return JER;
	}

	@Module(modid = MFR_MODID, name = "MineFactory Reloaded", message = "Adding BrainStoneOre to the MiningLaser.")
	public static boolean MFR() {
		if (MFR == null) {
			MFR = Loader.isModLoaded(MFR_MODID);
		}

		return MFR;
	}

	@Module(modid = OVERLORD_MODID, name = "Overlord", message = "Adding Pulsating BrainStone Augment.", integration = OverlordCompat.class)
	public static boolean overlord() {
		if (OVERLORD == null) {
			OVERLORD = Loader.isModLoaded(OVERLORD_MODID);
		}

		return OVERLORD;
	}

	@Module(modid = THAUMCRAFT_MODID, name = "Thaumcraft", message = "Adding aspect and fitting aspects to items.")
	public static boolean thaumcraft() {
		if (THAUMCRAFT == null) {
			THAUMCRAFT = Loader.isModLoaded(THAUMCRAFT_MODID);
		}

		return THAUMCRAFT;
	}

	@Module(modid = TINKERS_CONSTRUCT_MODID, name = "Tinker's Construct", message = "Registering BrainStone materials as tool materials.", integration = TiConCompat.class)
	public static boolean tinkersConstruct() {
		if (TINKERS_CONSTRUCT == null) {
			TINKERS_CONSTRUCT = Loader.isModLoaded(TINKERS_CONSTRUCT_MODID);
		}

		return TINKERS_CONSTRUCT;
	}

	private static Set<ModuleInformation> detectAllModules() {
		Set<ModuleInformation> modules = new TreeSet<>();
		Map<String, Field> modidsToFields = Arrays.asList(BrainStoneModules.class.getDeclaredFields()).stream()
				.filter(field -> String.class.equals(field.getType())).collect(Collectors.toMap(field -> {
					try {
						return (String) field.get(null);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				} , field -> {
					try {
						return BrainStoneModules.class.getDeclaredField(field.getName().replace("_MODID", ""));
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}));

		for (Method method : BrainStoneModules.class.getMethods()) {
			Module module = method.getAnnotation(Module.class);

			if (module != null) {
				try {
					modules.add(new ModuleInformation(module.modid(), module.name(), module.message(),
							modidsToFields.get(module.modid()), method, module.integration()));
				} catch (Exception e) {
					BSP.errorException(e);
				}
			}
		}

		return modules;
	}

	public static void detectActiveModules() {
		BSP.info("Checking available modules:");

		for (ModuleInformation module : allModules) {
			try {
				if (module.isActive()) {
					BSP.info("\t" + module.getName() + " detected! " + module.getMessage());

					if (module.getIntegrationClass() != IModIntegration.class)
						module.setIntegration(module.getIntegrationClass().newInstance());
				}
			} catch (Exception e) {
				BSP.errorException(e);
			}
		}

		BSP.debug("Finished checking available modules!");
	}

	public static void preInit(FMLPreInitializationEvent event) {
		getFilteredModuleStream().forEach(module -> module.getIntegration().preInit(event));
	}

	public static void init(FMLInitializationEvent event) {
		getFilteredModuleStream().forEach(module -> module.getIntegration().init(event));
	}

	public static void postInit(FMLPostInitializationEvent event) {
		getFilteredModuleStream().forEach(module -> module.getIntegration().postInit(event));
	}

	public static void addAchievement() {
		getFilteredModuleStream().forEach(module -> module.getIntegration().addAchievement());
	}

	private static Stream<ModuleInformation> getFilteredModuleStream() {
		return activeModules.stream().filter(module -> module.getIntegration() != null);
	}
}
