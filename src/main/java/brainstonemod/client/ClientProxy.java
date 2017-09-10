package brainstonemod.client;

import static brainstonemod.BrainStone.RESOURCE_PREFIX;

import brainstonemod.BrainStoneBlocks;
import brainstonemod.client.render.BSTriggerModel;
import brainstonemod.common.CommonProxy;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAnvilBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ClientProxy extends CommonProxy {
	private static final String INVENTORY = "inventory";

	private static void registerModel(Item item, ModelResourceLocation model) {
		registerModel(item, 0, model);
	}

	private static void registerModel(Item item, int meta, ModelResourceLocation model) {
		ModelLoader.setCustomModelResourceLocation(item, meta, model);
	}

	@SubscribeEvent
	public void registerModels(ModelRegistryEvent event) {
		StateMapperBase ignoreState = new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState iBlockState) {
				return BSTriggerModel.variantTag;
			}
		};
		ModelLoader.setCustomStateMapper(BrainStoneBlocks.brainStoneTrigger(), ignoreState);

		ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation(
				RESOURCE_PREFIX + "brain_stone_trigger", INVENTORY);
		final int DEFAULT_ITEM_SUBTYPE = 0;
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(BrainStoneBlocks.brainStoneTrigger()),
				DEFAULT_ITEM_SUBTYPE, itemModelResourceLocation);
	}

	/**
	 * Returns the Client Minecraft Instance.
	 *
	 * @return the Client Minecraft Instance
	 */
	@Override
	public Minecraft getClient() {
		return FMLClientHandler.instance().getClient();
	}

	/**
	 * Returns the Client Player Instance.
	 *
	 * @return the Client player Instance
	 */
	@Override
	public EntityPlayerSP getPlayer() {
		return getClient().player;
	}

	@Override
	public EntityPlayer getPlayerEntity(MessageContext ctx) {
		return (ctx.side.isClient() ? Minecraft.getMinecraft().player : super.getPlayerEntity(ctx));
	}

	@Override
	public String format(String key, Object... args) {
		return I18n.format(key, args);
	}

	@Override
	public void registerModel(Item item) {
		final String prefix = item.getRegistryName().toString();

		if (item instanceof ItemAnvilBlock) {
			ModelResourceLocation intact = new ModelResourceLocation(prefix + "_intact", INVENTORY);
			ModelResourceLocation slightlyDamaged = new ModelResourceLocation(prefix + "_slightly_damaged", INVENTORY);
			ModelResourceLocation veryDamaged = new ModelResourceLocation(prefix + "_very_damaged", INVENTORY);

			ModelBakery.registerItemVariants(item, intact, slightlyDamaged, veryDamaged);
			registerModel(item, 0, intact);
			registerModel(item, 1, slightlyDamaged);
			registerModel(item, 2, veryDamaged);
		} else {
			registerModel(item, new ModelResourceLocation(prefix, INVENTORY));
		}
	}
}
