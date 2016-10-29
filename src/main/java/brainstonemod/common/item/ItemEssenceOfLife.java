package brainstonemod.common.item;

import brainstonemod.BrainStone;
import brainstonemod.common.item.template.ItemBrainStoneFood;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemEssenceOfLife extends ItemBrainStoneFood {
	public ItemEssenceOfLife() {
		super(20, 10.0F);

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
			int duration;
			PotionEffect potion;
			Object[][] potionEffects = new Object[][] {
					// Absorption X for 120s
					{ MobEffects.ABSORPTION, 120, 9 },
					// Resitance III for 120s
					{ MobEffects.RESISTANCE, 120, 2 },
					// Strength III for 120s
					{ MobEffects.STRENGTH, 120, 2 },
					// Regeneration III for 30s
					{ MobEffects.REGENERATION, 30, 2 },
					// Fire Resistance for 300s
					{ MobEffects.FIRE_RESISTANCE, 300, 0 },
					// Water Breathing for 300s
					{ MobEffects.WATER_BREATHING, 300, 0 } };

			for (Object[] potionToAdd : potionEffects) {
				duration = 0;
				potion = player.getActivePotionEffect((Potion) potionToAdd[0]);

				if (potion != null)
					duration = potion.getDuration();

				player.addPotionEffect(new PotionEffect(((Potion) potionToAdd[0]),
						Math.max((Integer) potionToAdd[1] * 20, duration), (Integer) potionToAdd[2]));
			}
		}
	}
}
