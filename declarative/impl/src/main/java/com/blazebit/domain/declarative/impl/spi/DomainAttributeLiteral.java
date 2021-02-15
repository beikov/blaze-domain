/*
 * Copyright 2019 - 2021 Blazebit.
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

package com.blazebit.domain.declarative.impl.spi;

import com.blazebit.domain.declarative.DomainAttribute;

import java.lang.annotation.Annotation;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class DomainAttributeLiteral implements DomainAttribute {

    @Override
    public Class<?> value() {
        return void.class;
    }

    @Override
    public String typeName() {
        return "";
    }

    @Override
    public boolean collection() {
        return false;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return DomainAttribute.class;
    }
}
