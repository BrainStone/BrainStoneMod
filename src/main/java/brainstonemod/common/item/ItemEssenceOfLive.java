package brainstonemod.common.item;

import brainstonemod.BrainStone;
import brainstonemod.common.item.template.ItemBrainStoneFood;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemEssenceOfLive extends ItemBrainStoneFood {
	public ItemEssenceOfLive() {
		super(20, 10.0F);

		setAlwaysEdible();
		setCreativeTab(BrainStone.getCreativeTab(CreativeTabs.tabMaterials));
	}

	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return EnumRarity.uncommon;
	}

	@Override
	protected void onFoodEaten(ItemStack stack, World world, EntityPlayer player) {
		if (!world.isRemote) {
			int duration;
			PotionEffect potion;
			Object[][] potionEffects = new Object[][]{
				// Absorption X for 120s
				{Potion.field_76444_x, 120, 9},
				// Resitance III for 120s
				{Potion.resistance, 120, 2},
				// Strength III for 120s
				{Potion.damageBoost, 120, 2},
				// Regeneration III for 30s
				{Potion.regeneration, 30, 2},
				// Fire Resistance for 300s
				{Potion.fireResistance, 300, 0},
				// Water Breathing for 300s
				{Potion.waterBreathing, 300, 0}
			};
			
			for(Object[] potionToAdd : potionEffects) {
				duration = 0;
				potion = player.getActivePotionEffect((Potion) potionToAdd[0]);
				if (potion != null)
					duration = potion.getDuration();
				player.addPotionEffect(new PotionEffect(((Potion) potionToAdd[0]).id, (Integer)potionToAdd[1] * 20, (Integer)potionToAdd[2]));
			}
		}
	}
}
