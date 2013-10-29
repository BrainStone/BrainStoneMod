package brainstonemod.templates;

import java.util.HashMap;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;

public abstract class BrainStoneEnchantmentHelper extends EnchantmentHelper {
	public static final ItemStack addEnchantment(ItemStack item,
			Enchantment enchantment, int level) {
		return addEnchantment(item, enchantment.effectId, level);
	}

	public static final ItemStack addEnchantment(ItemStack item,
			int enchantment, int level) {
		@SuppressWarnings("unchecked")
		final HashMap<Integer, Integer> enchantments = (HashMap<Integer, Integer>) EnchantmentHelper
				.getEnchantments(item);
		final int size = enchantments.size();
		final Integer[] keys = enchantments.keySet().toArray(new Integer[size]);

		if (enchantments.containsKey(enchantment)) {
			int key;
			int tmp;

			for (int i = 0; i < size; i++) {
				key = keys[i];

				tmp = enchantments.get(key);
				enchantments.remove(key);

				if (key == enchantment) {
					final int maxLevel = getEnchantment(enchantment)
							.getMaxLevel();
					level += tmp;

					tmp = (level > maxLevel) ? maxLevel : level;
				}

				enchantments.put(key, tmp);
			}
		} else {
			final Enchantment newEnchantment = getEnchantment(enchantment);

			if (!canEnchantItem(newEnchantment, item))
				return item;

			Enchantment tmpEnch;

			for (int i = 0; i < size; i++) {
				tmpEnch = getEnchantment(keys[i]);

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

	public static final boolean canApplyTogether(Enchantment enchantment1,
			int enchantment2) {
		return canApplyTogether(enchantment1, getEnchantment(enchantment2));
	}

	public static final boolean canApplyTogether(int enchantment1,
			Enchantment enchantment2) {
		return canApplyTogether(getEnchantment(enchantment1), enchantment2);
	}

	public static final boolean canApplyTogether(int enchantment1,
			int enchantment2) {
		return canApplyTogether(getEnchantment(enchantment1),
				getEnchantment(enchantment2));
	}

	public static final boolean canEnchantItem(Enchantment enchantment,
			ItemStack item) {
		return enchantment.canApply(item);
	}

	public static final boolean canEnchantItem(int enchantment, ItemStack item) {
		return canEnchantItem(getEnchantment(enchantment), item);
	}

	public static final Enchantment getEnchantment(int enchantmentId) {
		return Enchantment.enchantmentsList[enchantmentId];
	}

	public static final ItemStack setEnchantment(ItemStack item,
			Enchantment enchantment, int level) {
		return setEnchantment(item, enchantment.effectId, level);
	}

	public static final ItemStack setEnchantment(ItemStack item,
			int enchantment, int level) {
		@SuppressWarnings("unchecked")
		final HashMap<Integer, Integer> enchantments = (HashMap<Integer, Integer>) EnchantmentHelper
				.getEnchantments(item);
		final int size = enchantments.size();
		final Integer[] keys = enchantments.keySet().toArray(new Integer[size]);

		if (enchantments.containsKey(enchantment)) {
			int key;
			int tmp;

			for (int i = 0; i < size; i++) {
				key = keys[i];

				tmp = enchantments.get(key);
				enchantments.remove(key);

				if (key == enchantment) {
					final int maxLevel = getEnchantment(enchantment)
							.getMaxLevel();

					tmp = (level > maxLevel) ? maxLevel : level;
				}

				enchantments.put(key, tmp);
			}
		} else {
			final Enchantment newEnchantment = getEnchantment(enchantment);

			if (!canEnchantItem(newEnchantment, item))
				return item;

			Enchantment tmpEnch;

			for (int i = 0; i < size; i++) {
				tmpEnch = getEnchantment(keys[i]);

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