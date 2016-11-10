package brainstonemod.common.compat;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import brainstonemod.common.helper.BSP;
import lombok.Data;
import lombok.NonNull;

@Data
public class ModuleInformation implements Comparable<ModuleInformation> {
	@NonNull
	private final String modid;
	@NonNull
	private final String name;
	@NonNull
	private final String message;
	@NonNull
	private final Field statusField;
	@NonNull
	private final Method method;
	@NonNull
	private final Class<? extends IModIntegration> integrationClass;
	private IModIntegration integration;

	@Override
	public int compareTo(ModuleInformation module) {
		return name.compareToIgnoreCase(module.name);
	}

	public boolean isActive() {
		try {
			return (Boolean) method.invoke(null);
		} catch (Exception e) {
			BSP.errorException(e);

			return false;
		}
	}

	public void disable() {
		try {
			statusField.setAccessible(true);
			statusField.set(null, false);
			statusField.setAccessible(false);
		} catch (Exception e) {
			BSP.errorException(e);
		}
	}
}
