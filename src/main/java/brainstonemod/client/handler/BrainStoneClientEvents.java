package brainstonemod.client.handler;

import brainstonemod.BrainStone;
import brainstonemod.BrainStoneItems;
import brainstonemod.client.render.BSTriggerModel;
import brainstonemod.client.render.BrainStoneRenderHelper;
import brainstonemod.common.config.BrainStoneConfigWrapper;
import brainstonemod.common.handler.BrainStoneEventHandler;
import brainstonemod.common.item.ItemBrainStoneLifeCapacitor;
import brainstonemod.network.PacketDispatcher;
import brainstonemod.network.packet.serverbound.PacketRequestOverrides;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.NoArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author The_Fireplace
 * @author BrainStone
 */
@SuppressFBWarnings(
    value = "ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD",
    justification =
        "GuiIngameForge.left_height needs to modifed. This is normal practice and cannot be achieved sensibly in other ways.")
@SideOnly(Side.CLIENT)
@NoArgsConstructor(staticName = "registrar")
public class BrainStoneClientEvents {
  private static final ResourceLocation brainStoneLifeCapacitorTexture =
      new ResourceLocation(
          BrainStone.RESOURCE_PACKAGE, "textures/items/brain_stone_life_capacitor.png");

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
      Minecraft mc = Minecraft.getMinecraft();

      mc.profiler.startSection("BSLC");

      ItemStack capacitor = BrainStoneEventHandler.getBrainStoneLiveCapacitor(mc.player);

      if (capacitor != null) {
        GlStateManager.enableBlend();
        BrainStoneRenderHelper.setTexture(brainStoneLifeCapacitorTexture, 16);

        boolean rendersArmor =
            (ForgeHooks.getTotalArmorValue(mc.player) == 0) && GuiIngameForge.renderArmor;

        if (rendersArmor) {
          GuiIngameForge.left_height -= 10;
        }

        int width = event.getResolution().getScaledWidth();
        int height = event.getResolution().getScaledHeight();
        int left = (width / 2) - 91;
        int top = height - GuiIngameForge.left_height;
        ItemBrainStoneLifeCapacitor itemCapacitor = BrainStoneItems.brainStoneLifeCapacitor();

        BrainStoneRenderHelper.drawTexturedRect(left, top - 8, 16, 16, 0, 0, 16, 16, 10);

        BrainStoneRenderHelper.setTexture(Gui.ICONS, 256);

        BrainStoneRenderHelper.drawTexturedRect(left + 16, top - 4, 9, 9, 16, 0, 9, 9, 10);
        BrainStoneRenderHelper.drawTexturedRect(left + 16, top - 4, 9, 9, 52, 0, 9, 9, 10);

        float hearts =
            (float) itemCapacitor.getEnergyStoredLong(capacitor)
                / (float) (ItemBrainStoneLifeCapacitor.RFperHalfHeart * 2);
        long maxHearts =
            itemCapacitor.getMaxEnergyStoredLong(capacitor)
                / (ItemBrainStoneLifeCapacitor.RFperHalfHeart * 2);

        BrainStoneRenderHelper.drawString(
            String.format("%.1f/%d", hearts, maxHearts), left + 26, top - 3, 0xFF0000);

        if (rendersArmor) {
          GuiIngameForge.left_height += 26;
        } else {
          GuiIngameForge.left_height += 16;
        }

        GlStateManager.disableBlend();
      }

      mc.profiler.endSection();
    }
  }
}
