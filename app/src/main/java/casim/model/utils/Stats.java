package casim.model.utils;

import java.util.EnumMap;

/**
 * An interface used to describe stats about an {@link casim.model.Automaton}.
 * 
 *  @param <T> the type of the finite states of the {@link casim.model.Automaton}'s {@link casim.model.cell.Cell}.
 */
public interface Stats<T extends Enum<T>>   {

    /**
     * Get the number of iterations done by the Automaton.
     * 
     * @return the number of iterations.
     */
    int getIteration();

    /**
     * Get the number of cells alive for each types.
     * 
     * @return an EnumMap:
     *   - Key: the types of the enumeration describing the cell's types;
     *   - Value: the number of cells of that type.
     */
    EnumMap<T, Integer> getCellStats();

}
