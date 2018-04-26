package brainstonemod.common.compat.enderio;

import brainstonemod.BrainStone;
import brainstonemod.BrainStoneBlocks;
import brainstonemod.common.compat.BrainStoneModules;
import crazypants.enderio.api.upgrades.IDarkSteelItem;
import crazypants.enderio.api.upgrades.IHasPlayerRenderer;
import crazypants.enderio.api.upgrades.IRenderUpgrade;
import crazypants.enderio.base.handler.darksteel.AbstractUpgrade;
import crazypants.enderio.base.init.ModObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Optional.Interface(iface = "crazypants.enderio.api.upgrades.IDarkSteelUpgrade", modid = BrainStoneModules.ENDER_IO_MODID)
public class BrainStoneUpgrade extends AbstractUpgrade implements IHasPlayerRenderer {
	private static String UPGRADE_NAME = "brainstone";

	public static final BrainStoneUpgrade UPGRADE = new BrainStoneUpgrade(new ItemStack(BrainStoneBlocks.brainStone()), 20);
	
	private Render render;

	@SuppressWarnings("deprecation")
	protected BrainStoneUpgrade(ItemStack upgradeItem, int levelCost) {
		super(BrainStone.MOD_ID, UPGRADE_NAME, 0, KEY_UPGRADE_PREFIX + UPGRADE_NAME, upgradeItem, levelCost);
	}
	
	@Override
	@Optional.Method(modid = BrainStoneModules.ENDER_IO_MODID)
	public boolean canAddToItem(ItemStack stack, IDarkSteelItem item) {
		return !((stack == null) || ((stack.getItem() != ModObject.itemDarkSteelBoots.getItem())
				&& (stack.getItem() != ModObject.itemDarkSteelLeggings.getItem())
				&& (stack.getItem() != ModObject.itemDarkSteelChestplate.getItem())
				&& (stack.getItem() != ModObject.itemDarkSteelHelmet.getItem()))) && !hasUpgrade(stack);
	}
	
	@Override
	@Optional.Method(modid = BrainStoneModules.ENDER_IO_MODID)
	@SideOnly(Side.CLIENT)
	public IRenderUpgrade getRender() {
		return render == null ? render = new Render() : render;
	}

	@SideOnly(Side.CLIENT)
	private class Render implements IRenderUpgrade {
		private final ItemStack brainStone = new ItemStack(BrainStoneBlocks.brainStone());

		@Override
		public void doRenderLayer(RenderPlayer renderPlayer, EntityEquipmentSlot equipmentSlot, ItemStack piece,
				AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTicks,
				float ageInTicks, float netHeadYaw, float headPitch, float scale) {
			Item stackItem = piece.getItem();
			boolean sneaking = player.isSneaking();

			if (stackItem == ModObject.itemDarkSteelHelmet.getItem()) {
				renderStart(sneaking, renderPlayer.getMainModel().bipedHead);

				GlStateManager.translate(0.0f, -0.45f, -0.27f);
				GlStateManager.scale(0.3f, 0.3f, 0.3f);

				renderEnd();
			} else if (stackItem == ModObject.itemDarkSteelChestplate.getItem()) {
				renderStart(sneaking, renderPlayer.getMainModel().bipedBody);

				GlStateManager.translate(0, 0.3f, -0.15f);
				GlStateManager.scale(0.5f, 0.5f, 0.5f);

				renderEnd();
			} else if (stackItem == ModObject.itemDarkSteelLeggings.getItem()) {
				renderStart(sneaking, renderPlayer.getMainModel().bipedRightLeg);

				GlStateManager.translate(0, 0.3f, -0.125f);
				GlStateManager.scale(0.3f, 0.3f, 0.3f);

				renderEnd();

				renderStart(sneaking, renderPlayer.getMainModel().bipedLeftLeg);

				GlStateManager.translate(0, 0.3f, -0.125f);
				GlStateManager.scale(0.3f, 0.3f, 0.3f);

				renderEnd();
			} else if (stackItem == ModObject.itemDarkSteelBoots.getItem()) {
				renderStart(sneaking, renderPlayer.getMainModel().bipedRightLeg);

				GlStateManager.translate(-0.15f, 0.55f, 0.0f);
				GlStateManager.scale(0.3f, 0.3f, 0.3f);

				renderEnd();

				renderStart(sneaking, renderPlayer.getMainModel().bipedLeftLeg);

				GlStateManager.translate(0.15f, 0.55f, 0.0f);
				GlStateManager.scale(0.3f, 0.3f, 0.3f);

				renderEnd();
			}
		}

		private void renderStart(boolean sneaking, ModelRenderer modelRenderer) {
			GlStateManager.pushMatrix();

			if (sneaking) {
				GlStateManager.translate(0.0f, 0.2f, 0.0f);
			}

			modelRenderer.postRender(0.0625F);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		}

		private void renderEnd() {
			Minecraft.getMinecraft().getRenderItem().renderItem(brainStone, TransformType.FIXED);

			GlStateManager.popMatrix();
		}
	}
}
