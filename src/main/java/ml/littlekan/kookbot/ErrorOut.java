package ml.littlekan.kookbot;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class ErrorOut {
    public ErrorOut(Throwable e){
        Logger logger = JavaPlugin.getPlugin(KOOKBot.class).getLogger();
        logger.warning("插件报错啦！＞﹏＜");
        e.printStackTrace();
    }
}
