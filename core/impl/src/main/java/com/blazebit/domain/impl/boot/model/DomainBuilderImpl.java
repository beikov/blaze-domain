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

package com.blazebit.domain.impl.boot.model;

import com.blazebit.domain.boot.model.CollectionDomainTypeDefinition;
import com.blazebit.domain.boot.model.DomainBuilder;
import com.blazebit.domain.boot.model.DomainFunctionBuilder;
import com.blazebit.domain.boot.model.DomainFunctionDefinition;
import com.blazebit.domain.boot.model.DomainTypeDefinition;
import com.blazebit.domain.boot.model.EntityDomainTypeAttributeDefinition;
import com.blazebit.domain.boot.model.EntityDomainTypeBuilder;
import com.blazebit.domain.boot.model.EntityDomainTypeDefinition;
import com.blazebit.domain.boot.model.MetadataDefinition;
import com.blazebit.domain.impl.runtime.model.DomainModelImpl;
import com.blazebit.domain.runtime.model.BooleanLiteralResolver;
import com.blazebit.domain.runtime.model.CollectionDomainType;
import com.blazebit.domain.runtime.model.CollectionLiteralResolver;
import com.blazebit.domain.runtime.model.DomainFunction;
import com.blazebit.domain.runtime.model.DomainFunctionTypeResolver;
import com.blazebit.domain.runtime.model.DomainModel;
import com.blazebit.domain.runtime.model.DomainOperationTypeResolver;
import com.blazebit.domain.runtime.model.DomainOperator;
import com.blazebit.domain.runtime.model.DomainPredicateType;
import com.blazebit.domain.runtime.model.DomainPredicateTypeResolver;
import com.blazebit.domain.runtime.model.DomainType;
import com.blazebit.domain.runtime.model.EntityLiteralResolver;
import com.blazebit.domain.runtime.model.EnumLiteralResolver;
import com.blazebit.domain.runtime.model.NumericLiteralResolver;
import com.blazebit.domain.runtime.model.StaticDomainOperationTypeResolvers;
import com.blazebit.domain.runtime.model.StaticDomainPredicateTypeResolvers;
import com.blazebit.domain.runtime.model.StringLiteralResolver;
import com.blazebit.domain.runtime.model.TemporalLiteralResolver;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class DomainBuilderImpl implements DomainBuilder {

    private final DomainModel baseModel;
    private Map<String, DomainFunctionDefinitionImpl> domainFunctionDefinitions = new HashMap<>();
    private Map<String, Set<DomainOperator>> enabledOperators = new HashMap<>();
    private Map<String, Set<DomainPredicateType>> enabledPredicates = new HashMap<>();
    private Map<String, DomainTypeDefinitionImplementor<?>> domainTypeDefinitions = new HashMap<>();
    private Map<Class<?>, DomainTypeDefinitionImplementor<?>> domainTypeDefinitionsByJavaType = new HashMap<>();
    private Map<String, CollectionDomainTypeDefinitionImpl> collectionDomainTypeDefinitions = new HashMap<>();
    private Map<Class<?>, CollectionDomainTypeDefinitionImpl> collectionDomainTypeDefinitionsByJavaType = new HashMap<>();
    private Map<String, DomainFunctionTypeResolver> domainFunctionTypeResolvers = new HashMap<>();
    private Map<String, Map<DomainOperator, DomainOperationTypeResolver>> domainOperationTypeResolvers = new HashMap<>();
    private Map<Class<?>, Map<DomainOperator, DomainOperationTypeResolver>> domainOperationTypeResolversByJavaType = new HashMap<>();
    private Map<String, Map<DomainPredicateType, DomainPredicateTypeResolver>> domainPredicateTypeResolvers = new HashMap<>();
    private Map<Class<?>, Map<DomainPredicateType, DomainPredicateTypeResolver>> domainPredicateTypeResolversByJavaType = new HashMap<>();
    private NumericLiteralResolver numericLiteralResolver;
    private BooleanLiteralResolver booleanLiteralResolver;
    private StringLiteralResolver stringLiteralResolver;
    private TemporalLiteralResolver temporalLiteralResolver;
    private EnumLiteralResolver enumLiteralResolver;
    private EntityLiteralResolver entityLiteralResolver;
    private CollectionLiteralResolver collectionLiteralResolver;
    private boolean functionsCaseSensitive = true;

    public DomainBuilderImpl() {
        this.baseModel = null;
    }

    public DomainBuilderImpl(DomainModel domainModel) {
        this.baseModel = domainModel;
        this.numericLiteralResolver = domainModel.getNumericLiteralResolver();
        this.booleanLiteralResolver = domainModel.getBooleanLiteralResolver();
        this.stringLiteralResolver = domainModel.getStringLiteralResolver();
        this.temporalLiteralResolver = domainModel.getTemporalLiteralResolver();
        this.enumLiteralResolver = domainModel.getEnumLiteralResolver();
        this.entityLiteralResolver = domainModel.getEntityLiteralResolver();
        this.collectionLiteralResolver = domainModel.getCollectionLiteralResolver();
    }

    DomainModel getBaseModel() {
        return baseModel;
    }

    DomainBuilderImpl withDomainTypeDefinition(DomainTypeDefinitionImplementor<?> domainTypeDefinition) {
        domainTypeDefinitions.put(domainTypeDefinition.getName(), domainTypeDefinition);
        if (domainTypeDefinition.getJavaType() != null) {
            domainTypeDefinitionsByJavaType.put(domainTypeDefinition.getJavaType(), domainTypeDefinition);
        }
        return this;
    }

    DomainBuilderImpl withDomainFunctionDefinition(DomainFunctionDefinitionImpl domainFunctionDefinition) {
        domainFunctionDefinitions.put(domainFunctionDefinition.getName(), domainFunctionDefinition);
        return this;
    }

    public DomainTypeDefinition<?> getDomainTypeDefinition(String typeName) {
        DomainTypeDefinition<?> typeDefinition = domainTypeDefinitions.get(typeName);
        if (typeDefinition == null && baseModel != null) {
            typeDefinition = (DomainTypeDefinition<?>) baseModel.getType(typeName);
        }
        return typeDefinition;
    }

    public DomainTypeDefinition<?> getDomainTypeDefinition(Class<?> javaType) {
        DomainTypeDefinition<?> typeDefinition = domainTypeDefinitionsByJavaType.get(javaType);
        if (typeDefinition == null && baseModel != null) {
            typeDefinition = (DomainTypeDefinition<?>) baseModel.getType(javaType);
        }
        return typeDefinition;
    }

    public CollectionDomainTypeDefinitionImpl getCollectionDomainTypeDefinition(DomainTypeDefinition<?> typeDefinition) {
        if (typeDefinition == null) {
            return null;
        }
        CollectionDomainTypeDefinitionImpl collectionDomainTypeDefinition = collectionDomainTypeDefinitions.get(typeDefinition.getName());
        if (collectionDomainTypeDefinition == null && typeDefinition.getJavaType() != null) {
            collectionDomainTypeDefinition = collectionDomainTypeDefinitionsByJavaType.get(typeDefinition.getJavaType());
        }
        if (collectionDomainTypeDefinition == null) {
            if (baseModel != null) {
                collectionDomainTypeDefinition = collectionDomainTypeDefinitions.get(typeDefinition.getName());
                if (collectionDomainTypeDefinition == null && typeDefinition.getJavaType() != null) {
                    collectionDomainTypeDefinition = collectionDomainTypeDefinitionsByJavaType.get(typeDefinition.getJavaType());
                }
            }
            if (collectionDomainTypeDefinition == null) {
                collectionDomainTypeDefinition = new CollectionDomainTypeDefinitionImpl("Collection", Collection.class, typeDefinition);
                collectionDomainTypeDefinitions.put(typeDefinition.getName(), collectionDomainTypeDefinition);
                if (typeDefinition.getJavaType() != null) {
                    collectionDomainTypeDefinitionsByJavaType.put(typeDefinition.getJavaType(), collectionDomainTypeDefinition);
                }
            }
        }
        return collectionDomainTypeDefinition;
    }

    @Override
    public DomainBuilder withBooleanLiteralResolver(BooleanLiteralResolver literalResolver) {
        this.booleanLiteralResolver = literalResolver;
        return this;
    }

    @Override
    public DomainBuilder withNumericLiteralResolver(NumericLiteralResolver literalResolver) {
        this.numericLiteralResolver = literalResolver;
        return this;
    }

    @Override
    public DomainBuilder withStringLiteralResolver(StringLiteralResolver literalResolver) {
        this.stringLiteralResolver = literalResolver;
        return this;
    }

    @Override
    public DomainBuilder withTemporalLiteralResolver(TemporalLiteralResolver literalResolver) {
        this.temporalLiteralResolver = literalResolver;
        return this;
    }

    @Override
    public DomainBuilder withEnumLiteralResolver(EnumLiteralResolver literalResolver) {
        this.enumLiteralResolver = literalResolver;
        return this;
    }

    @Override
    public DomainBuilder withEntityLiteralResolver(EntityLiteralResolver literalResolver) {
        this.entityLiteralResolver = literalResolver;
        return this;
    }

    @Override
    public DomainBuilder withCollectionLiteralResolver(CollectionLiteralResolver literalResolver) {
        this.collectionLiteralResolver = literalResolver;
        return this;
    }

    @Override
    public DomainBuilder withFunctionTypeResolver(String functionName, DomainFunctionTypeResolver functionTypeResolver) {
        domainFunctionTypeResolvers.put(functionName, functionTypeResolver);
        return this;
    }

    @Override
    public DomainBuilder withOperationTypeResolver(String typeName, DomainOperator domainOperator, DomainOperationTypeResolver operationTypeResolver) {
        Map<DomainOperator, DomainOperationTypeResolver> operationTypeResolverMap = domainOperationTypeResolvers.get(typeName);
        if (operationTypeResolverMap == null) {
            operationTypeResolverMap = new HashMap<>();
            domainOperationTypeResolvers.put(typeName, operationTypeResolverMap);
        }
        operationTypeResolverMap.put(domainOperator, operationTypeResolver);
        return this;
    }

    @Override
    public DomainBuilder withOperationTypeResolver(Class<?> javaType, DomainOperator domainOperator, DomainOperationTypeResolver operationTypeResolver) {
        Map<DomainOperator, DomainOperationTypeResolver> operationTypeResolverMap = domainOperationTypeResolversByJavaType.get(javaType);
        if (operationTypeResolverMap == null) {
            operationTypeResolverMap = new HashMap<>();
            domainOperationTypeResolversByJavaType.put(javaType, operationTypeResolverMap);
        }
        operationTypeResolverMap.put(domainOperator, operationTypeResolver);
        return this;
    }

    @Override
    public DomainBuilder withPredicateTypeResolver(String typeName, DomainPredicateType domainPredicateType, DomainPredicateTypeResolver predicateTypeResolver) {
        Map<DomainPredicateType, DomainPredicateTypeResolver> operationTypeResolverMap = domainPredicateTypeResolvers.get(typeName);
        if (operationTypeResolverMap == null) {
            operationTypeResolverMap = new HashMap<>();
            domainPredicateTypeResolvers.put(typeName, operationTypeResolverMap);
        }
        operationTypeResolverMap.put(domainPredicateType, predicateTypeResolver);
        return this;
    }

    @Override
    public DomainBuilder withPredicateTypeResolver(Class<?> javaType, DomainPredicateType domainPredicateType, DomainPredicateTypeResolver predicateTypeResolver) {
        Map<DomainPredicateType, DomainPredicateTypeResolver> operationTypeResolverMap = domainPredicateTypeResolversByJavaType.get(javaType);
        if (operationTypeResolverMap == null) {
            operationTypeResolverMap = new HashMap<>();
            domainPredicateTypeResolversByJavaType.put(javaType, operationTypeResolverMap);
        }
        operationTypeResolverMap.put(domainPredicateType, predicateTypeResolver);
        return this;
    }

    @Override
    public DomainBuilder withOperator(String typeName, DomainOperator operator) {
        return withElement(enabledOperators, typeName, operator);
    }

    @Override
    public DomainBuilder withPredicate(String typeName, DomainPredicateType predicate) {
        return withElement(enabledPredicates, typeName, predicate);
    }

    @Override
    public DomainBuilder withOperator(String typeName, DomainOperator... operators) {
        return withElements(enabledOperators, typeName, operators);
    }

    @Override
    public DomainBuilder withPredicate(String typeName, DomainPredicateType... predicates) {
        return withElements(enabledPredicates, typeName, predicates);
    }

    public Set<DomainOperator> getOperators(DomainTypeDefinition<?> typeDefinition) {
        return getElements(enabledOperators, typeDefinition.getName());
    }

    public Set<DomainPredicateType> getPredicates(DomainTypeDefinition<?> typeDefinition) {
        return getElements(enabledPredicates, typeDefinition.getName());
    }

    private <T extends Enum<T>> DomainBuilder withElement(Map<String, Set<T>> map, String typeName, T predicate) {
        Set<T> domainPredicates = map.get(typeName);
        if (domainPredicates == null) {
            domainPredicates = EnumSet.of(predicate);
            map.put(typeName, domainPredicates);
        } else {
            domainPredicates.add(predicate);
        }
        return this;
    }

    private <T extends Enum<T>> DomainBuilder withElements(Map<String, Set<T>> map, String typeName, T... predicates) {
        Set<T> domainPredicates = map.get(typeName);
        if (domainPredicates == null) {
            domainPredicates = EnumSet.noneOf((Class<T>) predicates[0].getClass());
            map.put(typeName, domainPredicates);
        }

        for (int i = 0; i < predicates.length; i++) {
            T predicate = predicates[i];
            domainPredicates.add(predicate);
        }

        return this;
    }

    private <T> Set<T> getElements(Map<String, Set<T>> map, String typeName) {
        Set<T> set = map.get(typeName);
        if (set == null) {
            return Collections.emptySet();
        }
        return set;
    }

    @Override
    public DomainFunctionBuilder createFunction(String name) {
        return new DomainFunctionBuilderImpl(this, name);
    }

    @Override
    public DomainBuilder createBasicType(String name) {
        return withDomainTypeDefinition(new BasicDomainTypeDefinitionImpl(name, null));
    }

    @Override
    public DomainBuilder createBasicType(String name, Class<?> javaType) {
        return withDomainTypeDefinition(new BasicDomainTypeDefinitionImpl(name, javaType));
    }

    @Override
    public DomainBuilder createBasicType(String name, MetadataDefinition<?>... metadataDefinitions) {
        BasicDomainTypeDefinitionImpl basicDomainTypeDefinition = new BasicDomainTypeDefinitionImpl(name, null);
        for (MetadataDefinition<?> metadataDefinition : metadataDefinitions) {
            basicDomainTypeDefinition.withMetadataDefinition(metadataDefinition);
        }

        return withDomainTypeDefinition(basicDomainTypeDefinition);
    }

    @Override
    public DomainBuilder createBasicType(String name, Class<?> javaType, MetadataDefinition<?>... metadataDefinitions) {
        BasicDomainTypeDefinitionImpl basicDomainTypeDefinition = new BasicDomainTypeDefinitionImpl(name, javaType);
        for (MetadataDefinition<?> metadataDefinition : metadataDefinitions) {
            basicDomainTypeDefinition.withMetadataDefinition(metadataDefinition);
        }

        return withDomainTypeDefinition(basicDomainTypeDefinition);
    }

    @Override
    public EntityDomainTypeBuilderImpl createEntityType(String name) {
        return new EntityDomainTypeBuilderImpl(this, name, null);
    }

    @Override
    public EntityDomainTypeBuilderImpl createEntityType(String name, Class<?> javaType) {
        return new EntityDomainTypeBuilderImpl(this, name, javaType);
    }

    @Override
    public EntityDomainTypeBuilder extendEntityType(String name, EntityDomainTypeDefinition baseEntityType) {
        EntityDomainTypeBuilderImpl builder = createEntityType(name);
        for (MetadataDefinition<?> metadataDefinition : baseEntityType.getMetadataDefinitions().values()) {
            builder.withMetadata(metadataDefinition);
        }

        for (EntityDomainTypeAttributeDefinition attribute : baseEntityType.getAttributes().values()) {
            builder.addAttribute(attribute.getName(), attribute.getTypeName(), attribute.getMetadataDefinitions().values().toArray(new MetadataDefinition[0]));
        }

        return builder;
    }

    @Override
    public EntityDomainTypeBuilder extendEntityType(String name, Class<?> javaType, EntityDomainTypeDefinition baseEntityType) {
        EntityDomainTypeBuilderImpl builder = createEntityType(name, javaType);
        for (MetadataDefinition<?> metadataDefinition : baseEntityType.getMetadataDefinitions().values()) {
            builder.withMetadata(metadataDefinition);
        }

        for (EntityDomainTypeAttributeDefinition attribute : baseEntityType.getAttributes().values()) {
            builder.addAttribute(attribute.getName(), attribute.getTypeName(), attribute.getMetadataDefinitions().values().toArray(new MetadataDefinition[0]));
        }

        return builder;
    }

    @Override
    public EnumDomainTypeBuilderImpl createEnumType(String name) {
        return new EnumDomainTypeBuilderImpl(this, name, null);
    }

    @Override
    public EnumDomainTypeBuilderImpl createEnumType(String name, Class<? extends Enum<?>> javaType) {
        return new EnumDomainTypeBuilderImpl(this, name, javaType);
    }

    @Override
    public DomainTypeDefinition<?> getType(String name) {
        return domainTypeDefinitions.get(name);
    }

    @Override
    public DomainTypeDefinition<?> getType(Class<?> javaType) {
        return domainTypeDefinitionsByJavaType.get(javaType);
    }

    @Override
    public EntityDomainTypeDefinition getEntityType(String name) {
        return (EntityDomainTypeDefinition) (DomainTypeDefinition<?>) domainTypeDefinitions.get(name);
    }

    @Override
    public EntityDomainTypeDefinition getEntityType(Class<?> javaType) {
        return (EntityDomainTypeDefinition) (DomainTypeDefinition<?>) domainTypeDefinitionsByJavaType.get(javaType);
    }

    @Override
    public CollectionDomainTypeDefinition getCollectionType(String elementDomainTypeName) {
        return collectionDomainTypeDefinitions.get(elementDomainTypeName);
    }

    @Override
    public CollectionDomainTypeDefinition getCollectionType(Class<?> elementDomainJavaType) {
        return collectionDomainTypeDefinitionsByJavaType.get(elementDomainJavaType);
    }

    @Override
    public Map<String, DomainTypeDefinition<?>> getTypes() {
        return (Map<String, DomainTypeDefinition<?>>) (Map<?, ?>) domainTypeDefinitions;
    }

    @Override
    public Map<Class<?>, DomainTypeDefinition<?>> getTypesByJavaType() {
        return (Map<Class<?>, DomainTypeDefinition<?>>) (Map<?, ?>) domainTypeDefinitionsByJavaType;
    }

    @Override
    public Map<String, CollectionDomainTypeDefinition> getCollectionTypes() {
        return (Map<String, CollectionDomainTypeDefinition>) (Map<?, ?>) collectionDomainTypeDefinitions;
    }

    @Override
    public Map<Class<?>, CollectionDomainTypeDefinition> getCollectionTypesByJavaType() {
        return (Map<Class<?>, CollectionDomainTypeDefinition>) (Map<?, ?>) collectionDomainTypeDefinitionsByJavaType;
    }

    @Override
    public DomainFunctionDefinition getFunction(String name) {
        return domainFunctionDefinitions.get(name);
    }

    @Override
    public Map<String, DomainFunctionDefinition> getFunctions() {
        return (Map<String, DomainFunctionDefinition>) (Map<?, ?>) domainFunctionDefinitions;
    }

    @Override
    public DomainBuilder setFunctionCaseSensitive(boolean caseSensitive) {
        this.functionsCaseSensitive = caseSensitive;
        return this;
    }

    @Override
    public DomainModel build() {
        MetamodelBuildingContext context = new MetamodelBuildingContext(this);
        for (DomainTypeDefinitionImplementor<?> typeDefinition : domainTypeDefinitions.values()) {
            typeDefinition.bindTypes(this, context);
        }
        for (DomainFunctionDefinitionImpl domainFunctionDefinition : domainFunctionDefinitions.values()) {
            domainFunctionDefinition.bindTypes(this, context);
        }

        Map<String, DomainType> domainTypes = new HashMap<>(domainTypeDefinitions.size());
        Map<Class<?>, DomainType> domainTypesByJavaType = new HashMap<>(domainTypeDefinitions.size());
        Map<DomainType, CollectionDomainType> collectionDomainTypes = new HashMap<>(domainTypeDefinitions.size());
        if (!context.hasErrors()) {
            if (baseModel != null) {
                domainTypes.putAll(baseModel.getTypes());
                domainTypesByJavaType.putAll(baseModel.getTypesByJavaType());
                collectionDomainTypes.putAll(baseModel.getCollectionTypes());
            }

            for (DomainTypeDefinitionImplementor<?> typeDefinition : domainTypeDefinitions.values()) {
                DomainType domainType = context.getType(typeDefinition);
                domainTypes.put(typeDefinition.getName(), domainType);
                if (typeDefinition.getJavaType() != null) {
                    domainTypesByJavaType.put(domainType.getJavaType(), domainType);
                }
                CollectionDomainTypeDefinitionImpl collectionDomainTypeDefinition = getCollectionDomainTypeDefinition(typeDefinition);
                collectionDomainTypeDefinition.bindTypes(this, context);
                collectionDomainTypes.put(domainType, collectionDomainTypeDefinition.getType(context));
            }
        }
        Map<String, DomainFunction> domainFunctions;
        if (functionsCaseSensitive) {
            domainFunctions = new HashMap<>(domainFunctionDefinitions.size());
        } else {
            domainFunctions = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        }
        if (!context.hasErrors()) {
            if (baseModel != null) {
                domainFunctions.putAll(baseModel.getFunctions());
            }
            for (DomainFunctionDefinitionImpl functionDefinition : domainFunctionDefinitions.values()) {
                domainFunctions.put(functionDefinition.getName().toUpperCase(), functionDefinition.getFunction(context));
            }
        }
        Map<String, DomainFunctionTypeResolver> domainFunctionTypeResolvers = new HashMap<>(this.domainFunctionTypeResolvers.size());
        if (!context.hasErrors()) {
            if (baseModel != null) {
                domainFunctionTypeResolvers.putAll(baseModel.getFunctionTypeResolvers());
            }
            for (Map.Entry<String, DomainFunctionTypeResolver> entry : this.domainFunctionTypeResolvers.entrySet()) {
                String name = entry.getKey().toUpperCase();
                domainFunctionTypeResolvers.put(name, entry.getValue());
                if (!domainFunctions.containsKey(name)) {
                    context.addError("A function type resolver was registered but no function with the name '" + entry.getKey() + "' was found: " + entry.getValue());
                }
            }
        }

        Map<String, Map<DomainOperator, DomainOperationTypeResolver>> domainOperationTypeResolvers = new HashMap<>(this.domainOperationTypeResolvers.size());
        Map<Class<?>, Map<DomainOperator, DomainOperationTypeResolver>> domainOperationTypeResolversByJavaType = new HashMap<>(this.domainOperationTypeResolversByJavaType.size());
        if (!context.hasErrors()) {
            resolveDomainOperationTypeResolvers(context, domainTypes, domainOperationTypeResolvers, domainOperationTypeResolversByJavaType);
        }

        if (!context.hasErrors()) {
            resolveDomainOperationTypeResolversByJavaType(context, domainTypesByJavaType, domainOperationTypeResolvers, domainOperationTypeResolversByJavaType);
        }

        Map<String, Map<DomainPredicateType, DomainPredicateTypeResolver>> domainPredicateTypeResolvers = new HashMap<>(this.domainPredicateTypeResolvers.size());
        Map<Class<?>, Map<DomainPredicateType, DomainPredicateTypeResolver>> domainPredicateTypeResolversByJavaType = new HashMap<>(this.domainPredicateTypeResolversByJavaType.size());
        if (!context.hasErrors()) {
            resolveDomainPredicateTypeResolvers(context, domainTypes, domainPredicateTypeResolvers, domainPredicateTypeResolversByJavaType);
        }

        if (!context.hasErrors()) {
            resolveDomainPredicateTypeResolversByJavaType(context, domainTypesByJavaType, domainPredicateTypeResolvers, domainPredicateTypeResolversByJavaType);
        }

        if (!context.hasErrors()) {
            for (DomainType domainType : domainTypes.values()) {
                Map<DomainOperator, DomainOperationTypeResolver> operationTypeResolverMap = domainOperationTypeResolvers.get(domainType.getName());
                if (operationTypeResolverMap == null && !domainType.getEnabledOperators().isEmpty()) {
                    operationTypeResolverMap = new HashMap<>();
                    domainOperationTypeResolvers.put(domainType.getName(), operationTypeResolverMap);
                }
                for (DomainOperator enabledOperator : domainType.getEnabledOperators()) {
                    if (!operationTypeResolverMap.containsKey(enabledOperator)) {
                        // TODO: Maybe throw an error instead?
                        operationTypeResolverMap.put(enabledOperator, StaticDomainOperationTypeResolvers.returning(domainType.getName()));
                    }
                }

                Map<DomainPredicateType, DomainPredicateTypeResolver> predicateTypeResolverMap = domainPredicateTypeResolvers.get(domainType.getName());
                if (predicateTypeResolverMap == null && !domainType.getEnabledPredicates().isEmpty()) {
                    predicateTypeResolverMap = new HashMap<>();
                    domainPredicateTypeResolvers.put(domainType.getName(), predicateTypeResolverMap);
                }
                for (DomainPredicateType enabledPredicate : domainType.getEnabledPredicates()) {
                    if (!predicateTypeResolverMap.containsKey(enabledPredicate)) {
                        predicateTypeResolverMap.put(enabledPredicate, StaticDomainPredicateTypeResolvers.returning(Boolean.class));
                    }
                }
            }
        }

        if (context.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Couldn't build the domain model because of some errors:");
            for (String error : context.getErrors()) {
                sb.append('\n').append(error);
            }

            throw new IllegalArgumentException(sb.toString());
        }

        return new DomainModelImpl(
                domainTypes,
                domainTypesByJavaType,
                collectionDomainTypes,
                domainFunctions,
                domainFunctionTypeResolvers,
                domainOperationTypeResolvers,
                domainOperationTypeResolversByJavaType,
                domainPredicateTypeResolvers,
                domainPredicateTypeResolversByJavaType,
                numericLiteralResolver,
                booleanLiteralResolver,
                stringLiteralResolver,
                temporalLiteralResolver,
                enumLiteralResolver,
                entityLiteralResolver,
                collectionLiteralResolver
        );
    }

    private void resolveDomainOperationTypeResolvers(MetamodelBuildingContext context, Map<String, DomainType> domainTypes, Map<String, Map<DomainOperator, DomainOperationTypeResolver>> domainOperationTypeResolvers, Map<Class<?>, Map<DomainOperator, DomainOperationTypeResolver>> domainOperationTypeResolversByJavaType) {
        if (baseModel != null) {
            for (Map.Entry<String, Map<DomainOperator, DomainOperationTypeResolver>> entry : baseModel.getOperationTypeResolvers().entrySet()) {
                domainOperationTypeResolvers.put(entry.getKey(), entry.getValue());
            }
            for (Map.Entry<Class<?>, Map<DomainOperator, DomainOperationTypeResolver>> entry : baseModel.getOperationTypeResolversByJavaType().entrySet()) {
                domainOperationTypeResolversByJavaType.put(entry.getKey(), entry.getValue());
            }
        }
        for (Map.Entry<String, Map<DomainOperator, DomainOperationTypeResolver>> entry : this.domainOperationTypeResolvers.entrySet()) {
            String typeName = entry.getKey();
            DomainType domainType = domainTypes.get(typeName);
            if (domainType == null) {
                context.addError("An operation type resolver was registered but no type with the name '" + typeName + "' was found: " + entry.getValue());
            } else {
                Map<DomainOperator, DomainOperationTypeResolver> operationTypeResolverMap = new HashMap<>(entry.getValue().size());
                domainOperationTypeResolvers.put(typeName, operationTypeResolverMap);

                Map<DomainOperator, DomainOperationTypeResolver> operationTypeResolverMapByJavaType = domainOperationTypeResolversByJavaType.get(domainType.getJavaType());
                if (operationTypeResolverMapByJavaType == null && domainType.getJavaType() != null) {
                    operationTypeResolverMapByJavaType = new HashMap<>();
                    domainOperationTypeResolversByJavaType.put(domainType.getJavaType(), operationTypeResolverMapByJavaType);
                }

                for (Map.Entry<DomainOperator, DomainOperationTypeResolver> resolverEntry : entry.getValue().entrySet()) {
                    if (domainType.getEnabledOperators().contains(resolverEntry.getKey())) {
                        operationTypeResolverMap.put(resolverEntry.getKey(), resolverEntry.getValue());
                        if (operationTypeResolverMapByJavaType != null) {
                            operationTypeResolverMapByJavaType.put(resolverEntry.getKey(), resolverEntry.getValue());
                        }
                    } else {
                        context.addError("An operation type resolver for the type with the name '" + typeName + "' was registered for a non enabled operator '" + resolverEntry.getKey() + "': " + resolverEntry.getValue());
                    }
                }
            }
        }
    }

    private void resolveDomainOperationTypeResolversByJavaType(MetamodelBuildingContext context, Map<Class<?>, DomainType> domainTypesByJavaType, Map<String, Map<DomainOperator, DomainOperationTypeResolver>> domainOperationTypeResolvers, Map<Class<?>, Map<DomainOperator, DomainOperationTypeResolver>> domainOperationTypeResolversByJavaType) {
        for (Map.Entry<Class<?>, Map<DomainOperator, DomainOperationTypeResolver>> entry : this.domainOperationTypeResolversByJavaType.entrySet()) {
            Class<?> javaType = entry.getKey();
            DomainType domainType = domainTypesByJavaType.get(javaType);
            if (domainType == null) {
                context.addError("An operation type resolver was registered but no type with the java type '" + javaType + "' was found: " + entry.getValue());
            } else {
                Map<DomainOperator, DomainOperationTypeResolver> operationTypeResolverMap = domainOperationTypeResolvers.get(domainType.getName());
                if (operationTypeResolverMap == null) {
                    operationTypeResolverMap = new HashMap<>(entry.getValue().size());
                    domainOperationTypeResolvers.put(domainType.getName(), operationTypeResolverMap);
                }

                Map<DomainOperator, DomainOperationTypeResolver> operationTypeResolverMapByJavaType = domainOperationTypeResolversByJavaType.get(domainType.getJavaType());
                if (operationTypeResolverMapByJavaType == null) {
                    operationTypeResolverMapByJavaType = new HashMap<>();
                    domainOperationTypeResolversByJavaType.put(domainType.getJavaType(), operationTypeResolverMapByJavaType);
                }

                for (Map.Entry<DomainOperator, DomainOperationTypeResolver> resolverEntry : entry.getValue().entrySet()) {
                    if (domainType.getEnabledOperators().contains(resolverEntry.getKey())) {
                        operationTypeResolverMap.put(resolverEntry.getKey(), resolverEntry.getValue());
                        operationTypeResolverMapByJavaType.put(resolverEntry.getKey(), resolverEntry.getValue());
                    } else {
                        context.addError("An operation type resolver for the type with the java type '" + javaType + "' was registered for a non enabled operator '" + resolverEntry.getKey() + "': " + resolverEntry.getValue());
                    }
                }
            }
        }
    }

    private void resolveDomainPredicateTypeResolvers(MetamodelBuildingContext context, Map<String, DomainType> domainTypes, Map<String, Map<DomainPredicateType, DomainPredicateTypeResolver>> domainPredicateTypeResolvers, Map<Class<?>, Map<DomainPredicateType, DomainPredicateTypeResolver>> domainPredicateTypeResolversByJavaType) {
        if (baseModel != null) {
            for (Map.Entry<String, Map<DomainPredicateType, DomainPredicateTypeResolver>> entry : baseModel.getPredicateTypeResolvers().entrySet()) {
                domainPredicateTypeResolvers.put(entry.getKey(), entry.getValue());
            }
            for (Map.Entry<Class<?>, Map<DomainPredicateType, DomainPredicateTypeResolver>> entry : baseModel.getPredicateTypeResolversByJavaType().entrySet()) {
                domainPredicateTypeResolversByJavaType.put(entry.getKey(), entry.getValue());
            }
        }
        for (Map.Entry<String, Map<DomainPredicateType, DomainPredicateTypeResolver>> entry : this.domainPredicateTypeResolvers.entrySet()) {
            String typeName = entry.getKey();
            DomainType domainType = domainTypes.get(typeName);
            if (domainType == null) {
                context.addError("An operation type resolver was registered but no type with the name '" + typeName + "' was found: " + entry.getValue());
            } else {
                Map<DomainPredicateType, DomainPredicateTypeResolver> predicateTypeResolverMap = new HashMap<>(entry.getValue().size());
                domainPredicateTypeResolvers.put(typeName, predicateTypeResolverMap);

                Map<DomainPredicateType, DomainPredicateTypeResolver> predicateTypeResolverMapByJavaType = domainPredicateTypeResolversByJavaType.get(domainType.getJavaType());
                if (predicateTypeResolverMapByJavaType == null && domainType.getJavaType() != null) {
                    predicateTypeResolverMapByJavaType = new HashMap<>();
                    domainPredicateTypeResolversByJavaType.put(domainType.getJavaType(), predicateTypeResolverMapByJavaType);
                }

                for (Map.Entry<DomainPredicateType, DomainPredicateTypeResolver> resolverEntry : entry.getValue().entrySet()) {
                    if (domainType.getEnabledPredicates().contains(resolverEntry.getKey())) {
                        predicateTypeResolverMap.put(resolverEntry.getKey(), resolverEntry.getValue());
                        if (predicateTypeResolverMapByJavaType != null) {
                            predicateTypeResolverMapByJavaType.put(resolverEntry.getKey(), resolverEntry.getValue());
                        }
                    } else {
                        context.addError("A predicate type resolver for the type with the name '" + typeName + "' was registered for a non enabled predicate '" + resolverEntry.getKey() + "': " + resolverEntry.getValue());
                    }
                }
            }
        }
    }

    private void resolveDomainPredicateTypeResolversByJavaType(MetamodelBuildingContext context, Map<Class<?>, DomainType> domainTypesByJavaType, Map<String, Map<DomainPredicateType, DomainPredicateTypeResolver>> domainPredicateTypeResolvers, Map<Class<?>, Map<DomainPredicateType, DomainPredicateTypeResolver>> domainPredicateTypeResolversByJavaType) {
        for (Map.Entry<Class<?>, Map<DomainPredicateType, DomainPredicateTypeResolver>> entry : this.domainPredicateTypeResolversByJavaType.entrySet()) {
            Class<?> javaType = entry.getKey();
            DomainType domainType = domainTypesByJavaType.get(javaType);
            if (domainType == null) {
                context.addError("An operation type resolver was registered but no type with the java type '" + javaType + "' was found: " + entry.getValue());
            } else {
                Map<DomainPredicateType, DomainPredicateTypeResolver> predicateTypeResolverMap = domainPredicateTypeResolvers.get(domainType.getName());
                if (predicateTypeResolverMap == null) {
                    predicateTypeResolverMap = new HashMap<>(entry.getValue().size());
                    domainPredicateTypeResolvers.put(domainType.getName(), predicateTypeResolverMap);
                }

                Map<DomainPredicateType, DomainPredicateTypeResolver> predicateTypeResolverMapByJavaType = domainPredicateTypeResolversByJavaType.get(domainType.getJavaType());
                if (predicateTypeResolverMapByJavaType == null) {
                    predicateTypeResolverMapByJavaType = new HashMap<>();
                    domainPredicateTypeResolversByJavaType.put(domainType.getJavaType(), predicateTypeResolverMapByJavaType);
                }

                for (Map.Entry<DomainPredicateType, DomainPredicateTypeResolver> resolverEntry : entry.getValue().entrySet()) {
                    if (domainType.getEnabledPredicates().contains(resolverEntry.getKey())) {
                        predicateTypeResolverMap.put(resolverEntry.getKey(), resolverEntry.getValue());
                        predicateTypeResolverMapByJavaType.put(resolverEntry.getKey(), resolverEntry.getValue());
                    } else {
                        context.addError("An operation type resolver for the type with the java type '" + javaType + "' was registered for a non enabled operator '" + resolverEntry.getKey() + "': " + resolverEntry.getValue());
                    }
                }
            }
        }
    }
}
