package brainstonemod.common.capability;

import brainstonemod.common.compat.BrainStoneModules;
import brainstonemod.common.compat.tesla.TeslaCompat;
import javax.annotation.Nullable;
import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.api.ITeslaProducer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.Optional;

/**
 * This code is mostly taken from <a href=
 * "https://github.com/SleepyTrousers/EnderIO/blob/1.10/src/main/java/crazypants/enderio/power/forge/InternalPoweredItemWrapper.java">
 * https://github.com/SleepyTrousers/EnderIO/blob/1.10/src/main/java/crazypants/
 * enderio/power/forge/InternalPoweredItemWrapper.java</a>. All credit to the original author
 *
 * @author BrainStone
 */
@Optional.InterfaceList({
  @Optional.Interface(
      iface = "net.darkhax.tesla.api.ITeslaConsumer",
      modid = BrainStoneModules.TESLA_MODID),
  @Optional.Interface(
      iface = "net.darkhax.tesla.api.ITeslaHolder",
      modid = BrainStoneModules.TESLA_MODID),
  @Optional.Interface(
      iface = "net.darkhax.tesla.api.ITeslaProducer",
      modid = BrainStoneModules.TESLA_MODID)
})
public class EnergyContainerItemProvider
    implements IEnergyStorage, ITeslaHolder, ITeslaConsumer, ITeslaProducer, ICapabilityProvider {
  protected final ItemStack container;
  protected final IAllEnergyContainerItem item;

  public EnergyContainerItemProvider(IAllEnergyContainerItem item, ItemStack container) {
    this.container = container;
    this.item = item;
  }

  @Override
  public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
    return (capability == CapabilityEnergy.ENERGY)
        || (BrainStoneModules.tesla() && TeslaCompat.isTeslaCapability(capability));
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
    if (hasCapability(capability, facing)) return (T) this;

    return null;
  }

  @Override
  public long givePower(long maxReceive, boolean simulate) {
    return item.receiveEnergyLong(container, maxReceive, simulate);
  }

  @Override
  public int receiveEnergy(int maxReceive, boolean simulate) {
    return item.receiveEnergy(container, maxReceive, simulate);
  }

  @Override
  public long takePower(long maxExtract, boolean simulate) {
    return item.extractEnergyLong(container, maxExtract, simulate);
  }

  @Override
  public int extractEnergy(int maxExtract, boolean simulate) {
    return item.extractEnergy(container, maxExtract, simulate);
  }

  public void setEnergyStored(long energy) {
    item.setEnergyStored(container, energy);
  }

  @Override
  public long getStoredPower() {
    return item.getEnergyStoredLong(container);
  }

  @Override
  public int getEnergyStored() {
    return item.getEnergyStored(container);
  }

  @Override
  public long getCapacity() {
    return item.getMaxEnergyStoredLong(container);
  }

  @Override
  public int getMaxEnergyStored() {
    return item.getMaxEnergyStored(container);
  }

  @Override
  public boolean canExtract() {
    return item.canExtract(container);
  }

  @Override
  public boolean canReceive() {
    return item.canReceive(container);
  }
}
