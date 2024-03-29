package brainstonemod;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Nullable;

import brainstonemod.common.block.BlockBrainLightSensor;
import brainstonemod.common.block.BlockBrainStone;
import brainstonemod.common.block.BlockBrainStoneAnvil;
import brainstonemod.common.block.BlockBrainStoneOre;
import brainstonemod.common.block.BlockBrainStoneTrigger;
import brainstonemod.common.block.BlockPulsatingBrainStone;
import brainstonemod.common.block.BlockStablePulsatingBrainStone;
import lombok.NoArgsConstructor;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemAnvilBlock;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@NoArgsConstructor(staticName = "registrar")
public class BrainStoneBlocks {
	private static final Map<String, Block> blocks = new LinkedHashMap<>();

	protected static void generateBlocks() {
		addBlock("brain_stone", new BlockBrainStone(false));
		addBlock("brain_stone_out", new BlockBrainStone(true));
		addBlock("brain_stone_ore", new BlockBrainStoneOre());
		addBlock("dirty_brain_stone", (new Block(Material.ROCK)).setHardness(2.4F).setResistance(0.5F)
				.setLightLevel(0.5F).setCreativeTab(BrainStone.getCreativeTab(CreativeTabs.BUILDING_BLOCKS)));
		addBlock("brain_light_sensor", new BlockBrainLightSensor());
		addBlock("brain_stone_trigger", new BlockBrainStoneTrigger());
		addBlock("pulsating_brain_stone", new BlockPulsatingBrainStone(false));
		addBlock("pulsating_brain_stone_effect", new BlockPulsatingBrainStone(true));
		addBlock("stable_pulsating_brain_stone", new BlockStablePulsatingBrainStone());
		addBlock("brain_stone_anvil", new BlockBrainStoneAnvil());
		addBlock("pulsating_brain_stone_anvil", new BlockBrainStoneAnvil(true));

		dirtyBrainStone().blockParticleGravity = -0.1F;
		dirtyBrainStone().setHarvestLevel("pickaxe", 2);
	}

	public static void addBlock(String name, Block block) {
		if (blocks.containsKey(name))
			return;

		if (block.getRegistryName() == null) {
			block.setTranslationKey(name).setRegistryName(BrainStone.MOD_ID, name);
		}

		blocks.put(name, block);
		ItemBlock item;

		if (block instanceof BlockBrainStoneAnvil) {
			item = new ItemAnvilBlock(block);
		} else {
			item = new ItemBlock(block);
		}

		BrainStoneItems.addItem(name, item);
	}

	/**
	 * @return the instance of BrainStone
	 */
	public static final Block brainStone() {
		return blocks.get("brain_stone");
	}

	/**
	 * @return the instance of BrainStoneOut
	 */
	public static final Block brainStoneOut() {
		return blocks.get("brain_stone_out");
	}

	/**
	 * @return the instance of BrainStoneOre
	 */
	public static final Block brainStoneOre() {
		return blocks.get("brain_stone_ore");
	}

	/**
	 * @return the instance of DirtyBrainStone
	 */
	public static final Block dirtyBrainStone() {
		return blocks.get("dirty_brain_stone");
	}

	/**
	 * @return the instance of BrainLightSensor
	 */
	public static final Block brainLightSensor() {
		return blocks.get("brain_light_sensor");
	}

	/**
	 * @return the instance of BrainStoneTrigger
	 */
	public static final Block brainStoneTrigger() {
		return blocks.get("brain_stone_trigger");
	}

	/**
	 * @return the instance of PulsatingBrainStone
	 */
	public static final Block pulsatingBrainStone() {
		return blocks.get("pulsating_brain_stone");
	}

	/**
	 * @return the instance of PulsatingBrainStoneEffect
	 */
	public static final Block pulsatingBrainStoneEffect() {
		return blocks.get("pulsating_brain_stone_effect");
	}

	/**
	 * @return the instance of StablePulsatingBrainStone
	 */
	public static final Block stablePulsatingBrainStone() {
		return blocks.get("stable_pulsating_brain_stone");
	}

	/**
	 * @return the instance of BrainStoneAnvil
	 */
	public static final BlockBrainStoneAnvil brainStoneAnvil() {
		return (BlockBrainStoneAnvil) blocks.get("brain_stone_anvil");
	}

	/**
	 * @return the instance of PulsatingBrainStoneAnvil
	 */
	public static final BlockBrainStoneAnvil pulsatingBrainStoneAnvil() {
		return (BlockBrainStoneAnvil) blocks.get("pulsating_brain_stone_anvil");
	}

	/**
	 * @return the instance of Brain Stone Crop.<br>
	 *         <strong>Only available when Mystical Agriculture is
	 *         present!</strong>
	 */
	@Nullable
	public static final Block brainStoneCrop() {
		return blocks.get("brain_stone_crop");
	}

	/**
	 * @return the instance of Essence of Life Crop.<br>
	 *         <strong>Only available when Mystical Agriculture is
	 *         present!</strong>
	 */
	@Nullable
	public static final Block essenceOfLifeCrop() {
		return blocks.get("essence_of_life_crop");
	}

	@SubscribeEvent
	public void registerBlocks(RegistryEvent.Register<Block> event) {
		blocks.values().stream().forEach(block -> {
			event.getRegistry().register(block);
		});
	}
}
