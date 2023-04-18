package ru.optimus.handlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.lwjgl.input.Keyboard;
import ru.optimus.MainRadioCar;
import ru.optimus.cooldown.CooldownSystem;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import ru.optimus.packets.PacketSound;

import java.nio.channels.NetworkChannel;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class GenericEventHandler {

    private static CooldownSystem cooldownSystem = new CooldownSystem();
    private static CooldownSystem cooldownSystemPacket = new CooldownSystem();
    private static Set<VehicleRadio> links = new HashSet<>();


    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent e) {
        EntityPlayer player = e.player;
        if (player.world.isRemote) return;
        NBTTagCompound nbtTagCompound = player.getEntityData();
        if (isVehicle(player)) {
            Entity ridding = player.getRidingEntity();
            assert ridding != null;
            NBTTagCompound riddingData = ridding.getEntityData();
            if (!riddingData.getString("name").equals("")) {
                riddingData.setString("name", getRandomName());
            }
            String name = riddingData.getString("name");
            if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD0)) {
                if (cooldownSystem.has(player.getUniqueID())) return;

                VehicleRadio vehicleRadio = getVehicle(name);
                if (vehicleRadio == null) {
                    vehicleRadio = new VehicleRadio(ridding, ridding.getPosition().getX(), ridding.getPosition().getY(), ridding.getPosition().getZ(),
                        name);
                    links.add(vehicleRadio);
                } else {
                    vehicleRadio.setActive(!vehicleRadio.isActive());
                }
                cooldownSystem.add(player.getUniqueID(), 1);
                final String message = vehicleRadio.isActive() ? "§aРадио включено" : "§cРадио выключено";
                player.sendMessage(new TextComponentString(message));
            }
        }
    }


    @SubscribeEvent
    public void onSound(TickEvent.PlayerTickEvent e) {
        for (VehicleRadio vehicleRadio : links) {
            if (!vehicleRadio.isActive()) continue;

            List<EntityPlayer> nearbyPlayers = vehicleRadio.getEntity().getEntityWorld().getEntitiesWithinAABBExcludingEntity(vehicleRadio.getEntity(), vehicleRadio.getEntity().getEntityBoundingBox())
                .stream()
                .filter(d -> d instanceof EntityPlayer)
                .map(v -> (EntityPlayer) v)
                .collect(Collectors.toList());
            for (EntityPlayer player : nearbyPlayers) {
                float distance = player.getDistance(vehicleRadio.getEntity());
                float k = 1F;
                float volume = Math.min(1f, k / (distance * distance));
                player.sendMessage(new TextComponentString(vehicleRadio.getActiveUrl().getSoundName().getNamespace()));

                vehicleRadio.getEntity().world.playSound(player, vehicleRadio.getEntity().getPosition(), TFSounds.MUSIC, SoundCategory.PLAYERS, 1.0F, 1.0F);
                cooldownSystemPacket.add(player.getUniqueID(), 10);
            }
        }
    }


    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().register(TFSounds.MUSIC);
        System.out.println(event.getRegistry().getEntries());
    }


    public static boolean isVehicle(EntityPlayer player) {
        if (player.getRidingEntity() == null) return false;
        if (player.getRidingEntity().getClass().toString().contains("dev.toma.vehiclemod.common.entity.vehicle"))
            return true;
        return true;
    }

    private VehicleRadio getVehicle(String name) {
        for (VehicleRadio vehicleRadio : links) {
            if (vehicleRadio.getNameCar().equals(name)) {
                return vehicleRadio;
            }
        }
        return null;
    }



    private String getRandomName() {
        final String chars = "QWERTYUIOPASDFGHJKLZXCVBNM1234567890";
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            builder.append(chars.charAt(new Random().nextInt(chars.length() - 1)));
        }
        return builder.toString();
    }

}
