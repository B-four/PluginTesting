package bbib.plugintesting;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class WorldManager{
    public void createPeacefulFlatWorld(String worldName, Player player) {
        if(Bukkit.getWorld(worldName) != null) {
            player.teleport(Bukkit.getWorld(worldName).getSpawnLocation());
            return;
        }

        WorldCreator worldCreator = new WorldCreator(worldName);
        worldCreator.type(WorldType.FLAT);

        World world = Bukkit.createWorld(worldCreator);

        if (world != null) {
            // Set game rules for peaceful mode
            world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
            world.setDifficulty(Difficulty.PEACEFUL);

            // Teleport the player to the new world
            player.teleport(world.getSpawnLocation());
            player.sendMessage("새로운 평평한 월드로 이동했습니다: " + worldName);
        } else {
            player.sendMessage("월드 생성에 실패했습니다.");
            Bukkit.getLogger().severe("World creation failed: " + worldName);
        }
    }
}