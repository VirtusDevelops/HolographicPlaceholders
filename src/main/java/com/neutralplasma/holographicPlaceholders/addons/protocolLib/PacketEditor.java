package com.neutralplasma.holographicPlaceholders.addons.protocolLib;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.*;
import org.bukkit.entity.*;
import java.util.*;


public class PacketEditor extends PacketAdapter{
    private boolean useOptional;

    public PacketEditor(final AdapterParameteters params){
        super(params);
        final String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        if (version.contains("v1_13") || version.contains("v1_13") || version.contains("v1_14") || version.contains("v1_15")) {
            this.useOptional = true;
        }
    }

    @Override
    @SuppressWarnings("all")
    public void onPacketSending(PacketEvent event) {
        try {
            PacketContainer packet = event.getPacket();

            if (packet.getType() == PacketType.Play.Server.ENTITY_METADATA) {
                final WrapperPlayServerEntityMetadata entityMetadataPacket = new WrapperPlayServerEntityMetadata(packet.deepClone());
                final List<WrappedWatchableObject> dataWatcherValues = entityMetadataPacket.getMetadata();
                for (final WrappedWatchableObject watchableObject : dataWatcherValues) {
                    if (watchableObject.getIndex() == 2) {
                        if (getPlaceholders(watchableObject, event.getPlayer())) {
                            event.setPacket(entityMetadataPacket.getHandle());
                        }
                    }
                }
            }
        }catch (Exception error){

        }
    }

    public boolean getPlaceholders(WrappedWatchableObject customNameWatchableObject, Player player){
        if (customNameWatchableObject == null) {
            return true;
        }
        final Object customNameWatchableObjectValue = customNameWatchableObject.getValue();
        String newName;
        if (this.useOptional) {
            if (!(customNameWatchableObjectValue instanceof Optional)) {
            return false;
            }
            final Optional<?> customNameOptional = (Optional<?>)customNameWatchableObjectValue;

            if (!customNameOptional.isPresent()) {
                return false;
            }
        final WrappedChatComponent componentWrapper = WrappedChatComponent.fromHandle(customNameOptional.get());
        newName = componentWrapper.getJson();
        } else {
            newName = (String)customNameWatchableObjectValue;
        }
        newName = PlaceholderAPI.setPlaceholders(player, newName);

        if (this.useOptional) {
            customNameWatchableObject.setValue(Optional.of(WrappedChatComponent.fromJson(newName).getHandle()));
        } else {
            customNameWatchableObject.setValue(newName);
        }
        return true;
    }
}
