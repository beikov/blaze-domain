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

package com.blazebit.domain.boot.model;

import java.util.Map;

/**
 * A domain entity type definition.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public interface EntityDomainTypeDefinition extends DomainTypeDefinition {

    /**
     * The attribute of the entity domain type with the given name or <code>null</code>.
     *
     * @param attributeName The name of the attribute
     * @return the attribute of the entity domain type
     */
    public EntityDomainTypeAttributeDefinition getAttribute(String attributeName);

    /**
     * The attributes of the entity domain type.
     *
     * @return the attributes of the entity domain type
     */
    public Map<String, ? extends EntityDomainTypeAttributeDefinition> getAttributes();

}
