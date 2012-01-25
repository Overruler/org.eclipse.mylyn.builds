/*******************************************************************************
 * Copyright (c) 2011 Tasktop Technologies and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Tasktop Technologies - initial API and implementation
 *******************************************************************************/

package org.eclipse.mylyn.builds.tests.operations;

import java.util.Collections;

import junit.framework.TestCase;

import org.eclipse.mylyn.builds.core.IBuild;
import org.eclipse.mylyn.builds.core.IBuildElement;
import org.eclipse.mylyn.builds.core.IBuildPlan;
import org.eclipse.mylyn.builds.internal.core.BuildServer;
import org.eclipse.mylyn.builds.internal.core.operations.RefreshOperation;
import org.eclipse.mylyn.builds.tests.support.BuildHarness;
import org.eclipse.mylyn.builds.tests.support.MockBuildServerBehaviour;
import org.eclipse.mylyn.commons.core.operations.OperationUtil;

/**
 * @author Steffen Pingel
 */
public class RefreshOperationTest extends TestCase {

	private BuildHarness harness;

	private MockBuildServerBehaviour behavior;

	private BuildServer server;

	private IBuildPlan plan;

	@Override
	protected void setUp() throws Exception {
		harness = new BuildHarness();

		server = harness.createServer();
		behavior = (MockBuildServerBehaviour) server.getBehaviour();

		plan = harness.createBuildPlan();

		IBuild existingBuild = harness.createBuild();
		plan.setLastBuild(existingBuild);
	}

	@Override
	protected void tearDown() throws Exception {
		harness.dispose();
	}

	public void testMergeExtendedAttributesBuild() throws Exception {
		IBuild newBuild = harness.createBuild();
		newBuild.getAttributes().put("key", "value2");

		behavior.setPlans(Collections.singletonList(plan));
		behavior.setBuilds(Collections.singletonList(newBuild));

		RefreshOperation operation = new RefreshOperation(harness.getOperationService(), harness.getModel(),
				Collections.singletonList((IBuildElement) plan));
		operation.doExecute(OperationUtil.convert(null));

		assertEquals(newBuild, plan.getLastBuild());
		assertEquals("value2", plan.getLastBuild().getAttributes().get("key"));
	}

	public void testMergeExtendedAttributesBuildPlan() throws Exception {
		IBuildPlan plan2 = harness.createBuildPlan();
		plan2.getAttributes().put("key", "value2");

		behavior.setPlans(Collections.singletonList(plan2));

		RefreshOperation operation = new RefreshOperation(harness.getOperationService(), harness.getModel(),
				Collections.singletonList((IBuildElement) plan));
		operation.doExecute(OperationUtil.convert(null));

		// check that attributes were merged from plan2 into plan
		assertEquals("value2", plan.getAttributes().get("key"));
	}

}