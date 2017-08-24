package brainstonemod.client.gui;

import brainstonemod.common.container.ContainerBrainStoneAnvil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiRepair;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.world.World;

public class GuiBrainStoneAnvil extends GuiRepair {
	private final ContainerBrainStoneAnvil anvil;

	public GuiBrainStoneAnvil(InventoryPlayer inventoryIn, World worldIn) {
		super(inventoryIn, worldIn);

		anvil = new ContainerBrainStoneAnvil(inventoryIn, worldIn, Minecraft.getMinecraft().player);
		inventorySlots = anvil;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		GlStateManager.disableLighting();
		GlStateManager.disableBlend();
		fontRenderer.drawString(I18n.format("container.repair", new Object[0]), 60, 6, 4210752);

		if (anvil.maximumCost > 0) {
			int i = 8453920;
			boolean flag = true;
			String s = I18n.format("container.repair.cost", new Object[] { Integer.valueOf(anvil.maximumCost) });

			if (!anvil.getSlot(2).getHasStack()) {
				flag = false;
			} else if (!anvil.getSlot(2).canTakeStack(playerInventory.player)) {
				i = 16736352;
			}

			if (flag) {
				int j = -16777216 | ((i & 16579836) >> 2) | (i & -16777216);
				int k = xSize - 8 - fontRenderer.getStringWidth(s);

				// No max checking!
				if (fontRenderer.getUnicodeFlag()) {
					drawRect(k - 3, 65, xSize - 7, 77, -16777216);
					drawRect(k - 2, 66, xSize - 8, 76, -12895429);
				} else {
					fontRenderer.drawString(s, k, 68, j);
					fontRenderer.drawString(s, k + 1, 67, j);
					fontRenderer.drawString(s, k + 1, 68, j);
				}

				fontRenderer.drawString(s, k, 67, i);
			}
		}

		GlStateManager.enableLighting();
	}
}
