package net.nuggetmc.core.protocol;

import java.lang.reflect.Field;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;

public class PacketHandler {
	
	public void removePlayer(Player player) {
		Channel channel = ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel;
		channel.eventLoop().submit(() -> {
			channel.pipeline().remove(player.getName());
			return null;
		});
	}

	public void injectPlayer(Player player) {
		ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
			@Override
			public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
				super.channelRead(channelHandlerContext, packet);
				return;
			}

			@Override
			public void write(ChannelHandlerContext channelHandlerContext, Object packet, ChannelPromise channelPromise) throws Exception {
				if (packet instanceof PacketPlayOutChat) {
					PacketPlayOutChat chatPacket = (PacketPlayOutChat) packet;
					try {
						Class<?> cls = Class.forName("net.minecraft.server.v1_8_R3.PacketPlayOutChat");
						Field field = cls.getDeclaredField("a");
						field.setAccessible(true);
						Object object = field.get(chatPacket);
						
						if (object instanceof ChatComponentText) {
							ChatComponentText textComponent = (ChatComponentText) object;
							
							if (textComponent.a().size() > 0) {
								String message = textComponent.a().get(0).getText();
								
								if (message.equals(" ")) {
										return;
									}
								}
							}
							
						} catch (Exception e) {
							return;
						}
					}
					super.write(channelHandlerContext, packet, channelPromise);
					return;
				}
			};

		ChannelPipeline pipeline = ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel.pipeline();
		pipeline.addBefore("packet_handler", player.getName(), channelDuplexHandler);
		return;
	}
}
