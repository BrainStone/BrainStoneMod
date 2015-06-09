package brainstonemod.common.api.thaumcraft;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import brainstonemod.BrainStone;
import cpw.mods.fml.common.Optional;

@Optional.Interface(iface = "thaumcraft.api.ThaumcraftApi", modid = "Thaumcraft")
public class AspectCreator {
	public static final Aspect BRAINSTONE = new Aspect("brainstone", 0x56c000,
			new Aspect[] { Aspect.ENTROPY, Aspect.ENTROPY },
			new ResourceLocation("thaumcraft", "textures/aspects/victus.png"),
			1);

	@Optional.Method(modid = "Thaumcraft")
	public static void initAspects() {
		registerBlockAspects();
	}

	@Optional.Method(modid = "Thaumcraft")
	private static void registerBlockAspects() {
		AspectList brainstone = new AspectList();
		brainstone.aspects.put(BRAINSTONE, 64);
		brainstone.aspects.put(Aspect.EARTH, 64);

		ThaumcraftApi.registerObjectTag(new ItemStack(BrainStone.brainStone()),
				brainstone);
	}

	@Optional.Method(modid = "Thaumcraft")
	private static void registerItemAspects() {

	}
}
