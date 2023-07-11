package ml.littlekan.kookbot.bot;

public class ResponseException extends Exception{
    public ResponseException(int code){
        super("服务器返回了错误代码：" + new StringBuilder(code).toString());
    }
    /** 没啥用.jpg
     * public ResponseException(Throwable e){
     *     super(e);
     * }
     * public ResponseException(int code, Throwable e){
     *     super("服务器返回了错误代码：" + new StringBuilder(code).toString(), e);
     * }
     * */
}
