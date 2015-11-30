package brainstonemod.common.api.thaumcraft;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import brainstonemod.BrainStone;
import brainstonemod.common.helper.BSP;
import cpw.mods.fml.common.Optional;

@Optional.Interface(iface = "thaumcraft.api.ThaumcraftApi", modid = "Thaumcraft")
public class AspectCreator {
	public static final Aspect BRAINSTONE = new Aspect("brainstone", 0x24FF45,
			new Aspect[] { Aspect.EARTH, Aspect.MIND },
			new ResourceLocation("brainstonemod", "textures/aspects/brainstone.png"),
			1);

	@Optional.Method(modid = "Thaumcraft")
	public static void initAspects() {
		BSP.info("Thaumcraft detected! Adding aspect and aspects to items!");
		
		registerBlockAspects();
		registerItemAspects();
	}

	@Optional.Method(modid = "Thaumcraft")
	private static void registerBlockAspects() {
		AspectList brainstone = new AspectList();
		brainstone.aspects.put(BRAINSTONE, 10);
		brainstone.aspects.put(Aspect.EARTH, 5);

		ThaumcraftApi.registerObjectTag(new ItemStack(BrainStone.brainStone()),
				brainstone);
	}

	@Optional.Method(modid = "Thaumcraft")
	private static void registerItemAspects() {

	}
}
