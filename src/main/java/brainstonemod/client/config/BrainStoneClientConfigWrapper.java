package brainstonemod.client.config;

import java.util.Map;
import java.util.Map.Entry;

import brainstonemod.common.item.energy.ItemBrainStoneLifeCapacitor;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import mezz.jei.JustEnoughItems;

@UtilityClass
public class BrainStoneClientConfigWrapper {
	@Getter
	private static boolean BSLCallowStealing;
	@Getter
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
		for (Entry<String, Object> value : values.entrySet())
			BrainStoneClientConfigWrapper.class.getDeclaredField(value.getKey()).set(null, value.getValue());
		
		ItemBrainStoneLifeCapacitor.updateRFperHalfHeart();
		
		// TODO: Only call when necessary
		JustEnoughItems.getProxy().restartJEI();
	}
}
