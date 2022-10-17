package cc.happyareabean.sjm.utils;

import cc.happyareabean.sjm.SimpleJoinMessage;
import cc.happyareabean.sjm.config.SJMConfig;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.audience.Audience;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MessageUtil {

	public static void sendMessageDelay(Player player, final List<String> messages, int delayTicks) {
		Audience aPlayer = SimpleJoinMessage.getAdventure().player(player);
		SJMConfig config = SimpleJoinMessage.getInstance().getSJMConfig();
		DateTimeFormatter formatter =
				DateTimeFormatter.ofPattern(config.getTimeFormat())
						.withZone( ZoneId.systemDefault() );
		new BukkitRunnable() {
			@Override
			public void run() {
				List<String> newMessage = new ArrayList<>();
				messages.forEach((m) -> newMessage.add(m.replace("{player}", player.getName())
						.replace("{online}", String.valueOf(Bukkit.getServer().getOnlinePlayers().size()))
						.replace("{time}", formatter.format(Instant.now()))));
				aPlayer.sendMessage(SimpleJoinMessage.MINIMESSAGE.deserialize(String.join("\n", (SimpleJoinMessage.getInstance().isPAPISupported() ? PlaceholderAPI.setPlaceholders(player, newMessage) : newMessage))));
			}
		}.runTaskLater(SimpleJoinMessage.getInstance(), (delayTicks > 0 ? delayTicks : 1L));
	}
}
