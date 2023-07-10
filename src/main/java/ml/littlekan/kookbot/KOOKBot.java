package ml.littlekan.kookbot;

import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import ml.littlekan.kookbot.bot.SQL;

public final class KOOKBot extends JavaPlugin {
    private static SQL sqli;
    private static boolean stop;

    @Override
    public void onEnable() {
        // Plugin startup logic
        if(new File(getDataFolder(), "config.yml").exists()) saveDefaultConfig();
        sqli = new SQL(this);
        stop = false;
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

    public static SQL getSql(){
        return sqli;
    }

    public static boolean isStop(){
        return stop;
    }
}
