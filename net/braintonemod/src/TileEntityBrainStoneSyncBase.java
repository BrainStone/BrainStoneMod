package net.braintonemod.src;

import anq;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class TileEntityBrainStoneSyncBase extends anq
{
  public abstract void readFromInputStream(DataInputStream paramDataInputStream)
    throws IOException;

  protected abstract void generateOutputStream(DataOutputStream paramDataOutputStream)
    throws IOException;

  public abstract void update(boolean paramBoolean)
    throws IOException;
}