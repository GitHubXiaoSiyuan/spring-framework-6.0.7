package cn.xiaosy.xspring.service;

import cn.xiaosy.xspring.spring.BeanPostProcessor;
import cn.xiaosy.xspring.spring.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Component
public class XiaosyPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessorBeforeInitialization(String beanName, Object bean) {
        // 初始化前处理逻辑
        if ("userService".equals(beanName)) {
            System.out.println("判断出当前bean是 userService，并执行 userService 的初始化前处理逻辑");
        }

        return bean;
    }

    @Override
    public Object postProcessorAfterInitialization(String beanName, Object bean) {
        // 初始化后处理逻辑
        if ("userService".equals(beanName)) {
            System.out.println("判断出当前bean是 userService，并执行 userService 的初始化后处理逻辑");
        }

        if ("userService".equals(beanName)) {
            Object proxyInstance = Proxy.newProxyInstance(XiaosyPostProcessor.class.getClassLoader(), bean.getClass().getInterfaces(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                    System.out.println("切面逻辑");

                    // 执行的是原来的 bean 的逻辑，不是代理对象的逻辑
                    return method.invoke(bean,args);
                }
            });

            return proxyInstance;
        }

        return bean;
    }

}
