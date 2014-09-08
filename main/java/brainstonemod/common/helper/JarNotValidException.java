package brainstonemod.common.helper;

public class JarNotValidException extends RuntimeException {
	public JarNotValidException() {
		super("The verification of the jar file of the BrainStoneMod failed. The hash of this file and the verification hash did not match!");
	}
}