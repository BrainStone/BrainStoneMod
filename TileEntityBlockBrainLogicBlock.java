import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class TileEntityBlockBrainLogicBlock extends kw
{
  private boolean correctConnect;
  private boolean ignoreIncorrectConnect;
  private boolean invertOutput;
  private boolean swapable;
  private byte direction;
  private byte GuiFocused;
  private byte mode;
  private byte[] PinState;
  private byte[] PinType;
  private int data;
  private long lastUpdate;
  private String[] Pins;
  private Vector TASKS;
  public static final byte numGates = getNumGates();

  public TileEntityBlockBrainLogicBlock()
  {
    this((byte)0);
  }

  public TileEntityBlockBrainLogicBlock(byte dir)
  {
    this.TASKS = new Vector();
    this.Pins = new String[] { "0", "1", "2", "3" };
    this.PinState = new byte[] { 0, -1, -1, -1 };
    this.mode = 0;
    this.data = 0;
    this.lastUpdate = -100L;
    this.GuiFocused = 0;

    if ((dir >= 0) && (dir < 4)) {
      this.direction = dir;
    }
    modeUpdated();
  }

  public void addTASKS(String TASK, String[] parameters)
  {
    String TASKtoAdd = new StringBuilder().append(TASK).append(" ").toString();

    int size = parameters.length - 1;

    for (int i = 0; i < size; i++)
      TASKtoAdd = new StringBuilder().append(TASKtoAdd).append(parameters[i]).append(",").toString();
    TASKtoAdd = new StringBuilder().append(TASKtoAdd).append(parameters[size]).toString();

    this.TASKS.add(TASKtoAdd);
  }

  public void doTASKS()
  {
    int size = this.TASKS.size();

    if (size != 0)
    {
      print(new StringBuilder().append("doTASKS() (size: ").append(String.valueOf(size)).append(")").toString());

      for (int i = 0; i < size; i++)
      {
        runTASK((String)this.TASKS.get(i));
      }

      this.TASKS.clear();
    }
  }

  public void drawBoxes(GuiBrainLogicBlock gui, int x, int y)
  {
    byte tmp = 0;

    if (this.PinState[0] != 0) {
      gui.b(x + 20, y + 40, 176, 0, 20, 20);
    }
    byte state = this.PinState[1];
    byte type = this.PinType[1];

    if ((state != 0) || (type == -1))
    {
      if (type != -1)
      {
        if (state == 1) {
          tmp = 0;
        }
        if (state == -1)
          tmp = 1;
      }
      else
      {
        tmp = 2;
      }

      gui.b(x + 20, y, 176, 20 * tmp, 20, 20);
    }

    state = this.PinState[2];
    type = this.PinType[2];

    if ((state != 0) || (type == -1))
    {
      if (type != -1)
      {
        if (state == 1) {
          tmp = 0;
        }
        if (state == -1)
          tmp = 1;
      }
      else
      {
        tmp = 2;
      }

      gui.b(x + 40, y + 20, 176, 20 * tmp, 20, 20);
    }

    state = this.PinState[3];
    type = this.PinType[3];

    if ((state != 0) || (type == -1))
    {
      if (type != -1)
      {
        if (state == 1) {
          tmp = 0;
        }
        if (state == -1)
          tmp = 1;
      }
      else
      {
        tmp = 2;
      }

      gui.b(x, y + 20, 176, 20 * tmp, 20, 20);
    }
  }

  public void drawGates(GuiBrainLogicBlock gui, int x, int y)
  {
    for (int i = 0; i < numGates; i++)
    {
      gui.drawString(getGateName(i), x, y + 12 * i, i == this.mode ? 32768 : 4210752);
    }

    gui.drawSplitString(cy.a("gui.brainstone.invertOutput"), x + 85, y + 50, this.invertOutput ? 32768 : 4210752, 80);

    if ((!this.correctConnect) && (!this.ignoreIncorrectConnect))
      gui.drawSplitString(cy.a("gui.brainstone.warning1"), x + 75, y + 70, 10485760, 95);
    else if (!this.correctConnect)
      gui.drawSplitString(cy.a("gui.brainstone.warning2"), x + 75, y + 70, 10506240, 95);
  }

  public void invertInvertOutput()
  {
    this.TASKS.add(new StringBuilder().append("setInvertOutput ").append(String.valueOf(!this.invertOutput)).toString());
  }

  public void renderInOutPut(nl fontrenderer, byte index)
  {
    index = transformDirection(index);

    if (this.PinType[index] != -1)
    {
      byte tmp = this.PinState[index];

      fontrenderer.b(this.Pins[index], -fontrenderer.a(this.Pins[index]) / 2, 4, tmp == 0 ? 328965 : tmp == -1 ? 8947848 : 12977413);
    }
  }

  public void setDirection(byte dir)
  {
    if ((dir >= 0) && (dir < 4))
      this.direction = dir;
  }

  public void setDirection(int dir)
  {
    setDirection((byte)dir);
  }

  public void setFocused(byte focused)
  {
    if ((focused >= 0) && (focused < 4))
      this.GuiFocused = focused;
  }

  public void setFocused(int focused)
  {
    setFocused((byte)focused);
  }

  public void setMode(byte mode)
  {
    this.TASKS.add(new StringBuilder().append("modeUpdated ").append(String.valueOf(mode)).toString());
  }

  public void setPinState(byte[] States)
  {
    if (States.length == 3)
    {
      System.arraycopy(States, 0, this.PinState, 1, 3);

      this.PinState[0] = setOutput();
    }
  }

  public void swapPosition(int pos1, int pos2)
  {
    this.TASKS.add(new StringBuilder().append("swapPosition ").append(String.valueOf((byte)pos1)).append(",").append(String.valueOf((byte)pos2)).toString());
  }

  public boolean getOutput()
  {
    return this.PinState[0] == 1;
  }

  public boolean isCorrectConnected()
  {
    return this.correctConnect;
  }

  public boolean isSwapable()
  {
    return this.swapable;
  }

  public boolean isUseableByPlayer(yw par1EntityPlayer)
  {
    if (this.i.b(this.j, this.k, this.l) != this)
    {
      return false;
    }

    return par1EntityPlayer.f(this.j + 0.5D, this.k + 0.5D, this.l + 0.5D) <= 64.0D;
  }

  public boolean shallDoUpdate(long worldTime)
  {
    if (worldTime > this.lastUpdate)
    {
      this.lastUpdate = worldTime;
      return true;
    }

    return false;
  }

  public boolean[] shallRender()
  {
    boolean[] render = new boolean[4];

    for (int i = 0; i < 4; i++)
    {
      render[i] = (this.PinType[i] != -1 ? 1 : false);
    }

    return render;
  }

  public byte getDirection()
  {
    return this.direction;
  }

  public byte getFocused()
  {
    return this.GuiFocused;
  }

  public byte getMode()
  {
    return this.mode;
  }

  public byte reverseTransformDirection(int index)
  {
    switch (this.direction)
    {
    case 1:
      switch (index) {
      case 2:
        index = 0;
        break;
      case 3:
        index = 1;
        break;
      case 1:
        index = 2;
        break;
      case 0:
        index = 3;
      }

      break;
    case 2:
      switch (index) {
      case 1:
        index = 0;
        break;
      case 0:
        index = 1;
        break;
      case 3:
        index = 2;
        break;
      case 2:
        index = 3;
      }

      break;
    case 3:
      switch (index) {
      case 3:
        index = 0;
        break;
      case 2:
        index = 1;
        break;
      case 0:
        index = 2;
        break;
      case 1:
        index = 3;
      }

      break;
    }

    return (byte)index;
  }

  public byte transformDirection(int index)
  {
    switch (this.direction)
    {
    case 1:
      switch (index) {
      case 0:
        index = 2;
        break;
      case 1:
        index = 3;
        break;
      case 2:
        index = 1;
        break;
      case 3:
        index = 0;
      }

      break;
    case 2:
      switch (index) {
      case 0:
        index = 1;
        break;
      case 1:
        index = 0;
        break;
      case 2:
        index = 3;
        break;
      case 3:
        index = 2;
      }

      break;
    case 3:
      switch (index) {
      case 0:
        index = 3;
        break;
      case 1:
        index = 2;
        break;
      case 2:
        index = 0;
        break;
      case 3:
        index = 1;
      }

      break;
    }

    return (byte)index;
  }

  public int getPinColor(int pos)
  {
    if ((pos < 0) || (pos >= 4)) {
      return 0;
    }
    byte tmp = this.PinState[pos];

    return tmp == 0 ? 328965 : tmp == -1 ? 8947848 : 12977413;
  }

  public int getPinStateBasedTexture(int index)
  {
    if ((index < 0) || (index >= this.PinState.length)) {
      return mod_BrainStone.brainLogicBlockNotConnectedTexture;
    }
    if (this.PinType[index] == -1) {
      return 45;
    }
    byte tmp = this.PinState[index];

    return tmp == 0 ? mod_BrainStone.brainLogicBlockOffTexture : tmp == -1 ? mod_BrainStone.brainLogicBlockNotConnectedTexture : mod_BrainStone.brainLogicBlockOnTexture;
  }

  public static String getGateName(int id)
  {
    switch (id) {
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

  public static String getInvName()
  {
    return "container.brainstonetrigger";
  }

  public String getPin(int pos)
  {
    if ((pos < 0) || (pos >= 4)) {
      return "";
    }
    return this.Pins[pos];
  }

  private void modeUpdated(byte mode)
  {
    this.mode = mode;
    modeUpdated();
  }

  private void modeUpdated()
  {
    this.swapable = true;
    this.ignoreIncorrectConnect = false;
    this.invertOutput = false;

    switch (this.mode)
    {
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

  private void print(String str)
  {
    System.out.println(str);
  }

  private void printErrorInrunTASK(String TASK, String task, String[] parameters)
  {
    print("===================================================");
    print("Error Signature in \"runTASK\":");
    print("===================================================");
    print(new StringBuilder().append("\tOriginal Task: \"").append(TASK).append("\"").toString());
    print(new StringBuilder().append("\tCutted Task:   \"").append(task).append("\"").toString());
    print("\tParameters:");

    int size = parameters.length;

    if (size == 0)
    {
      print("\t\tNo Parameters!!!");
    }
    else
    {
      for (int i = 0; i < size; i++)
      {
        print(new StringBuilder().append("\t\tParameter ").append(String.valueOf(i)).append(": \"").append(parameters[i]).append("\"").toString());
      }
    }

    print("===================================================");
    print("!!!\t\t\tEND\t\t\t!!!");
    print("===================================================");
  }

  private void runTASK(String TASK)
  {
    String[] parameters = TASK.split(" ");
    String task = parameters[0];
    parameters = parameters[1].split(",");

    if (task.equals("swapPosition"))
    {
      print("!!!\tRun Task: \"swapPosition\"\t\t!!!");

      if (parameters.length == 2)
      {
        swapPosition(Byte.valueOf(parameters[0]).byteValue(), Byte.valueOf(parameters[1]).byteValue());
      }
      else
      {
        print("!!!\tError: Wrong number of parameters\t!!!\n");
        printErrorInrunTASK(TASK, task, parameters);

        throw new RuntimeException("Wrong number of parameters");
      }
    }
    else if (task.equals("modeUpdated"))
    {
      print("!!!\tRun Task: \"modeUpdated\"\t\t\t!!!");

      if (parameters.length == 1)
      {
        modeUpdated(Byte.valueOf(parameters[0]).byteValue());
      }
      else
      {
        print("!!!\tError: Wrong number of parameters\t!!!\n");
        printErrorInrunTASK(TASK, task, parameters);

        throw new RuntimeException("Wrong number of parameters");
      }
    }
    else if (task.equals("setFocused"))
    {
      print("!!!\tRun Task: \"setFocused\"\t\t\t!!!");

      if (parameters.length == 1)
      {
        setFocused(Byte.valueOf(parameters[0]).byteValue());
      }
      else
      {
        print("!!!\tError: Wrong number of parameters\t!!!\n");
        printErrorInrunTASK(TASK, task, parameters);

        throw new RuntimeException("Wrong number of parameters");
      }
    }
    else if (task.equals("setInvertOutput"))
    {
      print("!!!\tRun Task: \"setInvertOutput\"\t\t!!!");

      if (parameters.length == 1)
      {
        setInvertOutput(Boolean.valueOf(parameters[0]).booleanValue());
      }
      else
      {
        print("!!!\tError: Wrong number of parameters\t!!!\n");
        printErrorInrunTASK(TASK, task, parameters);

        throw new RuntimeException("Wrong number of parameters");
      }
    }
    else
    {
      print("!!!\t\tInvalid Task\t\t\t!!!\n");
      printErrorInrunTASK(TASK, task, parameters);

      throw new RuntimeException("Invalid Task");
    }

    print("!!!\t\tTask Finished\t\t\t!!!\n");
  }

  private void setInvertOutput(boolean invertOutput)
  {
    this.invertOutput = invertOutput;
  }

  private void swapPosition(byte pos1, byte pos2)
  {
    if ((pos1 > 0) && (pos1 < 4) && (pos2 > 0) && (pos2 < 4) && (pos1 != pos2) && (this.swapable))
    {
      byte tmpPinType = this.PinType[pos1];
      String tmpPins = this.Pins[pos1];

      this.PinType[pos1] = this.PinType[pos2];
      this.Pins[pos1] = this.Pins[pos2];

      this.PinType[pos2] = tmpPinType;
      this.Pins[pos2] = tmpPins;

      this.GuiFocused = pos2;
    }
  }

  private static final byte getNumGates()
  {
    byte i = 0;

    while (getGateName(i) != "")
    {
      i = (byte)(i + 1);
    }

    return i;
  }

  private byte setOutput()
  {
    boolean out = false;
    this.correctConnect = true;

    for (int i = 1; i < 4; i++)
    {
      this.correctConnect = ((this.correctConnect) && ((this.PinType[i] != 1) || ((this.PinType[i] == 1) && (this.PinState[i] != -1))));
    }

    this.correctConnect = ((this.correctConnect) && ((this.PinState[1] != -1) || (this.PinState[2] != -1) || (this.PinState[3] != -1)));

    if ((this.correctConnect) || (this.ignoreIncorrectConnect))
    {
      ArrayList Inputs = new ArrayList();
      HashMap MapedInputs = new HashMap();

      for (int i = 1; i < 4; i++)
      {
        if ((this.PinState[i] != -1) && (this.PinType[i] != -1))
        {
          boolean tmp;
          Inputs.add(Boolean.valueOf(tmp = this.PinState[i] == 1 ? 1 : 0));
          MapedInputs.put(this.Pins[i], Boolean.valueOf(tmp));
        }

        if ((this.PinState[i] == -1) && (this.ignoreIncorrectConnect) && (!MapedInputs.containsKey(this.Pins[i])))
        {
          MapedInputs.put(this.Pins[i], Boolean.valueOf(this.PinState[i] == 1));
        }
      }

      int size = Inputs.size();

      switch (this.mode)
      {
      case 0:
        out = size != 0;

        for (int i = 0; i < size; i++)
        {
          out = (out) && (((Boolean)Inputs.get(i)).booleanValue());
        }

        break;
      case 1:
        for (int i = 0; i < size; i++)
        {
          out = (out) || (((Boolean)Inputs.get(i)).booleanValue());
        }

        break;
      case 2:
        for (int i = 0; i < size; i++)
        {
          out ^= ((Boolean)Inputs.get(i)).booleanValue();
        }

        break;
      case 3:
        if (size == 2)
          out = (!((Boolean)Inputs.get(0)).booleanValue()) || (((Boolean)Inputs.get(1)).booleanValue()); break;
      case 4:
        out = !((Boolean)MapedInputs.get("A")).booleanValue();

        break;
      case 5:
        if (((Boolean)MapedInputs.get("R")).booleanValue())
        {
          this.data = 0;
          out = false;
        }
        else if (((Boolean)MapedInputs.get("S")).booleanValue())
        {
          this.data = 1;
          out = true;
        }
        else
        {
          out = this.data == 1;
        }

        break;
      case 6:
        if (((Boolean)MapedInputs.get("C")).booleanValue())
        {
          this.data = ((out = ((Boolean)MapedInputs.get("D")).booleanValue()) ? 1 : 0);
        }
        else
        {
          out = this.data == 1;
        }

        break;
      case 7:
        if (((Boolean)MapedInputs.get("T")).booleanValue())
        {
          if ((this.data & 0x2) != 2)
          {
            this.data = (this.data ^ 0x1 | 0x2);
          }
        }
        else
        {
          this.data &= 1;
        }

        out = (this.data & 0x1) == 1;

        break;
      case 8:
        this.data = ((this.data & 0x1) << 1);

        boolean j = ((Boolean)MapedInputs.get("J")).booleanValue(); boolean k = ((Boolean)MapedInputs.get("K")).booleanValue();

        if ((!j) && (!k))
        {
          this.data |= this.data >> 1;
        }
        else if ((j) && (k))
        {
          this.data |= this.data >> 1 ^ 0x1;
        }
        else if (j)
        {
          this.data |= 1;
        }

        out = (this.data & 0x1) == 1;
      }

    }

    return (byte)((out ^ this.invertOutput) ? 1 : 0);
  }

  public void a(ady par1NBTTagCompound)
  {
    super.a(par1NBTTagCompound);

    this.direction = par1NBTTagCompound.d("Direction");
    this.mode = par1NBTTagCompound.d("Mode");
    this.data = par1NBTTagCompound.f("Data");
    this.invertOutput = par1NBTTagCompound.o("InvertOutput");

    for (int i = 0; i < 4; i++)
    {
      this.PinState[i] = par1NBTTagCompound.d(new StringBuilder().append("State").append(String.valueOf(i)).toString());
      this.PinType[i] = par1NBTTagCompound.d(new StringBuilder().append("Type").append(String.valueOf(i)).toString());
      this.Pins[i] = par1NBTTagCompound.j(new StringBuilder().append("Pins").append(String.valueOf(i)).toString());
    }

    byte size = par1NBTTagCompound.d("TASKS-Size");

    for (byte i = 0; i < size; i = (byte)(i + 1))
    {
      this.TASKS.add(i, par1NBTTagCompound.j(new StringBuilder().append("TASK").append(String.valueOf(i)).toString()));
    }
  }

  public void b(ady par1NBTTagCompound)
  {
    super.b(par1NBTTagCompound);

    par1NBTTagCompound.a("Direction", this.direction);
    par1NBTTagCompound.a("Mode", this.mode);
    par1NBTTagCompound.a("Data", this.data);
    par1NBTTagCompound.a("InvertOutput", this.invertOutput);

    for (int i = 0; i < 4; i++)
    {
      par1NBTTagCompound.a(new StringBuilder().append("State").append(String.valueOf(i)).toString(), this.PinState[i]);
      par1NBTTagCompound.a(new StringBuilder().append("Type").append(String.valueOf(i)).toString(), this.PinType[i]);
      par1NBTTagCompound.a(new StringBuilder().append("Pins").append(String.valueOf(i)).toString(), this.Pins[i]);
    }

    byte size = (byte)this.TASKS.size();

    par1NBTTagCompound.a("TASKS-Size", size);

    for (byte i = 0; i < size; i = (byte)(i + 1))
    {
      par1NBTTagCompound.a(new StringBuilder().append("TASK").append(String.valueOf(i)).toString(), (String)this.TASKS.get(i));
    }
  }
}