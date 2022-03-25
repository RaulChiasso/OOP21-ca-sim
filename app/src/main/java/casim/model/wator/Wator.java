package casim.model.wator;

import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;

import casim.model.abstraction.automaton.AbstractAutomaton;
import casim.model.abstraction.utils.NeighborsFunctions;
import casim.utils.PlayableAutomaton;
import casim.utils.coordinate.CoordinatesUtil;
import casim.utils.grid.Grid2D;
import casim.utils.range.Ranges;

/**
 * Predators and Preys automaton.
 */
@PlayableAutomaton(AutomatonName = "Predators and Preys")
public class Wator extends AbstractAutomaton<CellState, WatorCell> {

    private static final int INIT_HEALTH = 5;
    private static final int DEAD_HEALTH = 0;

    private final Grid2D<WatorCell> state;

    /**
     * Builds a new {@link Wator} automaton with initial state from input.
     * 
     * @param state {@link Grid2D} of {@link CellState} representing the
     * initial state of the automaton.
     */
    public Wator(final Grid2D<CellState> state) {
        this.state = state.map(x -> new WatorCell(x, INIT_HEALTH));
    }

    @Override
    public boolean hasNext() {
        // return this.state.stream().anyMatch(x -> !x.getState().equals(CellState.DEAD));
        return true;
    }

    @Override
    protected Grid2D<WatorCell> doStep() {
        for (final var x : Ranges.of(0, this.state.getHeight())) {
            for (final var y : Ranges.of(0, this.state.getWidth())) {
                final var coord = CoordinatesUtil.of(x, y);
                final var currCell = this.state.get(coord);
                final var neighborsList = NeighborsFunctions.neighbors2DFunction(Pair.of(coord, currCell), this.state)
                        .stream()
                        .map(Pair::getRight)
                        .collect(Collectors.toList());
                if (neighborsList.isEmpty()) {
                    System.out.println("uffa");
                }
                final var preyNeighbors = neighborsList.stream()
                        .filter(w -> w.getState().equals(CellState.PREY))
                        .collect(Collectors.toList());
                final var deadNeighbors = neighborsList.stream()
                        .filter(w -> w.getState().equals(CellState.DEAD))
                        .collect(Collectors.toList());
                switch (currCell.getState()) {
                    case PREY:
                        this.preyStep(currCell, neighborsList);
                        currCell.heal();
                        currCell.move();
                        break;
                    case PREDATOR:
                        if (preyNeighbors.size() > 0) {
                            this.predatorStep(currCell, preyNeighbors, WatorCell::heal);
                        } else if (deadNeighbors.size() > 0) {
                            this.predatorStep(currCell, deadNeighbors, WatorCell::starve);
                        } else {
                            currCell.starve();
                            this.applyDeath(currCell);
                            currCell.move();
                        }
                        break;
                    default:
                        break;

                }
            }
        }
        this.state.stream().forEach(WatorCell::resetMovement);
        return this.state;
    }

    @Override
    public Grid2D<WatorCell> getGrid() {
        return this.state;
    }

    private void predatorStep(final WatorCell currentCell, final List<WatorCell> neighbors,
            final Consumer<WatorCell> movementAction) {
        if (this.applyDeath(currentCell)) {
            return;
        } else if (currentCell.hasMoved()) {
            return;
        }
        final var rand = new Random();
        final var toChange = neighbors.get(rand.nextInt(neighbors.size()));
        toChange.clone(currentCell);
        movementAction.accept(toChange);
        toChange.move();
        final var spawn = toChange.reproduce();
        currentCell.clone(spawn);
        currentCell.move();
    }

    private boolean applyDeath(final WatorCell currentCell) {
        if (currentCell.isDead()) {
            currentCell.setState(CellState.DEAD);
            currentCell.setHealth(DEAD_HEALTH);
            return true;
        } else {
            return false;
        }
    }

    private void preyStep(final WatorCell currentCell, final List<WatorCell> neighborsList) {
        final var rand = new Random();
        final var chosenNeighbor = neighborsList.get(rand.nextInt(neighborsList.size()));
        if (chosenNeighbor.getState().equals(CellState.DEAD)) {
            chosenNeighbor.clone(currentCell);
            chosenNeighbor.move();
            chosenNeighbor.heal();
            final var spawn = currentCell.reproduce();
            currentCell.clone(spawn);
        }
    }

}
