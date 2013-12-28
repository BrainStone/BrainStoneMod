package brainstonemod.common.helper;

import java.util.logging.Level;
import java.util.logging.Logger;

import brainstonemod.BrainStone;

/**
 * <center><b><u>B</u>rain<u>S</u>tone<u>P</u>rinter</b></center><br>
 * This class is the printer class for the BrainStoneMod. It contains a lot of
 * static final printing methods, that will print the given object under the
 * right conditions. Exceptions will be thrown here as well.
 * 
 * @author Yannick Schinko (alias The_BrainStone)
 * @version 2.0.0
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
	 * Logs all Objects to the console with the normal CONFIG level
	 * 
	 * @param obj
	 *            the objects to be logged
	 * @return Return whether the log was logged or not
	 * @see public static final boolean print(Level.CONFIG, Object... obj)
	 */
	public static final boolean config(Object... obj) {
		for (final Object log : obj) {
			logger.config(log.toString());
		}

		return logger.isLoggable(Level.CONFIG);
	}

	/**
	 * This logs an error to the console with the CONFIG level and appends the
	 * errorMessageAddon
	 * 
	 * @param ex
	 *            the exception to be logged
	 * @return Return whether the log was logged or not
	 * @see printException(Level.CONFIG, Exception ex)
	 * @see printException(Exception ex)
	 */
	public static final boolean configException(Exception ex) {
		return configException(ex, "");
	}

	/**
	 * This logs an error to the console with the CONFIG level and appends the
	 * errorMessageAddon and an additional message
	 * 
	 * @param ex
	 *            the exception to be logged
	 * @param additionalMessage
	 *            a message appended to the standard error message addon
	 * @return Return whether the log was logged or not
	 * @see printException(Level.CONFIG, Exception ex, String additionalMessage)
	 * @see printException(Exception ex, String additionalMessage)
	 */
	public static final boolean configException(Exception ex,
			String additionalMessage) {
		logger.log(Level.CONFIG, errorMessageAddon + ANLIN(additionalMessage),
				ex);

		return logger.isLoggable(Level.CONFIG);
	}

	/**
	 * This logs an error to the console with the CONFIG level
	 * 
	 * @param ex
	 *            the exception to be logged
	 * @return Return whether the log was logged or not
	 *         printException_noAddon(Level.CONFIG, Exception ex)
	 * @see printException_noAddon(Exception ex)
	 */
	public static final boolean configException_noAddon(Exception ex) {
		return configException_noAddon(ex, "");
	}

	/**
	 * This logs an error to the console with the CONFIG level and an additional
	 * message
	 * 
	 * @param ex
	 *            the exception to be logged
	 * @param additionalMessage
	 *            a message put in front of the error log
	 * @return Return whether the log was logged or not
	 * @see printException_noAddon(Level.CONFIG, Exception ex, String
	 *      additionalMessage)
	 * @see printException_noAddon(Exception ex, String additionalMessage)
	 */
	public static final boolean configException_noAddon(Exception ex,
			String additionalMessage) {
		logger.log(Level.CONFIG, additionalMessage, ex);

		return logger.isLoggable(Level.CONFIG);
	}

	/**
	 * Logs all Objects to the console with the normal FINE level
	 * 
	 * @param obj
	 *            the objects to be logged
	 * @return Return whether the log was logged or not
	 * @see public static final boolean print(Level.FINE, Object... obj)
	 */
	public static final boolean fine(Object... obj) {
		for (final Object log : obj) {
			logger.fine(log.toString());
		}

		return logger.isLoggable(Level.FINE);
	}

	/**
	 * This logs an error to the console with the FINE level and appends the
	 * errorMessageAddon
	 * 
	 * @param ex
	 *            the exception to be logged
	 * @return Return whether the log was logged or not
	 * @see printException(Level.FINE, Exception ex)
	 * @see printException(Exception ex)
	 */
	public static final boolean fineException(Exception ex) {
		return fineException(ex, "");
	}

	/**
	 * This logs an error to the console with the FINE level and appends the
	 * errorMessageAddon and an additional message
	 * 
	 * @param ex
	 *            the exception to be logged
	 * @param additionalMessage
	 *            a message appended to the standard error message addon
	 * @return Return whether the log was logged or not
	 * @see printException(Level.FINE, Exception ex, String additionalMessage)
	 * @see printException(Exception ex, String additionalMessage)
	 */
	public static final boolean fineException(Exception ex,
			String additionalMessage) {
		logger.log(Level.FINE, errorMessageAddon + ANLIN(additionalMessage), ex);

		return logger.isLoggable(Level.FINE);
	}

	/**
	 * This logs an error to the console with the FINE level
	 * 
	 * @param ex
	 *            the exception to be logged
	 * @return Return whether the log was logged or not
	 *         printException_noAddon(Level.FINE, Exception ex)
	 * @see printException_noAddon(Exception ex)
	 */
	public static final boolean fineException_noAddon(Exception ex) {
		return fineException_noAddon(ex, "");
	}

	/**
	 * This logs an error to the console with the FINE level and an additional
	 * message
	 * 
	 * @param ex
	 *            the exception to be logged
	 * @param additionalMessage
	 *            a message put in front of the error log
	 * @return Return whether the log was logged or not
	 * @see printException_noAddon(Level.FINE, Exception ex, String
	 *      additionalMessage)
	 * @see printException_noAddon(Exception ex, String additionalMessage)
	 */
	public static final boolean fineException_noAddon(Exception ex,
			String additionalMessage) {
		logger.log(Level.FINE, additionalMessage, ex);

		return logger.isLoggable(Level.FINE);
	}

	/**
	 * Logs all Objects to the console with the normal FINER level
	 * 
	 * @param obj
	 *            the objects to be logged
	 * @return Return whether the log was logged or not
	 * @see public static final boolean print(Level.FINER, Object... obj)
	 */
	public static final boolean finer(Object... obj) {
		for (final Object log : obj) {
			logger.finer(log.toString());
		}

		return logger.isLoggable(Level.FINER);
	}

	/**
	 * This logs an error to the console with the FINER level and appends the
	 * errorMessageAddon
	 * 
	 * @param ex
	 *            the exception to be logged
	 * @return Return whether the log was logged or not
	 * @see printException(Level.FINER, Exception ex)
	 * @see printException(Exception ex)
	 */
	public static final boolean finerException(Exception ex) {
		return finerException(ex, "");
	}

	/**
	 * This logs an error to the console with the FINER level and appends the
	 * errorMessageAddon and an additional message
	 * 
	 * @param ex
	 *            the exception to be logged
	 * @param additionalMessage
	 *            a message appended to the standard error message addon
	 * @return Return whether the log was logged or not
	 * @see printException(Level.FINER, Exception ex, String additionalMessage)
	 * @see printException(Exception ex, String additionalMessage)
	 */
	public static final boolean finerException(Exception ex,
			String additionalMessage) {
		logger.log(Level.FINER, errorMessageAddon + ANLIN(additionalMessage),
				ex);

		return logger.isLoggable(Level.FINER);
	}

	/**
	 * This logs an error to the console with the FINER level
	 * 
	 * @param ex
	 *            the exception to be logged
	 * @return Return whether the log was logged or not
	 *         printException_noAddon(Level.FINER, Exception ex)
	 * @see printException_noAddon(Exception ex)
	 */
	public static final boolean finerException_noAddon(Exception ex) {
		return finerException_noAddon(ex, "");
	}

	/**
	 * This logs an error to the console with the FINER level and an additional
	 * message
	 * 
	 * @param ex
	 *            the exception to be logged
	 * @param additionalMessage
	 *            a message put in front of the error log
	 * @return Return whether the log was logged or not
	 * @see printException_noAddon(Level.FINER, Exception ex, String
	 *      additionalMessage)
	 * @see printException_noAddon(Exception ex, String additionalMessage)
	 */
	public static final boolean finerException_noAddon(Exception ex,
			String additionalMessage) {
		logger.log(Level.FINER, additionalMessage, ex);

		return logger.isLoggable(Level.FINER);
	}

	/**
	 * Logs all Objects to the console with the normal FINEST level
	 * 
	 * @param obj
	 *            the objects to be logged
	 * @return Return whether the log was logged or not
	 * @see public static final boolean print(Level.FINEST, Object... obj)
	 */
	public static final boolean finest(Object... obj) {
		for (final Object log : obj) {
			logger.finest(log.toString());
		}

		return logger.isLoggable(Level.FINEST);
	}

	/**
	 * This logs an error to the console with the FINEST level and appends the
	 * errorMessageAddon
	 * 
	 * @param ex
	 *            the exception to be logged
	 * @return Return whether the log was logged or not
	 * @see printException(Level.FINEST, Exception ex)
	 * @see printException(Exception ex)
	 */
	public static final boolean finestException(Exception ex) {
		return finestException(ex, "");
	}

	/**
	 * This logs an error to the console with the FINEST level and appends the
	 * errorMessageAddon and an additional message
	 * 
	 * @param ex
	 *            the exception to be logged
	 * @param additionalMessage
	 *            a message appended to the standard error message addon
	 * @return Return whether the log was logged or not
	 * @see printException(Level.FINEST, Exception ex, String additionalMessage)
	 * @see printException(Exception ex, String additionalMessage)
	 */
	public static final boolean finestException(Exception ex,
			String additionalMessage) {
		logger.log(Level.FINEST, errorMessageAddon + ANLIN(additionalMessage),
				ex);

		return logger.isLoggable(Level.FINEST);
	}

	/**
	 * This logs an error to the console with the FINEST level
	 * 
	 * @param ex
	 *            the exception to be logged
	 * @return Return whether the log was logged or not
	 *         printException_noAddon(Level.FINEST, Exception ex)
	 * @see printException_noAddon(Exception ex)
	 */
	public static final boolean finestException_noAddon(Exception ex) {
		return finestException_noAddon(ex, "");
	}

	/**
	 * This logs an error to the console with the FINEST level and an additional
	 * message
	 * 
	 * @param ex
	 *            the exception to be logged
	 * @param additionalMessage
	 *            a message put in front of the error log
	 * @return Return whether the log was logged or not
	 * @see printException_noAddon(Level.FINEST, Exception ex, String
	 *      additionalMessage)
	 * @see printException_noAddon(Exception ex, String additionalMessage)
	 */
	public static final boolean finestException_noAddon(Exception ex,
			String additionalMessage) {
		logger.log(Level.FINEST, additionalMessage, ex);

		return logger.isLoggable(Level.FINEST);
	}

	/**
	 * Logs all Objects to the console with the normal INFO level
	 * 
	 * @param obj
	 *            the objects to be logged
	 * @return Return whether the log was logged or not
	 * @see public static final boolean print(Object... obj)
	 * @see public static final boolean print(Level.INFO, Object... obj)
	 */
	public static final boolean info(Object... obj) {
		for (final Object log : obj) {
			logger.info(log.toString());
		}

		return logger.isLoggable(Level.INFO);
	}

	/**
	 * This logs an error to the console with the normal INFO level and appends
	 * the errorMessageAddon
	 * 
	 * @param ex
	 *            the exception to be logged
	 * @return Return whether the log was logged or not
	 * @see printException(Level.INFO, Exception ex)
	 * @see printException(Exception ex)
	 */
	public static final boolean infoException(Exception ex) {
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
	 * @see printException(Level.INFO, Exception ex, String additionalMessage)
	 * @see printException(Exception ex, String additionalMessage)
	 */
	public static final boolean infoException(Exception ex,
			String additionalMessage) {
		logger.log(Level.INFO, errorMessageAddon + ANLIN(additionalMessage), ex);

		return logger.isLoggable(Level.INFO);
	}

	/**
	 * This logs an error to the console with the normal INFO level
	 * 
	 * @param ex
	 *            the exception to be logged
	 * @return Return whether the log was logged or not
	 *         printException_noAddon(Level.INFO, Exception ex)
	 * @see printException_noAddon(Exception ex)
	 */
	public static final boolean infoException_noAddon(Exception ex) {
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
	 * @see printException_noAddon(Level.INFO, Exception ex, String
	 *      additionalMessage)
	 * @see printException_noAddon(Exception ex, String additionalMessage)
	 */
	public static final boolean infoException_noAddon(Exception ex,
			String additionalMessage) {
		logger.log(Level.INFO, additionalMessage, ex);

		return logger.isLoggable(Level.INFO);
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
	public static final boolean print(Level level, Object... obj) {
		for (final Object log : obj) {
			logger.log(level, log.toString());
		}

		return logger.isLoggable(level);
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
	public static final boolean print(Object... obj) {
		for (final Object log : obj) {
			logger.info(log.toString());
		}

		return logger.isLoggable(Level.INFO);
	}

	/**
	 * This logs an error to the console with the normal INFO level and appends
	 * the errorMessageAddon
	 * 
	 * @param ex
	 *            the exception to be logged
	 * @return Return whether the log was logged or not
	 * @see printException(Level.INFO, Exception ex)
	 * @see infoException(Exception ex)
	 */
	public static final boolean printException(Exception ex) {
		return printException(ex, "");
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
	 * @see printException(Level.INFO, Exception ex, String additionalMessage)
	 * @see infoException(Exception ex, String additionalMessage)
	 */
	public static final boolean printException(Exception ex,
			String additionalMessage) {
		logger.log(Level.INFO, errorMessageAddon + ANLIN(additionalMessage), ex);

		return logger.isLoggable(Level.INFO);
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
	public static final boolean printException(Level level, Exception ex) {
		return printException(level, ex, "");
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
	public static final boolean printException(Level level, Exception ex,
			String additionalMessage) {
		logger.log(level, errorMessageAddon + ANLIN(additionalMessage), ex);

		return logger.isLoggable(level);
	}

	/**
	 * This logs an error to the console with the normal INFO level
	 * 
	 * @param ex
	 *            the exception to be logged
	 * @return Return whether the log was logged or not
	 *         printException_noAddon(Level.INFO, Exception ex)
	 * @see infoException_noAddon(Exception ex)
	 */
	public static final boolean printException_noAddon(Exception ex) {
		return printException_noAddon(ex, "");
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
	 * @see printException_noAddon(Level.INFO, Exception ex, String
	 *      additionalMessage)
	 * @see infoException_noAddon(Exception ex, String additionalMessage)
	 */
	public static final boolean printException_noAddon(Exception ex,
			String additionalMessage) {
		logger.log(Level.INFO, additionalMessage, ex);

		return logger.isLoggable(Level.INFO);
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
	public static final boolean printException_noAddon(Level level, Exception ex) {
		return printException_noAddon(level, ex, "");
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
	public static final boolean printException_noAddon(Level level,
			Exception ex, String additionalMessage) {
		logger.log(level, additionalMessage, ex);

		return logger.isLoggable(level);
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
		} else if (BrainStone.release) {
			BSP.logger.setLevel(Level.INFO);
		} else {
			BSP.logger.setLevel(Level.FINE);
		}
	}

	/**
	 * Logs all Objects to the console with the normal SEVERE level
	 * 
	 * @param obj
	 *            the objects to be logged
	 * @return Return whether the log was logged or not
	 * @see public static final boolean print(Level.SEVERE, Object... obj)
	 */
	public static final boolean severe(Object... obj) {
		for (final Object log : obj) {
			logger.severe(log.toString());
		}

		return logger.isLoggable(Level.SEVERE);
	}

	/**
	 * This logs an error to the console with the SEVERE level and appends the
	 * errorMessageAddon
	 * 
	 * @param ex
	 *            the exception to be logged
	 * @return Return whether the log was logged or not
	 * @see printException(Level.SEVERE, Exception ex)
	 * @see printException(Exception ex)
	 */
	public static final boolean severeException(Exception ex) {
		return severeException(ex, "");
	}

	/**
	 * This logs an error to the console with the SEVERE level and appends the
	 * errorMessageAddon and an additional message
	 * 
	 * @param ex
	 *            the exception to be logged
	 * @param additionalMessage
	 *            a message appended to the standard error message addon
	 * @return Return whether the log was logged or not
	 * @see printException(Level.SEVERE, Exception ex, String additionalMessage)
	 * @see printException(Exception ex, String additionalMessage)
	 */
	public static final boolean severeException(Exception ex,
			String additionalMessage) {
		logger.log(Level.SEVERE, errorMessageAddon + ANLIN(additionalMessage),
				ex);

		return logger.isLoggable(Level.SEVERE);
	}

	/**
	 * This logs an error to the console with the SEVERE level
	 * 
	 * @param ex
	 *            the exception to be logged
	 * @return Return whether the log was logged or not
	 *         printException_noAddon(Level.SEVERE, Exception ex)
	 * @see printException_noAddon(Exception ex)
	 */
	public static final boolean severeException_noAddon(Exception ex) {
		return severeException_noAddon(ex, "");
	}

	/**
	 * This logs an error to the console with the SEVERE level and an additional
	 * message
	 * 
	 * @param ex
	 *            the exception to be logged
	 * @param additionalMessage
	 *            a message put in front of the error log
	 * @return Return whether the log was logged or not
	 * @see printException_noAddon(Level.SEVERE, Exception ex, String
	 *      additionalMessage)
	 * @see printException_noAddon(Exception ex, String additionalMessage)
	 */
	public static final boolean severeException_noAddon(Exception ex,
			String additionalMessage) {
		logger.log(Level.SEVERE, additionalMessage, ex);

		return logger.isLoggable(Level.SEVERE);
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
				+ ANLIN(additionalMessage));
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
				+ ANLIN(additionalMessage));
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
				+ ANLIN(additionalMessage));
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
				+ ANLIN(additionalMessage));
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
				+ ANLIN(additionalMessage));
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
	@SuppressWarnings("unchecked")
	public static final <E extends Throwable> void throwException(E exception,
			String additionalMessage) throws E {
		throw (E) new Throwable("(" + exception.getClass().getName() + ")"
				+ BSP.errorMessageAddon + ANLIN(additionalMessage + "\n")
				+ "\nOriginal message: \"" + exception.getMessage() + "\"\n",
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
	@SuppressWarnings("unchecked")
	public static final <E extends RuntimeException> void throwException(
			E exception, String additionalMessage) {
		throw (E) new RuntimeException("(" + exception.getClass().getName()
				+ ")" + errorMessageAddon + ANLIN(additionalMessage + "\n")
				+ "\nOriginal message: \"" + exception.getMessage() + "\"\n",
				exception);
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
		throw new Exception(errorMessageAddon + ANLIN(additionalMessage));
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
				+ ANLIN(additionalMessage));
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
				+ ANLIN(additionalMessage));
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
				+ ANLIN(additionalMessage));
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
				+ ANLIN(additionalMessage));
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
				+ ANLIN(additionalMessage));
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
				+ ANLIN(additionalMessage));
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
				+ ANLIN(additionalMessage));
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
				+ ANLIN(additionalMessage));
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
				+ ANLIN(additionalMessage));
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
				+ ANLIN(additionalMessage));
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
				+ ANLIN(additionalMessage));
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
		throw new SecurityException(errorMessageAddon
				+ ANLIN(additionalMessage));
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
				+ ANLIN(additionalMessage));
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
		throw new Throwable(errorMessageAddon + ANLIN(additionalMessage));
	}

	/**
	 * Logs all Objects to the console with the normal WARNING level
	 * 
	 * @param obj
	 *            the objects to be logged
	 * @return Return whether the log was logged or not
	 * @see public static final boolean print(Level.WARNING, Object... obj)
	 */
	public static final boolean warning(Object... obj) {
		for (final Object log : obj) {
			logger.warning(log.toString());
		}

		return logger.isLoggable(Level.WARNING);
	}

	/**
	 * This logs an error to the console with the WARNING level and appends the
	 * errorMessageAddon
	 * 
	 * @param ex
	 *            the exception to be logged
	 * @return Return whether the log was logged or not
	 * @see printException(Level.WARNING, Exception ex)
	 * @see printException(Exception ex)
	 */
	public static final boolean warningException(Exception ex) {
		return warningException(ex, "");
	}

	/**
	 * This logs an error to the console with the WARNING level and appends the
	 * errorMessageAddon and an additional message
	 * 
	 * @param ex
	 *            the exception to be logged
	 * @param additionalMessage
	 *            a message appended to the standard error message addon
	 * @return Return whether the log was logged or not
	 * @see printException(Level.WARNING, Exception ex, String
	 *      additionalMessage)
	 * @see printException(Exception ex, String additionalMessage)
	 */
	public static final boolean warningException(Exception ex,
			String additionalMessage) {
		logger.log(Level.WARNING, errorMessageAddon + ANLIN(additionalMessage),
				ex);

		return logger.isLoggable(Level.WARNING);
	}

	/**
	 * This logs an error to the console with the WARNING level
	 * 
	 * @param ex
	 *            the exception to be logged
	 * @return Return whether the log was logged or not
	 *         printException_noAddon(Level.WARNING, Exception ex)
	 * @see printException_noAddon(Exception ex)
	 */
	public static final boolean warningException_noAddon(Exception ex) {
		return warningException_noAddon(ex, "");
	}

	/**
	 * This logs an error to the console with the WARNING level and an
	 * additional message
	 * 
	 * @param ex
	 *            the exception to be logged
	 * @param additionalMessage
	 *            a message put in front of the error log
	 * @return Return whether the log was logged or not
	 * @see printException_noAddon(Level.WARNING, Exception ex, String
	 *      additionalMessage)
	 * @see printException_noAddon(Exception ex, String additionalMessage)
	 */
	public static final boolean warningException_noAddon(Exception ex,
			String additionalMessage) {
		logger.log(Level.WARNING, additionalMessage, ex);

		return logger.isLoggable(Level.WARNING);
	}
}