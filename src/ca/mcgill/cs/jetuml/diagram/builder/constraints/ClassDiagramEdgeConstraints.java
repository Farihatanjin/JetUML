/*******************************************************************************
 * JetUML - A desktop application for fast UML diagramming.
 *
 * Copyright (C) 2020 by McGill University.
 *     
 * See: https://github.com/prmr/JetUML
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses.
 *******************************************************************************/

package ca.mcgill.cs.jetuml.diagram.builder.constraints;
import static ca.mcgill.cs.jetuml.application.ApplicationResources.RESOURCES;

import java.util.HashMap;

import ca.mcgill.cs.jetuml.diagram.Diagram;
import ca.mcgill.cs.jetuml.diagram.Edge;
import ca.mcgill.cs.jetuml.diagram.Node;
import ca.mcgill.cs.jetuml.diagram.edges.AggregationEdge;
import ca.mcgill.cs.jetuml.diagram.edges.AssociationEdge;
import ca.mcgill.cs.jetuml.diagram.edges.DependencyEdge;
import ca.mcgill.cs.jetuml.diagram.edges.GeneralizationEdge;
import ca.mcgill.cs.jetuml.geom.Point;

/**
 * Methods to create edge addition constraints that only apply to
 * class diagrams. CSOFF:
 */
public final class ClassDiagramEdgeConstraints
{
	private ClassDiagramEdgeConstraints() {}

	/*
	 * Self edges are not allowed for Generalization edges.
	 */
	public static Constraint noSelfGeneralization()
	{
		return (Edge pEdge, Node pStart, Node pEnd, Point pStartPoint, Point pEndPoint, Diagram pDiagram)-> 
		{
			HashMap<String, Boolean> outputHashmap  = new HashMap<String,Boolean>();
			
			outputHashmap.put(RESOURCES.getString("classdiagram_edge_constraints.noSelfGeneralization.text"),!( pEdge.getClass() == GeneralizationEdge.class && pStart == pEnd ));
			
			return outputHashmap;
		};
	}
	
	/*
	 * Self edges are not allowed for Dependency edges.
	 */
	public static Constraint noSelfDependency()
	{
		return (Edge pEdge, Node pStart, Node pEnd, Point pStartPoint, Point pEndPoint, Diagram pDiagram) ->
		{
			HashMap<String, Boolean> outputHashmap  = new HashMap<String,Boolean>();
			
			outputHashmap.put(RESOURCES.getString("classdiagram_edge_constraints.noSelfDependency.text"),!( pEdge.getClass() == DependencyEdge.class && pStart == pEnd ));
			
			return outputHashmap;
		};
	}
	
	/*
	 * There can't be two edges of a given type, one in each direction, between two nodes.
	 */
	public static Constraint noDirectCycles(Class<? extends Edge> pEdgeType)
	{
		return (Edge pEdge, Node pStart, Node pEnd, Point pStartPoint, Point pEndPoint, Diagram pDiagram) ->
		{
			HashMap<String, Boolean> outputHashmap  = new HashMap<String,Boolean>();
			
			if( pEdge.getClass() != pEdgeType )
			{
				outputHashmap.put(RESOURCES.getString("classdiagram_edge_constraints.noDirectCycles.text"), true);
				return outputHashmap;
			}
			for( Edge edge : pStart.getDiagram().get().edgesConnectedTo(pStart) )
			{
				if( edge.getClass() == pEdgeType && edge.getEnd() == pStart && edge.getStart() == pEnd )
				{
					outputHashmap.put(RESOURCES.getString("classdiagram_edge_constraints.noDirectCycles.text"), false);
					return outputHashmap;
				}
			}
			outputHashmap.put(RESOURCES.getString("classdiagram_edge_constraints.noDirectCycles.text"), true);
			return outputHashmap;
		};
	}
	
	/*
	 * There can't be both an association and an aggregation edge between two nodes
	 */
	public static Constraint noCombinedAssociationAggregation()
	{
		return (Edge pEdge, Node pStart, Node pEnd, Point pStartPoint, Point pEndPoint, Diagram pDiagram) ->
		{
			HashMap<String, Boolean> outputHashmap  = new HashMap<String,Boolean>();
			
			if( pEdge.getClass() != AssociationEdge.class && pEdge.getClass() != AggregationEdge.class )
			{
				outputHashmap.put(RESOURCES.getString("classdiagram_edge_constraints.noCombinedAssociationAggregation.text"), true);
				return outputHashmap;
			}
			for( Edge edge : pStart.getDiagram().get().edgesConnectedTo(pStart) )
			{
				boolean targetEdge = edge.getClass() == AssociationEdge.class || edge.getClass() == AggregationEdge.class;
				boolean sameInOneDirection = edge.getStart() == pStart && edge.getEnd() == pEnd;
				boolean sameInOtherDirection = edge.getStart() == pEnd && edge.getEnd() == pStart;
				if( targetEdge && (sameInOneDirection || sameInOtherDirection))
				{
					outputHashmap.put(RESOURCES.getString("classdiagram_edge_constraints.noCombinedAssociationAggregation.text"), false);
					return outputHashmap;
				}
			}
			outputHashmap.put(RESOURCES.getString("classdiagram_edge_constraints.noCombinedAssociationAggregation.text"), true);
			return outputHashmap;
		};
	}
}