package com.neutralplasma.holographicPlaceholders.addons.statistics;

import com.neutralplasma.holographicPlaceholders.HolographicPlaceholders;
import com.neutralplasma.holographicPlaceholders.placeholder.PlaceholderRegistry;
import com.neutralplasma.holographicPlaceholders.placeholder.PlaceholderReplacer;
import com.neutralplasma.holographicPlaceholders.storage.DataStorage;
import com.neutralplasma.holographicPlaceholders.storage.SignLocation;
import com.neutralplasma.holographicPlaceholders.utils.PluginHook;
import com.neutralplasma.holographicPlaceholders.utils.TextFormater;
import eu.virtusdevelops.virtuscore.compatibility.ServerVersion;
import eu.virtusdevelops.virtuscore.managers.FileManager;
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
    private PlaceholderRegistry placeholderRegistry;

    private int size;
    private int placeholderdelay;

    private String type;
    private int format;
    private boolean doSigns;
    private boolean doHeads;

    private DataStorage dataStorage;
    private FileManager fileManager;

    private HashMap<UUID, Double> data = new HashMap<>();
    private HashMap<SignLocation, Integer> signs = new HashMap<>();
    private HashMap<SignLocation, Integer> heads = new HashMap<>();
    private LinkedHashMap<UUID, Double> sorted = new LinkedHashMap<>();

    private ArrayList<Double> values = new ArrayList<>();
    private ArrayList<UUID> users = new ArrayList<>();




    private List<String> valueplaceholders = new ArrayList<>();
    private List<String> userplaceholders = new ArrayList<>();


    public Module(String placeholder, long interval, int size, String type, int format, int placeholderdelay,
                  boolean doSigns, boolean doHeads,
                  DataStorage dataStorage, HolographicPlaceholders holographicPlaceholders,
                  FileManager fileManager, PlaceholderRegistry placeholderRegistry){
        this.placeholder = placeholder;
        this.size = size;
        this.type = type;
        this.format = format;
        this.doSigns = doSigns;
        this.doHeads = doHeads;
        this.dataStorage = dataStorage;
        this.placeholderdelay = placeholderdelay;
        this.fileManager = fileManager;
        this.placeholderRegistry = placeholderRegistry;

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
        List<String> data = new ArrayList<>();
        for(SignLocation location : signs.keySet()){
            data.add(location.toString() + ":" + signs.get(location));
        }
        dataStorage.getData().set("addon-data." + placeholder + ".signs", data);
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
        // Save data.
        List<String> data = new ArrayList<>();
        for(SignLocation location : signs.keySet()){
            data.add(location.toString() + ":" + signs.get(location));
        }
        dataStorage.getData().set("addon-data." + placeholder + ".signs", data);

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

        // Save data
        List<String> data2 = new ArrayList<>();
        for(SignLocation location : heads.keySet()){
            data2.add(location.toString() + ":" + heads.get(location));
        }
        dataStorage.getData().set("addon-data." + placeholder + ".heads", data2);

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
        // Save data
        List<String> data2 = new ArrayList<>();
        for(SignLocation location : heads.keySet()){
            data2.add(location.toString() + ":" + heads.get(location));
        }
        dataStorage.getData().set("addon-data." + placeholder + ".heads", data2);

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
                double value = dataStorage.getData().getDouble("data." + placeholder + "." + uuid + ".value");
                data.put(uuid1, value);
            }
        }

        // LOAD ONLINE PLAYERS DATA
        for(Player player : Bukkit.getOnlinePlayers()){
            try{

                double value = Double.parseDouble(PlaceholderAPI.setPlaceholders(player, placeholder));
                data.put(player.getUniqueId(), value);
                dataStorage.getData().set("data." + placeholder + "." + player.getUniqueId().toString() + ".value", value);
            }catch (Exception ignored){};
        }



    }

    @Override
    public void run(){
        for(Player player : Bukkit.getOnlinePlayers()){
            try{
                double value = Double.parseDouble(PlaceholderAPI.setPlaceholders(player, placeholder));
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
                            lines = TextUtil.colorFormatList(lines);
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

    public double getValue(int position){
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
            placeholderRegistry.getRegister().unregisterPlaceholder(holographicPlaceholders, placeholder);
        }
        for(String placeholder : userplaceholders){
            placeholderRegistry.getRegister().unregisterPlaceholder(holographicPlaceholders, placeholder);
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

            placeholderRegistry.getRegister().registerPlaceholder(holographicPlaceholders, replacedplaceholder, placeholderdelay, new PlaceholderReplacer() {
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

            placeholderRegistry.getRegister().registerPlaceholder(holographicPlaceholders, replacedplaceholder, placeholderdelay, new PlaceholderReplacer() {
                @Override
                public String update() {
                    UUID data = getPlayer(i);
                    if (data == null) {
                        return "";
                    } else {
                        return Bukkit.getOfflinePlayer(data).getName();
                    }
                }
            });

        }

    }
}
