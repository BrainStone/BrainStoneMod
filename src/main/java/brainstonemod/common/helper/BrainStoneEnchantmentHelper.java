package brainstonemod.common.helper;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;

import java.util.HashMap;

public abstract class BrainStoneEnchantmentHelper extends EnchantmentHelper {
	public static final ItemStack addEnchantment(ItemStack item,
			Enchantment enchantment, int level) {
		@SuppressWarnings("unchecked")
		final HashMap<Enchantment, Integer> enchantments = (HashMap<Enchantment, Integer>) EnchantmentHelper
				.getEnchantments(item);
		final int size = enchantments.size();
		final Enchantment[] keys = enchantments.keySet().toArray(new Enchantment[size]);

		if (enchantments.containsKey(enchantment)) {
			Enchantment key;
			int tmp;

			for (int i = 0; i < size; i++) {
				key = keys[i];

				tmp = enchantments.get(key);
				enchantments.remove(key);

				if (key == enchantment) {
					final int maxLevel = enchantment
							.getMaxLevel();
					level += tmp;

					tmp = (level > maxLevel) ? maxLevel : level;
				}

				enchantments.put(key, tmp);
			}
		} else {
			final Enchantment newEnchantment = enchantment;

			if (!canEnchantItem(newEnchantment, item))
				return item;

			Enchantment tmpEnch;

			for (int i = 0; i < size; i++) {
				tmpEnch = keys[i];

				if (!canApplyTogether(newEnchantment, tmpEnch))
					return item;
			}

			final int maxLevel = newEnchantment.getMaxLevel();

			enchantments
					.put(enchantment, (level > maxLevel) ? maxLevel : level);
		}

		EnchantmentHelper.setEnchantments(enchantments, item);

		return item;
	}

	public static final boolean canApplyTogether(Enchantment enchantment1,
			Enchantment enchantment2) {
		return enchantment1.canApplyTogether(enchantment2)
				&& enchantment2.canApplyTogether(enchantment1);
	}

	public static final boolean canEnchantItem(Enchantment enchantment, ItemStack item) {
		return enchantment.canApply(item);
	}

	public static final ItemStack setEnchantment(ItemStack item,
			Enchantment enchantment, int level) {
		@SuppressWarnings("unchecked")
		final HashMap<Enchantment, Integer> enchantments = (HashMap<Enchantment, Integer>) EnchantmentHelper
				.getEnchantments(item);
		final int size = enchantments.size();
		final Enchantment[] keys = enchantments.keySet().toArray(new Enchantment[size]);

		if (enchantments.containsKey(enchantment)) {
			Enchantment key;
			int tmp;

			for (int i = 0; i < size; i++) {
				key = keys[i];

				tmp = enchantments.get(key);
				enchantments.remove(key);

				if (key == enchantment) {
					final int maxLevel = enchantment
							.getMaxLevel();

					tmp = (level > maxLevel) ? maxLevel : level;
				}

				enchantments.put(key, tmp);
			}
		} else {
			final Enchantment newEnchantment = enchantment;

			if (!canEnchantItem(newEnchantment, item))
				return item;

			Enchantment tmpEnch;

			for (int i = 0; i < size; i++) {
				tmpEnch = keys[i];

				if (!canApplyTogether(newEnchantment, tmpEnch))
					return item;
			}

			final int maxLevel = newEnchantment.getMaxLevel();

			enchantments
					.put(enchantment, (level > maxLevel) ? maxLevel : level);
		}

		EnchantmentHelper.setEnchantments(enchantments, item);

		return item;
	}
}