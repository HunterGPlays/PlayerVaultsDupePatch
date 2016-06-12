package net.terrocidepvp.playervaultsdupepatch;

import net.terrocidepvp.playervaultsdupepatch.utils.ColorCodeUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class PlayerVaultsDupePatch extends JavaPlugin implements Listener {

    private List<String> message;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadConfig();

        if (!getConfig().isSet("config-version")) {
            getLogger().severe("The config.yml file is broken!");
            getLogger().severe("The plugin failed to detect a 'config-version'.");
            getLogger().severe("The plugin will not load until you generate a new, working config OR if you fix the config.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        int configVersion = 3;
        /*
         Updated for: N/A
         Release: v1.0.0
        */
        if (getConfig().getInt("config-version") != configVersion) {
            getLogger().severe("Your config is outdated!");
            getLogger().severe("The plugin will not load unless you change the config version to " + configVersion + ".");
            getLogger().severe("This means that you will need to reset your config, as there may have been major changes to the plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        message = ColorCodeUtil.translate(getConfig().getStringList("message"));

        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void onBlockBreak(BlockBreakEvent event) {
        if (!event.isCancelled()) return;

        Player player = event.getPlayer();
        if (player == null) return;

        Block block = event.getBlock();
        if (player.getLocation().getBlockX() != block.getLocation().getBlockX()
                || player.getLocation().getBlockZ() != block.getLocation().getBlockZ()) return;
        if (block.getType() == Material.WATER_LILY) {
            player.teleport(player.getLocation().add(0, 1, 0));
            message.forEach(player::sendMessage);
        }
    }

}
