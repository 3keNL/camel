/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.processor.aggregate.cassandra;

import com.datastax.oss.driver.api.core.CqlSession;

/**
 * Concrete implementation of {@link CassandraAggregationRepository} using 2 columns as primary key: name (partition
 * key) and key (clustering key).
 */
public class NamedCassandraAggregationRepository extends CassandraAggregationRepository {

    public NamedCassandraAggregationRepository() {
        setPKColumns("NAME", "KEY");
        setName("DEFAULT");
    }

    NamedCassandraAggregationRepository(CqlSession session, String name) {
        super(session);
        setPKColumns("NAME", "KEY");
        setName(name);
    }

    public String getName() {
        return (String) getPrefixPKValues()[0];
    }

    public final void setName(String name) {
        setPrefixPKValues(name);
    }

}
