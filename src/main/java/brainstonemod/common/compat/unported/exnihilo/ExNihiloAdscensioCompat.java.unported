package brainstonemod.common.compat.exnihilo;

import brainstonemod.BrainStone;
import brainstonemod.common.compat.IModIntegration;
import exnihiloadscensio.blocks.BlockSieve.MeshType;
import exnihiloadscensio.blocks.ENBlocks;
import exnihiloadscensio.registries.SieveRegistry;
import exnihiloadscensio.registries.manager.ISieveDefaultRegistryProvider;
import exnihiloadscensio.registries.manager.RegistryManager;
import exnihiloadscensio.util.BlockInfo;
import exnihiloadscensio.util.ItemInfo;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ExNihiloAdscensioCompat implements IModIntegration, ISieveDefaultRegistryProvider {
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		BrainStone.items.put("brain_stone_dust_tiny",
				(new Item()).setCreativeTab(BrainStone.getCreativeTab(CreativeTabs.MATERIALS)));

		RegistryManager.registerSieveDefaultRecipeHandler(this);
	}

	@Override
	public void init(FMLInitializationEvent event) {
		BrainStone.addRecipe(new ItemStack(BrainStone.brainStoneDust(), 1), "TT", "TT", 'T',
				BrainStone.brainStoneDustTiny());
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		// Do nothing
	}

	@Override
	public void addAchievement() {
		// Do nothing
	}

	@Override
	public void registerSieveRecipeDefaults() {
		BlockInfo dust = new BlockInfo(ENBlocks.dust.getDefaultState());
		ItemInfo brainStoneDustTiny = new ItemInfo(new ItemStack(BrainStone.brainStoneDustTiny()));

		SieveRegistry.register(dust, brainStoneDustTiny, 0.01f, MeshType.IRON.getID());
		SieveRegistry.register(dust, brainStoneDustTiny, 0.05f, MeshType.DIAMOND.getID());
	}
}
