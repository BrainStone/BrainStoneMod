package brainstonemod.common.compat.thaumcraft;

/*
 * import brainstonemod.BrainStone; import brainstonemod.common.helper.BSP;
 * import net.minecraftforge.fml.common.Optional; import
 * net.minecraft.item.ItemStack; import net.minecraft.util.ResourceLocation;
 * import thaumcraft.api.ThaumcraftApi; import thaumcraft.api.aspects.Aspect;
 * import thaumcraft.api.aspects.AspectList;
 *
 * public class AspectCreator { public static final Aspect BRAINSTONE = new
 * Aspect("brainstone", 0x24FF45, new Aspect[] { Aspect.EARTH, Aspect.MIND },
 * new ResourceLocation("brainstonemod", "textures/aspects/brainstone.png"), 1);
 *
 * @Optional.Method(modid = "Thaumcraft") public static void initAspects() {
 * BSP.info("Thaumcraft detected! Adding aspect and aspects to items!");
 *
 * registerBlockAspects(); registerItemAspects(); }
 *
 * @Optional.Method(modid = "Thaumcraft") private static void
 * registerBlockAspects() { AspectList brainstone = new AspectList();
 * brainstone.aspects.put(BRAINSTONE, 10); brainstone.aspects.put(Aspect.EARTH,
 * 5);
 *
 * ThaumcraftApi.registerObjectTag(new ItemStack(BrainStone.brainStone()),
 * brainstone);
 *
 * brainstone = new AspectList(); brainstone.aspects.put(BRAINSTONE, 1);
 * brainstone.aspects.put(Aspect.EARTH, 1); ThaumcraftApi.registerObjectTag(new
 * ItemStack(BrainStone.brainStoneOre()), brainstone);
 *
 * brainstone = new AspectList(); brainstone.aspects.put(BRAINSTONE, 5);
 * brainstone.aspects.put(Aspect.EARTH, 5); ThaumcraftApi.registerObjectTag(new
 * ItemStack(BrainStone.dirtyBrainStone()), brainstone);
 *
 * brainstone = new AspectList(); brainstone.aspects.put(BRAINSTONE, 10);
 * brainstone.aspects.put(Aspect.MECHANISM, 2);
 * ThaumcraftApi.registerObjectTag(new ItemStack(BrainStone.brainLightSensor()),
 * brainstone);
 *
 * brainstone = new AspectList(); brainstone.aspects.put(BRAINSTONE, 10);
 * brainstone.aspects.put(Aspect.MECHANISM, 6);
 * ThaumcraftApi.registerObjectTag(new
 * ItemStack(BrainStone.brainStoneTrigger()), brainstone);
 *
 * brainstone = new AspectList(); brainstone.aspects.put(BRAINSTONE, 32);
 * brainstone.aspects.put(Aspect.GREED, 5); brainstone.aspects.put(Aspect.TRAP,
 * 10); ThaumcraftApi.registerObjectTag(new
 * ItemStack(BrainStone.pulsatingBrainStone()), brainstone);
 *
 * brainstone = new AspectList(); brainstone.aspects.put(BRAINSTONE, 8);
 * brainstone.aspects.put(Aspect.MECHANISM, 16);
 * ThaumcraftApi.registerObjectTag(new ItemStack(BrainStone.brainLogicBlock()),
 * brainstone); }
 *
 * @Optional.Method(modid = "Thaumcraft") private static void
 * registerItemAspects() {
 *
 * AspectList brainstone = new AspectList(); brainstone.aspects.put(BRAINSTONE,
 * 1); ThaumcraftApi.registerObjectTag(new
 * ItemStack(BrainStone.brainStoneDust()), brainstone);
 *
 * brainstone = new AspectList(); brainstone.aspects.put(BRAINSTONE, 8);
 * brainstone.aspects.put(Aspect.MECHANISM, 10);
 * ThaumcraftApi.registerObjectTag(new ItemStack(BrainStone.brainProcessor()),
 * brainstone);
 *
 * brainstone = new AspectList(); brainstone.aspects.put(BRAINSTONE, 8);
 * brainstone.aspects.put(Aspect.WEAPON, 8); ThaumcraftApi.registerObjectTag(new
 * ItemStack(BrainStone.brainStoneSword()), brainstone);
 *
 * brainstone = new AspectList(); brainstone.aspects.put(BRAINSTONE, 8);
 * brainstone.aspects.put(Aspect.TOOL, 8); ThaumcraftApi.registerObjectTag(new
 * ItemStack(BrainStone.brainStoneShovel()), brainstone);
 *
 * brainstone = new AspectList(); brainstone.aspects.put(BRAINSTONE, 8);
 * brainstone.aspects.put(Aspect.MINE, 8); ThaumcraftApi.registerObjectTag(new
 * ItemStack(BrainStone.brainStonePickaxe()), brainstone);
 *
 * brainstone = new AspectList(); brainstone.aspects.put(BRAINSTONE, 8);
 * brainstone.aspects.put(Aspect.TOOL, 8); ThaumcraftApi.registerObjectTag(new
 * ItemStack(BrainStone.brainStoneAxe()), brainstone);
 *
 * brainstone = new AspectList(); brainstone.aspects.put(BRAINSTONE, 8);
 * brainstone.aspects.put(Aspect.HARVEST, 8);
 * ThaumcraftApi.registerObjectTag(new ItemStack(BrainStone.brainStoneHoe()),
 * brainstone);
 *
 * brainstone = new AspectList(); brainstone.aspects.put(BRAINSTONE, 8);
 * brainstone.aspects.put(Aspect.ARMOR, 8); ThaumcraftApi.registerObjectTag(new
 * ItemStack(BrainStone.brainStoneHelmet()), brainstone);
 *
 * brainstone = new AspectList(); brainstone.aspects.put(BRAINSTONE, 8);
 * brainstone.aspects.put(Aspect.ARMOR, 8); ThaumcraftApi.registerObjectTag(new
 * ItemStack(BrainStone.brainStonePlate()), brainstone);
 *
 * brainstone = new AspectList(); brainstone.aspects.put(BRAINSTONE, 8);
 * brainstone.aspects.put(Aspect.ARMOR, 8); ThaumcraftApi.registerObjectTag(new
 * ItemStack(BrainStone.brainStoneLeggings()), brainstone);
 *
 * brainstone = new AspectList(); brainstone.aspects.put(BRAINSTONE, 8);
 * brainstone.aspects.put(Aspect.ARMOR, 8); ThaumcraftApi.registerObjectTag(new
 * ItemStack(BrainStone.brainStoneBoots()), brainstone); } }
 */