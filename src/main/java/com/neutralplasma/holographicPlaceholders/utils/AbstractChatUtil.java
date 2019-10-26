package com.neutralplasma.holographicPlaceholders.utils;

import com.neutralplasma.holographicPlaceholders.HolographicPlaceholders;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/*
    This code is made by SONGODA and not me!
    For more info check their page:
    https://songoda.com/
 */

public class AbstractChatUtil implements Listener {

    private static HolographicPlaceholders holographicPlaceholders;

    private static final List<UUID> registered = new ArrayList<>();

    private final Player player;
    private final ChatConfirmHandler handler;

    private OnClose onClose = null;
    private Listener listener;

    public AbstractChatUtil(Player player, ChatConfirmHandler hander, HolographicPlaceholders holographicPlaceholders) {
        this.player = player;
        this.handler = hander;
        player.closeInventory();
        initializeListeners(holographicPlaceholders);
        registered.add(player.getUniqueId());
    }

    public static boolean isRegistered(Player player) {
        return registered.contains(player.getUniqueId());
    }

    public static boolean unregister(Player player) {
        return registered.remove(player.getUniqueId());
    }

    public void initializeListeners(JavaPlugin plugin) {

        this.listener = new Listener() {
            @EventHandler
            public void onChat(AsyncPlayerChatEvent event) {
                Player player = event.getPlayer();
                if (!AbstractChatUtil.isRegistered(player)) return;

                AbstractChatUtil.unregister(player);
                event.setCancelled(true);

                ChatConfirmEvent chatConfirmEvent = new ChatConfirmEvent(player, event.getMessage());

                handler.onChat(chatConfirmEvent);

                if (onClose != null) {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () ->
                            onClose.onClose(), 0L);
                }
                HandlerList.unregisterAll(listener);
            }
        };


        Bukkit.getPluginManager().registerEvents(listener, plugin);
    }

    public void setOnClose(OnClose onClose) {
        this.onClose = onClose;
    }

    public interface ChatConfirmHandler {
        void onChat(ChatConfirmEvent event);
    }

    public interface OnClose {
        void onClose();
    }

    public class ChatConfirmEvent {

        private final Player player;
        private final String message;

        public ChatConfirmEvent(Player player, String message) {
            this.player = player;
            this.message = message;
        }

        public Player getPlayer() {
            return player;
        }

        public String getMessage() {
            return message;
        }
    }

}

