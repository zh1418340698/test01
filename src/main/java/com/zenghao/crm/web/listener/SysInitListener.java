package com.zenghao.crm.web.listener;

import com.zenghao.crm.settings.domain.DicValue;
import com.zenghao.crm.settings.service.DicService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SysInitListener implements ServletContextListener {


    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("全局作用域对象创建了");

        ApplicationContext atc = new ClassPathXmlApplicationContext("applicationContext.xml");
        DicService dicService = (DicService)atc.getBean("dicService");

        Map<String, List<DicValue>> map = dicService.getAll();
        ServletContext application = sce.getServletContext();

        Set<String> set = map.keySet();
        for (String key: set) {
            application.setAttribute(key,map.get(key));
            System.out.println("key=="+key+"value=="+map.get(key));
        }

    }


}
