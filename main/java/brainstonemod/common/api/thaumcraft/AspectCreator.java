package brainstonemod.common.api.thaumcraft;

import net.minecraft.item.ItemStack;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import brainstonemod.BrainStone;
import cpw.mods.fml.common.Optional;

@Optional.Interface(iface = "thaumcraft.api.ThaumcraftApi", modid = "Thaumcraft")
public class AspectCreator {

	public AspectCreator() {
		AspectList brainstone = new AspectList();
		brainstone.aspects.put(Aspect.METAL, 156);
		
		ThaumcraftApi.registerObjectTag(new ItemStack(BrainStone.brainStone()), brainstone);
	}
	
}
