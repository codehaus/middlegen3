package org.codehaus.rdbms.impl;

import junit.framework.TestCase;
import org.codehaus.rdbms.MutableTable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Aslak Helles&oslash;y
 * @version $Revision: 1.3 $
 */
public class PicoTableFactoryTestCase extends TestCase {
    public void testShouldCreatedTableWithCorrectName() {
        PicoTableFactory tableFactory = new PicoTableFactory();
        MutableTable foo = tableFactory.getTable("foo");
        assertEquals("foo", foo.getSqlName());
    }

    public void testShouldCacheInstances() {
        PicoTableFactory tableFactory = new PicoTableFactory();
        assertSame(tableFactory.getTable("foo"), tableFactory.getTable("foo"));
    }

    public void testShouldAddMixins() {
        PicoTableFactory tableFactory = new PicoTableFactory(new TableMixin[]{
            new TableMixin(ArrayList.class)
        });
        MutableTable foo = tableFactory.getTable("foo");
        assertTrue(foo instanceof List);
    }

}