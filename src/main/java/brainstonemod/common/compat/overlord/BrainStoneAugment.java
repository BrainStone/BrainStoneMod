package brainstonemod.common.compat.overlord;

import java.util.Random;

import brainstonemod.BrainStoneBlocks;
import brainstonemod.common.block.BlockPulsatingBrainStone;
import brainstonemod.common.compat.BrainStoneModules;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
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

	public BrainStoneAugment() {
		random = new Random();
		effect = false;
		AugmentRegistry.registerAugment(new ItemStack(BrainStoneBlocks.pulsatingBrainStone()), this);
	}

	@Override
	@Optional.Method(modid = BrainStoneModules.OVERLORD_MODID)
	public void onEntityTick(EntityArmyMember entityArmyMember) {
		updateTick(entityArmyMember.world, entityArmyMember.getPosition(), entityArmyMember);
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
					effect = false;
				}
			} else {
				if (random.nextInt(4) == 0) {
					effect = true;
				}
			}
		} else if ((metaData == 8) && effect) {
			BlockPulsatingBrainStone.applyEffect(world, pos, random);
		}
	}
}
