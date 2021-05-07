package it.polito.tdp.extflightdelays.model;

import java.util.*;

import org.jgrapht.Graphs;
import org.jgrapht.event.ConnectedComponentTraversalEvent;
import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.*;
import org.jgrapht.traverse.BreadthFirstIterator;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;

public class Model {

	private SimpleWeightedGraph<Airport,DefaultWeightedEdge> grafo;
	private ExtFlightDelaysDAO dao;
	private Map<Integer,Airport> idMap;
	private Map<Airport,Airport> visita;
	public Model() {
		dao = new ExtFlightDelaysDAO();
		idMap = new HashMap<Integer,Airport>();
		dao.loadAllAirports(idMap);
	}
	
	public void creaGrafo(int x) {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		//ora filtriamo gli aereoporti che ci servono nelle indicazioni del pdf nel progetto
		
		// aggiungo i vertici " filtrati "
		Graphs.addAllVertices(grafo, dao.getVertici(idMap, x));
		
		// aggiungo gli archi
		for(Rotta r : dao.getRotte(idMap)) {
			//se gli aereoporti sono presenti nel grafo
			if(grafo.containsVertex(r.getA1()) && grafo.containsVertex(r.getA2())) {
				DefaultWeightedEdge e = this.grafo.getEdge(r.getA1(), r.getA2());//indipendente dall ordine dei 2 vertici perche non e orientato
				//se non c'è ancora un arco tra i 2 veertici lo aggiungo normalmente
				if(e==null) {
					Graphs.addEdgeWithVertices(grafo, r.getA1(), r.getA2(),r.getN());
				}else {
					double pesoVecchio = this.grafo.getEdgeWeight(e);
					double pesoNuovo = pesoVecchio + r.getN();
					this.grafo.setEdgeWeight(e, pesoNuovo);
				}
			}
		}
		System.out.println("Grafo creato");
		System.out.println("# Vertici "+grafo.vertexSet().size());
		System.out.println("# Archi "+grafo.edgeSet().size());
	}

	public Set<Airport> getVertici() {
		// TODO Auto-generated method stub
		return this.grafo.vertexSet();
	}

	public int getNVertici() {
		if(grafo != null)
			return grafo.vertexSet().size();
		
		return 0;
	}
	
	public int getNArchi() {
		if(grafo != null)
			return grafo.edgeSet().size();
		
		return 0;
	}
	
	
	public List<Airport> trovaPercorso(Airport a1,Airport a2) {
		List<Airport> percorso  = new LinkedList<>();
		BreadthFirstIterator<Airport,DefaultWeightedEdge> it = new BreadthFirstIterator<>(grafo,a1);
		
		visita = new HashMap<>();
		visita.put(a1, null);
		it.addTraversalListener(new TraversalListener<Airport,DefaultWeightedEdge>(){

			@Override
			public void connectedComponentFinished(ConnectedComponentTraversalEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void connectedComponentStarted(ConnectedComponentTraversalEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void edgeTraversed(EdgeTraversalEvent<DefaultWeightedEdge> e) {
				// TODO Auto-generated method stub
				Airport a1 = grafo.getEdgeSource(e.getEdge());
				Airport a2 = grafo.getEdgeTarget(e.getEdge());
				
				if(visita.containsKey(a1) && !visita.containsKey(a2)) {
					visita.put(a2, a1);
				}else if(visita.containsKey(a2) && !visita.containsKey(a1)){
					visita.put(a1,a2);
				}
				
			}

			@Override
			public void vertexTraversed(VertexTraversalEvent<Airport> e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void vertexFinished(VertexTraversalEvent<Airport> e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		while(it.hasNext()) {
			it.next();
		}
		
		percorso.add(a2);
		
		Airport step= a2;
		while(visita.get(step)!=null) {
			step=visita.get(step);
			percorso.add(0,step);
		}
		
		return percorso;
	}
}
