package cc.happyareabean.sjm.utils;

import cc.happyareabean.sjm.SimpleJoinMessage;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.audience.Audience;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class MessageUtil {

	public static void sendMessageDelay(Player player, final List<String> messages, int delayTicks) {
		Audience aPlayer = SimpleJoinMessage.getAdventure().player(player);
		new BukkitRunnable() {
			@Override
			public void run() {
				List<String> newMessage = new ArrayList<>();
				messages.forEach((m) -> newMessage.add(m.replace("{player}", player.getName())));
				aPlayer.sendMessage(SimpleJoinMessage.MINIMESSAGE.deserialize(String.join("\n", (SimpleJoinMessage.getInstance().isPAPISupported() ? PlaceholderAPI.setPlaceholders(player, newMessage) : newMessage))));
			}
		}.runTaskLater(SimpleJoinMessage.getInstance(), (delayTicks > 0 ? delayTicks : 1L));
	}
}
