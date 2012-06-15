package org.cytoscape.io.internal.write.xgmml;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.cytoscape.view.presentation.property.BasicVisualLexicon.*;

import java.io.UnsupportedEncodingException;

import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyIdentifiable;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.View;
import org.cytoscape.view.presentation.property.NodeShapeVisualProperty;
import org.cytoscape.view.vizmap.VisualStyle;
import org.junit.Before;
import org.junit.Test;

public class SessionXGMMLNetworkViewWriterTest extends AbstractXGMMLWriterTest {

	private VisualStyle style;
	
	@Before
	public void init(){
		super.init();
		
		style = mock(VisualStyle.class);
		when(style.getTitle()).thenReturn("My Style");
		when(vmMgr.getVisualStyle(any(CyNetworkView.class))).thenReturn(style);
	}
	
	@Test
	public void testRootNetworkGraph() throws UnsupportedEncodingException {
		write(view);
		assertEquals(""+view.getSUID(), evalString("/x:graph/@id"));
		assertEquals(""+view.getSUID(), evalString("/x:graph/@label")); // default value
		assertEquals("1", evalString("/x:graph/@cy:view"));
		assertEquals(""+view.getModel().getSUID(), evalString("/x:graph/@cy:networkId"));
		assertEquals(""+GenericXGMMLWriter.VERSION, evalString("/x:graph/@cy:documentVersion"));
	}
	
	@Test
	public void testVisualStyleAttribute() throws UnsupportedEncodingException {
		write(view);
		assertEquals("My Style", evalString("/x:graph/@cy:visualStyle"));
		assertEquals("1", evalString("/x:graph/@cy:view"));
		assertEquals(""+view.getModel().getSUID(), evalString("/x:graph/@cy:networkId"));
		assertEquals(""+GenericXGMMLWriter.VERSION, evalString("/x:graph/@cy:documentVersion"));
	}
	
	@Test
	public void testShouldHaveNoSubGraphs() throws UnsupportedEncodingException {
		write(view);
		assertEquals(1, evalNumber("count(//x:graph)"));
	}
	
	@Test
	public void testNumberOfNodeElements() {
		write(view);
		assertEquals(NODE_COUNT, evalNumber("count(/x:graph/x:node)"));
	}
	
	@Test
	public void testNumberOfEdgeElements() {
		write(view);
		// Writing edges is optional when they have no locked visual properties
		assertTrue(evalBoolean("count(/x:graph/x:edge) <= 2"));
	}
	
	@Test
	public void testNodeHasNoAttElements() {
		write(view);
		assertEquals(0, evalNumber("count(//x:node/x:att)"));
	}
	
	@Test
	public void testGraphGraphicsAtt() {
		view.setVisualProperty(NETWORK_TITLE, "Test Network View");
		view.setVisualProperty(NETWORK_WIDTH, 625d);
		view.setVisualProperty(NETWORK_HEIGHT, 472d);
		write(view);
		
		assertEquals("Test Network View", evalString("/x:graph/x:graphics/x:att[@name=\""+NETWORK_TITLE.getIdString()+"\"]/@value"));
		assertEquals("string", evalString("/x:graph/x:graphics/x:att[@name=\""+NETWORK_TITLE.getIdString()+"\"]/@type"));
		
		assertEquals(625, evalNumber("/x:graph/x:graphics/x:att[@name=\""+NETWORK_WIDTH.getIdString()+"\"]/@value"));
		assertEquals(472, evalNumber("/x:graph/x:graphics/x:att[@name=\""+NETWORK_HEIGHT.getIdString()+"\"]/@value"));
	}
	
	@Test
	public void testStandardNodeGraphics() {
		write(view);
		assertEquals(NODE_COUNT, evalNumber("count(//x:node/x:graphics/@x)"));
		assertEquals(NODE_COUNT, evalNumber("count(//x:node/x:graphics/@y)"));
		assertEquals(NODE_COUNT, evalNumber("count(//x:node/x:graphics/@z)"));
		assertEquals(NODE_COUNT, evalNumber("count(//x:node/x:graphics/@h)"));
		assertEquals(NODE_COUNT, evalNumber("count(//x:node/x:graphics/@w)"));
		assertEquals(NODE_COUNT, evalNumber("count(//x:node/x:graphics/@width)"));
		assertEquals(NODE_COUNT, evalNumber("count(//x:node/x:graphics/@fill)"));
		assertEquals(NODE_COUNT, evalNumber("count(//x:node/x:graphics/@type)"));
		
		// TODO: test values
	}
	
	@Test
	public void testNodeGraphicsAtt() {
		View<CyNode> nv = view.getNodeViews().iterator().next();
		nv.setLockedValue(NODE_SIZE, 122d);
		nv.setLockedValue(NODE_SHAPE, NodeShapeVisualProperty.OCTAGON);
		write(view);
		
		assertEquals("list", evalString("//x:node[@id="+nv.getSUID()+"]/x:graphics/x:att[@name=\"lockedVisualProperties\"]/@type"));
		assertEquals(2, evalNumber("count(//x:node[@id="+nv.getSUID()+"]/x:graphics/x:att[@name=\"lockedVisualProperties\"]/x:att)"));
		
		assertEquals(122, evalNumber("//x:node[@id="+nv.getSUID()+"]/x:graphics/x:att/x:att[@name=\""+NODE_SIZE.getIdString()+"\"]/@value"));
		assertEquals("OCTAGON", evalString("//x:node[@id="+nv.getSUID()+"]/x:graphics/x:att/x:att[@name=\""+NODE_SHAPE.getIdString()+"\"]/@value"));
	}
	
	@Test
	public void testEdgeGraphicsAtt() {
		View<CyEdge> ev = view.getEdgeViews().iterator().next();
		ev.setLockedValue(EDGE_TRANSPARENCY, 120);
		write(view);
		
		assertEquals("list", evalString("//x:edge[@id="+ev.getSUID()+"]/x:graphics/x:att[@name=\"lockedVisualProperties\"]/@type"));
		assertEquals(1, evalNumber("count(//x:edge[@id="+ev.getSUID()+"]/x:graphics/x:att[@name=\"lockedVisualProperties\"]/x:att)"));
		
		assertEquals(120, evalNumber("//x:edge[@id="+ev.getSUID()+"]/x:graphics/x:att/x:att[@name=\""+EDGE_TRANSPARENCY.getIdString()+"\"]/@value"));
	}
	
	@Test
	public void testEdgeHasNoAttElements() {
		write(view);
		assertEquals(0, evalNumber("count(//x:edge/x:att)"));
	}
	
	@Test
	public void testLabelEqualsIdByDefault() {
		write(view);
		// Assuming the title (network view) name attribute (node/edge views) is not set!
		assertEquals(""+view.getSUID(), evalString("/x:graph/@label"));
		for (View<CyNode> nv : view.getNodeViews())
			assertEquals(""+nv.getModel().getSUID(), evalString("//x:node[@id="+nv.getSUID()+"]/@label"));
	}
	
	@Test
	public void testLabelAttribute() {
		// Set name attributes first
		view.setVisualProperty(NETWORK_TITLE, "Test Network View");
		for (View<CyNode> nv : view.getNodeViews())
			net.getRow(nv.getModel()).set(CyNetwork.NAME, "NODE_"+nv.getSUID());
		write(view);
		// Now test
		assertEquals("Test Network View", evalString("/x:graph/@label"));
		for (View<CyNode> nv : view.getNodeViews())
			assertEquals("NODE_"+nv.getSUID(), evalString("//x:node[@id="+nv.getSUID()+"]/@label"));
	}

	// PRIVATE Methods:

	@Override
	protected GenericXGMMLWriter newWriter(CyIdentifiable netOrView) {
		SessionXGMMLNetworkViewWriter writer = null;
		
		if (netOrView instanceof CyNetworkView)
			writer = new SessionXGMMLNetworkViewWriter(out, renderingEngineMgr, (CyNetworkView) netOrView,
					unrecogVisPropMgr, netMgr, rootNetMgr, vmMgr);
		else
			throw new IllegalArgumentException("netOrView must be a CyNetworkView!");
			
		return writer;
	}
}