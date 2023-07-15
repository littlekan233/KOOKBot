package ml.littlekan.kookbot;

import lombok.Getter;
import lombok.Setter;
import ml.littlekan.kookbot.bot.BotSession;
import ml.littlekan.kookbot.bot.SQL;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class KOOKBot extends JavaPlugin {
    private static SQL sqli;
    private static boolean stop;
    @Getter @Setter private static FileConfiguration pluginConfig;
    @Getter private static BindManager bindManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        if(new File(getDataFolder(), "config.yml").exists()) saveDefaultConfig();
        sqli = new SQL(this);
        stop = false;
        pluginConfig = getConfig();
        bindManager = new BindManager();
        new BotSession(pluginConfig.getString("token"));
        getCommand("kook").setExecutor(new BotCommand());
        getLogger().info("§a插件启用成功！（ '▿ ' ）");
        getLogger().info("§a主人正在使用的版本：§61.0.0-SNAPSHOT");
        getLogger().info("§a作者：§6小阚LittleKan（littlekan233）");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        stop = true;
        getLogger().info("§a已关闭所有线程！");
        sqli.close();
        getLogger().info("§a已断开 §bSQLite §a数据库！");
        getLogger().info("§a主人再见！ (◍ ´꒳` ◍)");
    }

    public static void silentKillDaemon(){
        stop = true;
        sqli.close();
    }

    public static SQL getSQL(){
        return sqli;
    }

    public static boolean isStop(){
        return stop;
    }

    public static void updateConfig(){
        pluginConfig = JavaPlugin.getPlugin(KOOKBot.class).getConfig();
    }
}
