package com.neutralplasma.holographicPlaceholders.addons.protocolLib;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;

import eu.virtusdevelops.virtuscore.compatibility.ServerVersion;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.*;
import org.json.simple.parser.JSONParser;

import java.util.*;


public class PacketEditor extends PacketAdapter{
    private boolean useOptional;
    private JSONParser parser = new JSONParser();

    public PacketEditor(final AdapterParameteters params){
        super(params);

        if (ServerVersion.isServerVersionAtLeast(ServerVersion.V1_13)) {
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
                        if (updateText(watchableObject, event.getPlayer())) {
                            event.setPacket(entityMetadataPacket.getHandle());
                        }
                    }
                }
            }
        }catch (Exception error){

        }
    }

    public boolean updateText(WrappedWatchableObject customNameWatchableObject, Player player){
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



        //newName = HexUtil.colorify(newName);
        //VirtusCore.console().sendMessage("Value: << " + newName + " >>");
        //player.sendMessage(newName);
        //String test = HexUtil.colorify("<r>This is a test message lets see if it works");

        if (this.useOptional) {

            customNameWatchableObject.setValue(Optional.of(WrappedChatComponent.fromJson(newName).getHandle() /*WrappedChatComponent.fromChatMessage(test)[0].getHandle()*/));
        } else {
            customNameWatchableObject.setValue(newName);
        }
        return true;
    }
}
