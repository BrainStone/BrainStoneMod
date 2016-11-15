package brainstonemod.common.capabilities;

import java.math.BigInteger;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

/**
 * This code is mostly taken from <a href=
 * "https://github.com/SleepyTrousers/EnderIO/blob/1.10/src/main/java/crazypants/enderio/power/forge/InternalPoweredItemWrapper.java">
 * https://github.com/SleepyTrousers/EnderIO/blob/1.10/src/main/java/crazypants/
 * enderio/power/forge/InternalPoweredItemWrapper.java</a>. All credit to the
 * original author
 * 
 * @author BrainStone
 */
public class EnergyContainerItemWrapper implements IEnergyStorage {
	protected final ItemStack container;
	protected IEnergyContainerItem item;

	public static class EnergyContainerItemProvider implements ICapabilityProvider {
		private final IEnergyContainerItem item;
		private final ItemStack stack;

		public EnergyContainerItemProvider(IEnergyContainerItem item, ItemStack stack) {
			this.item = item;
			this.stack = stack;
		}

		@Override
		public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
			return capability == CapabilityEnergy.ENERGY;
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
			if (capability == CapabilityEnergy.ENERGY)
				return (T) new EnergyContainerItemWrapper(item, stack);

			return null;
		}
	}

	public EnergyContainerItemWrapper(IEnergyContainerItem item, ItemStack container) {
		this.container = container;
		this.item = item;
	}

	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		if (!canReceive())
			return 0;

		long energy = item.getEnergyStored(container);
		int energyReceived = (int) Math.min(item.getMaxEnergyStored(container) - energy,
				Math.min(item.getMaxInput(container), maxReceive));

		if (!simulate) {
			energy += energyReceived;
			setEnergyStored(energy);
		}

		return energyReceived;
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		if (!canExtract())
			return 0;

		long energy = item.getEnergyStored(container);
		int energyExtracted = (int) Math.min(energy, Math.min(item.getMaxOutput(container), maxExtract));

		if (!simulate) {
			energy -= energyExtracted;
			setEnergyStored(energy);
		}

		return energyExtracted;
	}

	public void setEnergyStored(long energy) {
		item.setEnergyStored(container, energy);
	}

	@Override
	public int getEnergyStored() {
		long energyStored = item.getEnergyStored(container);
		long maxEnergyStored = item.getMaxEnergyStored(container);

		if (maxEnergyStored > Integer.MAX_VALUE)
			return BigInteger.valueOf(energyStored).multiply(BigInteger.valueOf(Integer.MAX_VALUE))
					.divide(BigInteger.valueOf(maxEnergyStored)).intValue();
		else
			return (int) energyStored;
	}

	@Override
	public int getMaxEnergyStored() {
		long maxEnergyStored = item.getMaxEnergyStored(container);

		if (maxEnergyStored > Integer.MAX_VALUE)
			return Integer.MAX_VALUE;
		else
			return (int) maxEnergyStored;
	}

	@Override
	public boolean canExtract() {
		if (container.stackSize > 1)
			return false;

		return item.getMaxOutput(container) > 0;
	}

	@Override
	public boolean canReceive() {
		if (container.stackSize > 1)
			return false;

		return item.getMaxInput(container) > 0;
	}
}
