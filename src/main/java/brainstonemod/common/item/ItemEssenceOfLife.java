package brainstonemod.common.item;

import brainstonemod.BrainStone;
import lombok.AllArgsConstructor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemEssenceOfLife extends ItemFood {
	public ItemEssenceOfLife() {
		super(20, 10.0F, false);

		setAlwaysEdible();
		setCreativeTab(BrainStone.getCreativeTab(CreativeTabs.MATERIALS));
	}

	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return EnumRarity.UNCOMMON;
	}

	@Override
	protected void onFoodEaten(ItemStack stack, World world, EntityPlayer player) {
		if (!world.isRemote) {
			PotionInfo[] potionEffects = new PotionInfo[] {
					// Absorption X for 120s
					new PotionInfo(MobEffects.ABSORPTION, 120, 10),
					// Resistance III for 120s
					new PotionInfo(MobEffects.RESISTANCE, 120, 3),
					// Strength III for 120s
					new PotionInfo(MobEffects.STRENGTH, 120, 3),
					// Regeneration III for 30s
					new PotionInfo(MobEffects.REGENERATION, 30, 3),
					// Fire Resistance for 300s
					new PotionInfo(MobEffects.FIRE_RESISTANCE, 300),
					// Water Breathing for 300s
					new PotionInfo(MobEffects.WATER_BREATHING, 300) };

			for (PotionInfo potionToAdd : potionEffects) {
				player.addPotionEffect(potionToAdd.toPotionEffect());
			}
		}
	}

	@AllArgsConstructor
	private class PotionInfo {
		private final Potion potionEffect;
		private final int duration;
		private final int level;

		public PotionInfo(Potion potionEffect, int duration) {
			this(potionEffect, duration, 1);
		}

		public PotionEffect toPotionEffect() {
			return new PotionEffect(potionEffect, duration * 20, level - 1);
		}
	}
}
