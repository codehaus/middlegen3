package org.codehaus.rdbms.impl;

import org.codehaus.rdbms.Column;
import org.codehaus.rdbms.ForeignKey;
import org.codehaus.rdbms.PrimaryKey;
import org.codehaus.rdbms.Table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author Aslak Helles&oslash;y
 * @version $Revision: 1.1 $
 */
public class ForeignKeyImpl extends KeyImpl implements ForeignKey {
    private final Collection references = new ArrayList();
    private final String name;
    private final PrimaryKeyImpl primaryKey;
    private final DatabaseNameConverter databaseNameConverter;

    private boolean isTargetNavigable = true;
    private boolean isOriginNavigable = true;
    private String nameA;
    private String nameB;

    public ForeignKeyImpl(Table table, String name, PrimaryKeyImpl primaryKey, DatabaseNameConverter databaseNameConverter) {
        super(table);
        this.name = name;
        this.primaryKey = primaryKey;
        this.databaseNameConverter = databaseNameConverter;
        this.primaryKey.addForeignKey(this);
    }

    /**
     * @return the logical name of the key
     */
    public String getName() {
        return name;
    }

    public Collection getReferences() {
        return references;
    }

    public PrimaryKey getPrimaryKey() {
        return primaryKey;
    }

    // TODO pico out to a separate class, just like JavaTable and JavaField?
    public String getNameA() {
        if (nameA == null) {
            String name = Util.decapitalise(databaseNameConverter.tableNameToVariableName(getTable().getSqlName()));
            if (isManyA()) {
                name = Util.pluralise(name);
            } else {
                name = Util.singularise(name);
            }
            name += getSuffix();
            setNameA(name);
        }
        return nameA;
    }

    public String getNameB() {
        if (nameB == null) {
            String name = Util.decapitalise(databaseNameConverter.tableNameToVariableName(getPrimaryKey().getTable().getSqlName()));
            name = Util.singularise(name);
            name += getSuffix();
            setNameB(name);
        }
        return nameB;
    }

    public void setNameA(String nameA) {
        this.nameA = nameA;
    }

    public void setNameB(String nameB) {
        this.nameB = nameB;
    }

    /**
     * Gets a nice name for a relationship. Tries to pluralise properly
     * and also adds unique suffixes if there are more than one relation between
     * the two tables.
     *
     * @return
     */
    private String getSuffix() {
        String result = "";
        if (hasCompetitor()) {
            // We must invent a unique name.
            result += "By";
            boolean and = false;
            for (Iterator i = getColumns().iterator(); i.hasNext();) {
                if (and) {
                    result += "And";
                }
                Column column = (Column) i.next();
                result += Util.capitalise(databaseNameConverter.columnNameToVariableName(column.getSqlName()));
                and = true;
            }
        }
        return result;
    }

    /**
     * Returns true if there are no other foreign keys on our table referencing
     * our primary key.
     *
     * @return true if we have no competitors.
     */
    private boolean hasCompetitor() {
        boolean result = false;
        // loop over all our table's foreign key and see if there is another
        // referencing the same table.
        for (Iterator fks = getTable().getForeignKeys().iterator(); fks.hasNext();) {
            ForeignKey fk = (ForeignKey) fks.next();
            if (fk != this && fk.getPrimaryKey() == getPrimaryKey()) {
                // We have a competitor!
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * Adds a pair of columns
     *
     * @param fkColumn
     * @param pkColumn
     */
    public void addReference(Column fkColumn, Column pkColumn) {
        if (primaryKey.getColumns().contains(pkColumn)) {
            //throw new IllegalArgumentException(pkColumn.getSqlName() + " is not a pk column.");
        }
        addColumn(fkColumn);
        ReferenceImpl reference = new ReferenceImpl(fkColumn, pkColumn);
        references.add(reference);
    }

    public void setNavigableB(boolean flag) {
        isTargetNavigable = flag;
    }

    public void setNavigableA(boolean flag) {
        isOriginNavigable = flag;
    }

    public boolean isNavigableB() {
        return isTargetNavigable;
    }

    public boolean isNavigableA() {
        return isOriginNavigable;
    }

    public boolean isManyB() {
        // always one
        return false;
    }

    public boolean isManyA() {
        return !isUnique();
    }

    /*
    public void setOriginMany(boolean isOriginMany) throws DatabaseException {
        if( isUnique() ) {
            throw new DatabaseException("Can't change the cardinality of a unique foreign key.");
        }
        _isOriginMany = isOriginMany;
    }
    */

    public Table getTableA() {
        return getTable();
    }

    public Table getTableB() {
        return getPrimaryKey().getTable();
    }

    public boolean isUnique() {
        // If all our columns are unique or pk, we're unique
        for (Iterator i = getColumns().iterator(); i.hasNext();) {
            Column c = (Column) i.next();
            if (!(c.isUnique() || c.isPrimaryKey())) {
                // System.out.println("Not unique:" + c.getSqlName() + ":" + c.isUnique() + ":" + c.isPk() );
                return false;
            }
            // all columns are unique. if we represent the whole pk, we're unique.
            int fkSize = getColumns().size();
            int pkSize = getTable().getPrimaryKey().getColumns().size();
            boolean equalFkAndPkSize = fkSize == pkSize;
            if (equalFkAndPkSize) {
                // System.out.println( "Equal Size" );
            } else {
                // System.out.println("Not same number in fk as in pk: " + getTable() + ":" + getTable().getPrimaryKey().getColumns().size() + ":" + getColumns().size() );
            }
            return equalFkAndPkSize;
        }
        return false;
    }

    public int compareTo(Object o) {
        ForeignKeyImpl other = (ForeignKeyImpl) o;
        return getName().compareTo(other.getName());
    }

    public String toString() {
        return getColumns().toString() + "-" + getPrimaryKey().toString();
    }
}
