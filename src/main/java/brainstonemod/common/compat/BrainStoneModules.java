package brainstonemod.common.compat;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import brainstonemod.common.compat.betterachievements.BetterAchievementsCompat;
import brainstonemod.common.compat.cofh.thermalexpansion.ThermalExpansionCompat;
import brainstonemod.common.compat.forestry.ForestryCompat;
import brainstonemod.common.compat.immersiveengineering.ImmersiveEngineeringCompat;
import brainstonemod.common.compat.jeresources.JEResourcesCompat;
import brainstonemod.common.compat.mysticalagriculture.MysticalAgricultureCompat;
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
	public static final String BAUBLES_MODID = "baubles";
	public static final String BETTER_ACHIEVEMENTS_MODID = "betterachievements";
	public static final String CTM_MODID = "ctm";
	public static final String DRACONIC_EVOLUTION_MODID = "draconicevolution";
	public static final String ENDER_IO_MODID = "enderiobase";
	public static final String EX_NIHILO_ADSCENSIO_MODID = "exnihiloadscensio";
	public static final String FORESTRY_MODID = "forestry";
	public static final String IMMERSIVE_ENGINEERING_MODID = "immersiveengineering";
	public static final String JEI_MODID = "jei";
	public static final String JER_MODID = "jeresources";
	public static final String MFR_MODID = "minefactoryreloaded";
	public static final String MYSTICAL_AGRICULTURE_MODID = "mysticalagriculture";
	public static final String OVERLORD_MODID = "overlord";
	public static final String TESLA_MODID = "tesla";
	public static final String THAUMCRAFT_MODID = "thaumcraft";
	public static final String THERMAL_EXPANSION_MODID = "thermalexpansion";
	public static final String TINKERS_CONSTRUCT_MODID = "tconstruct";

	public static final String DEPENDENCIES = "after:" + BAUBLES_MODID + ";after:" + BETTER_ACHIEVEMENTS_MODID
			+ ";after:" + CTM_MODID + ";after:" + DRACONIC_EVOLUTION_MODID + ";after:" + ENDER_IO_MODID + ";after:"
			+ EX_NIHILO_ADSCENSIO_MODID + ";after:" + FORESTRY_MODID + ";after:" + IMMERSIVE_ENGINEERING_MODID
			+ ";after:" + JEI_MODID + ";after:" + JER_MODID + ";after:" + MFR_MODID + ";after:"
			+ MYSTICAL_AGRICULTURE_MODID + ";after:" + OVERLORD_MODID + ";after:" + TESLA_MODID + ";after:"
			+ THAUMCRAFT_MODID + ";after:" + THERMAL_EXPANSION_MODID + ";after:" + TINKERS_CONSTRUCT_MODID;

	private static Boolean BAUBLES;
	private static Boolean BETTER_ACHIEVEMENTS;
	private static Boolean CTM;
	private static Boolean DRACONIC_EVOLUTION;
	private static Boolean ENDER_IO;
	private static Boolean EX_NIHILO_ADSCENSIO;
	private static Boolean FORESTRY;
	private static Boolean IMMERSIVE_ENGINEERING;
	private static Boolean JEI;
	private static Boolean JER;
	private static Boolean MFR;
	private static Boolean MYSTICAL_AGRICULTURE;
	private static Boolean OVERLORD;
	private static Boolean THAUMCRAFT;
	private static Boolean THERMAL_EXPANSION;
	private static Boolean TINKERS_CONSTRUCT;
	private static Boolean TESLA;

	@Getter
	private static final Set<ModuleInformation> allModules = detectAllModules();
	@Getter
	private static final Set<ModuleInformation> activeModules = new TreeSet<>();

	@Module(modid = BAUBLES_MODID, name = "Baubles", message = "Enabling brainStoneLiveCapacitor in the BELT slot.")
	public static boolean baubles() {
		return testMod(BAUBLES_MODID, BAUBLES);
	}

	@Module(modid = BETTER_ACHIEVEMENTS_MODID, name = "Better Achievements", message = "Setting custom achievement icon.", integration = BetterAchievementsCompat.class)
	public static boolean betterAchievements() {
		return testMod(BETTER_ACHIEVEMENTS_MODID, BETTER_ACHIEVEMENTS);
	}

	@Module(modid = CTM_MODID, name = "Connected Textures Mod", message = "Using connected Textures.")
	public static boolean CTM() {
		return testMod(CTM_MODID, CTM);
	}

	@Module(modid = DRACONIC_EVOLUTION_MODID, name = "Draconic Evolution", message = "Balancing and adding recipes.")
	public static boolean draconicEvolution() {
		return testMod(DRACONIC_EVOLUTION_MODID, DRACONIC_EVOLUTION);
	}

	// @Module(modid = ENDER_IO_MODID, name = "Ender IO", message = "Enabling
	// recipes and armor upgrade.", integration = EnderIOCompat.class)
	protected static boolean enderIO() {
		return testMod(ENDER_IO_MODID, ENDER_IO);
	}

	// @Module(modid = EX_NIHILO_ADSCENSIO_MODID, name = "Ex Nihilo Adscensio",
	// message = "Making Brain Stone siftable.", integration =
	// ExNihiloAdscensioCompat.class)
	protected static boolean exNihiloAdscensio() {
		return testMod(EX_NIHILO_ADSCENSIO_MODID, EX_NIHILO_ADSCENSIO);
	}

	@Module(modid = FORESTRY_MODID, name = "Forestry", message = "Adding bees and related items.", integration = ForestryCompat.class)
	public static boolean forestry() {
		return testMod(FORESTRY_MODID, FORESTRY);
	}

	@Module(modid = IMMERSIVE_ENGINEERING_MODID, name = "Immersive Engineering", message = "Adding recipes.", integration = ImmersiveEngineeringCompat.class)
	public static boolean immersiveEngineering() {
		return testMod(IMMERSIVE_ENGINEERING_MODID, IMMERSIVE_ENGINEERING);
	}

	@Module(modid = JEI_MODID, name = "Just Enough Items (JEI)", message = "Adding recipes to JEI.")
	public static boolean JEI() {
		return testMod(JEI_MODID, JEI);
	}

	@Module(modid = JER_MODID, name = "Just Enough Resources (JER)", message = "Adding drop and orgen information.", integration = JEResourcesCompat.class)
	public static boolean JER() {
		return testMod(JER_MODID, JER);
	}

	// @Module(modid = MFR_MODID, name = "MineFactory Reloaded", message =
	// "Adding BrainStoneOre to the MiningLaser.")
	protected static boolean MFR() {
		return testMod(MFR_MODID, MFR);
	}

	@Module(modid = MYSTICAL_AGRICULTURE_MODID, name = "Mystical Agriculture", message = "Adding BrainStone Seeds.", integration = MysticalAgricultureCompat.class)
	public static boolean mysticalAgriculture() {
		return testMod(MYSTICAL_AGRICULTURE_MODID, MYSTICAL_AGRICULTURE);
	}

	@Module(modid = OVERLORD_MODID, name = "Overlord", message = "Adding Pulsating BrainStone Augment.", integration = OverlordCompat.class)
	public static boolean overlord() {
		return testMod(OVERLORD_MODID, OVERLORD);
	}
	
	@Module(modid = TESLA_MODID, name = "TESLA", message = "Enabling support for TESLA energy API.")
	public static boolean tesla() {
		return testMod(TESLA_MODID, TESLA);
	}

	// @Module(modid = THAUMCRAFT_MODID, name = "Thaumcraft", message = "Adding
	// aspect and fitting aspects to items.")
	protected static boolean thaumcraft() {
		return testMod(THAUMCRAFT_MODID, THAUMCRAFT);
	}

	@Module(modid = THERMAL_EXPANSION_MODID, name = "Thermal Expansion", message = "Adding recipes.", integration = ThermalExpansionCompat.class)
	public static boolean thermalExpansion() {
		return testMod(THERMAL_EXPANSION_MODID, THERMAL_EXPANSION);
	}

	@Module(modid = TINKERS_CONSTRUCT_MODID, name = "Tinker's Construct", message = "Registering BrainStone materials as tool materials.", integration = TiConCompat.class)
	public static boolean tinkersConstruct() {
		return testMod(TINKERS_CONSTRUCT_MODID, TINKERS_CONSTRUCT);
	}

	private static boolean testMod(String modID, Boolean storage) {
		if (storage == null) {
			storage = Loader.isModLoaded(modID);
		}

		return storage;
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
				}, field -> {
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

					if (module.getIntegrationClass() != IModIntegration.class) {
						module.setIntegration(module.getIntegrationClass().newInstance());
					}

					activeModules.add(module);
				}
			} catch (Exception e) {
				BSP.errorException(e);
			}
		}

		BSP.debug("Finished checking available modules!");
	}

	public static void preInit(FMLPreInitializationEvent event) {
		forEachActiveModule(integration -> integration.preInit(event));
	}

	public static void init(FMLInitializationEvent event) {
		forEachActiveModule(integration -> integration.init(event));
	}

	public static void postInit(FMLPostInitializationEvent event) {
		forEachActiveModule(integration -> integration.postInit(event));
	}

	public static void addAchievement() {
		forEachActiveModule(integration -> integration.addAchievement());
	}

	private static void forEachActiveModule(Consumer<? super IModIntegration> consumer) {
		activeModules.stream().map(ModuleInformation::getIntegration).filter(Objects::nonNull).forEach(consumer);
	}
}
