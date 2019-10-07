/*
 * Copyright 2019 Blazebit.
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

package com.blazebit.domain.declarative.persistence;

import com.blazebit.domain.boot.model.MetadataDefinition;
import com.blazebit.domain.boot.model.MetadataDefinitionHolder;

/**
 * An implementation for the {@link com.blazebit.domain.persistence.EntityAttribute} interface that also is a {@link MetadataDefinition}.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
class EntityAttributeImpl implements com.blazebit.domain.persistence.EntityAttribute, MetadataDefinition<com.blazebit.domain.persistence.EntityAttribute> {

    private final String expression;

    /**
     * Creates a new instance based on the given annotation.
     *
     * @param entityAttribute The entity attribute annotation
     */
    public EntityAttributeImpl(EntityAttribute entityAttribute) {
        this(entityAttribute.value());
    }

    /**
     * Creates a new instance based on the given expression.
     *
     * @param expression The expression
     */
    public EntityAttributeImpl(String expression) {
        this.expression = expression;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getExpression() {
        return expression;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<com.blazebit.domain.persistence.EntityAttribute> getJavaType() {
        return com.blazebit.domain.persistence.EntityAttribute.class;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public com.blazebit.domain.persistence.EntityAttribute build(MetadataDefinitionHolder<?> definitionHolder) {
        return this;
    }
}
