package org.codehaus.rdbms.jdbc;

/**
 * @author Aslak Helles&oslash;y
 * @version $Revision: 1.1 $
 */
public class DatabaseInfo {
	private final String databaseProductName;
	private final String databaseProductVersion;
	private final String driverName;
	private final String driverVersion;

	public DatabaseInfo(
			String databaseProductName,
			String databaseProductVersion,
			String driverName,
			String driverVersion
			) {
		this.databaseProductName = databaseProductName;
		this.databaseProductVersion = databaseProductVersion;
		this.driverName = driverName;
		this.driverVersion = driverVersion;
	}

	public String getDatabaseProductName() {
		return databaseProductName;
	}

	public String getDatabaseProductVersion() {
		return databaseProductVersion;
	}

	public String getDriverName() {
		return driverName;
	}

	public String getDriverVersion() {
		return driverVersion;
	}
}
