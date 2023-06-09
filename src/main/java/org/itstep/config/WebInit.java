package org.itstep.config;
import jakarta.servlet.*;
import org.h2.server.web.JakartaWebServlet;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

import java.util.EnumSet;

public class WebInit implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        var appContext = new AnnotationConfigWebApplicationContext();
        // context-param
        /*
        <context-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>/WEB-INF/config/application.xml</param-value>
        </context-param>
         */
        appContext.register(WebConfig.class);
        appContext.setServletContext(servletContext);
        // ContextLoaderListener
        /*
        <listener>
            <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
        </listener>
         */
        servletContext.addListener(new ContextLoaderListener(appContext));
        // DispatcherServlet
        /*
        <servlet>
            <servlet-name>dispatcherServlet</servlet-name>
            <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
            <init-param>
                <param-name>contextConfigLocation</param-name>
                <param-value>/WEB-INF/config/application.xml</param-value>
            </init-param>
        </servlet>
          <servlet-mapping>
            <servlet-name>dispatcherServlet</servlet-name>
            <url-pattern>/</url-pattern>
        </servlet-mapping>
         */
        var dispatcherServletRegistration = servletContext.addServlet("dispatcherServlet",
                new DispatcherServlet(new GenericWebApplicationContext()));
        dispatcherServletRegistration.addMapping("/");
        dispatcherServletRegistration.setLoadOnStartup(1);
        // encodingFilter
        /*
        <filter>
            <filter-name>encodingFilter</filter-name>
            <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
            <init-param>
                <param-name>encoding</param-name>
                <param-value>UTF-8</param-value>
            </init-param>
            <init-param>
                <param-name>forceRequestEncoding</param-name>
                <param-value>true</param-value>
            </init-param>
            <init-param>
                <param-name>forceResponseEncoding</param-name>
                <param-value>true</param-value>
            </init-param>
        </filter>
          <filter-mapping>
            <filter-name>encodingFilter</filter-name>
            <url-pattern>/*</url-pattern>
        </filter-mapping>
         */
        CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
        encodingFilter.setEncoding("UTF-8");
        encodingFilter.setForceEncoding(true);
        var encodingFilterRegistration = servletContext.addFilter("encodingFilter", encodingFilter);
        encodingFilterRegistration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
        // h2-console Servlet
        var h2ConsoleRegistration = servletContext.addServlet("h2-console", JakartaWebServlet.class);
        h2ConsoleRegistration.addMapping("/h2-console/*");
    }
}
