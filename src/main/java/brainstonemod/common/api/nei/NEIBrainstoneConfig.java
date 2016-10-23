package brainstonemod.common.api.nei;

import brainstonemod.BrainStone;
import brainstonemod.common.api.BrainStoneModules;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import cpw.mods.fml.common.Optional;
import net.minecraft.item.ItemStack;

@Optional.Interface(iface = "codechicken.nei.api.API", modid = "NotEnoughItems")
public class NEIBrainstoneConfig implements IConfigureNEI {
	@Optional.Method(modid = "NotEnoughItems")
	@Override
	public String getName() {
		return BrainStone.NAME;
	}

	@Optional.Method(modid = "NotEnoughItems")
	@Override
	public String getVersion() {
		return BrainStone.VERSION;
	}

	@Optional.Method(modid = "NotEnoughItems")
	@Override
	public void loadConfig() {
		API.hideItem(new ItemStack(BrainStone.pulsatingBrainStoneEffect()));
		API.hideItem(new ItemStack(BrainStone.brainStoneOut()));
		if(BrainStoneModules.energy()) {
			API.registerRecipeHandler(new CapacitorUpgradeRecipeHandler());
			API.registerUsageHandler(new CapacitorUpgradeRecipeHandler());
		}
	}
}