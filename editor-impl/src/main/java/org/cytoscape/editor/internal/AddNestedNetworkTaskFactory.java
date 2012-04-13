package org.cytoscape.editor.internal;

import org.cytoscape.group.CyGroupManager;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.CyNode;
import org.cytoscape.task.AbstractNodeViewTaskFactory;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.View;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.work.TaskIterator;

public class AddNestedNetworkTaskFactory extends AbstractNodeViewTaskFactory {
	final CyNetworkManager netMgr;
	final VisualMappingManager vmMgr;
	final CyGroupManager grMgr;

	public AddNestedNetworkTaskFactory(final CyNetworkManager netMgr,
									   final VisualMappingManager vmMgr,
									   final CyGroupManager grMgr) {
		this.netMgr = netMgr;
		this.vmMgr = vmMgr;
		this.grMgr = grMgr;
	}

	@Override
	public TaskIterator createTaskIterator(View<CyNode> nodeView, CyNetworkView networkView) {
		return new TaskIterator(new AddNestedNetworkTask(nodeView, networkView, netMgr, vmMgr, grMgr));
	}

	@Override
	public boolean isReady(View<CyNode> nodeView, CyNetworkView networkView) {
		if (!super.isReady(nodeView, networkView))
			return false;
		
		// Check if the network is registered.
		// Nodes with unregistered network pointers should be ignored because they are probably being used as something
		// else other than regular nested networks (e.g. groups).
		final CyNode node  = nodeView.getModel();
		final CyNetwork net = networkView.getModel();
		final CyNetwork netPointer = node.getNetworkPointer();
		
		return ( netPointer == null ) || ( netMgr.networkExists(netPointer.getSUID()) && !grMgr.isGroup(node, net) );
	}
}
