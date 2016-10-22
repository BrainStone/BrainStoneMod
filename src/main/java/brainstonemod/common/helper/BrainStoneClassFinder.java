package brainstonemod.common.helper;

import sun.net.www.protocol.file.FileURLConnection;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class BrainStoneClassFinder {
	/**
	 * Private helper method
	 * 
	 * @param directory
	 *            The directory to start with
	 * @param pckgname
	 *            The package name to search for. Will be needed for getting the
	 *            Class object.
	 * @param classes
	 *            if a file isn't loaded but still is in the directory
	 * @throws ClassNotFoundException
	 */
	private static void checkDirectory(File directory, String pckgname, ArrayList<Class<?>> classes)
			throws ClassNotFoundException {
		File tmpDirectory;

		if (directory.exists() && directory.isDirectory()) {
			final String[] files = directory.list();

			for (final String file : files) {
				if (file.endsWith(".class")) {
					try {
						classes.add(Class.forName(pckgname + '.' + file.substring(0, file.length() - 6)));
					} catch (final NoClassDefFoundError e) {
						// do nothing. this class hasn't been found by the
						// loader, and we don't care.
					}
				} else if ((tmpDirectory = new File(directory, file)).isDirectory()) {
					checkDirectory(tmpDirectory, pckgname + "." + file, classes);
				}
			}
		}
	}

	/**
	 * Private helper method.
	 * 
	 * @param connection
	 *            the connection to the jar
	 * @param pckgname
	 *            the package name to search for
	 * @param classes
	 *            the current ArrayList of all classes. This method will simply
	 *            add new classes.
	 * @throws ClassNotFoundException
	 *             if a file isn't loaded but still is in the jar file
	 * @throws IOException
	 *             if it can't correctly read from the jar file.
	 */
	private static void checkJarFile(JarURLConnection connection, String pckgname, ArrayList<Class<?>> classes)
			throws ClassNotFoundException, IOException {
		final JarFile jarFile = connection.getJarFile();
		final Enumeration<JarEntry> entries = jarFile.entries();
		String name;

		for (JarEntry jarEntry = null; entries.hasMoreElements() && ((jarEntry = entries.nextElement()) != null);) {
			name = jarEntry.getName();

			if (name.contains(".class")) {
				name = name.substring(0, name.length() - 6).replace('/', '.');

				if (name.contains(pckgname)) {
					classes.add(Class.forName(name));
				}
			}
		}
	}

	/**
	 * If the provided class has been loaded from a jar file that is on the
	 * local file system, will find the absolute path to that jar file.
	 * 
	 * @param context
	 *            The jar file that contained the class file that represents
	 *            this class will be found. Specify {@code null} to let
	 *            {@code LiveInjector} find its own jar.
	 * @throws IllegalStateException
	 *             If the specified class was loaded from a directory or in some
	 *             other way (such as via HTTP, from a database, or some other
	 *             custom classloading device).
	 */
	public static String findPathJar(Class<?> context) throws IllegalStateException {
		if (context == null) {
			context = BrainStoneClassFinder.class;
		}
		final String rawName = context.getName();
		String classFileName;
		/*
		 * rawName is something like package.name.ContainingClass$ClassName. We
		 * need to turn this into ContainingClass$ClassName.class.
		 */ {
			final int idx = rawName.lastIndexOf('.');
			classFileName = (idx == -1 ? rawName : rawName.substring(idx + 1)) + ".class";
		}

		final String uri = context.getResource(classFileName).toString();
		if (uri.startsWith("file:"))
			throw new IllegalStateException("This class has been loaded from a directory and not from a jar file.");
		if (!uri.startsWith("jar:file:")) {
			final int idx = uri.indexOf(':');
			final String protocol = idx == -1 ? "(unknown)" : uri.substring(0, idx);
			throw new IllegalStateException("This class has been loaded remotely via the " + protocol
					+ " protocol. Only loading from a jar on the local file system is supported.");
		}

		final int idx = uri.indexOf('!');
		// As far as I know, the if statement below can't ever trigger, so it's
		// more of a sanity check thing.
		if (idx == -1)
			throw new IllegalStateException(
					"You appear to have loaded this class from a local jar file, but I can't make sense of the URL!");

		try {
			final String fileName = URLDecoder.decode(uri.substring("jar:file:".length(), idx),
					Charset.defaultCharset().name());
			return new File(fileName).getAbsolutePath();
		} catch (final UnsupportedEncodingException e) {
			throw new InternalError("default charset doesn't exist. Your VM is borked.");
		}
	}

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
	public static ArrayList<Class<?>> getClassesForPackage(String pckgname) throws ClassNotFoundException {
		final ArrayList<Class<?>> classes = new ArrayList<>();

		try {
			final ClassLoader cld = Thread.currentThread().getContextClassLoader();

			if (cld == null)
				throw new ClassNotFoundException("Can't get class loader.");

			final Enumeration<URL> resources = cld.getResources(pckgname.replace('.', '/'));
			URLConnection connection;

			for (URL url = null; resources.hasMoreElements() && ((url = resources.nextElement()) != null);) {
				try {
					connection = url.openConnection();

					if (connection instanceof JarURLConnection) {
						checkJarFile((JarURLConnection) connection, pckgname, classes);
					} else if (connection instanceof FileURLConnection) {
						try {
							checkDirectory(new File(URLDecoder.decode(url.getPath(), "UTF-8")), pckgname, classes);
						} catch (final UnsupportedEncodingException ex) {
							throw new ClassNotFoundException(
									pckgname + " does not appear to be a valid package (Unsupported encoding)", ex);
						}
					} else
						throw new ClassNotFoundException(
								pckgname + " (" + url.getPath() + ") does not appear to be a valid package");
				} catch (final IOException ioex) {
					throw new ClassNotFoundException(
							"IOException was thrown when trying to get all resources for " + pckgname, ioex);
				}
			}
		} catch (final NullPointerException ex) {
			throw new ClassNotFoundException(
					pckgname + " does not appear to be a valid package (Null pointer exception)", ex);
		} catch (final IOException ioex) {
			throw new ClassNotFoundException("IOException was thrown when trying to get all resources for " + pckgname,
					ioex);
		}

		return classes;
	}
}