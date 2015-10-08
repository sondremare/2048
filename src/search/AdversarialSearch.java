package search;

import game.Board;
import game.Direction;

public interface AdversarialSearch {
    abstract Direction decision(Board board);
    abstract double getHeuristicValue(Board board);
}
