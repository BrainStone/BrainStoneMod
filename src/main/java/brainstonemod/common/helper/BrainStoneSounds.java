package brainstonemod.common.helper;

import brainstonemod.BrainStone;
import brainstonemod.network.BrainStonePacketHelper;
import lombok.experimental.UtilityClass;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@UtilityClass
public class BrainStoneSounds {
	public static final SoundEvent nyan_intro = sound("nyan.intro");
	public static final SoundEvent nyan_loop = sound("nyan.loop");

	private static SoundEvent sound(String name) {
		ResourceLocation location = new ResourceLocation(BrainStone.RESOURCE_PACKAGE, name);
		SoundEvent event = new SoundEvent(location);
		GameRegistry.register(event, location);

		return event;
	}

	public static void playSoundForAll(Entity entity, SoundEvent sound) {
		playSoundForAll(entity, sound, 1.0f, 1.0f);
	}

	public static void playSoundForAll(Entity entity, SoundEvent sound, float volume, float pitch) {
		entity.world.playSound(null, entity.getPosition(), sound, entity.getSoundCategory(), volume, pitch);
	}

	public static void playSoundForPlayer(Entity entity, SoundEvent sound) {
		playSoundForPlayer(entity, sound, 1.0f, 1.0f);
	}

	public static void playSoundForPlayer(Entity player, SoundEvent sound, float volume, float pitch) {
		if (player instanceof EntityPlayerMP) {
			BrainStonePacketHelper.sendPacket(player, new SPacketSoundEffect(sound, player.getSoundCategory(),
					player.posX, player.posY, player.posZ, volume, pitch));
		}
	}
}
