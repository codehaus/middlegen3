package org.codehaus.rdbms.impl;

/**
 * @author Aslak Helles&oslash;y
 * @version $Revision: 1.3 $
 */
public class TableMixin {
    private final Class mixinClass;

    public TableMixin(Class mixinClass) {
        this.mixinClass = mixinClass;
    }

    public Class getMixinClass() {
        return mixinClass;
    }
}