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

    public static ItemStack createCustomArrowType1(int amount) {
        // 화살을 생성하는 코드
        ItemStack customArrow = new ItemStack(Material.ARROW, amount);
        ItemMeta customArrowMeta = customArrow.getItemMeta();
        customArrowMeta.setDisplayName("기본화살");

        List<String> lore = new ArrayList<>();
        lore.add("화살 타입 1");
        lore.add("한칸 제거");
        customArrowMeta.setLore(lore);

        PersistentDataContainer data = customArrowMeta.getPersistentDataContainer();
        data.set(KEY, PersistentDataType.INTEGER, 1);
        customArrow.setItemMeta(customArrowMeta);

        return customArrow;
    }

    public static ItemStack createCustomArrowType2(int amount) {
        // 화살을 생성하는 코드
        ItemStack customArrow = new ItemStack(Material.ARROW, amount);
        ItemMeta customArrowMeta = customArrow.getItemMeta();
        customArrowMeta.setDisplayName("가로 화살");

        List<String> lore = new ArrayList<>();
        lore.add("화살 타입 2");
        lore.add("좌우 추가 제거");
        customArrowMeta.setLore(lore);

        PersistentDataContainer data = customArrowMeta.getPersistentDataContainer();
        data.set(KEY, PersistentDataType.INTEGER, 2);
        customArrow.setItemMeta(customArrowMeta);

        return customArrow;
    }

    public static ItemStack createCustomArrowType3(int amount) {
        // 화살을 생성하는 코드
        ItemStack customArrow = new ItemStack(Material.ARROW, amount);
        ItemMeta customArrowMeta = customArrow.getItemMeta();
        customArrowMeta.setDisplayName("세로 화살");

        List<String> lore = new ArrayList<>();
        lore.add("화살 타입 3");
        lore.add("상하 추가 제거");
        customArrowMeta.setLore(lore);

        PersistentDataContainer data = customArrowMeta.getPersistentDataContainer();
        data.set(KEY, PersistentDataType.INTEGER, 3);
        customArrow.setItemMeta(customArrowMeta);

        return customArrow;
    }

    public void giveArrow(Player player, int amount1, int amount2, int amount3) {
        // 화살을 생성하는 코드
        player.getInventory().addItem(createCustomArrowType1(amount1));
        player.getInventory().addItem(createCustomArrowType2(amount2));
        player.getInventory().addItem(createCustomArrowType3(amount3));
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
