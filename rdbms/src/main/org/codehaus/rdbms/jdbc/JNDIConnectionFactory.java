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

/*
 * Change log
 *
 */
package org.codehaus.rdbms.jdbc;

import javax.naming.CommunicationException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.NoInitialContextException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;

/**
 * Connection Factory that uses JNDI to obtain a {@link Connection}.
 *
 * @author Aslak Hellesøy
 */
public class JNDIConnectionFactory implements ConnectionFactory {

	private String initialContextFactory;
	private String providerURL;
	private String dataSourceJNDIName;

    public void setInitialContextFacory(String initialContextFactory) {
        this.initialContextFactory = initialContextFactory;
    }
    public void setProviderURL(String providerURL) {
        this.providerURL = providerURL;
    }
    public void setDataSourceJNDIName(String dataSourceJNDIName) {
        this.dataSourceJNDIName = dataSourceJNDIName;
    }

	public Connection getConnection() throws org.codehaus.rdbms.jdbc.DatabaseException {

        if (initialContextFactory == null) {
            throw new DatabaseException("initialContextFactory can't be null");
        }
        if (providerURL == null) {
            throw new DatabaseException("providerURL can't be null");
        }
        if (dataSourceJNDIName == null) {
            throw new DatabaseException("dataSourceJNDIName can't be null");
        }

		Context ctx = null;
		Hashtable ht = new Hashtable();
		ht.put(Context.INITIAL_CONTEXT_FACTORY, initialContextFactory);
		ht.put(Context.PROVIDER_URL, providerURL);

		try {
			ctx = new InitialContext(ht);
			// Use the context in your program
			DataSource ds = (DataSource)ctx.lookup(dataSourceJNDIName);
			Connection connection = ds.getConnection();
			return connection;
		} catch (NoInitialContextException e) {
			throw new DatabaseException("You should put your JNDI implementation classes on the system CLASSPATH:", e);
		} catch (CommunicationException e) {
			throw new DatabaseException("Is your JNDI server running?:", e);
		} catch (NamingException e) {
			throw new DatabaseException("Couldn't look up database using JNDI:", e);
		} catch (SQLException e) {
			// a failure occurred
			throw new DatabaseException("Couldn't get Connection:", e);
		} finally {
			try {
				ctx.close();
			} catch (Exception e) {
				// a failure occurred
			}
		}
	}
}
