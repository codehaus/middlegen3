package org.codehaus.rdbms.impl;

/**
 * This class is used for implied cross references that aren't
 * actually declared in the database.
 *
 * @author Aslak Hellesøy
 * @author Kyle Downey
 */
public class Crossref {
    private String name;
    private String fktable;
    private String pkcolumn;
    private String fkcolumn;

    public void setName(String name) {
        this.name = name;
    }

    public void setFktable(String fktable) {
        this.fktable = fktable;
    }

    public void setPkcolumn(String pkcolumn) {
        this.pkcolumn = pkcolumn;
    }

    public void setFkcolumn(String fkcolumn) {
        this.fkcolumn = fkcolumn;
    }

    public String getName() {
        return name;
    }

    public String getFktable() {
        return fktable;
    }

    public String getPkcolumn() {
        return pkcolumn;
    }

    public String getFkcolumn() {
        return fkcolumn;
    }

}
