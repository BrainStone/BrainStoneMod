package mods.brainstone.logicgates;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import mods.brainstone.templates.BSP;

public abstract class Gate {
	public static final HashMap<Long, Gate> Gates = getGates();

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
	 * This function checks for all gates in mods.brainstone.logicgates.gates.
	 * 
	 * @return all gates in mods.brainstone.logicgates.gates mapped to their ID
	 *         and checks if it already exists. <small>(Should not
	 *         happen!)</small>
	 */
	final public static HashMap<Long, Gate> getGates() {
		final HashMap<Long, Gate> Gates = new HashMap<Long, Gate>();

		try {
			Gate tmp;

			for (final Class gate : getClasses("mods.brainstone.logicgates.gates")) {
				try {
					Gates.put((tmp = (Gate) gate.newInstance()).ID, tmp);

					if (Gates.containsKey(tmp.ID)) {
						BSP.throwIllegalArgumentException("Well, that should NOT have happenend! This IS a HUGE problem if you notice this please report it to yannick@tedworld.de.\nThanks!\n\nDeveloper Information:\nThere is a NOT unique ID for the gates: "
								+ tmp.ID);
					}
				} catch (final InstantiationException e) {
					BSP.force_printException(
							e,
							"Well, that should NOT have happenend! But it is not a big problem. Just report it to yannick@tedworld.de.\nThanks!");
				} catch (final IllegalAccessException e) {
					BSP.force_printException(
							e,
							"Well, that should NOT have happenend! But it is not a big problem. Just report it to yannick@tedworld.de.\nThanks!");
				}
			}
		} catch (final IOException e) {
			BSP.force_printException(
					e,
					"Well, that should NOT have happenend! This IS a HUGE problem if you notice this please report it to yannick@tedworld.de.\nThanks!");

			return null;
		} catch (final ClassNotFoundException e) {
			BSP.force_printException(
					e,
					"Well, that should NOT have happenend! This IS a HUGE problem if you notice this please report it to yannick@tedworld.de.\nThanks!");

			return null;
		}

		return Gates;
	}

	/**
	 * Statically generates a (usually) unique ID from a String.<br>
	 * <small>The chance that it is NOT unique is about
	 * 1:18,446,744,073,709,551,616 since there are so many
	 * possibilities!</small><br>
	 * <b>This function cannot be overwritten!</b>
	 * 
	 * @param Name
	 *            The class name that will be turned into a unique long
	 * @return A unique long
	 */
	final public static long getID(String Name) {
		MessageDigest md5;
		byte[] result;
		try {
			md5 = MessageDigest.getInstance("MD5");
			md5.reset();
			md5.update(Name.getBytes());
			result = md5.digest();
		} catch (final NoSuchAlgorithmException e) {
			e.printStackTrace();
			return 0;
		}

		long output = 0;
		int i = 0;

		for (final byte tmp : result) {
			output ^= ((long) tmp) << ((i % 8) * 8);
			i++;
		}

		return output;
	}

	public Pin[] Pins = new Pin[6];
	public Option[] Options;
	public final long ID = getID(this.getClass().getSimpleName());

	public final String Name = this.getClass().getSimpleName();

	protected int tickRate;

	public boolean canSwapWith(Pin pin1, Pin pin2) {
		return false;
	}

	/**
	 * This function return the tickRate of the Gate.<br>
	 * <b>This function cannot be overwritten!</b>
	 * 
	 * @return tickRate
	 */
	final public int getTickRate() {
		return tickRate;
	}

	public abstract void onOptionsChange();

	public abstract void onTick();
}
