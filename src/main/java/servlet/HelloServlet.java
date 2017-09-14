package servlet;/**
 * Created by Administrator on 2017/9/13.
 */

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * 扩展httpServlet
 * date:2017/9/13
 * time:16:08
 * author:LYH
 */
public class HelloServlet extends HttpServlet {

    private static ResourceBundle lStrings = ResourceBundle.getBundle("javax.servlet.http.LocalStrings");

    @Override
    public void init() throws ServletException {
        System.out.println("初始化servlet");
    }
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("执行get方法");
        String message = "hello world";
        // 设置响应内容类型
        response.setContentType("text/html");

        // 实际的逻辑是在这里
        PrintWriter out = response.getWriter();
        out.println("<h1>" + message + "</h1>");
        //super.doGet(req, resp);
    }

    private void maybeSetLastModified(HttpServletResponse resp, long lastModified) {
        if(!resp.containsHeader("Last-Modified")) {
            if(lastModified >= 0L) {
                resp.setDateHeader("Last-Modified", lastModified);
            }

        }
    }
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("--------------"+Thread.currentThread().getName());
        String method = req.getMethod();
        long lastModified;
        if(method.equals("GET")) {
            lastModified = this.getLastModified(req);
            if(lastModified == -1L) {
                this.doGet(req, resp);
            } else {
                long ifModifiedSince = req.getDateHeader("If-Modified-Since");
                if(ifModifiedSince < lastModified) {
                    this.maybeSetLastModified(resp, lastModified);
                    this.doGet(req, resp);
                } else {
                    resp.setStatus(304);
                }
            }
        } else if(method.equals("HEAD")) {
            lastModified = this.getLastModified(req);
            this.maybeSetLastModified(resp, lastModified);
            this.doHead(req, resp);
        } else if(method.equals("POST")) {
            this.doPost(req, resp);
        } else if(method.equals("PUT")) {
            this.doPut(req, resp);
        } else if(method.equals("DELETE")) {
            this.doDelete(req, resp);
        } else if(method.equals("OPTIONS")) {
            this.doOptions(req, resp);
        } else if(method.equals("TRACE")) {
            this.doTrace(req, resp);
        } else {
            String errMsg = lStrings.getString("http.method_not_implemented");
            Object[] errArgs = new Object[]{method};
            errMsg = MessageFormat.format(errMsg, errArgs);
            resp.sendError(501, errMsg);
        }
    }

    @Override
    public void destroy() {
        System.out.println("销毁servlet");
    }
}
