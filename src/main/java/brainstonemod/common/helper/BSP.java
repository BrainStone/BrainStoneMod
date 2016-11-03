package brainstonemod.common.helper;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Logger;

import brainstonemod.BrainStone;

/**
 * <center><b><u>B</u>rain<u>S</u>tone<u>P</u>rinter</b></center><br>
 * This class is the printer class for the BrainStoneMod. It contains a lot of
 * static final printing methods, that will print the given object under the
 * right conditions. Exceptions will be thrown here as well.
 * 
 * @author Yannick Schinko (alias The_BrainStone)
 * @version 3.0.0
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

	/**
	 * This is the logger for the BSM. Will be initialized in the BrainStone
	 * class
	 */
	private static Logger logger;

	/**
	 * <b>A</b>dd<b>N</b>ew<b>L</b>ine<b>I</b>f<b>N</b>ecessary<br>
	 * This function adds a new line in front of a string if necessary.
	 * 
	 * @param str
	 *            String to be processed
	 * @return If the String is no empty, a new line will be added in front of
	 *         str
	 */
	private static final String ANLIN(String str) {
		return (str.isEmpty()) ? str : "\n" + str;
	}

	/**
	 * Logs all Objects to the console with the normal DEBUG level
	 * 
	 * @param obj
	 *            the objects to be logged
	 * @return Return whether the log was logged or not
	 * @see {@link BSP#log(Level level, Object... obj)}
	 */
	public static final boolean debug(Object... obj) {
		return log(Level.DEBUG, obj);
	}

	/**
	 * This logs an error to the console with the normal DEBUG level and appends
	 * the errorMessageAddon
	 * 
	 * @param ex
	 *            the exception to be logged
	 * @return Return whether the log was logged or not
	 * @see {@link BSP#logException(Level level, Object... obj)}
	 */
	public static final boolean debugException(Throwable ex) {
		return debugException(ex, "");
	}

	/**
	 * This logs an error to the console with the normal DEBUG level and appends
	 * the errorMessageAddon and an additional message
	 * 
	 * @param ex
	 *            the exception to be logged
	 * @param additionalMessage
	 *            a message appended to the standard error message addon
	 * @return Return whether the log was logged or not
	 * @see {@link BSP#printException(Level level, Throwable ex, String additionalMessage)}
	 */
	public static final boolean debugException(Throwable ex, String additionalMessage) {
		return logException(Level.DEBUG, ex, additionalMessage);
	}

	/**
	 * This logs an error to the console with the normal DEBUG level
	 * 
	 * @param ex
	 *            the exception to be logged
	 * @return Return whether the log was logged or not
	 *         printException_noAddon(Level.INFO, Throwable ex)
	 * @see {@link BSP#logException_noAddon(Level level, Object... obj)}
	 */
	public static final boolean debugException_noAddon(Throwable ex) {
		return debugException_noAddon(ex, "");
	}

	/**
	 * This logs an error to the console with the normal DEBUG level and an
	 * additional message
	 * 
	 * @param ex
	 *            the exception to be logged
	 * @param additionalMessage
	 *            a message put in front of the error log
	 * @return Return whether the log was logged or not
	 * @see {@link BSP#printException_noAddon(Level level, Throwable ex, String additionalMessage)}
	 */
	public static final boolean debugException_noAddon(Throwable ex, String additionalMessage) {
		return logException_noAddon(Level.DEBUG, ex, additionalMessage);
	}

	/**
	 * Logs all Objects to the console with the normal ERROR level
	 * 
	 * @param obj
	 *            the objects to be logged
	 * @return Return whether the log was logged or not
	 * @see {@link BSP#log(Level level, Object... obj)}
	 */
	public static final boolean error(Object... obj) {
		return log(Level.ERROR, obj);
	}

	/**
	 * This logs an error to the console with the normal ERROR level and appends
	 * the errorMessageAddon
	 * 
	 * @param ex
	 *            the exception to be logged
	 * @return Return whether the log was logged or not
	 * @see {@link BSP#logException(Level level, Object... obj)}
	 */
	public static final boolean errorException(Throwable ex) {
		return errorException(ex, "");
	}

	/**
	 * This logs an error to the console with the normal ERROR level and appends
	 * the errorMessageAddon and an additional message
	 * 
	 * @param ex
	 *            the exception to be logged
	 * @param additionalMessage
	 *            a message appended to the standard error message addon
	 * @return Return whether the log was logged or not
	 * @see {@link BSP#printException(Level level, Throwable ex, String additionalMessage)}
	 */
	public static final boolean errorException(Throwable ex, String additionalMessage) {
		return logException(Level.ERROR, ex, additionalMessage);
	}

	/**
	 * This logs an error to the console with the normal ERROR level
	 * 
	 * @param ex
	 *            the exception to be logged
	 * @return Return whether the log was logged or not
	 *         printException_noAddon(Level.INFO, Throwable ex)
	 * @see {@link BSP#logException_noAddon(Level level, Object... obj)}
	 */
	public static final boolean errorException_noAddon(Throwable ex) {
		return errorException_noAddon(ex, "");
	}

	/**
	 * This logs an error to the console with the normal ERROR level and an
	 * additional message
	 * 
	 * @param ex
	 *            the exception to be logged
	 * @param additionalMessage
	 *            a message put in front of the error log
	 * @return Return whether the log was logged or not
	 * @see {@link BSP#printException_noAddon(Level level, Throwable ex, String additionalMessage)}
	 */
	public static final boolean errorException_noAddon(Throwable ex, String additionalMessage) {
		return logException_noAddon(Level.ERROR, ex, additionalMessage);
	}

	/**
	 * Logs all Objects to the console with the normal FATAL level
	 * 
	 * @param obj
	 *            the objects to be logged
	 * @return Return whether the log was logged or not
	 * @see {@link BSP#log(Level level, Object... obj)}
	 */
	public static final boolean fatal(Object... obj) {
		return log(Level.FATAL, obj);
	}

	/**
	 * This logs an error to the console with the normal FATAL level and appends
	 * the errorMessageAddon
	 * 
	 * @param ex
	 *            the exception to be logged
	 * @return Return whether the log was logged or not
	 * @see {@link BSP#logException(Level level, Object... obj)}
	 */
	public static final boolean fatalException(Throwable ex) {
		return fatalException(ex, "");
	}

	/**
	 * This logs an error to the console with the normal FATAL level and appends
	 * the errorMessageAddon and an additional message
	 * 
	 * @param ex
	 *            the exception to be logged
	 * @param additionalMessage
	 *            a message appended to the standard error message addon
	 * @return Return whether the log was logged or not
	 * @see {@link BSP#printException(Level level, Throwable ex, String additionalMessage)}
	 */
	public static final boolean fatalException(Throwable ex, String additionalMessage) {
		return logException(Level.FATAL, ex, additionalMessage);
	}

	/**
	 * This logs an error to the console with the normal FATAL level
	 * 
	 * @param ex
	 *            the exception to be logged
	 * @return Return whether the log was logged or not
	 *         printException_noAddon(Level.INFO, Throwable ex)
	 * @see {@link BSP#logException_noAddon(Level level, Object... obj)}
	 */
	public static final boolean fatalException_noAddon(Throwable ex) {
		return fatalException_noAddon(ex, "");
	}

	/**
	 * This logs an error to the console with the normal FATAL level and an
	 * additional message
	 * 
	 * @param ex
	 *            the exception to be logged
	 * @param additionalMessage
	 *            a message put in front of the error log
	 * @return Return whether the log was logged or not
	 * @see {@link BSP#printException_noAddon(Level level, Throwable ex, String additionalMessage)}
	 */
	public static final boolean fatalException_noAddon(Throwable ex, String additionalMessage) {
		return logException_noAddon(Level.FATAL, ex, additionalMessage);
	}

	/**
	 * Logs all Objects to the console with the normal INFO level
	 * 
	 * @param obj
	 *            the objects to be logged
	 * @return Return whether the log was logged or not
	 * @see {@link BSP#log(Object... obj)}
	 * @see {@link BSP#log(Level level, Object... obj)}
	 */
	public static final boolean info(Object... obj) {
		return log(obj);
	}

	/**
	 * This logs an error to the console with the normal INFO level and appends
	 * the errorMessageAddon
	 * 
	 * @param ex
	 *            the exception to be logged
	 * @return Return whether the log was logged or not
	 * @see {@link BSP#logException(Object... obj)}
	 * @see {@link BSP#logException(Level level, Object... obj)}
	 */
	public static final boolean infoException(Throwable ex) {
		return infoException(ex, "");
	}

	/**
	 * This logs an error to the console with the normal INFO level and appends
	 * the errorMessageAddon and an additional message
	 * 
	 * @param ex
	 *            the exception to be logged
	 * @param additionalMessage
	 *            a message appended to the standard error message addon
	 * @return Return whether the log was logged or not
	 * @see {@link BSP#printException(Throwable ex, String additionalMessage)}
	 * @see {@link BSP#printException(Level level, Throwable ex, String additionalMessage)}
	 */
	public static final boolean infoException(Throwable ex, String additionalMessage) {
		return logException(ex, additionalMessage);
	}

	/**
	 * This logs an error to the console with the normal INFO level
	 * 
	 * @param ex
	 *            the exception to be logged
	 * @return Return whether the log was logged or not
	 *         printException_noAddon(Level.INFO, Throwable ex)
	 * @see {@link BSP#logException_noAddon(Object... obj)}
	 * @see {@link BSP#logException_noAddon(Level level, Object... obj)}
	 */
	public static final boolean infoException_noAddon(Throwable ex) {
		return infoException_noAddon(ex, "");
	}

	/**
	 * This logs an error to the console with the normal INFO level and an
	 * additional message
	 * 
	 * @param ex
	 *            the exception to be logged
	 * @param additionalMessage
	 *            a message put in front of the error log
	 * @return Return whether the log was logged or not
	 * @see {@link BSP#printException_noAddon(Throwable ex, String additionalMessage)}
	 * @see {@link BSP#printException_noAddon(Level level, Throwable ex, String additionalMessage)}
	 */
	public static final boolean infoException_noAddon(Throwable ex, String additionalMessage) {
		return logException_noAddon(ex, additionalMessage);
	}

	/**
	 * Logs all Objects to the console with the level to be logged
	 * 
	 * @param level
	 *            the level the objects will be logged
	 * @param obj
	 *            the objects to be logged
	 * @return Return whether the log was logged or not
	 */
	public static final boolean log(Level level, Object... obj) {
		for (final Object log : obj) {
			if (log == null) {
				logger.log(level, "~~NULL~~");
			} else {
				logger.log(level, log.toString());
			}
		}

		return logger.isEnabled(level);
	}

	/**
	 * Logs all Objects to the console with the normal INFO level
	 * 
	 * @param obj
	 *            the objects to be logged
	 * @return Return whether the log was logged or not
	 * @see public static final boolean info(Object... obj)
	 * @see public static final boolean print(Level.INFO, Object... obj)
	 */
	public static final boolean log(Object... obj) {
		return log(Level.INFO, obj);
	}

	/**
	 * This logs an error to the console with the level to be logged
	 * 
	 * @param level
	 *            the level the objects will be logged
	 * @param ex
	 *            the exception to be logged
	 * @return Return whether the log was logged or not
	 */
	public static final boolean logException(Level level, Throwable ex) {
		return logException(level, ex, "");
	}

	/**
	 * This logs an error to the console with an additional message and the
	 * level to be logged
	 * 
	 * @param level
	 *            the level the objects will be logged
	 * @param ex
	 *            the exception to be logged
	 * @param additionalMessage
	 *            a message appended to the standard error message addon
	 * @return Return whether the log was logged or not
	 */
	public static final boolean logException(Level level, Throwable ex, String additionalMessage) {
		logger.log(level, errorMessageAddon + ANLIN(additionalMessage), ex);

		return logger.isEnabled(level);
	}

	/**
	 * This logs an error to the console with the normal INFO level and appends
	 * the errorMessageAddon
	 * 
	 * @param ex
	 *            the exception to be logged
	 * @return Return whether the log was logged or not
	 * @see printException(Level.INFO, Throwable ex)
	 * @see infoException(Throwable ex)
	 */
	public static final boolean logException(Throwable ex) {
		return logException(ex, "");
	}

	/**
	 * This logs an error to the console with the normal INFO level and appends
	 * the errorMessageAddon and an additional message
	 * 
	 * @param ex
	 *            the exception to be logged
	 * @param additionalMessage
	 *            a message appended to the standard error message addon
	 * @return Return whether the log was logged or not
	 * @see printException(Level.INFO, Throwable ex, String additionalMessage)
	 * @see infoException(Throwable ex, String additionalMessage)
	 */
	public static final boolean logException(Throwable ex, String additionalMessage) {
		return logException(Level.INFO, ex, additionalMessage);
	}

	/**
	 * This logs an error to the console with the level to be logged
	 * 
	 * @param level
	 *            the level the objects will be logged
	 * @param ex
	 *            the exception to be logged
	 * @return Return whether the log was logged or not
	 */
	public static final boolean logException_noAddon(Level level, Throwable ex) {
		return logException_noAddon(level, ex, "");
	}

	/**
	 * This logs an error to the console with an additional message and the
	 * level to be logged
	 * 
	 * @param level
	 *            the level the objects will be logged
	 * @param ex
	 *            the exception to be logged
	 * @param additionalMessage
	 *            a message put in front of the error log
	 * @return Return whether the log was logged or not
	 */
	public static final boolean logException_noAddon(Level level, Throwable ex, String additionalMessage) {
		logger.log(level, additionalMessage, ex);

		return logger.isEnabled(level);
	}

	/**
	 * This logs an error to the console with the normal INFO level
	 * 
	 * @param ex
	 *            the exception to be logged
	 * @return Return whether the log was logged or not
	 *         printException_noAddon(Level.INFO, Throwable ex)
	 * @see infoException_noAddon(Throwable ex)
	 */
	public static final boolean logException_noAddon(Throwable ex) {
		return logException_noAddon(ex, "");
	}

	/**
	 * This logs an error to the console with the normal INFO level and an
	 * additional message
	 * 
	 * @param ex
	 *            the exception to be logged
	 * @param additionalMessage
	 *            a message put in front of the error log
	 * @return Return whether the log was logged or not
	 * @see printException_noAddon(Level.INFO, Throwable ex, String
	 *      additionalMessage)
	 * @see infoException_noAddon(Throwable ex, String additionalMessage)
	 */
	public static final boolean logException_noAddon(Throwable ex, String additionalMessage) {
		return logException_noAddon(Level.INFO, ex, additionalMessage);
	}

	/**
	 * Sets up the logger and also sets up the corresponding filter
	 * 
	 * @param logger
	 *            - the logger for this class
	 */
	public static void setUpLogger(Logger logger) {
		BSP.logger = logger;

		if (BrainStone.debug) {
			BSP.logger.setLevel(Level.ALL);
		} else {
			BSP.logger.setLevel(Level.DEBUG);
		}
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
		throw new ArithmeticException(errorMessageAddon + ANLIN(additionalMessage));
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
	public static final void throwArrayIndexOutOfBoundsException(String additionalMessage) {
		throw new ArrayIndexOutOfBoundsException(errorMessageAddon + ANLIN(additionalMessage));
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
		throw new ArrayStoreException(errorMessageAddon + ANLIN(additionalMessage));
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
		throw new ClassCastException(errorMessageAddon + ANLIN(additionalMessage));
	}

	/**
	 * Throws a ClassNotFoundException with the error message addon
	 * 
	 * @throws ClassNotFoundException
	 */
	public static final void throwClassNotFoundException() throws ClassNotFoundException {
		throwClassNotFoundException("");
	}

	/**
	 * Throws a ClassNotFoundException with the error message addon
	 * 
	 * @param additionalMessage
	 *            a additional message added after the addon
	 * @throws ClassNotFoundException
	 */
	public static final void throwClassNotFoundException(String additionalMessage) throws ClassNotFoundException {
		throw new ClassNotFoundException(errorMessageAddon + ANLIN(additionalMessage));
	}

	/**
	 * Throws a CloneNotSupportedException with the error message addon
	 * 
	 * @throws CloneNotSupportedException
	 */
	public static final void throwCloneNotSupportedException() throws CloneNotSupportedException {
		throwCloneNotSupportedException("");
	}

	/**
	 * Throws a CloneNotSupportedException with the error message addon
	 * 
	 * @param additionalMessage
	 *            a additional message added after the addon
	 * @throws CloneNotSupportedException
	 */
	public static final void throwCloneNotSupportedException(String additionalMessage)
			throws CloneNotSupportedException {
		throw new CloneNotSupportedException(
				errorMessageAddon + ((additionalMessage.isEmpty()) ? "" : "\n" + additionalMessage));
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
	public static final <E extends Throwable> void throwException(E exception) throws E {
		throwException(exception, "");
	}

	/**
	 * Throws a exception with the error message addon. This is for all
	 * <b><u>RuntimeExceptions</u></b>
	 * 
	 * @param exception
	 *            the exception to throw
	 */
	public static final <E extends RuntimeException> void throwException(E exception) {
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
	@SuppressWarnings("unchecked")
	public static final <E extends Throwable> void throwException(E exception, String additionalMessage) throws E {
		throw (E) new Throwable("(" + exception.getClass().getName() + ")" + BSP.errorMessageAddon
				+ ANLIN(additionalMessage + "\n") + "\nOriginal message: \"" + exception.getMessage() + "\"\n",
				exception);
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
	public static final <E extends RuntimeException> void throwException(E exception, String additionalMessage) {
		throw new RuntimeException("(" + exception.getClass().getName() + ")" + errorMessageAddon
				+ ANLIN(additionalMessage + "\n") + "\nOriginal message: \"" + exception.getMessage() + "\"\n",
				exception);
	}

	/**
	 * Throws a Exception with the error message addon
	 * 
	 * @param additionalMessage
	 *            a additional message added after the addon
	 * @throws Exception
	 */
	public static final void throwException(String additionalMessage) throws Exception {
		throw new Exception(errorMessageAddon + ANLIN(additionalMessage));
	}

	/**
	 * Throws a IllegalAccessException with the error message addon
	 * 
	 * @throws IllegalAccessException
	 */
	public static final void throwIllegalAccessException() throws IllegalAccessException {
		throwIllegalAccessException("");
	}

	/**
	 * Throws a IllegalAccessException with the error message addon
	 * 
	 * @param additionalMessage
	 *            a additional message added after the addon
	 * @throws IllegalAccessException
	 */
	public static final void throwIllegalAccessException(String additionalMessage) throws IllegalAccessException {
		throw new IllegalAccessException(errorMessageAddon + ANLIN(additionalMessage));
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
	public static final void throwIllegalArgumentException(String additionalMessage) {
		throw new IllegalArgumentException(errorMessageAddon + ANLIN(additionalMessage));
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
	public static final void throwIllegalMonitorStateException(String additionalMessage) {
		throw new IllegalMonitorStateException(errorMessageAddon + ANLIN(additionalMessage));
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
	public static final void throwIllegalThreadStateException(String additionalMessage) {
		throw new IllegalThreadStateException(errorMessageAddon + ANLIN(additionalMessage));
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
	public static final void throwIndexOutOfBoundsException(String additionalMessage) {
		throw new IndexOutOfBoundsException(errorMessageAddon + ANLIN(additionalMessage));
	}

	/**
	 * Throws a InstantiationException with the error message addon
	 * 
	 * @throws InstantiationException
	 */
	public static final void throwInstantiationException() throws InstantiationException {
		throwInstantiationException("");
	}

	/**
	 * Throws a InstantiationException with the error message addon
	 * 
	 * @param additionalMessage
	 *            a additional message added after the addon
	 * @throws InstantiationException
	 */
	public static final void throwInstantiationException(String additionalMessage) throws InstantiationException {
		throw new InstantiationException(errorMessageAddon + ANLIN(additionalMessage));
	}

	/**
	 * Throws a InterruptedException with the error message addon
	 * 
	 * @throws InterruptedException
	 */
	public static final void throwInterruptedException() throws InterruptedException {
		throwInterruptedException("");
	}

	/**
	 * Throws a InterruptedException with the error message addon
	 * 
	 * @param additionalMessage
	 *            a additional message added after the addon
	 * @throws InterruptedException
	 */
	public static final void throwInterruptedException(String additionalMessage) throws InterruptedException {
		throw new InterruptedException(errorMessageAddon + ANLIN(additionalMessage));
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
	public static final void throwNegativeArraySizeException(String additionalMessage) {
		throw new NegativeArraySizeException(errorMessageAddon + ANLIN(additionalMessage));
	}

	/**
	 * Throws a NoSuchMethodException with the error message addon
	 * 
	 * @throws NoSuchMethodException
	 */
	public static final void throwNoSuchMethodException() throws NoSuchMethodException {
		throwNoSuchMethodException("");
	}

	/**
	 * Throws a NoSuchMethodException with the error message addon
	 * 
	 * @param additionalMessage
	 *            a additional message added after the addon
	 * @throws NoSuchMethodException
	 */
	public static final void throwNoSuchMethodException(String additionalMessage) throws NoSuchMethodException {
		throw new NoSuchMethodException(errorMessageAddon + ANLIN(additionalMessage));
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
		throw new NullPointerException(errorMessageAddon + ANLIN(additionalMessage));
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
		throw new NumberFormatException(errorMessageAddon + ANLIN(additionalMessage));
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
		throw new RuntimeException(errorMessageAddon + ANLIN(additionalMessage));
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
		throw new SecurityException(errorMessageAddon + ANLIN(additionalMessage));
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
	public static final void throwStringIndexOutOfBoundsException(String additionalMessage) {
		throw new StringIndexOutOfBoundsException(errorMessageAddon + ANLIN(additionalMessage));
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
	public static final void throwThrowable(String additionalMessage) throws Throwable {
		throw new Throwable(errorMessageAddon + ANLIN(additionalMessage));
	}

	/**
	 * Logs all Objects to the console with the normal TRACE level
	 * 
	 * @param obj
	 *            the objects to be logged
	 * @return Return whether the log was logged or not
	 * @see {@link BSP#log(Level level, Object... obj)}
	 */
	public static final boolean trace(Object... obj) {
		return log(Level.TRACE, obj);
	}

	/**
	 * This logs an error to the console with the normal TRACE level and appends
	 * the errorMessageAddon
	 * 
	 * @param ex
	 *            the exception to be logged
	 * @return Return whether the log was logged or not
	 * @see {@link BSP#logException(Level level, Object... obj)}
	 */
	public static final boolean traceException(Throwable ex) {
		return traceException(ex, "");
	}

	/**
	 * This logs an error to the console with the normal TRACE level and appends
	 * the errorMessageAddon and an additional message
	 * 
	 * @param ex
	 *            the exception to be logged
	 * @param additionalMessage
	 *            a message appended to the standard error message addon
	 * @return Return whether the log was logged or not
	 * @see {@link BSP#printException(Level level, Throwable ex, String additionalMessage)}
	 */
	public static final boolean traceException(Throwable ex, String additionalMessage) {
		return logException(Level.TRACE, ex, additionalMessage);
	}

	/**
	 * This logs an error to the console with the normal TRACE level
	 * 
	 * @param ex
	 *            the exception to be logged
	 * @return Return whether the log was logged or not
	 *         printException_noAddon(Level.INFO, Throwable ex)
	 * @see {@link BSP#logException_noAddon(Level level, Object... obj)}
	 */
	public static final boolean traceException_noAddon(Throwable ex) {
		return traceException_noAddon(ex, "");
	}

	/**
	 * This logs an error to the console with the normal TRACE level and an
	 * additional message
	 * 
	 * @param ex
	 *            the exception to be logged
	 * @param additionalMessage
	 *            a message put in front of the error log
	 * @return Return whether the log was logged or not
	 * @see {@link BSP#printException_noAddon(Level level, Throwable ex, String additionalMessage)}
	 */
	public static final boolean traceException_noAddon(Throwable ex, String additionalMessage) {
		return logException_noAddon(Level.TRACE, ex, additionalMessage);
	}

	/**
	 * Logs all Objects to the console with the normal WARN level
	 * 
	 * @param obj
	 *            the objects to be logged
	 * @return Return whether the log was logged or not
	 * @see {@link BSP#log(Level level, Object... obj)}
	 */
	public static final boolean warn(Object... obj) {
		return log(Level.WARN, obj);
	}

	/**
	 * This logs an error to the console with the normal WARN level and appends
	 * the errorMessageAddon
	 * 
	 * @param ex
	 *            the exception to be logged
	 * @return Return whether the log was logged or not
	 * @see {@link BSP#logException(Level level, Object... obj)}
	 */
	public static final boolean warnException(Throwable ex) {
		return warnException(ex, "");
	}

	/**
	 * This logs an error to the console with the normal WARN level and appends
	 * the errorMessageAddon and an additional message
	 * 
	 * @param ex
	 *            the exception to be logged
	 * @param additionalMessage
	 *            a message appended to the standard error message addon
	 * @return Return whether the log was logged or not
	 * @see {@link BSP#printException(Level level, Throwable ex, String additionalMessage)}
	 */
	public static final boolean warnException(Throwable ex, String additionalMessage) {
		return logException(Level.WARN, ex, additionalMessage);
	}

	/**
	 * This logs an error to the console with the normal WARN level
	 * 
	 * @param ex
	 *            the exception to be logged
	 * @return Return whether the log was logged or not
	 *         printException_noAddon(Level.INFO, Throwable ex)
	 * @see {@link BSP#logException_noAddon(Level level, Object... obj)}
	 */
	public static final boolean warnException_noAddon(Throwable ex) {
		return warnException_noAddon(ex, "");
	}

	/**
	 * This logs an error to the console with the normal WARN level and an
	 * additional message
	 * 
	 * @param ex
	 *            the exception to be logged
	 * @param additionalMessage
	 *            a message put in front of the error log
	 * @return Return whether the log was logged or not
	 * @see {@link BSP#printException_noAddon(Level level, Throwable ex, String additionalMessage)}
	 */
	public static final boolean warnException_noAddon(Throwable ex, String additionalMessage) {
		return logException_noAddon(Level.WARN, ex, additionalMessage);
	}
}