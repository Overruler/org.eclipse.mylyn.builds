/*******************************************************************************
 * Copyright (c) 2010 Tasktop Technologies and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Tasktop Technologies - initial API and implementation
 *******************************************************************************/

package org.eclipse.mylyn.internal.builds.core.operations;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.mylyn.builds.core.util.ProgressUtil;
import org.eclipse.mylyn.commons.core.DelegatingProgressMonitor;
import org.eclipse.mylyn.commons.core.IDelegatingProgressMonitor;
import org.eclipse.mylyn.commons.core.IOperationMonitor;
import org.eclipse.mylyn.internal.builds.core.util.BuildsConstants;

/**
 * @author Steffen Pingel
 */
public abstract class AbstractBuildOperation extends Job {

	IStatus status;

	protected final IDelegatingProgressMonitor monitor;

	public AbstractBuildOperation(String name) {
		super(name);
		this.monitor = new DelegatingProgressMonitor();
	}

	@Override
	public boolean belongsTo(Object family) {
		return family == BuildsConstants.JOB_FAMILY;
	}

	public IStatus getStatus() {
		return status;
	}

	protected void setStatus(IStatus status) {
		this.status = status;
	}

	@Override
	public IStatus run(IProgressMonitor jobMonitor) {
		try {
			monitor.setCanceled(false);
			monitor.attach(jobMonitor);
			try {
				return doExecute(ProgressUtil.convert(monitor));
			} catch (OperationCanceledException e) {
				return Status.CANCEL_STATUS;
			} finally {
				monitor.done();
			}
		} finally {
			monitor.detach(jobMonitor);
		}
	}

	protected abstract IStatus doExecute(IOperationMonitor convert);

	/**
	 * @since 3.3
	 */
	public IDelegatingProgressMonitor getMonitor() {
		return monitor;
	}

}
