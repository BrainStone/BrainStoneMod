package brainstonemod.common.helper;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.ISpecialArmor.ArmorProperties;

public class BrainStoneDamageHelper {
	public static float getAdjustedDamage(DamageSource damageSource, float initalDamage, EntityPlayer player,
			boolean simluate) {
		float adjustedDamage = initalDamage;

		adjustedDamage = applyArmor(player, player.inventory.armorInventory, damageSource, adjustedDamage, simluate);
		if (adjustedDamage <= 0)
			return 0;

		adjustedDamage = applyPotionDamageCalculations(damageSource, adjustedDamage, player);
		float f1 = adjustedDamage;
		adjustedDamage = Math.max(adjustedDamage - player.getAbsorptionAmount(), 0.0F);

		if (!simluate) {
			player.setAbsorptionAmount(player.getAbsorptionAmount() - (f1 - adjustedDamage));
		}

		return adjustedDamage;
	}

	/**
	 * Reduces damage, depending on potions<br />
	 * Taken from
	 * {@link net.minecraft.entity.EntityLivingBase#applyPotionDamageCalculations}
	 */
	private static float applyPotionDamageCalculations(DamageSource damageSource, float initialDamage,
			EntityPlayer player) {
		if (damageSource.isDamageAbsolute())
			return initialDamage;
		else {
			if (player.isPotionActive(MobEffects.RESISTANCE) && (damageSource != DamageSource.OUT_OF_WORLD)) {
				int i = (player.getActivePotionEffect(MobEffects.RESISTANCE).getAmplifier() + 1) * 5;
				int j = 25 - i;
				float f = initialDamage * j;
				initialDamage = f / 25.0F;
			}

			if (initialDamage <= 0.0F)
				return 0.0F;
			else {
				int k = EnchantmentHelper.getEnchantmentModifierDamage(player.getArmorInventoryList(), damageSource);

				if (k > 0) {
					initialDamage = CombatRules.getDamageAfterMagicAbsorb(initialDamage, k);
				}

				return initialDamage;
			}
		}
	}

	/**
	 * Gathers and applies armor reduction to damage being dealt to a
	 * entity.<br />
	 * Taken from
	 * {@link net.minecraftforge.common.ISpecialArmor.ArmorProperties#applyArmor}.
	 * Added simulate flag to prevent damaging of armor.
	 *
	 * @param entity
	 *            The Entity being damage
	 * @param inventory
	 *            An array of armor items
	 * @param source
	 *            The damage source type
	 * @param damage
	 *            The total damage being done
	 * @return The left over damage that has not been absorbed by the armor
	 */
	private static float applyArmor(EntityLivingBase entity, NonNullList<ItemStack> inventory, DamageSource source,
			double damage, boolean simulate) {
		if (source.isUnblockable())
			return (float) damage;

		double totalArmor = entity.getTotalArmorValue();
		double totalToughness = entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue();

		ArrayList<ArmorProperties> dmgVals = new ArrayList<ArmorProperties>();
		for (int slot = 0; slot < inventory.size(); slot++) {
			ItemStack stack = inventory.get(slot);

			if (stack.isEmpty()) {
				continue;
			}

			ArmorProperties prop = null;
			if (stack.getItem() instanceof ISpecialArmor) {
				ISpecialArmor armor = (ISpecialArmor) stack.getItem();
				prop = armor.getProperties(entity, stack, source, damage, slot).copy();
				totalArmor += prop.Armor;
				totalToughness += prop.Toughness;
			} else if (stack.getItem() instanceof ItemArmor) {
				ItemArmor armor = (ItemArmor) stack.getItem();
				prop = new ArmorProperties(0, 0, Integer.MAX_VALUE);
				prop.Armor = armor.damageReduceAmount;
				prop.Toughness = armor.toughness;
			}
			if (prop != null) {
				prop.Slot = slot;
				dmgVals.add(prop);
			}
		}
		if (dmgVals.size() > 0) {
			ArmorProperties[] props = dmgVals.toArray(new ArmorProperties[dmgVals.size()]);
			standardizeList(props, damage);
			int level = props[0].Priority;
			double ratio = 0;
			for (ArmorProperties prop : props) {
				if (level != prop.Priority) {
					damage -= (damage * ratio);
					ratio = 0;
					level = prop.Priority;
				}
				ratio += prop.AbsorbRatio;

				double absorb = damage * prop.AbsorbRatio;
				if ((absorb > 0) && !simulate) {
					ItemStack stack = inventory.get(prop.Slot);
					int itemDamage = (int) Math.max(1, absorb);
					if (stack.getItem() instanceof ISpecialArmor) {
						((ISpecialArmor) stack.getItem()).damageArmor(entity, stack, source, itemDamage, prop.Slot);
					} else {
						stack.damageItem(itemDamage, entity);
					}
					if (stack.isEmpty()) {
						inventory.set(prop.Slot, ItemStack.EMPTY);
					}
				}
			}
			damage -= (damage * ratio);
		}
		if ((damage > 0) && ((totalArmor > 0) || (totalToughness > 0))) {
			double armorDamage = Math.max(1.0F, damage / 4.0F);

			for (int i = 0; i < inventory.size(); i++) {
				if ((inventory.get(i).getItem() instanceof ItemArmor) && !simulate) {
					inventory.get(i).damageItem((int) armorDamage, entity);

					if (inventory.get(i).getCount() == 0) {
						inventory.set(i, ItemStack.EMPTY);
					}
				}
			}
			damage = CombatRules.getDamageAfterAbsorb((float) damage, (float) totalArmor, (float) totalToughness);
		}

		return (float) (damage);
	}

	/**
	 * Sorts and standardizes the distribution of damage over armor. <br />
	 * Taken from
	 * {@link net.minecraftforge.common.ISpecialArmor.ArmorProperties#StandardizeList}
	 *
	 * @param armor
	 *            The armor information
	 * @param damage
	 *            The total damage being received
	 */
	private static void standardizeList(ArmorProperties[] armor, double damage) {
		Arrays.sort(armor);

		int start = 0;
		double total = 0;
		int priority = armor[0].Priority;
		int pStart = 0;
		boolean pChange = false;
		boolean pFinished = false;

		for (int x = 0; x < armor.length; x++) {
			total += armor[x].AbsorbRatio;
			if ((x == (armor.length - 1)) || (armor[x].Priority != priority)) {
				if (armor[x].Priority != priority) {
					total -= armor[x].AbsorbRatio;
					x--;
					pChange = true;
				}
				if (total > 1) {
					for (int y = start; y <= x; y++) {
						double newRatio = armor[y].AbsorbRatio / total;
						if ((newRatio * damage) > armor[y].AbsorbMax) {
							armor[y].AbsorbRatio = armor[y].AbsorbMax / damage;
							total = 0;
							for (int z = pStart; z <= y; z++) {
								total += armor[z].AbsorbRatio;
							}
							start = y + 1;
							x = y;
							break;
						} else {
							armor[y].AbsorbRatio = newRatio;
							pFinished = true;
						}
					}
					if (pChange && pFinished) {
						damage -= (damage * total);
						total = 0;
						start = x + 1;
						priority = armor[start].Priority;
						pStart = start;
						pChange = false;
						pFinished = false;
						if (damage <= 0) {
							for (int y = x + 1; y < armor.length; y++) {
								armor[y].AbsorbRatio = 0;
							}
							break;
						}
					}
				} else {
					for (int y = start; y <= x; y++) {
						total -= armor[y].AbsorbRatio;
						if ((damage * armor[y].AbsorbRatio) > armor[y].AbsorbMax) {
							armor[y].AbsorbRatio = armor[y].AbsorbMax / damage;
						}
						total += armor[y].AbsorbRatio;
					}
					damage -= (damage * total);
					total = 0;
					if (x != (armor.length - 1)) {
						start = x + 1;
						priority = armor[start].Priority;
						pStart = start;
						pChange = false;
						if (damage <= 0) {
							for (int y = x + 1; y < armor.length; y++) {
								armor[y].AbsorbRatio = 0;
							}
							break;
						}
					}
				}
			}
		}
	}
}
