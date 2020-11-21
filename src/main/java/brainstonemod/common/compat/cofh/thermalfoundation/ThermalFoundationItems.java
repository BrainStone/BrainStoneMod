package brainstonemod.common.compat.cofh.thermalfoundation;

import cofh.thermalfoundation.item.ItemMaterial;
import lombok.experimental.UtilityClass;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

@UtilityClass
public class ThermalFoundationItems {
  public static ItemStack getSlag() {
    return ItemMaterial.crystalSlag;
  }

  public static ItemStack getPyrotheumDust() {
    return ItemMaterial.dustPyrotheum;
  }

  public static String getPyrotheumDustDict() {
    return OreDictionary.getOreName(OreDictionary.getOreIDs(getPyrotheumDust())[0]);
  }
}
