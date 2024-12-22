package cc.happyareabean.sjm.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.Nullable;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.command.ExecutableCommand;
import revxrsal.commands.help.Help;

import java.util.ArrayList;
import java.util.List;

import static cc.happyareabean.sjm.SimpleJoinMessage.LEGACY_SERIALIZER;
import static cc.happyareabean.sjm.SimpleJoinMessage.MINIMESSAGE;
import static cc.happyareabean.sjm.utils.Constants.PAGE_TEXT;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.event.ClickEvent.openUrl;

public class CommandUtils {

    public static void handleHelpMenu(BukkitCommandActor actor,
                                      int page,
                                      Help.CommandList<BukkitCommandActor> commands,
                                      int elementsPerPage,
                                      @Nullable String filterFor) {
        List<ExecutableCommand<BukkitCommandActor>> list = new ArrayList<>();

        if (filterFor != null) {
            for (ExecutableCommand<BukkitCommandActor> command : commands.all()) {
                String usage = command.usage();
                if (!usage.startsWith(filterFor)) continue;
                list.add(command);
            }
        } else {
            list = commands.all();
        }

        List<ExecutableCommand<BukkitCommandActor>> commandList = Help.paginate(list, page, elementsPerPage);

        TextComponent.Builder builder = Component.text();

        builder.append(Constants.HEADER);
        builder.appendNewline();
        builder.append(MINIMESSAGE.deserialize(String.format("<gradient:#f7ff00:#db36a4><b>SimpleJoinMessage</b></gradient> <white>%s</white>",
                        Constants.VERSION))
                .clickEvent(openUrl("https://go.happyareabean.cc/sjm"))
                .hoverEvent(MINIMESSAGE.deserialize("<rainbow>click me!")));
        builder.appendNewline();
        builder.append(text()
                .content("By ")
                .color(NamedTextColor.GRAY)
                .append(text("HappyAreaBean", NamedTextColor.GREEN)).build());
        builder.appendNewline();
        builder.append(Constants.HEADER);
        builder.appendNewline();

        commandList.forEach(command -> {
            builder.append(LEGACY_SERIALIZER.deserialize(String.format(" &8• &e/%s %s", command.usage(), (command.description() != null ? "&7- &f" + command.description() : ""))));
            builder.appendNewline();
        });

        int numberOfPages = Help.numberOfPages(list.size(), elementsPerPage);
        if (numberOfPages > 1) {
            builder.appendNewline();
            builder.append(paginateNavigation(page, numberOfPages, Constants.HELP_COMMAND_FORMAT));
            builder.appendNewline();
        }

        builder.append(Constants.HEADER);

        actor.reply(builder.build());
    }

    public static void handleHelpMenu(BukkitCommandActor actor, int page, Help.CommandList<BukkitCommandActor> commands, int elementsPerPage) {
        handleHelpMenu(actor, page, commands, elementsPerPage, null);
    }

    public static Component paginateNavigation(int currentPage, int maxPage, String commandFormat) {
        int previousPage = currentPage - 1;
        int nextPage = currentPage + 1;

        boolean havePreviousPage = previousPage != 0;
        boolean haveNextPage = maxPage != currentPage;

        TextComponent.Builder pageText = text()
                .decorate(TextDecoration.BOLD)
                .color(NamedTextColor.YELLOW);

        pageText.append(text("«", !havePreviousPage ? NamedTextColor.DARK_GRAY : null)
                .clickEvent(havePreviousPage ? ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, String.format(commandFormat, previousPage)) : null)
                .hoverEvent(havePreviousPage ? text(String.format(PAGE_TEXT, previousPage)).color(NamedTextColor.GOLD) : null));

        pageText.append(text(" ▍ "));

        pageText.append(text("»", !haveNextPage ? NamedTextColor.DARK_GRAY : null)
                .clickEvent(haveNextPage ? ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, String.format(commandFormat, nextPage)) : null)
                .hoverEvent(haveNextPage ? text(String.format(PAGE_TEXT, nextPage)).color(NamedTextColor.GOLD) : null));

        return pageText.build();
    }

}
