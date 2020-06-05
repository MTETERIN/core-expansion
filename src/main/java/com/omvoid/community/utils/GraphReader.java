package com.omvoid.community.utils;

import com.omvoid.community.Arguments;
import com.omvoid.community.exception.GraphReaderException;
import javafx.util.Pair;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultUndirectedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.io.*;

public class GraphReader {

    public Graph<Integer, DefaultWeightedEdge> readGraph(Arguments arguments) throws GraphReaderException {

        Graph<Integer, DefaultWeightedEdge> graph = new DefaultUndirectedGraph<>(DefaultWeightedEdge.class);

        BufferedReader reader = new BufferedReader(getFileReader(arguments));

        try {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(arguments.getCommentStartWith())) continue;
                Pair<Integer, Integer> vertexPair = getVertexPair(line, arguments.getDelimiter());
                graph.addVertex(vertexPair.getKey());//что тут происходит?
                graph.addVertex(vertexPair.getValue());
                graph.addEdge(vertexPair.getKey(), vertexPair.getValue());
            }
        } catch (IOException e) {
            throw new GraphReaderException(e);
        }
        return graph;
    }

    private FileReader getFileReader(Arguments arguments) throws GraphReaderException {
        try {
            return new FileReader(arguments.getInputFile());
        } catch (FileNotFoundException e) {
            throw new GraphReaderException(e);
        }
    }

    private Pair<Integer, Integer> getVertexPair(String line, String delimiter) throws GraphReaderException {
        String[] rawVertex = line.split(delimiter);

        if (rawVertex.length != 2) throw new GraphReaderException("Line " + line + " has vertex size not equals 2");

        Integer firstVertex;
        Integer secondVertex;

        try {
            firstVertex = Integer.parseInt(rawVertex[0]);
            secondVertex = Integer.parseInt(rawVertex[1]);
        } catch (NumberFormatException e) {
            throw new GraphReaderException("Line " + line + " can't parse vertex pair");
        }

        if (firstVertex.equals(secondVertex))
            throw new GraphReaderException("Line " + line + " this algorithm does not support loop");

        return new Pair<>(firstVertex, secondVertex);
    }
}
