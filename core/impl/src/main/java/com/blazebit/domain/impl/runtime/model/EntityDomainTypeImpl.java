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

import com.blazebit.domain.boot.model.EntityDomainTypeDefinition;
import com.blazebit.domain.boot.model.MetadataDefinition;
import com.blazebit.domain.impl.boot.model.EntityDomainTypeAttributeDefinitionImpl;
import com.blazebit.domain.impl.boot.model.EntityDomainTypeDefinitionImpl;
import com.blazebit.domain.impl.boot.model.MetamodelBuildingContext;
import com.blazebit.domain.runtime.model.EntityDomainType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class EntityDomainTypeImpl extends AbstractDomainType implements EntityDomainType, EntityDomainTypeDefinition {

    private final Map<String, EntityDomainTypeAttributeImpl> attributes;
    private final Map<Class<?>, Object> metadata;

    @SuppressWarnings("unchecked")
    public EntityDomainTypeImpl(EntityDomainTypeDefinitionImpl typeDefinition, MetamodelBuildingContext context) {
        super(typeDefinition, context);
        Map<String, EntityDomainTypeAttributeImpl> attributes;
        if (typeDefinition.isCaseSensitive()) {
            attributes = new HashMap<>(typeDefinition.getAttributes().size());
        } else {
            attributes = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        }
        for (EntityDomainTypeAttributeDefinitionImpl attributeDefinition : (Collection<EntityDomainTypeAttributeDefinitionImpl>) (Collection<?>) typeDefinition.getAttributes().values()) {
            EntityDomainTypeAttributeImpl old;
            if ((old = attributes.put(attributeDefinition.getName(), attributeDefinition.createAttribute(this, context))) != null) {
                context.addError("Duplicate attribute definition due to case insensitivity: [" + old.getName() + ", " + attributeDefinition.getName() + "]");
            }
        }
        this.attributes = attributes;
        this.metadata = context.createMetadata(typeDefinition);
    }

    @Override
    public DomainTypeKind getKind() {
        return DomainTypeKind.ENTITY;
    }

    @Override
    public EntityDomainTypeAttributeImpl getAttribute(String name) {
        return attributes.get(name);
    }

    @Override
    public Map<String, EntityDomainTypeAttributeImpl> getAttributes() {
        return attributes;
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
}
