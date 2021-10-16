package net.smelly.tieightxp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A builder class used to compile TI programs.
 *
 * @author SmellyModder (Luke Tonon)
 */
public final class TIPrgmCompiler {
	private static final byte[] HEADER_BYTES = "TI83F".getBytes();
	private final List<TIInstruction> instructions = new ArrayList<>();
	private String comment = "Created by TI Prgm Compiler";
	private boolean uneditable = false;
	private String name = "Untitled";

	private TIPrgmCompiler() {
	}

	/**
	 * Gets a new {@link TIPrgmCompiler} instance.
	 *
	 * @return A new {@link TIPrgmCompiler} instance.
	 */
	public static TIPrgmCompiler compiler() {
		return new TIPrgmCompiler();
	}

	private static byte[] intToBytes(int value) {
		return new byte[]{(byte) (value & 0xFF), (byte) ((value >> 8) & 0xFF)};
	}

	/**
	 * Sets the program's comment.
	 *
	 * @param comment A new comment.
	 * @return This compiler.
	 */
	public TIPrgmCompiler comment(String comment) {
		this.comment = comment;
		if (comment.length() > 42) {
			throw new IllegalArgumentException("Comment cannot exceed 42 characters!");
		}
		return this;
	}

	/**
	 * Sets if the program should be uneditable.
	 *
	 * @param uneditable If the program should be uneditable.
	 * @return This compiler.
	 */
	public TIPrgmCompiler uneditable(boolean uneditable) {
		this.uneditable = uneditable;
		return this;
	}

	/**
	 * Sets the program's name.
	 *
	 * @param name A new name.
	 * @return This compiler.
	 */
	public TIPrgmCompiler name(String name) {
		this.name = name;
		if (name.length() > 8) {
			throw new IllegalArgumentException("Name cannot exceed 8 characters!");
		}
		return this;
	}

	/**
	 * Adds a {@link TIInstruction} to the program.
	 *
	 * @param instruction A {@link TIInstruction} to add.
	 * @return This compiler.
	 */
	public TIPrgmCompiler add(TIInstruction instruction) {
		this.instructions.add(instruction);
		return this;
	}

	/**
	 * Adds an array of {@link TIInstruction}s to the program.
	 *
	 * @param instructions An array of {@link TIInstruction}s to add.
	 * @return This compiler.
	 */
	public TIPrgmCompiler add(TIInstruction... instructions) {
		this.instructions.addAll(Arrays.asList(instructions));
		return this;
	}

	/**
	 * Compiles this compiler into a new {@link ByteArrayOutputStream} containing the program's bytes.
	 *
	 * @return A new {@link ByteArrayOutputStream} containing the compiled program's bytes.
	 * @throws IOException If the compiler fails to compile.
	 */
	//TODO: Try to optimize this
	public ByteArrayOutputStream compile() throws IOException {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		//Write header
		stream.write(42);
		stream.write(42);
		stream.writeBytes(HEADER_BYTES);
		stream.write(42);
		stream.write(26);
		stream.write(10);
		stream.write(10);
		//Write comment
		byte[] commentBytes = new byte[42];
		byte[] comment = this.comment.getBytes();
		System.arraycopy(comment, 0, commentBytes, 0, comment.length);
		stream.write(commentBytes);
		//Write size of instructions + 19 as two bytes
		int instructionsByteCount = 0;
		for (TIInstruction instruction : this.instructions) {
			instructionsByteCount += instruction.bytecode().size();
		}
		stream.write(intToBytes(instructionsByteCount + 19));
		//Write two more bytes. Not sure what these are for.
		int checkSum = 0;
		stream.write(13);
		checkSum += 13;
		stream.write(0);
		int instructionSizePlus2 = instructionsByteCount + 2;
		//Write size of instructions + 2 as two bytes
		stream.write(intToBytes(instructionSizePlus2));
		checkSum += instructionSizePlus2;
		//Write protection bytes
		int protection = this.uneditable ? 6 : 5;
		stream.write(protection);
		checkSum += protection;
		//Write name
		byte[] nameBytes = new byte[8];
		byte[] name = this.name.getBytes();
		System.arraycopy(name, 0, nameBytes, 0, name.length);
		stream.write(nameBytes);
		for (byte b : name) {
			checkSum += Byte.toUnsignedInt(b);
		}
		//Write two nulls. Not sure what these are for.
		stream.write(0);
		stream.write(0);
		//Write the instructions size + 2 again
		stream.write(intToBytes(instructionSizePlus2));
		checkSum += instructionSizePlus2;
		//Write the instructions size
		stream.write(intToBytes(instructionsByteCount));
		checkSum += instructionsByteCount;
		ByteArrayOutputStream instructionBytes = new ByteArrayOutputStream();
		for (TIInstruction instruction : this.instructions) {
			for (Integer integer : instruction.bytecode()) {
				checkSum += integer;
				instructionBytes.write(integer);
			}
		}
		stream.write(instructionBytes.toByteArray());
		//Write the check sum
		stream.write(intToBytes(checkSum));
		return stream;
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
		stringBuilder.append("Instructions: [");
		List<TIInstruction> instructions = this.instructions;
		int size = instructions.size();
		for (int i = 0; i < size; i++) {
			stringBuilder.append('[');
			TIInstruction instruction = instructions.get(i);
			stringBuilder.append(instruction.bytecode());
			stringBuilder.append(", ");
			stringBuilder.append(instruction.name());
			stringBuilder.append(']');
			if (i + 1 < size) {
				stringBuilder.append(", ");
			}
		}
		stringBuilder.append(']');
		return stringBuilder.toString();
	}
}
