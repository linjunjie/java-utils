
import java.io.BufferedReader;  
import java.io.IOException;
import java.io.InputStream;  
import java.io.InputStreamReader;  
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;  
import java.net.HttpURLConnection;  
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL; 
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
 
/** 
 * Http请求工具类 
 * 可通过proxy代理发起请求
 * @author junjie.lin 
 * @since 2016-1-14
 * @version v1.0.2
 */
public class HttpRequestUtil {
    static boolean proxySet = false;
    static String proxyHost = "116.213.214.28";
    static int proxyPort = 8080;
    /** 
     * 编码 
     * @param source 
     * @return 
     */ 
    public static String urlEncode(String source,String encode) {  
        String result = source;  
        try {  
            result = java.net.URLEncoder.encode(source,encode);  
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
            return "0";  
        }  
        return result;  
    }
    public static String urlEncodeGBK(String source) {  
        String result = source;  
        try {  
            result = java.net.URLEncoder.encode(source,"GBK");  
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
            return "0";  
        }  
        return result;  
    }
    /** 
     * 发起http请求获取返回结果 
     * @param req_url 请求地址 
     * @return 
     */ 
    public static String httpRequest(String req_url) {
        StringBuffer buffer = new StringBuffer();  
        try {  
            URL url = new URL(req_url);  
            HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();  
   
            httpUrlConn.setDoOutput(false);  
            httpUrlConn.setDoInput(true);  
            httpUrlConn.setUseCaches(false);  
   
            httpUrlConn.setRequestMethod("GET");  
            httpUrlConn.connect();  
   
            // 将返回的输入流转换成字符串  
            InputStream inputStream = httpUrlConn.getInputStream();  
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");  
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);  
   
            String str = null;  
            while ((str = bufferedReader.readLine()) != null) {  
                buffer.append(str);  
            }  
            bufferedReader.close();  
            inputStreamReader.close();  
            // 释放资源  
            inputStream.close();  
            inputStream = null;  
            httpUrlConn.disconnect();  
   
        } catch (Exception e) {  
            System.out.println(e.getStackTrace());  
        }  
        return buffer.toString();  
    }  
       
    /** 
     * 发送http请求取得返回的输入流 
     * @param requestUrl 请求地址 
     * @return InputStream 
     */ 
    public static InputStream httpRequestIO(String requestUrl) {  
        InputStream inputStream = null;  
        try {  
            URL url = new URL(requestUrl);  
            HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();  
            httpUrlConn.setDoInput(true);  
            httpUrlConn.setRequestMethod("GET");  
            httpUrlConn.connect();  
            // 获得返回的输入流  
            inputStream = httpUrlConn.getInputStream();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return inputStream;  
    }
     
     
    /**
     * 向指定URL发送GET方法的请求
     * 
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }
 
    /**
     * 向指定 URL 发送POST方法的请求
     * 
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @param isproxy
     *               是否使用代理模式
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param,boolean isproxy) {
        OutputStreamWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            HttpURLConnection conn = null;
            if(isproxy){//使用代理模式
                @SuppressWarnings("static-access")
                Proxy proxy = new Proxy(Proxy.Type.DIRECT.HTTP, new InetSocketAddress(proxyHost, proxyPort));
                conn = (HttpURLConnection) realUrl.openConnection(proxy);
            }else{
                conn = (HttpURLConnection) realUrl.openConnection();
            }
            // 打开和URL之间的连接
             
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");    // POST方法
             

            // 设置通用的请求属性
             
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // 如果有header签名可以在这里设置
            conn.setRequestProperty("signature","YourEncrypedSignString");

            // 通过代理访问
            String headerKey = "Proxy-Authorization";
            String headerValue = "Basic YourProxyName:YourProxyPassword";
            conn.setRequestProperty(headerKey, headerValue);

            if(true){
                // POST 参数传递方法一
                String body = "eyJvcmRlciI6eyJjb21wYW55Q29kZSI6InRpYW5hbiIsIm9yZGVyTm8iOiIxNjAxMTMxNjUwMzk0NDc5OTM1Iiwib3JkZXJUaW1lIjoiMjAxNi0wMS0xMyAxNjo1MDo0MCIsInBheUVuZERhdGUiOiIyMDE2LTAxLTE4IDE2OjUwOjQwIiwicGF5U3RhdHVzIjoiMCIsInBheVVSTCI6Imh0dHA6Ly93d3cuOTU1MDUuY29tLmNuL29ubGluZS90aGlyZFBhcnRlclBheW1lbnQvdG9QYXkuZG8/Y2hhbm5lbD0wOSZzb3VyY2U9MiZhcmVhRmxhZz0wJm9yZGVyTm89MTYwMTEzMTY1MDM5NDQ3OTkzNSZvcmRlckFtb3VudD02NTM1MjEmb3JkZXJUaW1lPTIwMTYwMTEzMTY1MDQ3JnBhZ2VVcmw9aHR0cDovL3d3dy53eGIuY29tLmNuLyZtZXNnVXJsPWh0dHA6Ly9jYWxsYmFjay53eGIuY29tLmNuL2luc3VyYW5jZS9jYWxsYmFjay90aWFuYW4vcGF5U3RhdHVzJmNhckZsYWc9MSZ0cmFuc0NvZGU9JnByb3Bvc2FsTm89MDY0MzIzMDEzNjIyMDE2MDAwMTMwNiZjaXR5Q29kZT0zMzMwNDAwJnBheWVlTmFtZT3lvKDkuIkiLCJ0b3RhbFByZW1pdW0iOjY1MzUyMSwibXVzdFBlcmNlbnQiOiIyLjAiLCJiaXpQZXJjZW50IjoiMTEuMCJ9LCJraW5kIjpbeyJraW5kQ29kZSI6IjEiLCJraW5kUHJlbWl1bSI6NTExNTIxLCJpdGVtIjpbeyJpdGVtQ29kZSI6MSwiaXRlbVByZW1pdW0iOjMzODAwMiwiY2xhaW1zUHJlbWl1bSI6MzY0NDEwMDB9LHsiaXRlbUNvZGUiOjIsIml0ZW1QcmVtaXVtIjoxMDY3OTksImNsYWltc1ByZW1pdW0iOjUwMDAwMDAwfSx7Iml0ZW1Db2RlIjoxMCwiaXRlbVByZW1pdW0iOjUwNzAwLCJjbGFpbXNQcmVtaXVtIjowfSx7Iml0ZW1Db2RlIjoxMSwiaXRlbVByZW1pdW0iOjE2MDIwLCJjbGFpbXNQcmVtaXVtIjowfV19LHsia2luZENvZGUiOiIyIiwia2luZFByZW1pdW0iOjE0MjAwMCwiaXRlbSI6W3siaXRlbUNvZGUiOjE2LCJpdGVtUHJlbWl1bSI6NzYwMDAsImNsYWltc1ByZW1pdW0iOjEyMjAwMDAwfSx7Iml0ZW1Db2RlIjoxNywiaXRlbVByZW1pdW0iOjY2MDAwLCJjbGFpbXNQcmVtaXVtIjo2NjAwMH1dfV0sInBlcnNvbiI6eyJjYXJkTm8iOiIzMzAxMDUxOTkwMDMqKioqKioiLCJjYXJkVHlwZSI6IjEiLCJuYW1lIjoi5byg5LiJIiwicGVyc29uVHlwZSI6MiwicGhvbmUiOiIxNTc1NzExNTg1MCJ9LCJjYXIiOnsiY2FyTm8iOiLmtZlGMDcxNU0iLCJtb2RlbCI6IkxFWFVTIEVTMzAwaCJ9fQ==";
                conn.setRequestProperty("Content-Length", Integer.toString(body.length()));
                conn.getOutputStream().write(body.getBytes("UTF8"));   
            }else{
                // POST 参数传递方法二
                // 获取URLConnection对象对应的输出流
                out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
                // 发送请求参数
                out.write(param);
                // flush输出流的缓冲
                out.flush();
            }

            conn.connect();

            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }    
     
    public static void main(String[] args) {
        //demo:代理访问
        String url = "http://api.adf.ly/api.php";
        String para = "key=youkeyid&youuid=uid&advert_type=int&domain=adf.ly&url=http://somewebsite.com";
        
        String rs=HttpRequestUtil.sendPost(url,para,true);
        System.out.println(rs);
    }
     
}