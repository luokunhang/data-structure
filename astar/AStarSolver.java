package astar;

import edu.princeton.cs.algs4.Stopwatch;
import heap.ArrayHeapMinPQ;
import heap.ExtrinsicMinPQ;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * @see ShortestPathsSolver for more method documentation
 */
public class AStarSolver<Vertex> implements ShortestPathsSolver<Vertex> {
    private SolverOutcome outcome;
    private double solutionWeight;
    private List<Vertex> solution;
    private double timeSpent;
    private int numStatesExplored;

    private Map<Vertex, Double> distTo;
    private Map<Vertex, Vertex> edgeTo;

    /**
     * Immediately solves and stores the result of running memory optimized A*
     * search, computing everything necessary for all other methods to return
     * their results in constant time. The timeout is given in seconds.
     */
    public AStarSolver(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout) {
        Stopwatch sw = new Stopwatch();
        numStatesExplored = 0;

        ExtrinsicMinPQ<Vertex> fringe = new ArrayHeapMinPQ<>();
        fringe.add(start, input.estimatedDistanceToGoal(start, end));

        distTo = new HashMap<>();
        edgeTo = new HashMap<>();
        distTo.put(start, 0.0);
        edgeTo.put(start, null);

        while (!fringe.isEmpty()) {
            Vertex current = fringe.removeSmallest();
            numStatesExplored += 1;
            if (current.equals(end)) {
                solution = path(current);
                solutionWeight = distTo.get(current);
                outcome = SolverOutcome.SOLVED;
                timeSpent = sw.elapsedTime();
                return;
            }

            List<WeightedEdge<Vertex>> children = input.neighbors(current);
            for (WeightedEdge<Vertex> e: children) {
                Vertex w = e.to();
                //relax all edges
                double pot = distTo.get(current) + e.weight();
                if (!distTo.containsKey(w) || pot < distTo.get(w)) {
                    distTo.put(w, distTo.get(current) + e.weight());
                    edgeTo.put(w, current);
                    if (!fringe.contains(w)) {
                        fringe.add(w, distTo.get(w) + input.estimatedDistanceToGoal(w, end));
                    } else {
                        fringe.changePriority(w, distTo.get(w) + input.estimatedDistanceToGoal(w, end));
                    }
                }
            }

            if (sw.elapsedTime() > 3000) {
                outcome = SolverOutcome.UNSOLVABLE;
                return;
            }
        }
    }

    private List<Vertex> path(Vertex end) {
        Vertex curr = end;
        Stack<Vertex> reverse = new Stack<>();
        reverse.push(curr);
        while (edgeTo.get(curr) != null) {
            reverse.push(edgeTo.get(curr));
            curr = edgeTo.get(curr);
        }
        List<Vertex> toReturn = new ArrayList<>();
        while (!reverse.isEmpty()) {
            toReturn.add(reverse.pop());
        }
        return toReturn;
    }

    @Override
    public SolverOutcome outcome() { return outcome; }

    @Override
    public List<Vertex> solution() { return solution; }

    @Override
    public double solutionWeight() { return solutionWeight; }

    /** The total number of priority queue removeSmallest operations. */
    @Override
    public int numStatesExplored() {
        return numStatesExplored;
    }

    @Override
    public double explorationTime() { return timeSpent; }
}
