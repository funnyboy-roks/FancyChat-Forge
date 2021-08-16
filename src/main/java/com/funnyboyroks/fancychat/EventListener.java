package com.funnyboyroks.fancychat;

import net.minecraft.block.WallSignBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.NameTagItem;
import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventListener {

    private static String translateAlternates(String message) {
        return message.replace('&', '§');
    }

    public static void updateItemName(ItemStack stack) {
        String name = stack.getHoverName().getString();
        if (name.contains("&")) {
            stack.setHoverName(new StringTextComponent(translateAlternates(name)));
        }

    }

    @SubscribeEvent
    public void serverChat(ServerChatEvent event) {
        String name = String.format("<&b%s&f>", event.getUsername());
        String message = name + " " + event.getMessage();
        message = translateAlternates(message);
        TextComponent comp = new StringTextComponent(message);
        event.setComponent(comp);
    }

    @SubscribeEvent
    public void anvilRepair(AnvilRepairEvent event) {
        if (event.isCanceled()) return;
        updateItemName(event.getItemResult());
    }

    @SubscribeEvent
    public void blockEvent(BlockEvent event) {
        if (event.isCanceled()) return;
        if (!(event.getState().getBlock() instanceof WallSignBlock)) return;
        SignTileEntity sign = (SignTileEntity) event.getWorld().getBlockEntity(event.getPos());
        if (sign == null) return;
        for (int i = 0; i < 4; ++i) {
            IFormattableTextComponent comp = ITextComponent.Serializer.fromJson(sign.getTileData().getString("Text" + (i+1)));
            if(comp == null) continue;
            sign.setMessage(
                i,
                new StringTextComponent(
                    translateAlternates(
                        comp.getContents().replaceAll("^\\[(.*)]$", "$1")
                    )
                )
            );
        }
    }

    @SubscribeEvent
    public void entityInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.isCanceled()) return;
        if (event.getItemStack().getItem() instanceof NameTagItem) {
            event.getEntityLiving().setCustomName(new StringTextComponent(translateAlternates(event.getItemStack().getDisplayName().getContents())));
        }

    }

    @SubscribeEvent
    public void rightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.isCanceled()) return;
        if (!(event.getWorld().getBlockState(event.getPos()).getBlock() instanceof WallSignBlock)) return;
        SignTileEntity sign = (SignTileEntity) event.getWorld().getBlockEntity(event.getPos());
        if (sign == null) return;
        for (int i = 0; i < 4; ++i) {
            IFormattableTextComponent comp = ITextComponent.Serializer.fromJson(sign.getTileData().getString("Text" + (i+1)));
            if(comp == null) continue;
            sign.setMessage(
                i,
                new StringTextComponent(
                    translateAlternates(
                        comp.getContents().replaceAll("^\\[(.*)]$", "$1")
                    )
                )
            );
        }
    }

    @SubscribeEvent
    public void itemTooltip(ItemTooltipEvent event) {
        if (event.isCanceled()) return;
        updateItemName(event.getItemStack());
        event.getPlayer().inventory.items.forEach(EventListener::updateItemName);
    }
}
