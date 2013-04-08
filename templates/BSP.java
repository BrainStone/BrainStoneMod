package mods.brainstone.templates;

import mods.brainstone.BrainStone;

/**
 * <center><b><u>B</u>rain<u>S</u>tone<u>P</u>rinter</b></center><br>
 * This class is the printer class for the BrainStoneMod. It contains a lot of
 * static final printing methods, that will print the given object under the
 * right conditions. Exceptions will be thrown here as well.
 * 
 * @author Yannick Schinko (alias The_BrainStone)
 * @version 1.0.0
 * @category Print
 */
public abstract class BSP {
	/**
	 * A error message addon that is very big, asks the user to send the error
	 * log to the mod developer, and contains the email address
	 */
	public static final String errorMessageAddon = "\n==================================================\n"
			+ "==================================================\n"
			+ "   There occured an error in the BrainStoneMod!   \n"
			+ "   Please send the error log to the developer.    \n"
			+ "   This is important if you want the error to be  \n"
			+ "  fixed. To send the log to me, just send a email \n"
			+ "         with the file of the error log to        \n"
			+ "                yannick@tedworld.de               \n\n"
			+ "              Thank you for your help!            \n"
			+ "==================================================\n"
			+ "==================================================\n";

	/** Is the debug mode on */
	public static final boolean debug = BrainStone.debug;

	/** Should anything be printed */
	public static final boolean notRelease = !BrainStone.release || debug;

	/**
	 * Prints the given object to stdout only if debug mode is on
	 * 
	 * @param obj
	 *            The object to be printed
	 */
	public static final boolean debugOnly_print(Object obj) {
		if (debug) {
			System.out.print(obj);
		}

		return debug;
	}

	/**
	 * Prints the given object to stderr only if debug mode is on
	 * 
	 * @param obj
	 *            The object to be printed
	 */
	public static final boolean debugOnly_print_error(Object obj) {
		if (debug) {
			System.err.print(obj);
		}

		return debug;
	}

	/**
	 * Prints the standard error message addon with the given exception to
	 * stderr only if debug mode is on
	 * 
	 * @param exception
	 *            the exception to be printed
	 */
	public static final boolean debugOnly_printException(Throwable exception) {
		return debugOnly_printException_noAddon(exception, errorMessageAddon);
	}

	/**
	 * Prints the standard error message addon and an additional message with
	 * the given exception to stderr only if debug mode is on
	 * 
	 * @param exception
	 *            the exception to be printed
	 * @param additionalMessage
	 *            the message to be printed after the standard error message
	 *            addon
	 */
	public static final boolean debugOnly_printException(Throwable exception,
			String additionalMessage) {
		return debugOnly_printException_noAddon(exception, errorMessageAddon
				+ "\n" + additionalMessage);
	}

	/**
	 * Prints the given exception to stderr only if debug mode is on
	 * 
	 * @param exception
	 *            the exception to be printed
	 */
	public static final boolean debugOnly_printException_noAddon(
			Throwable exception) {
		if (debug) {
			exception.printStackTrace();
		}

		return debug;
	}

	/**
	 * Prints the additional message and the given exception to stderr only if
	 * debug mode is on or the mod is not
	 * 
	 * @param exception
	 *            the exception to be printed
	 * @param additionalMessage
	 *            the message to be printed in front of the exception
	 */
	public static final boolean debugOnly_printException_noAddon(
			Throwable exception, String additionalMessage) {
		debugOnly_println_error(additionalMessage);

		return debugOnly_printException_noAddon(exception);
	}

	/**
	 * Prints the given object to stdout with a linewarp only if debug mode is
	 * on
	 * 
	 * @param obj
	 *            The object to be printed
	 */
	public static final boolean debugOnly_println(Object obj) {
		if (debug) {
			System.out.println(obj);
		}

		return debug;
	}

	/**
	 * Prints the given object to stderr with a linewarp only if debug mode is
	 * on
	 * 
	 * @param obj
	 *            The object to be printed
	 */
	public static final boolean debugOnly_println_error(Object obj) {
		if (debug) {
			System.err.println(obj);
		}

		return debug;
	}

	/**
	 * Prints the given object always to stdout
	 * 
	 * @param obj
	 *            The object to be printed
	 */
	public static final boolean force_print(Object obj) {
		System.out.print(obj);

		return true;
	}

	/**
	 * Prints the given object always to stderr
	 * 
	 * @param obj
	 *            The object to be printed
	 */
	public static final boolean force_print_error(Object obj) {
		System.err.print(obj);

		return true;
	}

	/**
	 * Prints the standard error message addon with the given exception always
	 * to stderr
	 * 
	 * @param exception
	 *            the exception to be printed
	 */
	public static final boolean force_printException(Throwable exception) {
		return force_printException_noAddon(exception, errorMessageAddon);
	}

	/**
	 * Prints the standard error message addon and an additional message with
	 * the given exception always to stderr
	 * 
	 * @param exception
	 *            the exception to be printed
	 * @param additionalMessage
	 *            the message to be printed after the standard error message
	 *            addon
	 */
	public static final boolean force_printException(Throwable exception,
			String additionalMessage) {
		return force_printException_noAddon(exception, errorMessageAddon + "\n"
				+ additionalMessage);
	}

	/**
	 * Prints the given exception always to stderr
	 * 
	 * @param exception
	 *            the exception to be printed
	 */
	public static final boolean force_printException_noAddon(Throwable exception) {
		exception.printStackTrace();

		return true;
	}

	/**
	 * Prints the additional message and the given exception always to stderr
	 * 
	 * @param exception
	 *            the exception to be printed
	 * @param additionalMessage
	 *            the message to be printed in front of the exception
	 */
	public static final boolean force_printException_noAddon(
			Throwable exception, String additionalMessage) {
		force_println_error(additionalMessage);

		return force_printException_noAddon(exception);
	}

	/**
	 * PPrints the given object always to stdout with a linewarp
	 * 
	 * @param obj
	 *            The object to be printed
	 */
	public static final boolean force_println(Object obj) {
		System.out.println(obj);

		return true;
	}

	/**
	 * Prints the given object always to stderr with a linewarp
	 * 
	 * @param obj
	 *            The object to be printed
	 */
	public static final boolean force_println_error(Object obj) {
		System.err.println(obj);

		return true;
	}

	/**
	 * Prints the given object to stdout if debug mode is on or the mod is not
	 * released
	 * 
	 * @param obj
	 *            The object to be printed
	 */
	public static final boolean print(Object obj) {
		if (notRelease) {
			System.out.print(obj);
		}

		return notRelease;
	}

	public static final boolean print(Object... obj) {
		final int length = obj.length;

		for (int i = 0; i < length; i++) {
			print(obj[i]);
		}

		return notRelease;
	}

	/**
	 * Prints the given object to stderr if debug mode is on or the mod is not
	 * released
	 * 
	 * @param obj
	 *            The object to be printed
	 */
	public static final boolean print_error(Object obj) {
		if (notRelease) {
			System.err.print(obj);
		}

		return notRelease;
	}

	/**
	 * Prints the standard error message addon with the given exception to
	 * stderr if debug mode is on or the mod is not released
	 * 
	 * @param exception
	 *            the exception to be printed
	 */
	public static final boolean printException(Throwable exception) {
		return printException_noAddon(exception, errorMessageAddon);
	}

	/**
	 * Prints the standard error message addon and an additional message with
	 * the given exception to stderr if debug mode is on or the mod is not
	 * released
	 * 
	 * @param exception
	 *            the exception to be printed
	 * @param additionalMessage
	 *            the message to be printed after the standard error message
	 *            addon
	 */
	public static final boolean printException(Throwable exception,
			String additionalMessage) {
		return printException_noAddon(exception, errorMessageAddon + "\n"
				+ additionalMessage);
	}

	/**
	 * Prints the given exception to stderr if debug mode is on or the mod is
	 * not released
	 * 
	 * @param exception
	 *            the exception to be printed
	 */
	public static final boolean printException_noAddon(Throwable exception) {
		if (notRelease) {
			exception.printStackTrace();
		}

		return notRelease;
	}

	/**
	 * Prints the additional message and the given exception to stderr if debug
	 * mode is on or the mod is not
	 * 
	 * @param exception
	 *            the exception to be printed
	 * @param additionalMessage
	 *            the message to be printed in front of the exception
	 */
	public static final boolean printException_noAddon(Throwable exception,
			String additionalMessage) {
		println_error(additionalMessage);

		return printException_noAddon(exception);
	}

	/**
	 * Prints the given object to stdout with a linewarp if debug mode is on or
	 * the mod is not released
	 * 
	 * @param obj
	 *            The object to be printed
	 */
	public static final boolean println(Object obj) {
		if (notRelease) {
			System.out.println(obj);
		}

		return notRelease;
	}

	public static final boolean println(Object... obj) {
		final int length = obj.length;

		for (int i = 0; i < length; i++) {
			println(obj[i]);
		}

		return notRelease;
	}

	/**
	 * Prints the given object to stderr with a linewarp if debug mode is on or
	 * the mod is not released
	 * 
	 * @param obj
	 *            The object to be printed
	 */
	public static final boolean println_error(Object obj) {
		if (notRelease) {
			System.err.println(obj);
		}

		return notRelease;
	}

	/**
	 * Throws a ArithmeticException with the error message addon
	 */
	public static final void throwArithmeticException() {
		throwArithmeticException("");
	}

	/**
	 * Throws a ArithmeticException with the error message addon
	 * 
	 * @param additionalMessage
	 *            a additional message added after the addon
	 */
	public static final void throwArithmeticException(String additionalMessage) {
		throw new ArithmeticException(errorMessageAddon
				+ ((additionalMessage.equals("")) ? "" : "\n"
						+ additionalMessage));
	}

	/**
	 * Throws a ArrayIndexOutOfBoundsException with the error message addon
	 */
	public static final void throwArrayIndexOutOfBoundsException() {
		throwArrayIndexOutOfBoundsException("");
	}

	/**
	 * Throws a ArrayIndexOutOfBoundsException with the error message addon
	 * 
	 * @param additionalMessage
	 *            a additional message added after the addon
	 */
	public static final void throwArrayIndexOutOfBoundsException(
			String additionalMessage) {
		throw new ArrayIndexOutOfBoundsException(errorMessageAddon
				+ ((additionalMessage.equals("")) ? "" : "\n"
						+ additionalMessage));
	}

	/**
	 * Throws a ArrayStoreException with the error message addon
	 */
	public static final void throwArrayStoreException() {
		throwArrayStoreException("");
	}

	/**
	 * Throws a ArrayStoreException with the error message addon
	 * 
	 * @param additionalMessage
	 *            a additional message added after the addon
	 */
	public static final void throwArrayStoreException(String additionalMessage) {
		throw new ArrayStoreException(errorMessageAddon
				+ ((additionalMessage.equals("")) ? "" : "\n"
						+ additionalMessage));
	}

	/**
	 * Throws a ClassCastException with the error message addon
	 */
	public static final void throwClassCastException() {
		throwClassCastException("");
	}

	/**
	 * Throws a ClassCastException with the error message addon
	 * 
	 * @param additionalMessage
	 *            a additional message added after the addon
	 */
	public static final void throwClassCastException(String additionalMessage) {
		throw new ClassCastException(errorMessageAddon
				+ ((additionalMessage.equals("")) ? "" : "\n"
						+ additionalMessage));
	}

	/**
	 * Throws a ClassNotFoundException with the error message addon
	 * 
	 * @throws ClassNotFoundException
	 */
	public static final void throwClassNotFoundException()
			throws ClassNotFoundException {
		throwClassNotFoundException("");
	}

	/**
	 * Throws a ClassNotFoundException with the error message addon
	 * 
	 * @param additionalMessage
	 *            a additional message added after the addon
	 * @throws ClassNotFoundException
	 */
	public static final void throwClassNotFoundException(
			String additionalMessage) throws ClassNotFoundException {
		throw new ClassNotFoundException(errorMessageAddon
				+ ((additionalMessage.equals("")) ? "" : "\n"
						+ additionalMessage));
	}

	/**
	 * Throws a CloneNotSupportedException with the error message addon
	 * 
	 * @throws CloneNotSupportedException
	 */
	public static final void throwCloneNotSupportedException()
			throws CloneNotSupportedException {
		throwCloneNotSupportedException("");
	}

	/**
	 * Throws a CloneNotSupportedException with the error message addon
	 * 
	 * @param additionalMessage
	 *            a additional message added after the addon
	 * @throws CloneNotSupportedException
	 */
	public static final void throwCloneNotSupportedException(
			String additionalMessage) throws CloneNotSupportedException {
		throw new CloneNotSupportedException(errorMessageAddon
				+ ((additionalMessage.equals("")) ? "" : "\n"
						+ additionalMessage));
	}

	/**
	 * Throws a Exception with the error message addon
	 * 
	 * @throws Exception
	 */
	public static final void throwException() throws Exception {
		throwException("");
	}

	/**
	 * Throws a exception with the error message addon. This is for all
	 * <b><u>non-RuntimeExceptions</u></b>
	 * 
	 * @param exception
	 *            the exception to throw
	 * @throws E
	 *             the exception type
	 */
	public static final <E extends Throwable> void throwException(E exception)
			throws E {
		throwException(exception, "");
	}

	/**
	 * Throws a exception with the error message addon. This is for all
	 * <b><u>RuntimeExceptions</u></b>
	 * 
	 * @param exception
	 *            the exception to throw
	 */
	public static final <E extends RuntimeException> void throwException(
			E exception) {
		throwException(exception, "");
	}

	/**
	 * Throws a exception with the error message addon and a custom message.
	 * This is for all <b><u>non-RuntimeExceptions</u></b>
	 * 
	 * @param exception
	 *            the exception to throw
	 * @param additionalMessage
	 *            the custom message that will be added
	 * @throws E
	 *             the exception type
	 */
	public static final <E extends Throwable> void throwException(E exception,
			String additionalMessage) throws E {
		throw (E) new Throwable("("
				+ exception.getClass().getName()
				+ ")"
				+ BSP.errorMessageAddon
				+ ((additionalMessage.equals("")) ? "" : "\n"
						+ additionalMessage + "\n") + "\nOriginal message: \""
				+ exception.getMessage() + "\"\n", exception);
	}

	/**
	 * Throws a exception with the error message addon and a custom message.
	 * This is for all <b><u>RuntimeExceptions</u></b>
	 * 
	 * @param exception
	 *            the exception to throw
	 * @param additionalMessage
	 *            the custom message that will be added
	 */
	public static final <E extends RuntimeException> void throwException(
			E exception, String additionalMessage) {
		throw (E) new RuntimeException("("
				+ exception.getClass().getName()
				+ ")"
				+ errorMessageAddon
				+ ((additionalMessage.equals("")) ? "" : "\n"
						+ additionalMessage + "\n") + "\nOriginal message: \""
				+ exception.getMessage() + "\"\n", exception);
	}

	/**
	 * Throws a Exception with the error message addon
	 * 
	 * @param additionalMessage
	 *            a additional message added after the addon
	 * @throws Exception
	 */
	public static final void throwException(String additionalMessage)
			throws Exception {
		throw new Exception(errorMessageAddon
				+ ((additionalMessage.equals("")) ? "" : "\n"
						+ additionalMessage));
	}

	/**
	 * Throws a IllegalAccessException with the error message addon
	 * 
	 * @throws IllegalAccessException
	 */
	public static final void throwIllegalAccessException()
			throws IllegalAccessException {
		throwIllegalAccessException("");
	}

	/**
	 * Throws a IllegalAccessException with the error message addon
	 * 
	 * @param additionalMessage
	 *            a additional message added after the addon
	 * @throws IllegalAccessException
	 */
	public static final void throwIllegalAccessException(
			String additionalMessage) throws IllegalAccessException {
		throw new IllegalAccessException(errorMessageAddon
				+ ((additionalMessage.equals("")) ? "" : "\n"
						+ additionalMessage));
	}

	/**
	 * Throws a IllegalArgumentException with the error message addon
	 */
	public static final void throwIllegalArgumentException() {
		throwIllegalArgumentException("");
	}

	/**
	 * Throws a IllegalArgumentException with the error message addon
	 * 
	 * @param additionalMessage
	 *            a additional message added after the addon
	 */
	public static final void throwIllegalArgumentException(
			String additionalMessage) {
		throw new IllegalArgumentException(errorMessageAddon
				+ ((additionalMessage.equals("")) ? "" : "\n"
						+ additionalMessage));
	}

	/**
	 * Throws a IllegalMonitorStateException with the error message addon
	 */
	public static final void throwIllegalMonitorStateException() {
		throwIllegalMonitorStateException("");
	}

	/**
	 * Throws a IllegalMonitorStateException with the error message addon
	 * 
	 * @param additionalMessage
	 *            a additional message added after the addon
	 */
	public static final void throwIllegalMonitorStateException(
			String additionalMessage) {
		throw new IllegalMonitorStateException(errorMessageAddon
				+ ((additionalMessage.equals("")) ? "" : "\n"
						+ additionalMessage));
	}

	/**
	 * Throws a IllegalThreadStateException with the error message addon
	 */
	public static final void throwIllegalThreadStateException() {
		throwIllegalThreadStateException("");
	}

	/**
	 * Throws a IllegalThreadStateException with the error message addon
	 * 
	 * @param additionalMessage
	 *            a additional message added after the addon
	 */
	public static final void throwIllegalThreadStateException(
			String additionalMessage) {
		throw new IllegalThreadStateException(errorMessageAddon
				+ ((additionalMessage.equals("")) ? "" : "\n"
						+ additionalMessage));
	}

	/**
	 * Throws a IndexOutOfBoundsException with the error message addon
	 */
	public static final void throwIndexOutOfBoundsException() {
		throwIndexOutOfBoundsException("");
	}

	/**
	 * Throws a IndexOutOfBoundsException with the error message addon
	 * 
	 * @param additionalMessage
	 *            a additional message added after the addon
	 */
	public static final void throwIndexOutOfBoundsException(
			String additionalMessage) {
		throw new IndexOutOfBoundsException(errorMessageAddon
				+ ((additionalMessage.equals("")) ? "" : "\n"
						+ additionalMessage));
	}

	/**
	 * Throws a InstantiationException with the error message addon
	 * 
	 * @throws InstantiationException
	 */
	public static final void throwInstantiationException()
			throws InstantiationException {
		throwInstantiationException("");
	}

	/**
	 * Throws a InstantiationException with the error message addon
	 * 
	 * @param additionalMessage
	 *            a additional message added after the addon
	 * @throws InstantiationException
	 */
	public static final void throwInstantiationException(
			String additionalMessage) throws InstantiationException {
		throw new InstantiationException(errorMessageAddon
				+ ((additionalMessage.equals("")) ? "" : "\n"
						+ additionalMessage));
	}

	/**
	 * Throws a InterruptedException with the error message addon
	 * 
	 * @throws InterruptedException
	 */
	public static final void throwInterruptedException()
			throws InterruptedException {
		throwInterruptedException("");
	}

	/**
	 * Throws a InterruptedException with the error message addon
	 * 
	 * @param additionalMessage
	 *            a additional message added after the addon
	 * @throws InterruptedException
	 */
	public static final void throwInterruptedException(String additionalMessage)
			throws InterruptedException {
		throw new InterruptedException(errorMessageAddon
				+ ((additionalMessage.equals("")) ? "" : "\n"
						+ additionalMessage));
	}

	/**
	 * Throws a NegativeArraySizeException with the error message addon
	 */
	public static final void throwNegativeArraySizeException() {
		throwNegativeArraySizeException("");
	}

	/**
	 * Throws a NegativeArraySizeException with the error message addon
	 * 
	 * @param additionalMessage
	 *            a additional message added after the addon
	 */
	public static final void throwNegativeArraySizeException(
			String additionalMessage) {
		throw new NegativeArraySizeException(errorMessageAddon
				+ ((additionalMessage.equals("")) ? "" : "\n"
						+ additionalMessage));
	}

	/**
	 * Throws a NoSuchMethodException with the error message addon
	 * 
	 * @throws NoSuchMethodException
	 */
	public static final void throwNoSuchMethodException()
			throws NoSuchMethodException {
		throwNoSuchMethodException("");
	}

	/**
	 * Throws a NoSuchMethodException with the error message addon
	 * 
	 * @param additionalMessage
	 *            a additional message added after the addon
	 * @throws NoSuchMethodException
	 */
	public static final void throwNoSuchMethodException(String additionalMessage)
			throws NoSuchMethodException {
		throw new NoSuchMethodException(errorMessageAddon
				+ ((additionalMessage.equals("")) ? "" : "\n"
						+ additionalMessage));
	}

	/**
	 * Throws a NullPointerException with the error message addon
	 */
	public static final void throwNullPointerException() {
		throwNullPointerException("");
	}

	/**
	 * Throws a NullPointerException with the error message addon
	 * 
	 * @param additionalMessage
	 *            a additional message added after the addon
	 */
	public static final void throwNullPointerException(String additionalMessage) {
		throw new NullPointerException(errorMessageAddon
				+ ((additionalMessage.equals("")) ? "" : "\n"
						+ additionalMessage));
	}

	/**
	 * Throws a NumberFormatException with the error message addon
	 */
	public static final void throwNumberFormatException() {
		throwNumberFormatException("");
	}

	/**
	 * Throws a NumberFormatException with the error message addon
	 * 
	 * @param additionalMessage
	 *            a additional message added after the addon
	 */
	public static final void throwNumberFormatException(String additionalMessage) {
		throw new NumberFormatException(errorMessageAddon
				+ ((additionalMessage.equals("")) ? "" : "\n"
						+ additionalMessage));
	}

	/**
	 * Throws a RuntimeException with the error message addon
	 */
	public static final void throwRuntimeException() {
		throwRuntimeException("");
	}

	/**
	 * Throws a RuntimeException with the error message addon
	 * 
	 * @param additionalMessage
	 *            a additional message added after the addon
	 */
	public static final void throwRuntimeException(String additionalMessage) {
		throw new RuntimeException(errorMessageAddon
				+ ((additionalMessage.equals("")) ? "" : "\n"
						+ additionalMessage));
	}

	/**
	 * Throws a SecurityException with the error message addon
	 */
	public static final void throwSecurityException() {
		throwSecurityException("");
	}

	/**
	 * Throws a SecurityException with the error message addon
	 * 
	 * @param additionalMessage
	 *            a additional message added after the addon
	 */
	public static final void throwSecurityException(String additionalMessage) {
		throw new SecurityException(errorMessageAddon
				+ ((additionalMessage.equals("")) ? "" : "\n"
						+ additionalMessage));
	}

	/**
	 * Throws a StringIndexOutOfBoundsException with the error message addon
	 */
	public static final void throwStringIndexOutOfBoundsException() {
		throwStringIndexOutOfBoundsException("");
	}

	/**
	 * Throws a StringIndexOutOfBoundsException with the error message addon
	 * 
	 * @param additionalMessage
	 *            a additional message added after the addon
	 */
	public static final void throwStringIndexOutOfBoundsException(
			String additionalMessage) {
		throw new StringIndexOutOfBoundsException(errorMessageAddon
				+ ((additionalMessage.equals("")) ? "" : "\n"
						+ additionalMessage));
	}

	/**
	 * Throws a Throwable with the error message addon
	 * 
	 * @throws Throwable
	 */
	public static final void throwThrowable() throws Throwable {
		throwThrowable("");
	}

	/**
	 * Throws a Throwable with the error message addon
	 * 
	 * @param additionalMessage
	 *            a additional message added after the addon
	 * @throws Throwable
	 */
	public static final void throwThrowable(String additionalMessage)
			throws Throwable {
		throw new Throwable(errorMessageAddon
				+ ((additionalMessage.equals("")) ? "" : "\n"
						+ additionalMessage));
	}
}