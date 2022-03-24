/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package casim;

import casim.controller.automaton.AutomatonController;
import casim.controller.automaton.AutomatonControllerImpl;
import casim.model.codi.CoDi;
import casim.model.codi.cell.attributes.CellState;
import casim.ui.components.grid.CanvasGridBuilderImpl;
import casim.ui.components.grid.CanvasGridImpl;
import casim.ui.components.page.PageContainer;
import casim.ui.utils.StateColorMapper;
import casim.ui.view.AutomatonView;
import casim.utils.Colors;
import casim.utils.grid.Grid3DImpl;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main project class.
 */
public class App extends Application {

    private static final int ROWS = 20;
    private static final int COLS = 20;
    private static final int DEPTH = 20;
    private static final int CELL_SIZE = 40;

    private CanvasGridImpl getGrid() {
        return (CanvasGridImpl) new CanvasGridBuilderImpl().build(ROWS, COLS, CELL_SIZE);
    }

    private AutomatonView<CellState> getView(final Stage stage, final AutomatonController<CellState> controller, final CanvasGridImpl grid, final StateColorMapper<CellState> mapper) {
        return new AutomatonView<>(stage, controller, grid, mapper);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(final Stage primaryStage) throws Exception {
        final var state = new Grid3DImpl<CellState>(ROWS, COLS, DEPTH, () -> CellState.BLANK);
        final var automaton = new CoDi(state);
        final var controller = new AutomatonControllerImpl<>(automaton);
        final var view = this.getView(primaryStage, controller, this.getGrid(), s -> {
            switch (s) {
                case BLANK:
                    return Colors.BLACK;
                case AXON:
                    return Colors.FUSCIA;
                case DENDRITE:
                    return Colors.ARCTIC;
                case ACTIVATED_AXON:
                    return Colors.CARROT;
                case ACTIVATED_DENDRITE:
                    return Colors.PARAKEET;
                case NEURON:
                    return Colors.AMETHYST;
                default:
                    throw new IllegalArgumentException("Invalid state.");
            }
        });

        final var root = new PageContainer(primaryStage);
        root.addPage(view);
        final var scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Entry point.
     * 
     * @param args command line args.
     */
    public static void main(final String[] args) {
        launch(args);
    }
}
