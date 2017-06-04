package info.doula.security.config;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        final HttpServletResponse response = (HttpServletResponse) res;
        Map<String, Object> headerKeys = new HashMap<>();
        headerKeys.put("Access-Control-Allow-Origin", "*");
        headerKeys.put("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
        headerKeys.put("Access-Control-Allow-Headers", "Authorization, Content-Type");
        headerKeys.put("Access-Control-Max-Age", "3600");
        setHeaders(response, headerKeys);

        if ("OPTIONS".equalsIgnoreCase(((HttpServletRequest) req).getMethod()))
            response.setStatus(HttpServletResponse.SC_OK);
        else
            chain.doFilter(req, res);
    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    private void setHeaders(HttpServletResponse response, Map<String,Object> headerKeys){
        for(Map.Entry headerKey : headerKeys.entrySet())
            response.setHeader(headerKey.getKey().toString(), headerKey.getValue().toString());
    }
}
