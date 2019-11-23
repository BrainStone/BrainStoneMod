package brainstonemod.common.compat.enderio;

import brainstonemod.BrainStone;
import brainstonemod.BrainStoneBlocks;
import brainstonemod.common.compat.BrainStoneModules;
import com.enderio.core.common.util.NNList;
import crazypants.enderio.api.upgrades.IHasPlayerRenderer;
import crazypants.enderio.api.upgrades.IRenderUpgrade;
import crazypants.enderio.api.upgrades.IRule;
import crazypants.enderio.base.handler.darksteel.AbstractUpgrade;
import crazypants.enderio.base.handler.darksteel.Rules;
import crazypants.enderio.base.init.ModObject;
import crazypants.enderio.base.item.darksteel.ItemDarkSteelArmor;
import info.loenwind.autoconfig.factory.IValue;
import lombok.RequiredArgsConstructor;
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

import java.util.List;

import javax.annotation.Nonnull;

@Optional.Interface(iface = "crazypants.enderio.api.upgrades.IDarkSteelUpgrade", modid = BrainStoneModules.ENDER_IO_MODID)
public class BrainStoneUpgrade extends AbstractUpgrade implements IHasPlayerRenderer {
	private static final String UPGRADE_NAME = "brainstone";
	private static final ItemStack ITEM_BRAINSTONE = new ItemStack(BrainStoneBlocks.brainStone());
	private static final int LEVEL_COST = 20;

	public static final @Nonnull BrainStoneUpgrade HELMET = new BrainStoneUpgrade(EntityEquipmentSlot.HEAD,
			ITEM_BRAINSTONE, LEVEL_COST);
	public static final @Nonnull BrainStoneUpgrade CHEST = new BrainStoneUpgrade(EntityEquipmentSlot.CHEST,
			ITEM_BRAINSTONE, LEVEL_COST);
	public static final @Nonnull BrainStoneUpgrade LEGS = new BrainStoneUpgrade(EntityEquipmentSlot.LEGS,
			ITEM_BRAINSTONE, LEVEL_COST);
	public static final @Nonnull BrainStoneUpgrade BOOTS = new BrainStoneUpgrade(EntityEquipmentSlot.FEET,
			ITEM_BRAINSTONE, LEVEL_COST);

	private Render render;
	private final @Nonnull EntityEquipmentSlot slot;

	protected BrainStoneUpgrade(@Nonnull EntityEquipmentSlot slot, ItemStack upgradeItem, int levelCost) {
		super(BrainStone.MOD_ID, UPGRADE_NAME, KEY_UPGRADE_PREFIX + UPGRADE_NAME, new IntValue(levelCost));
		this.slot = slot;
	}

	@Override
	@Optional.Method(modid = BrainStoneModules.ENDER_IO_MODID)
	@SideOnly(Side.CLIENT)
	public IRenderUpgrade getRender() {
		return render == null ? render = new Render() : render;
	}

	@Override
	public List<IRule> getRules() {
		return new NNList<>(Rules.forSlot(slot), Rules.staticCheck(item -> item instanceof ItemDarkSteelArmor),
				Rules.itemTypeTooltip(slot));
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

	@RequiredArgsConstructor
	private static class IntValue implements IValue<Integer> {
		private final int value;

		@Override
		public Integer get() {
			return value;
		}
	}
}
