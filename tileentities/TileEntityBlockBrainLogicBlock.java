package mods.brainstone.tileentities;

import awp;
import bo;
import bs;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import mods.brainstone.guis.GuiBrainLogicBlock;
import mods.brainstone.handlers.BrainStonePacketHandler;
import mods.brainstone.templates.BSP;
import mods.brainstone.templates.TileEntityBrainStoneSyncBase;
import sk;
import zv;

public class TileEntityBlockBrainLogicBlock extends TileEntityBrainStoneSyncBase
{
  private boolean correctConnect;
  private boolean ignoreIncorrectConnect;
  private boolean invertOutput;
  private boolean swapable;
  private byte direction;
  private byte GuiFocused;
  private byte mode;
  private final byte[] PinState = { 0, -1, -1, -1 };
  private byte[] PinType;
  private int data;
  private long lastUpdate;
  private String[] Pins = { "0", "1", "2", "3" };
  private final Vector TASKS;
  private final Vector Users;
  private ArrayList PrintErrorBuff;
  private boolean PrintErrorBuffActive;
  public static final byte numGates = getNumGates();

  public static String getGateName(int i)
  {
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

  private static final byte getNumGates()
  {
    for (byte byte0 = 0; getGateName(byte0) != ""; byte0 = (byte)(byte0 + 1));
    return byte0;
  }

  public static String getInvName()
  {
    return "container.brainstonetrigger";
  }

  public TileEntityBlockBrainLogicBlock() {
    this((byte)0);
  }

  public TileEntityBlockBrainLogicBlock(byte byte0) {
    this.TASKS = new Vector();
    this.mode = 0;
    this.data = 0;
    this.lastUpdate = -100L;
    this.GuiFocused = 0;
    this.Users = new Vector();
    this.PrintErrorBuffActive = false;

    if ((byte0 >= 0) && (byte0 < 4)) {
      this.direction = byte0;
    }

    modeUpdated();
  }

  public void addTASKS(String s) {
    Side side = FMLCommonHandler.instance().getEffectiveSide();
    if (side == Side.SERVER)
    {
      this.TASKS.add(s);
    } else if (side == Side.CLIENT)
    {
      ByteArrayOutputStream bos = new ByteArrayOutputStream(0);
      DataOutputStream outputStream = new DataOutputStream(bos);
      try
      {
        outputStream.writeInt(this.l);
        outputStream.writeInt(this.m);
        outputStream.writeInt(this.n);

        outputStream.writeUTF(s);
      } catch (IOException e) {
        BSP.printException(e);
      }

      BrainStonePacketHandler.sendPacketToServer("BSM.TEBBLBS", bos);
    }
  }

  public void addTASKS(String s, String[] as)
  {
    String s1 = new StringBuilder().append(s).append(" ").toString();
    int i = as.length - 1;

    for (int j = 0; j < i; j++) {
      s1 = new StringBuilder().append(s1).append(as[j]).append(",").toString();
    }

    s1 = new StringBuilder().append(s1).append(as[i]).toString();

    addTASKS(s1);
  }

  public void doTASKS() {
    int i = this.TASKS.size();

    if (i != 0) {
      print(new StringBuilder().append("doTASKS() (size: ").append(String.valueOf(i)).append(")").toString());

      for (int j = 0; j < i; j++) {
        runTASK((String)this.TASKS.get(j));
      }

      this.TASKS.clear();
    }
    try
    {
      update(false);
    } catch (IOException e) {
      BSP.printException(e);
    }
  }

  public void drawBoxes(GuiBrainLogicBlock guibrainlogicblock, int i, int j) {
    byte byte0 = 0;

    if (this.PinState[0] != 0) {
      guibrainlogicblock.b(i + 20, j + 40, 176, 0, 20, 20);
    }

    byte byte1 = this.PinState[1];
    byte byte2 = this.PinType[1];

    if ((byte1 != 0) || (byte2 == -1)) {
      if (byte2 != -1) {
        if (byte1 == 1) {
          byte0 = 0;
        }

        if (byte1 == -1)
          byte0 = 1;
      }
      else {
        byte0 = 2;
      }

      guibrainlogicblock.b(i + 20, j, 176, 20 * byte0, 20, 20);
    }

    byte1 = this.PinState[2];
    byte2 = this.PinType[2];

    if ((byte1 != 0) || (byte2 == -1)) {
      if (byte2 != -1) {
        if (byte1 == 1) {
          byte0 = 0;
        }

        if (byte1 == -1)
          byte0 = 1;
      }
      else {
        byte0 = 2;
      }

      guibrainlogicblock.b(i + 40, j + 20, 176, 20 * byte0, 20, 20);
    }

    byte1 = this.PinState[3];
    byte2 = this.PinType[3];

    if ((byte1 != 0) || (byte2 == -1)) {
      if (byte2 != -1) {
        if (byte1 == 1) {
          byte0 = 0;
        }

        if (byte1 == -1)
          byte0 = 1;
      }
      else {
        byte0 = 2;
      }

      guibrainlogicblock.b(i, j + 20, 176, 20 * byte0, 20, 20);
    }
  }

  public void drawGates(GuiBrainLogicBlock guibrainlogicblock, int i, int j)
  {
    for (int k = 0; k < numGates; k++) {
      guibrainlogicblock.drawString(getGateName(k), i, j + 12 * k, k != this.mode ? 4210752 : 32768);
    }

    guibrainlogicblock.drawSplitString(bo.a("gui.brainstone.invertOutput"), i + 85, j + 50, this.invertOutput ? 32768 : 4210752, 80);

    if ((!this.correctConnect) && (!this.ignoreIncorrectConnect)) {
      guibrainlogicblock.drawSplitString(bo.a("gui.brainstone.warning1"), i + 75, j + 70, 10485760, 95);
    }
    else if (!this.correctConnect)
      guibrainlogicblock.drawSplitString(bo.a("gui.brainstone.warning2"), i + 75, j + 70, 10506240, 95);
  }

  protected void generateOutputStream(DataOutputStream outputStream)
    throws IOException
  {
    outputStream.writeInt(this.l);
    outputStream.writeInt(this.m);
    outputStream.writeInt(this.n);

    outputStream.writeBoolean(this.correctConnect);
    outputStream.writeBoolean(this.ignoreIncorrectConnect);
    outputStream.writeBoolean(this.invertOutput);
    outputStream.writeBoolean(this.swapable);
    outputStream.writeByte(this.direction);
    outputStream.writeByte(this.GuiFocused);
    outputStream.writeByte(this.mode);

    int size = this.PinState.length;
    outputStream.writeInt(size);
    for (int i = 0; i < size; i++) {
      outputStream.writeByte(this.PinState[i]);
    }

    size = this.PinType.length;
    outputStream.writeInt(size);
    for (int i = 0; i < size; i++) {
      outputStream.writeByte(this.PinType[i]);
    }

    outputStream.writeInt(this.data);
    outputStream.writeLong(this.lastUpdate);

    size = this.Pins.length;
    outputStream.writeInt(size);
    for (int i = 0; i < size; i++)
      outputStream.writeUTF(this.Pins[i]);
  }

  public byte getDirection()
  {
    return this.direction;
  }

  public byte getFocused() {
    return this.GuiFocused;
  }

  public byte getMode() {
    return this.mode;
  }

  public boolean getOutput() {
    return this.PinState[0] == 1;
  }

  public String getPin(int i) {
    if ((i < 0) || (i >= 4)) {
      return "";
    }
    return this.Pins[i];
  }

  public int getPinColor(int i) {
    if ((i < 0) || (i >= 4)) {
      return 0;
    }
    byte byte0 = this.PinState[i];
    return byte0 != -1 ? 328965 : byte0 != 0 ? 12977413 : 8947848;
  }

  public int getPinStateBasedTextureIndex(int i)
  {
    if ((i < 0) || (i >= this.PinState.length)) {
      return 0;
    }
    if (this.PinType[i] == -1) {
      return 3;
    }
    byte byte0 = this.PinState[i];
    return byte0 != -1 ? 1 : byte0 != 0 ? 2 : 0;
  }

  private String getPrintErrorBuff()
  {
    if (!this.PrintErrorBuffActive) {
      return "";
    }
    String tmp = "";
    int size = this.PrintErrorBuff.size();

    for (int i = 0; i < size; i++) {
      tmp = new StringBuilder().append(tmp).append((String)this.PrintErrorBuff.get(i)).append("\n").toString();
    }

    tmp = new StringBuilder().append(tmp).append("\n").toString();

    return tmp;
  }

  public void invertInvertOutput() {
    addTASKS(new StringBuilder().append("setInvertOutput ").append(String.valueOf(!this.invertOutput)).toString());
  }

  public boolean isCorrectConnected()
  {
    return this.correctConnect;
  }

  public boolean isSwapable() {
    return this.swapable;
  }

  public boolean isUseableByPlayer(sk entityplayer) {
    if (this.k.r(this.l, this.m, this.n) != this) {
      return false;
    }
    return entityplayer.e(this.l + 0.5D, this.m + 0.5D, this.n + 0.5D) <= 64.0D;
  }

  public void logIn(String user)
  {
    addTASKS("logInOut", new String[] { "true", user });
  }

  public void logOut(String user) {
    addTASKS("logInOut", new String[] { "false", user });
  }

  private void modeUpdated() {
    this.swapable = true;
    this.ignoreIncorrectConnect = false;
    this.invertOutput = false;

    switch (this.mode) {
    case 0:
    case 1:
    case 2:
      this.swapable = false;
      this.Pins = new String[] { "Q", "B", "A", "C" };
      this.PinType = new byte[] { 10, 0, 0, 0 };
      break;
    case 3:
      this.Pins = new String[] { "Q", "", "A", "B" };
      this.PinType = new byte[] { 10, -1, 1, 1 };
      break;
    case 4:
      this.ignoreIncorrectConnect = true;
      this.Pins = new String[] { "Q", "A", "", "" };
      this.PinType = new byte[] { 10, 1, -1, -1 };
      break;
    case 5:
      this.data = 0;
      this.ignoreIncorrectConnect = true;
      this.Pins = new String[] { "Q", "", "R", "S" };
      this.PinType = new byte[] { 10, -1, 1, 1 };
      break;
    case 6:
      this.data = 0;
      this.Pins = new String[] { "Q", "", "D", "C" };
      this.PinType = new byte[] { 10, -1, 1, 1 };
      break;
    case 7:
      this.data = 0;
      this.Pins = new String[] { "Q", "T", "", "" };
      this.PinType = new byte[] { 10, 1, -1, -1 };
      break;
    case 8:
      this.data = 0;
      this.ignoreIncorrectConnect = true;
      this.Pins = new String[] { "Q", "", "J", "K" };
      this.PinType = new byte[] { 10, -1, 1, 1 };
    }
  }

  private void modeUpdated(byte byte0)
  {
    this.mode = byte0;
    modeUpdated();
  }

  private void print(Object obj) {
    if ((!BSP.println(obj)) && (this.PrintErrorBuffActive))
      this.PrintErrorBuff.add(obj.toString());
  }

  private void printErrorInrunTASK(String s, String s1, String[] as)
  {
    print("===================================================");
    print("Error Signature in \"runTASK\":");
    print("===================================================");
    print(new StringBuilder().append("\tOriginal Task: \"").append(s).append("\"").toString());

    print(new StringBuilder().append("\tCutted Task:   \"").append(s1).append("\"").toString());

    print("\tParameters:");
    int i = as.length;

    if (i == 0)
      print("\t\tNo Parameters!!!");
    else {
      for (int j = 0; j < i; j++) {
        print(new StringBuilder().append("\t\tParameter ").append(String.valueOf(j)).append(": \"").append(as[j]).append("\"").toString());
      }

    }

    print("===================================================");
    print("!!!\t\t\tEND\t\t\t!!!");
    print("===================================================");
  }

  public void readFromInputStream(DataInputStream inputStream)
    throws IOException
  {
    this.correctConnect = inputStream.readBoolean();
    this.ignoreIncorrectConnect = inputStream.readBoolean();
    this.invertOutput = inputStream.readBoolean();
    this.swapable = inputStream.readBoolean();
    this.direction = inputStream.readByte();
    this.GuiFocused = inputStream.readByte();
    this.mode = inputStream.readByte();

    int size = inputStream.readInt();
    for (int i = 0; i < size; i++) {
      this.PinState[i] = inputStream.readByte();
    }

    size = inputStream.readInt();
    for (int i = 0; i < size; i++) {
      this.PinType[i] = inputStream.readByte();
    }

    this.data = inputStream.readInt();
    this.lastUpdate = inputStream.readLong();

    size = inputStream.readInt();
    for (int i = 0; i < size; i++)
      this.Pins[i] = inputStream.readUTF();
  }

  public void a(bs nbttagcompound)
  {
    super.a(nbttagcompound);
    this.direction = nbttagcompound.c("Direction");
    this.mode = nbttagcompound.c("Mode");
    this.data = nbttagcompound.e("Data");
    this.invertOutput = nbttagcompound.n("InvertOutput");
    this.swapable = nbttagcompound.n("Swapable");

    for (int i = 0; i < 4; i++) {
      this.PinState[i] = nbttagcompound.c(new StringBuilder().append("State").append(String.valueOf(i)).toString());

      this.PinType[i] = nbttagcompound.c(new StringBuilder().append("Type").append(String.valueOf(i)).toString());

      this.Pins[i] = nbttagcompound.i(new StringBuilder().append("Pins").append(String.valueOf(i)).toString());
    }

    byte byte0 = nbttagcompound.c("TASKS-Size");

    for (byte byte1 = 0; byte1 < byte0; byte1 = (byte)(byte1 + 1))
      this.TASKS.add(byte1, nbttagcompound.i(new StringBuilder().append("TASK").append(String.valueOf(byte1)).toString()));
  }

  public void renderInOutPut(awp fontrenderer, byte byte0)
  {
    byte0 = transformDirection(byte0);

    if (this.PinType[byte0] != -1) {
      byte byte1 = this.PinState[byte0];
      fontrenderer.b(this.Pins[byte0], -fontrenderer.a(this.Pins[byte0]) / 2, 4, byte1 != -1 ? 328965 : byte1 != 0 ? 12977413 : 8947848);
    }
  }

  public byte reverseTransformDirection(int i)
  {
    switch (this.direction) {
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
      }

      break;
    }

    return (byte)i;
  }

  private void runTASK(String s) {
    startPrintErrorBuff();

    String[] as = s.split(" ");
    String s1 = as[0];
    as = as[1].split(",");

    if (s1.equals("swapPosition")) {
      print("!!!\tRun Task: \"swapPosition\"\t\t!!!");

      if (as.length == 2) {
        swapPosition(Byte.valueOf(as[0]).byteValue(), Byte.valueOf(as[1]).byteValue());
      }
      else {
        print("!!!\tError: Wrong number of parameters\t!!!\n");
        printErrorInrunTASK(s, s1, as);
        BSP.throwRuntimeException(new StringBuilder().append(getPrintErrorBuff()).append("Wrong number of parameters").toString());
      }
    }
    else if (s1.equals("modeUpdated")) {
      print("!!!\tRun Task: \"modeUpdated\"\t\t\t!!!");

      if (as.length == 1) {
        modeUpdated(Byte.valueOf(as[0]).byteValue());
      } else {
        print("!!!\tError: Wrong number of parameters\t!!!\n");
        printErrorInrunTASK(s, s1, as);
        BSP.throwRuntimeException(new StringBuilder().append(getPrintErrorBuff()).append("Wrong number of parameters").toString());
      }
    }
    else if (s1.equals("setFocused")) {
      print("!!!\tRun Task: \"setFocused\"\t\t\t!!!");

      if (as.length == 1) {
        setFocused(Byte.valueOf(as[0]).byteValue());
      } else {
        print("!!!\tError: Wrong number of parameters\t!!!\n");
        printErrorInrunTASK(s, s1, as);
        BSP.throwRuntimeException(new StringBuilder().append(getPrintErrorBuff()).append("Wrong number of parameters").toString());
      }
    }
    else if (s1.equals("setInvertOutput")) {
      print("!!!\tRun Task: \"setInvertOutput\"\t\t!!!");

      if (as.length == 1) {
        setInvertOutput(Boolean.valueOf(as[0]).booleanValue());
      } else {
        print("!!!\tError: Wrong number of parameters\t!!!\n");
        printErrorInrunTASK(s, s1, as);
        BSP.throwRuntimeException(new StringBuilder().append(getPrintErrorBuff()).append("Wrong number of parameters").toString());
      }
    }
    else if (s1.equals("logInOut")) {
      print("!!!\tRun Task: \"logInOut\"\t\t!!!");

      if (as.length == 2) {
        if (Boolean.valueOf(as[0]).booleanValue()) {
          if (!this.Users.contains(as[1]))
            this.Users.add(as[1]);
          else {
            print(new StringBuilder().append("User \"").append(as[1]).append("\" is already logged in!").toString());
          }

        }
        else if (this.Users.contains(as[1]))
          this.Users.remove(as[1]);
        else {
          print(new StringBuilder().append("User \"").append(as[1]).append("\" is already logged out!").toString());
        }

        if (this.Users.size() == 0)
          setFocused(0);
      }
      else {
        print("!!!\tError: Wrong number of parameters\t!!!\n");
        printErrorInrunTASK(s, s1, as);
        BSP.throwRuntimeException(new StringBuilder().append(getPrintErrorBuff()).append("Wrong number of parameters").toString());
      }
    }
    else {
      print("!!!\t\tInvalid Task\t\t\t!!!\n");
      printErrorInrunTASK(s, s1, as);
      BSP.throwRuntimeException(new StringBuilder().append(getPrintErrorBuff()).append("Invalid Task").toString());
    }

    print("!!!\t\tTask Finished\t\t\t!!!\n");

    stopPrintErrorBuff();
  }

  public void setDirection(byte byte0) {
    if ((byte0 >= 0) && (byte0 < 4))
      this.direction = byte0;
  }

  public void setDirection(int i)
  {
    setDirection((byte)i);
  }

  public void setFocused(byte byte0) {
    Side side = FMLCommonHandler.instance().getEffectiveSide();
    if (side == Side.SERVER)
    {
      if ((byte0 >= 0) && (byte0 < 4))
        this.GuiFocused = byte0;
    }
    else if (side == Side.CLIENT)
    {
      addTASKS("setFocused", new String[] { String.valueOf(byte0) });
    }
  }

  public void setFocused(int i)
  {
    setFocused((byte)i);
  }

  private void setInvertOutput(boolean flag) {
    this.invertOutput = flag;
  }

  public void setMode(byte byte0) {
    addTASKS(new StringBuilder().append("modeUpdated ").append(String.valueOf(byte0)).toString());
  }

  private byte setOutput()
  {
    boolean flag = false;
    this.correctConnect = true;

    for (int i = 1; i < 4; i++) {
      this.correctConnect = ((this.correctConnect) && ((this.PinType[i] != 1) || ((this.PinType[i] == 1) && (this.PinState[i] != -1))));
    }

    this.correctConnect = ((this.correctConnect) && ((this.PinState[1] != -1) || (this.PinState[2] != -1) || (this.PinState[3] != -1)));

    if ((this.correctConnect) || (this.ignoreIncorrectConnect)) {
      ArrayList arraylist = new ArrayList();
      HashMap hashmap = new HashMap();

      for (int j = 1; j < 4; j++) {
        if ((this.PinState[j] != -1) && (this.PinType[j] != -1))
        {
          boolean flag1;
          arraylist.add(Boolean.valueOf(flag1 = this.PinState[j] == 1 ? 1 : 0));
          hashmap.put(this.Pins[j], Boolean.valueOf(flag1));
        }

        if ((this.PinState[j] == -1) && (this.ignoreIncorrectConnect) && (!hashmap.containsKey(this.Pins[j])))
        {
          hashmap.put(this.Pins[j], Boolean.valueOf(this.PinState[j] == 1));
        }
      }

      int k = arraylist.size();

      switch (this.mode) {
      default:
        break;
      case 0:
        flag = k != 0;

        for (int l = 0; l < k; l++) {
          flag = (flag) && (((Boolean)arraylist.get(l)).booleanValue());
        }

        break;
      case 1:
        for (int i1 = 0; i1 < k; i1++) {
          flag = (flag) || (((Boolean)arraylist.get(i1)).booleanValue());
        }

        break;
      case 2:
        for (int j1 = 0; j1 < k; j1++) {
          flag ^= ((Boolean)arraylist.get(j1)).booleanValue();
        }

        break;
      case 3:
        if (k == 2)
          flag = (!((Boolean)arraylist.get(0)).booleanValue()) || (((Boolean)arraylist.get(1)).booleanValue()); break;
      case 4:
        flag = !((Boolean)hashmap.get("A")).booleanValue();
        break;
      case 5:
        if (((Boolean)hashmap.get("R")).booleanValue()) {
          this.data = 0;
          flag = false;
        }
        else if (((Boolean)hashmap.get("S")).booleanValue()) {
          this.data = 1;
          flag = true;
        } else {
          flag = this.data == 1;
        }

        break;
      case 6:
        if (((Boolean)hashmap.get("C")).booleanValue()) {
          this.data = ((flag = ((Boolean)hashmap.get("D")).booleanValue()) ? 1 : 0);
        }
        else {
          flag = this.data == 1;
        }

        break;
      case 7:
        if (((Boolean)hashmap.get("T")).booleanValue()) {
          if ((this.data & 0x2) != 2)
            this.data = (this.data ^ 0x1 | 0x2);
        }
        else {
          this.data &= 1;
        }

        flag = (this.data & 0x1) == 1;
        break;
      case 8:
        this.data = ((this.data & 0x1) << 1);
        boolean flag2 = ((Boolean)hashmap.get("J")).booleanValue();

        boolean flag3 = ((Boolean)hashmap.get("K")).booleanValue();

        if ((!flag2) && (!flag3))
          this.data |= this.data >> 1;
        else if ((flag2) && (flag3))
          this.data |= this.data >> 1 ^ 0x1;
        else if (flag2) {
          this.data |= 1;
        }

        flag = (this.data & 0x1) == 1;
      }

    }

    return (byte)((flag ^ this.invertOutput) ? 1 : 0);
  }

  public void setPinState(byte[] abyte0) {
    if (abyte0.length == 3) {
      System.arraycopy(abyte0, 0, this.PinState, 1, 3);
      this.PinState[0] = setOutput();
    }
  }

  public boolean shallDoUpdate(long l) {
    if (l > this.lastUpdate + 1L) {
      this.lastUpdate = l;
      return true;
    }
    return false;
  }

  public boolean[] shallRender() {
    boolean[] aflag = new boolean[4];

    for (int i = 0; i < 4; i++) {
      aflag[i] = (this.PinType[i] != -1 ? 1 : false);
    }

    return aflag;
  }

  private void startPrintErrorBuff() {
    this.PrintErrorBuffActive = true;
    this.PrintErrorBuff = new ArrayList();
  }

  private void stopPrintErrorBuff() {
    this.PrintErrorBuffActive = false;
    this.PrintErrorBuff = null;
  }

  private void swapPosition(byte byte0, byte byte1) {
    if ((byte0 > 0) && (byte0 < 4) && (byte1 > 0) && (byte1 < 4) && (byte0 != byte1) && (this.swapable))
    {
      byte byte2 = this.PinType[byte0];
      String s = this.Pins[byte0];
      this.PinType[byte0] = this.PinType[byte1];
      this.Pins[byte0] = this.Pins[byte1];
      this.PinType[byte1] = byte2;
      this.Pins[byte1] = s;
      this.GuiFocused = byte1;
    }
  }

  public void swapPosition(int i, int j) {
    addTASKS(new StringBuilder().append("swapPosition ").append(String.valueOf((byte)i)).append(",").append(String.valueOf((byte)j)).toString());
  }

  public byte transformDirection(int i)
  {
    switch (this.direction) {
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
      }

      break;
    }

    return (byte)i;
  }

  public void update(boolean sendToServer) throws IOException
  {
    ByteArrayOutputStream bos = new ByteArrayOutputStream(0);
    DataOutputStream outputStream = new DataOutputStream(bos);

    generateOutputStream(outputStream);

    if (sendToServer)
      BrainStonePacketHandler.sendPacketToServer("BSM.TEBBLBS", bos);
    else
      BrainStonePacketHandler.sendPacketToClosestPlayers(this, "BSM.TEBBLBC", bos);
  }

  public void b(bs nbttagcompound)
  {
    super.b(nbttagcompound);
    nbttagcompound.a("Direction", this.direction);
    nbttagcompound.a("Mode", this.mode);
    nbttagcompound.a("Data", this.data);
    nbttagcompound.a("InvertOutput", this.invertOutput);
    nbttagcompound.a("Swapable", this.swapable);

    for (int i = 0; i < 4; i++) {
      nbttagcompound.a(new StringBuilder().append("State").append(String.valueOf(i)).toString(), this.PinState[i]);

      nbttagcompound.a(new StringBuilder().append("Type").append(String.valueOf(i)).toString(), this.PinType[i]);

      nbttagcompound.a(new StringBuilder().append("Pins").append(String.valueOf(i)).toString(), this.Pins[i]);
    }

    byte byte0 = (byte)this.TASKS.size();
    nbttagcompound.a("TASKS-Size", byte0);

    for (byte byte1 = 0; byte1 < byte0; byte1 = (byte)(byte1 + 1))
      nbttagcompound.a(new StringBuilder().append("TASK").append(String.valueOf(byte1)).toString(), (String)this.TASKS.get(byte1));
  }
}