package tracing;

import api.ApiConfiguration;
import com.github.kristofa.brave.Brave;
import org.apache.commons.io.IOUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.StringJoiner;


/**
 * Custom tracing interceptor
 *
 * @author m.mazigh
 */
public class MyServletHandlerInterceptor extends HandlerInterceptorAdapter {

    Brave brave() {
        return ApiConfiguration.instance().getBrave();
    }


    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response,
                             final Object handler) throws Exception {
        traceHeader(request);
        tracePayload(request);
        return super.preHandle(request, response, handler);
    }

    private void traceHeader(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        StringJoiner stringJoiner = new StringJoiner(" <br> ", "", "");
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            stringJoiner.add(headerName + ":" + headerValue);
        }
        brave().serverTracer().submitBinaryAnnotation("headers", stringJoiner.toString());
    }

    private void tracePayload(HttpServletRequest request) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

        String str_request = IOUtils.toString(request.getInputStream(), "UTF-8");

        if (!StringUtils.isEmpty(str_request)) {
            stringBuilder.append(str_request);
        } else {
            stringBuilder.append("");
        }
        String payload = stringBuilder.toString();
        if (!payload.contains("password")) {
            brave().serverTracer().submitBinaryAnnotation("payload", payload);
        }
    }

}
