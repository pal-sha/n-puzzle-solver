package fifteenpuzzle;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.invoke.MethodHandles;

public class Solver {
	public static void main(String[] args) throws IOException {
//		System.out.println("number of arguments: " + args.length);
//		for (int i = 0; i < args.length; i++) {
//			System.out.println(args[i]);
//		}

		if (args.length < 2) {
			System.out.println("File names are not specified");
			System.out.println("usage: java " + MethodHandles.lookup().lookupClass().getName() + " input_file output_file");
			return;
		}
		
		
		// TODO
		File input = new File(args[0]);	// Creating file object with input file name
		GameSolver game1 = new GameSolver(input);	// Creating n-puzzle solver object using input file name
		long start = System.currentTimeMillis();	// Starting time
		// solve...
		GameSolver.algorithmSwitch(game1.getRoot()); // Calling function to invoke the correct search algorithm
		long timeElapsed = System.currentTimeMillis() - start; // Calculating time taken for board to run
		System.out.println("Time taken:  " + timeElapsed);

		File output = new File(args[1]);	// Creating a file object with output file name
		BufferedWriter newWriter = new BufferedWriter(new FileWriter(output));	// Creating a buffered writer object to write to file

		// Iterating through output string and writing each line onto output file
		for (String s: GameSolver.getOutputSolution()){
			newWriter.write(s + "\n");
		}

		newWriter.close();
	}
}
