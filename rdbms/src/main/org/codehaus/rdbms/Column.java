package org.codehaus.rdbms;

/**
 * A Column represents a column in a table.
 *
 * @author <a href="mailto:aslak.hellesoy at netcom.no">Aslak Hellesøy</a>
 * @version $Id: Column.java,v 1.1 2004/03/31 16:08:41 rinkrank Exp $
 */
public interface Column extends Comparable {
   public Table getTable();
   public int getSqlType();
   public String getSqlTypeName();
   public String getSqlName();
   public int getSqlSize();
   public int getDecimalDigits();
   public boolean isPrimaryKey();
   public boolean isForeignKey();
   public boolean isNullable();
   public boolean isIndexed();
   public boolean isUnique();
   public String getDefaultValue();
}

