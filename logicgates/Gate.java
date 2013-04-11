package mods.brainstone.logicgates;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public abstract class Gate {
	public Pin[] Pins = new Pin[6];
	public Option[] Options;
	public final long ID = getID(this.getClass().getSimpleName());
	public final String Name = this.getClass().getSimpleName();
	protected int tickRate;
	
	/**
	 * Statically generates a (usually) unique ID from a String.<br>
	 * <small>The chance that it is NOT unique is about 1:18,446,744,073,709,551,616 since there are so many possibilities!</small>
	 * @param Name
	 * @return
	 */
	public static long getID(String Name) {
		MessageDigest md5;
		byte[] result;
		try {
			md5 = MessageDigest.getInstance("MD5");
			md5.reset();
	        md5.update(Name.getBytes());
	        result = md5.digest();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return 0;
		}
		
		long output = 0;
		int i = 0;
		
		for(byte tmp : result) {
			output ^= ((long) tmp) << ((i % 8) * 8);
			i++;
		}
		
		return output;
	}
}
