package brainstonemod.common.api.energy;

import baubles.api.BaublesApi;
import brainstonemod.common.api.BrainStoneModules;
import brainstonemod.common.helper.BSP;
import brainstonemod.common.helper.BrainStoneDamageHelper;
import brainstonemod.common.item.ItemBrainStoneLifeCapacitor;
import brainstonemod.network.PacketDispatcher;
import brainstonemod.network.packet.clientbound.PacketCapacitorData;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.util.FoodStats;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.lang.reflect.Field;
import java.util.UUID;

import static brainstonemod.common.api.energy.EnergyCompat.brainStoneLifeCapacitor;

/**
 * @author The_Fireplace
 */
public class EnergyEvents {
    private static final Field reflectionFoodTimer = getUnlockedFoodTimer();
    private static final Field reflectionLastDamage = getUnlockedLastDamage();

    private static Field getUnlockedFoodTimer() {
        try {
            Field reflectionFoodTimer = FoodStats.class.getDeclaredFields()[3];
            reflectionFoodTimer.setAccessible(true);

            return reflectionFoodTimer;
        } catch (SecurityException e) {
            BSP.warnException(e);
        }

        return null;
    }

    private static Field getUnlockedLastDamage() {
        try {
            Field reflectionLastDamage = EntityLivingBase.class.getDeclaredFields()[40];
            reflectionLastDamage.setAccessible(true);

            return reflectionLastDamage;
        } catch (SecurityException e) {
            BSP.warnException(e);
        }

        return null;
    }
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onEntityDamage(LivingHurtEvent event) {
            if (event.getEntityLiving() instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) event.getEntityLiving();
                ItemStack capacitor = getBrainStoneLiveCapacitor(player);

                if (capacitor != null) {
                    float newDamage = brainStoneLifeCapacitor().handleDamage(capacitor, event.getAmount());

                    if (newDamage == 0.0F) {
                        event.setCanceled(true);
                    } else {
                        event.setAmount(newDamage);
                    }
                }
            }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onEntityAttack(LivingAttackEvent event) {
            try {
                if (event.getEntityLiving() instanceof EntityPlayer) {
                    EntityPlayer player = (EntityPlayer) event.getEntityLiving();
                    float ammount = event.getAmount();

                    if (event.getSource().isFireDamage() && player.isPotionActive(MobEffects.FIRE_RESISTANCE))
                        return;

                    if ((float) player.hurtResistantTime > ((float) player.maxHurtResistantTime / 2.0F)) {
                        if (ammount <= reflectionLastDamage.getFloat(player))
                            return;

                        ammount -= reflectionLastDamage.getFloat(player);
                        reflectionLastDamage.set(player, event.getAmount());
                    }

                    ItemStack capacitor = getBrainStoneLiveCapacitor(player);

                    if (capacitor != null) {
                        float adjustedDamage = BrainStoneDamageHelper.getAdjustedDamage(event.getSource(), ammount, player,
                                true);
                        float newDamage = brainStoneLifeCapacitor().handleDamage(capacitor, adjustedDamage,
                                true);

                        if (newDamage == 0.0F) {
                            event.setCanceled(true);

                            BrainStoneDamageHelper.getAdjustedDamage(event.getSource(), ammount, player, false);
                            brainStoneLifeCapacitor().handleDamage(capacitor, adjustedDamage, false);
                            player.hurtResistantTime = player.maxHurtResistantTime;
                            reflectionLastDamage.set(player, event.getAmount());
                        }
                    }
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                BSP.warnException(e);
            }
    }

    @SubscribeEvent
    public void onPlayerPreTick(TickEvent.PlayerTickEvent event) {
            if (event.phase == TickEvent.Phase.START) {
                EntityPlayer player = event.player;
                FoodStats stats = player.getFoodStats();
                ItemStack capacitor = getBrainStoneLiveCapacitor(player);

                if (capacitor != null) {
                    brainStoneLifeCapacitor().healPlayer(capacitor, player);

                    // Prevent natural regeneration
                    if ((reflectionFoodTimer != null) && (stats.getFoodLevel() >= 18)) {
                        try {
                            reflectionFoodTimer.set(stats, 0);
                        } catch (IllegalArgumentException | IllegalAccessException e) {
                            BSP.warnException(e);
                        }
                    }
                }
            }
    }

    private static ItemStack getBrainStoneLiveCapacitor(EntityPlayer player) {
        ItemBrainStoneLifeCapacitor capacitor = brainStoneLifeCapacitor();
        ItemBrainStoneLifeCapacitor.PlayerCapacitorMapping mapping = capacitor.getPlayerCapacitorMapping();
        UUID capacitorUUID = mapping.getCapacitorUUID(player.getUniqueID());

        if (capacitorUUID == null)
            return null;

        if (BrainStoneModules.baubles()) {
            ItemStack bStack = BaublesApi.getBaubles(player).getStackInSlot(3);

            if (checkStack(bStack, capacitorUUID)) {
                return bStack;
            }
        }

        for (ItemStack stack : player.inventory.mainInventory) {
            if (checkStack(stack, capacitorUUID)) {
                return stack;
            }
        }

        return null;
    }

    @SubscribeEvent
    public void playerJoinServer(PlayerEvent.PlayerLoggedInEvent event){
        brainStoneLifeCapacitor().getPlayerCapacitorMapping().updateName(event.player.getUniqueID(), false);
        PacketDispatcher
                .sendToAll(new PacketCapacitorData(brainStoneLifeCapacitor().getPlayerCapacitorMapping().getMap()));
    }

    private static boolean checkStack(ItemStack stack, UUID capacitorUUID) {
        return (stack != null) && (stack.getItem() != null) && (stack.getItem() == brainStoneLifeCapacitor())
                && (capacitorUUID.equals(brainStoneLifeCapacitor().getUUID(stack)));
    }
}
