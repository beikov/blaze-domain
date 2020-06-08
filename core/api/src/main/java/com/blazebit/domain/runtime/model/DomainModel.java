/*
 * Copyright 2019 - 2020 Blazebit.
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

package com.blazebit.domain.runtime.model;

import com.blazebit.domain.spi.DomainSerializer;

import java.util.List;
import java.util.Map;

/**
 * A type checked domain model that can be used for domain introspection.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public interface DomainModel {

    /**
     * Returns the domain type with the given type name or <code>null</code>.
     *
     * @param name The type name of the desired domain type
     * @return the domain type or <code>null</code>
     */
    public DomainType getType(String name);

    /**
     * Returns the domain type with the given java type or <code>null</code>.
     *
     * @param javaType The java type of the desired domain type
     * @return the domain type or <code>null</code>
     */
    public DomainType getType(Class<?> javaType);

    /**
     * Returns the entity domain type with the given type name or <code>null</code>.
     *
     * @param name The type name of the desired domain type
     * @return the entity domain type or <code>null</code>
     */
    public EntityDomainType getEntityType(String name);

    /**
     * Returns the entity domain type with the given java type or <code>null</code>.
     *
     * @param javaType The java type of the desired domain type
     * @return the entity domain type or <code>null</code>
     */
    public EntityDomainType getEntityType(Class<?> javaType);

    /**
     * Returns the collection domain type with the given element domain type or <code>null</code>.
     *
     * @param elementDomainType The element domain type of the desired collection domain type
     * @return the collection domain type or <code>null</code>
     */
    public CollectionDomainType getCollectionType(DomainType elementDomainType);

    /**
     * Returns the types of the domain model as map indexed by their type name.
     *
     * @return the types of the domain model
     */
    public Map<String, DomainType> getTypes();

    /**
     * Returns the types of the domain model as map indexed by their java type.
     *
     * @return the types of the domain model
     */
    public Map<Class<?>, DomainType> getTypesByJavaType();

    /**
     * Returns the collection types of the domain model as map indexed by their element domain type.
     *
     * @return the collection types of the domain model
     */
    public Map<DomainType, CollectionDomainType> getCollectionTypes();

    /**
     * Returns the domain function with the given name or <code>null</code>.
     *
     * @param name The name of the desired domain function
     * @return the domain function or <code>null</code>
     */
    public DomainFunction getFunction(String name);

    /**
     * Returns the functions of the domain model as map indexed by their function name.
     *
     * @return the functions of the domain model
     */
    public Map<String, DomainFunction> getFunctions();

    /**
     * Returns the function type resolver for the function with the given name.
     *
     * @param functionName The name of the function
     * @return the function type resolver
     */
    public DomainFunctionTypeResolver getFunctionTypeResolver(String functionName);

    /**
     * Returns the function type resolvers of the domain model as map indexed by their function name.
     *
     * @return the function type resolvers of the domain model
     */
    public Map<String, DomainFunctionTypeResolver> getFunctionTypeResolvers();

    /**
     * Returns the operation type resolver for resolving the type of the domain operator applied to the given type name.
     *
     * @param typeName The type name for which to apply the domain operator
     * @param operator The operator to apply on the type name
     * @return the operation type resolver
     */
    public DomainOperationTypeResolver getOperationTypeResolver(String typeName, DomainOperator operator);

    /**
     * Returns the operation type resolver for resolving the type of the domain operator applied to the given java type.
     *
     * @param javaType The java type for which to apply the domain operator
     * @param operator The operator to apply on the java type
     * @return the operation type resolver
     */
    public DomainOperationTypeResolver getOperationTypeResolver(Class<?> javaType, DomainOperator operator);

    /**
     * Returns the predicate type resolver for resolving the type of the domain predicate applied to the given type name.
     *
     * @param typeName The type name for which to apply the domain operator
     * @param predicateType The predicate to apply on the type name
     * @return the predicate type resolver
     */
    public DomainPredicateTypeResolver getPredicateTypeResolver(String typeName, DomainPredicate predicateType);

    /**
     * Returns the predicate type resolver for resolving the type of the domain predicate applied to the given java type.
     *
     * @param javaType The java type for which to apply the domain operator
     * @param predicateType The predicate to apply on the java type
     * @return the predicate type resolver
     */
    public DomainPredicateTypeResolver getPredicateTypeResolver(Class<?> javaType, DomainPredicate predicateType);

    /**
     * Returns the operation type resolvers of the domain model as map indexed by their type name.
     *
     * @return the operation type resolvers of the domain model
     */
    public Map<String, Map<DomainOperator, DomainOperationTypeResolver>> getOperationTypeResolvers();

    /**
     * Returns the operation type resolvers of the domain model as map indexed by their java type.
     *
     * @return the operation type resolvers of the domain model
     */
    public Map<Class<?>, Map<DomainOperator, DomainOperationTypeResolver>> getOperationTypeResolversByJavaType();

    /**
     * Returns the predicate type resolvers of the domain model as map indexed by their type name.
     *
     * @return the predicate type resolvers of the domain model
     */
    public Map<String, Map<DomainPredicate, DomainPredicateTypeResolver>> getPredicateTypeResolvers();

    /**
     * Returns the predicate type resolvers of the domain model as map indexed by their java type.
     *
     * @return the predicate type resolvers of the domain model
     */
    public Map<Class<?>, Map<DomainPredicate, DomainPredicateTypeResolver>> getPredicateTypeResolversByJavaType();

    /**
     * Returns the numeric literal resolver.
     *
     * @return the numeric literal resolver
     */
    public NumericLiteralResolver getNumericLiteralResolver();

    /**
     * Returns the boolean literal resolver.
     *
     * @return the boolean literal resolver
     */
    public BooleanLiteralResolver getBooleanLiteralResolver();

    /**
     * Returns the string literal resolver.
     *
     * @return the string literal resolver
     */
    public StringLiteralResolver getStringLiteralResolver();

    /**
     * Returns the temporal literal resolver.
     *
     * @return the temporal literal resolver
     */
    public TemporalLiteralResolver getTemporalLiteralResolver();

    /**
     * Returns the enum literal resolver.
     *
     * @return the enum literal resolver
     */
    public EnumLiteralResolver getEnumLiteralResolver();

    /**
     * Returns the entity literal resolver.
     *
     * @return the entity literal resolver
     */
    public EntityLiteralResolver getEntityLiteralResolver();

    /**
     * Returns the collection literal resolver.
     *
     * @return the collection literal resolver
     */
    public CollectionLiteralResolver getCollectionLiteralResolver();

    /**
     * Returns the domain serializers.
     *
     * @return the domain serializers
     */
    public List<DomainSerializer<DomainModel>> getDomainSerializers();

    /**
     * Serializes the domain model to the given target type with the given format.
     *
     * @param targetType The target type
     * @param format The serialization format
     * @param properties Serialization properties
     * @param <T> The target type
     * @return The serialized form
     */
    public <T> T serialize(Class<T> targetType, String format, Map<String, Object> properties);
}
