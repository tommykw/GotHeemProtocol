package com.github.tommykw.compiler;

import com.github.tommykw.library.Generatable;

import java.io.IOException;
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
}
