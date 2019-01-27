package brainstonemod.common.advancement.criterion;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import brainstonemod.BrainStone;
import brainstonemod.common.helper.BrainStoneLifeCapacitorUpgrade;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

public class CriterionUpgradeBrainStoneLifeCapacitor
		implements ICriterionTrigger<CriterionUpgradeBrainStoneLifeCapacitor.Instance> {
	@Getter
	private final ResourceLocation id;
	private final Map<PlayerAdvancements, CriterionUpgradeBrainStoneLifeCapacitor.Listeners> listeners = Maps
			.newHashMap();

	public CriterionUpgradeBrainStoneLifeCapacitor() {
		id = new ResourceLocation(BrainStone.MOD_ID, "upgrade_brain_stone_life_capacitor");
	}

	@Override
	public void addListener(PlayerAdvancements playerAdvancementsIn,
			ICriterionTrigger.Listener<CriterionUpgradeBrainStoneLifeCapacitor.Instance> listener) {
		CriterionUpgradeBrainStoneLifeCapacitor.Listeners upgradeBrainStoneLifeCapacitor$listeners = listeners
				.get(playerAdvancementsIn);

		if (upgradeBrainStoneLifeCapacitor$listeners == null) {
			upgradeBrainStoneLifeCapacitor$listeners = new CriterionUpgradeBrainStoneLifeCapacitor.Listeners(
					playerAdvancementsIn);
			listeners.put(playerAdvancementsIn, upgradeBrainStoneLifeCapacitor$listeners);
		}

		upgradeBrainStoneLifeCapacitor$listeners.add(listener);
	}

	@Override
	public void removeListener(PlayerAdvancements playerAdvancementsIn,
			ICriterionTrigger.Listener<CriterionUpgradeBrainStoneLifeCapacitor.Instance> listener) {
		CriterionUpgradeBrainStoneLifeCapacitor.Listeners upgradeBrainStoneLifeCapacitor$listeners = listeners
				.get(playerAdvancementsIn);

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
	public CriterionUpgradeBrainStoneLifeCapacitor.Instance deserializeInstance(JsonObject json,
			JsonDeserializationContext context) {
		try {
			BrainStoneLifeCapacitorUpgrade.Upgrade type = BrainStoneLifeCapacitorUpgrade.Upgrade
					.valueOf(JsonUtils.getString(json, "type").trim().toUpperCase());
			int level = JsonUtils.getInt(json, "level");

			return new CriterionUpgradeBrainStoneLifeCapacitor.Instance(getId(), type, level);
		} catch (IllegalArgumentException e) {
			throw new JsonSyntaxException("Invalid value for type, expected to be either \"capacity\" or \"charging\"",
					e);
		}
	}

	public void trigger(EntityPlayerMP parPlayer, BrainStoneLifeCapacitorUpgrade.Upgrade type, int level) {
		final Minecraft minecraft = Minecraft.getMinecraft();

		if (!minecraft.isCallingFromMinecraftThread()) {
			minecraft.addScheduledTask(() -> trigger(parPlayer, type, level));

			return;
		}

		CriterionUpgradeBrainStoneLifeCapacitor.Listeners upgradeBrainStoneLifeCapacitor$listeners = listeners
				.get(parPlayer.getAdvancements());

		if (upgradeBrainStoneLifeCapacitor$listeners != null) {
			upgradeBrainStoneLifeCapacitor$listeners.trigger(parPlayer, type, level);
		}
	}

	public static class Instance extends AbstractCriterionInstance {
		private final BrainStoneLifeCapacitorUpgrade.Upgrade type;
		private final int level;

		public Instance(ResourceLocation parID, BrainStoneLifeCapacitorUpgrade.Upgrade type, int level) {
			super(parID);

			this.type = type;
			this.level = level;
		}

		public boolean test(BrainStoneLifeCapacitorUpgrade.Upgrade type, int level) {
			return this.type.equals(type) && (this.level <= level);
		}
	}

	@RequiredArgsConstructor
	static class Listeners {
		private final PlayerAdvancements playerAdvancements;
		private final Collection<ICriterionTrigger.Listener<CriterionUpgradeBrainStoneLifeCapacitor.Instance>> listeners = Sets
				.newHashSet();

		public boolean isEmpty() {
			return listeners.isEmpty();
		}

		public void add(ICriterionTrigger.Listener<CriterionUpgradeBrainStoneLifeCapacitor.Instance> listener) {
			listeners.add(listener);
		}

		public void remove(ICriterionTrigger.Listener<CriterionUpgradeBrainStoneLifeCapacitor.Instance> listener) {
			listeners.remove(listener);
		}

		public void trigger(EntityPlayerMP player, BrainStoneLifeCapacitorUpgrade.Upgrade type, int level) {
			List<ICriterionTrigger.Listener<CriterionUpgradeBrainStoneLifeCapacitor.Instance>> toGrant = new LinkedList<>();

			for (ICriterionTrigger.Listener<CriterionUpgradeBrainStoneLifeCapacitor.Instance> listener : listeners) {
				if (listener.getCriterionInstance().test(type, level)) {
					toGrant.add(listener);
				}
			}

			for (ICriterionTrigger.Listener<CriterionUpgradeBrainStoneLifeCapacitor.Instance> listener : toGrant) {
				listener.grantCriterion(playerAdvancements);
			}
		}
	}
}
