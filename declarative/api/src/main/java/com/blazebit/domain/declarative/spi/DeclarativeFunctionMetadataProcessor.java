/*
 * Copyright 2019 - 2024 Blazebit.
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

package com.blazebit.domain.declarative.spi;

import com.blazebit.domain.boot.model.MetadataDefinition;
import com.blazebit.domain.spi.ServiceProvider;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * A declarative metadata processor for domain functions.
 *
 * @param <T> The annotation type
 * @author Christian Beikov
 * @since 1.0.0
 */
public interface DeclarativeFunctionMetadataProcessor<T extends Annotation> {

    /**
     * The annotation type that is processed.
     *
     * @return the annotation type
     */
    public Class<T> getProcessingAnnotation();

    /**
     * Processes the annotation of the given annotated class and method and produces a metadata definition.
     *
     * @param annotatedClass The annotated class
     * @param method The method
     * @param annotation The annotation
     * @param serviceProvider The service provider
     * @return A metadata definition or <code>null</code>
     */
    @Deprecated
    public MetadataDefinition<?> process(Class<?> annotatedClass, Method method, T annotation, ServiceProvider serviceProvider);

    /**
     * Processes the annotation of the given annotated class and method and produces a metadata definition.
     *
     * @param annotatedClass The annotated class
     * @param method The method
     * @param annotation The annotation
     * @param name The name of the function that is processed
     * @param typeName The resolved result type name of the function that is processed
     * @param collection Whether the resolved result type of the function that is processed is a collection
     * @param serviceProvider The service provider
     * @return A metadata definition or <code>null</code>
     * @since 2.0.3
     */
    public default MetadataDefinition<?> process(Class<?> annotatedClass, Method method, T annotation, String name, String typeName, boolean collection, ServiceProvider serviceProvider) {
        return process(annotatedClass, method, annotation, serviceProvider);
    }

}
