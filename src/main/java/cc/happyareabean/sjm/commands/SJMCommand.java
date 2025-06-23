package cc.happyareabean.sjm.commands;

import cc.happyareabean.sjm.SimpleJoinMessage;
import cc.happyareabean.sjm.config.SJMConfig;
import cc.happyareabean.sjm.utils.AdventureWebEditorAPI;
import cc.happyareabean.sjm.utils.CommandUtils;
import cc.happyareabean.sjm.utils.Constants;
import cc.happyareabean.sjm.utils.MessageUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.CommandPlaceholder;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Named;
import revxrsal.commands.annotation.Range;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;
import revxrsal.commands.help.Help;

import java.util.Arrays;
import java.util.stream.Collectors;

import static cc.happyareabean.sjm.SimpleJoinMessage.MINIMESSAGE;
import static net.kyori.adventure.text.Component.newline;
import static net.kyori.adventure.text.Component.space;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.event.ClickEvent.clickEvent;
import static net.kyori.adventure.text.event.ClickEvent.openUrl;

@Command({"simplejoinmessage", "sjm"})
@CommandPermission("sjm.admin")
public class SJMCommand {

	@CommandPlaceholder
	@Description("SimpleJoinMessage commands list")
	public void help(BukkitCommandActor actor, @Range(min = 1) @Default("1") int page, Help.RelatedCommands<BukkitCommandActor> helpEntries) {
		CommandUtils.handleHelpMenu(actor, page, helpEntries, 5, "sjm");
	}

	@Subcommand("reload")
	@Description("Reload SimpleJoinMessage")
	public void reload(BukkitCommandActor actor) {
		SimpleJoinMessage.getInstance().getSJMConfig().reloadAndSave();
		SimpleJoinMessage.getInstance().loadCustomCommand(true);
		actor.reply(Constants.PREFIX.append(text("Settings and commands reloaded.")));
	}

	@Subcommand({"reloadshow", "rs"})
	@Description("Reload SimpleJoinMessage settings and show the join message after reloading")
	public void reloadShow(BukkitCommandActor actor) {
		reload(actor);
		if (!actor.isPlayer()) {
			actor.reply(Constants.PREFIX.append(text("The settings have been reloaded, but you're in console mode, so the message won't be sent.", NamedTextColor.RED)));
			return;
		}
		MessageUtil.sendMessageDelay(actor.asPlayer(), SimpleJoinMessage.getInstance().getSJMConfig().getJoinMessage(), 0);
	}

	@Subcommand({"show"})
	@Description("Send a join message in your chat")
	public void show(BukkitCommandActor actor) {
		actor.requirePlayer();
		MessageUtil.sendMessageDelay(actor.asPlayer(), SimpleJoinMessage.getInstance().getSJMConfig().getJoinMessage(), 0);
	}

	@Subcommand({"editor"})
	@Description("Open join message editor")
	public void editor(BukkitCommandActor actor) {
		SJMConfig config = SimpleJoinMessage.getInstance().getSJMConfig();
		AdventureWebEditorAPI api = SimpleJoinMessage.getInstance().getAdventureWebEditorAPI();
		actor.reply(Constants.PREFIX.append(text("Preparing new editor session, please wait...", NamedTextColor.GRAY)));
		api.startSession(String.join("\n", config.getJoinMessage()), "/sjm applyedits {token}", Constants.USER_AGENT)
				.whenComplete((token, throwable) -> {
					if (throwable != null) {
						actor.reply(Constants.PREFIX.append(text("Error occurred when processing your request: " + throwable.getMessage(), NamedTextColor.RED)));
						return;
					}
					String url = config.getAdventureWebURL() + "?token=" + token;

					actor.reply(Constants.PREFIX.append(text("Click the link below to open the editor:", NamedTextColor.GREEN)));
					actor.reply(text(url, NamedTextColor.AQUA).clickEvent(clickEvent(ClickEvent.Action.OPEN_URL, url)));
				});
	}

	@Subcommand({"applyedits"})
	@Description("Apply edit from editor")
	public void applyEdits(BukkitCommandActor actor, @Named("token") String token) {
		SJMConfig config = SimpleJoinMessage.getInstance().getSJMConfig();
		AdventureWebEditorAPI api = SimpleJoinMessage.getInstance().getAdventureWebEditorAPI();
		actor.reply(Constants.PREFIX.append(text("Applying your edits...", NamedTextColor.GRAY)));
		api.retrieveSession(token)
				.whenComplete((value, throwable) -> {
					if (throwable != null) {
						actor.reply(Constants.PREFIX.append(text("Error occurred when processing your request: " + throwable.getMessage(), NamedTextColor.RED)));
						return;
					}

					config.setJoinMessage(Arrays.stream(value.split("\n")).collect(Collectors.toList()));
					config.save();
					config.reloadAndSave();
					actor.reply(Constants.PREFIX
							.append(text("Web editor data was applied successfully.", NamedTextColor.GREEN))
							.append(space())
							.append(text("Click here to view join message", NamedTextColor.WHITE, TextDecoration.BOLD)
									.hoverEvent(text("Click here to run /sjm show", NamedTextColor.YELLOW))
									.clickEvent(clickEvent(ClickEvent.Action.RUN_COMMAND, "/sjm show")))
					);
				});
	}

	@Subcommand({"regenerate"})
	@Description("Regenerate SJM settings back to default")
	public void regenerate(BukkitCommandActor actor) {
		actor.reply(Constants.PREFIX.append(text("Regenerating SJM settings...")));
		String fileName = SimpleJoinMessage.getInstance().regenerateSettings();
		actor.reply(Constants.PREFIX.append(text("Settings has been regenerated and a backup has been saved as "))
				.append(text(fileName, NamedTextColor.GRAY)));
	}

	@Subcommand({"about"})
	@Description("Show SimpleJoinMessage plugin info <3")
	public void about(BukkitCommandActor actor) {
		actor.reply(Component.textOfChildren(
				Constants.HEADER,
				newline(),
				MINIMESSAGE.deserialize(String.format("<gradient:#f7ff00:#db36a4><b>SimpleJoinMessage</b></gradient> <white>%s</white>", Constants.VERSION))
						.clickEvent(openUrl("https://go.happyareabean.cc/sjm"))
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