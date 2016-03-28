package brainstonemod.common.item.template;

import brainstonemod.BrainStone;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemBrainStoneBase extends Item {
	private final EnumRarity customRarity;

	public ItemBrainStoneBase() {
		this(null);
	}

	public ItemBrainStoneBase(EnumRarity customRarity) {
		super();

		this.customRarity = customRarity;
	}

	@Override
	public EnumRarity getRarity(ItemStack stack) {
		if (customRarity == null)
			return super.getRarity(stack);
		else
			return customRarity;
	}

	@Override
	public void registerIcons(IIconRegister iconRegister) {
		itemIcon = iconRegister
				.registerIcon(BrainStone.RESOURCE_PREFIX + this.getUnlocalizedName().replaceFirst("item.", ""));
	}
}