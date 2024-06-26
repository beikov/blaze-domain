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

import com.blazebit.domain.boot.model.EnumDomainTypeValueDefinition;
import com.blazebit.domain.boot.model.MetadataDefinition;
import com.blazebit.domain.impl.boot.model.EnumDomainTypeValueDefinitionImpl;
import com.blazebit.domain.impl.boot.model.MetamodelBuildingContext;
import com.blazebit.domain.runtime.model.EnumDomainTypeValue;

import java.util.Map;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class EnumDomainTypeValueImpl extends AbstractMetadataHolder implements EnumDomainTypeValue, EnumDomainTypeValueDefinition {

    private final EnumDomainTypeImpl owner;
    private final String value;
    private final Map<Class<?>, Object> metadata;

    public EnumDomainTypeValueImpl(EnumDomainTypeImpl owner, EnumDomainTypeValueDefinitionImpl enumValueDefinition, MetamodelBuildingContext context) {
        this.owner = owner;
        this.value = enumValueDefinition.getValue();
        this.metadata = context.createMetadata(enumValueDefinition);
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public EnumDomainTypeImpl getOwner() {
        return owner;
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
        return value;
    }
}
