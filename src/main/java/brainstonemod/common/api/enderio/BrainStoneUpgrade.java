package brainstonemod.common.api.enderio;

import brainstonemod.BrainStone;
import brainstonemod.common.api.BrainStoneModules;
import crazypants.enderio.item.darksteel.DarkSteelItems;
import crazypants.enderio.item.darksteel.upgrade.AbstractUpgrade;
import crazypants.enderio.item.darksteel.upgrade.IRenderUpgrade;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Optional.Interface(iface = "crazypants.enderio.item.darksteel.upgrade.IDarkSteelUpgrade", modid = BrainStoneModules.ENDER_IO_MODID)
public class BrainStoneUpgrade extends AbstractUpgrade {
	private static String UPGRADE_NAME = "brainstone";

	public static final BrainStoneUpgrade UPGRADE = new BrainStoneUpgrade(new ItemStack(BrainStone.brainStone()), 20);

	private Render render;

	protected BrainStoneUpgrade(ItemStack upgradeItem, int levelCost) {
		super(UPGRADE_NAME, KEY_UPGRADE_PREFIX + UPGRADE_NAME, upgradeItem, levelCost);
	}

	public BrainStoneUpgrade(NBTTagCompound tag) {
		super(UPGRADE_NAME, tag);
	}

	@Override
	@Optional.Method(modid = BrainStoneModules.ENDER_IO_MODID)
	public boolean canAddToItem(ItemStack stack) {
		return !(stack == null || (stack.getItem() != DarkSteelItems.itemDarkSteelBoots
				&& stack.getItem() != DarkSteelItems.itemDarkSteelLeggings
				&& stack.getItem() != DarkSteelItems.itemDarkSteelChestplate
				&& stack.getItem() != DarkSteelItems.itemDarkSteelHelmet)) && !hasUpgrade(stack);

	}

	@Override
	@Optional.Method(modid = BrainStoneModules.ENDER_IO_MODID)
	public void writeUpgradeToNBT(NBTTagCompound upgradeRoot) {
		// Do nothing
	}

	@Override
	@Optional.Method(modid = BrainStoneModules.ENDER_IO_MODID)
	@SideOnly(Side.CLIENT)
	public IRenderUpgrade getRender() {
		return render == null ? render = new Render() : render;
	}

	@SideOnly(Side.CLIENT)
	private class Render implements IRenderUpgrade {
		private final ItemStack brainStone = new ItemStack(BrainStone.brainStone());

		@Override
		public void doRenderLayer(RenderPlayer renderPlayer, ItemStack stack, AbstractClientPlayer player,
				float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_,
				float p_177141_7_, float scale) {
			Item stackItem = stack.getItem();
			boolean sneaking = player.isSneaking();

			if (stackItem == DarkSteelItems.itemDarkSteelHelmet) {
				renderStart(sneaking, renderPlayer.getMainModel().bipedHead);

				GlStateManager.translate(0.0f, -0.45f, -0.27f);
				GlStateManager.scale(0.3f, 0.3f, 0.3f);

				renderEnd();
			} else if (stackItem == DarkSteelItems.itemDarkSteelChestplate) {
				renderStart(sneaking, renderPlayer.getMainModel().bipedBody);

				GlStateManager.translate(0, 0.3f, -0.15f);
				GlStateManager.scale(0.5f, 0.5f, 0.5f);

				renderEnd();
			} else if (stackItem == DarkSteelItems.itemDarkSteelLeggings) {
				renderStart(sneaking, renderPlayer.getMainModel().bipedRightLeg);

				GlStateManager.translate(0, 0.3f, -0.125f);
				GlStateManager.scale(0.3f, 0.3f, 0.3f);

				renderEnd();

				renderStart(sneaking, renderPlayer.getMainModel().bipedLeftLeg);

				GlStateManager.translate(0, 0.3f, -0.125f);
				GlStateManager.scale(0.3f, 0.3f, 0.3f);

				renderEnd();
			} else if (stackItem == DarkSteelItems.itemDarkSteelBoots) {
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
