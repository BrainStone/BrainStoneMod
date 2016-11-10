package brainstonemod.common.compat.overlord;

import java.util.List;
import java.util.Random;

import brainstonemod.BrainStone;
import brainstonemod.common.block.BlockPulsatingBrainStone;
import brainstonemod.common.compat.BrainStoneModules;
import brainstonemod.common.helper.BSP;
import brainstonemod.network.BrainStonePacketHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import the_fireplace.overlord.entity.EntityArmyMember;
import the_fireplace.overlord.registry.AugmentRegistry;
import the_fireplace.overlord.tools.Augment;

/**
 * @author The_Fireplace
 */
@Optional.Interface(iface = "the_fireplace.overlord.tileentity.ISkeletonMaker", modid = BrainStoneModules.OVERLORD_MODID)
public class BrainStoneAugment extends Augment {
    private boolean effect;
    private final Random random;

    public BrainStoneAugment(){
        random = new Random();
        effect = false;
        AugmentRegistry.registerAugment(new ItemStack(BrainStone.pulsatingBrainStone()), this);
    }

    @Override
    @Optional.Method(modid = BrainStoneModules.OVERLORD_MODID)
    public void onEntityTick(EntityArmyMember entityArmyMember) {
        updateTick(entityArmyMember.worldObj, entityArmyMember.getPosition(), entityArmyMember);
    }

    @Override
    @Optional.Method(modid = BrainStoneModules.OVERLORD_MODID)
    public void onStrike(EntityArmyMember entityArmyMember, Entity entity) {
    	// Do nothing
    }

    @Override
    @Optional.Method(modid = BrainStoneModules.OVERLORD_MODID)
    public String augmentId() {
        return "pulsatingbrainstone";
    }

    public void updateTick(World world, BlockPos pos, EntityArmyMember augmented) {
        final int metaData = (int) ((world.getTotalWorldTime() / 2) % 16);

        if (metaData >= 15) {
            if (effect) {
                if (random.nextInt(2) == 0) {
                    effect=false;
                }
            } else {
                if (random.nextInt(4) == 0) {
                    effect=true;
                }
            }
        } else if (metaData == 8 && effect) {
            BSP.debug("", "Effect Time!");

            double radius;
            int taskRand;
            EntityLivingBase entity;
            Object tmpEntity;
            final List<?> list = world.getEntitiesWithinAABBExcludingEntity(augmented,
                    new AxisAlignedBB(pos.add(-5,-5,-5), pos.add(5,5,5)));

            for (Object aList : list) {
                tmpEntity = aList;

                BSP.debug(tmpEntity.getClass().getName());

                if (tmpEntity instanceof EntityLivingBase) {
                    entity = (EntityLivingBase) tmpEntity;

                    BSP.debug(entity, entity.getArmorInventoryList());

                    if (BlockPulsatingBrainStone.isProtected(entity.getArmorInventoryList())) {
                        BSP.debug("Mob/Player wears armor! No effect!");

                        continue;
                    }else if(entity instanceof EntityArmyMember && ((EntityArmyMember)entity).getAugment() instanceof BrainStoneAugment){
                        BSP.debug("Army Member has BrainStone Augment! No effect!");

                        continue;
                    }


                    radius = MathHelper.getRandomDoubleInRange(random, 2.0, 10.0);

                    if (entity.getDistance(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= radius) {
                        taskRand = random.nextInt(10);

                        if ((taskRand >= 0) && (taskRand < 6)) {
                            BSP.debug("Potion Effect");

                            entity.addPotionEffect(new PotionEffect(BlockPulsatingBrainStone.getRandomPotion(random), random.nextInt(5980) + 20,
                                    random.nextInt(4)));
                        } else if ((taskRand >= 6) && (taskRand < 10)) {
                            BSP.debug("Kick");

                            final double x1 = MathHelper.getRandomDoubleInRange(random, -1.5, 1.5);
                            final double y1 = MathHelper.getRandomDoubleInRange(random, 0.0, 3.0);
                            final double z1 = MathHelper.getRandomDoubleInRange(random, -1.5, 1.5);

                            if (tmpEntity instanceof EntityPlayer) {
                                BrainStonePacketHelper.sendPlayerUpdateMovementPacket((EntityPlayer) entity, x1, y1, z1);
                            } else {
                                entity.addVelocity(x1, y1, z1);
                            }
                        }
                    }
                }
            }
        }
    }
}
