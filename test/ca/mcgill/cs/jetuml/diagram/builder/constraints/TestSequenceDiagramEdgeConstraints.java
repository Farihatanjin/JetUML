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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.mcgill.cs.jetuml.JavaFXLoader;
import ca.mcgill.cs.jetuml.diagram.Diagram;
import ca.mcgill.cs.jetuml.diagram.DiagramType;
import ca.mcgill.cs.jetuml.diagram.edges.CallEdge;
import ca.mcgill.cs.jetuml.diagram.edges.ReturnEdge;
import ca.mcgill.cs.jetuml.diagram.nodes.CallNode;
import ca.mcgill.cs.jetuml.diagram.nodes.ImplicitParameterNode;
import ca.mcgill.cs.jetuml.geom.Point;

public class TestSequenceDiagramEdgeConstraints
{
	private Diagram aDiagram;
	private ImplicitParameterNode aParameter1;
	private ImplicitParameterNode aParameter2;
	private CallNode aCallNode1;
	private CallNode aCallNode2;
	private CallNode aCallNode3;
	private CallEdge aCallEdge;
	private ReturnEdge aReturnEdge;
	private Point aPoint;

	@BeforeAll
	public static void setupClass()
	{
		JavaFXLoader.load();
	}
	
	@BeforeEach
	public void setUp()
	{
		aDiagram = new Diagram(DiagramType.SEQUENCE);
		aParameter1 = new ImplicitParameterNode();
		aParameter2 = new ImplicitParameterNode();
		aCallNode1 = new CallNode();
		aCallNode2 = new CallNode();	
		aCallNode3 = new CallNode();
		aCallEdge = new CallEdge();
		aReturnEdge = new ReturnEdge();
		aPoint = new Point(0,0);
	}
	
	private void createDiagram()
	{
		aDiagram.addRootNode(aParameter1);
		aDiagram.addRootNode(aParameter2);
		aParameter1.addChild(aCallNode1);
		aParameter1.addChild(aCallNode3);
		aParameter2.addChild(aCallNode2);
	}
	
	@Test
	public void testNoEdgeFromParameterTopNotParameterNode()
	{
		createDiagram();
		assertEquals("No edge is allowed to start in a parameter node.",(String)SequenceDiagramEdgeConstraints.noEdgesFromParameterTop().satisfied(aCallEdge, aCallNode1, aCallNode1, aPoint, aPoint, aDiagram).keySet().toArray()[0]);
		assertTrue((boolean)SequenceDiagramEdgeConstraints.noEdgesFromParameterTop().satisfied(aCallEdge, aCallNode1, aCallNode1, aPoint, aPoint, aDiagram).values().toArray()[0]);
	}
	
	@Test
	public void testNoEdgeFromParameterTopParameterFalse()
	{
		createDiagram();
		assertEquals("No edge is allowed to start in a parameter node.",(String)SequenceDiagramEdgeConstraints.noEdgesFromParameterTop().satisfied(aCallEdge, aParameter1, aParameter1,new Point(5,5),aPoint, aDiagram).keySet().toArray()[0]);
		assertFalse((boolean)SequenceDiagramEdgeConstraints.noEdgesFromParameterTop().satisfied(aCallEdge, aParameter1, aParameter1,new Point(5,5),aPoint, aDiagram).values().toArray()[0]);
	}
	
	@Test
	public void testNoEdgeFromParameterTopParameterTrue()
	{
		createDiagram();
		assertEquals("No edge is allowed to start in a parameter node.",(String)SequenceDiagramEdgeConstraints.noEdgesFromParameterTop().satisfied(aCallEdge, aParameter1, aParameter1, new Point(40,65), aPoint, aDiagram).keySet().toArray()[0]);
		assertTrue((boolean)SequenceDiagramEdgeConstraints.noEdgesFromParameterTop().satisfied(aCallEdge, aParameter1, aParameter1, new Point(40,65), aPoint, aDiagram).values().toArray()[0]);
	}
	
	@Test
	public void testreturnEdgeNotReturnEdge()
	{
		createDiagram();
		assertEquals("For a return edge, the end node has to be the caller, and return edges on self-calls are not allowed.",(String)SequenceDiagramEdgeConstraints.returnEdge().satisfied(aCallEdge, aCallNode1, aCallNode2, aPoint, aPoint, aDiagram).keySet().toArray()[0]);
		assertTrue((boolean)SequenceDiagramEdgeConstraints.returnEdge().satisfied(aCallEdge, aCallNode1, aCallNode2, aPoint, aPoint, aDiagram).values().toArray()[0]);
	}
	
	@Test
	public void testreturnEdgeIncompatibleStart()
	{
		createDiagram();
		assertEquals("For a return edge, the end node has to be the caller, and return edges on self-calls are not allowed.",(String)SequenceDiagramEdgeConstraints.returnEdge().satisfied(aReturnEdge, aParameter1, aCallNode2, aPoint, aPoint, aDiagram).keySet().toArray()[0]);
		assertFalse((boolean)SequenceDiagramEdgeConstraints.returnEdge().satisfied(aReturnEdge, aParameter1, aCallNode2, aPoint, aPoint, aDiagram).values().toArray()[0]);
	}
	
	@Test
	public void testreturnEdgeIncompatibleEnd()
	{
		createDiagram();
		assertEquals("For a return edge, the end node has to be the caller, and return edges on self-calls are not allowed.",(String)SequenceDiagramEdgeConstraints.returnEdge().satisfied(aReturnEdge, aCallNode1, aParameter2, aPoint, aPoint, aDiagram).keySet().toArray()[0]);
		assertFalse((boolean)SequenceDiagramEdgeConstraints.returnEdge().satisfied(aReturnEdge, aCallNode1, aParameter2, aPoint, aPoint, aDiagram).values().toArray()[0]);
	}
	
	@Test
	public void testreturnEdgeEndNoCaller()
	{
		createDiagram();
		assertEquals("For a return edge, the end node has to be the caller, and return edges on self-calls are not allowed.",(String)SequenceDiagramEdgeConstraints.returnEdge().satisfied(aReturnEdge, aCallNode1, aCallNode2, aPoint, aPoint, aDiagram).keySet().toArray()[0]);
		assertFalse((boolean)SequenceDiagramEdgeConstraints.returnEdge().satisfied(aReturnEdge, aCallNode1, aCallNode2, aPoint, aPoint, aDiagram).values().toArray()[0]);
	}
	
	@Test
	public void testreturnEdgeEndNotCaller()
	{
		createDiagram();
		aCallEdge.connect(aCallNode1, aCallNode2, aDiagram);
		aDiagram.addEdge(aCallEdge);
		assertEquals("For a return edge, the end node has to be the caller, and return edges on self-calls are not allowed.",(String)SequenceDiagramEdgeConstraints.returnEdge().satisfied(aReturnEdge, aCallNode2, aCallNode3, aPoint, aPoint, aDiagram).keySet().toArray()[0]);
		assertFalse((boolean)SequenceDiagramEdgeConstraints.returnEdge().satisfied(aReturnEdge, aCallNode2, aCallNode3, aPoint, aPoint, aDiagram).values().toArray()[0]);
	}
	
	@Test
	public void testreturnEdgeSelfCaller()
	{
		createDiagram();
		aCallEdge.connect(aCallNode1, aCallNode3, aDiagram);
		aDiagram.addEdge(aCallEdge);
		assertEquals("For a return edge, the end node has to be the caller, and return edges on self-calls are not allowed.",(String)SequenceDiagramEdgeConstraints.returnEdge().satisfied(aReturnEdge, aCallNode3, aCallNode1, aPoint, aPoint, aDiagram).keySet().toArray()[0]);
		assertFalse((boolean)SequenceDiagramEdgeConstraints.returnEdge().satisfied(aReturnEdge, aCallNode3, aCallNode1, aPoint, aPoint, aDiagram).values().toArray()[0]);
	}
	
	@Test
	public void testreturnEdgeValid()
	{
		createDiagram();
		aCallEdge.connect(aCallNode1, aCallNode2, aDiagram);
		aDiagram.addEdge(aCallEdge);
		assertEquals("For a return edge, the end node has to be the caller, and return edges on self-calls are not allowed.",(String)SequenceDiagramEdgeConstraints.returnEdge().satisfied(aReturnEdge, aCallNode2, aCallNode1, aPoint, aPoint, aDiagram).keySet().toArray()[0]);
		assertTrue((boolean)SequenceDiagramEdgeConstraints.returnEdge().satisfied(aReturnEdge, aCallNode2, aCallNode1, aPoint, aPoint, aDiagram).values().toArray()[0]);
	}	
	
	@Test
	public void testCallEdgeEndNotCallEdge()
	{
		createDiagram();
		assertEquals("Call edges that land on a parameter node must land on the life line part, except if it is allowed to create a constructor.",(String)SequenceDiagramEdgeConstraints.callEdgeEnd().satisfied(aReturnEdge, aCallNode2, aCallNode1, aPoint, new Point(10,10), aDiagram).keySet().toArray()[0]);
		assertTrue((boolean)SequenceDiagramEdgeConstraints.callEdgeEnd().satisfied(aReturnEdge, aCallNode2, aCallNode1, aPoint, new Point(10,10), aDiagram).values().toArray()[0]);
	}	
	
	@Test
	public void testCallEdgeEndEndNotParameter()
	{
		createDiagram();
		assertEquals("Call edges that land on a parameter node must land on the life line part, except if it is allowed to create a constructor.",(String)SequenceDiagramEdgeConstraints.callEdgeEnd().satisfied(aCallEdge, aCallNode2, aCallNode1, aPoint, new Point(10,10), aDiagram).keySet().toArray()[0]);
		assertTrue((boolean)SequenceDiagramEdgeConstraints.callEdgeEnd().satisfied(aCallEdge, aCallNode2, aCallNode1, aPoint, new Point(10,10), aDiagram).values().toArray()[0]);
	}	
	
	@Test
	public void testCallEdgeEndEndOnLifeLine()
	{
		createDiagram();
		assertEquals("Call edges that land on a parameter node must land on the life line part, except if it is allowed to create a constructor.",(String)SequenceDiagramEdgeConstraints.callEdgeEnd().satisfied(aCallEdge, aParameter2, aCallNode1, aPoint, new Point(0,85), aDiagram).keySet().toArray()[0]);
		assertTrue((boolean)SequenceDiagramEdgeConstraints.callEdgeEnd().satisfied(aCallEdge, aParameter2, aCallNode1, aPoint, new Point(0,85), aDiagram).values().toArray()[0]);
	}	
	
	@Test
	public void testCallEdgeEndEndOnTopRectangle()
	{
		createDiagram();
		assertEquals("Call edges that land on a parameter node must land on the life line part, except if it is allowed to create a constructor.",(String)SequenceDiagramEdgeConstraints.callEdgeEnd().satisfied(aCallEdge, aParameter2, aCallNode1, aPoint, new Point(0,5), aDiagram).keySet().toArray()[0]);
		assertTrue((boolean)SequenceDiagramEdgeConstraints.callEdgeEnd().satisfied(aCallEdge, aParameter2, aCallNode1, aPoint, new Point(0,5), aDiagram).values().toArray()[0]);
	}	
	
	@Test
	public void testSingleEntryPointNotACallEdge()
	{
		createDiagram();
		assertEquals("It's only legal to start an interaction on a parameter node if there are no existing activations in the diagram.",(String)SequenceDiagramEdgeConstraints.singleEntryPoint().satisfied(aReturnEdge, aParameter1, aParameter1, aPoint, aPoint, aDiagram).keySet().toArray()[0]);
		assertTrue((boolean)SequenceDiagramEdgeConstraints.singleEntryPoint().satisfied(aReturnEdge, aParameter1, aParameter1, aPoint, aPoint, aDiagram).values().toArray()[0]);
	}	
	
	@Test
	public void testSingleEntryPointNotStartingOnAParameterNode()
	{
		createDiagram();
		assertEquals("It's only legal to start an interaction on a parameter node if there are no existing activations in the diagram.",(String)SequenceDiagramEdgeConstraints.singleEntryPoint().satisfied(aCallEdge, aCallNode1, aCallNode1, aPoint, aPoint, aDiagram).keySet().toArray()[0]);
		assertTrue((boolean)SequenceDiagramEdgeConstraints.singleEntryPoint().satisfied(aCallEdge, aCallNode1, aCallNode1, aPoint, aPoint, aDiagram).values().toArray()[0]);
	}	
	
	@Test
	public void testSingleEntryPointStartingOnParameterNodeNotSatisfied()
	{
		createDiagram();
		assertEquals("It's only legal to start an interaction on a parameter node if there are no existing activations in the diagram.",(String)SequenceDiagramEdgeConstraints.singleEntryPoint().satisfied(aCallEdge, aParameter1, aParameter1, aPoint, aPoint, aDiagram).keySet().toArray()[0]);
		assertFalse((boolean)SequenceDiagramEdgeConstraints.singleEntryPoint().satisfied(aCallEdge, aParameter1, aParameter1, aPoint, aPoint, aDiagram).values().toArray()[0]);
	}	
	
	@Test
	public void testSingleEntryPointStartingOnParameterNodeSatisfied()
	{
		aDiagram.addRootNode(aParameter1);
		assertEquals("It's only legal to start an interaction on a parameter node if there are no existing activations in the diagram.",(String)SequenceDiagramEdgeConstraints.singleEntryPoint().satisfied(aCallEdge, aParameter1, aParameter1, aPoint, aPoint, aDiagram).keySet().toArray()[0]);
		assertTrue((boolean)SequenceDiagramEdgeConstraints.singleEntryPoint().satisfied(aCallEdge, aParameter1, aParameter1, aPoint, aPoint, aDiagram).values().toArray()[0]);
	}	
}