package com.neutralplasma.holographicPlaceholders.addons.statistics;

import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.placeholder.PlaceholderReplacer;
import com.neutralplasma.holographicPlaceholders.HolographicPlaceholders;
import com.neutralplasma.holographicPlaceholders.storage.DataStorage;
import com.neutralplasma.holographicPlaceholders.storage.SignLocation;
import com.neutralplasma.holographicPlaceholders.utils.TextFormater;
import eu.virtusdevelops.virtuscore.compatibility.ServerVersion;
import eu.virtusdevelops.virtuscore.managers.FileManager;
import eu.virtusdevelops.virtuscore.utils.HexUtil;
import eu.virtusdevelops.virtuscore.utils.TextUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.stream.Collectors;

public class Module extends BukkitRunnable {

    private String placeholder;

    private int size;
    private int placeholderdelay;

    private String type;
    private int format;
    private boolean doSigns;
    private boolean doHeads;

    private DataStorage dataStorage;
    private FileManager fileManager;

    private HashMap<UUID, Long> data = new HashMap<>();
    private HashMap<SignLocation, Integer> signs = new HashMap<>();
    private HashMap<SignLocation, Integer> heads = new HashMap<>();
    private LinkedHashMap<UUID, Long> sorted = new LinkedHashMap<>();

    private ArrayList<Long> values = new ArrayList<>();
    private ArrayList<UUID> users = new ArrayList<>();




    private List<String> valueplaceholders = new ArrayList<>();
    private List<String> userplaceholders = new ArrayList<>();


    public Module(String placeholder, long interval, int size, String type, int format, int placeholderdelay,
                  boolean doSigns, boolean doHeads,
                  DataStorage dataStorage, HolographicPlaceholders holographicPlaceholders,
                  FileManager fileManager){
        this.placeholder = placeholder;
        this.size = size;
        this.type = type;
        this.format = format;
        this.doSigns = doSigns;
        this.doHeads = doHeads;
        this.dataStorage = dataStorage;
        this.placeholderdelay = placeholderdelay;
        this.fileManager = fileManager;

        preLoadData();
        registerPlaceholders(holographicPlaceholders);

        this.runTaskTimer(holographicPlaceholders, 0L, interval);
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public boolean addSign(SignLocation signLocation, int position){
        if(signs.containsKey(signLocation)){
            return false;
        }
        signs.put(signLocation, position);
        return true;
    }

    public void addSigns(HashMap<SignLocation, Integer> signs){
        this.signs = signs;
    }

    public boolean removeSign(SignLocation signLocation){
        for(SignLocation location : signs.keySet()){
            if(location.toString().equalsIgnoreCase(signLocation.toString())){
                signs.remove(location);
                return true;
            }
        }
        return false;
    }

    public HashMap<SignLocation, Integer> getSigns(){
        return this.signs;
    }

    public boolean addHead(SignLocation signLocation, int position){
        if(heads.containsKey(signLocation)){
            return false;
        }
        heads.put(signLocation, position);
        return true;
    }

    public  void  addHeads(HashMap<SignLocation, Integer> signs){
        this.heads = signs;
    }

    public boolean removeHead(SignLocation signLocation){
        for(SignLocation location : heads.keySet()){
            if(location.toString().equalsIgnoreCase(signLocation.toString())){
                heads.remove(location);
                return true;
            }
        }
        return false;
    }

    public HashMap<SignLocation, Integer> getHeads(){
        return this.heads;
    }
    public void clearHeads(){
        this.heads.clear();
    }
    public void clearSigns(){
        this.signs.clear();
    }






    public void preLoadData(){
        // LOAD SAVED DATA
        ConfigurationSection section = dataStorage.getData().getConfigurationSection("data." + placeholder);
        if(section != null) {
            for (String uuid : section.getKeys(false)) {
                UUID uuid1 = UUID.fromString(uuid);
                long value = dataStorage.getData().getLong("data." + placeholder + "." + uuid + ".value");
                data.put(uuid1, value);
            }
        }

        // LOAD ONLINE PLAYERS DATA
        for(Player player : Bukkit.getOnlinePlayers()){
            try{
                long value = Long.parseLong(PlaceholderAPI.setPlaceholders(player, placeholder));
                data.put(player.getUniqueId(), value);
                dataStorage.getData().set("data." + placeholder + "." + player.getUniqueId().toString() + ".value", value);
            }catch (Exception ignored){};
        }



    }

    @Override
    public void run(){
        for(Player player : Bukkit.getOnlinePlayers()){
            try{
                long value = Long.parseLong(PlaceholderAPI.setPlaceholders(player, placeholder));
                data.put(player.getUniqueId(), value);
                dataStorage.getData().set("data." + placeholder + "." + player.getUniqueId().toString() + ".value", value);
            }catch (Exception ignored){};
        }

        sorted = data
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        values = new ArrayList<>(sorted.values());
        users = new ArrayList<>(sorted.keySet());

        if(doSigns){
            for(SignLocation signLocation : signs.keySet()) {
                World world = Bukkit.getWorld(signLocation.getWorld());
                Block block = world.getBlockAt(signLocation.getX(), signLocation.getY(), signLocation.getZ());

                int position = signs.get(signLocation) - 1;
                int pos2 = position + 1;
                if (block.getChunk().isLoaded()) {
                    if (block.getState() instanceof Sign) {
                        Sign sign = (Sign) block.getState();
                        List<String> lines;

                        if (fileManager.getConfiguration("signs").contains(placeholder + "." + pos2)) {
                            lines = fileManager.getConfiguration("signs").getStringList(placeholder + "." + pos2 + ".lines");
                        } else {
                            lines = fileManager.getConfiguration("signs").getStringList(placeholder + ".default.lines");
                        }

                        String playername;
                        UUID id = getPlayer(position);
                        if (id != null) {
                            playername = Bukkit.getOfflinePlayer(id).getName();
                        } else {
                            playername = "EMPTY";
                        }
                        String value = null;
                        if (type.equalsIgnoreCase("number")) {
                            value = TextFormater.formatValue(format, getValue(position));
                        } else if (type.equalsIgnoreCase("time")) {
                            value = TextFormater.formatTime(getValue(position) * 1000);
                        }
                        if(value == null){
                            value = String.valueOf(getValue(position));
                        }

                        lines = TextUtil.formatList(lines, "{position}:" + pos2, "{value}:" + value, "{player}:" + playername);
                        lines = TextUtil.colorFormatList(lines);
                        if(ServerVersion.isServerVersionAtLeast(ServerVersion.V1_16)){
                            lines = HexUtil.hexFormatList(lines);
                        }


                        for (int i = 0; i < lines.size(); i++) {
                            sign.setLine(i, lines.get(i));
                        }

                        sign.update();
                    }
                }
            }
        }

        if(doHeads){
            for(SignLocation signLocation : heads.keySet()) {
                World world = Bukkit.getWorld(signLocation.getWorld());
                Block block = world.getBlockAt(signLocation.getX(), signLocation.getY(), signLocation.getZ());

                int position = heads.get(signLocation) - 1;

                if (block.getChunk().isLoaded()) {
                    if (block.getState() instanceof Skull) {
                        Skull skull = (Skull) block.getState();
                        UUID id = getPlayer(position);
                        if (id != null) {
                            skull.setOwningPlayer(Bukkit.getOfflinePlayer(id));
                        }
                        skull.update();
                    }
                }
            }
        }
    }

    public long getValue(int position){
        if(values.size() > position) {
            return values.get(position);
        }
        return 0;
    }

    public UUID getPlayer(int position){
        if(values.size() > position) {
            return users.get(position);
        }
        return null;
    }

    public void unRegisterPlaceholders(HolographicPlaceholders holographicPlaceholders){
        for(String placeholder : valueplaceholders){
            HologramsAPI.unregisterPlaceholder(holographicPlaceholders, placeholder);
        }
        for(String placeholder : userplaceholders){
            HologramsAPI.unregisterPlaceholder(holographicPlaceholders, placeholder);
        }
    }

    public void registerPlaceholders(HolographicPlaceholders holographicPlaceholders){
        for(int index = 0; index < size; ++index){
            int id = index + 1;
            userplaceholders.add("{hpe-" + placeholder.replace("%", "") + "-" + id + "-user}");
            valueplaceholders.add("{hpe-" + placeholder.replace("%", "") + "-" + id + "-value}");
        }


        for (int index = 0; index < valueplaceholders.size(); ++index) {
            String replacedplaceholder = valueplaceholders.get(index);
            int i = index;
            HologramsAPI.registerPlaceholder(holographicPlaceholders, replacedplaceholder, placeholderdelay, new
                    PlaceholderReplacer() {
                        @Override
                        public String update() {
                            if(type != null) {
                                if (type.equalsIgnoreCase("number")) {
                                    return TextFormater.formatValue(format, getValue(i));
                                } else if (type.equalsIgnoreCase("time")) {
                                    return TextFormater.formatTime(getValue(i) * 1000);
                                }
                            }
                            return String.valueOf(getValue(i));
                        }
                    });
        }

        for (int index = 0; index < userplaceholders.size(); ++index) {
            String replacedplaceholder = userplaceholders.get(index);
            int i = index;
            HologramsAPI.registerPlaceholder(holographicPlaceholders, replacedplaceholder, placeholderdelay, new
                    PlaceholderReplacer() {
                        @Override
                        public String update() {
                            UUID data = getPlayer(i);
                            if(data == null){
                                return "";
                            }else {
                                return Bukkit.getOfflinePlayer(data).getName();
                            }
                        }
                    });
        }

    }
}
