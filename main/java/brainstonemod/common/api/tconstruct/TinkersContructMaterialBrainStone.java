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
				600, 5, 1.5F, 1, 0f, EnumChatFormatting.GREEN.toString(),
				0x27B23D);
		PatternBuilder.instance.registerFullMaterial(
				new ItemStack(BrainStone.brainStone()), 2, "BrainStone",
				new ItemStack(TinkerTools.toolShard, 1, materialID),
				new ItemStack(TinkerTools.toolRod, 1, materialID), materialID);
		for (int meta = 0; meta < TinkerTools.patternOutputs.length; meta++) {
			if (TinkerTools.patternOutputs[meta] != null)
				TConstructRegistry.addPartMapping(TinkerTools.woodPattern,
						meta + 1, materialID,
						new ItemStack(TinkerTools.patternOutputs[meta], 1,
								materialID));
		}

		if (TConstruct.pulsar.isPulseLoaded("Tinkers' Weaponry")) {
			for (int m = 0; m < TinkerWeaponry.patternOutputs.length; m++)
				TConstructRegistry.addPartMapping(TinkerWeaponry.woodPattern,
						m, materialID,
						new ItemStack(TinkerWeaponry.patternOutputs[m], 1,
								materialID));

			TConstructRegistry.addPartMapping(TinkerTools.woodPattern, 25,
					materialID, new ItemStack(TinkerWeaponry.arrowhead, 1,
							materialID));

			TConstructRegistry.addBowMaterial(materialID, 30, 5.25f);
			TConstructRegistry.addArrowMaterial(materialID, 1.4F, 0.0F);
		}

		TConstructRegistry.addDefaultToolPartMaterial(materialID);
		TConstructRegistry.addDefaultShardMaterial(materialID);
	}
}
