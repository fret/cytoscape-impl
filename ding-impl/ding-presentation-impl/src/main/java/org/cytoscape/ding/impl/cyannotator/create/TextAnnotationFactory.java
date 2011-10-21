

package org.cytoscape.ding.impl.cyannotator.create;

import org.cytoscape.ding.impl.DGraphView;
import javax.swing.JFrame;
import java.util.Map;
import org.cytoscape.ding.impl.cyannotator.CyAnnotator;
import org.cytoscape.ding.impl.cyannotator.annotations.Annotation;
import org.cytoscape.ding.impl.cyannotator.annotations.TextAnnotation;

public class TextAnnotationFactory implements AnnotationFactory {

	public JFrame createAnnotationFrame(DGraphView view) {
		return new cTextAnnotation(view);
	}

	public Annotation createAnnotation(String type, CyAnnotator cyAnnotator, DGraphView view, Map<String, String> argMap) {
		if ( type.equals(TextAnnotation.NAME) ) 
			return new TextAnnotation(cyAnnotator, view,argMap);
		else 
			return null;
	}
}