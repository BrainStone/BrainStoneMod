package brainstonemod.common.advancement.criterion;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;

import brainstonemod.BrainStone;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;

public class CriterionClaimBrainStoneLifeCapacitor
		implements ICriterionTrigger<CriterionClaimBrainStoneLifeCapacitor.Instance> {
	@Getter
	private final ResourceLocation id;
	private final Map<PlayerAdvancements, CriterionClaimBrainStoneLifeCapacitor.Listeners> listeners = Maps
			.newHashMap();

	public CriterionClaimBrainStoneLifeCapacitor() {
		id = new ResourceLocation(BrainStone.MOD_ID, "claim_brain_stone_life_capacitor");
	}

	@Override
	public void addListener(PlayerAdvancements playerAdvancementsIn,
			ICriterionTrigger.Listener<CriterionClaimBrainStoneLifeCapacitor.Instance> listener) {
		CriterionClaimBrainStoneLifeCapacitor.Listeners claimBrainStoneLifeCapacitor$listeners = listeners
				.get(playerAdvancementsIn);

		if (claimBrainStoneLifeCapacitor$listeners == null) {
			claimBrainStoneLifeCapacitor$listeners = new CriterionClaimBrainStoneLifeCapacitor.Listeners(
					playerAdvancementsIn);
			listeners.put(playerAdvancementsIn, claimBrainStoneLifeCapacitor$listeners);
		}

		claimBrainStoneLifeCapacitor$listeners.add(listener);
	}

	@Override
	public void removeListener(PlayerAdvancements playerAdvancementsIn,
			ICriterionTrigger.Listener<CriterionClaimBrainStoneLifeCapacitor.Instance> listener) {
		CriterionClaimBrainStoneLifeCapacitor.Listeners claimBrainStoneLifeCapacitor$listeners = listeners
				.get(playerAdvancementsIn);

		if (claimBrainStoneLifeCapacitor$listeners != null) {
			claimBrainStoneLifeCapacitor$listeners.remove(listener);

			if (claimBrainStoneLifeCapacitor$listeners.isEmpty()) {
				listeners.remove(playerAdvancementsIn);
			}
		}
	}

	@Override
	public void removeAllListeners(PlayerAdvancements playerAdvancementsIn) {
		listeners.remove(playerAdvancementsIn);
	}

	@Override
	public CriterionClaimBrainStoneLifeCapacitor.Instance deserializeInstance(JsonObject json,
			JsonDeserializationContext context) {
		return new CriterionClaimBrainStoneLifeCapacitor.Instance(getId());
	}

	public void trigger(EntityPlayerMP parPlayer) {
		CriterionClaimBrainStoneLifeCapacitor.Listeners claimBrainStoneLifeCapacitor$listeners = listeners
				.get(parPlayer.getAdvancements());

		if (claimBrainStoneLifeCapacitor$listeners != null) {
			claimBrainStoneLifeCapacitor$listeners.trigger(parPlayer);
		}
	}

	public static class Instance extends AbstractCriterionInstance {
		public Instance(ResourceLocation parID) {
			super(parID);
		}

		public boolean test() {
			return true;
		}
	}

	@RequiredArgsConstructor
	static class Listeners {
		private final PlayerAdvancements playerAdvancements;
		private final Collection<ICriterionTrigger.Listener<CriterionClaimBrainStoneLifeCapacitor.Instance>> listeners = Sets
				.newHashSet();

		public boolean isEmpty() {
			return listeners.isEmpty();
		}

		public void add(ICriterionTrigger.Listener<CriterionClaimBrainStoneLifeCapacitor.Instance> listener) {
			listeners.add(listener);
		}

		public void remove(ICriterionTrigger.Listener<CriterionClaimBrainStoneLifeCapacitor.Instance> listener) {
			listeners.remove(listener);
		}

		public void trigger(EntityPlayerMP player) {
			List<ICriterionTrigger.Listener<CriterionClaimBrainStoneLifeCapacitor.Instance>> toGrant = new LinkedList<>();

			for (ICriterionTrigger.Listener<CriterionClaimBrainStoneLifeCapacitor.Instance> listener : listeners) {
				if (listener.getCriterionInstance().test()) {
					toGrant.add(listener);
				}
			}

			for (ICriterionTrigger.Listener<CriterionClaimBrainStoneLifeCapacitor.Instance> listener : toGrant) {
				listener.grantCriterion(playerAdvancements);
			}
		}
	}
}
