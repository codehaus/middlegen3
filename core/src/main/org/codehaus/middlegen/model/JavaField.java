package org.codehaus.middlegen.model;

/**
 * This is an abstraction of java field features.
 *
 * @author <a href="mailto:aslak.hellesoy at netcom.no">Aslak Helles&oslash;y</a>
 * @version $Revision: 1.2 $
 */
public interface JavaField {
    String getJavaType();

    String getGetterName();

    String getSetterName();

}
