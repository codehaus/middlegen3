package org.codehaus.rdbms.impl;

import org.codehaus.rdbms.ColumnFactory;
import org.codehaus.rdbms.MutableColumn;
import org.codehaus.rdbms.MutableTable;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.thoughtworks.proxy.toys.multicast.Multicasting;

/**
 * Implementation of ColumnFactory that returns a {@link java.lang.reflect.Proxy}
 * instance of a {@link org.codehaus.rdbms.Column}. This proxy can implement additional interfaces.
 * This is particularly handy if you want to "augment" the feature set of
 * the Column objects. The internal implementation uses
 * <a href="http://www.picocontainer.org/">PicoContainer<a/>.
 *
 * @see org.codehaus.rdbms.impl.PicoTableFactory
 * @author Aslak Helles&oslash;y
 * @version $Revision: 1.1 $
 */
public class PicoColumnFactory implements ColumnFactory {
    private final List mixins = new ArrayList();

    public PicoColumnFactory() {
        addColumnMixin(ColumnImpl.class);
    }

    public void addColumnMixin(Class clazz) {
        mixins.add(clazz);
    }

    public MutableColumn createColumn(MutableTable table, String sqlName) {
        MutablePicoContainer tablePico = new DefaultPicoContainer();
        tablePico.registerComponentInstance(table);

        MutablePicoContainer pico = new DefaultPicoContainer(tablePico);
        // Register the mixins.
        for (Iterator iterator = mixins.iterator(); iterator.hasNext();) {
            pico.registerComponentImplementation((Class) iterator.next());
        }

        List components = new ArrayList(pico.getComponentInstances());
        Object proxy = Multicasting.object(components.toArray());

        return (MutableColumn) proxy;
    }
}
