package org.cytoscape.view.vizmap.gui.internal.task;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.VisualProperty;
import org.cytoscape.view.vizmap.VisualStyle;
import org.cytoscape.view.vizmap.gui.SelectedVisualStyleManager;
import org.cytoscape.view.vizmap.gui.internal.AbstractVizMapperPanel;
import org.cytoscape.view.vizmap.gui.internal.VizMapperProperty;
import org.cytoscape.view.vizmap.gui.internal.event.CellType;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

import com.l2fprod.common.propertysheet.Property;
import com.l2fprod.common.propertysheet.PropertySheetTable;
import com.l2fprod.common.propertysheet.PropertySheetTableModel;
import com.l2fprod.common.propertysheet.PropertySheetTableModel.Item;

public class DeleteMappingFunctionTask extends AbstractTask {
	
	private final PropertySheetTable table;
	private final SelectedVisualStyleManager manager;
	private final CyApplicationManager appManager;
	
	public DeleteMappingFunctionTask(final PropertySheetTable table, final SelectedVisualStyleManager manager, final CyApplicationManager appManager) {
		this.table = table;
		this.manager = manager;
		this.appManager = appManager;
	}

	@Override
	public void run(TaskMonitor monitor) throws Exception {
		int selectedRow = table.getSelectedRow();
		
		// If not selected, do nothing.
		if(selectedRow < 0)
			return;
		
		final Item value = (Item) table.getValueAt(selectedRow, 0);
		
		
		if(value.isProperty()) {
			final VizMapperProperty<?, ?, ?> prop = (VizMapperProperty<?, ?, ?>) value.getProperty();
			
			if(prop.getCellType() == CellType.VISUAL_PROPERTY_TYPE) {
				final VisualProperty<?> vp = (VisualProperty<?>) prop.getKey();
				removeMapping(manager.getCurrentVisualStyle(), vp);
				
				updatePropertySheet(prop, vp);
			}
		}
	}
	
	private void removeMapping(final VisualStyle style, final VisualProperty<?> vp) {
		style.removeVisualMappingFunction(vp);
		final CyNetworkView currentView = appManager.getCurrentNetworkView();
		style.apply(currentView);
		currentView.updateView();
	}
	
	private void updatePropertySheet(final VizMapperProperty<?, ?, ?> prop, final VisualProperty<?> vp) {
		
		final PropertySheetTableModel sheetModel = table.getSheetModel();
		final Property[] children = prop.getSubProperties();
		
		// Remove all children
		for(Property p: children)
			sheetModel.removeProperty(p);
		
		// Remove itself
		sheetModel.removeProperty(prop);
		
		// Create new unused prop
		final VizMapperProperty<VisualProperty<?>, String, ?> unuded = new VizMapperProperty<VisualProperty<?>, String, Object>(CellType.UNUSED, vp, String.class);
		unuded.setCategory(AbstractVizMapperPanel.CATEGORY_UNUSED);
		unuded.setDisplayName(vp.getDisplayName());
		unuded.setValue("Double-Click to create...");
		prop.setEditable(false);
		
		sheetModel.addProperty(unuded);
		table.repaint();
	}
	

}
