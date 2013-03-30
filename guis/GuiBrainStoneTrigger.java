package mods.brainstone.guis;

import ava;
import avy;
import awv;
import bkd;
import bo;
import java.util.LinkedHashMap;
import java.util.Set;
import mods.brainstone.containers.ContainerBlockBrainStoneTrigger;
import mods.brainstone.handlers.BrainStonePacketHandler;
import mods.brainstone.templates.BrainStoneButton;
import mods.brainstone.templates.BrainStoneGuiButton;
import mods.brainstone.templates.GuiBrainStoneBase;
import mods.brainstone.tileentities.TileEntityBlockBrainStoneTrigger;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import so;

public class GuiBrainStoneTrigger extends GuiBrainStoneBase
{
  public static LinkedHashMap triggerEntities;
  private final TileEntityBlockBrainStoneTrigger tileentity;
  private int j;
  private int k;
  private int page;
  private final int max_page;
  private final String[] Mobs;
  private final BrainStoneGuiButton buttons;

  public GuiBrainStoneTrigger(so inventoryplayer, TileEntityBlockBrainStoneTrigger tileentityblockbrainstonetrigger)
  {
    super(new ContainerBlockBrainStoneTrigger(inventoryplayer, tileentityblockbrainstonetrigger));

    this.tileentity = tileentityblockbrainstonetrigger;
    this.j = 0;
    this.k = 0;
    this.Mobs = new String[4];
    this.page = 0;
    this.max_page = (triggerEntities.size() / 4 + (triggerEntities.size() % 4 == 0 ? -1 : 0));

    this.buttons = new BrainStoneGuiButton(this);
    this.buttons.addButton(new BrainStoneButton(0, 10, 32, 131, 9, 194, 9, 204, 9));

    this.buttons.addButton(new BrainStoneButton(1, 10, 32, 131, 45, 194, 45, 204, 45));

    for (int i = 0; i < 4; i++) {
      this.buttons.addButton(new BrainStoneButton(i + 10, 8, 8, 12, 12 + i * 18, 0, 166, 0, 174).changeTriggerBox(7, 7 + i * 18, 128, 25 + i * 18));
    }

    this.buttons.addButton(new BrainStoneButton(20, 9, 6, 160, 65, 176, 65, 185, 65));

    this.buttons.addButton(new BrainStoneButton(21, 9, 6, 160, 73, 176, 73, 185, 73));

    setMobs();

    int tmp = this.tileentity.max_delay;

    if (tmp < 1)
      tmp = 1;
    else if (tmp > 9) {
      tmp = 9;
    }

    this.buttons.getButton(21).inactive = (tmp == 1);
    this.buttons.getButton(20).inactive = (tmp == 9);
  }

  public void buttonClicked(int ID) {
    if (ID == 0) {
      this.page -= 1;
      setMobs();
    } else if (ID == 1) {
      this.page += 1;
      setMobs();
    } else if ((ID >= 10) && (ID < 14)) {
      this.tileentity.invertMobTriggered(this.Mobs[(ID - 10)]);
    } else if (ID >= 20) {
      int tmp = this.tileentity.max_delay + (ID * 2 - 41) * -1;

      if (tmp < 1)
        tmp = 1;
      else if (tmp > 9) {
        tmp = 9;
      }

      this.buttons.getButton(21).inactive = (tmp == 1);
      this.buttons.getButton(20).inactive = (tmp == 9);

      this.tileentity.max_delay = ((byte)tmp);
    }
  }

  private void click() {
    this.f.B.a("random.click", 1.0F, 1.0F);
  }

  protected void a(float f, int i, int l)
  {
    if (this.tileentity == null) {
      return;
    }
    registerTexture();
    this.j = ((this.g - this.b) / 2);
    this.k = ((this.h - this.c) / 2);
    b(this.j, this.k, 0, 0, this.b, this.c);

    this.buttons.render(this.j, this.k);

    for (int t = 0; t < 4; t++)
      if (this.tileentity.getMobTriggered(this.Mobs[t]))
        b(this.j + 12, this.k + 12 + 18 * t, 8, 166, 10, 7);
  }

  protected void b(int par1, int par2)
  {
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

    if (this.tileentity == null)
      return;
    String tmp;
    this.l.b(bo.a(tmp = this.Mobs[0]), 25, 13, this.tileentity.getMobTriggered(tmp) ? 16777215 : 8947848);

    this.l.b(bo.a(tmp = this.Mobs[1]), 25, 31, this.tileentity.getMobTriggered(tmp) ? 16777215 : 8947848);

    this.l.b(bo.a(tmp = this.Mobs[2]), 25, 49, this.tileentity.getMobTriggered(tmp) ? 16777215 : 8947848);

    this.l.b(bo.a(tmp = this.Mobs[3]), 25, 67, this.tileentity.getMobTriggered(tmp) ? 16777215 : 8947848);

    this.l.b(this.page + 1 + "/" + (this.max_page + 1), 146, 12, 0);

    this.l.b("Delay", 144, 54, 0);
    this.l.b(String.valueOf(this.tileentity.max_delay), 148, 68, 16777215);
  }

  private boolean inField(int i, int l, int i1, int j1, int k1, int l1)
  {
    return (i >= i1) && (i <= k1) && (l >= j1) && (l <= l1);
  }

  protected void a(char c, int i)
  {
    super.a(c, i);

    if ((i == 1) || (i == this.f.z.N.d))
      quit();
  }

  protected void a(int i, int l, int i1)
  {
    super.a(i, l, i1);

    if (this.tileentity == null) {
      return;
    }
    if (i1 == 0) {
      i -= (this.g - this.b) / 2;
      l -= (this.h - this.c) / 2;

      this.buttons.onClick(i, l);

      if (inField(i, l, 167, 4, 171, 8)) {
        quit();
      }

      BrainStonePacketHandler.sendUpdateOptions(this.tileentity);
    }
  }

  protected void b(int i, int l, int i1)
  {
    super.b(i, l, i1);

    this.buttons.hover();
  }

  private void quit() {
    click();
    this.f.a(null);
    this.f.h();
  }

  private void setMobs() {
    if (this.page < 0) {
      this.page = 0;
    }

    if (this.page > this.max_page) {
      this.page = this.max_page;
    }

    this.buttons.getButton(0).inactive = (this.page == 0);
    this.buttons.getButton(1).inactive = (this.page == this.max_page);

    int length = triggerEntities.size();
    String[] keys = (String[])triggerEntities.keySet().toArray(new String[length]);

    int page4 = this.page * 4;

    if ((this.page == this.max_page) && (length != (this.max_page - 1) * 4))
    {
      for (int i = 0; i < 4; i++) {
        int tmp = i + page4;

        if (tmp < length) {
          this.Mobs[i] = keys[tmp];
          this.buttons.getButton(i + 10).inactive = false;
        } else {
          this.Mobs[i] = "";
          this.buttons.getButton(i + 10).inactive = true;
        }
      }
    }
    else for (int i = 0; i < 4; i++) {
        this.Mobs[i] = keys[(i + page4)];
        this.buttons.getButton(i + 10).inactive = false;
      }
  }
}