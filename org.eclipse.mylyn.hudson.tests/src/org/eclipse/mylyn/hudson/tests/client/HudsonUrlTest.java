/*******************************************************************************
 * Copyright (c) 2014 Tasktop Technologies and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Tasktop Technologies - initial API and implementation
 *******************************************************************************/

package org.eclipse.mylyn.hudson.tests.client;

import java.io.UnsupportedEncodingException;
import java.util.Collections;

import junit.framework.TestCase;

import org.eclipse.mylyn.internal.hudson.core.client.HudsonUrl;

public class HudsonUrlTest extends TestCase {

	public void testQuotes() throws Exception {
		assertEquals(getExpectedUrl("%27example%27"), createHudsonUrl("example"));
		assertEquals(getExpectedUrl("%22exampleWithSingle%27Quote%22"), createHudsonUrl("exampleWithSingle'Quote"));
		assertEquals(getExpectedUrl("%27exampleWithDouble%22Quote%27"), createHudsonUrl("exampleWithDouble\"Quote"));
		try {
			createHudsonUrl("exampleWithSingle'AndDouble\"Quote");
			fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {// expected
		}

	}

	private String createHudsonUrl(String buildName) throws UnsupportedEncodingException {
		return HudsonUrl.create("http://hudson.com").depth(1).include("/hudson/job") //$NON-NLS-1$
				.match("name", Collections.singletonList(buildName)) //$NON-NLS-1$
				.exclude("/hudson/job/build") //$NON-NLS-1$
				.toUrl();
	}

	private static String getExpectedUrl(String quotedName) {
		return "http://hudson.com/api/xml?wrapper=hudson&depth=1&xpath=/hudson/job%5Bname%3D" + quotedName
				+ "%5D&exclude=/hudson/job/build";
	}
}
