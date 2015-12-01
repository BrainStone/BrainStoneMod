package brainstonemod.common.api.tconstruct;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import tconstruct.TConstruct;
import tconstruct.library.TConstructRegistry;
import tconstruct.library.client.TConstructClientRegistry;
import tconstruct.library.crafting.PatternBuilder;
import tconstruct.tools.TinkerTools;
import tconstruct.weaponry.TinkerWeaponry;
import brainstonemod.BrainStone;
import brainstonemod.common.helper.BSP;
import cpw.mods.fml.common.Optional;

public class TinkersContructMaterialBrainStone {
	@Optional.Method(modid = "TConstruct")
	public static void initToolMaterials() {
		BSP.info("Tinker's Construct detected! Registering BrainStone as tool material!");

		int materialID = 1546;

		TConstructClientRegistry.addMaterialRenderMapping(materialID, "tinker",
				"brainstone", true);
		TConstructRegistry.addToolMaterial(materialID, "BrainStone", 4, 5350,
				600, 5, 2.5F, 1, 0f, EnumChatFormatting.GREEN.toString(),
				0x27B23D);
		PatternBuilder.instance.registerFullMaterial(
				new ItemStack(BrainStone.brainStone()), 2, "BrainStone",
				new ItemStack(TinkerTools.toolShard, 1, 1546), new ItemStack(
						TinkerTools.toolRod, 1, 1546), 1546);
		for (int meta = 0; meta < TinkerTools.patternOutputs.length; meta++) {
			if (TinkerTools.patternOutputs[meta] != null)
				TConstructRegistry.addPartMapping(TinkerTools.woodPattern,
						meta + 1, 31, new ItemStack(
								TinkerTools.patternOutputs[meta], 1, 31));
		}

		for (int m = 0; m < TinkerWeaponry.patternOutputs.length; m++)
			TConstructRegistry.addPartMapping(TinkerWeaponry.woodPattern, m,
					materialID, new ItemStack(TinkerWeaponry.patternOutputs[m],
							1, materialID));

		if (TConstruct.pulsar.isPulseLoaded("Tinkers' Weaponry")) {
			TConstructRegistry.addPartMapping(TinkerTools.woodPattern, 25,
					materialID, new ItemStack(TinkerWeaponry.arrowhead, 1,
							materialID));

			TConstructRegistry.addBowstringMaterial(1, 2, new ItemStack(
					BrainStone.brainStone()), new ItemStack(
					TinkerWeaponry.bowstring, 1, 3), 1F, 0.8F, 0.9f, 0x24FF45);
			TConstructRegistry.addBowMaterial(materialID, 35, 4.75f);
			TConstructRegistry.addArrowMaterial(materialID, 1.8F, 0.5F);
		}

		TConstructRegistry.addDefaultToolPartMaterial(materialID);
		TConstructRegistry.addDefaultShardMaterial(materialID);
	}
}
