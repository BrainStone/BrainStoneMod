package brainstonemod.common.compat.enderio;

import crazypants.enderio.base.init.ModObject;
import lombok.experimental.UtilityClass;
import net.minecraft.item.ItemStack;

@UtilityClass
public final class EnderIOItems {
  public static final ItemStack getXPRod() {
    return new ItemStack(ModObject.itemXpTransfer.getItem(), 1, 0);
  }

  public static final ItemStack getOctadicCapacitor() {
    return new ItemStack(ModObject.itemBasicCapacitor.getItem(), 1, 2);
  }

  public static final ItemStack getSentientEnder() {
    return new ItemStack(ModObject.itemMaterial.getItem(), 1, 44);
  }
}
