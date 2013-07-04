package mods.brainstonemod.templates;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.tileentity.TileEntity;

public abstract class TileEntityBrainStoneSyncBase extends TileEntity {
	/**
	 * This function must be overwritten and should write all variables
	 * necessary to the parameter <b>outputStream</b>.<br>
	 * <br>
	 * <u><b>Including</b> the coordinates in the order:</u>
	 * <ul>
	 * <li>X</li>
	 * <li>Y</li>
	 * <li>Z</li>
	 * </ul>
	 * 
	 * @param outputStream
	 * @throws IOException
	 */
	protected abstract void generateOutputStream(DataOutputStream outputStream)
			throws IOException;

	/**
	 * This function must be overwritten and should read all variables necessary
	 * from the parameter <b>inputStream</b>.<br>
	 * <br>
	 * <u></b>Excluding</b> the coordinates in the order:</u>
	 * 
	 * @param inputStream
	 * @throws IOException
	 */
	public abstract void readFromInputStream(DataInputStream inputStream)
			throws IOException;

	/**
	 * This function must be overwritten and actually synchronizes the
	 * tileentities.
	 * 
	 * @param sendToServer
	 *            - This decides whether it should synchronize the server with
	 *            the client or the other way round.<br>
	 *            <table>
	 *            <tr>
	 *            <td><b>true:</b></td>
	 *            <td>Client synchronizes Server and then Server synchronizes
	 *            all Clients</td>
	 *            </tr>
	 *            <tr>
	 *            <td><b>false:</b></td>
	 *            <td>Server synchronizes Client</td>
	 *            </tr>
	 *            </table>
	 * @throws IOException
	 */
	public abstract void update(boolean sendToServer) throws IOException;
}