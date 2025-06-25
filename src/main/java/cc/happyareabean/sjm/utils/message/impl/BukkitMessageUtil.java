package cc.happyareabean.sjm.utils.message.impl;

import cc.happyareabean.sjm.utils.message.AbstractMessageUtil;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitMessageUtil extends AbstractMessageUtil {

    private final BukkitAudiences audiences;

    public BukkitMessageUtil(JavaPlugin plugin) {
        super(plugin);
        audiences = BukkitAudiences.create(plugin);
    }

    @Override
    public void sendMessage(Player player, ComponentLike componentLike) {
        audiences.player(player).sendMessage(componentLike);
    }

}
