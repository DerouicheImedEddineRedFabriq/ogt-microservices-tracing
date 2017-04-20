//package tracing;
//
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.GenericFilterBean;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.xml.ws.ResponseWrapper;
//import java.io.IOException;
//
///**
// * Created by m.mazigh on 27/03/2017.
// */
//@Component
//public class ResponseWrappingFilter extends GenericFilterBean {
//
//    @Override
//    public void doFilter(
//            ServletRequest request,
//            ServletResponse response,
//            FilterChain chain) {
//
//        // Perform the rest of the chain, populating the response.
//        try {
//            chain.doFilter(request, response);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ServletException e) {
//            e.printStackTrace();
//        }
//
//        // No way to read the body from the response here. getBody() doesn't exist.
//        response.setBody(new ResponseWrapper(response.getStatus(), response.getBody());
//    }
//}