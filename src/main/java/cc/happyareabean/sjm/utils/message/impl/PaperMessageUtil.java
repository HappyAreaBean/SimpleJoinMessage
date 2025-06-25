package cc.happyareabean.sjm.utils.message.impl;

import cc.happyareabean.sjm.utils.message.AbstractMessageUtil;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PaperMessageUtil extends AbstractMessageUtil {

    public PaperMessageUtil(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public void sendMessage(Player player, ComponentLike componentLike) {
        player.sendMessage(componentLike);
    }

}
