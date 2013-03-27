package mods.brainstone.templates;

import java.util.HashMap;
import java.util.Set;
import wg;
import yt;
import yv;

public abstract class BrainStoneEnchantmentHelper extends yv
{
  public static final wg addEnchantment(wg item, yt enchantment, int level)
  {
    return addEnchantment(item, enchantment.z, level);
  }

  public static final wg addEnchantment(wg item, int enchantment, int level)
  {
    HashMap enchantments = (HashMap)yv.a(item);

    int size = enchantments.size();
    Integer[] keys = (Integer[])enchantments.keySet().toArray(new Integer[size]);

    if (enchantments.containsKey(Integer.valueOf(enchantment)))
    {
      for (int i = 0; i < size; i++) {
        int key = keys[i].intValue();

        int tmp = ((Integer)enchantments.get(Integer.valueOf(key))).intValue();
        enchantments.remove(Integer.valueOf(key));

        if (key == enchantment) {
          int maxLevel = getEnchantment(enchantment).b();

          level += tmp;

          tmp = level > maxLevel ? maxLevel : level;
        }

        enchantments.put(Integer.valueOf(key), Integer.valueOf(tmp));
      }
    } else {
      yt newEnchantment = getEnchantment(enchantment);

      if (!canEnchantItem(newEnchantment, item)) {
        return item;
      }

      for (int i = 0; i < size; i++) {
        yt tmpEnch = getEnchantment(keys[i].intValue());

        if (!canApplyTogether(newEnchantment, tmpEnch)) {
          return item;
        }
      }
      int maxLevel = newEnchantment.b();

      enchantments.put(Integer.valueOf(enchantment), Integer.valueOf(level > maxLevel ? maxLevel : level));
    }

    yv.a(enchantments, item);

    return item;
  }

  public static final boolean canApplyTogether(yt enchantment1, yt enchantment2)
  {
    return (enchantment1.a(enchantment2)) && (enchantment2.a(enchantment1));
  }

  public static final boolean canApplyTogether(yt enchantment1, int enchantment2)
  {
    return canApplyTogether(enchantment1, getEnchantment(enchantment2));
  }

  public static final boolean canApplyTogether(int enchantment1, yt enchantment2)
  {
    return canApplyTogether(getEnchantment(enchantment1), enchantment2);
  }

  public static final boolean canApplyTogether(int enchantment1, int enchantment2)
  {
    return canApplyTogether(getEnchantment(enchantment1), getEnchantment(enchantment2));
  }

  public static final boolean canEnchantItem(yt enchantment, wg item)
  {
    return enchantment.a(item);
  }

  public static final boolean canEnchantItem(int enchantment, wg item) {
    return canEnchantItem(getEnchantment(enchantment), item);
  }

  public static final yt getEnchantment(int enchantmentId) {
    return yt.b[enchantmentId];
  }

  public static final wg setEnchantment(wg item, yt enchantment, int level)
  {
    return setEnchantment(item, enchantment.z, level);
  }

  public static final wg setEnchantment(wg item, int enchantment, int level)
  {
    HashMap enchantments = (HashMap)yv.a(item);

    int size = enchantments.size();
    Integer[] keys = (Integer[])enchantments.keySet().toArray(new Integer[size]);

    if (enchantments.containsKey(Integer.valueOf(enchantment)))
    {
      for (int i = 0; i < size; i++) {
        int key = keys[i].intValue();

        int tmp = ((Integer)enchantments.get(Integer.valueOf(key))).intValue();
        enchantments.remove(Integer.valueOf(key));

        if (key == enchantment) {
          int maxLevel = getEnchantment(enchantment).b();

          tmp = level > maxLevel ? maxLevel : level;
        }

        enchantments.put(Integer.valueOf(key), Integer.valueOf(tmp));
      }
    } else {
      yt newEnchantment = getEnchantment(enchantment);

      if (!canEnchantItem(newEnchantment, item)) {
        return item;
      }

      for (int i = 0; i < size; i++) {
        yt tmpEnch = getEnchantment(keys[i].intValue());

        if (!canApplyTogether(newEnchantment, tmpEnch)) {
          return item;
        }
      }
      int maxLevel = newEnchantment.b();

      enchantments.put(Integer.valueOf(enchantment), Integer.valueOf(level > maxLevel ? maxLevel : level));
    }

    yv.a(enchantments, item);

    return item;
  }
}