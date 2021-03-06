/*******************************************************************************
 * This file is protected by Copyright. 
 * Please refer to the COPYRIGHT file distributed with this source distribution.
 *
 * This file is part of REDHAWK IDE.
 *
 * All rights reserved.  This program and the accompanying materials are made available under 
 * the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package gov.redhawk.ide.graphiti.ui.properties;

import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.platform.AbstractPropertySectionFilter;
import org.eclipse.jface.viewers.IFilter;

import gov.redhawk.core.graphiti.ui.properties.BusinessObjectFilter;
import gov.redhawk.core.graphiti.ui.properties.CompoundFilter;
import gov.redhawk.core.graphiti.ui.util.DUtil;
import mil.jpeojtrs.sca.partitioning.ConnectInterface;

public class ConnectionPropertiesFilter extends CompoundFilter {

	public ConnectionPropertiesFilter() {
		super(CompoundFilter.BooleanOperator.FILTER_AND);

		IFilter boFilter = new BusinessObjectFilter(ConnectInterface.class);
		IFilter designTimeFilter = new AbstractPropertySectionFilter() {
			@Override
			protected boolean accept(PictogramElement pictogramElement) {
				Diagram diagram = Graphiti.getPeService().getDiagramForPictogramElement(pictogramElement);
				return !DUtil.isDiagramRuntime(diagram);
			}
		};

		addFilter(boFilter);
		addFilter(designTimeFilter);
	}

}
