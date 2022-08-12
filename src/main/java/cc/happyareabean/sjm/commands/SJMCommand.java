package cc.happyareabean.sjm.commands;

import cc.happyareabean.sjm.SimpleJoinMessage;
import cc.happyareabean.sjm.utils.Constants;
import cc.happyareabean.sjm.utils.MessageUtil;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static cc.happyareabean.sjm.SimpleJoinMessage.MINIMESSAGE;
import static net.kyori.adventure.text.Component.newline;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.event.ClickEvent.openUrl;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

public class SJMCommand implements CommandExecutor {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		Audience aSender = SimpleJoinMessage.getAdventure().sender(sender);
		if (args.length == 0) {
			label = "/" + label;
			aSender.sendMessage(Component.textOfChildren(
					Constants.HEADER,
					newline(),
					text()
							.content("SimpleJoinMessage " + Constants.VERSION)
							.color(TextColor.fromCSSHexString("#e78960"))
							.clickEvent(openUrl("https://go.happyareabean/sjm"))
							.hoverEvent(MINIMESSAGE.deserialize("<rainbow>click me!")),
					newline(),
					text()
							.content("By ")
							.color(GRAY)
							.append(text("HappyAreaBean", GREEN)),
					newline(),
					Constants.HEADER,
					newline(),
					text().content(label + " reload - ").append(text("Reload SJM", GRAY))
							.append(newline()),
					text().content(label + " reloadshow (rs) - ").append(text("Show the join message after reloading SJM settings", GRAY))
							.append(newline()),
					text().content(label + " about - ").append(text("Show SJM plugin info", GRAY))
							.append(newline()),
					text().content(label + " show - ").append(text("Send a join message in your chat", GRAY))
							.append(newline()),
					Constants.HEADER
			));
		} else if (args[0].equalsIgnoreCase("reload")) {
			SimpleJoinMessage.getInstance().getSJMConfig().loadAndSave();
			aSender.sendMessage(Constants.PREFIX.append(text(" Reloaded settings.")));
		} else if (args[0].equalsIgnoreCase("reloadshow") || args[0].equalsIgnoreCase("rs")) {
			SimpleJoinMessage.getInstance().getSJMConfig().loadAndSave();
			aSender.sendMessage(Constants.PREFIX.append(text(" Reloaded settings.")));
			if (sender instanceof ConsoleCommandSender) {
				aSender.sendMessage(Constants.PREFIX.append(text(" The settings have been reloaded, but you're in console mode, so the message won't be sent.", RED)));
				return true;
			}
			MessageUtil.sendMessageDelay((Player) sender, SimpleJoinMessage.getInstance().getSJMConfig().getJoinMessage(), 0);
		} else if (args[0].equalsIgnoreCase("show")) {
			if (!(sender instanceof Player)) {
				aSender.sendMessage(Constants.PREFIX.append(text(" You're not a player! The join message can only be shown to the player.", RED)));
				return true;
			}
			MessageUtil.sendMessageDelay((Player) sender, SimpleJoinMessage.getInstance().getSJMConfig().getJoinMessage(), 0);
		} else if (args[0].equalsIgnoreCase("about")) {
			aSender.sendMessage(Component.textOfChildren(
					Constants.HEADER,
					newline(),
					text()
							.content("SimpleJoinMessage " + Constants.VERSION)
							.color(TextColor.fromCSSHexString("#e78960"))
							.clickEvent(openUrl("https://go.happyareabean/sjm"))
							.hoverEvent(MINIMESSAGE.deserialize("<rainbow>click me!")),
					newline(),
					text()
							.content("By ")
							.color(GRAY)
							.append(text("HappyAreaBean", GREEN)),
					newline(),
					Constants.HEADER
			));
		}

		return true;
	}
}
