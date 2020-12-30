package wang.angi.sample.async.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author angi
 */
@WebServlet(name = "asyncServletExecutorService", urlPatterns = "/asyncServletExecutorService", asyncSupported = true)
public class AsyncServletExecutorService extends HttpServlet {

    Logger logger = LoggerFactory.getLogger(AsyncServletExecutorService.class);

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("doGet begin");

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (Exception e) {
            e.printStackTrace();
        }

        final AsyncContext asyncContext = req.startAsync();
        asyncContext.setTimeout(30 * 1000);
        // 另开worker线程进行回调处理
        asyncContext.addListener(new AsyncListener() {
            public void onComplete(AsyncEvent event) throws IOException {
                logger.info("onComplete begin");
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                logger.info("onComplete end");
            }

            public void onTimeout(AsyncEvent event) throws IOException {
                logger.info("onTimeout");
            }

            public void onError(AsyncEvent event) throws IOException {
                logger.info("onError");
            }

            public void onStartAsync(AsyncEvent event) throws IOException {
                logger.info("onStartAsync");
            }
        });
        // 另开业务线程进行业务处理
        executorService.execute(new Runnable() {
            public void run() {
                logger.info("biz begin");
                PrintWriter printWriter = null;
                try {
                    TimeUnit.SECONDS.sleep(20);
                    printWriter = asyncContext.getResponse().getWriter();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                printWriter.println("Hello, AsyncServlet.");
                printWriter.flush();
                printWriter.close();
                asyncContext.complete();
                logger.info("biz end");
            }
        });
        logger.info("doGet end");
    }
}
