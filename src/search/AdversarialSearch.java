package search;

import game.Board;
import game.Direction;

public interface AdversarialSearch {
    abstract Direction decision(Board board, int maxDepth);
    abstract double getHeuristicValue(Board board);
}
