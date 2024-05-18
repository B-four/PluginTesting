package bbib.plugintesting;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;


public final class PluginTesting extends JavaPlugin {
    private final GameManager gameManager = new GameManager(this, new ItemManager(), new PlayerManager());
    private final WorldManager worldManager = new WorldManager();
    private final GameEventHandler gameEventHandler = new GameEventHandler(gameManager);

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getLogger().info("플러그인 활성화");
        //Bukkit.getPluginManager().registerEvents(gameManager, this);
        Bukkit.getPluginManager().registerEvents(gameEventHandler, this);
        Command commandExecutor = new Command(gameManager, worldManager);
        getCommand("startgame").setExecutor(commandExecutor);
        getCommand("endgame").setExecutor(commandExecutor);
        getCommand("newworld").setExecutor(commandExecutor);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        gameManager.endGame();
        Bukkit.getLogger().info("플러그인 비활성화");

    }
}
