package com.glimsil.template.vertx.lib.persistence.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface DBTable {
  public String value() default "";
}