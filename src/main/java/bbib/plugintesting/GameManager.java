package bbib.plugintesting;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class GameManager implements Listener{
    private final JavaPlugin plugin;
    private final List<Block> targetList = new ArrayList<>();
    private final List<Block> fenceList = new ArrayList<>();
    private final ItemManager itemManager;
    private final PlayerManager playerManager;
    private int TARGET_SIZE;
    private int arrowType;
    private Instant startTime;
    private Instant endTime;
    private boolean isGameRunning = false;

    public List<Block> getTargetList() {
        return targetList;
    }

    public int getArrowType() {
        return arrowType;
    }

    public void setArrowType( ) {
        this.arrowType = ItemManager.readData(playerManager.getPlayer().getInventory().getItemInOffHand());
    }

    public GameManager(JavaPlugin plugin, ItemManager itemManager, PlayerManager playerManager) {
        this.plugin = plugin;
        this.itemManager = itemManager;
        this.playerManager = playerManager;
    }

    public void startGame(Player player, int targetSize, int type1ArrowAmount, int type2ArrowAmount, int type3ArrowAmount) {
        if(isGameRunning) {
            Bukkit.broadcast(Component.text("게임이 이미 시작되었습니다."));
            return;
        }
        this.startTime = Instant.now();
        playerManager.setPlayer(player);
        TARGET_SIZE = targetSize;
        itemManager.giveArrow(player, type1ArrowAmount, type2ArrowAmount, type3ArrowAmount);
        itemManager.giveBow(player);
        createTarget();
        createFence();
        isGameRunning = true;
    }

    public void endGame() {
        this.endTime = Instant.now();
        isGameRunning = false;
        Bukkit.broadcast(Component.text("남은 화살: "+playerManager.leftArrowAmount()+"개"+", 남은 타겟: "+targetList.size()+"개"));
        clearTargets();
        clearInventory();
        removeFence();
        Bukkit.broadcast(Component.text("게임 종료, 플레이 시간: "+calculatePlayTime()+"초"));
    }

    public long calculatePlayTime() {
        if(startTime == null || endTime == null) {
            return 0;
        }
        return Duration.between(startTime, endTime).toSeconds();
    }

    public void clearInventory() {
        if (playerManager.getPlayer() == null) {
            return;
        }
        playerManager.getPlayer().getInventory().clear();
    }

    public boolean isGameEnd() {
        if(isTargetEmpty()) {
            playerManager.getPlayer().sendMessage("플레이어 승리");
            return true;
        }
        else if (playerManager.isEmptyArrow())
        {
            playerManager.getPlayer().sendMessage("플레이어 패배");
            return true;
        }
        return false;
    }

    public void createTarget() {
        Player player = playerManager.getPlayer();
        Location playerLocation = player.getLocation();
        Vector playerDirection = playerLocation.getDirection();

        boolean useXAxis = Math.abs(playerDirection.getX()) > Math.abs(playerDirection.getZ());

        if(useXAxis){
            playerDirection = new Vector(playerDirection.getX(),0,0);
        }
        else {
            playerDirection = new Vector(0,0,playerDirection.getZ());
        }

        Location targetLocation = playerLocation.clone().add(playerDirection.multiply(21)); // 플레이어가 바라보는 방향으로 20블록 앞

        if(targetLocation.getBlock().getType() != Material.AIR) {
            targetLocation = getTopBlockLocation(targetLocation);
        }

        for (int i = 0; i < TARGET_SIZE; i++) {
            for (int j = 0; j < TARGET_SIZE; j++) {
                Location blockLocation;
                if (useXAxis) {
                    blockLocation = targetLocation.clone().add(0, j, i-TARGET_SIZE/2);
                } else {
                    blockLocation = targetLocation.clone().add(i-TARGET_SIZE/2, j, 0);
                }
                Block targetBlock = blockLocation.getBlock();
                targetList.add(targetBlock);
                targetBlock.setType(Material.TARGET);
            }
        }
    }

    public void createFence() {
        Player player = playerManager.getPlayer();
        Location playerLocation = player.getLocation();
        Vector playerDirection = playerLocation.getDirection();
        Block playerBlock = playerLocation.getBlock().getRelative(0,-1,0);
        playerBlock.setType(Material.RED_CONCRETE);


        boolean useXAxis = Math.abs(playerDirection.getX()) > Math.abs(playerDirection.getZ());

        Location targetLocation = playerLocation.clone().add(playerDirection.multiply(1));
        if(targetLocation.getBlock().getType() != Material.AIR) {
            targetLocation = getTopBlockLocation(targetLocation);
        }

        for (int i = 0; i < 3; i++) {
            Location blockLocation;
            if (useXAxis) {
                blockLocation = targetLocation.clone().add(0, 0, i-1);
            } else {
                blockLocation = targetLocation.clone().add(i-1, 0, 0);
            }
            Block targetBlock = blockLocation.getBlock();
            fenceList.add(targetBlock);
            targetBlock.setType(Material.OAK_FENCE);
        }
    }

    public void removeFence() {
        if(fenceList.isEmpty()) {
            return;
        }
        for(Block fence : fenceList) {
            fence.setType(Material.AIR);
        }
        fenceList.clear();
    }

    private Location getTopBlockLocation(Location location) {
        World world = location.getWorld();
        int x = location.getBlockX();
        int z = location.getBlockZ();

        // 해당 x, z 좌표의 최고 높이 블록 위치를 가져옵니다.
        int y = world.getHighestBlockYAt(x, z);
        return new Location(world, x, y + 1, z); // 타겟을 최고 블록 위에 배치
    }

    public void removeTargetByType(Block hitBlock,int arrowType) {
        if(targetList.contains(hitBlock) && arrowType == 1) {
            removeTarget(hitBlock);
        }
        else if(targetList.contains(hitBlock) && arrowType == 2) {
            removeTarget(hitBlock.getRelative(1,0,0));
            removeTarget(hitBlock.getRelative(-1,0,0));
            removeTarget(hitBlock.getRelative(0,0,1));
            removeTarget(hitBlock.getRelative(0,0,-1));
            removeTarget(hitBlock);
        }
        else if(targetList.contains(hitBlock) && arrowType == 3) {
            removeTarget(hitBlock.getRelative(0,1,0));
            removeTarget(hitBlock.getRelative(0,-1,0));
            removeTarget(hitBlock);
        }
    }

    public void removeTarget(Block hitBlock) {
        if(targetList.contains(hitBlock)) {
            targetList.remove(hitBlock);
            hitBlock.setType(Material.AIR);
        }
    }

    public void clearTargets() {
        if(targetList.isEmpty()) {
            return;
        }
        for(Block target : targetList) {
            target.setType(Material.AIR);
        }
        targetList.clear();
    }

    public boolean isTargetEmpty() {
        return targetList.isEmpty();
    }

    public int calculateDistance(Location loc1, Location loc2) {
        return (int) loc1.distance(loc2);
    }

}
