package src.interceptors;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Enumeration;

@Slf4j
public class LoggerInterceptor implements HandlerInterceptor {
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("[PreHangle][" + request + "]" + "[" + request.getMethod() + "]" + request.getRequestURI()
                + "(" +  getParameters(request) + ")");
        return true;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        log.info("[postHandle][" + request + "]");
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        if (ex != null){
            ex.printStackTrace();
        }
        log.info("[afterCompletion][" + request + "][exception: " + ex + "]");
    }



    private String getParameters(HttpServletRequest request){
        StringBuffer sb = new StringBuffer();
        Enumeration<?> e = request.getParameterNames();
        if (e == null){
            sb.append("?");
        }

        while (e.hasMoreElements()){
            if (sb.length() > 1){
                sb.append("&");
            }
            String cur = (String) e.nextElement();
            sb.append(cur + "= ");
            if (cur.contains("password") || cur.contains("pwd") || cur.contains("pass")){
                sb.append("***");
            } else {
                sb.append(request.getParameter(cur));
            }
        }
        String ip = request.getHeader("X-FORWARDED-FOR");
        String ipAddr = (ip == null) ? getRemoteAddr(request) : ip;
        if (ipAddr!=null && !ipAddr.equals("")) {
            sb.append("&_psip=" + ipAddr);
        }
        return sb.toString();
    }

    private String getRemoteAddr(HttpServletRequest request) {
        String ipFromHeader = request.getHeader("X-FORWARDED-FOR");
        if (ipFromHeader != null && ipFromHeader.length() > 0) {
            log.debug("ip from proxy - X-FORWARDED-FOR : " + ipFromHeader);
            return ipFromHeader;
        }
        return request.getRemoteAddr();
    }
}