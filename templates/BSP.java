package mods.brainstone.templates;

import java.io.PrintStream;
import mods.brainstone.BrainStone;

public abstract class BSP
{
  public static final String errorMessageAddon = "\n==================================================\n==================================================\n   There occured an error in the BrainStoneMod!   \n   Please send the error log to the developer.    \n   This is important if you want the error to be  \n  fixed. To send the log to me, just send a email \n         with the file of the error log to        \n                yannick@tedworld.de               \n\n              Thank you for your help!            \n==================================================\n==================================================\n";
  public static final boolean debug = BrainStone.debug;

  public static final boolean notRelease = (!BrainStone.release) || (debug);

  public static final boolean debugOnly_print(Object obj)
  {
    if (debug) {
      System.out.print(obj);
    }

    return debug;
  }

  public static final boolean debugOnly_print_error(Object obj)
  {
    if (debug) {
      System.err.print(obj);
    }

    return debug;
  }

  public static final boolean debugOnly_printException(Throwable exception)
  {
    return debugOnly_printException_noAddon(exception, "\n==================================================\n==================================================\n   There occured an error in the BrainStoneMod!   \n   Please send the error log to the developer.    \n   This is important if you want the error to be  \n  fixed. To send the log to me, just send a email \n         with the file of the error log to        \n                yannick@tedworld.de               \n\n              Thank you for your help!            \n==================================================\n==================================================\n");
  }

  public static final boolean debugOnly_printException(Throwable exception, String additionalMessage)
  {
    return debugOnly_printException_noAddon(exception, new StringBuilder().append("\n==================================================\n==================================================\n   There occured an error in the BrainStoneMod!   \n   Please send the error log to the developer.    \n   This is important if you want the error to be  \n  fixed. To send the log to me, just send a email \n         with the file of the error log to        \n                yannick@tedworld.de               \n\n              Thank you for your help!            \n==================================================\n==================================================\n\n").append(additionalMessage).toString());
  }

  public static final boolean debugOnly_printException_noAddon(Throwable exception)
  {
    if (debug) {
      exception.printStackTrace();
    }

    return debug;
  }

  public static final boolean debugOnly_printException_noAddon(Throwable exception, String additionalMessage)
  {
    debugOnly_println_error(additionalMessage);

    return debugOnly_printException_noAddon(exception);
  }

  public static final boolean debugOnly_println(Object obj)
  {
    if (debug) {
      System.out.println(obj);
    }

    return debug;
  }

  public static final boolean debugOnly_println_error(Object obj)
  {
    if (debug) {
      System.err.println(obj);
    }

    return debug;
  }

  public static final boolean force_print(Object obj)
  {
    System.out.print(obj);

    return true;
  }

  public static final boolean force_print_error(Object obj)
  {
    System.err.print(obj);

    return true;
  }

  public static final boolean force_printException(Throwable exception)
  {
    return force_printException_noAddon(exception, "\n==================================================\n==================================================\n   There occured an error in the BrainStoneMod!   \n   Please send the error log to the developer.    \n   This is important if you want the error to be  \n  fixed. To send the log to me, just send a email \n         with the file of the error log to        \n                yannick@tedworld.de               \n\n              Thank you for your help!            \n==================================================\n==================================================\n");
  }

  public static final boolean force_printException(Throwable exception, String additionalMessage)
  {
    return force_printException_noAddon(exception, new StringBuilder().append("\n==================================================\n==================================================\n   There occured an error in the BrainStoneMod!   \n   Please send the error log to the developer.    \n   This is important if you want the error to be  \n  fixed. To send the log to me, just send a email \n         with the file of the error log to        \n                yannick@tedworld.de               \n\n              Thank you for your help!            \n==================================================\n==================================================\n\n").append(additionalMessage).toString());
  }

  public static final boolean force_printException_noAddon(Throwable exception)
  {
    exception.printStackTrace();

    return true;
  }

  public static final boolean force_printException_noAddon(Throwable exception, String additionalMessage)
  {
    force_println_error(additionalMessage);

    return force_printException_noAddon(exception);
  }

  public static final boolean force_println(Object obj)
  {
    System.out.println(obj);

    return true;
  }

  public static final boolean force_println_error(Object obj)
  {
    System.err.println(obj);

    return true;
  }

  public static final boolean print(Object obj)
  {
    if (notRelease) {
      System.out.print(obj);
    }

    return notRelease;
  }

  public static final boolean print(Object[] obj) {
    int length = obj.length;

    for (int i = 0; i < length; i++) {
      print(obj[i]);
    }

    return notRelease;
  }

  public static final boolean print_error(Object obj)
  {
    if (notRelease) {
      System.err.print(obj);
    }

    return notRelease;
  }

  public static final boolean printException(Throwable exception)
  {
    return printException_noAddon(exception, "\n==================================================\n==================================================\n   There occured an error in the BrainStoneMod!   \n   Please send the error log to the developer.    \n   This is important if you want the error to be  \n  fixed. To send the log to me, just send a email \n         with the file of the error log to        \n                yannick@tedworld.de               \n\n              Thank you for your help!            \n==================================================\n==================================================\n");
  }

  public static final boolean printException(Throwable exception, String additionalMessage)
  {
    return printException_noAddon(exception, new StringBuilder().append("\n==================================================\n==================================================\n   There occured an error in the BrainStoneMod!   \n   Please send the error log to the developer.    \n   This is important if you want the error to be  \n  fixed. To send the log to me, just send a email \n         with the file of the error log to        \n                yannick@tedworld.de               \n\n              Thank you for your help!            \n==================================================\n==================================================\n\n").append(additionalMessage).toString());
  }

  public static final boolean printException_noAddon(Throwable exception)
  {
    if (notRelease) {
      exception.printStackTrace();
    }

    return notRelease;
  }

  public static final boolean printException_noAddon(Throwable exception, String additionalMessage)
  {
    println_error(additionalMessage);

    return printException_noAddon(exception);
  }

  public static final boolean println(Object obj)
  {
    if (notRelease) {
      System.out.println(obj);
    }

    return notRelease;
  }

  public static final boolean println(Object[] obj) {
    int length = obj.length;

    for (int i = 0; i < length; i++) {
      println(obj[i]);
    }

    return notRelease;
  }

  public static final boolean println_error(Object obj)
  {
    if (notRelease) {
      System.err.println(obj);
    }

    return notRelease;
  }

  public static final void throwArithmeticException()
  {
    throwArithmeticException("");
  }

  public static final void throwArithmeticException(String additionalMessage)
  {
    throw new ArithmeticException(new StringBuilder().append("\n==================================================\n==================================================\n   There occured an error in the BrainStoneMod!   \n   Please send the error log to the developer.    \n   This is important if you want the error to be  \n  fixed. To send the log to me, just send a email \n         with the file of the error log to        \n                yannick@tedworld.de               \n\n              Thank you for your help!            \n==================================================\n==================================================\n").append(additionalMessage.equals("") ? "" : new StringBuilder().append("\n").append(additionalMessage).toString()).toString());
  }

  public static final void throwArrayIndexOutOfBoundsException()
  {
    throwArrayIndexOutOfBoundsException("");
  }

  public static final void throwArrayIndexOutOfBoundsException(String additionalMessage)
  {
    throw new ArrayIndexOutOfBoundsException(new StringBuilder().append("\n==================================================\n==================================================\n   There occured an error in the BrainStoneMod!   \n   Please send the error log to the developer.    \n   This is important if you want the error to be  \n  fixed. To send the log to me, just send a email \n         with the file of the error log to        \n                yannick@tedworld.de               \n\n              Thank you for your help!            \n==================================================\n==================================================\n").append(additionalMessage.equals("") ? "" : new StringBuilder().append("\n").append(additionalMessage).toString()).toString());
  }

  public static final void throwArrayStoreException()
  {
    throwArrayStoreException("");
  }

  public static final void throwArrayStoreException(String additionalMessage)
  {
    throw new ArrayStoreException(new StringBuilder().append("\n==================================================\n==================================================\n   There occured an error in the BrainStoneMod!   \n   Please send the error log to the developer.    \n   This is important if you want the error to be  \n  fixed. To send the log to me, just send a email \n         with the file of the error log to        \n                yannick@tedworld.de               \n\n              Thank you for your help!            \n==================================================\n==================================================\n").append(additionalMessage.equals("") ? "" : new StringBuilder().append("\n").append(additionalMessage).toString()).toString());
  }

  public static final void throwClassCastException()
  {
    throwClassCastException("");
  }

  public static final void throwClassCastException(String additionalMessage)
  {
    throw new ClassCastException(new StringBuilder().append("\n==================================================\n==================================================\n   There occured an error in the BrainStoneMod!   \n   Please send the error log to the developer.    \n   This is important if you want the error to be  \n  fixed. To send the log to me, just send a email \n         with the file of the error log to        \n                yannick@tedworld.de               \n\n              Thank you for your help!            \n==================================================\n==================================================\n").append(additionalMessage.equals("") ? "" : new StringBuilder().append("\n").append(additionalMessage).toString()).toString());
  }

  public static final void throwClassNotFoundException()
    throws ClassNotFoundException
  {
    throwClassNotFoundException("");
  }

  public static final void throwClassNotFoundException(String additionalMessage)
    throws ClassNotFoundException
  {
    throw new ClassNotFoundException(new StringBuilder().append("\n==================================================\n==================================================\n   There occured an error in the BrainStoneMod!   \n   Please send the error log to the developer.    \n   This is important if you want the error to be  \n  fixed. To send the log to me, just send a email \n         with the file of the error log to        \n                yannick@tedworld.de               \n\n              Thank you for your help!            \n==================================================\n==================================================\n").append(additionalMessage.equals("") ? "" : new StringBuilder().append("\n").append(additionalMessage).toString()).toString());
  }

  public static final void throwCloneNotSupportedException()
    throws CloneNotSupportedException
  {
    throwCloneNotSupportedException("");
  }

  public static final void throwCloneNotSupportedException(String additionalMessage)
    throws CloneNotSupportedException
  {
    throw new CloneNotSupportedException(new StringBuilder().append("\n==================================================\n==================================================\n   There occured an error in the BrainStoneMod!   \n   Please send the error log to the developer.    \n   This is important if you want the error to be  \n  fixed. To send the log to me, just send a email \n         with the file of the error log to        \n                yannick@tedworld.de               \n\n              Thank you for your help!            \n==================================================\n==================================================\n").append(additionalMessage.equals("") ? "" : new StringBuilder().append("\n").append(additionalMessage).toString()).toString());
  }

  public static final void throwException()
    throws Exception
  {
    throwException("");
  }

  public static final void throwException(Throwable exception)
    throws Throwable
  {
    throwException(exception, "");
  }

  public static final void throwException(RuntimeException exception)
  {
    throwException(exception, "");
  }

  public static final void throwException(Throwable exception, String additionalMessage)
    throws Throwable
  {
    throw new Throwable(new StringBuilder().append("(").append(exception.getClass().getName()).append(")").append("\n==================================================\n==================================================\n   There occured an error in the BrainStoneMod!   \n   Please send the error log to the developer.    \n   This is important if you want the error to be  \n  fixed. To send the log to me, just send a email \n         with the file of the error log to        \n                yannick@tedworld.de               \n\n              Thank you for your help!            \n==================================================\n==================================================\n").append(additionalMessage.equals("") ? "" : new StringBuilder().append("\n").append(additionalMessage).append("\n").toString()).append("\nOriginal message: \"").append(exception.getMessage()).append("\"\n").toString(), exception);
  }

  public static final void throwException(RuntimeException exception, String additionalMessage)
  {
    throw new RuntimeException(new StringBuilder().append("(").append(exception.getClass().getName()).append(")").append("\n==================================================\n==================================================\n   There occured an error in the BrainStoneMod!   \n   Please send the error log to the developer.    \n   This is important if you want the error to be  \n  fixed. To send the log to me, just send a email \n         with the file of the error log to        \n                yannick@tedworld.de               \n\n              Thank you for your help!            \n==================================================\n==================================================\n").append(additionalMessage.equals("") ? "" : new StringBuilder().append("\n").append(additionalMessage).append("\n").toString()).append("\nOriginal message: \"").append(exception.getMessage()).append("\"\n").toString(), exception);
  }

  public static final void throwException(String additionalMessage)
    throws Exception
  {
    throw new Exception(new StringBuilder().append("\n==================================================\n==================================================\n   There occured an error in the BrainStoneMod!   \n   Please send the error log to the developer.    \n   This is important if you want the error to be  \n  fixed. To send the log to me, just send a email \n         with the file of the error log to        \n                yannick@tedworld.de               \n\n              Thank you for your help!            \n==================================================\n==================================================\n").append(additionalMessage.equals("") ? "" : new StringBuilder().append("\n").append(additionalMessage).toString()).toString());
  }

  public static final void throwIllegalAccessException()
    throws IllegalAccessException
  {
    throwIllegalAccessException("");
  }

  public static final void throwIllegalAccessException(String additionalMessage)
    throws IllegalAccessException
  {
    throw new IllegalAccessException(new StringBuilder().append("\n==================================================\n==================================================\n   There occured an error in the BrainStoneMod!   \n   Please send the error log to the developer.    \n   This is important if you want the error to be  \n  fixed. To send the log to me, just send a email \n         with the file of the error log to        \n                yannick@tedworld.de               \n\n              Thank you for your help!            \n==================================================\n==================================================\n").append(additionalMessage.equals("") ? "" : new StringBuilder().append("\n").append(additionalMessage).toString()).toString());
  }

  public static final void throwIllegalArgumentException()
  {
    throwIllegalArgumentException("");
  }

  public static final void throwIllegalArgumentException(String additionalMessage)
  {
    throw new IllegalArgumentException(new StringBuilder().append("\n==================================================\n==================================================\n   There occured an error in the BrainStoneMod!   \n   Please send the error log to the developer.    \n   This is important if you want the error to be  \n  fixed. To send the log to me, just send a email \n         with the file of the error log to        \n                yannick@tedworld.de               \n\n              Thank you for your help!            \n==================================================\n==================================================\n").append(additionalMessage.equals("") ? "" : new StringBuilder().append("\n").append(additionalMessage).toString()).toString());
  }

  public static final void throwIllegalMonitorStateException()
  {
    throwIllegalMonitorStateException("");
  }

  public static final void throwIllegalMonitorStateException(String additionalMessage)
  {
    throw new IllegalMonitorStateException(new StringBuilder().append("\n==================================================\n==================================================\n   There occured an error in the BrainStoneMod!   \n   Please send the error log to the developer.    \n   This is important if you want the error to be  \n  fixed. To send the log to me, just send a email \n         with the file of the error log to        \n                yannick@tedworld.de               \n\n              Thank you for your help!            \n==================================================\n==================================================\n").append(additionalMessage.equals("") ? "" : new StringBuilder().append("\n").append(additionalMessage).toString()).toString());
  }

  public static final void throwIllegalThreadStateException()
  {
    throwIllegalThreadStateException("");
  }

  public static final void throwIllegalThreadStateException(String additionalMessage)
  {
    throw new IllegalThreadStateException(new StringBuilder().append("\n==================================================\n==================================================\n   There occured an error in the BrainStoneMod!   \n   Please send the error log to the developer.    \n   This is important if you want the error to be  \n  fixed. To send the log to me, just send a email \n         with the file of the error log to        \n                yannick@tedworld.de               \n\n              Thank you for your help!            \n==================================================\n==================================================\n").append(additionalMessage.equals("") ? "" : new StringBuilder().append("\n").append(additionalMessage).toString()).toString());
  }

  public static final void throwIndexOutOfBoundsException()
  {
    throwIndexOutOfBoundsException("");
  }

  public static final void throwIndexOutOfBoundsException(String additionalMessage)
  {
    throw new IndexOutOfBoundsException(new StringBuilder().append("\n==================================================\n==================================================\n   There occured an error in the BrainStoneMod!   \n   Please send the error log to the developer.    \n   This is important if you want the error to be  \n  fixed. To send the log to me, just send a email \n         with the file of the error log to        \n                yannick@tedworld.de               \n\n              Thank you for your help!            \n==================================================\n==================================================\n").append(additionalMessage.equals("") ? "" : new StringBuilder().append("\n").append(additionalMessage).toString()).toString());
  }

  public static final void throwInstantiationException()
    throws InstantiationException
  {
    throwInstantiationException("");
  }

  public static final void throwInstantiationException(String additionalMessage)
    throws InstantiationException
  {
    throw new InstantiationException(new StringBuilder().append("\n==================================================\n==================================================\n   There occured an error in the BrainStoneMod!   \n   Please send the error log to the developer.    \n   This is important if you want the error to be  \n  fixed. To send the log to me, just send a email \n         with the file of the error log to        \n                yannick@tedworld.de               \n\n              Thank you for your help!            \n==================================================\n==================================================\n").append(additionalMessage.equals("") ? "" : new StringBuilder().append("\n").append(additionalMessage).toString()).toString());
  }

  public static final void throwInterruptedException()
    throws InterruptedException
  {
    throwInterruptedException("");
  }

  public static final void throwInterruptedException(String additionalMessage)
    throws InterruptedException
  {
    throw new InterruptedException(new StringBuilder().append("\n==================================================\n==================================================\n   There occured an error in the BrainStoneMod!   \n   Please send the error log to the developer.    \n   This is important if you want the error to be  \n  fixed. To send the log to me, just send a email \n         with the file of the error log to        \n                yannick@tedworld.de               \n\n              Thank you for your help!            \n==================================================\n==================================================\n").append(additionalMessage.equals("") ? "" : new StringBuilder().append("\n").append(additionalMessage).toString()).toString());
  }

  public static final void throwNegativeArraySizeException()
  {
    throwNegativeArraySizeException("");
  }

  public static final void throwNegativeArraySizeException(String additionalMessage)
  {
    throw new NegativeArraySizeException(new StringBuilder().append("\n==================================================\n==================================================\n   There occured an error in the BrainStoneMod!   \n   Please send the error log to the developer.    \n   This is important if you want the error to be  \n  fixed. To send the log to me, just send a email \n         with the file of the error log to        \n                yannick@tedworld.de               \n\n              Thank you for your help!            \n==================================================\n==================================================\n").append(additionalMessage.equals("") ? "" : new StringBuilder().append("\n").append(additionalMessage).toString()).toString());
  }

  public static final void throwNoSuchMethodException()
    throws NoSuchMethodException
  {
    throwNoSuchMethodException("");
  }

  public static final void throwNoSuchMethodException(String additionalMessage)
    throws NoSuchMethodException
  {
    throw new NoSuchMethodException(new StringBuilder().append("\n==================================================\n==================================================\n   There occured an error in the BrainStoneMod!   \n   Please send the error log to the developer.    \n   This is important if you want the error to be  \n  fixed. To send the log to me, just send a email \n         with the file of the error log to        \n                yannick@tedworld.de               \n\n              Thank you for your help!            \n==================================================\n==================================================\n").append(additionalMessage.equals("") ? "" : new StringBuilder().append("\n").append(additionalMessage).toString()).toString());
  }

  public static final void throwNullPointerException()
  {
    throwNullPointerException("");
  }

  public static final void throwNullPointerException(String additionalMessage)
  {
    throw new NullPointerException(new StringBuilder().append("\n==================================================\n==================================================\n   There occured an error in the BrainStoneMod!   \n   Please send the error log to the developer.    \n   This is important if you want the error to be  \n  fixed. To send the log to me, just send a email \n         with the file of the error log to        \n                yannick@tedworld.de               \n\n              Thank you for your help!            \n==================================================\n==================================================\n").append(additionalMessage.equals("") ? "" : new StringBuilder().append("\n").append(additionalMessage).toString()).toString());
  }

  public static final void throwNumberFormatException()
  {
    throwNumberFormatException("");
  }

  public static final void throwNumberFormatException(String additionalMessage)
  {
    throw new NumberFormatException(new StringBuilder().append("\n==================================================\n==================================================\n   There occured an error in the BrainStoneMod!   \n   Please send the error log to the developer.    \n   This is important if you want the error to be  \n  fixed. To send the log to me, just send a email \n         with the file of the error log to        \n                yannick@tedworld.de               \n\n              Thank you for your help!            \n==================================================\n==================================================\n").append(additionalMessage.equals("") ? "" : new StringBuilder().append("\n").append(additionalMessage).toString()).toString());
  }

  public static final void throwRuntimeException()
  {
    throwRuntimeException("");
  }

  public static final void throwRuntimeException(String additionalMessage)
  {
    throw new RuntimeException(new StringBuilder().append("\n==================================================\n==================================================\n   There occured an error in the BrainStoneMod!   \n   Please send the error log to the developer.    \n   This is important if you want the error to be  \n  fixed. To send the log to me, just send a email \n         with the file of the error log to        \n                yannick@tedworld.de               \n\n              Thank you for your help!            \n==================================================\n==================================================\n").append(additionalMessage.equals("") ? "" : new StringBuilder().append("\n").append(additionalMessage).toString()).toString());
  }

  public static final void throwSecurityException()
  {
    throwSecurityException("");
  }

  public static final void throwSecurityException(String additionalMessage)
  {
    throw new SecurityException(new StringBuilder().append("\n==================================================\n==================================================\n   There occured an error in the BrainStoneMod!   \n   Please send the error log to the developer.    \n   This is important if you want the error to be  \n  fixed. To send the log to me, just send a email \n         with the file of the error log to        \n                yannick@tedworld.de               \n\n              Thank you for your help!            \n==================================================\n==================================================\n").append(additionalMessage.equals("") ? "" : new StringBuilder().append("\n").append(additionalMessage).toString()).toString());
  }

  public static final void throwStringIndexOutOfBoundsException()
  {
    throwStringIndexOutOfBoundsException("");
  }

  public static final void throwStringIndexOutOfBoundsException(String additionalMessage)
  {
    throw new StringIndexOutOfBoundsException(new StringBuilder().append("\n==================================================\n==================================================\n   There occured an error in the BrainStoneMod!   \n   Please send the error log to the developer.    \n   This is important if you want the error to be  \n  fixed. To send the log to me, just send a email \n         with the file of the error log to        \n                yannick@tedworld.de               \n\n              Thank you for your help!            \n==================================================\n==================================================\n").append(additionalMessage.equals("") ? "" : new StringBuilder().append("\n").append(additionalMessage).toString()).toString());
  }

  public static final void throwThrowable()
    throws Throwable
  {
    throwThrowable("");
  }

  public static final void throwThrowable(String additionalMessage)
    throws Throwable
  {
    throw new Throwable(new StringBuilder().append("\n==================================================\n==================================================\n   There occured an error in the BrainStoneMod!   \n   Please send the error log to the developer.    \n   This is important if you want the error to be  \n  fixed. To send the log to me, just send a email \n         with the file of the error log to        \n                yannick@tedworld.de               \n\n              Thank you for your help!            \n==================================================\n==================================================\n").append(additionalMessage.equals("") ? "" : new StringBuilder().append("\n").append(additionalMessage).toString()).toString());
  }
}