package cc.happyareabean.sjm;

import cc.happyareabean.sjm.commands.SJMCommand;
import cc.happyareabean.sjm.commands.SJMTabComplete;
import cc.happyareabean.sjm.config.SJMConfig;
import cc.happyareabean.sjm.listener.PlayerJoinListener;
import lombok.Getter;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class SimpleJoinMessage extends JavaPlugin {

	@Getter private static SimpleJoinMessage instance;
	@Getter public static final MiniMessage MINIMESSAGE = MiniMessage.miniMessage();
	@Getter private static BukkitAudiences adventure;

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
		getServer().getPluginCommand("simplejoinmessage").setExecutor(new SJMCommand());
		getServer().getPluginCommand("simplejoinmessage").setTabCompleter(new SJMTabComplete());

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
