package brainstonemod.common.item.template;

import brainstonemod.BrainStone;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemFood;

public class ItemBrainStoneFood extends ItemFood {
	public ItemBrainStoneFood(int healAmount, float saturationModifier) {
		this(healAmount, saturationModifier, false);
	}

	public ItemBrainStoneFood(int healAmount, float saturationModifier, boolean isWolfsFavoriteMeat) {
		super(healAmount, saturationModifier, isWolfsFavoriteMeat);
	}

	@Override
	public void registerIcons(IIconRegister iconRegister) {
		itemIcon = iconRegister
				.registerIcon(BrainStone.RESOURCE_PREFIX + this.getUnlocalizedName().replaceFirst("item.", ""));
	}
}
