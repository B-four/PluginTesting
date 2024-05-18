package bbib.plugintesting;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerManager {
    private Player player;

    public PlayerManager() {}

    public Player getPlayer() {
        return player == null ? null : player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public boolean isEmptyArrow() {
        // 화살이 비어있는지 확인하는 코드
        return leftArrowAmount() == 0;
    }

    public int leftArrowAmount() {
        // 남은 화살의 개수를 반환하는 코드
        if(getPlayer() == null) return 0;

        int arrowAmount = 0;
        ItemStack[] items = getPlayer().getInventory().getContents();
        for (ItemStack item : items) {
            if (item != null && item.getType() == Material.ARROW) {
                arrowAmount += item.getAmount();
            }
        }
        return arrowAmount;
    }

}
