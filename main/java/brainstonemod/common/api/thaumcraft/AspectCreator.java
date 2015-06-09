package brainstonemod.common.api.thaumcraft;

import brainstonemod.BrainStone;
import net.minecraft.item.ItemStack;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

public class AspectCreator {

	public AspectCreator() {
		AspectList brainstone = new AspectList();
		brainstone.aspects.put(Aspect.METAL, 156);
		
		ThaumcraftApi.registerObjectTag(new ItemStack(BrainStone.brainStone()), brainstone);
	}
	
}
