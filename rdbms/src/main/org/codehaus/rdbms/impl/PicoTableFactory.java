package org.codehaus.rdbms.impl;

import com.thoughtworks.proxy.toys.multicast.Multicasting;
import org.codehaus.rdbms.MutableTable;
import org.codehaus.rdbms.Table;
import org.codehaus.rdbms.TableFactory;
import org.picocontainer.defaults.DefaultPicoContainer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of TableFactory that returns a {@link java.lang.reflect.Proxy}
 * instance of a {@link org.codehaus.rdbms.Table}. This proxy can implement additional interfaces.
 * This is particularly handy if you want to "augment" the feature set of
 * the Table objects. The internal implementation uses
 * <a href="http://www.picocontainer.org/">PicoContainer<a/>.
 *
 * @author Aslak Helles&oslash;y
 * @version $Revision: 1.1 $
 * @see org.codehaus.rdbms.impl.PicoColumnFactory
 */
public class PicoTableFactory implements TableFactory {
    private final TableMixin[] mixinClasses;
    private final Map tables = new HashMap();

    public PicoTableFactory(TableMixin[] mixinClasses) {
        this.mixinClasses = mixinClasses;
    }

    public PicoTableFactory() {
        this(new TableMixin[0]);
    }

    private MutableTable createTable() {
        DefaultPicoContainer pico = new DefaultPicoContainer();

        // Register the basic components
        pico.registerComponentInstance(this);
        pico.registerComponentImplementation(TableImpl.class);

        // Register the mixin classes.
        for (int i = 0; i < mixinClasses.length; i++) {
            pico.registerComponentImplementation(mixinClasses[i].getMixinClass());
        }

        Object proxy = Multicasting.object(pico.getComponentInstances().toArray());
        return (MutableTable) proxy;
    }

    public MutableTable createTable(String sqlName) {
        MutableTable result = createTable();
        result.setSqlName(sqlName);
        tables.put(sqlName, result);
        return result;
    }

    public MutableTable getTable(String sqlName) {
        if (sqlName == null) {
            throw new NullPointerException("sqlName can't be null");
        }
        MutableTable result = (MutableTable) tables.get(sqlName);
        return result;
    }

    public List getTables() {
        List result = new ArrayList(tables.values());
        Collections.sort(result, new Comparator() {
            public int compare(Object o1, Object o2) {
                Table t1 = (Table) o1;
                Table t2 = (Table) o2;
                return t1.getSchemaPrefixedSqlName().compareTo(t2.getSchemaPrefixedSqlName());
            }
        });
        return result;
    }
}
