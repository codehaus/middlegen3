package org.codehaus.rdbms;

/**
 * @author Aslak Helles&oslash;y
 * @version $Revision: 1.1 $
 */
public interface Relationship extends Comparable {
    Table getTableA();
    Table getTableB();
    boolean isNavigableA();
    boolean isNavigableB();
    void setNavigableA(boolean flag);
    void setNavigableB(boolean flag);
    boolean isManyA();
    boolean isManyB();
    String getNameA();
    String getNameB();
    void setNameA(String nameA);
    void setNameB(String nameB);
}
