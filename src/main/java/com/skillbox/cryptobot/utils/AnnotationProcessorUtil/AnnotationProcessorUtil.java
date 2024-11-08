package com.skillbox.cryptobot.utils.AnnotationProcessorUtil;

import com.skillbox.cryptobot.annotations.ImmutableDataMapper;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.util.Set;

@SupportedAnnotationTypes("com.skillbox.cryptobot.annotations.ImmutableDataMapper")
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class AnnotationProcessorUtil extends AbstractProcessor {

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    for (Element element : roundEnv.getElementsAnnotatedWith(ImmutableDataMapper.class)) {
      String fieldName = element.getSimpleName().toString();
      String className =
          ((TypeElement) element.getEnclosingElement()).getQualifiedName().toString();
      String capitalizedFieldName =
          fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

      // Generate getter method
      MethodSpec getter =
          MethodSpec.methodBuilder("get" + capitalizedFieldName)
              .addModifiers(Modifier.PUBLIC)
              .returns(element.getClass()) // Adjust the return type based on the field type
              .addStatement("return this.$N", fieldName)
              .build();

      // Generate setter method
      MethodSpec setter =
          MethodSpec.methodBuilder("set" + capitalizedFieldName)
              .addModifiers(Modifier.PUBLIC)
              .addParameter(
                  element.getClass(),
                  fieldName) // Adjust the parameter type based on the field type
              .addStatement("this.$N = $N", fieldName, fieldName)
              .build();

      // Create a new class with the generated methods
      TypeSpec accessorClass =
          TypeSpec.classBuilder(className + "Accessors")
              .addModifiers(Modifier.PUBLIC)
              .addMethod(getter)
              .addMethod(setter)
              .build();

      // Write the generated class to a file
      JavaFile javaFile =
          JavaFile.builder("com.skillbox.cryptobot.generated", accessorClass).build();

      try {
        javaFile.writeTo(processingEnv.getFiler());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return true;
  }
}
