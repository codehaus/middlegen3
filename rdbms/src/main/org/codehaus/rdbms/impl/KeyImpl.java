/*
 * Copyright (c) 2001-2003, Aslak Hellesøy
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
 * - Neither the name of The RdbmsTableFromJdbcMetadataProvider Team nor the names of its
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
package org.codehaus.rdbms.impl;

import org.codehaus.rdbms.Column;
import org.codehaus.rdbms.Key;
import org.codehaus.rdbms.Table;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Baseclass for keys. Holds a ref to 1..* columns (1 in most cases).
 *
 * @author <a href="mailto:aslak.hellesoy at netcom.no">Aslak Helles&oslash;y</a>
 * @version $Revision: 1.1 $
 */
public abstract class KeyImpl implements Key {

    private final Table table;

   /**
    * The columns in the key
    */
   private final List _columns = new ArrayList();

    protected KeyImpl(Table table) {
        this.table = table;
    }

   public List getColumns() {
      return _columns;
   }


   public boolean isCompound() {
      return _columns.size() > 1;
   }


   public Table getTable() {
      return table;
   }


   /**
    * Adds a column
    *
    * @param column column to add
    */
   public void addColumn(Column column) {
       if(_columns.contains(column)) throw new IllegalStateException("Can't add same column twice.");
      _columns.add(column);
   }

    public boolean isNullable() {
        // if all our columns are unique or pk, we're unique
        for(Iterator i = getColumns().iterator(); i.hasNext();) {
            Column c = (Column) i.next();
            if( !c.isNullable() ) {
                return false;
            }
        }
        // they were all nullable
        return true;
    }
}
