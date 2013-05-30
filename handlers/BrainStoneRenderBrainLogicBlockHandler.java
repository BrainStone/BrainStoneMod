package mods.brainstone.handlers;

import mods.brainstone.ClientProxy;
import mods.brainstone.tileentities.TileEntityBlockBrainLogicBlock;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
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

			// Top Face

			tileEntity.currentRenderDirection = 0;

			if (tileEntity.shallRenderPin(tileEntity.currentRenderDirection)) {
				renderer.setRenderBounds(0.0F, 0.9F, 0.0F, 1.0F, 1.0F, 1.0F);
				renderer.renderStandardBlock(block, x, y, z);
			}

			// Bottom Face

			tileEntity.currentRenderDirection++;

			if (tileEntity.shallRenderPin(tileEntity.currentRenderDirection)) {
				renderer.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.1F, 1.0F);
				renderer.renderStandardBlock(block, x, y, z);
			}

			// North Face

			tileEntity.currentRenderDirection++;

			if (tileEntity.shallRenderPin(tileEntity.currentRenderDirection)) {
				renderer.setRenderBounds(0.0F, 0.0F, 0.9F, 1.0F, 1.0F, 1.0F);
				renderer.renderStandardBlock(block, x, y, z);
			}

			// East Face

			tileEntity.currentRenderDirection++;

			if (tileEntity.shallRenderPin(tileEntity.currentRenderDirection)) {
				renderer.setRenderBounds(0.9F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
				renderer.renderStandardBlock(block, x, y, z);
			}

			// South Face

			tileEntity.currentRenderDirection++;

			if (tileEntity.shallRenderPin(tileEntity.currentRenderDirection)) {
				renderer.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.1F);
				renderer.renderStandardBlock(block, x, y, z);
			}

			// West Face

			tileEntity.currentRenderDirection++;

			if (tileEntity.shallRenderPin(tileEntity.currentRenderDirection)) {
				renderer.setRenderBounds(0.0F, 0.0F, 0.0F, 0.1F, 1.0F, 1.0F);
				renderer.renderStandardBlock(block, x, y, z);
			}

			// Reset Render Bounds

			renderer.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		}
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,
			Block block, int modelId, RenderBlocks renderer) {

		if (ClientProxy.renderPass == 0) {
			// we are on the solid block render pass

			renderer.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
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