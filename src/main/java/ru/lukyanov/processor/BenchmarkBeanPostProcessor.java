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

    private Map<String, Class<?>> map = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        Class<?> aClass = bean.getClass();
        if (bean.getClass().isAnnotationPresent(Benchmark.class) || isMethodAnnotated(bean.getClass())) {
            map.put(beanName, aClass);
            System.out.printf("<<< before init %s <<< Benchmark started %n", beanName);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (map.get(beanName) != null) {

            bean = proxy(bean);

            System.out.printf(">>> after init %s%n", beanName);
        }
        return bean;
    }

    private Object proxy(Object bean) {

        MethodInterceptor handler = (obj, method, args, proxy) -> {
            Object result;
            if (bean.getClass().isAnnotationPresent(Benchmark.class) || method.isAnnotationPresent(Benchmark.class)) {
                System.out.printf("==== Method %s started ====%n", method.getName());
                long time = System.nanoTime();
                result = proxy.invoke(bean, args);
                time = System.nanoTime() - time;
                System.out.printf("==== Method %s complete. Time = %d ms ==== %n%n", method.getName(), TimeUnit.NANOSECONDS.toMillis(time));
            } else {
                result = proxy.invoke(bean, args);
            }
            return result;
        };

        Object proxy = Enhancer.create(bean.getClass(), handler);
        System.out.println("here proxy for " + bean);
        return proxy;
    }

    public boolean isMethodAnnotated(Class<?> aClass) {
        return Arrays.stream(aClass.getMethods()).anyMatch(m -> m.isAnnotationPresent(Benchmark.class));
    }

}


