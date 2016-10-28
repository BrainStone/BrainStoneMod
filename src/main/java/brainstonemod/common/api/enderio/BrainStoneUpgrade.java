package brainstonemod.common.api.enderio;

import brainstonemod.BrainStone;
import crazypants.enderio.item.darksteel.DarkSteelItems;
import crazypants.enderio.item.darksteel.upgrade.AbstractUpgrade;
import crazypants.enderio.item.darksteel.upgrade.IRenderUpgrade;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Optional.Interface(iface = "crazypants.enderio.item.darksteel.upgrade.IDarkSteelUpgrade", modid = "EnderIO")
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
	@Optional.Method(modid = "EnderIO")
	public boolean canAddToItem(ItemStack stack) {
		return !(stack == null || (stack.getItem() != DarkSteelItems.itemDarkSteelBoots
				&& stack.getItem() != DarkSteelItems.itemDarkSteelLeggings
				&& stack.getItem() != DarkSteelItems.itemDarkSteelChestplate
				&& stack.getItem() != DarkSteelItems.itemDarkSteelHelmet)) && !hasUpgrade(stack);

	}

	@Override
	@Optional.Method(modid = "EnderIO")
	public void writeUpgradeToNBT(NBTTagCompound upgradeRoot) {
		// Do nothing
	}

	@Override
	@Optional.Method(modid = "EnderIO")
	@SideOnly(Side.CLIENT)
	public IRenderUpgrade getRender() {
		return render == null ? render = new Render() : render;
	}

	@SideOnly(Side.CLIENT)
	private class Render implements IRenderUpgrade {

		private EntityItem item = new EntityItem(Minecraft.getMinecraft().theWorld);
		private final float radToDEgFactor = 57.2958f;

		@Override
		public void doRenderLayer(RenderPlayer renderPlayer, ItemStack stack, AbstractClientPlayer player, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale) {
			/*Item stackItem = stack.getItem();

			item.setEntityItemStack(getUpgradeItem());
			bindItemTexture();
			glDepthMask(true);
			item.hoverStart = 0;

			if (stackItem == DarkSteelItems.itemDarkSteelHelmet && head) {
				Helper.translateToHeadLevel(player);
				GL11.glTranslated(0.27, -0.2375, 0);
				GL11.glRotated(180, 1, 0, 0);
				GL11.glScalef(0.5f, 0.5f, 0.5f);

				RenderManager.instance.renderEntityWithPosYaw(item, 0, 0, 0, 0,
						0);
			} else if (stackItem == DarkSteelItems.itemDarkSteelChestplate
					&& !head) {
				Helper.rotateIfSneaking(player);
				GL11.glTranslated(0, 0.2, -0.15);
				GL11.glScalef(0.75f, 0.75f, 0.75f);

				RenderManager.instance.renderEntityWithPosYaw(item, 0, 0, 0, 0,
						0);
			} else if (stackItem == DarkSteelItems.itemDarkSteelLeggings
					&& !head) {
				if (player.isSneaking())
					GL11.glTranslated(0, -0.2, 0.25);

				GL11.glTranslated(0, 0.75, 0);
				GL11.glRotatef(
						renderPlayer.getMainModel().bipedRightLeg.rotateAngleX
								* radToDEgFactor, 1, 0, 0);
				GL11.glTranslated(-0.25, 0.2, 0);
				GL11.glScalef(0.5f, 0.5f, 0.5f);

				RenderManager.instance.renderEntityWithPosYaw(item, 0, 0, 0, 0,
						0);

				GL11.glScalef(2f, 2f, 2f);
				GL11.glTranslated(0.25, -0.2, 0);
				GL11.glRotatef(
						renderPlayer.getMainModel().bipedRightLeg.rotateAngleX
								* -radToDEgFactor, 1, 0, 0);

				GL11.glRotatef(
						renderPlayer.getMainModel().bipedLeftLeg.rotateAngleX
								* radToDEgFactor, 1, 0, 0);
				GL11.glTranslated(0.25, 0.2, 0);
				GL11.glScalef(0.5f, 0.5f, 0.5f);

				RenderManager.instance.renderEntityWithPosYaw(item, 0, 0, 0, 0,
						0);
			} else if (stackItem == DarkSteelItems.itemDarkSteelBoots && !head) {
				if (player.isSneaking())
					GL11.glTranslated(0, -0.2, 0.25);

				GL11.glTranslated(0, 0.75, 0);
				GL11.glRotatef(
						renderPlayer.getMainModel().bipedRightLeg.rotateAngleX
								* radToDEgFactor, 1, 0, 0);
				GL11.glTranslated(-0.15, 0.55, -0.175);
				GL11.glScalef(0.4f, 0.4f, 0.4f);

				RenderManager.instance.renderEntityWithPosYaw(item, 0, 0, 0, 0,
						0);

				GL11.glScalef(2.5f, 2.5f, 2.5f);
				GL11.glTranslated(0.15, -0.55, 0.175);
				GL11.glRotatef(
						renderPlayer.getMainModel().bipedRightLeg.rotateAngleX
								* -radToDEgFactor, 1, 0, 0);

				GL11.glRotatef(
						renderPlayer.getMainModel().bipedLeftLeg.rotateAngleX
								* radToDEgFactor, 1, 0, 0);
				GL11.glTranslated(0.15, 0.55, -0.175);
				GL11.glScalef(0.4f, 0.4f, 0.4f);

				RenderManager.instance.renderEntityWithPosYaw(item, 0, 0, 0, 0,
						0);
			}*/
		}
	}

	public static void bindItemTexture() {
		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
	}
}