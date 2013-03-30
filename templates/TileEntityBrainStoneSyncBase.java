package mods.brainstone.templates;

import aqp;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class TileEntityBrainStoneSyncBase extends aqp
{
  protected abstract void generateOutputStream(DataOutputStream paramDataOutputStream)
    throws IOException;

  public abstract void readFromInputStream(DataInputStream paramDataInputStream)
    throws IOException;

  public abstract void update(boolean paramBoolean)
    throws IOException;
}