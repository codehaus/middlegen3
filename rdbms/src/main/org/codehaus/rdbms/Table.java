/*
 * Copyright (c) 2001, The RdbmsTableFromJdbcMetadataProvider Group
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * - Neither the name of The RdbmsTableFromJdbcMetadataProvider Group nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package org.codehaus.rdbms;

import org.apache.commons.collections.Predicate;

import java.util.Collection;
import java.util.List;

/**
 * This class represents a table in a database.
 *
 * @author Aslak Helles&oslash;y
 * @version $Revision: 1.1 $
 */
public interface Table extends Comparable {

    String getSqlName();

    String getSqlName(boolean withSchemaPrefix);

    String getSchemaPrefixedSqlName();

    Collection getForeignKeys();

    Collection getForeignKeys(Predicate predicate);

    ForeignKey getForeignKey(String name);

    /**
     * @return a List of {@link Column}, sorted by name.
     */
    List getColumns();

    Collection getColumns(Predicate predicate);

    PrimaryKey getPrimaryKey();

    String getSchemaName();

    /**
     * Gets the column with the specified name
     *
     * @param sqlName the name of the column in the database
     * @return the column with the specified name
     */
    Column getColumn(String sqlName);

    /**
     * Returns the column that is a pk column. If zero or 2+ columns are pk
     * columns, null is returned.
     *
     * @return The single primary key column or null
     */
    Column getPrimaryKeyColumn();

    boolean isEnabled();
}
