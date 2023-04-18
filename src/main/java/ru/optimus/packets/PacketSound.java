package ru.optimus.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.client.ForgeClientHandler;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import ru.optimus.handlers.Radio;

public class PacketSound implements IMessage {

    private String url;
    private float volume;

    public PacketSound() {
        // пустой конструктор (обязательно)
    }

    public PacketSound(String url, float volume) {
        this.url = url;
        this.volume = volume;

    }

    @Override
    public void fromBytes(ByteBuf buf) {
        url = ByteBufUtils.readUTF8String(buf);
        volume = buf.readFloat();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, url);
        buf.writeFloat(volume);
    }

    public static class Handler implements IMessageHandler<PacketSound, IMessage> {

        @Override
        public IMessage onMessage(PacketSound message, MessageContext ctx) {
            EntityPlayer player = FMLClientHandler.instance().getClientPlayerEntity();

            return null;
        }
    }
}
