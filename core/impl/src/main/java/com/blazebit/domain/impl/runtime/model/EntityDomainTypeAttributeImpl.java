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

import com.blazebit.domain.boot.model.EntityDomainTypeAttributeDefinition;
import com.blazebit.domain.boot.model.MetadataDefinition;
import com.blazebit.domain.impl.boot.model.EntityDomainTypeAttributeDefinitionImpl;
import com.blazebit.domain.impl.boot.model.MetamodelBuildingContext;
import com.blazebit.domain.runtime.model.DomainType;
import com.blazebit.domain.runtime.model.EntityDomainTypeAttribute;

import java.util.Map;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class EntityDomainTypeAttributeImpl extends AbstractMetadataHolder implements EntityDomainTypeAttribute, EntityDomainTypeAttributeDefinition {

    private final EntityDomainTypeImpl owner;
    private final String name;
    private final DomainType type;
    private final Map<Class<?>, Object> metadata;

    public EntityDomainTypeAttributeImpl(EntityDomainTypeImpl owner, EntityDomainTypeAttributeDefinitionImpl attributeDefinition, MetamodelBuildingContext context) {
        this.owner = owner;
        this.name = attributeDefinition.getName();
        this.type = context.getType(attributeDefinition.getTypeDefinition());
        this.metadata = context.createMetadata(attributeDefinition);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public EntityDomainTypeImpl getOwner() {
        return owner;
    }

    @Override
    public DomainType getType() {
        return type;
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
    public boolean isCollection() {
        return type.getKind() == DomainType.DomainTypeKind.COLLECTION;
    }

    @Override
    public String getTypeName() {
        return type.getName();
    }

    @Override
    public Map<Class<?>, MetadataDefinition<?>> getMetadataDefinitions() {
        return getMetadataDefinitions(metadata);
    }

    @Override
    public String toString() {
        return "EntityDomainTypeAttribute{" +
            "name='" + name + '\'' +
            ", type=" + type +
            '}';
    }
}
