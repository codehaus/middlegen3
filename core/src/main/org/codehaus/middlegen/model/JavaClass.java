package org.codehaus.middlegen.model;

import org.codehaus.rdbms.Relationship;

import java.util.Collection;

/**
 * Provides information about a java class to be generated.
 *
 * @author <a href="mailto:aslak.hellesoy at netcom.no">Aslak Helles&oslash;y</a>
 * @version $Revision: 1.2 $
 */
public interface JavaClass {
    String getJavaPackageName();

    JavaField getSimplePkClass();

    String getJavaQualifiedClassName();

    String getQualifiedClassName(String packageName, String className);

    String getJavaColumnSignature(Collection columns, int mode);

    String getJavaRelationSignature(Collection relationships, int mode);

    String getJavaColumnAndRelationSignature(Collection columns, Collection relations, int mode);

    String getVariableName(Relationship relationship);

    String getCapitalisedVariableName(Relationship relationship);

    String getJavaGetterName();

    String getJavaSetterName();

    String getJavaGetterName(Relationship relationship);

    String getJavaSetterName(Relationship relationship);

}
