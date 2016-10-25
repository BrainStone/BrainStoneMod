package brainstonemod.client.handler;

import brainstonemod.client.render.BSTriggerModel;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author The_Fireplace
 */
public class BrainStoneClientEvents {
    @SubscribeEvent
    public void onModelBakeEvent(ModelBakeEvent event)
    {
        Object object =  event.getModelRegistry().getObject(BSTriggerModel.variantTag);
        if (object instanceof IBakedModel) {
            IBakedModel existingModel = (IBakedModel)object;
            BSTriggerModel customModel = new BSTriggerModel(existingModel);
            event.getModelRegistry().putObject(BSTriggerModel.variantTag, customModel);
        }
    }
}
