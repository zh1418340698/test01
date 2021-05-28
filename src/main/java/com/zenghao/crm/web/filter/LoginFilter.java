package com.zenghao.crm.web.filter;

import com.zenghao.crm.settings.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //前后端的访问路径都能拿到
        String path = request.getServletPath();

        //不应该被拦截的资源，自动放行请求
        if ( "/login.jsp".equals(path) || "/login.do".equals(path)){

            //继续执行下一步（就是这个过滤器结束）
            filterChain.doFilter(servletRequest,servletResponse);

        //其他资源必须验证有没有登陆过
        }else {
            User user = (User) request.getSession().getAttribute("user");

            //如果user不为null，说明登录过
            if ( user != null){

                //继续执行下一步
                filterChain.doFilter(servletRequest,servletResponse);

                //没有登录过
            }else {

            /*
                重定向使用的是传统路径的使用方式，前面必须以/项目名开头，后面跟具体的资源路径
                /crm/login.jsp

                为什么使用重定向？
                    使用转发之后，路径会停留在老路径上，而不是跳转到最新的资源路径上
                    我们应该为用户跳转到登录页的同时，将浏览器的地址栏自动设置为当前的登录页的路径
             */
                response.sendRedirect(request.getContextPath()+"/login.jsp");
            }
        }


    }
}
