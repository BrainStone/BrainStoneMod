package mods.brainstonemod.tileentities;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import mods.brainstonemod.guis.GuiBrainLogicBlock;
import mods.brainstonemod.handlers.BrainStonePacketHandler;
import mods.brainstonemod.templates.BSP;
import mods.brainstonemod.templates.TileEntityBrainStoneSyncBase;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class TileEntityBlockBrainLogicBlock extends
		TileEntityBrainStoneSyncBase {
	public static String getGateName(int i) {
		switch (i) {
		case 0:
			return "AND-Gate";

		case 1:
			return "OR-Gate";

		case 2:
			return "XOR-Gate";

		case 3:
			return "Implies-Gate";

		case 4:
			return "NOT-Gate";

		case 5:
			return "RS-NOR-Latch";

		case 6:
			return "D-Flip-Flop";

		case 7:
			return "T-Flip-Flop";

		case 8:
			return "JK-Flip-Flop";
		}

		return "";
	}

	private static final byte getNumGates() {
		byte byte0;

		for (byte0 = 0; getGateName(byte0) != ""; byte0++) {
		}

		return byte0;
	}

	private boolean correctConnect;
	private boolean ignoreIncorrectConnect;
	private boolean invertOutput;
	private boolean swapable;
	private byte direction;
	private byte GuiFocused;
	private byte mode;
	private final byte PinState[] = { 0, -1, -1, -1 };
	private byte PinType[];
	private int data;
	private long lastUpdate;
	private String Pins[] = { "0", "1", "2", "3" };
	private final Vector TASKS;
	private final Vector Users;
	private ArrayList<String> PrintErrorBuff;

	private boolean PrintErrorBuffActive;

	public static final byte numGates = getNumGates();

	public static String getInvName() {
		return "container.brainstonetrigger";
	}

	public TileEntityBlockBrainLogicBlock() {
		this((byte) 0);
	}

	public TileEntityBlockBrainLogicBlock(byte byte0) {
		TASKS = new Vector();
		mode = 0;
		data = 0;
		lastUpdate = -100L;
		GuiFocused = 0;
		Users = new Vector();
		PrintErrorBuffActive = false;

		if ((byte0 >= 0) && (byte0 < 4)) {
			direction = byte0;
		}

		this.modeUpdated();
	}

	public void addTASKS(String s) {
		final Side side = FMLCommonHandler.instance().getEffectiveSide();
		if (side == Side.SERVER) {
			// We are on the server side.
			TASKS.add(s);
		} else if (side == Side.CLIENT) {
			// We are on the client side.
			final ByteArrayOutputStream bos = new ByteArrayOutputStream(0);
			final DataOutputStream outputStream = new DataOutputStream(bos);

			try {
				outputStream.writeInt(xCoord);
				outputStream.writeInt(yCoord);
				outputStream.writeInt(zCoord);

				outputStream.writeUTF(s);
			} catch (final IOException e) {
				BSP.printException(e);
			}

			BrainStonePacketHandler.sendPacketToServer("BSM.TEBBLBS", bos);
		} else {
			// We are on the Bukkit server. Bukkit is a server mod used for
			// security.
		}
	}

	public void addTASKS(String s, String as[]) {
		String s1 = (new StringBuilder()).append(s).append(" ").toString();
		final int i = as.length - 1;

		for (int j = 0; j < i; j++) {
			s1 = (new StringBuilder()).append(s1).append(as[j]).append(",")
					.toString();
		}

		s1 = (new StringBuilder()).append(s1).append(as[i]).toString();

		this.addTASKS(s1);
	}

	public boolean connectToRedstone(int side) {
		return PinType[this.convertRedstoneDirections(side)] != -1;
	}

	private int convertRedstoneDirections(int side) {
		/*
		 * switch(direction & 1) { case 0:
		 */
		switch ((side + (((direction & 1) == 1) ? (direction ^ 2) : direction)) % 4) {
		case 0:
			return 0;

		case 1:
			return 3;

		case 2:
			return 1;

		case 3:
			return 2;
		}

		return 0;

		/*
		 * case 1: switch((side + direction) % 4) { case 0: return 1;
		 * 
		 * case 1: return 2;
		 * 
		 * case 2: return 0;
		 * 
		 * case 3: return 3; }
		 * 
		 * return 0; }
		 * 
		 * return 0;
		 */
	}

	public void doTASKS() {
		final int i = TASKS.size();

		if (i != 0) {
			this.print((new StringBuilder()).append("doTASKS() (size: ")
					.append(String.valueOf(i)).append(")").toString());

			for (int j = 0; j < i; j++) {
				this.runTASK((String) TASKS.get(j));
			}

			TASKS.clear();
		}

		try {
			this.update(false);
		} catch (final IOException e) {
			BSP.printException(e);
		}
	}

	public void drawBoxes(GuiBrainLogicBlock guibrainlogicblock, int i, int j) {
		byte byte0 = 0;

		if (PinState[0] != 0) {
			guibrainlogicblock.drawTexturedModalRect(i + 20, j + 40, 176, 0,
					20, 20);
		}

		byte byte1 = PinState[1];
		byte byte2 = PinType[1];

		if ((byte1 != 0) || (byte2 == -1)) {
			if (byte2 != -1) {
				if (byte1 == 1) {
					byte0 = 0;
				}

				if (byte1 == -1) {
					byte0 = 1;
				}
			} else {
				byte0 = 2;
			}

			guibrainlogicblock.drawTexturedModalRect(i + 20, j, 176,
					20 * byte0, 20, 20);
		}

		byte1 = PinState[2];
		byte2 = PinType[2];

		if ((byte1 != 0) || (byte2 == -1)) {
			if (byte2 != -1) {
				if (byte1 == 1) {
					byte0 = 0;
				}

				if (byte1 == -1) {
					byte0 = 1;
				}
			} else {
				byte0 = 2;
			}

			guibrainlogicblock.drawTexturedModalRect(i + 40, j + 20, 176,
					20 * byte0, 20, 20);
		}

		byte1 = PinState[3];
		byte2 = PinType[3];

		if ((byte1 != 0) || (byte2 == -1)) {
			if (byte2 != -1) {
				if (byte1 == 1) {
					byte0 = 0;
				}

				if (byte1 == -1) {
					byte0 = 1;
				}
			} else {
				byte0 = 2;
			}

			guibrainlogicblock.drawTexturedModalRect(i, j + 20, 176,
					20 * byte0, 20, 20);
		}
	}

	public void drawGates(GuiBrainLogicBlock guibrainlogicblock, int i, int j) {
		for (int k = 0; k < numGates; k++) {
			guibrainlogicblock.drawString(getGateName(k), i, j + (12 * k),
					k != mode ? 0x404040 : 32768);
		}

		guibrainlogicblock.drawSplitString(
				StatCollector.translateToLocal("gui.brainstone.invertOutput"),
				i + 85, j + 50, invertOutput ? 32768 : 0x404040, 80);

		if (!correctConnect && !ignoreIncorrectConnect) {
			guibrainlogicblock.drawSplitString(
					StatCollector.translateToLocal("gui.brainstone.warning1"),
					i + 75, j + 70, 0xa00000, 95);
		} else if (!correctConnect) {
			guibrainlogicblock.drawSplitString(
					StatCollector.translateToLocal("gui.brainstone.warning2"),
					i + 75, j + 70, 0xa05000, 95);
		}
	}

	@Override
	protected void generateOutputStream(DataOutputStream outputStream)
			throws IOException {
		outputStream.writeInt(xCoord);
		outputStream.writeInt(yCoord);
		outputStream.writeInt(zCoord);

		outputStream.writeBoolean(correctConnect);
		outputStream.writeBoolean(ignoreIncorrectConnect);
		outputStream.writeBoolean(invertOutput);
		outputStream.writeBoolean(swapable);
		outputStream.writeByte(direction);
		outputStream.writeByte(GuiFocused);
		outputStream.writeByte(mode);

		int size = PinState.length;
		outputStream.writeInt(size);
		for (int i = 0; i < size; i++) {
			outputStream.writeByte(PinState[i]);
		}

		size = PinType.length;
		outputStream.writeInt(size);
		for (int i = 0; i < size; i++) {
			outputStream.writeByte(PinType[i]);
		}

		outputStream.writeInt(data);
		outputStream.writeLong(lastUpdate);

		size = Pins.length;
		outputStream.writeInt(size);
		for (int i = 0; i < size; i++) {
			outputStream.writeUTF(Pins[i]);
		}
	}

	public byte getDirection() {
		return direction;
	}

	public byte getFocused() {
		return GuiFocused;
	}

	public byte getMode() {
		return mode;
	}

	public boolean getOutput() {
		return PinState[0] == 1;
	}

	public String getPin(int i) {
		if ((i < 0) || (i >= 4))
			return "";
		else
			return Pins[i];
	}

	public int getPinColor(int i) {
		if ((i < 0) || (i >= 4))
			return 0;
		else {
			final byte byte0 = PinState[i];
			return (byte0 != -1) ? ((byte0 != 0) ? 0xc60505 : 0x50505)
					: 0x888888;
		}
	}

	public int getPinStateBasedTextureIndex(int i) {
		if ((i < 0) || (i >= PinState.length))
			return 0;

		if (PinType[i] == -1)
			return 3;
		else {
			final byte byte0 = PinState[i];
			return (byte0 != -1) ? ((byte0 != 0) ? 2 : 1) : 0;
		}
	}

	private String getPrintErrorBuff() {
		if (!PrintErrorBuffActive)
			return "";

		String tmp = "";
		final int size = PrintErrorBuff.size();

		for (int i = 0; i < size; i++) {
			tmp += PrintErrorBuff.get(i) + "\n";
		}

		tmp += "\n";

		return tmp;
	}

	public void invertInvertOutput() {
		this.addTASKS((new StringBuilder()).append("setInvertOutput ")
				.append(String.valueOf(!invertOutput)).toString());
	}

	public boolean isCorrectConnected() {
		return correctConnect;
	}

	public boolean isSwapable() {
		return swapable;
	}

	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		if (worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this)
			return false;
		else
			return entityplayer.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D,
					zCoord + 0.5D) <= 64D;
	}

	public void logIn(String user) {
		this.addTASKS("logInOut", new String[] { "true", user });
	}

	public void logOut(String user) {
		this.addTASKS("logInOut", new String[] { "false", user });
	}

	private void modeUpdated() {
		swapable = true;
		ignoreIncorrectConnect = false;
		invertOutput = false;

		switch (mode) {
		case 0:
		case 1:
		case 2:
			swapable = false;
			Pins = (new String[] { "Q", "B", "A", "C" });
			PinType = (new byte[] { 10, 0, 0, 0 });
			break;

		case 3:
			Pins = (new String[] { "Q", "", "A", "B" });
			PinType = (new byte[] { 10, -1, 1, 1 });
			break;

		case 4:
			ignoreIncorrectConnect = true;
			Pins = (new String[] { "Q", "A", "", "" });
			PinType = (new byte[] { 10, 1, -1, -1 });
			break;

		case 5:
			data = 0;
			ignoreIncorrectConnect = true;
			Pins = (new String[] { "Q", "", "R", "S" });
			PinType = (new byte[] { 10, -1, 1, 1 });
			break;

		case 6:
			data = 0;
			Pins = (new String[] { "Q", "", "D", "C" });
			PinType = (new byte[] { 10, -1, 1, 1 });
			break;

		case 7:
			data = 0;
			Pins = (new String[] { "Q", "T", "", "" });
			PinType = (new byte[] { 10, 1, -1, -1 });
			break;

		case 8:
			data = 0;
			ignoreIncorrectConnect = true;
			Pins = (new String[] { "Q", "", "J", "K" });
			PinType = (new byte[] { 10, -1, 1, 1 });
			break;
		}
	}

	private void modeUpdated(byte byte0) {
		mode = byte0;
		this.modeUpdated();
	}

	private void print(Object obj) {
		if (!BSP.print(obj) && PrintErrorBuffActive) {
			PrintErrorBuff.add(obj.toString());
		}
	}

	private void printErrorInrunTASK(String s, String s1, String as[]) {
		this.print("===================================================");
		this.print("Error Signature in \"runTASK\":");
		this.print("===================================================");
		this.print((new StringBuilder()).append("\tOriginal Task: \"")
				.append(s).append("\"").toString());
		this.print((new StringBuilder()).append("\tCutted Task:   \"")
				.append(s1).append("\"").toString());
		this.print("\tParameters:");
		final int i = as.length;

		if (i == 0) {
			this.print("\t\tNo Parameters!!!");
		} else {
			for (int j = 0; j < i; j++) {
				this.print((new StringBuilder()).append("\t\tParameter ")
						.append(String.valueOf(j)).append(": \"").append(as[j])
						.append("\"").toString());
			}
		}

		this.print("===================================================");
		this.print("!!!\t\t\tEND\t\t\t!!!");
		this.print("===================================================");
	}

	@Override
	public void readFromInputStream(DataInputStream inputStream)
			throws IOException {
		correctConnect = inputStream.readBoolean();
		ignoreIncorrectConnect = inputStream.readBoolean();
		invertOutput = inputStream.readBoolean();
		swapable = inputStream.readBoolean();
		direction = inputStream.readByte();
		GuiFocused = inputStream.readByte();
		mode = inputStream.readByte();

		int size = inputStream.readInt();
		for (int i = 0; i < size; i++) {
			PinState[i] = inputStream.readByte();
		}

		size = inputStream.readInt();
		for (int i = 0; i < size; i++) {
			PinType[i] = inputStream.readByte();
		}

		data = inputStream.readInt();
		lastUpdate = inputStream.readLong();

		size = inputStream.readInt();
		for (int i = 0; i < size; i++) {
			Pins[i] = inputStream.readUTF();
		}
	}

	/**
	 * Reads a tile entity from NBT.
	 */
	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		direction = nbttagcompound.getByte("Direction");
		mode = nbttagcompound.getByte("Mode");
		data = nbttagcompound.getInteger("Data");
		invertOutput = nbttagcompound.getBoolean("InvertOutput");
		swapable = nbttagcompound.getBoolean("Swapable");

		for (int i = 0; i < 4; i++) {
			PinState[i] = nbttagcompound.getByte((new StringBuilder())
					.append("State").append(String.valueOf(i)).toString());
			PinType[i] = nbttagcompound.getByte((new StringBuilder())
					.append("Type").append(String.valueOf(i)).toString());
			Pins[i] = nbttagcompound.getString((new StringBuilder())
					.append("Pins").append(String.valueOf(i)).toString());
		}

		final byte byte0 = nbttagcompound.getByte("TASKS-Size");

		for (byte byte1 = 0; byte1 < byte0; byte1++) {
			TASKS.add(
					byte1,
					nbttagcompound.getString((new StringBuilder())
							.append("TASK").append(String.valueOf(byte1))
							.toString()));
		}
	}

	public void renderInOutPut(FontRenderer fontrenderer, byte byte0) {
		byte0 = this.transformDirection(byte0);

		if (PinType[byte0] != -1) {
			final byte byte1 = PinState[byte0];
			fontrenderer.drawString(Pins[byte0],
					-fontrenderer.getStringWidth(Pins[byte0]) / 2, 4,
					byte1 != -1 ? byte1 != 0 ? 0xc60505 : 0x50505 : 0x888888);
		}
	}

	public byte reverseTransformDirection(int i) {
		switch (direction) {
		default:
			break;

		case 1:
			switch (i) {
			case 2:
				i = 0;
				break;

			case 3:
				i = 1;
				break;

			case 1:
				i = 2;
				break;

			case 0:
				i = 3;
				break;
			}

			break;

		case 2:
			switch (i) {
			case 1:
				i = 0;
				break;

			case 0:
				i = 1;
				break;

			case 3:
				i = 2;
				break;

			case 2:
				i = 3;
				break;
			}

			break;

		case 3:
			switch (i) {
			case 3:
				i = 0;
				break;

			case 2:
				i = 1;
				break;

			case 0:
				i = 2;
				break;

			case 1:
				i = 3;
				break;
			}

			break;
		}

		return (byte) i;
	}

	private void runTASK(String s) {
		this.startPrintErrorBuff();

		String as[] = s.split(" ");
		final String s1 = as[0];
		as = as[1].split(",");

		if (s1.equals("swapPosition")) {
			this.print("!!!\tRun Task: \"swapPosition\"\t\t!!!");

			if (as.length == 2) {
				this.swapPosition(Byte.valueOf(as[0]).byteValue(), Byte
						.valueOf(as[1]).byteValue());
			} else {
				this.print("!!!\tError: Wrong number of parameters\t!!!\n");
				this.printErrorInrunTASK(s, s1, as);
				BSP.throwRuntimeException(this.getPrintErrorBuff()
						+ "Wrong number of parameters");
			}
		} else if (s1.equals("modeUpdated")) {
			this.print("!!!\tRun Task: \"modeUpdated\"\t\t\t!!!");

			if (as.length == 1) {
				this.modeUpdated(Byte.valueOf(as[0]).byteValue());
			} else {
				this.print("!!!\tError: Wrong number of parameters\t!!!\n");
				this.printErrorInrunTASK(s, s1, as);
				BSP.throwRuntimeException(this.getPrintErrorBuff()
						+ "Wrong number of parameters");
			}
		} else if (s1.equals("setFocused")) {
			this.print("!!!\tRun Task: \"setFocused\"\t\t\t!!!");

			if (as.length == 1) {
				this.setFocused(Byte.valueOf(as[0]).byteValue());
			} else {
				this.print("!!!\tError: Wrong number of parameters\t!!!\n");
				this.printErrorInrunTASK(s, s1, as);
				BSP.throwRuntimeException(this.getPrintErrorBuff()
						+ "Wrong number of parameters");
			}
		} else if (s1.equals("setInvertOutput")) {
			this.print("!!!\tRun Task: \"setInvertOutput\"\t\t!!!");

			if (as.length == 1) {
				this.setInvertOutput(Boolean.valueOf(as[0]).booleanValue());
			} else {
				this.print("!!!\tError: Wrong number of parameters\t!!!\n");
				this.printErrorInrunTASK(s, s1, as);
				BSP.throwRuntimeException(this.getPrintErrorBuff()
						+ "Wrong number of parameters");
			}
		} else if (s1.equals("logInOut")) {
			this.print("!!!\tRun Task: \"logInOut\"\t\t!!!");

			if (as.length == 2) {
				if (Boolean.valueOf(as[0]).booleanValue()) {
					if (!Users.contains(as[1])) {
						Users.add(as[1]);
					} else {
						this.print("User \"" + as[1]
								+ "\" is already logged in!");
					}
				} else {
					if (Users.contains(as[1])) {
						Users.remove(as[1]);
					} else {
						this.print("User \"" + as[1]
								+ "\" is already logged out!");
					}
				}

				if (Users.size() == 0) {
					this.setFocused(0);
				}
			} else {
				this.print("!!!\tError: Wrong number of parameters\t!!!\n");
				this.printErrorInrunTASK(s, s1, as);
				BSP.throwRuntimeException(this.getPrintErrorBuff()
						+ "Wrong number of parameters");
			}
		} else {
			this.print("!!!\t\tInvalid Task\t\t\t!!!\n");
			this.printErrorInrunTASK(s, s1, as);
			BSP.throwRuntimeException(this.getPrintErrorBuff() + "Invalid Task");
		}

		this.print("!!!\t\tTask Finished\t\t\t!!!\n");

		this.stopPrintErrorBuff();
	}

	public void setDirection(byte byte0) {
		if ((byte0 >= 0) && (byte0 < 4)) {
			direction = byte0;
		}
	}

	public void setDirection(int i) {
		this.setDirection((byte) i);
	}

	public void setFocused(byte byte0) {
		final Side side = FMLCommonHandler.instance().getEffectiveSide();
		if (side == Side.SERVER) {
			// We are on the server side.
			if ((byte0 >= 0) && (byte0 < 4)) {
				GuiFocused = byte0;
			}
		} else if (side == Side.CLIENT) {
			// We are on the client side.

			this.addTASKS("setFocused", new String[] { String.valueOf(byte0) });
		} else {
			// We are on the Bukkit server. Bukkit is a server mod used for
			// security.
		}
	}

	public void setFocused(int i) {
		this.setFocused((byte) i);
	}

	private void setInvertOutput(boolean flag) {
		invertOutput = flag;
	}

	public void setMode(byte byte0) {
		this.addTASKS((new StringBuilder()).append("modeUpdated ")
				.append(String.valueOf(byte0)).toString());
	}

	private byte setOutput() {
		boolean flag = false;
		correctConnect = true;

		for (int i = 1; i < 4; i++) {
			correctConnect = correctConnect
					&& ((PinType[i] != 1) || ((PinType[i] == 1) && (PinState[i] != -1)));
		}

		correctConnect = correctConnect
				&& ((PinState[1] != -1) || (PinState[2] != -1) || (PinState[3] != -1));

		if (correctConnect || ignoreIncorrectConnect) {
			final ArrayList arraylist = new ArrayList();
			final HashMap hashmap = new HashMap();

			for (int j = 1; j < 4; j++) {
				if ((PinState[j] != -1) && (PinType[j] != -1)) {
					boolean flag1;
					arraylist.add(Boolean.valueOf(flag1 = PinState[j] == 1));
					hashmap.put(Pins[j], Boolean.valueOf(flag1));
				}

				if ((PinState[j] == -1) && ignoreIncorrectConnect
						&& !hashmap.containsKey(Pins[j])) {
					hashmap.put(Pins[j], Boolean.valueOf(PinState[j] == 1));
				}
			}

			final int k = arraylist.size();

			switch (mode) {
			default:
				break;

			case 0:
				flag = k != 0;

				for (int l = 0; l < k; l++) {
					flag = flag && ((Boolean) arraylist.get(l)).booleanValue();
				}

				break;

			case 1:
				for (int i1 = 0; i1 < k; i1++) {
					flag = flag || ((Boolean) arraylist.get(i1)).booleanValue();
				}

				break;

			case 2:
				for (int j1 = 0; j1 < k; j1++) {
					flag ^= ((Boolean) arraylist.get(j1)).booleanValue();
				}

				break;

			case 3:
				if (k == 2) {
					flag = !((Boolean) arraylist.get(0)).booleanValue()
							|| ((Boolean) arraylist.get(1)).booleanValue();
				}

				break;

			case 4:
				flag = !((Boolean) hashmap.get("A")).booleanValue();
				break;

			case 5:
				if (((Boolean) hashmap.get("R")).booleanValue()) {
					data = 0;
					flag = false;
					break;
				}

				if (((Boolean) hashmap.get("S")).booleanValue()) {
					data = 1;
					flag = true;
				} else {
					flag = data == 1;
				}

				break;

			case 6:
				if (((Boolean) hashmap.get("C")).booleanValue()) {
					data = (flag = ((Boolean) hashmap.get("D")).booleanValue()) ? 1
							: 0;
				} else {
					flag = data == 1;
				}

				break;

			case 7:
				if (((Boolean) hashmap.get("T")).booleanValue()) {
					if ((data & 2) != 2) {
						data = (data ^ 1) | 2;
					}
				} else {
					data = data & 1;
				}

				flag = (data & 1) == 1;
				break;

			case 8:
				data = (data & 1) << 1;
				final boolean flag2 = ((Boolean) hashmap.get("J"))
						.booleanValue();
				final boolean flag3 = ((Boolean) hashmap.get("K"))
						.booleanValue();

				if (!flag2 && !flag3) {
					data = data | (data >> 1);
				} else if (flag2 && flag3) {
					data = data | ((data >> 1) ^ 1);
				} else if (flag2) {
					data = data | 1;
				}

				flag = (data & 1) == 1;
				break;
			}
		}

		return (byte) (flag ^ invertOutput ? 1 : 0);
	}

	public void setPinState(byte abyte0[]) {
		if (abyte0.length == 3) {
			System.arraycopy(abyte0, 0, PinState, 1, 3);
			PinState[0] = this.setOutput();
		}
	}

	public boolean shallDoUpdate(long l) {
		if (l > (lastUpdate + 1)) {
			lastUpdate = l;
			return true;
		} else
			return false;
	}

	public boolean[] shallRender() {
		final boolean aflag[] = new boolean[4];

		for (int i = 0; i < 4; i++) {
			aflag[i] = PinType[i] != -1;
		}

		return aflag;
	}

	private void startPrintErrorBuff() {
		PrintErrorBuffActive = true;
		PrintErrorBuff = new ArrayList<String>();
	}

	private void stopPrintErrorBuff() {
		PrintErrorBuffActive = false;
		PrintErrorBuff = null;
	}

	private void swapPosition(byte byte0, byte byte1) {
		if ((byte0 > 0) && (byte0 < 4) && (byte1 > 0) && (byte1 < 4)
				&& (byte0 != byte1) && swapable) {
			final byte byte2 = PinType[byte0];
			final String s = Pins[byte0];
			PinType[byte0] = PinType[byte1];
			Pins[byte0] = Pins[byte1];
			PinType[byte1] = byte2;
			Pins[byte1] = s;
			GuiFocused = byte1;
		}
	}

	public void swapPosition(int i, int j) {
		this.addTASKS((new StringBuilder()).append("swapPosition ")
				.append(String.valueOf((byte) i)).append(",")
				.append(String.valueOf((byte) j)).toString());
	}

	public byte transformDirection(int i) {
		switch (direction) {
		default:
			break;

		case 1:
			switch (i) {
			case 0:
				i = 2;
				break;

			case 1:
				i = 3;
				break;

			case 2:
				i = 1;
				break;

			case 3:
				i = 0;
				break;
			}

			break;

		case 2:
			switch (i) {
			case 0:
				i = 1;
				break;

			case 1:
				i = 0;
				break;

			case 2:
				i = 3;
				break;

			case 3:
				i = 2;
				break;
			}

			break;

		case 3:
			switch (i) {
			case 0:
				i = 3;
				break;

			case 1:
				i = 2;
				break;

			case 2:
				i = 0;
				break;

			case 3:
				i = 1;
				break;
			}

			break;
		}

		return (byte) i;
	}

	@Override
	public void update(boolean sendToServer) throws IOException {
		final ByteArrayOutputStream bos = new ByteArrayOutputStream(0);
		final DataOutputStream outputStream = new DataOutputStream(bos);

		this.generateOutputStream(outputStream);

		if (sendToServer) {
			BrainStonePacketHandler.sendPacketToServer("BSM.TEBBLBS", bos);
		} else {
			BrainStonePacketHandler.sendPacketToClosestPlayers(this,
					"BSM.TEBBLBC", bos);
		}
	}

	/**
	 * Writes a tile entity to NBT.
	 */
	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setByte("Direction", direction);
		nbttagcompound.setByte("Mode", mode);
		nbttagcompound.setInteger("Data", data);
		nbttagcompound.setBoolean("InvertOutput", invertOutput);
		nbttagcompound.setBoolean("Swapable", swapable);

		for (int i = 0; i < 4; i++) {
			nbttagcompound.setByte((new StringBuilder()).append("State")
					.append(String.valueOf(i)).toString(), PinState[i]);
			nbttagcompound.setByte(
					(new StringBuilder()).append("Type")
							.append(String.valueOf(i)).toString(), PinType[i]);
			nbttagcompound.setString((new StringBuilder()).append("Pins")
					.append(String.valueOf(i)).toString(), Pins[i]);
		}

		final byte byte0 = (byte) TASKS.size();
		nbttagcompound.setByte("TASKS-Size", byte0);

		for (byte byte1 = 0; byte1 < byte0; byte1++) {
			nbttagcompound.setString((new StringBuilder()).append("TASK")
					.append(String.valueOf(byte1)).toString(),
					(String) TASKS.get(byte1));
		}
	}
}