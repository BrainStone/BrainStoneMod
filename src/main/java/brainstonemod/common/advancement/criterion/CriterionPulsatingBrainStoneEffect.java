package brainstonemod.common.advancement.criterion;

import brainstonemod.BrainStone;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

public class CriterionPulsatingBrainStoneEffect
    implements ICriterionTrigger<CriterionPulsatingBrainStoneEffect.Instance> {
  @Getter private final ResourceLocation id;
  private final Map<PlayerAdvancements, CriterionPulsatingBrainStoneEffect.Listeners> listeners =
      Maps.newHashMap();

  public CriterionPulsatingBrainStoneEffect() {
    id = new ResourceLocation(BrainStone.MOD_ID, "pulsating_brain_stone_effect");
  }

  @Override
  public void addListener(
      PlayerAdvancements playerAdvancementsIn,
      ICriterionTrigger.Listener<CriterionPulsatingBrainStoneEffect.Instance> listener) {
    CriterionPulsatingBrainStoneEffect.Listeners upgradeBrainStoneLifeCapacitor$listeners =
        listeners.get(playerAdvancementsIn);

    if (upgradeBrainStoneLifeCapacitor$listeners == null) {
      upgradeBrainStoneLifeCapacitor$listeners =
          new CriterionPulsatingBrainStoneEffect.Listeners(playerAdvancementsIn);
      listeners.put(playerAdvancementsIn, upgradeBrainStoneLifeCapacitor$listeners);
    }

    upgradeBrainStoneLifeCapacitor$listeners.add(listener);
  }

  @Override
  public void removeListener(
      PlayerAdvancements playerAdvancementsIn,
      ICriterionTrigger.Listener<CriterionPulsatingBrainStoneEffect.Instance> listener) {
    CriterionPulsatingBrainStoneEffect.Listeners upgradeBrainStoneLifeCapacitor$listeners =
        listeners.get(playerAdvancementsIn);

    if (upgradeBrainStoneLifeCapacitor$listeners != null) {
      upgradeBrainStoneLifeCapacitor$listeners.remove(listener);

      if (upgradeBrainStoneLifeCapacitor$listeners.isEmpty()) {
        listeners.remove(playerAdvancementsIn);
      }
    }
  }

  @Override
  public void removeAllListeners(PlayerAdvancements playerAdvancementsIn) {
    listeners.remove(playerAdvancementsIn);
  }

  @Override
  public CriterionPulsatingBrainStoneEffect.Instance deserializeInstance(
      JsonObject json, JsonDeserializationContext context) {
    boolean protectionRequired = JsonUtils.getBoolean(json, "protectionRequired", false);

    return new CriterionPulsatingBrainStoneEffect.Instance(getId(), protectionRequired);
  }

  public void trigger(EntityPlayerMP parPlayer, boolean protection) {
    final MinecraftServer server = parPlayer.getServer();

    if (!server.isCallingFromMinecraftThread()) {
      server.addScheduledTask(() -> trigger(parPlayer, protection));

      return;
    }

    CriterionPulsatingBrainStoneEffect.Listeners upgradeBrainStoneLifeCapacitor$listeners =
        listeners.get(parPlayer.getAdvancements());

    if (upgradeBrainStoneLifeCapacitor$listeners != null) {
      upgradeBrainStoneLifeCapacitor$listeners.trigger(parPlayer, protection);
    }
  }

  public static class Instance extends AbstractCriterionInstance {
    private final boolean requireProtection;

    public Instance(ResourceLocation parID, boolean requireProtection) {
      super(parID);

      this.requireProtection = requireProtection;
    }

    public boolean test(boolean protection) {
      return !requireProtection || protection;
    }
  }

  @RequiredArgsConstructor
  static class Listeners {
    private final PlayerAdvancements playerAdvancements;
    private final Collection<
            ICriterionTrigger.Listener<CriterionPulsatingBrainStoneEffect.Instance>>
        listeners = Sets.newHashSet();

    public boolean isEmpty() {
      return listeners.isEmpty();
    }

    public void add(
        ICriterionTrigger.Listener<CriterionPulsatingBrainStoneEffect.Instance> listener) {
      listeners.add(listener);
    }

    public void remove(
        ICriterionTrigger.Listener<CriterionPulsatingBrainStoneEffect.Instance> listener) {
      listeners.remove(listener);
    }

    public void trigger(EntityPlayerMP player, boolean protection) {
      List<ICriterionTrigger.Listener<CriterionPulsatingBrainStoneEffect.Instance>> toGrant =
          new LinkedList<>();

      for (ICriterionTrigger.Listener<CriterionPulsatingBrainStoneEffect.Instance> listener :
          listeners) {
        if (listener.getCriterionInstance().test(protection)) {
          toGrant.add(listener);
        }
      }

      for (ICriterionTrigger.Listener<CriterionPulsatingBrainStoneEffect.Instance> listener :
          toGrant) {
        listener.grantCriterion(playerAdvancements);
      }
    }
  }
}
