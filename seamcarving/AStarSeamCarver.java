package seamcarving;

import astar.AStarSolver;
import astar.ShortestPathsSolver;
import astar.example.WeightedDirectedGraph;
import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class AStarSeamCarver implements SeamCarver {
    private Picture picture;

    public AStarSeamCarver(Picture picture) {
        if (picture == null) {
            throw new NullPointerException("Picture cannot be null.");
        }
        this.picture = new Picture(picture);
    }

    public Picture picture() {
        return new Picture(picture);
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    public int width() {
        return picture.width();
    }

    public int height() {
        return picture.height();
    }

    public Color get(int x, int y) {
        return picture.get(x, y);
    }

    public double energy(int x, int y) {
        int left = x - 1;
        int right = x + 1;
        int up = y - 1;
        int down = y + 1;
        if (x == 0) {
            left = width() - 1;
        }
        if (x == width() - 1) {
            right = 0;
        }
        if (y == 0) {
            up = height() - 1;
        }
        if (y == height() - 1) {
            down = 0;
        }

        int redXGradient = picture.get(right, y).getRed() - picture.get(left, y).getRed();
        int greenXGradient = picture.get(right, y).getGreen() - picture.get(left, y).getGreen();
        int blueXGradient = picture.get(right, y).getBlue() - picture.get(left, y).getBlue();

        int redYGradient = picture.get(x, down).getRed() - picture.get(x, up).getRed();
        int greenYGradient = picture.get(x, down).getGreen() - picture.get(x, up).getGreen();
        int blueYGradient = picture.get(x, down).getBlue() - picture.get(x, up).getBlue();

        return Math.sqrt(Math.pow(redXGradient, 2) + Math.pow(greenXGradient, 2)
                + Math.pow(blueXGradient, 2) + Math.pow(redYGradient, 2)
                + Math.pow(greenYGradient, 2) + Math.pow(blueYGradient, 2));
    }

    public int[] findHorizontalSeam() {
        int pixels = width() * height();
        WeightedDirectedGraph graph = new WeightedDirectedGraph(pixels + 2);
        //adding edges from the start to the first column
        for (int i = 0; i < height(); i++) {
            graph.addEdge(pixels, i * width(), 0);
        }
        //adding intermediate edges
        for (int i = 0; i < pixels; i++) {
            if ((i + 1) % width() != 0) {
                double engy = energy(i % width(), i / width());
                if (i / width() == 0) {
                    for (int j = 0; j < Math.min(2, height()); j++) {
                        graph.addEdge(i, i + 1 + j * width(), engy);
                    }
                } else if (i / width() == height() - 1) {
                    for (int j = 0; j < Math.min(2, height()); j++) {
                        graph.addEdge(i, i + 1 - j * width(), engy);
                    }
                } else {
                    for (int j = -1; j < 2; j++) {
                        graph.addEdge(i, i + 1 + j * width(), engy);
                    }
                }
            }
        }
        //adding edges from the last column to the end
        for (int i = width() - 1; i < width() * height(); i += width()) {
            graph.addEdge(i, pixels + 1,
                    energy(i % width(), i / width()));
        }
        ShortestPathsSolver<Integer> hori = new AStarSolver<Integer>(graph,
                pixels, pixels + 1, 1000);
        int[] toReturn = new int[hori.solution().size() - 2];
        for (int i = 0; i < toReturn.length; i++) {
            toReturn[i] = hori.solution().get(i + 1) / width();
        }
        return toReturn;
    }

    public int[] findVerticalSeam() {
        int pixels = width() * height();
        WeightedDirectedGraph graph = new WeightedDirectedGraph(pixels + 2);
        //adding edges from the start to the first row
        for (int i = 0; i < width(); i++) {
            graph.addEdge(pixels, i, 0);
        }
        //adding intermediate edges
        for (int i = 0; i <= width() * (height() - 1); i++) {
            double engy = energy(i % width(), i / width());
            if (i % width() == 0) {
                for (int j = 0; j < Math.min(2, width()); j++) {
                    graph.addEdge(i, i + width() + j, engy);
                }
            } else if ((i + 1) % width() == 0) {
                for (int j = 0; j < Math.min(2, width()); j++) {
                    graph.addEdge(i, i + width() - j, engy);
                }
            } else {
                for (int j = -1; j < 2; j++) {
                    graph.addEdge(i, i + width() + j, engy);
                }
            }
        }
        //adding edges from the last row to the end
        for (int i = width() * (height() - 1); i < width() * height(); i++) {
            graph.addEdge(i, pixels + 1, energy(i % width(), i / width()));
        }
        ShortestPathsSolver<Integer> vert = new AStarSolver<Integer>(graph,
                pixels, pixels + 1, 1000);
        int[] toReturn = new int[vert.solution().size() - 2];
        for (int i = 0; i < toReturn.length; i++) {
            toReturn[i] = vert.solution().get(i + 1) % width();
        }
        return toReturn;
    }
}
