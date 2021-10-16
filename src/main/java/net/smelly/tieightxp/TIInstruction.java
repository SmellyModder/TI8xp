package net.smelly.tieightxp;

import java.util.List;

/**
 * A record class to store the name and 'bytecode' of a TI program instruction.
 * <p>Instructions can sorta be thought of as buttons on the calculator.</p>
 * <p>The 'bytecode' of an instruction is the 1-2 pair of bytes used to represent the instruction used by TI programs.</p>
 * <p>The 'name' of an instruction is just a term used to roughly describe what the instruction represents on TI programs.</p>
 *
 * @author SmellyModder (Luke Tonon)
 */
//Java doesn't have unassigned bytes, so we must store the bytes as unassigned ints.
public record TIInstruction(String name, List<Integer> bytecode) {
}
