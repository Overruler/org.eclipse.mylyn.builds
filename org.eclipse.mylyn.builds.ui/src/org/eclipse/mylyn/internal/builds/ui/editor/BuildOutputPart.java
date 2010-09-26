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

package org.eclipse.mylyn.internal.builds.ui.editor;

import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.mylyn.builds.core.IBuild;
import org.eclipse.mylyn.internal.builds.ui.actions.ShowBuildOutputAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;

/**
 * @author Steffen Pingel
 */
public class BuildOutputPart extends AbstractBuildEditorPart {

	private Hyperlink hyperlink;

	private ShowBuildOutputAction buildOutputAction;

	public BuildOutputPart() {
		super(ExpandableComposite.TITLE_BAR | ExpandableComposite.EXPANDED);
		setPartName("Output");
		this.span = 2;
	}

	@Override
	public void initialize(BuildEditorPage page) {
		super.initialize(page);

		buildOutputAction = new ShowBuildOutputAction();
		buildOutputAction.selectionChanged(new StructuredSelection(getInput(IBuild.class)));
	}

	@Override
	protected Control createContent(Composite parent, FormToolkit toolkit) {
		Composite composite = toolkit.createComposite(parent);
		composite.setLayout(new GridLayout(2, false));

		hyperlink = toolkit.createHyperlink(composite, "Show Output in Console", SWT.NONE);
		hyperlink.addHyperlinkListener(new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent event) {
				buildOutputAction.run();
			}
		});
		hyperlink.setEnabled(buildOutputAction.isEnabled());

		return composite;
	}

	@Override
	protected void fillToolBar(ToolBarManager toolBarManager) {
		super.fillToolBar(toolBarManager);

		toolBarManager.add(buildOutputAction);
	}

}
