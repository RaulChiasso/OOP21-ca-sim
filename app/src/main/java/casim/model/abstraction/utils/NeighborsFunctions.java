package casim.model.abstraction.utils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;

import casim.model.abstraction.cell.AbstractCell;
import casim.utils.coordinate.Coordinates2D;
import casim.utils.coordinate.Coordinates3D;
import casim.utils.coordinate.CoordinatesUtil;
import casim.utils.grid.Grid;

/**
 * Static class for the standard neighbors functions. 
 */
public final class NeighborsFunctions {

    private NeighborsFunctions() {
    }

    /**
     * Method to obtain all the neighbors of a 2D {@link casim.model.abstraction.cell.Cell}.
     * 
     * @param cellPair a pair {@link Coordinates2D}+{@link AbstractCell} implementation of the cell of which calculate the neighbors.
     * @param grid the 2D {@link Grid} where search for the neighbors.
     * @return a list containing all the neighbors of the cell.
     * @param <T> the {@link AbstractCell} implementation of the cells.
     */
    public static <T extends AbstractCell<?>> List<Pair<Coordinates2D<Integer>, T>> neighbors2DFunction(final Pair<Coordinates2D<Integer>, T> cellPair, final Grid<Coordinates2D<Integer>, T> grid) {
         return Stream.of(CoordinatesUtil.of(1, 0), CoordinatesUtil.of(0, 1), CoordinatesUtil.of(0, -1), CoordinatesUtil.of(-1, 0))
                 .map(coord -> CoordinatesUtil.sumInt(coord, cellPair.getLeft()))
                 .filter(grid::isCoordValid)
                 .map(coord -> Pair.of(coord, grid.get(coord))) 
                 .collect(Collectors.toList());
    }

    /**
     * Method to obtain all the neighbors of a 3D {@link casim.model.abstraction.cell.Cell}.
     * 
     * @param cellPair a pair {@link Coordinates3D}+{@link AbstractCell} implementation of the cell of which calculate the neighbors.
     * @param grid the 3D {@link Grid} where search for the neighbors.
     * @return a list containing all the neighbors of the cell.
     * @param <T> the {@link AbstractCell} implementation of the cells.
     */
    public static <T extends AbstractCell<?>> Iterable<Pair<Coordinates3D<Integer>, T>> neighbors3DFunction(final Pair<Coordinates3D<Integer>, T> cellPair, final Grid<Coordinates3D<Integer>, T> grid) {
        return Stream.of(CoordinatesUtil.of(1, 0, 0), CoordinatesUtil.of(-1, 0, 0), CoordinatesUtil.of(0, 1, 0),
                CoordinatesUtil.of(0, -1, 0), CoordinatesUtil.of(0, 0, 1), CoordinatesUtil.of(0, 0, -1))
            .map(coord -> CoordinatesUtil.sumInt(coord, cellPair.getLeft()))
            .filter(grid::isCoordValid)
            .map(coord -> Pair.of(coord, grid.get(coord)))
            .collect(Collectors.toList());
    }
}
