package brainstonemod.common.capability;

import brainstonemod.common.compat.BrainStoneModules;
import com.brandon3055.draconicevolution.api.IInvCharge;
import java.math.BigInteger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;

/**
 * This code is mostly taken from <a href=
 * "https://github.com/SleepyTrousers/EnderIO/blob/1.10/src/main/java/crazypants/enderio/power/IInternalPoweredItem.java">
 * https://github.com/SleepyTrousers/EnderIO/blob/1.10/src/main/java/crazypants/
 * enderio/power/IInternalPoweredItem.java</a>. All credit to the original author
 *
 * @author BrainStone
 */
@Optional.InterfaceList({
  @Optional.Interface(iface = "cofh.redstoneflux.api.IEnergyContainerItem", modid = "redstoneflux"),
  @Optional.Interface(
      iface = "com.brandon3055.draconicevolution.api.IInvCharge",
      modid = BrainStoneModules.DRACONIC_EVOLUTION_MODID)
})
public interface IEnergyContainerItem
    extends cofh.redstoneflux.api.IEnergyContainerItem, IInvCharge {
  long getMaxEnergyStoredLong(ItemStack stack);

  @Override
  default int getMaxEnergyStored(ItemStack container) {
    long maxEnergyStored = getMaxEnergyStoredLong(container);

    if (maxEnergyStored > Integer.MAX_VALUE) return Integer.MAX_VALUE;
    else return (int) maxEnergyStored;
  }

  long getEnergyStoredLong(ItemStack stack);

  @Override
  default int getEnergyStored(ItemStack container) {
    long energyStored = getEnergyStoredLong(container);
    long maxEnergyStored = getMaxEnergyStoredLong(container);

    if (maxEnergyStored > Integer.MAX_VALUE)
      return BigInteger.valueOf(energyStored)
          .multiply(BigInteger.valueOf(Integer.MAX_VALUE))
          .divide(BigInteger.valueOf(maxEnergyStored))
          .intValue();
    else return (int) energyStored;
  }

  void setEnergyStored(ItemStack container, long energy);

  long getMaxInput(ItemStack stack);

  long getMaxOutput(ItemStack stack);

  default boolean canExtract(ItemStack container) {
    if (container.getCount() > 1) return false;

    return getMaxOutput(container) > 0;
  }

  default boolean canReceive(ItemStack container) {
    if (container.getCount() > 1) return false;

    return getMaxInput(container) > 0;
  }

  @Override
  default boolean canCharge(ItemStack container, EntityPlayer player) {
    return canReceive(container);
  }

  default long receiveEnergyLong(ItemStack container, long maxReceive, boolean simulate) {
    if (!canReceive(container)) return 0;

    long energy = getEnergyStoredLong(container);
    long energyReceived =
        Math.min(
            getMaxEnergyStoredLong(container) - energy,
            Math.min(getMaxInput(container), maxReceive));

    if (!simulate) {
      energy += energyReceived;
      setEnergyStored(container, energy);
    }

    return energyReceived;
  }

  @Override
  default int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {
    return (int) receiveEnergyLong(container, maxReceive, simulate);
  }

  default long extractEnergyLong(ItemStack container, long maxExtract, boolean simulate) {
    if (!canExtract(container)) return 0;

    long energy = getEnergyStoredLong(container);
    long energyExtracted = Math.min(energy, Math.min(getMaxOutput(container), maxExtract));

    if (!simulate) {
      energy -= energyExtracted;
      setEnergyStored(container, energy);
    }

    return energyExtracted;
  }

  @Override
  default int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {
    return (int) extractEnergyLong(container, maxExtract, simulate);
  }
}
