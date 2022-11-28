package cc.happyareabean.sjm;

import cc.happyareabean.sjm.commands.SJMCommand;
import cc.happyareabean.sjm.config.SJMConfig;
import cc.happyareabean.sjm.listener.PlayerJoinListener;
import cc.happyareabean.sjm.utils.AdventureWebEditorAPI;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;
import revxrsal.commands.bukkit.BukkitCommandHandler;

import java.io.File;

public class SimpleJoinMessage extends JavaPlugin {

	@Getter private static SimpleJoinMessage instance;
	@Getter public static final MiniMessage MINIMESSAGE = MiniMessage.miniMessage();
	@Getter public static final LegacyComponentSerializer LEGACY_SERIALIZER = LegacyComponentSerializer.legacy('&');
	@Getter private static BukkitAudiences adventure;
	@Getter private BukkitCommandHandler commandHandler;
	@Getter private SJMConfig SJMConfig;

	@Override
	public void onEnable() {
		instance = this;
		adventure = BukkitAudiences.create(this);

		getLogger().info("Loading settings...");
		SJMConfig = new SJMConfig(new File(getDataFolder(), "settings.yml").toPath());

		getLogger().info("Registering listener...");
		getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);

		getLogger().info("Registering commands...");
		commandHandler = BukkitCommandHandler.create(this);
		commandHandler.enableAdventure(adventure);
		commandHandler.setHelpWriter((command, actor) -> {
			StringBuilder sb = new StringBuilder();
			sb.append("&8• &e/");
			sb.append(command.getPath().toRealString());
			if (!command.getUsage().isEmpty()) sb.append(command.getUsage());
			sb.append(" &7- &f");
			sb.append(command.getDescription());
			return sb.toString();
		});
		commandHandler.register(new SJMCommand());

		getLogger().info("Check supported plugins...");
		checkSupportedPlugin();

		new Metrics(this, 15462);

		getLogger().info("SimpleJoinMessage version " + getDescription().getVersion() + " has been successfully enabled!");
	}

	public void checkSupportedPlugin() {
		if (!isPAPISupported()) {
			getLogger().warning("PlaceholderAPI is not enabled, you will not be able to use any placeholder from PlaceholderAPI.");
		}
	}

	public boolean isPAPISupported() {
		return getServer().getPluginManager().isPluginEnabled("PlaceholderAPI");
	}
}
