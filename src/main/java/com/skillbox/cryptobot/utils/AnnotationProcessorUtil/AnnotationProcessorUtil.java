package com.skillbox.cryptobot.utils.AnnotationProcessorUtil;

import com.skillbox.cryptobot.annotations.ImmutableGetter;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

@SupportedAnnotationTypes("com.skillbox.cryptobot.annotations.ImmutableGetter")
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class AnnotationProcessorUtil extends AbstractProcessor {
    public static void processAnnotations(Object obj) {
    Class<?> clazz = obj.getClass();
    for (Field field : clazz.getDeclaredFields()) {
        if (field.isAnnotationPresent(ImmutableGetter.class)) {
            field.setAccessible(true);
            try {
                Object value = field.get(obj);
                if (value instanceof Collection<?>) {
                    Collection<?> immutableCollection = Collections.unmodifiableCollection((Collection<?>) value);
                    field.set(obj, immutableCollection);
                }
                if (value instanceof String){
                    String copyOfString = String.copyValueOf(((String) value).toCharArray());
                    field.set(obj, copyOfString);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        return false;
    }
}
