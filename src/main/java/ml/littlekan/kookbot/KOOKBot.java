package ml.littlekan.kookbot;

import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;

public final class KOOKBot extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        if(new File(getDataFolder(), "config.yml").exists()) saveDefaultConfig();
        getCommand("kook").setExecutor(new BotCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
