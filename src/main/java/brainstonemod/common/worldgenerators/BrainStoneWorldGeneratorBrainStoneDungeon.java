package brainstonemod.common.worldgenerators;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import brainstonemod.BrainStone;
import brainstonemod.common.helper.BSP;

public class BrainStoneWorldGeneratorBrainStoneDungeon extends WorldGenerator {
	private class SlotMemory {
		private final ArrayList<Integer> chestSlots;
		private Random random;

		public SlotMemory(int chestSize) {
			chestSlots = new ArrayList<>(chestSize);

			for (int i = 0; i < chestSize; i++) {
				chestSlots.add(i);
			}
		}

		public SlotMemory(int chestSize, Random random) {
			this(chestSize);

			this.random = random;
		}

		public int getRandomChestSlot() {
			final int size = chestSlots.size();

			if (size > 0) {
				final int slot = random.nextInt(size);

				final int temp = chestSlots.get(slot);
				chestSlots.remove(slot);

				return temp;
			}

			return -1;
		}
	}

	private int x, y, z;
	private World world;
	private Random random;
	private final int[] Options;

	public BrainStoneWorldGeneratorBrainStoneDungeon() {
		// height of the stairs

		Options = new int[] { 2 };
	}

	private boolean canPlaceSecretRoomHere() {
		int i, j, k;
		final int height = (Options[0] * 8) + 3;

		for (i = 3; i < 10; i++) {
			for (j = -1; j < 6; j++) {
				for (k = height + 1; k > (height - 8); k--)
					if (isNotSolid(x + i, y - k, z + j))
						return false;
			}
		}

		return true;
	}

	private boolean canPlaceShackHere() {
        int i, j, k;

        for (i = 0; i < 6; i++) {
            for (j = 0; j < 5; j++)
                if (isNotSolid(x + i, y - 1, z + j))
                    return false;
        }

        for (i = 0; i < 4; i++)
            if (isNotSolid(x + i, y - 1, z + 5))
                return false;

        for (i = 0; i < 6; i++) {
            for (j = 0; j < 5; j++) {
                for (k = 0; k < 4; k++)
                    if (!isReplaceable(x + i, y + k, z + j))
                        return false;
            }
        }

        for (i = 0; i < 4; i++) {
            for (j = 0; j < 4; j++)
                if (!isReplaceable(x + i, y + j, z + 5))
                    return false;
        }

        if (!(isReplaceable(x + 4, y + 2, z + 5) && isReplaceable(x + 5, y + 2,
                z + 5)))
            return false;

        for (i = -1; i < 5; i++)
            if (!isReplaceable(x + i, y + 2, z + 6))
                return false;

        for (i = -1; i < 6; i++)
            if (!isReplaceable(x - 1, y + 2, z + i))
                return false;

        for (i = 0; i < 7; i++)
            if (!isReplaceable(x + i, y + 2, z - 1))
                return false;

        for (i = 0; i < 6; i++)
            if (!isReplaceable(x + 6, y + 2, z + i))
                return false;

        for (i = 1; i < 4; i++) {
            for (j = 1; j < 5; j++)
                if (!isReplaceable(x + j, y + 4, z + i))
                    return false;
        }

        return (isReplaceable(x + 1, y + 4, z + 4) && isReplaceable(x + 2, y + 4,
                z + 4)) && isReplaceable(x + 2, y + 5, z + 2) && isReplaceable(x + 3, y + 5, z + 2);

    }

	private boolean canPlaceStairsHere() {
		int i, j, k;

		final int height = (Options[0] * 8) + 5;

		for (i = -1; i < 6; i++) {
			for (j = -1; j < 6; j++) {
				for (k = 2; k < height; k++)
					if (isNotSolid(x + i, y - k, z + j))
						return false;
			}
		}

		return true;
	}

	private boolean canPlaceStructHere(int structure, Object... options) {
		switch (structure) {
		case 0:
			return canPlaceShackHere();
		case 1:
			return canPlaceStairsHere() && canPlaceSecretRoomHere();
		}

		return false;
	}

	@Override
	public boolean generate(World world, Random random, int x, int y, int z) {
		this.world = world;
		this.random = random;
		this.x = x;
		this.y = world.getHeightValue(x, z);
		this.z = z;

		BSP.debug("Trying at " + x + ", " + this.y + ", " + z + "!");

		int counter = 0;
		int direction = 0;
		int directionCounter = 0;
		int maxDirection = 1;

		while (true) {
			if (directionCounter >= maxDirection) {
				directionCounter = 0;
				maxDirection += direction % 2;
				direction = (direction + 1) % 4;
			}

			this.y = world.getHeightValue(this.x, this.z);

			if (canPlaceStructHere(0)) {
				Options[0] = 3;

				if (canPlaceStructHere(1)) {
					break;
				}

				Options[0] = 2;

				if (canPlaceStructHere(1)) {
					break;
				}

				Options[0] = 4;

				if (canPlaceStructHere(1)) {
					break;
				}
			}

			switch (direction) {
			case 0:
				this.x++;
				break;
			case 1:
				this.z++;
				break;
			case 2:
				this.x--;
				break;
			case 3:
				this.z--;
				break;
			}

			if (counter >= 10000) {
				BSP.debug("Failed");

				return false;
			}

			counter++;
			directionCounter++;
		}

		generateShack();

		generateStairs();

		generateSecretRoom();

		BSP.debug("Placed at " + x + ", " + this.y + ", " + z + "!");

		return true;
	}

	private void generateSecretRoom() {
		int i, j, k;
		int height = (Options[0] * 8) + 3;

		for (i = 4; i < 9; i++) {
			for (j = 0; j < 5; j++) {
				for (k = height; k > (height - 7); k--) {
					setBlock(
							x + i,
							y - k,
							z + j,
							((i == 4) || (i == 8) || (j == 0) || (j == 4)
									|| (k == height) || (k == (height - 6))) ? Blocks.cobblestone
									: Blocks.air);
				}
			}
		}

		setBlock(x + 6, (y - height) + 6, z + 2, BrainStone.brainStone());
		setBlock(x + 6, y - height, z + 2, BrainStone.brainStone());

		setBlock(x + 7, y - height - 2, z + 2, BrainStone.pulsatingBrainStone());

		height -= 1;

		// Chests

		setBlock(x + 6, y - height, z + 1, Blocks.chest);
		TileEntityChest chest = (TileEntityChest) world.getTileEntity(x + 6, y
				- height, z + 1);

		int rand1 = random.nextInt(9) + 2;
		SlotMemory chestSlots = new SlotMemory(chest.getSizeInventory(), random);

		for (i = 0; i < rand1; i++) {
			chest.setInventorySlotContents(chestSlots.getRandomChestSlot(),
					getLoot(1));
		}

		setBlock(x + 7, y - height, z + 1, Blocks.chest);
		chest = (TileEntityChest) world.getTileEntity(x + 7, y - height, z + 1);

		rand1 = random.nextInt(9) + 2;
		int rand2 = random.nextInt(3);
		chestSlots = new SlotMemory(chest.getSizeInventory(), random);

		for (i = 0; i < rand1; i++) {
			chest.setInventorySlotContents(chestSlots.getRandomChestSlot(),
					getLoot(rand2));
		}

		setBlock(x + 6, y - height, z + 3, Blocks.chest);
		chest = (TileEntityChest) world.getTileEntity(x + 6, y - height, z + 3);

		rand1 = random.nextInt(9) + 2;
		rand2 = random.nextInt(3);
		chestSlots = new SlotMemory(chest.getSizeInventory(), random);

		for (i = 0; i < rand1; i++) {
			chest.setInventorySlotContents(chestSlots.getRandomChestSlot(),
					getLoot(rand2));
		}

		setBlock(x + 7, y - height, z + 3, Blocks.chest);
		chest = (TileEntityChest) world.getTileEntity(x + 7, y - height, z + 3);

		rand1 = random.nextInt(9) + 2;
		chestSlots = new SlotMemory(chest.getSizeInventory(), random);

		for (i = 0; i < rand1; i++) {
			chest.setInventorySlotContents(chestSlots.getRandomChestSlot(),
					getLoot(1));
		}
	}

	private void generateShack() {
		int i, j, k;

		// Basement

		for (j = 0; j < 2; j++) {
			for (i = 0; i < 6; i++) {
				setBlock(x + i, y + j, z, Blocks.planks);
			}

			for (i = 0; i < 6; i++) {
				setBlock(x, y + j, z + i, Blocks.planks);
			}

			for (i = 0; i < 4; i++) {
				setBlock(x + i, y + j, z + 5, Blocks.planks);
			}

			for (i = 0; i < 5; i++) {
				setBlock(x + 5, y + j, z + i, Blocks.planks);
			}

			for (i = 3; i < 6; i++) {
				setBlock(x + i, y + j, z + 4, Blocks.planks);
			}
		}

		// Door and Windows

		setBlock(x, y, z + 2, Blocks.air);
		setBlock(x, y + 1, z + 2, Blocks.air);
		setBlock(x + 2, y + 1, z, Blocks.air);
		setBlock(x + 5, y + 1, z + 2, Blocks.air);

		// Ceiling

		for (i = 0; i < 6; i++) {
			for (j = 0; j < 6; j++) {
				setBlock(x + i, y + 2, z + j, Blocks.planks);
			}
		}

		// Roof - Layer 0

		setBlockAndMetadata(x + 4, y + 2, z + 5, Blocks.oak_stairs, 1);
		setBlockAndMetadata(x + 5, y + 2, z + 5, Blocks.oak_stairs, 3);

		for (i = -1; i < 5; i++) {
			setBlockAndMetadata(x + i, y + 2, z + 6, Blocks.oak_stairs, 3);
		}

		for (i = -1; i < 6; i++) {
			setBlockAndMetadata(x - 1, y + 2, z + i, Blocks.oak_stairs, 0);
		}

		for (i = 0; i < 7; i++) {
			setBlockAndMetadata(x + i, y + 2, z - 1, Blocks.oak_stairs, 2);
		}

		for (i = 0; i < 6; i++) {
			setBlockAndMetadata(x + 6, y + 2, z + i, Blocks.oak_stairs, 1);
		}

		// Roof - Layer 1

		for (i = 1; i < 5; i++) {
			for (j = 1; j < 5; j++) {
				setBlock(x + i, y + 3, z + j, Blocks.planks);
			}
		}

		setBlockAndMetadata(x + 3, y + 3, z + 4, Blocks.oak_stairs, 1);
		setBlockAndMetadata(x + 4, y + 3, z + 4, Blocks.oak_stairs, 3);

		for (i = 0; i < 4; i++) {
			setBlockAndMetadata(x + i, y + 3, z + 5, Blocks.oak_stairs, 3);
		}

		for (i = 0; i < 5; i++) {
			setBlockAndMetadata(x, y + 3, z + i, Blocks.oak_stairs, 0);
		}

		for (i = 1; i < 6; i++) {
			setBlockAndMetadata(x + i, y + 3, z, Blocks.oak_stairs, 2);
		}

		for (i = 1; i < 5; i++) {
			setBlockAndMetadata(x + 5, y + 3, z + i, Blocks.oak_stairs, 1);
		}

		// Chest in Layer 1

		int rand = random.nextInt(14);
		final int chunkX = (new int[] { 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 4, 4,
				4 })[rand];
		final int chunkZ = (new int[] { 1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 1, 2,
				3 })[rand];

		setBlock(x + chunkX, y + 3, z + chunkZ, Blocks.chest);
		final TileEntityChest chest = (TileEntityChest) world.getTileEntity(x
				+ chunkX, y + 3, z + chunkZ);

		rand = random.nextInt(9) + 2;
		final SlotMemory chestSlots = new SlotMemory(chest.getSizeInventory(),
				random);

		for (i = 0; i < rand; i++) {
			chest.setInventorySlotContents(chestSlots.getRandomChestSlot(),
					getLoot(0));
		}

		// Roof - Layer 2

		setBlock(x + 2, y + 4, z + 2, Blocks.planks);
		setBlock(x + 3, y + 4, z + 2, Blocks.planks);

		setBlockAndMetadata(x + 2, y + 4, z + 3, Blocks.oak_stairs, 1);
		setBlockAndMetadata(x + 3, y + 4, z + 3, Blocks.oak_stairs, 3);
		setBlockAndMetadata(x + 2, y + 4, z + 4, Blocks.oak_stairs, 3);

		for (i = 2; i < 5; i++) {
			setBlockAndMetadata(x + 1, y + 4, z + i, Blocks.oak_stairs, 0);
		}

		for (i = 1; i < 5; i++) {
			setBlockAndMetadata(x + i, y + 4, z + 1, Blocks.oak_stairs, 2);
		}

		for (i = 2; i < 4; i++) {
			setBlockAndMetadata(x + 4, y + 4, z + i, Blocks.oak_stairs, 1);
		}

		// Roof - Layer 3 (Top)

		setBlock(x + 2, y + 5, z + 2, Blocks.wooden_slab);
		setBlock(x + 3, y + 5, z + 2, Blocks.wooden_slab);

		// Inside

		for (i = 1; i < 5; i++) {
			for (j = 1; j < 4; j++) {
				for (k = 0; k < 2; k++) {
					setBlock(x + i, y + k, z + j, Blocks.air);
				}
			}
		}

		for (i = 1; i < 3; i++) {
			for (j = 0; j < 2; j++) {
				setBlock(x + i, y + j, z + 4, Blocks.air);
			}
		}
	}

	private void generateStairs() {
		int i, j, k, l;

		// Top Layer

		for (i = 0; i < 5; i++) {
			for (j = 0; j < 5; j++) {
				setBlock(x + i, y - 2, z + j, Blocks.cobblestone);
			}
		}

		for (i = 1; i < 4; i++) {
			setBlock(x + 3, y - 2, z + i, Blocks.air);
		}

		setBlock(x + 3, y - 2, z + 2, Blocks.torch);
		setBlockAndMetadata(x + 2, y - 2, z + 1, Blocks.stone_stairs, 1);

		// Stairs Down

		final int[] tmpX = new int[] { 2, 3, 3, 3, 2, 1, 1, 1 };
		final int[] tmpZ = new int[] { 1, 1, 2, 3, 3, 3, 2, 1 };
		int tmp, tmp2, height;

		for (l = 0; l < Options[0]; l++) {
			height = (l * 8) + 3;

			// Walls

			for (i = 0; i < 5; i++) {
				for (j = 0; j < 5; j++) {
					for (k = 0; k < 8; k++) {
						setBlock(
								x + i,
								y - height - k,
								z + j,
								(((i == 1) || (i == 3) || (j == 1) || (j == 3)) && !((i == 0)
										|| (i == 4) || (j == 0) || (j == 4))) ? Blocks.air
										: Blocks.cobblestone);
					}
				}
			}

			// Actual Stairs

			for (i = 0; i < 8; i++) {
				setBlock(x + tmpX[i], y - height - i, z + tmpZ[i],
						Blocks.cobblestone);

				tmp = (i + 1) % 8;
				tmp2 = i / 2;
				setBlockAndMetadata(x + tmpX[tmp], y - height - i, z
						+ tmpZ[tmp], Blocks.stone_stairs, (tmp2 == 0) ? 3
						: (tmp2 == 1) ? 0 : (tmp2 == 2) ? 2 : 1);

				tmp = (i + 7) % 8;
				tmp2 = ((i + 1) / 2) % 4;
				setBlockAndMetadata(x + tmpX[tmp], y - height - i, z
						+ tmpZ[tmp], Blocks.stone_stairs, (tmp2 == 0) ? 7
						: (tmp2 == 1) ? 4 : (tmp2 == 2) ? 6 : 5);

				if ((i % 2) == 1) {
					tmp = (i + 3) % 8;
					tmp2 = i / 2;
					setBlockAndMetadata(x + tmpX[tmp], y - height - i, z
							+ tmpZ[tmp], Blocks.torch, (tmp2 == 0) ? 3
							: (tmp2 == 1) ? 2 : (tmp2 == 2) ? 4 : 1);
				}

			}
		}

		height = (Options[0] * 8) + 3;

		for (i = 0; i < 5; i++) {
			for (j = 0; j < 5; j++) {
				setBlock(x + i, y - height, z + j, Blocks.cobblestone);
			}
		}
	}

	private ItemStack getLoot(int lootId) {
		// Format: ItemStack, chance, min, rand1, rand2

		Object[][] loots = null;
		int size;

		switch (lootId) {
		case 0:
			loots = new Object[][] {
					{ new ItemStack(BrainStone.brainStone()), 0.1F, 1, 4, 5 },
					{ new ItemStack(BrainStone.dirtyBrainStone()), 1.0F, 1, 4,
							5 },
					{ new ItemStack(BrainStone.brainStoneDust()), 2.0F, 1, 6, 7 },
					{ new ItemStack(BrainStone.pulsatingBrainStone()), 0.05F,
							1, 1, 2 },
					{ new ItemStack(Items.diamond), 0.75F, 1, 4, 5 },
					{ new ItemStack(Items.emerald), 0.5F, 1, 4, 5 } };

			break;
		case 1:
			loots = new Object[][] {
					{ new ItemStack(BrainStone.brainStoneDust()), 0.1F, 1, 5, 6 },
					{ new ItemStack(Items.redstone), 1.0F, 3, 5, 7 },
					{ new ItemStack(Blocks.iron_ore), 1.0F, 3, 2, 5 },
					{ new ItemStack(Items.dye, 1, 4), 1.0F, 3, 5, 7 },
					{ new ItemStack(Blocks.gold_ore), 1.0F, 1, 1, 4 },
					{ new ItemStack(Items.diamond), 0.2F, 1, 2, 2 },
					{ new ItemStack(Items.dye, 1, 3), 1.0F, 3, 5, 7 },
					{ new ItemStack(Items.saddle), 0.2F, 1, 0, 0 },
					{ new ItemStack(Items.golden_apple), 0.1F, 1, 0, 0 },
					{ new ItemStack(Items.golden_apple, 1, 1), 0.01F, 1, 0, 0 } };

			break;
		case 2:
			loots = new Object[][] {
					{ new ItemStack(Blocks.mossy_cobblestone), 1.0F, 1, 19, 20 },
					{ new ItemStack(Blocks.glowstone), 1.0F, 1, 9, 10 },
					{ new ItemStack(Blocks.lit_pumpkin), 1.0F, 1, 9, 10 },
					{ new ItemStack(Blocks.ice), 1.0F, 1, 19, 20 },
					{ new ItemStack(Blocks.redstone_lamp), 1.0F, 1, 9, 10 },
					{ new ItemStack(Blocks.dragon_egg), 0.01F, 1, 0, 0 },
					{ new ItemStack(Items.nether_wart), 1.0F, 1, 9, 10 },
					{ new ItemStack(Items.slime_ball), 1.0F, 1, 9, 10 },
					{ new ItemStack(Items.book), 1.0F, 1, 9, 10 },
					{ new ItemStack(Items.blaze_rod), 1.0F, 1, 2, 2 },
					{ new ItemStack(Items.ender_pearl), 1.0F, 1, 2, 2 },

					{
							new ItemStack(BrainStone.brainStoneAxe(), 1,
									random.nextInt(5368)), 0.1F, 1, 0, 0 },
					{
							new ItemStack(BrainStone.brainStonePickaxe(), 1,
									random.nextInt(5368)), 0.1F, 1, 0, 0 },
					{
							new ItemStack(BrainStone.brainStoneShovel(), 1,
									random.nextInt(5368)), 0.1F, 1, 0, 0 },
					{
							new ItemStack(BrainStone.brainStoneHoe(), 1,
									random.nextInt(5368)), 0.1F, 1, 0, 0 },
					{
							new ItemStack(BrainStone.brainStoneSword(), 1,
									random.nextInt(5368)), 0.1F, 1, 0, 0 },
					{
							new ItemStack(BrainStone.brainStoneHelmet(), 1,
									random.nextInt(1824)), 0.1F, 1, 0, 0 },
					{
							new ItemStack(BrainStone.brainStonePlate(), 1,
									random.nextInt(1824)), 0.1F, 1, 0, 0 },
					{
							new ItemStack(BrainStone.brainStoneLeggings(), 1,
									random.nextInt(1824)), 0.1F, 1, 0, 0 },
					{
							new ItemStack(BrainStone.brainStoneBoots(), 1,
									random.nextInt(1824)), 0.1F, 1, 0, 0 },

					{
							EnchantmentHelper.addRandomEnchantment(random,
									new ItemStack(BrainStone.brainStoneAxe(),
											1, random.nextInt(5368)), 10),
							0.05F, 1, 0, 0 },
					{
							EnchantmentHelper.addRandomEnchantment(
									random,
									new ItemStack(BrainStone
											.brainStonePickaxe(), 1, random
											.nextInt(5368)), 10), 0.05F, 1, 0,
							0 },
					{
							EnchantmentHelper.addRandomEnchantment(random,
									new ItemStack(
											BrainStone.brainStoneShovel(), 1,
											random.nextInt(5368)), 10), 0.05F,
							1, 0, 0 },
					{
							EnchantmentHelper.addRandomEnchantment(random,
									new ItemStack(BrainStone.brainStoneSword(),
											1, random.nextInt(5368)), 10),
							0.05F, 1, 0, 0 },
					{
							EnchantmentHelper.addRandomEnchantment(random,
									new ItemStack(
											BrainStone.brainStoneHelmet(), 1,
											random.nextInt(1824)), 10), 0.05F,
							1, 0, 0 },
					{
							EnchantmentHelper.addRandomEnchantment(random,
									new ItemStack(BrainStone.brainStonePlate(),
											1, random.nextInt(1824)), 10),
							0.05F, 1, 0, 0 },
					{
							EnchantmentHelper.addRandomEnchantment(
									random,
									new ItemStack(BrainStone
											.brainStoneLeggings(), 1, random
											.nextInt(1824)), 10), 0.05F, 1, 0,
							0 },
					{
							EnchantmentHelper.addRandomEnchantment(random,
									new ItemStack(BrainStone.brainStoneBoots(),
											1, random.nextInt(1824)), 10),
							0.05F, 1, 0, 0 } };

			break;
		}

		if (loots == null)
			return null;

		float sum = 0.0F, rand, tmpChance;
		int i;
		ItemStack loot = null;
		Object[] tmpLoot;

		size = loots.length;

		for (i = 0; i < size; i++) {
			sum += (Float) loots[i][1];
		}

		rand = (float) MathHelper.getRandomDoubleInRange(random, 0.0, sum);

		for (i = 0; i < size; i++) {
			tmpLoot = loots[i];
			tmpChance = (Float) tmpLoot[1];

			if (rand < tmpChance) {
				loot = (ItemStack) tmpLoot[0];
				loot.stackSize = (Integer) tmpLoot[2]
						+ random.nextInt((Integer) tmpLoot[3] + 1)
						+ random.nextInt((Integer) tmpLoot[4] + 1);

				if (loot.isItemEqual(new ItemStack(Blocks.dragon_egg))) {
					BSP.debug("Dragon Egg!!!!");
				}

				break;
			}

			rand -= tmpChance;
		}

		return loot;
	}

	private boolean isReplaceable(int x, int y, int z) {
		return world.getBlock(x, y, z).getMaterial().isReplaceable();
	}

	private boolean isNotSolid(int x, int y, int z) {
		return !world.getBlock(x, y, z).getMaterial().isSolid();
	}

	/**
	 * Sets a Block at the specified Spot
	 */
	private void setBlock(int x, int y, int z, Block blockId) {
		setBlockAndMetadata(x, y, z, blockId, 0);
	}

	/**
	 * Sets a Block with MetaData at the specified Spot
	 */
	private void setBlockAndMetadata(int x, int y, int z, Block blockId,
			int metaData) {
		world.setBlock(x, y, z, blockId, metaData, 2);
	}
}