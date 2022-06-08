package cc.happyareabean.sjm.listener;

import cc.happyareabean.sjm.SimpleJoinMessage;
import cc.happyareabean.sjm.config.SJMConfig;
import cc.happyareabean.sjm.utils.MessageUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		SJMConfig config = SimpleJoinMessage.getInstance().getSJMConfig();
		MessageUtil.sendMessageDelay(player, config.getJoinMessage(), config.getDelayTicks());
	}
}
