package jwormbench.config.params;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

import com.google.inject.BindingAnnotation;


@BindingAnnotation
@Target({ElementType.PARAMETER}) 
@Retention(RetentionPolicy.RUNTIME)
public @interface WorldConfigFile {

}
