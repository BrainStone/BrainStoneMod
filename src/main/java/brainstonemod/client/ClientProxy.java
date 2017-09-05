package brainstonemod.client;

import static brainstonemod.BrainStone.RESOURCE_PREFIX;

import brainstonemod.common.CommonProxy;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAnvilBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ClientProxy extends CommonProxy {

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
		final String prefix = RESOURCE_PREFIX + item.getUnlocalizedName().substring(5);

		if (item instanceof ItemAnvilBlock) {
			ModelResourceLocation intact = new ModelResourceLocation(prefix + "_intact", "inventory");
			ModelResourceLocation slightlyDamaged = new ModelResourceLocation(prefix + "_slightly_damaged",
					"inventory");
			ModelResourceLocation veryDamaged = new ModelResourceLocation(prefix + "_very_damaged", "inventory");

			ModelBakery.registerItemVariants(item, intact, slightlyDamaged, veryDamaged);
			registerModel(item, 0, intact);
			registerModel(item, 1, slightlyDamaged);
			registerModel(item, 2, new ModelResourceLocation(prefix + "_very_damaged", "inventory"));
		} else {
			registerModel(item, new ModelResourceLocation(prefix, "inventory"));
		}
	}

	@Override
	public void registerModel(Block block) {
		registerModel(Item.getItemFromBlock(block));
	}

	private static void registerModel(Item item, ModelResourceLocation model) {
		registerModel(item, 0, model);
	}

	private static void registerModel(Item item, int meta, ModelResourceLocation model) {
		ModelLoader.setCustomModelResourceLocation(item, meta, model);
	}
}
