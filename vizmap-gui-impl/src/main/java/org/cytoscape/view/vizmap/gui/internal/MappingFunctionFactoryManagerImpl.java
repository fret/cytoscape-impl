package org.cytoscape.view.vizmap.gui.internal;

/*
 * #%L
 * Cytoscape VizMap GUI Impl (vizmap-gui-impl)
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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.cytoscape.view.vizmap.VisualMappingFunctionFactory;
import org.cytoscape.view.vizmap.gui.MappingFunctionFactoryManager;
import org.cytoscape.view.vizmap.gui.editor.EditorManager;
import org.cytoscape.view.vizmap.gui.internal.editor.propertyeditor.CyComboBoxPropertyEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MappingFunctionFactoryManagerImpl implements
		MappingFunctionFactoryManager {
	
	private static final Logger logger = LoggerFactory.getLogger(MappingFunctionFactoryManagerImpl.class);
	
	private final Map<Class<?>, VisualMappingFunctionFactory> factories;
	
	private final EditorManager editorManager;
	
	public MappingFunctionFactoryManagerImpl(final EditorManager editorManager) {
		this.editorManager = editorManager;
		factories = new HashMap<Class<?>, VisualMappingFunctionFactory>();
	}

	@Override
	public Collection<VisualMappingFunctionFactory> getFactories() {
		return factories.values();
	}
	
	
	public void addFactory(VisualMappingFunctionFactory factory, @SuppressWarnings("rawtypes") Map properties) {
		logger.debug("Got Mapping Factory: " + factory.toString());
		factories.put(factory.getMappingFunctionType(), factory);
		
		updateSelectorGUI();
	}

	
	public void removeFactory(VisualMappingFunctionFactory factory, @SuppressWarnings("rawtypes") Map properties) {
		logger.debug("************* Removing VM Function Factory ****************");
		factories.remove(factory.getMappingFunctionType());
		
		updateSelectorGUI();
	}
	
	private void updateSelectorGUI() {
//		final SortedSet<String> mappingNames = new TreeSet<String>();
//		for(final VisualMappingFunctionFactory factory: factories)
//			mappingNames.add(factory.toString());
		
		((CyComboBoxPropertyEditor)editorManager.getMappingFunctionSelector()).setAvailableValues(factories.values().toArray());
	}

	@Override
	public VisualMappingFunctionFactory getFactory(Class<?> mappingType) {
		return factories.get(mappingType);
	}

}
