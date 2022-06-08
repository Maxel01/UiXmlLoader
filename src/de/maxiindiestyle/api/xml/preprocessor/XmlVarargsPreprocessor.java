package de.maxiindiestyle.api.xml.preprocessor;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Method;
import com.badlogic.gdx.utils.reflect.ReflectionException;

import java.lang.annotation.*;
import java.lang.reflect.InvocationTargetException;

public class XmlVarargsPreprocessor extends XmlPreprocessor {

    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface Varargs {
    }

    @Override
    public String preprocess(String xml, FileHandle fileHandle) {
        for (int index = xml.indexOf("@"); index != -1; index = xml.indexOf("@", index + 1)) {
            try {
                xml = executeVararg(xml, index);
            } catch (ReflectionException | InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return xml;
    }

    private String executeVararg(String xml, int index) throws ReflectionException, InvocationTargetException, IllegalAccessException {
        String varargsTxt = xml.substring(index, xml.indexOf("\\>", index) + 2);
        String classTxt = varargsTxt.substring(1, varargsTxt.length() - 2);
        Class<?> clazz = ClassReflection.forName(classTxt);
        String replaceTxt = "";
        for (Method method : ClassReflection.getDeclaredMethods(clazz)) {
            if (method.isAnnotationPresent(Varargs.class)) {
                replaceTxt = method.invoke(null).toString();
                break;
            }
        }
        if (replaceTxt.isEmpty()) {
            throw new IllegalArgumentException("Varargs for '" + varargsTxt + "' not found");
        }
        xml = xml.replace(varargsTxt, replaceTxt);
        return xml;
    }
}
