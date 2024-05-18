package bbib.plugintesting;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {

    private static final NamespacedKey KEY= new NamespacedKey("plugin", "type");

    public static ItemStack createCustomArrowType(int amount,int arrowType, String name, String lore1, String lore2) {
        // 화살을 생성하는 코드
        ItemStack customArrow = new ItemStack(Material.ARROW, amount);
        ItemMeta customArrowMeta = customArrow.getItemMeta();
        customArrowMeta.setDisplayName(name);

        List<String> lore = new ArrayList<>();
        lore.add(lore1);
        lore.add(lore2);
        customArrowMeta.setLore(lore);

        PersistentDataContainer data = customArrowMeta.getPersistentDataContainer();
        data.set(KEY, PersistentDataType.INTEGER, arrowType);
        customArrow.setItemMeta(customArrowMeta);

        return customArrow;
    }

    public void giveArrow(Player player, int amount1, int amount2, int amount3) {
        // 화살을 생성하는 코드
        player.getInventory().addItem(createCustomArrowType(amount1,1, "기본화살", "화살 타입 1", "한칸 제거"));
        player.getInventory().addItem(createCustomArrowType(amount2,2, "가로 화살", "화살 타입 2", "좌우 추가 제거"));
        player.getInventory().addItem(createCustomArrowType(amount3, 3,"세로 화살", "화살 타입 3", "상하 추가 제거"));
    }

    public void giveBow(Player player) {
        // 활을 생성하는 코드
        player.getInventory().addItem(new ItemStack(Material.BOW));
    }

    public static int readData(ItemStack item) {
        // 화살의 타입을 읽어오는 코드
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            // ItemStack이 아이템을 나타내지 않는 경우, 0을 반환합니다.
            return 0;
        }
        PersistentDataContainer data = meta.getPersistentDataContainer();
        int type = data.getOrDefault(KEY, PersistentDataType.INTEGER, 0);
        return type;
    }
}
