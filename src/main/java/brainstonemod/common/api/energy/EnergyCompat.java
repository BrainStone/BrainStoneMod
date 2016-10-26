package brainstonemod.common.api.energy;

import brainstonemod.BrainStone;
import brainstonemod.common.api.BrainStoneModules;
import brainstonemod.common.api.IModIntegration;
import brainstonemod.common.api.enderio.EnderIOItems;
import brainstonemod.common.helper.BrainStoneConfigHelper;
import brainstonemod.common.helper.BrainStoneLifeCapacitorUpgrade;
import brainstonemod.common.item.ItemBrainStoneLifeCapacitor;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static brainstonemod.BrainStone.brainProcessor;
import static brainstonemod.BrainStone.intelligentTools;
import static brainstonemod.BrainStone.stablePulsatingBrainStonePlate;

/**
 * @author The_Fireplace
 */
public class EnergyCompat implements IModIntegration {
    @Override
    public void preInit() {
        BrainStone.items.put("brainStoneLifeCapacitor", (new ItemBrainStoneLifeCapacitor()));
    }

    @Override
    public void init() {
        MinecraftForge.EVENT_BUS.register(new EnergyEvents());
        GameRegistry.addRecipe(new BrainStoneLifeCapacitorUpgrade(BrainStoneLifeCapacitorUpgrade.Upgrade.CAPACITY));
        GameRegistry.addRecipe(new BrainStoneLifeCapacitorUpgrade(BrainStoneLifeCapacitorUpgrade.Upgrade.CHARGING));

        //TODO: Test this with energy without EnderIO and make sure this isn't a problem.
        Object craftingS = (BrainStoneModules.enderIO()) ? EnderIOItems.getSentientEnder()
                : new ItemStack(Items.SKULL, 1, 1);
        Object craftingX = (BrainStoneModules.enderIO()) ? EnderIOItems.getXPRod() : Items.BLAZE_ROD;
        Object craftingC = (BrainStoneModules.enderIO()) ? EnderIOItems.getOctadicCapacitor() : "dustRedstone";
        Object craftingH = /*(BrainStoneModules.tinkersConstruct()) ? TinkersConstructItems.getGreenHeartCanister()
					:*/ new ItemStack(Items.GOLDEN_APPLE, 1, 1);

        BrainStone.addRecipe(new ItemStack(brainStoneLifeCapacitor(), 1), "SBX", "CHC", " P ", 'S', craftingS, 'B',
                brainProcessor(), 'X', craftingX, 'C', craftingC, 'H', craftingH, 'P',
                stablePulsatingBrainStonePlate());
    }

    @Override
    public void postInit() {

    }

    @Override
    public void serverStarting() {
        brainStoneLifeCapacitor().newPlayerCapacitorMapping(DimensionManager.getCurrentSaveRootDirectory());
    }

    @Override
    public void addAchievement() {
        String curAch;
        BrainStone.achievements.put(curAch = "lifeCapacitor",
                (new Achievement(curAch, curAch, BrainStoneConfigHelper.getAchievementYPosition(curAch),
                        BrainStoneConfigHelper.getAchievementXPosition(curAch), brainStoneLifeCapacitor(),
                        intelligentTools())).setSpecial().registerStat());
    }

    /**
     * @return the instance of Brain Stone Life Capacitor
     */
    public static final ItemBrainStoneLifeCapacitor brainStoneLifeCapacitor() {
        return (ItemBrainStoneLifeCapacitor) BrainStone.items.get("brainStoneLifeCapacitor");
    }
}
