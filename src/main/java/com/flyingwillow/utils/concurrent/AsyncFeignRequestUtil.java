package com.flyingwillow.utils.concurrent;

import com.flyingwillow.utils.spi.FeignAnnotationDefinition;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.cloud.openfeign.FeignClient;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class AsyncFeignRequestUtil {

    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private static final List<Class<? extends Annotation>> annotationList = new ArrayList<>(5);

    static {
        annotationList.add(FeignClient.class);
        ServiceLoader<FeignAnnotationDefinition> services = ServiceLoader.load(FeignAnnotationDefinition.class);
        services.forEach(s -> {
            final List<Class<? extends Annotation>> annotations = s.getAnnotations();
            if(CollectionUtils.isNotEmpty(annotations)){
                annotationList.addAll(annotations);
            }
        });
    }

    private AsyncFeignRequestUtil() {
    }

    public static <T> T asyncOf(T target) {

        if (Objects.isNull(target)) {
            throw new IllegalArgumentException("target can not be null");
        }

        final List<Class<?>> interfaces = Arrays.stream(target.getClass().getInterfaces())
                .filter(clz -> validate(clz))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(interfaces)) {
            throw new IllegalArgumentException("target is not a valid feign client");
        }

        Enhancer enhancer = new Enhancer();
        enhancer.setInterfaces(interfaces.toArray(new Class[]{}));
        enhancer.setCallback(new SmartInvocationHandler(target));

        return (T) enhancer.create();
    }

    private static boolean validate(Class<?> clz) {
        return annotationList.stream().filter(a -> clz.isAnnotationPresent(a)).findAny().isPresent();
    }

    public static class SmartInvocationHandler implements MethodInterceptor {

        private Object target;

        public SmartInvocationHandler(Object target) {
            this.target = target;
        }

        @Override
        public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
            final Class<?> returnType = method.getReturnType();
            final CompletableFuture<Object> future = CompletableFuture.supplyAsync(() -> {
                try {
                    return method.invoke(target, args);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }, executorService);

            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(returnType);
            enhancer.setCallback((MethodInterceptor) (o, method1, objects, mp) -> mp.invoke(future.get(), args));
            return enhancer.create();
        }
    }
}
