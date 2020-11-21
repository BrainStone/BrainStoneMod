package brainstonemod.common.compat.mysticalagriculture;

import com.blakebr0.mysticalagriculture.items.ModItems;
import lombok.experimental.UtilityClass;
import net.minecraft.item.ItemStack;

@UtilityClass
public class MysticalAgricultureItems {
  public static ItemStack getSupremiumApple() {
    return new ItemStack(ModItems.itemSupremiumApple);
  }
}
