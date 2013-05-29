package mods.brainstone.handlers;

import mods.brainstone.ClientProxy;
import mods.brainstone.tileentities.TileEntityBlockBrainLogicBlock;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class BrainStoneRenderBrainLogicBlockHandler implements
		ISimpleBlockRenderingHandler {
	@Override
	public int getRenderId() {
		return ClientProxy.BrainLogicBlockRenderType;
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID,
			RenderBlocks renderer) {
		final Tessellator tessellator = Tessellator.instance;

		block.setBlockBoundsForItemRender();
		renderer.setRenderBoundsFromBlock(block);
		GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, -1.0F, 0.0F);
		renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D,
				renderer.getBlockIconFromSideAndMetadata(block, 0, metadata));
		tessellator.draw();

		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D,
				renderer.getBlockIconFromSideAndMetadata(block, 1, metadata));
		tessellator.draw();

		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, -1.0F);
		renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D,
				renderer.getBlockIconFromSideAndMetadata(block, 2, metadata));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, 1.0F);
		renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D,
				renderer.getBlockIconFromSideAndMetadata(block, 3, metadata));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(-1.0F, 0.0F, 0.0F);
		renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D,
				renderer.getBlockIconFromSideAndMetadata(block, 4, metadata));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(1.0F, 0.0F, 0.0F);
		renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D,
				renderer.getBlockIconFromSideAndMetadata(block, 5, metadata));
		tessellator.draw();
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}

	private void renderPins(IBlockAccess world, RenderBlocks renderer,
			Block block, int x, int y, int z) {
		final TileEntityBlockBrainLogicBlock tileEntity = (TileEntityBlockBrainLogicBlock) renderer.blockAccess
				.getBlockTileEntity(x, y, z);

		if (tileEntity != null) {
			final Icon icon = block.getBlockTexture(renderer.blockAccess, x, y,
					z, 0);
			renderer.enableAO = false;
			final Tessellator tessellator = Tessellator.instance;
			final float f3 = 0.5F;
			final float f4 = 1.0F;
			final float f5 = 0.8F;
			final float f6 = 0.6F;

			final int l = block.getMixedBrightnessForBlock(
					renderer.blockAccess, x, y, z);

			final float red;
			final float green;
			final float blue;

			byte direction = 1;

			if (tileEntity.shallRenderPin(direction)
					&& block.shouldSideBeRendered(renderer.blockAccess, x,
							y - 1, z, 0)) {
				tessellator.setBrightness(renderer.renderMinY > 0.0D ? l
						: block.getMixedBrightnessForBlock(
								renderer.blockAccess, x, y - 1, z));
				tessellator.setColorOpaque_F(
						f3 * tileEntity.getFactorForPinRed(direction), f3
								* tileEntity.getFactorForPinGreen(direction),
						f3 * tileEntity.getFactorForPinBlue(direction));
				renderer.renderFaceYNeg(block, x, y, z, icon);
			}

			direction = 0;

			if (tileEntity.shallRenderPin(direction)
					&& block.shouldSideBeRendered(renderer.blockAccess, x,
							y + 1, z, 1)) {
				tessellator.setBrightness(renderer.renderMaxY < 1.0D ? l
						: block.getMixedBrightnessForBlock(
								renderer.blockAccess, x, y + 1, z));
				tessellator.setColorOpaque_F(
						f4 * tileEntity.getFactorForPinRed(direction), f4
								* tileEntity.getFactorForPinGreen(direction),
						f4 * tileEntity.getFactorForPinBlue(direction));
				renderer.renderFaceYPos(block, x, y, z, icon);
			}

			direction = 4;

			if (tileEntity.shallRenderPin(direction)
					&& block.shouldSideBeRendered(renderer.blockAccess, x, y,
							z - 1, 2)) {
				tessellator.setBrightness(renderer.renderMinZ > 0.0D ? l
						: block.getMixedBrightnessForBlock(
								renderer.blockAccess, x, y, z - 1));
				tessellator.setColorOpaque_F(
						f5 * tileEntity.getFactorForPinRed(direction), f5
								* tileEntity.getFactorForPinGreen(direction),
						f5 * tileEntity.getFactorForPinBlue(direction));
				renderer.renderFaceZNeg(block, x, y, z, icon);
			}

			direction = 2;

			if (tileEntity.shallRenderPin(direction)
					&& block.shouldSideBeRendered(renderer.blockAccess, x, y,
							z + 1, 3)) {
				tessellator.setBrightness(renderer.renderMaxZ < 1.0D ? l
						: block.getMixedBrightnessForBlock(
								renderer.blockAccess, x, y, z + 1));
				tessellator.setColorOpaque_F(
						f5 * tileEntity.getFactorForPinRed(direction), f5
								* tileEntity.getFactorForPinGreen(direction),
						f5 * tileEntity.getFactorForPinBlue(direction));
				renderer.renderFaceZPos(block, x, y, z, icon);
			}

			direction = 5;

			if (tileEntity.shallRenderPin(direction)
					&& block.shouldSideBeRendered(renderer.blockAccess, x - 1,
							y, z, 4)) {
				tessellator.setBrightness(renderer.renderMinX > 0.0D ? l
						: block.getMixedBrightnessForBlock(
								renderer.blockAccess, x - 1, y, z));
				tessellator.setColorOpaque_F(
						f6 * tileEntity.getFactorForPinRed(direction), f6
								* tileEntity.getFactorForPinGreen(direction),
						f6 * tileEntity.getFactorForPinBlue(direction));
				renderer.renderFaceXNeg(block, x, y, z, icon);
			}

			direction = 3;

			if (tileEntity.shallRenderPin(direction)
					&& block.shouldSideBeRendered(renderer.blockAccess, x + 1,
							y, z, 5)) {
				tessellator.setBrightness(renderer.renderMaxX < 1.0D ? l
						: block.getMixedBrightnessForBlock(
								renderer.blockAccess, x + 1, y, z));
				tessellator.setColorOpaque_F(
						f6 * tileEntity.getFactorForPinRed(direction), f6
								* tileEntity.getFactorForPinGreen(direction),
						f6 * tileEntity.getFactorForPinBlue(direction));
				renderer.renderFaceXPos(block, x, y, z, icon);
			}
		}
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,
			Block block, int modelId, RenderBlocks renderer) {

		if (ClientProxy.renderPass == 0) {
			// we are on the solid block render pass

			renderer.renderStandardBlock(block, x, y, z);
		} else {
			// we are on the alpha render pass, draw the ice around the diamond

			this.renderPins(world, renderer, block, x, y, z);
		}

		return true;
	}

	@Override
	public boolean shouldRender3DInInventory() {
		return true;
	}
}