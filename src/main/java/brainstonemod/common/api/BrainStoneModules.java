package brainstonemod.common.api;

import brainstonemod.common.helper.Module;
import cpw.mods.fml.common.Loader;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class BrainStoneModules {
	private static Boolean BAUBLES;
	private static Boolean ENDER_IO;
	private static Boolean ENERGY;
	private static Boolean MFR;
	private static Boolean THAUMCRAFT;
	private static Boolean TINKERS_CONSTRUCT;

	@Module("Detected BaublesAPI! Enabling brainStoneLiveCapacitor in the BELT slot.")
	public static boolean baubles() {
		if (BAUBLES == null) {
			BAUBLES = isClassAvailable("baubles.api.BaublesApi");
		}

		return BAUBLES;
	}

	@Module("Detected EnderIO! Enabling recipes and armor upgrade.")
	public static boolean enderIO() {
		if (ENDER_IO == null) {
			ENDER_IO = Loader.isModLoaded("EnderIO");
		}

		return ENDER_IO;
	}

	@Module("Detected a mod with energy! Enabling brainStoneLiveCapacitor.")
	public static boolean energy() {
		if (ENERGY == null) {
			ENERGY = isClassAvailable("cofh.api.CoFHAPIProps");
		}

		return ENERGY;
	}

	@Module("MineFactory Reloaded detected! Adding BrainStoneOre to the MiningLaser.")
	public static boolean MFR() {
		if (MFR == null) {
			MFR = Loader.isModLoaded("MineFactoryReloaded");
		}

		return MFR;
	}

	@Module("Thaumcraft detected! Adding aspect and fitting aspects to items.")
	public static boolean thaumcraft() {
		if (THAUMCRAFT == null) {
			THAUMCRAFT = Loader.isModLoaded("Thaumcraft");
		}

		return THAUMCRAFT;
	}

	@Module("Tinker's Construct detected! Registering BrainStone materials as tool materials.")
	public static boolean tinkersConstruct() {
		if (TINKERS_CONSTRUCT == null) {
			TINKERS_CONSTRUCT = Loader.isModLoaded("TConstruct");
		}

		return TINKERS_CONSTRUCT;
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
