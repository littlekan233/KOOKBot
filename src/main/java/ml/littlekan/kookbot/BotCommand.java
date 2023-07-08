package ml.littlekan.kookbot;


import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

public class BotCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(
                    ChatColor.translateAlternateColorCodes('&', "&aKOOKBot插件正在运行 版本1.0.0-SNAPSHOT >w<")
            );
            sender.sendMessage(
                    ChatColor.translateAlternateColorCodes('&', "&a输入 &6/kook help &a获取帮助！")
            );
        }
        return true;
    }
}
