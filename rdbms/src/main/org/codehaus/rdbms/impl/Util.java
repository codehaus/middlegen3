package org.codehaus.rdbms.impl;

/**
 * Various static utility methods
 *
 * @author <a href="mailto:aslak.hellesoy at netcom.no">Aslak Hellesøy</a>
 * @version $Id: Util.java,v 1.1 2004/03/31 16:08:42 rinkrank Exp $
 */
public class Util {

	public static boolean equals(Object a, Object b) {
		if (a == null && b == null) {
			return true;
		}
		if (a != null && a.equals(b)) {
			return true;
		}
		return false;
	}


	public static String decapitalise(String s) {
		if (s.equals("")) {
			return "";
		}
		if (s.length() == 1) {
			return s.toLowerCase();
		}
		String result = s.substring(0, 1).toLowerCase() + s.substring(1);
		if (result.equals("class")) {
			// "class" is illegal becauseOf Object.getClass() clash
			result = "clazz";
		}
		return result;
	}

	public static String pluralise(String name) {
		String result = name;
		if (name.length() == 1) {
			// just append 's'
			result += 's';
		}
		else {
			if (!seemsPluralised(name)) {
				char secondLast = name.toLowerCase().charAt(name.length() - 2);
				if (!isVowel(secondLast) && name.toLowerCase().endsWith("y")) {
					// city, body etc --> cities, bodies
					result = name.substring(0, name.length() - 1) + "ies";
				}
				else if (name.toLowerCase().endsWith("ch")) {
					// switch --> switches
					result = name + "es";
				}
				else {
					result = name + "s";
				}
			}
		}
		return result;
	}

	public static String singularise(String name) {
		String result = name;
		if (seemsPluralised(name)) {
			if (name.toLowerCase().endsWith("ies")) {
				// cities --> city
				result = name.substring(0, name.length() - 3) + "y";
			}
			else if (name.toLowerCase().endsWith("es")) {
				// switches --> switch
				result = name.substring(0, name.length() - 2);
			}
			else if (name.toLowerCase().endsWith("s")) {
				// customers --> customer
				result = name.substring(0, name.length() - 1);
			}
		}
		return result;
	}

	public static String capitalise(String s) {
		if (s.equals("")) {
			return "";
		}
		if (s.length() == 1) {
			return s.toUpperCase();
		}
		else {
			String caps = s.substring(0, 1).toUpperCase();
			String rest = s.substring(1);
			return caps + rest;
		}
	}

	private final static boolean isVowel(char c) {
		boolean vowel = false;
		vowel |= c == 'a';
		vowel |= c == 'e';
		vowel |= c == 'i';
		vowel |= c == 'o';
		vowel |= c == 'u';
		vowel |= c == 'y';
		return vowel;
	}


	private static boolean seemsPluralised(String name) {
		name = name.toLowerCase();
		boolean pluralised = false;
		pluralised |= name.endsWith("es");
		pluralised |= name.endsWith("s");
		pluralised &= !name.endsWith("ss");
		return pluralised;
	}
}
