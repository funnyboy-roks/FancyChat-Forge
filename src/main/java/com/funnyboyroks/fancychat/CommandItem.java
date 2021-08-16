package com.funnyboyroks.fancychat;

import com.mojang.brigadier.CommandDispatcher;
import com.sun.java.accessibility.util.java.awt.TextComponentTranslator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MinecraftGame;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.MessageArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.World;
import net.minecraftforge.fml.loading.FMLCommonLaunchHandler;
import net.minecraftforge.fml.network.FMLHandshakeHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.logging.log4j.core.jmx.Server;

public class CommandItem {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(
            Commands.literal("item").then(
                Commands.argument("message", MessageArgument.message())
                    .executes(
                        (context) -> {
                            ServerPlayerEntity player;
                            try {
                                player = context.getSource().getPlayerOrException();
                            }catch (Exception ignored) {
                                return -1;
                            }
                            ItemStack item = player.getMainHandItem();
                            ItemStackHandler handler = new ItemStackHandler();
                            ITextComponent iComp = new StringTextComponent("");
                            IFormattableTextComponent c = (IFormattableTextComponent) iComp;
//                            (new HoverEvent(HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemHover(item)));
                            new StringTextComponent("").setStyle(null);

                            ITextComponent comp = MessageArgument.getMessage(context, "message");
                            int i = 0;



                            for (ServerPlayerEntity p : player.server.getPlayerList().getPlayers()) {
                                p.sendMessage(comp, p.getUUID());
                                ++i;
                            }

                            return i;
                        })));
    }

}
