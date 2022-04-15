package ru.itis.chat.initializer;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import ru.itis.chat.configuration.ApplicationConfig;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

public class ApplicationInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) {
        // поднимаем конфигурацию контекста Spring для WebMvc приложений
        AnnotationConfigWebApplicationContext springWebContext = new AnnotationConfigWebApplicationContext();

        // регистрируем в этом контексте наш конфигурационный бин
        springWebContext.register(ApplicationConfig.class);

        // добавить Listener, который есть в Spring в контекст сервлетов
        ContextLoaderListener listener = new ContextLoaderListener(springWebContext);
        servletContext.addListener(listener);

        // создать DispatcherServlet во время запуска приложения и зарегистрировать его
        ServletRegistration.Dynamic dispatcherServlet = servletContext.addServlet("dispatcher", new DispatcherServlet(springWebContext));
        dispatcherServlet.setLoadOnStartup(1);
        dispatcherServlet.addMapping("/");
    }
}
