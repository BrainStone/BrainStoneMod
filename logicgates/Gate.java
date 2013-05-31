package mods.brainstone.logicgates;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import mods.brainstone.templates.BSP;
import net.minecraft.nbt.NBTTagCompound;

public abstract class Gate {
	public static final HashMap<String, Gate> Gates = getGates();
	public static final int NumberGates = Gates.size();
	public static final String[] GateNames = Gates.keySet().toArray(
			new String[NumberGates]);

	/**
	 * Recursive method used to find all classes in a given directory and
	 * subdirs.
	 * 
	 * @param directory
	 *            The base directory
	 * @param packageName
	 *            The package name for classes found inside the base directory
	 * @return The classes
	 * @throws ClassNotFoundException
	 */
	private static ArrayList<Class> findClasses(File directory,
			String packageName) throws ClassNotFoundException {
		final ArrayList<Class> classes = new ArrayList<Class>();
		if (!directory.exists())
			return classes;
		final File[] files = directory.listFiles();
		for (final File file : files) {
			if (file.isDirectory()) {
				assert !file.getName().contains(".");
				classes.addAll(findClasses(file,
						packageName + "." + file.getName()));
			} else if (file.getName().endsWith(".class")) {
				classes.add(Class.forName(packageName
						+ '.'
						+ file.getName().substring(0,
								file.getName().length() - 6)));
			}
		}
		return classes;
	}

	/**
	 * Scans all classes accessible from the context class loader which belong
	 * to the given package and subpackages.
	 * 
	 * @param packageName
	 *            The base package
	 * @return The classes
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	private static Class[] getClasses(String packageName)
			throws ClassNotFoundException, IOException {
		final ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();

		assert classLoader != null;

		final String path = packageName.replace('.', '/');
		final Enumeration<URL> resources = classLoader.getResources(path);
		final List<File> dirs = new ArrayList<File>();

		while (resources.hasMoreElements()) {
			final URL resource = resources.nextElement();
			dirs.add(new File(resource.getFile()));
		}

		final ArrayList<Class> classes = new ArrayList<Class>();

		for (final File directory : dirs) {
			classes.addAll(findClasses(directory, packageName));
		}

		return classes.toArray(new Class[classes.size()]);
	}

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
				BSP.severeException(
						e,
						"This is fatal! You must report this!\nThanks!\n\nDeveloper Information:\nCannot clone Gate: \""
								+ Gates.get(Name).getClass().getName()
								+ "\" ID: \"" + Name + "\"");

				return null;
			}
		}

		BSP.severe("The name: \"" + Name + "\" was not recongnized!");

		return null;
	}

	/**
	 * This function checks for all gates in mods.brainstone.logicgates.gates.<br>
	 * 
	 * @return all gates in mods.brainstone.logicgates.gates mapped to their ID
	 *         and checks if it already exists. <small>(Should not
	 *         happen!)</small>
	 */
	private static HashMap<String, Gate> getGates() {
		final HashMap<String, Gate> Gates = new HashMap<String, Gate>();

		try {
			Gate tmp;

			for (final Class gate : getClasses("mods.brainstone.logicgates.gates")) {
				try {
					if (Gates
							.containsKey((tmp = (Gate) gate.newInstance()).Name)) {
						BSP.throwIllegalArgumentException("Well, that should NOT have happenend! This IS a HUGE problem if you notice this please report it to yannick@tedworld.de.\nThanks!\n\nDeveloper Information:\nThere is a NOT unique ID for the gates: "
								+ tmp.Name);
					}

					Gates.put(tmp.Name, tmp);
				} catch (final InstantiationException e) {
					BSP.severeException(
							e,
							"Well, that should NOT have happenend! But it is not a big problem. Just report it to yannick@tedworld.de.\nThanks!");
				} catch (final IllegalAccessException e) {
					BSP.severeException(
							e,
							"Well, that should NOT have happenend! But it is not a big problem. Just report it to yannick@tedworld.de.\nThanks!");
				}
			}
		} catch (final IOException e) {
			BSP.severeException(
					e,
					"Well, that should NOT have happenend! This IS a HUGE problem if you notice this please report it to yannick@tedworld.de.\nThanks!");

			return null;
		} catch (final ClassNotFoundException e) {
			BSP.severeException(
					e,
					"Well, that should NOT have happenend! This IS a HUGE problem if you notice this please report it to yannick@tedworld.de.\nThanks!");

			return null;
		}

		return Gates;
	}

	public final static Gate readFromInputStream(DataInputStream inputStream)
			throws IOException {
		final Gate out = Gate.getGate(inputStream.readUTF());
		out.tickRate = inputStream.readInt();

		for (int i = 0; i < 6; i++) {
			out.Pins[i] = Pin.readFromInputStream(inputStream);
		}

		// Options go here!

		return out;
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
			BSP.severeException(e);
			BSP.throwCloneNotSupportedException("An InstantiationException occured!");
		} catch (final IllegalAccessException e) {
			BSP.severeException(e);
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
		final Pin tmp = this.getPin(gateName);

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
	 * This function is called when the gate changed or the block is placed.<br>
	 * <br>
	 * 
	 * <b>Directions</b><br>
	 * <br>
	 * 
	 * <table>
	 * <tr>
	 * <td>0:</td>
	 * <td>North</td>
	 * </tr>
	 * <tr>
	 * <td>1:</td>
	 * <td>East</td>
	 * </tr>
	 * <tr>
	 * <td>2:</td>
	 * <td>South</td>
	 * </tr>
	 * <tr>
	 * <td>3:</td>
	 * <td>West</td>
	 * </tr>
	 * </table>
	 * 
	 * @param direction
	 *            The direction the player is looking
	 */
	public abstract void onGateChange(int direction);

	public abstract void onOptionsChange();

	public abstract void onTick();

	public final void setPin(char gateName, Pin pin) {
		for (int i = 0; i < 6; i++) {
			if (Pins[i].Name == gateName) {
				Pins[i] = pin;
			}
		}
	}

	public final void setPinState(char gateName, PinState state) {
		for (int i = 0; i < 6; i++) {
			if (Pins[i].Name == gateName) {
				Pins[i].State = state;
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

			nbttagcompound.setCompoundTag("Pin" + i, currentPin);
		}

		// Options go here!
	}

	public final void writeToOutputStream(DataOutputStream outputStream)
			throws IOException {
		outputStream.writeUTF(Name);
		outputStream.writeInt(tickRate);

		for (int i = 0; i < 6; i++) {
			Pins[i].writeToOutputStream(outputStream);
		}

		// Options go here!
	}
}
