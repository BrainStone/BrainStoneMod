package brainstonemod.common.helper;

import brainstonemod.BrainStone;
import java.text.NumberFormat;

/**
 * This code is mostly taken from <a href=
 * "https://github.com/SleepyTrousers/EnderIO/blob/1.7.10/src/main/java/crazypants/enderio/machine/power/PowerDisplayUtil.java">
 * https://github.com/SleepyTrousers/EnderIO/blob/1.7.10/src/main/java/
 * crazypants/enderio/machine/power/PowerDisplayUtil.java</a>. All credit to the original author
 *
 * @author BrainStone
 */
public class BrainStonePowerDisplayUtil {
  private static final NumberFormat INT_NF = NumberFormat.getIntegerInstance();
  private static final NumberFormat FLOAT_NF = NumberFormat.getInstance();

  static {
    FLOAT_NF.setMinimumFractionDigits(1);
    FLOAT_NF.setMaximumFractionDigits(1);
  }

  public static String abrevation() {
    return BrainStone.proxy.format("brainstone.power.rf");
  }

  public static String perTickStr() {
    return BrainStone.proxy.format("brainstone.power.tick");
  }

  public static String formatPowerPerTick(double powerPerTick) {
    return formatPower(powerPerTick) + " " + abrevation() + perTickStr();
  }

  public static String formatStoredPower(double amount, double capacity) {
    return formatPower(amount)
        + "/"
        + formatPower(capacity)
        + " "
        + BrainStonePowerDisplayUtil.abrevation();
  }

  public static String formatPower(double power) {
    return INT_NF.format(power);
  }
}
