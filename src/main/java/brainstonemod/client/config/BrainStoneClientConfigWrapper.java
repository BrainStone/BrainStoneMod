package brainstonemod.client.config;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Map.Entry;

import brainstonemod.common.compat.BrainStoneModules;
import brainstonemod.common.compat.jei.BrainstoneJEIPlugin;
import brainstonemod.common.item.energy.ItemBrainStoneLifeCapacitor;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BrainStoneClientConfigWrapper {
	@Getter
	private static boolean BSLCallowStealing;
	@Getter
	@JEIReload
	private static long BSLCRFperHalfHeart;

	/**
	 * Set the override values.
	 * 
	 * @param values
	 *            The values from the server
	 * @throws RuntimeException
	 *             Throws an exception when the map contains a field it doesn't
	 *             expect (using a sneaky throw from lombok) or if something
	 *             else went terribly wrong.
	 */
	@SneakyThrows
	public static void setOverrideValues(Map<String, Object> values) {
		Field field;
		Object valueCache;
		boolean reloadJEI = false;

		for (Entry<String, Object> value : values.entrySet()) {
			field = BrainStoneClientConfigWrapper.class.getDeclaredField(value.getKey());
			valueCache = field.get(null);

			field.set(null, value.getValue());

			if (field.isAnnotationPresent(JEIReload.class) && !value.getValue().equals(valueCache))
				reloadJEI = true;
		}

		ItemBrainStoneLifeCapacitor.updateRFperHalfHeart();

		if (BrainStoneModules.JEI() && reloadJEI)
			BrainstoneJEIPlugin.reloadJEI();
	}
}
