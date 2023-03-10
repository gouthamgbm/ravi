package devops1.webapp;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class MessageFilter implements Filter {

    public void doFilter(ServletRequest req, ServletResponse res,
                         FilterChain chain)
    throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;

        // grab messages from the session and put them into request
        // this is so they're not lost in a redirect
        Object message = request.getSession().getAttribute("successMessages");

        if (!request.getRequestURI().startsWith("/app") && message != null) {
            request.setAttribute("successMessages", message);
            request.getSession().removeAttribute("successMessages");
        }

        // set the requestURL as a request attribute for templates
        // particularly freemarker, which doesn't allow request.getRequestURL()
        request.setAttribute("requestURL", request.getRequestURL());
        chain.doFilter(req, res);
    }

    public void init(FilterConfig filterConfig) {
    }

    public void destroy() {
    }
}
