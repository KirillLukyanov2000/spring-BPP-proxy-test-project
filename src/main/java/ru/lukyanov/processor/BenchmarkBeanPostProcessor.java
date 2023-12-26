package ru.lukyanov.processor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;




@Component
public class BenchmarkBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        if (bean.getClass().isAnnotationPresent(Benchmark.class) || isMethodAnnotated(bean.getClass())) {
            System.out.printf("<<< before init %s <<< Benchmark started %n", beanName);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(Benchmark.class) || isMethodAnnotated(bean.getClass())) {
        System.out.printf(">>> after init %s%n", beanName);}
        return bean;
    }


    public boolean isMethodAnnotated(Class<?> aClass) {
      return Arrays.stream(aClass.getMethods()).anyMatch(m->m.isAnnotationPresent(Benchmark.class));
    }
}


