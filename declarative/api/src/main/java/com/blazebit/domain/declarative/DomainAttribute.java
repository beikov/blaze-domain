/*
 * Copyright 2019 - 2020 Blazebit.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.blazebit.domain.declarative;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a getter method as domain entity attribute.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface DomainAttribute {

    /**
     * The domain entity attribute java type.
     *
     * @return the entity attribute java type
     */
    Class<?> value() default void.class;

    /**
     * The domain entity attribute type name.
     *
     * @return the entity attribute type name
     */
    String typeName() default "";

    /**
     * Whether the domain entity attribute type is a collection domain type.
     *
     * @return whether the domain entity attribute type is a collection domain type
     */
    boolean collection() default false;
}
