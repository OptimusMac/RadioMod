package ru.optimus;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.DefaultResourcePack;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ru.optimus.handlers.ExampleConfig;
import ru.optimus.handlers.GenericEventHandler;
import ru.optimus.handlers.RegisterHandler;
import ru.optimus.handlers.SoundsHandler;
import ru.optimus.packets.PacketSound;
import ru.optimus.proxy.CommonProxy;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

@Mod(modid = MainRadioCar.MODID, version = MainRadioCar.VERSION, name = MainRadioCar.NAME)
public class MainRadioCar {
    public static final String MODID = "radiocar";
    public static final String NAME = "Radio in car";
    public static final String VERSION = "1.0.0";

    public static SimpleNetworkWrapper packetHandler;

    @SidedProxy(clientSide = "ru.optimus.proxy.ClientProxy", serverSide = "ru.optimus.proxy.CommonProxy")
    public static CommonProxy proxy;

    public static final Logger logger = LogManager.getLogger("RadioCar");
    public static final int howCoolAmI = Integer.MAX_VALUE;
    public static List<SoundEvent> sounds = new ArrayList<>();

    @EventHandler
    public void load(FMLInitializationEvent event) {
        RegisterHandler.preInitRegistries();
    }

    @EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
        // NO-OP
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ExampleConfig.load(event);

        packetHandler = NetworkRegistry.INSTANCE.newSimpleChannel("RadioCarChannel");
        packetHandler.registerMessage(PacketSound.Handler.class, PacketSound.class, 1, Side.CLIENT);

        MinecraftForge.EVENT_BUS.register(new GenericEventHandler());
        NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);
        createFile();
    }

    public static CreativeTabs tabExampleMod = new CreativeTabs("tabCar") {
        @Override
        @SideOnly(Side.CLIENT)
        public ItemStack createIcon() {
            return new ItemStack(Items.BAKED_POTATO);
        }
    };


    private void createFile() {
        File file = new File("sounds");
        if (!file.exists())
            file.mkdir();
    }


    private void registerSounds() {
        ResourceLocation[] soundFiles = new ResourceLocation[]{
            new ResourceLocation(MODID, "sounds/sound2.ogg"),
            new ResourceLocation(MODID, "sounds/ambient.ogg"),
        };

        for (ResourceLocation location : soundFiles) {
            SoundEvent soundEvent = new SoundEvent(location);
            soundEvent.setRegistryName(location);
            sounds.add(soundEvent);
            ForgeRegistries.SOUND_EVENTS.register(soundEvent);
        }
    }
}
