package brainstonemod.client;

import brainstonemod.common.CommonProxy;
import brainstonemod.common.block.BlockBrainStoneAnvil;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import static brainstonemod.BrainStone.MOD_ID;

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
		return getClient().thePlayer;
	}

	@Override
	public EntityPlayer getPlayerEntity(MessageContext ctx) {
		return (ctx.side.isClient() ? Minecraft.getMinecraft().thePlayer : super.getPlayerEntity(ctx));
	}

	@Override
	public String format(String key, Object... args) {
		return I18n.format(key, args);
	}

	@Override
	public void rmm(Item i) {
		ModelLoader.setCustomModelResourceLocation(i, 0,
				new ModelResourceLocation(MOD_ID + ":" + i.getUnlocalizedName().substring(5), "inventory"));
	}

	@Override
	public void rmm(Block b) {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(b), 0,
				new ModelResourceLocation(MOD_ID + ":" + b.getUnlocalizedName().substring(5), "inventory"));
	}

	@Override
	public void rmm(BlockBrainStoneAnvil b) {
		ModelBakery.registerItemVariants(Item.getItemFromBlock(b),
				new ModelResourceLocation(MOD_ID + ":" + b.getUnlocalizedName().substring(5)+"_intact", "inventory"),
				new ModelResourceLocation(MOD_ID + ":" + b.getUnlocalizedName().substring(5)+"_slightly_damaged", "inventory"),
				new ModelResourceLocation(MOD_ID + ":" + b.getUnlocalizedName().substring(5)+"_very_damaged", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(b), 0,
				new ModelResourceLocation(MOD_ID + ":" + b.getUnlocalizedName().substring(5)+"_intact", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(b), 1,
				new ModelResourceLocation(MOD_ID + ":" + b.getUnlocalizedName().substring(5)+"_slightly_damaged", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(b), 2,
				new ModelResourceLocation(MOD_ID + ":" + b.getUnlocalizedName().substring(5)+"_very_damaged", "inventory"));
	}
}