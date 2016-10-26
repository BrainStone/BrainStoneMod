package brainstonemod.common.api.jei;

import brainstonemod.common.helper.BrainStoneLifeCapacitorUpgrade;
import mezz.jei.api.recipe.wrapper.ICraftingRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import scala.actors.threadpool.Arrays;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

/**
 * @author The_Fireplace
 */
public class CapacitorUpgradeRecipeWrapper implements ICraftingRecipeWrapper {

    private BrainStoneLifeCapacitorUpgrade recipe;

    public CapacitorUpgradeRecipeWrapper(BrainStoneLifeCapacitorUpgrade recipe){
        this.recipe=recipe;
    }

    @Nonnull
    @Override
    public List getInputs() {
        return Arrays.asList(recipe.getStacks());
    }

    @Nonnull
    @Override
    public List<ItemStack> getOutputs() {
        return Collections.singletonList(recipe.getRecipeOutput());
    }

    @Override
    public List<FluidStack> getFluidInputs() {
        return null;
    }

    @Override
    public List<FluidStack> getFluidOutputs() {
        return null;
    }

    @Override
    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

    }

    @Override
    public void drawAnimations(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight) {

    }

    @Nullable
    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY) {
        return null;
    }

    @Override
    public boolean handleClick(@Nonnull Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
        return false;
    }
}