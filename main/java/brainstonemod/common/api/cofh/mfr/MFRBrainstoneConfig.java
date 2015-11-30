package brainstonemod.common.api.cofh.mfr;

import net.minecraft.item.ItemStack;
import powercrystals.minefactoryreloaded.MFRRegistry;
import brainstonemod.BrainStone;
import cpw.mods.fml.common.Optional;

public class MFRBrainstoneConfig {
	@Optional.Method(modid = "MineFactoryReloaded")
	public static void registerMFRItems() {
		// As rare as diamond
		MFRRegistry.registerLaserOre(50,
				new ItemStack(BrainStone.brainStoneOre()));
		// Preferred color is green
		MFRRegistry.addLaserPreferredOre(13,
				new ItemStack(BrainStone.brainStoneOre()));
	}
}
