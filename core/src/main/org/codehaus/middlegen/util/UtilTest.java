/*
 * Copyright (c) 2001, The RdbmsTableFromJdbcMetadataProvider Group
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * - Neither the name of The RdbmsTableFromJdbcMetadataProvider Group nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */

/*
 * Change log
 *
 */
package org.codehaus.middlegen.util;

import junit.framework.*;
import java.io.*;

import org.codehaus.rdbms.impl.Util;
import org.codehaus.rdbms.impl.Util;

/**
 * Describe what this class does
 *
 * @author Aslak Hellesøy
 * @created 22. mars 2002
 * @todo-javadoc Write javadocs
 */
public class UtilTest extends TestCase {

	/**
	 * Describe what the PrefsTest constructor does
	 *
	 * @param name Describe what the parameter does
	 * @todo-javadoc Write javadocs for constructor
	 * @todo-javadoc Write javadocs for method parameter
	 */
	public UtilTest(String name) {
		super(name);
	}


	/**
	 * A unit test for JUnit
	 *
	 * @exception java.lang.Exception Describe the exception
	 * @todo-javadoc Write javadocs for exception
	 */
	public void testPluraliseCountry() throws Exception {
		assertEquals("countries", Util.pluralise("country"));
	}


	/**
	 * A unit test for JUnit
	 *
	 * @exception java.lang.Exception Describe the exception
	 * @todo-javadoc Write javadocs for exception
	 */
	public void testSingulariseCountries() throws Exception {
		assertEquals("country", Util.singularise("countries"));
	}


	/**
	 * A unit test for JUnit
	 *
	 * @exception java.lang.Exception Describe the exception
	 * @todo-javadoc Write javadocs for exception
	 */
	public void testPluraliseCountries() throws Exception {
		assertEquals("countries", Util.pluralise("countries"));
	}


	/**
	 * A unit test for JUnit
	 *
	 * @exception java.lang.Exception Describe the exception
	 * @todo-javadoc Write javadocs for exception
	 */
	public void testSingulariseCountry() throws Exception {
		assertEquals("country", Util.singularise("country"));
	}


	/**
	 * A unit test for JUnit
	 *
	 * @exception java.lang.Exception Describe the exception
	 * @todo-javadoc Write javadocs for exception
	 */
	public void testPluraliseToy() throws Exception {
		assertEquals("toys", Util.pluralise("toy"));
	}


	/**
	 * A unit test for JUnit
	 *
	 * @exception java.lang.Exception Describe the exception
	 * @todo-javadoc Write javadocs for exception
	 */
	public void testSingulariseToys() throws Exception {
		assertEquals("toy", Util.singularise("toys"));
	}


	/**
	 * A unit test for JUnit
	 *
	 * @exception java.lang.Exception Describe the exception
	 * @todo-javadoc Write javadocs for exception
	 */
	public void testPluraliseToys() throws Exception {
		assertEquals("toys", Util.pluralise("toys"));
	}


	/**
	 * A unit test for JUnit
	 *
	 * @exception java.lang.Exception Describe the exception
	 * @todo-javadoc Write javadocs for exception
	 */
	public void testSingulariseToy() throws Exception {
		assertEquals("toy", Util.singularise("toy"));
	}


	/**
	 * A unit test for JUnit
	 *
	 * @exception java.lang.Exception Describe the exception
	 * @todo-javadoc Write javadocs for exception
	 */
	public void testSingulariseSwitches() throws Exception {
		assertEquals("switch", Util.singularise("switches"));
	}


	/**
	 * A unit test for JUnit
	 *
	 * @exception java.lang.Exception Describe the exception
	 * @todo-javadoc Write javadocs for exception
	 */
	public void testPluraliseSwitch() throws Exception {
		assertEquals("switches", Util.pluralise("switch"));
	}


	/**
	 * A unit test for JUnit
	 *
	 * @exception java.lang.Exception Describe the exception
	 * @todo-javadoc Write javadocs for exception
	 */
	public void testSingulariseSwitch() throws Exception {
		assertEquals("switch", Util.singularise("switch"));
	}


	/**
	 * A unit test for JUnit
	 *
	 * @exception java.lang.Exception Describe the exception
	 * @todo-javadoc Write javadocs for exception
	 */
	public void testPluraliseSwitches() throws Exception {
		assertEquals("switches", Util.pluralise("switches"));
	}
}
