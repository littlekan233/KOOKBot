package ml.littlekan.kookbot;

import java.util.List;
import java.util.ArrayList;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

public class BotCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length <= 0) {
            msg(sender, "&aKOOKBot插件正在运行 版本1.0.0-SNAPSHOT >w<");
            msg(sender, "&a输入 &6/kook help &a获取帮助！");
        }else{
            switch (args[0]) {
                case "invite":
                    String url = JavaPlugin.getPlugin(KOOKBot.class).getConfig().getString("invite-url");
                    msg(sender, "&b此服务器的KOOK链接：&6&n" + url);
                    break;
                case "help":
                    msg(sender, "&7----- &a[KOOKBot]&7 -----");
                    msg(sender, "&6/kook invite &7-- &a获取本服务器的KOOK邀请链接");
                    msg(sender, "&6/kook help &7-- &a显示此帮助");
                    break;
                default:
                    msg(sender, "&c无效的命令 T_T");
                    break;
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args){
        List<String> list = new ArrayList<>();
        if(args.length == 0){
            list.add("invite");
            list.add("help");
            list.add("link");
            list.add("send");
        }
        return null;
    }

    private void msg(CommandSender s, String m){
        String a = ChatColor.translateAlternateColorCodes('&', "&a[KOOKBot]&f " + m);
        s.sendMessage(a);
    }
}
