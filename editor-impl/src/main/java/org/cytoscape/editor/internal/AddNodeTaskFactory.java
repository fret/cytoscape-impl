package org.cytoscape.editor.internal;

/*
 * #%L
 * Cytoscape Editor Impl (editor-impl)
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2006 - 2013 The Cytoscape Consortium
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as 
 * published by the Free Software Foundation, either version 2.1 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-2.1.html>.
 * #L%
 */

//import java.awt.datatransfer.Transferable;
import java.awt.geom.Point2D;

import org.cytoscape.event.CyEventHelper;
import org.cytoscape.task.AbstractNetworkViewLocationTaskFactory;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.work.TaskIterator;

public class AddNodeTaskFactory extends AbstractNetworkViewLocationTaskFactory{

	private final CyEventHelper eh;
	private final VisualMappingManager vmm;
	
	public AddNodeTaskFactory(CyEventHelper eh, VisualMappingManager vmm) {
		this.eh = eh;
		this.vmm = vmm;
	}

	@Override
	public TaskIterator createTaskIterator(CyNetworkView networkView,
			Point2D javaPt, Point2D xformPt) {
		// TODO Auto-generated method stub
		return new TaskIterator(new AddNodeTask(vmm, networkView, xformPt, eh));
	}
}
