package com.rvg;

import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.DoubleBorder;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.layout.RootLayoutArea;
import com.itextpdf.layout.renderer.DivRenderer;
import com.itextpdf.layout.renderer.DocumentRenderer;
import com.itextpdf.layout.renderer.IRenderer;
public class CustomDocumentRenderer extends DocumentRenderer {

	 public CustomDocumentRenderer(Document document) {
	        super(document);
	    }

	    protected LayoutArea updateCurrentArea(LayoutResult overflowResult) {
	        LayoutArea area = super.updateCurrentArea(overflowResult);
	        Rectangle newBBox = area.getBBox().clone();
	        float[] borderWidths = { 1.0F, 1.0F, 1.0F, 1.0F };
	        newBBox.applyMargins(borderWidths[0], borderWidths[1], borderWidths[2], borderWidths[3], false);
	        float width = 1.0F;
	        Color color = ColorConstants.ORANGE;
	        DoubleBorder doubleBorder = new DoubleBorder(color, width);
	        Div div = (Div)((Div)((Div)(new Div()).setWidth(newBBox.getWidth())).setHeight(newBBox.getHeight())).setBorder((Border)doubleBorder);
	        addChild((IRenderer)new DivRenderer(div));
	        float[] paddingWidths = { 10.0F, 10.0F, 10.0F, 10.0F };
	        newBBox.applyMargins(paddingWidths[0], paddingWidths[1], paddingWidths[2], paddingWidths[3], false);
	        return (LayoutArea)(this.currentArea = new RootLayoutArea(area.getPageNumber(), newBBox));
	    }
}
