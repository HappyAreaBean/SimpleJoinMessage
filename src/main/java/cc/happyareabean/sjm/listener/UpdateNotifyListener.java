package cc.happyareabean.sjm.listener;

import cc.happyareabean.sjm.SimpleJoinMessage;
import cc.happyareabean.sjm.utils.Constants;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static net.kyori.adventure.text.Component.newline;
import static net.kyori.adventure.text.Component.space;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.WHITE;

public class UpdateNotifyListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission("sjm.admin")) return;
        if (SimpleJoinMessage.NEXT_VERSION.isEmpty()) return;

        SimpleJoinMessage.getMessageUtil().sendMessage(player, Component.textOfChildren(
                Constants.PREFIX,
                text("New version", WHITE),
                space(),
                text(SimpleJoinMessage.NEXT_VERSION, NamedTextColor.YELLOW, TextDecoration.BOLD),
                space(),
                text("is available!", WHITE),
                newline(),
                Constants.PREFIX,
                text("You are currently running"),
                space(),
                text(SimpleJoinMessage.getInstance().getDescription().getVersion()),
                text("."),
                newline(),
                Constants.PREFIX,
                text("Click here to download update!", NamedTextColor.GOLD, TextDecoration.BOLD)
                        .clickEvent(ClickEvent.openUrl(SimpleJoinMessage.DOWNLOAD_URL))
        ).color(NamedTextColor.GRAY));

    }

}
