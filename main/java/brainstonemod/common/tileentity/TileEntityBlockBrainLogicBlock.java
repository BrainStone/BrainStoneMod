package brainstonemod.common.tileentity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;
import java.util.Vector;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.StatCollector;
import brainstonemod.client.gui.GuiBrainLogicBlock;
import brainstonemod.common.helper.BSP;
import brainstonemod.common.tileentity.template.TileEntityBrainStoneSyncBase;
import brainstonemod.network.BrainStonePacketHelper;
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
		byte count;

		for (count = 0; getGateName(count) != ""; count++) {
		}

		return count;
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
	private final Vector<String> TASKS;
	private final Vector<UUID> Users;
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
		TASKS = new Vector<String>();
		mode = 0;
		data = 0;
		lastUpdate = -100L;
		GuiFocused = 0;
		Users = new Vector<UUID>();
		PrintErrorBuffActive = false;

		if ((byte0 >= 0) && (byte0 < 4)) {
			direction = byte0;
		}

		this.modeUpdated();
	}

	public void addTASK(String task) {
		TASKS.add(task);

		BrainStonePacketHelper.sendUpdateTileEntityPacket(this);
	}

	public void addTASK(String mainTask, String taskParameter[]) {
		String task = (new StringBuilder()).append(mainTask).append(" ")
				.toString();
		final int i = taskParameter.length - 1;

		for (int j = 0; j < i; j++) {
			task = (new StringBuilder()).append(task).append(taskParameter[j])
					.append(",").toString();
		}

		task = (new StringBuilder()).append(task).append(taskParameter[i])
				.toString();

		this.addTASK(task);
	}

	public boolean connectToRedstone(int side) {
		return PinType[convertRedstoneDirections(side)] != -1;
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
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			final int size = TASKS.size();

			if (size != 0) {
				print((new StringBuilder()).append("doTASKS() (size: ")
						.append(String.valueOf(size)).append(")").toString());

				for (int j = 0; j < size; j++) {
					runTASK(TASKS.get(j));
				}

				TASKS.clear();

				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			}
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

	public void drawGates(GuiBrainLogicBlock guibrainlogicblock, int x, int y) {
		for (int k = 0; k < numGates; k++) {
			guibrainlogicblock.drawString(getGateName(k), x, y + (12 * k),
					k != mode ? 0x404040 : 32768);
		}

		guibrainlogicblock.drawSplitString(
				StatCollector.translateToLocal("gui.brainstone.invertOutput"),
				x + 85, y + 50, invertOutput ? 32768 : 0x404040, 80);

		if (!correctConnect && !ignoreIncorrectConnect) {
			guibrainlogicblock.drawSplitString(
					StatCollector.translateToLocal("gui.brainstone.warning1"),
					x + 75, y + 70, 0xa00000, 95);
		} else if (!correctConnect) {
			guibrainlogicblock.drawSplitString(
					StatCollector.translateToLocal("gui.brainstone.warning2"),
					x + 75, y + 70, 0xa05000, 95);
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
		this.addTASK((new StringBuilder()).append("setInvertOutput ")
				.append(String.valueOf(!invertOutput)).toString());
	}

	public boolean isCorrectConnected() {
		return correctConnect;
	}

	public boolean isSwapable() {
		return swapable;
	}

	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		if (worldObj.getTileEntity(xCoord, yCoord, zCoord) != this)
			return false;
		else
			return entityplayer.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D,
					zCoord + 0.5D) <= 64D;
	}

	public void logIn(UUID user) {
		this.addTASK("logInOut", new String[] { "true", user.toString() });
	}

	public void logOut(UUID user) {
		this.addTASK("logInOut", new String[] { "false", user.toString() });
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

	private void modeUpdated(byte mode) {
		this.mode = mode;
		this.modeUpdated();
	}

	@Override
	public void onDataPacket(NetworkManager net,
			S35PacketUpdateTileEntity packet) {
		super.onDataPacket(net, packet);

		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
			worldObj.markBlockRangeForRenderUpdate(xCoord, yCoord, zCoord,
					xCoord, yCoord, zCoord);
		}
	}

	private void print(Object obj) {
		if (!BSP.debug(obj) && PrintErrorBuffActive) {
			PrintErrorBuff.add(obj.toString());
		}
	}

	private void printErrorInrunTASK(String s, String s1, String as[]) {
		print("===================================================");
		print("Error Signature in \"runTASK\":");
		print("===================================================");
		print((new StringBuilder()).append("\tOriginal Task: \"").append(s)
				.append("\"").toString());
		print((new StringBuilder()).append("\tCutted Task:   \"").append(s1)
				.append("\"").toString());
		print("\tParameters:");
		final int i = as.length;

		if (i == 0) {
			print("\t\tNo Parameters!!!");
		} else {
			for (int j = 0; j < i; j++) {
				print((new StringBuilder()).append("\t\tParameter ")
						.append(String.valueOf(j)).append(": \"").append(as[j])
						.append("\"").toString());
			}
		}

		print("===================================================");
		print("!!!\t\t\tEND\t\t\t!!!");
		print("===================================================");
	}

	/**
	 * Reads a tile entity from NBT.
	 */
	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		direction = nbttagcompound.getByte("Direction");
		mode = nbttagcompound.getByte("Mode");
		GuiFocused = nbttagcompound.getByte("GuiFocused");
		data = nbttagcompound.getInteger("Data");
		invertOutput = nbttagcompound.getBoolean("InvertOutput");
		swapable = nbttagcompound.getBoolean("Swapable");
		correctConnect = nbttagcompound.getBoolean("CorrectConnect");
		ignoreIncorrectConnect = nbttagcompound
				.getBoolean("IgnoreIncorrectConnect");

		for (int i = 0; i < 4; i++) {
			PinState[i] = nbttagcompound.getByte((new StringBuilder())
					.append("State").append(String.valueOf(i)).toString());
			PinType[i] = nbttagcompound.getByte((new StringBuilder())
					.append("Type").append(String.valueOf(i)).toString());
			Pins[i] = nbttagcompound.getString((new StringBuilder())
					.append("Pins").append(String.valueOf(i)).toString());
		}

		final byte size = nbttagcompound.getByte("TASKS-Size");
		TASKS.clear();
		TASKS.ensureCapacity(size);

		for (byte byte1 = 0; byte1 < size; byte1++) {
			TASKS.add(
					byte1,
					nbttagcompound.getString((new StringBuilder())
							.append("TASK").append(String.valueOf(byte1))
							.toString()));
		}
	}

	public void renderInOutPut(FontRenderer fontrenderer, byte byte0) {
		byte0 = transformDirection(byte0);

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
		startPrintErrorBuff();

		String as[] = s.split(" ");
		final String s1 = as[0];
		as = as[1].split(",");

		if (s1.equals("swapPosition")) {
			print("!!!\tRun Task: \"swapPosition\"\t\t!!!");

			if (as.length == 2) {
				this.swapPosition(Byte.valueOf(as[0]).byteValue(), Byte
						.valueOf(as[1]).byteValue());
			} else {
				print("!!!\tError: Wrong number of parameters\t!!!\n");
				printErrorInrunTASK(s, s1, as);
				BSP.throwRuntimeException(getPrintErrorBuff()
						+ "Wrong number of parameters");
			}
		} else if (s1.equals("modeUpdated")) {
			print("!!!\tRun Task: \"modeUpdated\"\t\t\t!!!");

			if (as.length == 1) {
				this.modeUpdated(Byte.valueOf(as[0]).byteValue());
			} else {
				print("!!!\tError: Wrong number of parameters\t!!!\n");
				printErrorInrunTASK(s, s1, as);
				BSP.throwRuntimeException(getPrintErrorBuff()
						+ "Wrong number of parameters");
			}
		} else if (s1.equals("setFocused")) {
			print("!!!\tRun Task: \"setFocused\"\t\t\t!!!");

			if (as.length == 1) {
				this.setFocused(Byte.valueOf(as[0]).byteValue());
			} else {
				print("!!!\tError: Wrong number of parameters\t!!!\n");
				printErrorInrunTASK(s, s1, as);
				BSP.throwRuntimeException(getPrintErrorBuff()
						+ "Wrong number of parameters");
			}
		} else if (s1.equals("setInvertOutput")) {
			print("!!!\tRun Task: \"setInvertOutput\"\t\t!!!");

			if (as.length == 1) {
				setInvertOutput(Boolean.valueOf(as[0]).booleanValue());
			} else {
				print("!!!\tError: Wrong number of parameters\t!!!\n");
				printErrorInrunTASK(s, s1, as);
				BSP.throwRuntimeException(getPrintErrorBuff()
						+ "Wrong number of parameters");
			}
		} else if (s1.equals("logInOut")) {
			print("!!!\tRun Task: \"logInOut\"\t\t!!!");

			if (as.length == 2) {
				final UUID player = UUID.fromString(as[1]);

				if (Boolean.valueOf(as[0]).booleanValue()) {
					if (!Users.contains(player)) {
						Users.add(player);
					} else {
						print("User with UUID \"" + as[1]
								+ "\" is already logged in!");
					}
				} else {
					if (Users.contains(player)) {
						Users.remove(player);
					} else {
						print("User with UUID \"" + as[1]
								+ "\" is already logged out!");
					}
				}

				if (Users.size() == 0) {
					this.setFocused(0);
				}
			} else {
				print("!!!\tError: Wrong number of parameters\t!!!\n");
				printErrorInrunTASK(s, s1, as);
				BSP.throwRuntimeException(getPrintErrorBuff()
						+ "Wrong number of parameters");
			}
		} else {
			print("!!!\t\tInvalid Task\t\t\t!!!\n");
			printErrorInrunTASK(s, s1, as);
			BSP.throwRuntimeException(getPrintErrorBuff() + "Invalid Task");
		}

		print("!!!\t\tTask Finished\t\t\t!!!\n");

		stopPrintErrorBuff();
	}

	public void setDirection(byte byte0) {
		if ((byte0 >= 0) && (byte0 < 4)) {
			direction = byte0;
		}
	}

	public void setDirection(int i) {
		this.setDirection((byte) i);
	}

	public void setFocused(byte pin) {
		final Side side = FMLCommonHandler.instance().getEffectiveSide();
		if (side == Side.SERVER) {
			// We are on the server side.

			if ((pin >= 0) && (pin < 4)) {
				GuiFocused = pin;
			}
		} else if (side == Side.CLIENT) {
			// We are on the client side.

			this.addTASK("setFocused", new String[] { String.valueOf(pin) });
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
		this.addTASK((new StringBuilder()).append("modeUpdated ")
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

	public boolean setPinState(byte pinStates[]) {
		final byte[] tmpPinState = new byte[4];
		System.arraycopy(PinState, 0, tmpPinState, 0, 4);

		if (pinStates.length == 3) {
			System.arraycopy(pinStates, 0, PinState, 1, 3);
			PinState[0] = setOutput();
		}

		final Byte tmp;

		return !Arrays.equals(tmpPinState, PinState);
	}

	public boolean shallDoUpdate(long time) {
		if (time > (lastUpdate + 1)) {
			lastUpdate = time;
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

	public void swapPosition(int firstPos, int secondPos) {
		this.addTASK((new StringBuilder()).append("swapPosition ")
				.append(String.valueOf((byte) firstPos)).append(",")
				.append(String.valueOf((byte) secondPos)).toString());
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
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setByte("Direction", direction);
		nbttagcompound.setByte("Mode", mode);
		nbttagcompound.setByte("GuiFocused", GuiFocused);
		nbttagcompound.setInteger("Data", data);
		nbttagcompound.setBoolean("InvertOutput", invertOutput);
		nbttagcompound.setBoolean("Swapable", swapable);
		nbttagcompound.setBoolean("CorrectConnect", correctConnect);
		nbttagcompound.setBoolean("IgnoreIncorrectConnect",
				ignoreIncorrectConnect);

		for (int i = 0; i < 4; i++) {
			nbttagcompound.setByte((new StringBuilder()).append("State")
					.append(String.valueOf(i)).toString(), PinState[i]);
			nbttagcompound.setByte(
					(new StringBuilder()).append("Type")
							.append(String.valueOf(i)).toString(), PinType[i]);
			nbttagcompound.setString((new StringBuilder()).append("Pins")
					.append(String.valueOf(i)).toString(), Pins[i]);
		}

		final byte size = (byte) TASKS.size();
		nbttagcompound.setByte("TASKS-Size", size);

		for (byte byte1 = 0; byte1 < size; byte1++) {
			nbttagcompound
					.setString(
							(new StringBuilder()).append("TASK")
									.append(String.valueOf(byte1)).toString(),
							TASKS.get(byte1));
		}
	}
}