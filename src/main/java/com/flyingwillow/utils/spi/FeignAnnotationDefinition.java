package com.flyingwillow.utils.spi;

import java.lang.annotation.Annotation;
import java.util.List;

public interface FeignAnnotationDefinition {

    /**
     * 获取自定义 Feign 注解
     * */
    List<Class<? extends Annotation>> getAnnotations();
}
