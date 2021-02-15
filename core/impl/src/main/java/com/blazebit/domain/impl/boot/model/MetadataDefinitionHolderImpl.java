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

package com.blazebit.domain.impl.boot.model;

import com.blazebit.domain.boot.model.MetadataDefinition;
import com.blazebit.domain.boot.model.MetadataDefinitionHolder;
import com.blazebit.domain.impl.runtime.model.RuntimeMetadataDefinition;
import com.blazebit.domain.runtime.model.MetadataHolder;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class MetadataDefinitionHolderImpl<X extends MetadataDefinitionHolder<X>> implements MetadataDefinitionHolder<X> {

    private final Map<Class<?>, MetadataDefinition<?>> metadataDefinitions = new HashMap<>();

    public MetadataDefinitionHolderImpl() {
    }

    public MetadataDefinitionHolderImpl(MetadataHolder metadataHolder) {
        for (Map.Entry<Class<?>, Object> entry : metadataHolder.getMetadata().entrySet()) {
            metadataDefinitions.put(entry.getKey(), new RuntimeMetadataDefinition(entry.getKey(), entry.getValue()));
        }
    }

    @Override
    public X withMetadataDefinition(MetadataDefinition<?> metadataDefinition) {
        metadataDefinitions.put(metadataDefinition.getJavaType(), metadataDefinition);
        return (X) this;
    }

    public Map<Class<?>, MetadataDefinition<?>> getMetadataDefinitions() {
        return metadataDefinitions;
    }
}
