package brainstonemod.common.api.nei;

import brainstonemod.common.helper.BrainStoneLifeCapacitorUpgrade;
import codechicken.nei.NEIClientUtils;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.ShapelessRecipeHandler;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author The_Fireplace
 */
public class CapacitorUpgradeRecipeHandler extends ShapelessRecipeHandler {
    @Override
    public void loadTransferRects() {}

    @Override
    public String getRecipeName() {
        return NEIClientUtils.translate("recipe.capacitorupgrade");
    }

    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return null;
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if(outputId.equals("crafting") && this.getClass() == CapacitorUpgradeRecipeHandler.class) {
            Iterator i$ = CraftingManager.getInstance().getRecipeList().iterator();

            while(i$.hasNext()) {
                IRecipe irecipe = (IRecipe)i$.next();
                CachedCapacitorRecipe recipe = null;
                if(irecipe instanceof BrainStoneLifeCapacitorUpgrade) {
                    recipe = new CachedCapacitorRecipe((BrainStoneLifeCapacitorUpgrade) irecipe);
                }

                if(recipe != null) {
                    recipe.computeVisuals();
                    this.arecipes.add(recipe);
                }
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }

    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        Iterator i$ = CraftingManager.getInstance().getRecipeList().iterator();

        while(i$.hasNext()) {
            IRecipe irecipe = (IRecipe)i$.next();
            if(NEIServerUtils.areStacksSameTypeCrafting(irecipe.getRecipeOutput(), result)) {
                CachedCapacitorRecipe recipe = null;
                if(irecipe instanceof BrainStoneLifeCapacitorUpgrade) {
                    recipe = new CachedCapacitorRecipe((BrainStoneLifeCapacitorUpgrade) irecipe);
                }

                if(recipe != null) {
                    recipe.computeVisuals();
                    this.arecipes.add(recipe);
                }
            }
        }

    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        Iterator i$ = CraftingManager.getInstance().getRecipeList().iterator();

        while(i$.hasNext()) {
            IRecipe irecipe = (IRecipe)i$.next();
            CachedCapacitorRecipe recipe = null;
            if(irecipe instanceof BrainStoneLifeCapacitorUpgrade) {
                recipe = new CachedCapacitorRecipe((BrainStoneLifeCapacitorUpgrade)irecipe);
            }

            if(recipe != null && recipe.contains(recipe.ingredients, ingredient.getItem())) {
                recipe.computeVisuals();
                if(recipe.contains(recipe.ingredients, ingredient)) {
                    recipe.setIngredientPermutation(recipe.ingredients, ingredient);
                    this.arecipes.add(recipe);
                }
            }
        }

    }

    public class CachedCapacitorRecipe extends CachedRecipe {
        public ArrayList<PositionedStack> ingredients;
        public PositionedStack result;

        public CachedCapacitorRecipe(int width, int height, Object[] items, ItemStack out) {
            super();
            this.result = new PositionedStack(out, 119, 24);
            this.ingredients = new ArrayList();
            this.setIngredients(width, height, items);
        }

        public CachedCapacitorRecipe(BrainStoneLifeCapacitorUpgrade recipe) {
            this(2, 1, recipe.getStacks(), recipe.getRecipeOutput());
        }

        public void setIngredients(int width, int height, Object[] items) {
            for(int x = 0; x < width; ++x) {
                for(int y = 0; y < height; ++y) {
                    if(items[y * width + x] != null) {
                        PositionedStack stack = new PositionedStack(items[y * width + x], 25 + x * 18, 6 + y * 18, false);
                        stack.setMaxSize(1);
                        this.ingredients.add(stack);
                    }
                }
            }

        }

        @Override
        public List<PositionedStack> getIngredients() {
            return this.getCycledIngredients(CapacitorUpgradeRecipeHandler.this.cycleticks / 20, this.ingredients);
        }

        @Override
        public PositionedStack getResult() {
            return this.result;
        }

        public void computeVisuals() {
            Iterator i$ = this.ingredients.iterator();

            while(i$.hasNext()) {
                PositionedStack p = (PositionedStack)i$.next();
                p.generatePermutations();
            }

        }
    }
}
