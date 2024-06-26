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

package com.blazebit.domain.declarative.impl.spi;

import com.blazebit.domain.declarative.DomainFunction;
import com.blazebit.domain.runtime.model.DomainFunctionTypeResolver;
import com.blazebit.domain.runtime.model.DomainFunctionVolatility;

import java.lang.annotation.Annotation;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class DomainFunctionLiteral implements DomainFunction {

    @Override
    public String value() {
        return "";
    }

    @Override
    public Class<?> type() {
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
    public Class<? extends DomainFunctionTypeResolver> typeResolver() {
        return DomainFunctionTypeResolver.class;
    }

    @Override
    public int minArguments() {
        return -1;
    }

    @Override
    public DomainFunctionVolatility volatility() {
        return DomainFunctionVolatility.IMMUTABLE;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return DomainFunction.class;
    }
}
