package brainstonemod.common.item.template;

import net.minecraft.item.ItemFood;

public class ItemBrainStoneFood extends ItemFood {
	public ItemBrainStoneFood(int healAmount, float saturationModifier) {
		this(healAmount, saturationModifier, false);
	}

	public ItemBrainStoneFood(int healAmount, float saturationModifier, boolean isWolfsFavoriteMeat) {
		super(healAmount, saturationModifier, isWolfsFavoriteMeat);
	}
}
