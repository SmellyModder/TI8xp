package net.smelly.tieightxp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class Main {

	public static void main(String[] args) throws IOException {
		TIPrgmDecompiler decompiler = TIPrgmDecompiler.fromPath(Path.of(args[0]));
		System.out.println(decompiler);
		TIPrgmCompiler compiler = TIPrgmCompiler.compiler();
		compiler.name("HELOWRLD");
		compiler.uneditable(true);
		compiler.add(TIInstructions.ClrHome);
		compiler.add(TIInstructions.NEXT_LINE_AND_CARRIAGE);
		compiler.add(TIInstructions.Disp);
		compiler.add(TIInstructions.string("Hello World!"));
		Files.write(Path.of(args[1]), compiler.compile().toByteArray());
	}

}
