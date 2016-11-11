package brainstonemod.client.handler;

import brainstonemod.BrainStone;
import brainstonemod.client.render.BSTriggerModel;
import brainstonemod.client.render.BrainStoneRenderHelper;
import brainstonemod.common.config.BrainStoneConfigWrapper;
import brainstonemod.network.PacketDispatcher;
import brainstonemod.network.packet.serverbound.PacketRequestOverrides;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author The_Fireplace
 * @author BrainStone
 */
@SideOnly(Side.CLIENT)
public class BrainStoneClientEvents {
	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
		if (eventArgs.getModID().equals(BrainStone.MOD_ID)) {
			BrainStoneConfigWrapper.loadConfig();
			PacketDispatcher.sendToServer(new PacketRequestOverrides());
		}
	}

	@SubscribeEvent
	public void onModelBakeEvent(ModelBakeEvent event) {
		Object object = event.getModelRegistry().getObject(BSTriggerModel.variantTag);

		if (object instanceof IBakedModel) {
			IBakedModel existingModel = (IBakedModel) object;
			BSTriggerModel customModel = new BSTriggerModel(existingModel);
			event.getModelRegistry().putObject(BSTriggerModel.variantTag, customModel);
		}
	}

	@SubscribeEvent
	public void onRendeGameOverlay(RenderGameOverlayEvent.Post event) {
		if (event.getType() == ElementType.ALL) {
			BrainStoneRenderHelper.setTexture("textures/items/brainStoneLifeCapacitor.png", 16);

			BrainStoneRenderHelper.drawTexturedRect(0, 100, 32, 32, 0, 0, 16, 16, 10);
		}
	}
}
