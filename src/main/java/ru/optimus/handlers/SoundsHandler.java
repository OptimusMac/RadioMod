package ru.optimus.handlers;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import ru.optimus.MainRadioCar;

public class SoundsHandler {

    public static SoundEvent FIRST_SOUND;

    public static void registerSounds() {
        FIRST_SOUND = registerSound("entity.centaur.ambient");

    }

    private static SoundEvent registerSound(String name) {
        ResourceLocation resourceLocation = new ResourceLocation(MainRadioCar.MODID, name);
        SoundEvent event = new SoundEvent(resourceLocation);
        event.setRegistryName(name);
        ForgeRegistries.SOUND_EVENTS.register(event);
        return event;
    }

}
