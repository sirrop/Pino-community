package com.branc.pino.core.annotaion;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.*;
/**
 * Represents the class, interface, enum, annotation, field, method and constructor that might be changed in the future or is implemented experimentally.
 */
@Retention(RetentionPolicy.SOURCE)
@Target({TYPE, ANNOTATION_TYPE, FIELD, METHOD, CONSTRUCTOR})
public @interface Beta {
}
