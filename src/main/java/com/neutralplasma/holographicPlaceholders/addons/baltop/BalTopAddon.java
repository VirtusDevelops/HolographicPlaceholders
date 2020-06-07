package com.neutralplasma.holographicPlaceholders.addons.baltop;

import com.comphenix.protocol.PacketType;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.placeholder.PlaceholderReplacer;
import com.neutralplasma.holographicPlaceholders.HolographicPlaceholders;
import com.neutralplasma.holographicPlaceholders.addons.Addon;
import com.neutralplasma.holographicPlaceholders.utils.BalanceFormater;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.stream.Collectors;

public class BalTopAddon extends Addon {

    private HolographicPlaceholders holographicPlaceholders;
    private BalanceFormater balanceFormater;
    private Map<String, Double> playersBalances = new HashMap<>();
    private Map<Integer, String> orderedPlayers = new HashMap<>();
    private List<String> excludedPlayers;
    private List<String> balplaceholders = new ArrayList<>();
    private List<String> userplaceholders = new ArrayList<>();

    private static Map<String, Double> playersBalancesStatic = new HashMap<>();

    // tasks
    private BukkitTask balanceRegister;
    private BukkitTask balanceUpdater;
    private BukkitTask offlineUpdater;


    public BalTopAddon(HolographicPlaceholders holographicPlaceholders, BalanceFormater balanceFormater){
        this.holographicPlaceholders = holographicPlaceholders;
        this.balanceFormater = balanceFormater;
        excludedPlayers = (List) holographicPlaceholders.getConfig().getList("BalTop.excluded-users");
    }

    @Override
    public void onEnable() {
        startBaltop();

        registerbalancesPlaceholders();
        registeruserPlaceholders();
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        unregisterPlaceholders();
        stopBaltop();
        super.onDisable();
    }

    public void registerAllBalances() {
        playersBalances.clear();
        balanceRegister = new BukkitRunnable() {
            public void run() {
                for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
                    if(!(excludedPlayers.contains(offlinePlayer.getName()))){
                        double playerbalance = holographicPlaceholders.getEconomy().getBalance(offlinePlayer);
                        //UUID playersuuid = offlinePlayer.getUniqueId();
                        playersBalances.put(offlinePlayer.getName(), playerbalance);
                    }
                }

            }
        }.runTaskAsynchronously(holographicPlaceholders);
        playersBalancesStatic = playersBalances;
    }
    // =================================================================================================================

    public static Map<String, Double> getPlayers(){
        return playersBalancesStatic;
    }

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
            }

        }.runTaskTimerAsynchronously(holographicPlaceholders, 0L, delay);

    }

    public void offlineBalanceUpdater(){
        long delay = holographicPlaceholders.getConfig().getLong("BalTop.offline-delay");
        offlineUpdater = new BukkitRunnable() {
            @Override
            public void run() {
                for(OfflinePlayer player : Bukkit.getOfflinePlayers()){
                    if(!(excludedPlayers.contains(player.getName()))){
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

            }
        }.runTaskTimerAsynchronously(holographicPlaceholders, 0L, delay);
    }

    //------------------------------------------------------------------------------------------------------------------
    public LinkedHashMap getBalancesUsers(){
        return playersBalances
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    //------------------------------------------------------------------------------------------------------------------
    private Map<Integer, String> setupBaltop(){
        Map<String, Double> users = getBalancesUsers();
        int index = 0;
        orderedPlayers.clear();
        for(String username : users.keySet()){
            if (!(orderedPlayers.containsValue(username))) {
                orderedPlayers.put(index ,username);
                index++;
            }
        }
        return orderedPlayers;
    }


    //------------------------------------------------------------------------------------------------------------------
    public String baltopget(int index){
        String baltopuser;
        try{
            baltopuser = setupBaltop().get(index);
        }catch(Exception error){
            baltopuser = "";
        }
        return baltopuser;
    }

    //------------------------------------------------------------------------------------------------------------------
    public String baltopuserbalance(int index){
        String baltopuserbalance = "";
        int format = holographicPlaceholders.getConfig().getInt("BalTop.format");
        try{
            //UUID playerraw = UUID.fromString(setupBaltop().get(index).toString());
            OfflinePlayer player1 = Bukkit.getOfflinePlayer(setupBaltop().get(index));
            if(format == 1){
                baltopuserbalance = "" +  balanceFormater.formatDecimals(holographicPlaceholders.getEconomy().getBalance(player1));
            }
            if(format == 2){
                baltopuserbalance = "" +  balanceFormater.formatNumbers(holographicPlaceholders.getEconomy().getBalance(player1));
            }
            if(format == 3){
                baltopuserbalance = "" +  balanceFormater.formatNames(holographicPlaceholders.getEconomy().getBalance(player1));
            }
        }catch(Exception error){
            baltopuserbalance = " ";
        }
        return baltopuserbalance;
    }




    //------------------------------------------------------------------------------------------------------------------------
    public boolean stopBaltop(){
        try{
            balanceUpdater.cancel();
            balanceRegister.cancel();
            offlineUpdater.cancel();
            return true;
        }catch (Exception error) {
            return false;
        }
    }

    public boolean startBaltop(){
        try{
            registerAllBalances();
            onlineBalanceUpdater();
            offlineBalanceUpdater();
            return true;
        }catch (Exception error){
            return false;
        }
    }

    public void registeruserPlaceholders() {
        int size = holographicPlaceholders.getConfig().getInt("BalTop.size");
        for(int index = 0; index < size; ++index){
            int id = index + 1;
            userplaceholders.add("{baltop" + id + "-user}");
        }

        long delay = holographicPlaceholders.getConfig().getLong("BalTop.placeholder-delay");
        for(int index = 0; index < userplaceholders.size(); ++index){
            String replacedplaceholder = userplaceholders.get(index);
            int i = index;
            HologramsAPI.registerPlaceholder(holographicPlaceholders, replacedplaceholder, delay, new
                    PlaceholderReplacer() {
                        @Override
                        public String update() {
                            return baltopget(i);
                        }
                    });
        }
    }

    public void registerbalancesPlaceholders() {
        int size = holographicPlaceholders.getConfig().getInt("BalTop.size");
        for(int index = 0; index < size; ++index){
            int id = index + 1;
            balplaceholders.add("{baltop" + id + "-userbalance}");
        }
        long delay = holographicPlaceholders.getConfig().getLong("BalTop.placeholder-delay");
        for (int index = 0; index < balplaceholders.size(); ++index) {
            String replacedplaceholder = balplaceholders.get(index);
            int i = index;
            HologramsAPI.registerPlaceholder(holographicPlaceholders, replacedplaceholder, delay, new
                    PlaceholderReplacer() {
                        @Override
                        public String update() {
                            return baltopuserbalance(i);
                        }
                    });
        }
    }

    public int getPlayerPosition(Player player){
        for(int i : orderedPlayers.keySet()){
            if(orderedPlayers.get(i).equals(player.getName())){
                return i + 1;
            }
        }
        return 0;
    }

    public void unregisterPlaceholders(){
        for(String placeholder : balplaceholders){
            HologramsAPI.unregisterPlaceholder(holographicPlaceholders, placeholder);
        }
        for(String placeholder : userplaceholders){
            HologramsAPI.unregisterPlaceholder(holographicPlaceholders, placeholder);
        }
    }

}
