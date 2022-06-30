package com.bitsmuggler.learning.springbootaspect.aspects;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FileProperty {
    String[] contentTypes();
}
