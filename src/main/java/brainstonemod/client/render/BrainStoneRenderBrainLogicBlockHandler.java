package brainstonemod.client.render;

import static brainstonemod.common.helper.BrainStoneDirection.DOWN;
import static brainstonemod.common.helper.BrainStoneDirection.EAST;
import static brainstonemod.common.helper.BrainStoneDirection.NORTH;
import static brainstonemod.common.helper.BrainStoneDirection.SOUTH;
import static brainstonemod.common.helper.BrainStoneDirection.UP;
import static brainstonemod.common.helper.BrainStoneDirection.WEST;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import brainstonemod.client.ClientProxy;
import brainstonemod.common.block.BlockBrainLogicBlock;
import brainstonemod.common.tileentity.TileEntityBrainLogicBlock;
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

	private void renderPins(TileEntityBrainLogicBlock tileEntity,
			IBlockAccess world, RenderBlocks renderer, Block block, int x,
			int y, int z) {
		renderer.setOverrideBlockTexture(BlockBrainLogicBlock.textures[0]);

		// Top Face

		tileEntity.currentRenderDirection = UP;

		if (tileEntity.shallRenderPin(tileEntity.currentRenderDirection)) {
			renderer.setRenderBounds(0.0F, 0.9F, 0.0F, 1.0F, 1.0F, 1.0F);
			renderer.renderStandardBlock(block, x, y, z);
		}

		// Bottom Face

		tileEntity.currentRenderDirection = DOWN;

		if (tileEntity.shallRenderPin(tileEntity.currentRenderDirection)) {
			renderer.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.1F, 1.0F);
			renderer.renderStandardBlock(block, x, y, z);
		}

		// North Face

		tileEntity.currentRenderDirection = NORTH;

		if (tileEntity.shallRenderPin(tileEntity.currentRenderDirection)) {
			renderer.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.1F);
			renderer.renderStandardBlock(block, x, y, z);
		}

		// East Face

		tileEntity.currentRenderDirection = EAST;

		if (tileEntity.shallRenderPin(tileEntity.currentRenderDirection)) {
			renderer.setRenderBounds(0.9F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			renderer.renderStandardBlock(block, x, y, z);
		}

		// South Face

		tileEntity.currentRenderDirection = SOUTH;

		if (tileEntity.shallRenderPin(tileEntity.currentRenderDirection)) {
			renderer.setRenderBounds(0.0F, 0.0F, 0.9F, 1.0F, 1.0F, 1.0F);
			renderer.renderStandardBlock(block, x, y, z);
		}

		// West Face

		tileEntity.currentRenderDirection = WEST;

		if (tileEntity.shallRenderPin(tileEntity.currentRenderDirection)) {
			renderer.setRenderBounds(0.0F, 0.0F, 0.0F, 0.1F, 1.0F, 1.0F);
			renderer.renderStandardBlock(block, x, y, z);
		}

		// Reset Render Bounds

		renderer.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);

		renderer.clearOverrideBlockTexture();
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,
			Block block, int modelId, RenderBlocks renderer) {
		final TileEntityBrainLogicBlock tileEntity = (TileEntityBrainLogicBlock) renderer.blockAccess
				.getTileEntity(x, y, z);

		if (tileEntity != null) {
			tileEntity.currentRenderDirection = null;

			renderer.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			renderer.renderStandardBlock(block, x, y, z);

			renderPins(tileEntity, world, renderer, block, x, y, z);

			return true;
		}

		return false;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}
}