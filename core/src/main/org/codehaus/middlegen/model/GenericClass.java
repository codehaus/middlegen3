package org.codehaus.middlegen.model;



/**
 *
 * @author <a href="mailto:aslak.hellesoy at bekk.no">Aslak Helles&oslash;y</a>
 * @version $Revision: 1.2 $
 */
public interface GenericClass {
    String getClassName();

    String getVariableName();

    String getCapitalisedVariableName();

    String getPlural();

    void setPlural( String plural );

    String getSingular();

    void setSingular( String singular );

    void setVariableName( String variableName );

    void setClassName( String className );
}
