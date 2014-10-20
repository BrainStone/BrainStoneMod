package brainstonemod.compat;

import net.minecraft.item.ItemStack;
import brainstonemod.BrainStone;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import cpw.mods.fml.common.Mod;

public class NEIBrainstoneConfig implements IConfigureNEI {

	@Override
	public String getName() {
		return BrainStone.class.getAnnotation(Mod.class).name();
	}

	@Override
	public String getVersion() {
		return BrainStone.class.getAnnotation(Mod.class).version();
	}

	@Override
	public void loadConfig() {
		API.hideItem(new ItemStack(BrainStone.pulsatingBrainStoneEffect()));
		API.hideItem(new ItemStack(BrainStone.brainStoneOut()));
	}

}