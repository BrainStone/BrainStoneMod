package brainstonemod.common.helper;

import net.minecraft.item.ItemStack;
import brainstonemod.BrainStone;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import cpw.mods.fml.common.Optional;

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
	}
}