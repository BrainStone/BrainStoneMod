package brainstonemod.common.logicgate;

import java.util.HashMap;

import net.minecraft.nbt.NBTTagCompound;
import brainstonemod.common.helper.BSP;
import brainstonemod.common.helper.BrainStoneClassFinder;
import brainstonemod.common.helper.BrainStoneDirection;

public abstract class Gate {
	public static final HashMap<String, Gate> Gates = getGates();
	public static final int NumberGates = Gates.size();
	public static final String[] GateNames = Gates.keySet().toArray(
			new String[NumberGates]);

	/**
	 * This returns a new instance of the gate<br>
	 * <b>This function cannot be overwritten!</b>
	 * 
	 * @param Name
	 *            The Name to get the Gate from
	 * @return A new instance of the gate
	 */
	final public static Gate getGate(String Name) {
		if (Gates.containsKey(Name)) {
			try {
				return Gates.get(Name).clone();
			} catch (final CloneNotSupportedException e) {
				BSP.fatalException(
						e,
						"This is fatal! You must report this!\nThanks!\n\nDeveloper Information:\nCannot clone Gate: \""
								+ Gates.get(Name).getClass().getName()
								+ "\" ID: \"" + Name + "\"");

				return null;
			}
		}

		BSP.error("The name: \"" + Name + "\" was not recongnized!");

		return null;
	}

	/**
	 * This function checks for all gates in brainstonemod.logicgates.gates.<br>
	 * 
	 * @return all gates in brainstonemod.logicgates.gates mapped to their ID
	 *         and checks if it already exists. <small>(Should not
	 *         happen!)</small>
	 */
	private static HashMap<String, Gate> getGates() {
		final HashMap<String, Gate> Gates = new HashMap<String, Gate>();

		try {
			Gate tmpGate;
			Object tmp;

			for (final Class<?> gate : BrainStoneClassFinder
					.getClassesForPackage(Gate.class.getPackage().getName()
							+ ".gate")) {
				try {
					tmp = gate.newInstance();

					if (tmp instanceof Gate) {
						if (Gates.containsKey((tmpGate = (Gate) tmp).Name)) {
							BSP.throwIllegalArgumentException("Well, that should NOT have happenend! This IS a HUGE problem if you notice this please report it to yannick@tedworld.de.\nThanks!\n\nDeveloper Information:\nThere is a NOT unique ID for the gates: "
									+ tmpGate.Name);
						}

						Gates.put(tmpGate.Name, tmpGate);
					}
				} catch (final InstantiationException e) {
					BSP.errorException(
							e,
							"Well, that should NOT have happenend! But it is not a big problem. Just report it to yannick@tedworld.de.\nThanks!");
				} catch (final IllegalAccessException e) {
					BSP.errorException(
							e,
							"Well, that should NOT have happenend! But it is not a big problem. Just report it to yannick@tedworld.de.\nThanks!");
				}
			}
		} catch (final ClassNotFoundException e) {
			BSP.errorException(
					e,
					"Well, that should NOT have happenend! This IS a HUGE problem if you notice this please report it to yannick@tedworld.de.\nThanks!");

			return null;
		}

		return Gates;
	}

	public final static Gate readFromNBT(NBTTagCompound nbttagcompound) {
		final Gate out = Gate.getGate(nbttagcompound.getString("Name"));
		out.tickRate = nbttagcompound.getInteger("tickRate");

		NBTTagCompound currentPin;

		for (int i = 0; i < 6; i++) {
			currentPin = nbttagcompound.getCompoundTag("Pin" + i);
			out.Pins[i] = Pin.readFromNBT(currentPin);
		}

		// Options go here!

		return out;
	}

	/**
	 * <b>Indices</b><br>
	 * <br>
	 * 
	 * <table>
	 * <tr>
	 * <td>0:</td>
	 * <td>Up</td>
	 * </tr>
	 * <tr>
	 * <td>1:</td>
	 * <td>Down</td>
	 * </tr>
	 * <tr>
	 * <td>2:</td>
	 * <td>North</td>
	 * </tr>
	 * <tr>
	 * <td>3:</td>
	 * <td>East</td>
	 * </tr>
	 * <tr>
	 * <td>4:</td>
	 * <td>South</td>
	 * </tr>
	 * <tr>
	 * <td>5:</td>
	 * <td>West</td>
	 * </tr>
	 * </table>
	 */
	public Pin[] Pins = new Pin[6];
	public Option[] Options;
	public final String Name = this.getClass().getSimpleName();

	protected int tickRate = 1;

	public boolean canSwapWith(Pin pin1, Pin pin2) {
		return pin1.Movable && pin2.Movable;
	}

	@Override
	final public Gate clone() throws CloneNotSupportedException {
		try {
			return this.getClass().newInstance();
		} catch (final InstantiationException e) {
			BSP.errorException(e);
			BSP.throwCloneNotSupportedException("An InstantiationException occured!");
		} catch (final IllegalAccessException e) {
			BSP.errorException(e);
			BSP.throwCloneNotSupportedException("An IllegalAccessException occured!");
		}

		return null;
	}

	public final Pin getPin(char gateName) {
		for (int i = 0; i < 6; i++) {
			if (Pins[i].Name == gateName)
				return Pins[i];
		}

		return null;
	}

	public final PinState getPinState(char gateName) {
		final Pin tmp = getPin(gateName);

		if (tmp == null)
			return PinState.NotExisting;

		return tmp.State;
	}

	/**
	 * This function returns the tickRate of the Gate in Redstone ticks (2 game
	 * ticks!).<br>
	 * <b>This function cannot be overwritten!</b>
	 * 
	 * @return tickRate
	 */
	final public int getTickRate() {
		return tickRate;
	}

	/**
	 * This function is called when the gate changed or the block is placed.
	 * 
	 * @param direction
	 *            The direction the player is looking
	 */
	public abstract void onGateChange(BrainStoneDirection direction);

	public abstract void onOptionsChange();

	public abstract void onTick();

	public final void setPin(char pinName, Pin pin) {
		for (int i = 0; i < 6; i++) {
			if (Pins[i].Name == pinName) {
				Pins[i] = pin;
			}
		}
	}

	public final void setPinState(char gateName, boolean on) {
		this.setPinState(gateName, (on) ? PinState.Powered : PinState.Unpowered);
	}

	public final void setPinState(char gateName, byte powerLevel) {
		this.setPinState(gateName, PinState.getPinState(powerLevel));
	}

	public final void setPinState(char gateName, int powerLevel) {
		this.setPinState(gateName, (byte) powerLevel);
	}

	public final void setPinState(char gateName, PinState state) {
		for (int i = 0; i < 6; i++) {
			if (Pins[i].Name == gateName) {
				Pins[i].State = state;

				break;
			}
		}
	}

	public final void writeToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setString("Name", Name);
		nbttagcompound.setInteger("tickRate", tickRate);

		NBTTagCompound currentPin;

		for (int i = 0; i < 6; i++) {
			currentPin = new NBTTagCompound();
			Pins[i].writeToNBT(currentPin);

			nbttagcompound.setTag("Pin" + i, currentPin);
		}

		// Options go here!
	}
}