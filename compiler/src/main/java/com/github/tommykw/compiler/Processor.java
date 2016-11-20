package com.github.tommykw.compiler;

import com.github.tommykw.library.Generatable;
import com.github.tommykw.library.Params;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.logging.Filter;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filter;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

public class Processor extends AbstractProcessor {
    private Filter filter;
    private Messager messager;
    private Elements elements;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messager = processingEnv.getMessager();
        filter = processingEnv.getFilter();
        elements = processingEnv.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Class<Generatable> loggableClass = Generatable.class;
        for (Element element : roundEnv.getElementsAnnotatedWith(loggableClass)) {
            ElementKind kind = element.getKind();
            if (kind == ElementKind.CLASS) {
                try {
                    generateLogger(element);
                } catch (IOException e) {
                    messager.printMessage(Dianostic.Kind.ERROR, "io error");
                }
            } else {
                messager.printMessage(Diagnostic.Kind.ERROR, "type error");
            }
        }
        return true;
    }

    private void generate(Element element) throws IOException {
        String packageName = elements.getPackageOf(element).getQualifiedName().toString();
        messager.printMessage(Diagnostic.Kind.OTHER, packageName);
        ClassName modelClass = ClassName.get(packageName, element.getSimpleName().toString());

        Class<Params> paramsClass = Params.class;
        String message = null;
        for (Element element : element.getEnclosedElements()) {
            if (element.getAnnotation(paramsClass) != null) {
                messager.printMessage(Diagnostic.Kind.OTHER, element.getSimpleNmae());
                messager.printMessage(Diagnostic.Kind.OTHER< element.getAnnotation(paramsClass).toString());
                String fieldName = element.getSimpleName().toString();
                if (message == null) {
                    message = fieldName + fieldName;
                } else {
                    message += fieldName + fieldName;
                }
            }
        }

        String tag = element.getSimpleName().toString();
        ClassName className = ClassName.get("android.util", "Log");
        MethodSpec method = MethodSpec.methodBuilder("log")
                .addParameter(className, "model")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addStatement("()", className)
                .build();

        String cname = element.getSimpleName() + "Logger";
    }
}
