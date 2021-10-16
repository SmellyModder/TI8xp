package net.smelly.tieightxp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * A class that represents a decompiled state of a TI Program from a given array of bytes.
 *
 * @author SmellyModder (Luke Tonon)
 */
public final class TIPrgmDecompiler {
	private final String comment;
	private final boolean uneditable;
	private final String name;
	private final TIInstruction[] instructions;

	private TIPrgmDecompiler(byte[] bytes) throws IOException {
		//A few sections of the bytes are ignored as they are only used by the calculator when reading a .8xp file.
		this.comment = new String(slice(bytes, 11, 42));
		this.uneditable = bytes[59] != 5;
		this.name = new String(slice(bytes, 60, 8));
		byte[] prgmCode = slice(bytes, 74, bytes.length - 76);
		int prgmCodeLength = prgmCode.length;
		List<TIInstruction> instructions = new ArrayList<>();
		for (int i = 0; i < prgmCodeLength; i++) {
			List<Integer> bytecodeSlice = TIInstructions.convertBytesToUnassignedInts(slice(prgmCode, i, 1));
			TIInstruction instruction = TIInstructions.by(bytecodeSlice);
			if (instruction == null) {
				if (i + 1 >= prgmCodeLength) {
					throw new IOException("TI Instruction with bytecode " + bytecodeSlice + " exceeded program length!");
				} else {
					bytecodeSlice = TIInstructions.convertBytesToUnassignedInts(slice(prgmCode, i, 2));
					instruction = TIInstructions.by(bytecodeSlice);
					if (instruction == null) {
						throw new IOException("Unknown TI Instruction: " + bytecodeSlice);
					}
					i++;
				}
			}
			instructions.add(instruction);
		}
		this.instructions = instructions.toArray(new TIInstruction[0]);
	}

	private static byte[] slice(byte[] bytes, int offset, int length) {
		byte[] slice = new byte[length];
		System.arraycopy(bytes, offset, slice, 0, length);
		return slice;
	}

	/**
	 * Decompiles a TI program from bytes.
	 *
	 * @param bytes An array of bytes to decompile from.
	 * @return An object representation of the decompiled TI program.
	 * @throws IOException If the algorithm fails to decompile a TI program from the given byte array.
	 */
	public static TIPrgmDecompiler fromBytes(byte[] bytes) throws IOException {
		return new TIPrgmDecompiler(bytes);
	}

	/**
	 * Decompiles a TI program from a given {@link Path}.
	 *
	 * @param path A {@link Path} to read from.
	 * @return An object representation of the decompiled TI program.
	 * @throws IOException If the algorithm fails to decompile a TI program from the given {@link Path}.
	 */
	public static TIPrgmDecompiler fromPath(Path path) throws IOException {
		return fromBytes(Files.readAllBytes(path));
	}

	/**
	 * Gets the comment of the program.
	 * <p>The comment is typically "Created by TI Connect {edition} {version}".</p>
	 * <p>The returned string should never be larger than 42 bytes.</p>
	 *
	 * @return The comment of the program.
	 */
	public String getComment() {
		return this.comment;
	}

	/**
	 * If this program is marked as uneditable.
	 * <p>When uneditable, calculators cannot edit the code of a program.</p>
	 *
	 * @return If this program is marked as uneditable.
	 */
	public boolean isUneditable() {
		return this.uneditable;
	}

	/**
	 * Gets the name of this program.
	 *
	 * @return The name of this program.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Gets the instructions of this program.
	 *
	 * @return The instructions of this program.
	 */
	public TIInstruction[] getInstructions() {
		return this.instructions;
	}

	/**
	 * Gets a new {@link TIPrgmCompiler} for this program.
	 * <p>Used to compile new copies of this program.</p>
	 *
	 * @return A new {@link TIPrgmCompiler} for this program.
	 */
	public TIPrgmCompiler asCompiler() {
		return TIPrgmCompiler.compiler().comment(this.comment).uneditable(this.uneditable).name(this.name).add(this.instructions);
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Comment: ");
		stringBuilder.append(this.comment);
		stringBuilder.append("\n");
		stringBuilder.append("Uneditable: ");
		stringBuilder.append(this.uneditable);
		stringBuilder.append("\n");
		stringBuilder.append("Name: ");
		stringBuilder.append(this.name);
		stringBuilder.append("\n");
		stringBuilder.append("Instructions: ");
		TIInstruction[] instructions = this.instructions;
		int length = instructions.length;
		if (length > 0) {
			stringBuilder.append('[');
			int lengthMinusOne = length - 1;
			for (int i = 0; i < length; i++) {
				stringBuilder.append(instructions[i].name());
				if (i < lengthMinusOne) {
					stringBuilder.append(", ");
				}
			}
			stringBuilder.append(']');
		} else {
			stringBuilder.append("None");
		}
		return stringBuilder.toString();
	}
}
