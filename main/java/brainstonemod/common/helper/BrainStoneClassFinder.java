package brainstonemod.common.helper;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class BrainStoneClassFinder {
	/**
	 * Attempts to list all the classes in the specified package as determined
	 * by the context class loader
	 * 
	 * @param pckgname
	 *            the package name to search
	 * @return a list of classes that exist within that package
	 * @throws ClassNotFoundException
	 *             if something went wrong
	 */
	public static List<Class> getClassesForPackage(String pckgname)
			throws ClassNotFoundException {
		// This will hold a list of directories matching the pckgname. There may
		// be more than one if a package is split over multiple jars/paths
		final ArrayList<File> directories = new ArrayList<File>();
		try {
			final ClassLoader cld = Thread.currentThread()
					.getContextClassLoader();
			if (cld == null)
				throw new ClassNotFoundException("Can't get class loader.");
			final String path = pckgname.replace('.', '/');
			// Ask for all resources for the path
			final Enumeration<URL> resources = cld.getResources(path);
			while (resources.hasMoreElements()) {
				directories.add(new File(URLDecoder.decode(resources
						.nextElement().getPath(), "UTF-8")));
			}
		} catch (final NullPointerException x) {
			throw new ClassNotFoundException(
					pckgname
							+ " does not appear to be a valid package (Null pointer exception)");
		} catch (final UnsupportedEncodingException encex) {
			throw new ClassNotFoundException(
					pckgname
							+ " does not appear to be a valid package (Unsupported encoding)");
		} catch (final IOException ioex) {
			throw new ClassNotFoundException(
					"IOException was thrown when trying to get all resources for "
							+ pckgname);
		}

		final ArrayList<Class> classes = new ArrayList<Class>();
		// For every directory identified capture all the .class files
		for (final File directory : directories) {
			if (directory.exists()) {
				// Get the list of the files contained in the package
				final String[] files = directory.list();
				for (final String file : files) {
					// we are only interested in .class files
					if (file.endsWith(".class")) {
						// removes the .class extension
						try {
							classes.add(Class.forName(pckgname + '.'
									+ file.substring(0, file.length() - 6)));
						} catch (final NoClassDefFoundError e) {
							// do nothing. this class hasn't been found by the
							// loader, and we don't care.
						}
					}
				}
			} else
				throw new ClassNotFoundException(pckgname + " ("
						+ directory.getPath()
						+ ") does not appear to be a valid package");
		}
		return classes;
	}
}