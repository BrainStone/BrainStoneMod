package brainstonemod.common.advancement;

import brainstonemod.common.advancement.criterion.CriterionClaimBrainStoneLifeCapacitor;
import brainstonemod.common.advancement.criterion.CriterionEasterEgg;
import brainstonemod.common.advancement.criterion.CriterionPulsatingBrainStoneEffect;
import brainstonemod.common.advancement.criterion.CriterionUpgradeBrainStoneLifeCapacitor;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.experimental.UtilityClass;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.ICriterionTrigger;

@SuppressFBWarnings(value = "MS_CANNOT_BE_FINAL", justification = "No easy way to fix this")
@UtilityClass
public class CriterionRegistry {
  public static CriterionClaimBrainStoneLifeCapacitor CLAIM_BRAIN_STONE_LIFE_CAPACITOR;
  public static CriterionEasterEgg EASTER_EGG;
  public static CriterionPulsatingBrainStoneEffect PULSATING_BRAIN_STONE_EFFECT;
  public static CriterionUpgradeBrainStoneLifeCapacitor UPGRADE_BRAIN_STONE_LIFE_CAPACITOR;

  public static void init() {
    CLAIM_BRAIN_STONE_LIFE_CAPACITOR = register(new CriterionClaimBrainStoneLifeCapacitor());
    EASTER_EGG = register(new CriterionEasterEgg());
    PULSATING_BRAIN_STONE_EFFECT = register(new CriterionPulsatingBrainStoneEffect());
    UPGRADE_BRAIN_STONE_LIFE_CAPACITOR = register(new CriterionUpgradeBrainStoneLifeCapacitor());
  }

  @SuppressWarnings("rawtypes")
  public <T extends ICriterionTrigger> T register(T criterion) {
    return CriteriaTriggers.register(criterion);
  }
}
