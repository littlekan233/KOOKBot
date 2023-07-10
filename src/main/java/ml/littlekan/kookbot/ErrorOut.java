package ml.littlekan.kookbot;

import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

public class ErrorOut {
    public ErrorOut(Exception e){
        Logger logger = JavaPlugin.getPlugin(KOOKBot.class).getLogger();
        logger.warning("插件报错啦！>~<");
        e.printStackTrace();
    }
}
