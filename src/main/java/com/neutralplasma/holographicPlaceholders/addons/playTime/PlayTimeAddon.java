package com.neutralplasma.holographicPlaceholders.addons.playTime;

import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.placeholder.PlaceholderReplacer;
import com.neutralplasma.holographicPlaceholders.HolographicPlaceholders;
import com.neutralplasma.holographicPlaceholders.addons.Addon;
import com.neutralplasma.holographicPlaceholders.storage.DataStorage;
import com.neutralplasma.holographicPlaceholders.utils.TextFormater;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.stream.Collectors;

public class PlayTimeAddon extends Addon implements Listener {
    private HolographicPlaceholders holographicPlaceholders;
    private DataStorage dataStorage;
    private Listener listener;
    private HashMap<UUID, Long> cached = new HashMap<>();
    private HashMap<UUID, Long> players = new HashMap<>();
    private Map<Integer, UUID> orderedPlayers = new HashMap<>();

    private List<String> timeplaceholders = new ArrayList<>();
    private List<String> userplaceholders = new ArrayList<>();

    private BukkitTask saveInterval;

    public PlayTimeAddon(HolographicPlaceholders holographicPlaceholders, DataStorage dataStorage){
        this.holographicPlaceholders = holographicPlaceholders;
        this.dataStorage = dataStorage;
    }

    @Override
    public void onEnable() {
        start();
        saveUpdate(holographicPlaceholders.getConfig().getInt("PlayTime.update-delay"));
        Bukkit.getPluginManager().registerEvents(this.listener, holographicPlaceholders);
        registerTimePlaceholders();
        registerUserPlaceholders();
        registerOfflineUsers();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        saveData();
        saveInterval.cancel();
        HandlerList.unregisterAll(this.listener);
        unregisterPlaceholders();

        cached.clear();
        players.clear();
        orderedPlayers.clear();
        timeplaceholders.clear();
        userplaceholders.clear();

        super.onDisable();
    }

    public void start(){
        this.listener = new Listener() {

            @EventHandler(priority = EventPriority.NORMAL)
            public void onPlayerJoin(PlayerJoinEvent event){
                cached.put(event.getPlayer().getUniqueId(), System.currentTimeMillis());
                long playtime = 0;
                try {
                    playtime = dataStorage.getData().getLong("playtime." + event.getPlayer().getUniqueId().toString());
                }catch (Exception ignored){}

                players.put(event.getPlayer().getUniqueId(), playtime);
            }

            @EventHandler(priority = EventPriority.NORMAL)
            public void onPlayerLeave(PlayerQuitEvent event){
                UUID uuid = event.getPlayer().getUniqueId();
                long playtime = dataStorage.getData().getLong("playtime." + uuid.toString());
                try {
                    playtime += System.currentTimeMillis() - cached.get(uuid);
                }catch (Exception ignored){}
                dataStorage.getData().set("playtime." + uuid.toString(), playtime);
                cached.remove(uuid);
            }
        };
    }

    public void saveData(){
        for(UUID uuid : cached.keySet()){
            long playtime = 0;
            if(Bukkit.getPlayer(uuid) != null) {
                long time = System.currentTimeMillis();
                try {
                    playtime = dataStorage.getData().getLong("playtime." + uuid.toString());
                } catch (Exception ignored) { }
                playtime += time - cached.get(uuid);
                dataStorage.getData().set("playtime." + uuid.toString(), playtime);
                cached.put(uuid, time);
            }else{
                cached.remove(uuid);
            }
            players.put(uuid, playtime);
        }
        dataStorage.saveData();
    }

    public void saveUpdate(int interval){
        saveInterval = new BukkitRunnable() {
            @Override
            public void run() {
                saveData();
            }
        }.runTaskTimer(holographicPlaceholders, 0L, interval);
    }

    public long getPlayTime(Player player){
        UUID uuid = player.getUniqueId();
        long time;
        time = dataStorage.getData().getLong("playtime." + uuid.toString());
        return time;
    }
    public long getPlayTime(UUID uuid){
        long time;
        time = dataStorage.getData().getLong("playtime." + uuid.toString());
        return time;

    }

    public long getPlayTime(String username){
        UUID uuid = Bukkit.getOfflinePlayer(username).getUniqueId();
        long time;
        time = dataStorage.getData().getLong("playtime." + uuid.toString());
        return time;
    }

    public void registerOfflineUsers(){
        for(OfflinePlayer oPlayer : Bukkit.getOfflinePlayers()){
            long time;
            time = dataStorage.getData().getLong("playtime." + oPlayer.getUniqueId().toString());
            if(time < 0){
                time = 0;
            }
            players.put(oPlayer.getUniqueId(), time);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    public LinkedHashMap getPlaytimeUsers(){
        return players
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    private Map setupPlayTop(){
        Map<UUID, Double> users = getPlaytimeUsers();
        int index = 0;
        orderedPlayers.clear();
        for(UUID uuid : users.keySet()){
            if (!(orderedPlayers.containsValue(uuid))) {
                orderedPlayers.put(index ,uuid);
                index++;
            }
        }
        return orderedPlayers;
    }


    public String playtopGet(int index){
        String playtopUser;
        try{
            UUID player1 = UUID.fromString(setupPlayTop().get(index).toString());
            playtopUser = Bukkit.getOfflinePlayer(player1).getName();
        }catch(Exception error){
            playtopUser = "";
        }
        return playtopUser;
    }

    public UUID playtopGetUUID(int index){
        UUID playtopUser;
        try{
            playtopUser = UUID.fromString(setupPlayTop().get(index).toString());
        }catch(Exception error){
            playtopUser = null;
        }
        return playtopUser;
    }

    public int getPosition(Player player){
        for(int i : orderedPlayers.keySet()){
            if(orderedPlayers.get(i).toString().equals(player.getUniqueId().toString())){
                return i + 1;
            }
        }
        if(!cached.containsKey(player.getUniqueId())){
            cached.put(player.getUniqueId(), System.currentTimeMillis());
        }
        return -1;
    }
    // -----------------------------------------------------------------------------------------------------------------

    public void registerUserPlaceholders() {
        int size = holographicPlaceholders.getConfig().getInt("PlayTime.size");
        for(int index = 0; index < size; ++index){
            int id = index + 1;
            userplaceholders.add("{playtime" + id + "-user}");
        }

        long delay = holographicPlaceholders.getConfig().getLong("PlayTime.placeholder-delay");
        for(int index = 0; index < userplaceholders.size(); ++index){
            String replacedplaceholder = userplaceholders.get(index);
            int i = index;
            HologramsAPI.registerPlaceholder(holographicPlaceholders, replacedplaceholder, delay, new
                    PlaceholderReplacer() {
                        @Override
                        public String update() {
                            return playtopGet(i);
                        }
                    });
        }
    }

    public void registerTimePlaceholders() {
        int size = holographicPlaceholders.getConfig().getInt("PlayTime.size");
        for(int index = 0; index < size; ++index){
            int id = index + 1;
            timeplaceholders.add("{playtime" + id + "-usertime}");
        }
        long delay = holographicPlaceholders.getConfig().getLong("PlayTime.placeholder-delay");
        for (int index = 0; index < timeplaceholders.size(); ++index) {
            String replacedplaceholder = timeplaceholders.get(index);
            int i = index;
            HologramsAPI.registerPlaceholder(holographicPlaceholders, replacedplaceholder, delay, new
                    PlaceholderReplacer() {
                        @Override
                        public String update() {
                            try{
                                return TextFormater.formatTime(players.get(playtopGetUUID(i)));
                            }catch (Exception ignored){
                                return TextFormater.formatTime(0L);
                            }
                        }
                    });
        }
    }

    public void unregisterPlaceholders(){
        for(String placeholder : timeplaceholders){
            HologramsAPI.unregisterPlaceholder(holographicPlaceholders, placeholder);
        }
        for(String placeholder : userplaceholders){
            HologramsAPI.unregisterPlaceholder(holographicPlaceholders, placeholder);
        }
    }
}
