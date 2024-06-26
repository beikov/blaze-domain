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

package com.blazebit.domain.impl.runtime.model;

import com.blazebit.domain.boot.model.CollectionDomainTypeDefinition;
import com.blazebit.domain.boot.model.MetadataDefinition;
import com.blazebit.domain.impl.boot.model.MetamodelBuildingContext;
import com.blazebit.domain.runtime.model.CollectionDomainType;
import com.blazebit.domain.runtime.model.DomainOperator;
import com.blazebit.domain.runtime.model.DomainPredicate;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class CollectionDomainTypeImpl extends AbstractDomainType implements CollectionDomainType, CollectionDomainTypeDefinition {

    public static final CollectionDomainTypeImpl INSTANCE = new CollectionDomainTypeImpl("Collection", null);
    private static final Set<DomainOperator> ENABLED_OPERATORS = EnumSet.noneOf(DomainOperator.class);
    private static final Set<DomainPredicate> ENABLED_PREDICATES = EnumSet.of(DomainPredicate.COLLECTION);
    private final DomainTypeImplementor elementType;
    private final Map<Class<?>, Object> metadata;

    public CollectionDomainTypeImpl(CollectionDomainTypeDefinition typeDefinition, MetamodelBuildingContext context) {
        super(typeDefinition, context);
        this.elementType = context.getType(typeDefinition.getElementType());
        this.metadata = context.createMetadata(typeDefinition);
    }

    public CollectionDomainTypeImpl(String name, DomainTypeImplementor elementType) {
        super(name, Collection.class);
        this.elementType = elementType;
        this.metadata = Collections.emptyMap();
    }

    @Override
    public DomainTypeKind getKind() {
        return DomainTypeKind.COLLECTION;
    }

    @Override
    public Set<DomainOperator> getEnabledOperators() {
        return ENABLED_OPERATORS;
    }

    @Override
    public Set<DomainPredicate> getEnabledPredicates() {
        return ENABLED_PREDICATES;
    }

    @Override
    public DomainTypeImplementor getElementType() {
        return elementType;
    }

    @Override
    public <T> T getMetadata(Class<T> metadataType) {
        return (T) metadata.get(metadataType);
    }

    @Override
    public Map<Class<?>, Object> getMetadata() {
        return metadata;
    }

    @Override
    public Map<Class<?>, MetadataDefinition<?>> getMetadataDefinitions() {
        return getMetadataDefinitions(metadata);
    }

    @Override
    public String toString() {
        if (elementType == null) {
            return "Collection";
        } else {
            return "Collection[" + elementType.toString() + "]";
        }
    }
}
