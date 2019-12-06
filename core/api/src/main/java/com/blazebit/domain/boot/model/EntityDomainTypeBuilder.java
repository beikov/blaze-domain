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

package com.blazebit.domain.boot.model;

/**
 * An domain entity type builder.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public interface EntityDomainTypeBuilder {

    /**
     * Sets whether attribute names are case sensitive.
     *
     * @param caseSensitive Whether attribute names are case sensitive
     * @return this for chaining
     */
    public EntityDomainTypeBuilder setCaseSensitive(boolean caseSensitive);

    /**
     * Adds an attribute with the given name and type name.
     *
     * @param attributeName The attribute name
     * @param typeName The type name
     * @return this for chaining
     */
    public EntityDomainTypeBuilder addAttribute(String attributeName, String typeName);

    /**
     * Adds an attribute with the given name and type name as well as metadata definitions.
     *
     * @param attributeName The attribute name
     * @param typeName The type name
     * @param metadataDefinitions The metadata for the attribute
     * @return this for chaining
     */
    public EntityDomainTypeBuilder addAttribute(String attributeName, String typeName, MetadataDefinition<?>... metadataDefinitions);

    /**
     * Adds an attribute with the given name and java type.
     *
     * @param attributeName The attribute name
     * @param javaType The java type
     * @return this for chaining
     */
    public EntityDomainTypeBuilder addAttribute(String attributeName, Class<?> javaType);

    /**
     * Adds an attribute with the given name and java type as well as metadata definitions.
     *
     * @param attributeName The attribute name
     * @param javaType The java type
     * @param metadataDefinitions The metadata for the attribute
     * @return this for chaining
     */
    public EntityDomainTypeBuilder addAttribute(String attributeName, Class<?> javaType, MetadataDefinition<?>... metadataDefinitions);

    /**
     * Adds a collection attribute with the given name and element type name.
     *
     * @param attributeName The attribute name
     * @param elementTypeName The element type name
     * @return this for chaining
     */
    public EntityDomainTypeBuilder addCollectionAttribute(String attributeName, String elementTypeName);

    /**
     * Adds a collection attribute with the given name and element type name as well as metadata definitions.
     *
     * @param attributeName The attribute name
     * @param elementTypeName The element type name
     * @param metadataDefinitions The metadata for the attribute
     * @return this for chaining
     */
    public EntityDomainTypeBuilder addCollectionAttribute(String attributeName, String elementTypeName, MetadataDefinition<?>... metadataDefinitions);

    /**
     * Adds a collection attribute with the given name and element java type.
     *
     * @param attributeName The attribute name
     * @param elementJavaType The element java type
     * @return this for chaining
     */
    public EntityDomainTypeBuilder addCollectionAttribute(String attributeName, Class<?> elementJavaType);

    /**
     * Adds a collection attribute with the given name and element java type as well as metadata definitions.
     *
     * @param attributeName The attribute name
     * @param elementJavaType The element java type
     * @param metadataDefinitions The metadata for the attribute
     * @return this for chaining
     */
    public EntityDomainTypeBuilder addCollectionAttribute(String attributeName, Class<?> elementJavaType, MetadataDefinition<?>... metadataDefinitions);

    /**
     * Adds the given metadata definition to the entity type.
     *
     * @param metadataDefinition The metadata definition
     * @return this for chaining
     */
    public EntityDomainTypeBuilder withMetadata(MetadataDefinition<?> metadataDefinition);

    /**
     * Builds and adds the domain entity type to the domain builder.
     *
     * @return the domain builder for chaining
     */
    public DomainBuilder build();

}
