package ml.littlekan.kookbot.bot;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import java.sql.*;
import java.io.File;
import java.lang.Class;
import ml.littlekan.kookbot.ErrorOut;

public class SQL {
    private JavaPlugin instance;
    private Connection sqlconn;
    private Statement sql;

    public SQL(JavaPlugin i){
        instance = i;
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            new ErrorOut(new RuntimeException("KOOKBot插件将因为 SQLite 数据库初始化错误禁用！", e));
            Bukkit.getPluginManager().disablePlugin(instance);
        }
        String path = new File(instance.getDataFolder(), "kookbot.db").getPath();
        try {
            sqlconn = DriverManager.getConnection("jdbc:sqlite:" + path);
            sql = sqlconn.createStatement();
            String exec = "create table if not exists kookbot_session ( token text primary key not null, wsurl text not null, session_id text not null, sn int not null );";
            String exec2 = "create table if not exists kookbot_link ( mcname text primary key not null, kookid text not null );";
            String exec3 = "create table if not exists kookbot_link_req ( mcid text primary key not null, reqid text not null );";
            sql.executeUpdate(exec);
            sql.executeUpdate(exec2);
            sql.executeUpdate(exec3);
        } catch (SQLException e) {
            new ErrorOut(e);return;
        }
    }

    public void setSession(String token, String wsurl, String sid, int sn) throws SQLException{
        String exec = "insert into kookbot_session values ( '" + token +"', '" + wsurl + "', '" + sid + "', " + new StringBuilder(sn).toString() + " );";
        if (getSession(token) == null) sql.executeUpdate(exec);
    }

    public void snUpdate(String token, int sn) throws SQLException{
        ResultSet rs = getSession(token);
        if(rs.getInt("sn") < sn){
            String exec = "update kookbot_session set sn=" + new StringBuilder(sn).toString() + " where token=\"" + token + "\";";
            sql.executeUpdate(exec);
        }
    }

    public ResultSet getSession(String token) throws SQLException{
        String exec = "select * from kookbot_session";
        ResultSet rs = sql.executeQuery(exec);
        while (rs.next()){
            if(rs.getString("token") == token) return rs;
        }
        return null;
    }

    public void setLink(String mcuser, String kookid) throws SQLException{
        String exec = "insert into kookbot_link values ( '" + mcuser + "', '" + kookid + "' )";
        if (getLink(mcuser) == null) sql.executeUpdate(exec);
    }

    public ResultSet getLink(String mcuser) throws SQLException{
        String exec = "select * from kookbot_link";
        ResultSet rs = sql.executeQuery(exec);
        while (rs.next()){
            if(rs.getString("mcname") == mcuser) return rs;
        }
        return null;
    }

    public void createLinkRequest(String mcname, String verifycode){
        //
    }

    public void close(){
        try {
            sql.close();
            sqlconn.close();
        } catch (SQLException e) {
            new ErrorOut(e);
        }
    }
}
