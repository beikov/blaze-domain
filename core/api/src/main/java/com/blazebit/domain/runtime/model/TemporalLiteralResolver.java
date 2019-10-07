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

package com.blazebit.domain.runtime.model;

import java.time.Instant;

/**
 * A literal resolver for temporal values.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public interface TemporalLiteralResolver {

    /**
     * Resolves the given instant value to a resolved domain literal.
     *
     * @param domainModel The domain model
     * @param value The instant value
     * @return the resolved literal
     */
    ResolvedLiteral resolveTimestampLiteral(DomainModel domainModel, Instant value);

    /**
     * Resolves the given interval value to a resolved domain literal.
     *
     * @param domainModel The domain model
     * @param value The interval value
     * @return the resolved literal
     */
    ResolvedLiteral resolveIntervalLiteral(DomainModel domainModel, TemporalInterval value);
}
