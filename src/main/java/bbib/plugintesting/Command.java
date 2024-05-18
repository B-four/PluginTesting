package bbib.plugintesting;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Command implements CommandExecutor {
    private Random random;
    private final GameManager gameManager;
    private final WorldManager worldManager;

    public Command(GameManager gameManager, WorldManager worldManager) {
        this.gameManager = gameManager;
        this.worldManager = worldManager;
    }


    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (command.getName().equalsIgnoreCase("startgame")) {
                gameManager.startGame(player, 7, 30, 10, 10);
                return true;
            } else if (command.getName().equalsIgnoreCase("endgame")) {
                gameManager.endGame();
                return true;
            } else if (command.getName().equalsIgnoreCase("newworld")){
                worldManager.createPeacefulFlatWorld("emptyWord", player);
                return true;
            }
        }
        return false;
    }

    public Location getRandomLocation(Player player)
    {
        // 랜덤한 위치의 좌표를 return 하는 코드
        World world = player.getWorld();

        double randomX=0;
        double randomZ=0;

        List<Location> randomLocations = new ArrayList<>();
        while (randomLocations.isEmpty()) {
            randomX = random.nextInt(20000) - 10000 + 0.5;
            randomZ = random.nextInt(20000) - 10000 + 0.5;
            int highestY = world.getHighestBlockYAt((int) randomX, (int) randomZ);
            Bukkit.getLogger().info("랜덤 좌표:" + randomX + ", " + randomZ + ", " + highestY + "에서 안전한 위치 탐색 중...");
            for (int i = highestY; i > world.getMinHeight(); i--) {
                Location tempLocation = new Location(world, randomX, i, randomZ);
                if (isSafeLocation(tempLocation)) {
                    Bukkit.getLogger().info("안전한 위치: " + tempLocation);
                    randomLocations.add(tempLocation);
                }
            }
        }
        return randomLocations.get(random.nextInt(randomLocations.size()));
    }

    public boolean isSafeLocation(Location location)
    {
        // 안전한 위치인지 확인하는 코드
        return location.getBlock().getRelative(0,2,0).getType().isAir()
                && location.getBlock().getRelative(0, 1, 0).getType().isAir()
                && !location.getBlock().isPassable();
    }
}

