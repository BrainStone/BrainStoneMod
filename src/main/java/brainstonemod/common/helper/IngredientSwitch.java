package brainstonemod.common.helper;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

@NoArgsConstructor(staticName = "make")
public class IngredientSwitch {
	private List<Switch> ingredients = new LinkedList<>();

	public IngredientSwitch add(Supplier<Boolean> condition, Object ingredient) {
		return add(condition, () -> ingredient);
	}

	public IngredientSwitch add(Supplier<Boolean> condition, Supplier<Object> ingredientSupplier) {
		ingredients.add(new Switch(condition, ingredientSupplier));

		return this;
	}

	public Optional<Object> getIngredient() {
		return ingredients.stream().filter(Switch::isActive).map(Switch::get).findFirst();
	}

	public Object getIngredient(Object fallback) {
		return getIngredient().orElse(fallback);
	}

	public Object getIngredient(Supplier<Object> fallbackSupplier) {
		return getIngredient().orElseGet(fallbackSupplier);
	}

	@RequiredArgsConstructor
	private class Switch {
		private final Supplier<Boolean> condition;
		@Delegate(types = Supplier.class)
		private final Supplier<Object> ingredientSupplier;

		public boolean isActive() {
			return condition.get();
		}
	}
}
