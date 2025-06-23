package cc.happyareabean.sjm.commands;

import cc.happyareabean.sjm.SimpleJoinMessage;
import cc.happyareabean.sjm.utils.MessageUtil;
import revxrsal.commands.annotation.CommandPlaceholder;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.orphan.OrphanCommand;

public class CustomShowCommand implements OrphanCommand {

    @CommandPlaceholder
    public void handle(BukkitCommandActor actor) {
        MessageUtil.sendMessageDelay(actor.asPlayer(), SimpleJoinMessage.getInstance().getSJMConfig().getJoinMessage(), 0);
    }

}
