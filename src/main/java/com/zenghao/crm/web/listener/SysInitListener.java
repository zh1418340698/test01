package com.zenghao.crm.web.listener;

import com.zenghao.crm.settings.domain.DicValue;
import com.zenghao.crm.settings.service.DicService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

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
        }


        Map<String,String> pMap = new HashMap<>();
        ResourceBundle rb = ResourceBundle.getBundle("stage2Possibility");

        Enumeration<String> e = rb.getKeys();

        while (e.hasMoreElements()){

            //阶段
            String key = e.nextElement();
            //可能性
            String value = rb.getString(key);

            pMap.put(key,value);
        }

        //将pMap保存到浏览器缓存中
        application.setAttribute("pMap",pMap);

    }


}
