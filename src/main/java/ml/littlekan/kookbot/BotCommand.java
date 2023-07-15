package ml.littlekan.kookbot;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BotCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length <= 0) {
            msg(sender, "&aKOOKBot插件正在运行 版本1.0.0-SNAPSHOT >w<");
            msg(sender, "&a输入 &6/kook help &a获取帮助！");
        }else{
            switch (args[0]) {
                case "bind":
                    if(sender instanceof Player){
                        KOOKBot.getBindManager().addBindRequest((Player) sender);
                    }else{
                        msg(sender, "&c只有玩家可以使用哦 ㄟ( ▔, ▔ )ㄏ");
                    }
                case "help":
                    msg(sender, "&7----- &a[KOOKBot]&7 -----");
                    msg(sender, "&6/kook bind &7绑定账户");
                    msg(sender, "&6/kook help &7-- &a显示此帮助");
                    msg(sender, "&6/kook invite &7-- &a获取本服务器的KOOK邀请链接");
                    msg(sender, "&6/kook reload &7-- &a重载插件");
                    msg(sender, "&6/kook reset &7-- &a将插件重置");
                    msg(sender, "&6/kook send <message> &7-- &a发送信息到本服务器的KOOK服务器");
                    break;
                case "invite":
                    String url = KOOKBot.getPluginConfig().getString("invite-url");
                    msg(sender, "&b此服务器的KOOK链接：&6&n" + url);
                    break;
                case "reload":
                    if(sender.hasPermission("kookbot.admin")){
                        JavaPlugin instance = JavaPlugin.getPlugin(KOOKBot.class);
                        instance.reloadConfig();
                        KOOKBot.updateConfig();
                        msg(sender, "&a重载成功！有些配置可能需要重启服务器");
                    }else{
                        sendNoPermission(sender);
                    }
                    break;
                case "reset":
                    if(sender.hasPermission("kookbot.admin")){
                        msg(sender, "&c你确定重置插件吗？");
                        msg(sender, "&c&l此操作不可撤销，并且要从头来重新配置！！！");
                        msg(sender, "&c输入&6&l /kook resetconfirm &r&c以确认！");
                    }else{
                        sendNoPermission(sender);
                    }
                    break;
                case "resetconfirm":
                    if(sender.hasPermission("kookbot.admin")){
                        JavaPlugin instance = JavaPlugin.getPlugin(KOOKBot.class);
                        KOOKBot.silentKillDaemon();
                        instance.getDataFolder().delete();
                        msg(sender, "&c&o恢复成功&r，插件&n已禁用&r，&l重启服务器生效");
                        Bukkit.getPluginManager().disablePlugin(instance);
                    }else{
                        msg(sender, "&c无效的命令 T_T");
                    }
                    break;
                case "send":
                    if(sender instanceof Player){
                        sendMessageToKOOK((Player) sender, args);
                    }else{
                        msg(sender, "&c只有玩家可以使用哦 ㄟ( ▔, ▔ )ㄏ");
                    }
                default:
                    msg(sender, "&c无效的命令 T_T");
                    break;
            }
        }
        return true;
    }

    private void sendNoPermission(CommandSender s) {
        msg(s, "&c你没有权限执行此命令！");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args){
        List<String> list = new ArrayList<>();
        if(args.length == 0){
            list.add("bind");
            list.add("help");
            list.add("invite");
            list.add("send");
        }
        return null;
    }

    private void msg(CommandSender s, String m){
        String a = ChatColor.translateAlternateColorCodes('&', "&a[KOOKBot]&f " + m);
        s.sendMessage(a);
    }

    private void sendMessageToKOOK(Player p, String[] arg){
        ResultSet data;
        String msg, format, uid, playername;
        try {
            data = KOOKBot.getSQL().getLink(p.getName());
            if(data == null){
                msg(p, "&c您还未绑定KOOK账户！o_o ....");
                return;
            }
            msg = String.join(" ", arg).substring(5);
            format = KOOKBot.getPluginConfig().getString("chat_format.kook");
            uid = data.getString("kookid");
            playername = data.getString("mcname");
        } catch (SQLException e) {
            new ErrorOut(e);
            msg(p, "&c数据库出错啦！〒▽〒 请联系管理员解决！");
            return;
        }
        String displayname = "(met)" + uid +"(met)<" + playername + ">";
        format.replace("{display_name}", displayname);
        format.replace("{message}", msg);
        new KOOKMessageSender(format, "-1").send();
    }
}
