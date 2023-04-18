package ru.optimus.handlers;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistryEntry;

@Mod.EventBusSubscriber(modid = "radiocar")
public class TFSounds {

    public static final SoundEvent MUSIC = createEvent("music.bg");


    private static SoundEvent createEvent(String sound) {
        ResourceLocation name = prefix(sound);
        return (SoundEvent) (new SoundEvent(name)).setRegistryName(name);
    }

    public static ResourceLocation prefix(String name) {
        return new ResourceLocation("radiocar", name);
    }

}
