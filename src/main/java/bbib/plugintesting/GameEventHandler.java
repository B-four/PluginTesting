package bbib.plugintesting;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.io.BukkitObjectInputStream;

import java.nio.Buffer;

public class GameEventHandler implements Listener {
    private final GameManager gameManager;

    private Location tempLocation;

    public GameEventHandler(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void hitTargetEvent(ProjectileHitEvent event) {

        if (event.getEntity() instanceof Arrow arrow) {
            int arrowType = gameManager.getArrowType();
            if (event.getHitBlock() != null) {
                int distance = gameManager.calculateDistance(event.getHitBlock().getLocation(), tempLocation);
                if(distance>=20)
                {
                    if (arrowType != 0) {
                        gameManager.removeTargetByType(event.getHitBlock(), arrowType);
                    }
                }
                else {
                    Bukkit.broadcastMessage("너무 가까워요");
                }

            }
        }
        event.getEntity().remove();
        if (gameManager.isGameEnd()) {
            gameManager.endGame();
        }
    }

    @EventHandler
    public void onArrowShoot(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (ItemManager.readData(player.getInventory().getItemInOffHand()) == 0) {
                event.setCancelled(true);
                player.sendMessage("화살을 왼손에 장착해 주세요");
                updateInventory(player);
                return;
            }
            if (event.getProjectile() instanceof Arrow) {
                gameManager.setArrowType();
                tempLocation = player.getLocation();
            }
        }
    }

    private void updateInventory(Player player) {
        PlayerInventory inventory = player.getInventory();
        ItemStack[] items = inventory.getContents();
        inventory.setContents(items); // 인벤토리를 강제로 업데이트
    }
}
