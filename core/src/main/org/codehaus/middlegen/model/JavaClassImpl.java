package org.codehaus.middlegen.model;

import org.codehaus.rdbms.Column;
import org.codehaus.rdbms.Relationship;
import org.codehaus.rdbms.Table;
import org.codehaus.rdbms.impl.Util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Aslak Helles&oslash;y
 * @version $Revision: 1.2 $
 */
public class JavaClassImpl implements JavaClass {
    public static final int TYPES = 0;
    public static final int VARIABLES = 1;
    public static final int TYPES_VARIABLES = 2;

    private Table table;
    private Map columnToJavaFieldMap = new HashMap();
    private GenericClass genericClass;

    public JavaClassImpl(Table table, GenericClass genericClass) {
        this.table = table;
        this.genericClass = genericClass;
    }

    public String getJavaPackageName() {
        int lastDot = genericClass.getClassName().lastIndexOf(".");
        return genericClass.getClassName().substring(0, lastDot - 1);
    }

    /**
     * Convenience method for the case when the pk is one column.
     * @return the class name mapping to the column, or null if it is compound.
     */
    public JavaField getSimplePkClass() {
        Column column = table.getPrimaryKeyColumn();
        if (column == null) {
            // compound
            return null;
        }
        return (JavaField) columnToJavaFieldMap.get(column);
    }

    public String getJavaQualifiedClassName() {
        return getQualifiedClassName(
                getJavaPackageName(),
                genericClass.getClassName()
        );
    }

    public String getQualifiedClassName(String packageName, String className) {
        String result;
        if ("".equals(packageName)) {
            result = className;
        } else {
            result = packageName + "." + className;
        }
        return result;
    }

    public String getJavaColumnSignature(Collection columns, int mode) {
        StringBuffer sb = new StringBuffer();
        for (Iterator i = columns.iterator(); i.hasNext();) {
            Column column = (Column) i.next();
            JavaFieldImpl javaField = null;
            try {
                javaField = (JavaFieldImpl) column;
            } catch (ClassCastException e) {
                throw new IllegalStateException("??????" + e.getMessage());
            }
            if (sb.length() != 0) {
                sb.append(", ");
            }
            String fieldType = javaField.getJavaType();
            String fieldName = javaField.getGenericField().getVariableName();
            switch (mode) {
                case TYPES:
                    sb.append(fieldType).append(" ");
                    break;
                case VARIABLES:
                    sb.append(fieldType).append(" ").append(fieldName);
                    break;
                case TYPES_VARIABLES:
                    sb.append(fieldName).append(" ");
            }
        }
        return sb.toString();
    }

    public String getJavaRelationSignature(Collection relationships, int mode) {
        StringBuffer sb = new StringBuffer();
        for (Iterator i = relationships.iterator(); i.hasNext();) {
            Relationship relationship = (Relationship) i.next();
            // GenericClass middlegenOrigin = (GenericClass) relationship.getOriginTable();
            JavaClass javaTarget = (JavaClass) relationship.getTableB();
            if (sb.length() != 0) {
                sb.append(", ");
            }
            String targetType = javaTarget.getJavaQualifiedClassName();
            String targetVariableName = relationship.getNameB();
            switch (mode) {
                case TYPES:
                    sb.append(targetType).append(" ");
                    break;
                case VARIABLES:
                    sb.append(targetType).append(" ").append(targetVariableName);
                    break;
                case TYPES_VARIABLES:
                    sb.append(targetVariableName).append(" ");
            }
        }
        return sb.toString();
    }

    public String getJavaColumnAndRelationSignature(Collection columns, Collection relations, int mode) {
        String columnSignature = getJavaColumnSignature(columns, mode);
        String relationSignature = getJavaRelationSignature(relations, mode);
        if (!columnSignature.equals("") && !relationSignature.equals("")) {
            // both are non-empty strings
            return columnSignature + ", " + relationSignature;
        } else {
            // at least one of them is an empty string. just concatenate
            return columnSignature + relationSignature;
        }
    }

    /**
     * Gets the variable name for a class based on the relationship.
     *
     * @param relationship
     * @return
     */
    public String getVariableName(Relationship relationship) {
        return genericClass.getVariableName() + relationship.getNameB();
    }

    public String getCapitalisedVariableName(Relationship relationship) {
        return Util.capitalise(getVariableName(relationship));
    }

    public String getJavaGetterName() {
        return "get" + genericClass.getCapitalisedVariableName();
    }

    public String getJavaSetterName() {
        return "set" + genericClass.getCapitalisedVariableName();
    }

    public String getJavaGetterName(Relationship relationship) {
        return "get" + getCapitalisedVariableName(relationship);
    }

    public String getJavaSetterName(Relationship relationship) {
        return "set" + getCapitalisedVariableName(relationship);
    }

//    protected void init() {
//      super.init();

    /*
     *  There are 3 ways to get the class name for a table. Attempts
     *  are done in the following order:
     *
     *  1) Look in prefs
     *  2) Use the singular name for the table (if specified)
     *  3) Use the DbNameConverter
     */
/*
      String prefsBaseClassName = getPrefsValue("base-class-name");

      String computedBaseClassName;
      if (getTableElement().getSingular() != null) {
         computedBaseClassName = getTableElement().getSingular();
         computedBaseClassName = Util.capitalise(computedBaseClassName);
      }
      else {
         computedBaseClassName = DbNameConverter.getInstance().tableNameToVariableName(getSqlName());
         computedBaseClassName = Util.singularise(computedBaseClassName);
      }
      String suffix = ((JavaPlugin)getPlugin()).getSuffix();
      computedBaseClassName += suffix;

      if (prefsBaseClassName != null && !prefsBaseClassName.equals(computedBaseClassName)) {
         _log.warn(
               "WARNING (" + getPlugin().getName() + "): " +
               "Your prefs file indicates that the base class name for table " + getSqlName() +
               " should be " + prefsBaseClassName + ", but according to your plugin settings " +
               "it should be " + computedBaseClassName + ". RdbmsTableFromJdbcMetadataProvider will use " + prefsBaseClassName + ". " +
               "If you want it to be the other way around, please edit or delete your prefs file, " +
               "or modify the name in the gui."
               );
      }
      if (prefsBaseClassName != null) {
         setBaseClassName(prefsBaseClassName);
      }
      else {
         setBaseClassName(computedBaseClassName);
      }

      // some sanity:
//		if (getPrimaryKeyColumn() != null && getSimplePkClassName() == null) {
//			throw new IllegalStateException("The simple PK class name shouldn't be null when a single pkColumn exists! PkColumn:" + getPrimaryKeyColumn() + ". " + org.codehaus.middlegen.RdbmsTableFromJdbcMetadataProvider.BUGREPORT);
//		}
    */
//    }

}

