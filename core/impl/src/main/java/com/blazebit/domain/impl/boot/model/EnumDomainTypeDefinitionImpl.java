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

package com.blazebit.domain.impl.boot.model;

import com.blazebit.domain.impl.runtime.model.EnumDomainTypeImpl;
import com.blazebit.domain.boot.model.EnumDomainTypeDefinition;
import com.blazebit.domain.boot.model.EnumDomainTypeValueDefinition;
import com.blazebit.domain.runtime.model.EnumDomainType;
import com.blazebit.domain.runtime.model.EnumDomainTypeValue;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class EnumDomainTypeDefinitionImpl extends AbstractMetadataDefinitionHolder implements EnumDomainTypeDefinition, DomainTypeDefinitionImplementor {

    private final String name;
    private final Class<?> javaType;
    private final Map<String, EnumDomainTypeValueDefinitionImpl> enumValues = new HashMap<>();
    private boolean caseSensitive = true;
    private EnumDomainTypeImpl domainType;

    public EnumDomainTypeDefinitionImpl(String name, Class<?> javaType) {
        this.name = name;
        this.javaType = javaType;
    }

    public EnumDomainTypeDefinitionImpl(EnumDomainType enumDomainType) {
        super(enumDomainType);
        this.name = enumDomainType.getName();
        this.javaType = enumDomainType.getJavaType();
        for (Map.Entry<String, ? extends EnumDomainTypeValue> entry : enumDomainType.getEnumValues().entrySet()) {
            enumValues.put(entry.getKey(), new EnumDomainTypeValueDefinitionImpl(this, entry.getValue()));
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Class<?> getJavaType() {
        return javaType;
    }

    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    public void addEnumValue(EnumDomainTypeValueDefinitionImpl enumValue) {
        enumValues.put(enumValue.getValue(), enumValue);
    }

    @Override
    public EnumDomainTypeValueDefinition getEnumValue(String name) {
        return enumValues.get(name);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, EnumDomainTypeValueDefinition> getEnumValues() {
        return (Map<String, EnumDomainTypeValueDefinition>) (Map<?, ?>) enumValues;
    }

    @Override
    public void bindTypes(DomainBuilderImpl domainBuilder, MetamodelBuildingContext context) {

    }

    @Override
    public EnumDomainTypeImpl getType(MetamodelBuildingContext context) {
        if (domainType == null) {
            domainType = new EnumDomainTypeImpl(this, context);
        }
        return domainType;
    }
}
