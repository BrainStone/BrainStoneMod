package brainstonemod.common.helper;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import sun.net.www.protocol.file.FileURLConnection;

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
	public static ArrayList<Class<?>> getClassesForPackage(String pckgname)
			throws ClassNotFoundException {
		final ArrayList<Class<?>> classes = new ArrayList<Class<?>>();

		try {
			final ClassLoader cld = Thread.currentThread()
					.getContextClassLoader();

			if (cld == null)
				throw new ClassNotFoundException("Can't get class loader.");

			final String path = pckgname.replace('.', '/');
			final Enumeration<URL> resources = cld.getResources(path);
			URLConnection connection;

			for (URL url = null; resources.hasMoreElements()
					&& ((url = resources.nextElement()) != null);) {
				try {
					connection = url.openConnection();

					if (connection instanceof JarURLConnection) {
						checkJarFile((JarURLConnection) connection, pckgname,
								classes);
					} else if (connection instanceof FileURLConnection) {
						try {
							checkDirectory(
									new File(URLDecoder.decode(url.getPath(),
											"UTF-8")), pckgname, classes);
						} catch (UnsupportedEncodingException ex) {
							throw new ClassNotFoundException(
									pckgname
											+ " does not appear to be a valid package (Unsupported encoding)",
									ex);
						}
					} else
						throw new ClassNotFoundException(pckgname + " ("
								+ url.getPath()
								+ ") does not appear to be a valid package");
				} catch (IOException ioex) {
					throw new ClassNotFoundException(
							"IOException was thrown when trying to get all resources for "
									+ pckgname, ioex);
				}
			}
		} catch (final NullPointerException ex) {
			throw new ClassNotFoundException(
					pckgname
							+ " does not appear to be a valid package (Null pointer exception)",
					ex);
		} catch (final IOException ioex) {
			throw new ClassNotFoundException(
					"IOException was thrown when trying to get all resources for "
							+ pckgname, ioex);
		}

		return classes;
	}

	private static void checkDirectory(File directory, String pckgname,
			ArrayList<Class<?>> classes) throws ClassNotFoundException {
		File tmpDirectory;

		if (directory.exists() && directory.isDirectory()) {
			String[] files = directory.list();

			for (final String file : files) {
				if (file.endsWith(".class")) {
					try {
						classes.add(Class.forName(pckgname + '.'
								+ file.substring(0, file.length() - 6)));
					} catch (final NoClassDefFoundError e) {
						// do nothing. this class hasn't been found by the
						// loader, and we don't care.
					}
				} else if ((tmpDirectory = new File(directory, file))
						.isDirectory()) {
					checkDirectory(tmpDirectory, pckgname + "." + file, classes);
				}
			}
		}
	}

	private static void checkJarFile(JarURLConnection connection,
			String pckgname, ArrayList<Class<?>> classes)
			throws ClassNotFoundException, IOException {
		JarFile jarFile = connection.getJarFile();
		Enumeration<JarEntry> entries = jarFile.entries();
		String name;

		for (JarEntry jarEntry = null; entries.hasMoreElements()
				&& ((jarEntry = entries.nextElement()) != null);) {
			name = jarEntry.getName();

			if (name.contains(".class")) {
				name = name.substring(0, name.length() - 6).replace('/', '.');

				if (name.contains(pckgname))
					classes.add(Class.forName(name));
			}
		}
	}
}