package brainstonemod.common.compat.industrialforegoing;

import brainstonemod.BrainStoneBlocks;
import brainstonemod.common.compat.IModIntegration;
import com.buuz135.industrial.api.IndustrialForegoingHelper;
import com.buuz135.industrial.api.recipe.LaserDrillEntry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class IndustrialForegoingCompat implements IModIntegration {
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		// Do nothing
	}

	@Override
	public void init(FMLInitializationEvent event) {
		// As rare as diamond
		// Preferred color is green
		IndustrialForegoingHelper
				.addLaserDrillEntry(new LaserDrillEntry(13, new ItemStack(BrainStoneBlocks.brainStoneOre()), 4));
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		// Do nothing
	}
}
