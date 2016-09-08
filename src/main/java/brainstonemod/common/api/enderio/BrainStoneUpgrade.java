package brainstonemod.common.api.enderio;

import static org.lwjgl.opengl.GL11.glDepthMask;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.event.RenderPlayerEvent;

import org.lwjgl.opengl.GL11;

import brainstonemod.BrainStone;
import brainstonemod.common.helper.BSP;

import com.enderio.core.client.render.RenderUtil;

import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import crazypants.enderio.item.darksteel.DarkSteelItems;
import crazypants.enderio.item.darksteel.upgrade.AbstractUpgrade;
import crazypants.enderio.item.darksteel.upgrade.IRenderUpgrade;

@Optional.Interface(iface = "crazypants.enderio.item.darksteel.upgrade.IDarkSteelUpgrade", modid = "EnderIO")
public class BrainStoneUpgrade extends AbstractUpgrade {
	private static String UPGRADE_NAME = "brainstone";

	public static final BrainStoneUpgrade UPGRADE = new BrainStoneUpgrade(
			new ItemStack(BrainStone.brainStone()), 20);

	private Render render;

	protected BrainStoneUpgrade(ItemStack upgradeItem, int levelCost) {
		super(UPGRADE_NAME, KEY_UPGRADE_PREFIX + UPGRADE_NAME, upgradeItem,
				levelCost);
	}

	public BrainStoneUpgrade(NBTTagCompound tag) {
		super(UPGRADE_NAME, tag);
	}

	@Override
	@Optional.Method(modid = "EnderIO")
	public boolean canAddToItem(ItemStack stack) {
		if (stack == null
				|| (stack.getItem() != DarkSteelItems.itemDarkSteelBoots
						&& stack.getItem() != DarkSteelItems.itemDarkSteelLeggings
						&& stack.getItem() != DarkSteelItems.itemDarkSteelChestplate && stack
						.getItem() != DarkSteelItems.itemDarkSteelHelmet)) {
			return false;
		}

		return !hasUpgrade(stack);
	}

	@Override
	@Optional.Method(modid = "EnderIO")
	public void writeUpgradeToNBT(NBTTagCompound upgradeRoot) {
	}

	@Override
	@Optional.Method(modid = "EnderIO")
	@SideOnly(Side.CLIENT)
	public IRenderUpgrade getRender() {
		return render == null ? render = new Render() : render;
	}

	@SideOnly(Side.CLIENT)
	private class Render implements IRenderUpgrade {

		private EntityItem item = new EntityItem(
				Minecraft.getMinecraft().theWorld);
		private final float radToDEgFactor = 57.2958f;

		@Override
		public void render(RenderPlayerEvent event, ItemStack stack,
				boolean head) {
			Item stackItem = stack.getItem();

			item.setEntityItemStack(getUpgradeItem());
			RenderUtil.bindItemTexture();
			glDepthMask(true);
			item.hoverStart = 0;

			if (stackItem == DarkSteelItems.itemDarkSteelHelmet && head) {
				Helper.translateToHeadLevel(event.entityPlayer);
				GL11.glTranslated(0.27, -0.2375, 0);
				GL11.glRotated(180, 1, 0, 0);
				GL11.glScalef(0.5f, 0.5f, 0.5f);

				RenderManager.instance.renderEntityWithPosYaw(item, 0, 0, 0, 0,
						0);
			} else if (stackItem == DarkSteelItems.itemDarkSteelChestplate
					&& !head) {
				Helper.rotateIfSneaking(event.entityPlayer);
				GL11.glTranslated(0, 0.2, -0.15);
				GL11.glScalef(0.75f, 0.75f, 0.75f);

				RenderManager.instance.renderEntityWithPosYaw(item, 0, 0, 0, 0,
						0);
			} else if (stackItem == DarkSteelItems.itemDarkSteelLeggings
					&& !head) {
				if (event.entityPlayer.isSneaking())
					GL11.glTranslated(0, -0.2, 0.25);

				GL11.glTranslated(0, 0.75, 0);
				GL11.glRotatef(
						event.renderer.modelBipedMain.bipedRightLeg.rotateAngleX
								* radToDEgFactor, 1, 0, 0);
				GL11.glTranslated(-0.25, 0.2, 0);
				GL11.glScalef(0.5f, 0.5f, 0.5f);

				RenderManager.instance.renderEntityWithPosYaw(item, 0, 0, 0, 0,
						0);

				GL11.glScalef(2f, 2f, 2f);
				GL11.glTranslated(0.25, -0.2, 0);
				GL11.glRotatef(
						event.renderer.modelBipedMain.bipedRightLeg.rotateAngleX
								* -radToDEgFactor, 1, 0, 0);

				GL11.glRotatef(
						event.renderer.modelBipedMain.bipedLeftLeg.rotateAngleX
								* radToDEgFactor, 1, 0, 0);
				GL11.glTranslated(0.25, 0.2, 0);
				GL11.glScalef(0.5f, 0.5f, 0.5f);

				RenderManager.instance.renderEntityWithPosYaw(item, 0, 0, 0, 0,
						0);
			} else if (stackItem == DarkSteelItems.itemDarkSteelBoots && !head) {
				if (event.entityPlayer.isSneaking())
					GL11.glTranslated(0, -0.2, 0.25);

				GL11.glTranslated(0, 0.75, 0);
				GL11.glRotatef(
						event.renderer.modelBipedMain.bipedRightLeg.rotateAngleX
								* radToDEgFactor, 1, 0, 0);
				GL11.glTranslated(-0.15, 0.55, -0.175);
				GL11.glScalef(0.4f, 0.4f, 0.4f);

				RenderManager.instance.renderEntityWithPosYaw(item, 0, 0, 0, 0,
						0);

				GL11.glScalef(2.5f, 2.5f, 2.5f);
				GL11.glTranslated(0.15, -0.55, 0.175);
				GL11.glRotatef(
						event.renderer.modelBipedMain.bipedRightLeg.rotateAngleX
								* -radToDEgFactor, 1, 0, 0);

				GL11.glRotatef(
						event.renderer.modelBipedMain.bipedLeftLeg.rotateAngleX
								* radToDEgFactor, 1, 0, 0);
				GL11.glTranslated(0.15, 0.55, -0.175);
				GL11.glScalef(0.4f, 0.4f, 0.4f);

				RenderManager.instance.renderEntityWithPosYaw(item, 0, 0, 0, 0,
						0);
			}
		}
	}
}