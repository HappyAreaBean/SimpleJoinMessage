package cc.happyareabean.sjm.utils.message;

import net.kyori.adventure.text.ComponentLike;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class AbstractMessageUtil {

    private final JavaPlugin plugin;

    protected AbstractMessageUtil(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public abstract void sendMessage(Player player, ComponentLike componentLike);

    private JavaPlugin getPlugin() {
        return plugin;
    }

}
