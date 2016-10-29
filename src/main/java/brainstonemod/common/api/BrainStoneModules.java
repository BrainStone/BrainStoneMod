package brainstonemod.common.api;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import brainstonemod.common.api.enderio.EIOCompat;
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
	private static Boolean BAUBLES;
	private static Boolean ENDER_IO;
	private static Boolean MFR;
	private static Boolean OVERLORD;
	private static Boolean THAUMCRAFT;
	private static Boolean TINKERS_CONSTRUCT;

	private static List<IModIntegration> activeModules = new LinkedList<>();

	@Module(message = "BaublesAPI detected! Enabling brainStoneLiveCapacitor in the BELT slot.")
	public static boolean baubles() {
		if (BAUBLES == null) {
			BAUBLES = isClassAvailable("baubles.api.BaublesApi");
		}

		return BAUBLES;
	}

	@Module(message = "EnderIO detected! Enabling recipes and armor upgrade.", integration = EIOCompat.class)
	public static boolean enderIO() {
		if (ENDER_IO == null) {
			ENDER_IO = Loader.isModLoaded("EnderIO");
		}

		return ENDER_IO;
	}

	@Module(message = "MineFactory Reloaded detected! Adding BrainStoneOre to the MiningLaser.")
	public static boolean MFR() {
		if (MFR == null) {
			MFR = Loader.isModLoaded("MineFactoryReloaded");
		}

		return MFR;
	}

	@Module(message = "Overlord detected! Adding Pulsating BrainStone Augment.", integration = OverlordCompat.class)
	public static boolean overlord() {
		if (OVERLORD == null) {
			OVERLORD = Loader.isModLoaded("overlord");
		}

		return OVERLORD;
	}

	@Module(message = "Thaumcraft detected! Adding aspect and fitting aspects to items.")
	public static boolean thaumcraft() {
		if (THAUMCRAFT == null) {
			THAUMCRAFT = Loader.isModLoaded("Thaumcraft");
		}

		return THAUMCRAFT;
	}

	@Module(message = "Tinker's Construct detected! Registering BrainStone materials as tool materials.", integration = TiConCompat.class)
	public static boolean tinkersConstruct() {
		if (TINKERS_CONSTRUCT == null) {
			TINKERS_CONSTRUCT = Loader.isModLoaded("tconstruct");
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

	private static final boolean isClassAvailable(String name) {
		try {
			Class.forName(name);

			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}
}
