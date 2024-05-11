package bbib.plugintesting;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class PluginTesting extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getLogger().info("플러그인 활성화");

        Bukkit.getCommandMap().register("randtp", new Command("randtp"));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getLogger().info("플러그인 비활성화");

    }
}
