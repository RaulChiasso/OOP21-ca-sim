package casim.ui.grid;

import java.util.stream.Stream;

import casim.ui.grid.events.GridCellClickListener;
import casim.ui.grid.events.GridCellHoverListener;
import casim.utils.Grid;
import casim.utils.Result;
import casim.utils.coordinate.Coordinate;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;

/**
 * Implementation of {@link CanvasGrid} to be used in the GUI.
 */
public class CanvasGridImpl extends Canvas implements CanvasGrid {

    private static final Color DEFAULT = new Color(1.0, 1.0, 1.0, 1.0);
    private static final Color SELECTED = new Color(0.0, 0.0, 0.0, 1.0);

    private final int rows;
    private final int columns;
    private final double cellSize;
    private final Color separatorColor;
    private final double separatorWidth;
    private final double separatorOffset;
    private final double width;
    private final double height;

    private Grid<CanvasGridCell> cells;

    /**
     * Construct a new {@link CanvasGridImpl}.
     * 
     * @param rows number of rows of the grid (cells, not pixels).
     * @param columns number of columns of the grid (cells, not pixels).
     * @param cellSize cell size in pixels.
     * @param separatorColor separator color.
     * @param separatorWidth separator width.
     * @param separatorOffset separator offset.
     */
    public CanvasGridImpl(final int rows, final int columns, final double cellSize, 
        final Color separatorColor, final double separatorWidth, final double separatorOffset) {
        this.rows = rows;
        this.columns = columns;
        this.cellSize = cellSize;
        this.separatorColor = separatorColor;
        this.separatorWidth = separatorWidth;
        this.separatorOffset = separatorOffset;

        this.width = columns * cellSize;
        this.height = rows * cellSize;

        this.cells = null; //TODO

        this.setOnMouseClicked(new GridCellClickListener(this));
        this.setOnMouseDragOver(new GridCellHoverListener(this));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCellClick(final MouseButton button, final CanvasGridCell cell, final Coordinate<Integer> coord) {
        if (!button.equals(MouseButton.PRIMARY)) {
            return;
        }
        cell.setColor(cell.getColor().equals(DEFAULT) ? SELECTED : DEFAULT);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCellHover(final CanvasGridCell cell, final Coordinate<Integer> coord) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getColumns() {
        return this.columns;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getRows() {
        return this.rows;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void populate() {
        // TODO Auto-generated method stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void draw() {
        final var graphics = this.getGraphicsContext2D();
        this.drawGrid(graphics);
        this.drawCells(graphics);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getCellSize() {
        return this.cellSize;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Grid<CanvasGridCell> getCells() {
        // TODO: defensive copy
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCells(final Grid<CanvasGridCell> cells) {
        //TODO: defensive copy
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Result<CanvasGridCell> getCell(final Coordinate<Integer> coord) {
        return this.cells.get(coord.getX(), coord.getY());
    }

    private void drawCells(final GraphicsContext graphics) {
        this.cells.flatStream()
            .forEach(cell -> {
                graphics.setFill(cell.getColor());
                graphics.fillRect(
                    cell.getTopLeft().getX(),
                    cell.getTopLeft().getY(),
                    cell.getBottomRight().getX(),
                    cell.getBottomRight().getY()
                );
            });
    }

    private void drawGrid(final GraphicsContext graphics) {
        graphics.setStroke(this.separatorColor);
        graphics.setLineWidth(this.separatorWidth);
        Stream.iterate(0, x -> x + 1)
            .takeWhile(x -> x <= this.width)
            .forEach(x -> graphics.strokeLine(x + this.separatorOffset, 0, x + this.separatorOffset, height));
        Stream.iterate(0, y -> y + 1)
            .takeWhile(y -> y <= this.height)
            .forEach(y -> graphics.strokeLine(0, y + this.separatorOffset, width, y + this.separatorOffset));
    }
}
