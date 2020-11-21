package brainstonemod.common.compat.tesla;

import lombok.experimental.UtilityClass;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraftforge.common.capabilities.Capability;

@UtilityClass
public class TeslaCompat {
  public static boolean isTeslaCapability(Capability<?> capability) {
    return (capability == TeslaCapabilities.CAPABILITY_CONSUMER)
        || (capability == TeslaCapabilities.CAPABILITY_PRODUCER)
        || (capability == TeslaCapabilities.CAPABILITY_HOLDER);
  }
}
