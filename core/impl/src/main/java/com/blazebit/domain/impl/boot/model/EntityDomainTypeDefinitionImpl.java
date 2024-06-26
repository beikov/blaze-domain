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

import com.blazebit.domain.boot.model.EntityDomainTypeAttributeDefinition;
import com.blazebit.domain.boot.model.EntityDomainTypeDefinition;
import com.blazebit.domain.impl.runtime.model.EntityDomainTypeImpl;
import com.blazebit.domain.runtime.model.EntityDomainType;
import com.blazebit.domain.runtime.model.EntityDomainTypeAttribute;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class EntityDomainTypeDefinitionImpl extends AbstractMetadataDefinitionHolder implements EntityDomainTypeDefinition, DomainTypeDefinitionImplementor {

    private final String name;
    private final Class<?> javaType;
    private final Map<String, EntityDomainTypeAttributeDefinitionImpl> attributes;
    private boolean caseSensitive = true;
    private EntityDomainTypeImpl domainType;

    public EntityDomainTypeDefinitionImpl(String name, Class<?> javaType) {
        this.name = name;
        this.javaType = javaType;
        this.attributes = new HashMap<>();
    }

    public EntityDomainTypeDefinitionImpl(EntityDomainType entityDomainType) {
        super(entityDomainType);
        this.name = entityDomainType.getName();
        this.javaType = entityDomainType.getJavaType();
        this.attributes = new HashMap<>(entityDomainType.getAttributes().size());
        for (Map.Entry<String, ? extends EntityDomainTypeAttribute> entry : entityDomainType.getAttributes().entrySet()) {
            attributes.put(entry.getKey(), new EntityDomainTypeAttributeDefinitionImpl(this, entry.getValue()));
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

    public void addAttribute(EntityDomainTypeAttributeDefinitionImpl attribute) {
        attributes.put(attribute.getName(), attribute);
    }

    @Override
    public EntityDomainTypeAttributeDefinition getAttribute(String name) {
        return attributes.get(name);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, EntityDomainTypeAttributeDefinition> getAttributes() {
        return (Map<String, EntityDomainTypeAttributeDefinition>) (Map<?, ?>) attributes;
    }

    public void bindTypes(DomainBuilderImpl domainBuilder, MetamodelBuildingContext context) {
        this.domainType = null;
        for (EntityDomainTypeAttributeDefinitionImpl attributeDefinition : attributes.values()) {
            attributeDefinition.bindTypes(domainBuilder, context);
        }
    }

    @Override
    public EntityDomainTypeImpl getType(MetamodelBuildingContext context) {
        if (domainType == null) {
            domainType = new EntityDomainTypeImpl(this, context);
        }
        return domainType;
    }
}
