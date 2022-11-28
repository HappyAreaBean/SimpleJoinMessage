package cc.happyareabean.sjm.commands;

import cc.happyareabean.sjm.SimpleJoinMessage;
import cc.happyareabean.sjm.utils.Constants;
import cc.happyareabean.sjm.utils.MessageUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;
import revxrsal.commands.help.CommandHelp;

import java.util.ArrayList;
import java.util.List;

import static cc.happyareabean.sjm.SimpleJoinMessage.LEGACY_SERIALIZER;
import static cc.happyareabean.sjm.SimpleJoinMessage.MINIMESSAGE;
import static net.kyori.adventure.text.Component.newline;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.event.ClickEvent.openUrl;

@Command({"simplejoinmessage", "sjm"})
@CommandPermission("sjm.admin")
public class SJMCommand {

	@Default
	@Description("SimpleJoinMessage commands list")
	public void help(BukkitCommandActor actor, CommandHelp<String> helpEntries, @Default("1") int page) {
		List<Component> list = new ArrayList<>();
		list.add(Constants.HEADER);
		list.add(MINIMESSAGE.deserialize(String.format("<gradient:#f7ff00:#db36a4><b>SimpleJoinMessage</b></gradient> <white>%s</white>", Constants.VERSION))
				.clickEvent(openUrl("https://go.happyareabean/sjm"))
				.hoverEvent(MINIMESSAGE.deserialize("<rainbow>click me!")));
		list.add(text()
				.content("By ")
				.color(NamedTextColor.GRAY)
				.append(text("HappyAreaBean", NamedTextColor.GREEN)).build());
		list.add(Constants.HEADER);
		helpEntries.paginate(page, 10).forEach(s -> list.add(LEGACY_SERIALIZER.deserialize(s)));
		list.add(Constants.HEADER);
		list.forEach(actor::reply);
	}

	@Subcommand("reload")
	@Description("Reload SimpleJoinMessage")
	public void reload(BukkitCommandActor actor) {
		SimpleJoinMessage.getInstance().getSJMConfig().reloadAndSave();
		actor.reply(Constants.PREFIX.append(text(" Settings reloaded.")));
	}

	@Subcommand({"reloadshow", "rs"})
	@Description("Reload SimpleJoinMessage settings and show the join message after reloading")
	public void reloadShow(BukkitCommandActor actor) {
		reload(actor);
		if (!actor.isPlayer()) {
			actor.reply(Constants.PREFIX.append(text(" The settings have been reloaded, but you're in console mode, so the message won't be sent.", NamedTextColor.RED)));
			return;
		}
		MessageUtil.sendMessageDelay(actor.getAsPlayer(), SimpleJoinMessage.getInstance().getSJMConfig().getJoinMessage(), 0);
	}

	@Subcommand({"show"})
	@Description("Send a join message in your chat")
	public void show(BukkitCommandActor actor) {
		actor.requirePlayer();
		MessageUtil.sendMessageDelay(actor.getAsPlayer(), SimpleJoinMessage.getInstance().getSJMConfig().getJoinMessage(), 0);
	}

	@Subcommand({"about"})
	@Description("Show SimpleJoinMessage plugin info <3")
	public void about(BukkitCommandActor actor) {
		actor.reply(Component.textOfChildren(
				Constants.HEADER,
				newline(),
				MINIMESSAGE.deserialize(String.format("<gradient:#f7ff00:#db36a4><b>SimpleJoinMessage</b></gradient> <white>%s</white>", Constants.VERSION))
						.clickEvent(openUrl("https://go.happyareabean/sjm"))
						.hoverEvent(MINIMESSAGE.deserialize("<rainbow>click me!")),
				newline(),
				text()
						.content("By ")
						.color(NamedTextColor.GRAY)
						.append(text("HappyAreaBean", NamedTextColor.GREEN)),
				newline(),
				Constants.HEADER
		));
	}

}