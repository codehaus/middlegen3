package org.codehaus.rdbms.jdbc;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * Connection Factory that uses plain JDBC to obtain a {@link Connection}.
 *
 * @author Aslak Helles&oslash;y
 */
public class JDBCConnectionFactory implements ConnectionFactory {
    private String driver;
    private String url;
    private String userid;
    private String password;
    private URLClassLoader driverClassLoader;

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Connection getConnection() throws org.codehaus.rdbms.jdbc.DatabaseException {
        try {
            Driver driver = (Driver) driverClassLoader.loadClass(this.driver).newInstance();
            DriverManager.registerDriver(new DynamicDriver(driver));
            Connection connection = DriverManager.getConnection(url, userid, password);
            return connection;
        } catch (ClassNotFoundException e) {
            throw new DatabaseException("Couldn't load JDBC driver " + driver + ". Make sure it's on your classpath.");
        } catch (InstantiationException e) {
            throw new DatabaseException("Couldn't instantiate JDBC driver " + driver + ". That's pretty bad news for your driver.");
        } catch (IllegalAccessException e) {
            throw new DatabaseException("Couldn't instantiate JDBC driver " + driver + ". That's pretty bad news for your driver.");
        } catch (SQLException e) {
            throw new DatabaseException("Couldn't connect to database: " + e.getMessage());
        }
    }

    public void setClasspath(String classpath) throws MalformedURLException {
        StringTokenizer cp = new StringTokenizer(classpath, ";:");
        URL[] urls = new URL[cp.countTokens()];
        for (int i = 0; i < urls.length; i++) {
            urls[i] = new File(cp.nextToken()).toURL();
        }
        driverClassLoader = new URLClassLoader(urls, getClass().getClassLoader());
    }

    // http://www.kfu.com/~nsayer/Java/dyn-jdbc.html
    private class DynamicDriver implements Driver {
        private Driver driver;

        DynamicDriver(Driver d) {
            this.driver = d;
        }

        public boolean acceptsURL(String u) throws SQLException {
            return driver.acceptsURL(u);
        }

        public Connection connect(String u, Properties p) throws SQLException {
            return driver.connect(u, p);
        }

        public int getMajorVersion() {
            return driver.getMajorVersion();
        }

        public int getMinorVersion() {
            return driver.getMinorVersion();
        }

        public DriverPropertyInfo[] getPropertyInfo(String u, Properties p) throws SQLException {
            return driver.getPropertyInfo(u, p);
        }

        public boolean jdbcCompliant() {
            return driver.jdbcCompliant();
        }
    }
}
