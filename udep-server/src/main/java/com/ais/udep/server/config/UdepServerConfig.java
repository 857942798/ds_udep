//package com.ais.udep.server.config;
//
//import com.ais.udep.server.netty.server.UdedServer;
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.BeanClassLoaderAware;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.beans.factory.config.BeanDefinition;
//import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
//import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
//import org.springframework.beans.factory.support.BeanDefinitionBuilder;
//import org.springframework.beans.factory.support.DefaultListableBeanFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//
///**
// * @author: dongsheng
// * @CreateTime: 2022/3/8
// * @Description:
// */
//public class UdepServerConfig implements BeanFactoryPostProcessor, BeanClassLoaderAware {
//    @Value("${config.udep.server.port}")
//    private int port;
//
//    @Override
//    public void setBeanClassLoader(ClassLoader classLoader) {
//        System.out.println(1);
//    }
//
//    @Override
//    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
//        System.out.println(2);
//    }
//
//
////    @Override
////    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
////        DefaultListableBeanFactory defaultListableBeanFactory
////                = (DefaultListableBeanFactory) beanFactory;
////
////        //注册Bean定义，容器根据定义返回bean
////        BeanDefinitionBuilder beanDefinitionBuilder =
////                BeanDefinitionBuilder.genericBeanDefinition(UdedServer.class);
//////        beanDefinitionBuilder.addPropertyReference("personDao", "personDao");
////        BeanDefinition personManagerBeanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
////        defaultListableBeanFactory.registerBeanDefinition("udedServer", personManagerBeanDefinition);
////
////        //注册bean实例
////        UdedServer personDao = beanFactory.getBean(UdedServer.class);
////        UdedServer personManager = new UdedServer();
////        beanFactory.registerSingleton("personManager2", personManager);
////    }
//}
