package servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "asyncServlet", urlPatterns = "/asyncServlet", asyncSupported = true)
public class AsyncServlet extends HttpServlet {

	Logger logger = LoggerFactory.getLogger(AsyncServlet.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		logger.info("receive a AsyncServlet request.");
		final AsyncContext asyncContext = req.startAsync();
		asyncContext.setTimeout(30 * 1000);
		asyncContext.addListener(new AsyncListener() {
			public void onComplete(AsyncEvent event) throws IOException {
				logger.info("AsyncServlet complete.");
			}

			public void onTimeout(AsyncEvent event) throws IOException {
				logger.info("AsyncServlet timeout.");
			}

			public void onError(AsyncEvent event) throws IOException {
				logger.info("AsyncServlet error.");
			}

			public void onStartAsync(AsyncEvent event) throws IOException {
				logger.info("AsyncServlet Start.");
			}
		});
		asyncContext.start(new Runnable() {
			public void run() {
				logger.info("begin biz");
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
				logger.info("end biz");
			}
		});
	}
}
