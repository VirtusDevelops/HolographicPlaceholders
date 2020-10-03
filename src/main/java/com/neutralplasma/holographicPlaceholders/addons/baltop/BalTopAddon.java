package com.neutralplasma.holographicPlaceholders.addons.baltop;

import com.neutralplasma.holographicPlaceholders.HolographicPlaceholders;
import com.neutralplasma.holographicPlaceholders.addons.Addon;
import com.neutralplasma.holographicPlaceholders.placeholder.PlaceholderRegistry;
import com.neutralplasma.holographicPlaceholders.placeholder.PlaceholderReplacer;
import com.neutralplasma.holographicPlaceholders.storage.DataStorage;
import com.neutralplasma.holographicPlaceholders.utils.BalanceFormater;
import com.neutralplasma.holographicPlaceholders.utils.PluginHook;
import eu.virtusdevelops.virtuscore.managers.FileManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.stream.Collectors;

public class BalTopAddon extends Addon {

    private HolographicPlaceholders holographicPlaceholders;
    private PlaceholderRegistry placeholderRegistry;
    private FileManager fileManager;
    private DataStorage dataStorage;

    private HashMap<String, Double> playersBalances = new HashMap<>();
    private HashMap<String, Double> sorted = new HashMap<>();


    private ArrayList<Double> values = new ArrayList<>();
    private ArrayList<String> users = new ArrayList<>();


    private List<String> excludedPlayers;
    private List<String> balplaceholders = new ArrayList<>();
    private List<String> userplaceholders = new ArrayList<>();


    // tasks
    private BukkitTask balanceRegister;
    private BukkitTask balanceUpdater;
    private BukkitTask offlineUpdater;



    public BalTopAddon(HolographicPlaceholders holographicPlaceholders,FileManager fileManager,DataStorage dataStorage,
                       String name, PlaceholderRegistry placeholderRegistry){
        this.holographicPlaceholders = holographicPlaceholders;
        this.fileManager = fileManager;
        this.dataStorage = dataStorage;
        this.name = name;
        this.placeholderRegistry = placeholderRegistry;
        this.setHook(PluginHook.BOTH);
        excludedPlayers = holographicPlaceholders.getConfig().getStringList("BalTop.excluded-users");
    }

    @Override
    public void onEnable() {



        startBaltop();
        registerPlaceholders();

        super.onEnable();

    }
    
    @Override
    public void onDisable() {

        unRegisterPlaceholders();
        stopBaltop();


        super.onDisable();
    }

    public void registerAllBalances() {
        playersBalances.clear();
        balanceRegister = new BukkitRunnable() {
            public void run() {
                for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
                    if(!(excludedPlayers.contains(offlinePlayer.getName())) && offlinePlayer.getName() != null){
                        double playerbalance = holographicPlaceholders.getEconomy().getBalance(offlinePlayer);
                        playersBalances.put(offlinePlayer.getName(), playerbalance);
                    }
                }

                sorted = playersBalances
                        .entrySet()
                        .stream()
                        .filter(name -> !excludedPlayers.contains(name.getKey()))
                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                        .collect(Collectors.toMap(
                                Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

                values = new ArrayList<>(sorted.values());
                users = new ArrayList<>(sorted.keySet());


            }
        }.runTaskAsynchronously(holographicPlaceholders);
    }
    // =================================================================================================================

    public void onlineBalanceUpdater() { // Update online players balance.
        long delay = holographicPlaceholders.getConfig().getLong("BalTop.delay");
        balanceUpdater = new BukkitRunnable() {
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if(!player.hasMetadata("NPC") && !(excludedPlayers.contains(player.getName()))) {
                        double playerbalance;
                        try {
                            playerbalance = holographicPlaceholders.getEconomy().getBalance(player);
                        }catch (Exception error){
                            playerbalance = 0.0;
                        }
                        //UUID playersuuid = player.getUniqueId();
                        playersBalances.put(player.getName(), playerbalance);
                    }
                }

                sorted = playersBalances
                        .entrySet()
                        .stream()
                        .filter(name -> !excludedPlayers.contains(name.getKey()))
                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                        .collect(Collectors.toMap(
                                Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

                values = new ArrayList<>(sorted.values());
                users = new ArrayList<>(sorted.keySet());

            }

        }.runTaskTimerAsynchronously(holographicPlaceholders, 0L, delay);

    }

    public void offlineBalanceUpdater(){
        long delay = holographicPlaceholders.getConfig().getLong("BalTop.offline-delay");
        offlineUpdater = new BukkitRunnable() {
            @Override
            public void run() {
                for(OfflinePlayer player : Bukkit.getOfflinePlayers()){
                    if(!(excludedPlayers.contains(player.getName())) && player.getName() != null){
                            double playerbalance;
                            try {
                                playerbalance = holographicPlaceholders.getEconomy().getBalance(player);
                            }catch (Exception error){
                                playerbalance = 0D;
                            }
                            //UUID playersuuid = player.getUniqueId();
                            playersBalances.put(player.getName(), playerbalance);
                    }
                }

                sorted = playersBalances
                        .entrySet()
                        .stream()
                        .filter(name -> !excludedPlayers.contains(name.getKey()))
                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                        .collect(Collectors.toMap(
                                Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

                values = new ArrayList<>(sorted.values());
                users = new ArrayList<>(sorted.keySet());

            }
        }.runTaskTimerAsynchronously(holographicPlaceholders, 0L, delay);
    }
    // -----------------------------------------------------------------------------------------------------------------

    public double getValue(int position){
        if(values.size() > position) {
            return values.get(position);
        }
        return 0;
    }

    public String getPlayer(int position){
        if(values.size() > position) {
            return users.get(position);
        }
        return "";
    }



    //------------------------------------------------------------------------------------------------------------------

    public void registerPlaceholders() {
        long delay = holographicPlaceholders.getConfig().getLong("BalTop.placeholder-delay");
        int size = holographicPlaceholders.getConfig().getInt("BalTop.size");
        int format = holographicPlaceholders.getConfig().getInt("BalTop.format");

        for (int index = 0; index < size; ++index) {
            int id = index + 1;
            userplaceholders.add("{hpe-baltop-" + id + "-user}");
            balplaceholders.add("{hpe-baltop-" + id + "-value}");
        }


        for (int index = 0; index < balplaceholders.size(); ++index) {
            String replacedplaceholder = balplaceholders.get(index);
            int i = index;

            placeholderRegistry.getRegister().registerPlaceholder(holographicPlaceholders, replacedplaceholder, delay, new PlaceholderReplacer() {
                @Override
                public String update() {
                    return BalanceFormater.formatValue(format, getValue(i));
                }
            });

        }

        for (int index = 0; index < userplaceholders.size(); ++index) {
            String replacedplaceholder = userplaceholders.get(index);
            int i = index;

            placeholderRegistry.getRegister().registerPlaceholder(holographicPlaceholders, replacedplaceholder, delay, new PlaceholderReplacer() {
                @Override
                public String update() {
                    return getPlayer(i);
                }
            });

        }
    }

    public void unRegisterPlaceholders(){


        for (String placeholder : balplaceholders) {
            placeholderRegistry.getRegister().unregisterPlaceholder(holographicPlaceholders, placeholder);
        }
        for (String placeholder : userplaceholders) {
            placeholderRegistry.getRegister().unregisterPlaceholder(holographicPlaceholders, placeholder);
        }
    }






    //------------------------------------------------------------------------------------------------------------------
    public void stopBaltop(){
        try{
            balanceUpdater.cancel();
            balanceRegister.cancel();
            offlineUpdater.cancel();

            sorted.clear();
            playersBalances.clear();
        }catch (Exception ignored) {}
    }

    public void startBaltop(){
        try{
            registerAllBalances();
            onlineBalanceUpdater();
            offlineBalanceUpdater();
        }catch (Exception ignored){ }
    }

    public int getPlayerPosition(Player player){
        for(int i = 0 ; i < this.users.size(); i++){
            if(this.users.get(i).equalsIgnoreCase(player.getName())){
                return i+1;
            }
        }
        return 0;
    }

}
