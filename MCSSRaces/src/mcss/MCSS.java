package mcss;

import java.text.MessageFormat;
import java.util.Random;

/**
 * A variety of solutions to the Maximum Contiguous Subsequence Sum problem,
 * from Weiss, 3e, section 5.3.
 */
public class MCSS {
	private static final int MAX_ABSOLUTE_VALUE = 10;
	private static final String MESSAGE_FORMAT = "found mcss of {0,number} "
			+ "between indexes {1,number} and {2,number} in {3,number} ms, "
			+ "sequence length was {4,number}";
	private static final Random RANDOM_GENERATOR = new Random();

	/**
	 * This nested class is just a "struct" that lets us return multiple values.
	 */
	public static class Result {
		/**
		 * The begining of the sequence, inclusive.
		 */
		public int startIndex;
		/**
		 * The end of the sequence, inclusive.
		 */
		public int endIndex;
		/**
		 * The sequence sum.
		 */
		public long sum;
	}

	/**
	 * Starts the demo.
	 * 
	 * @param args
	 *            ignored
	 */
	public static void main(String[] args) {
		Result result;
		
		// Change these sequence lengths to try to get all algorithms running
		// in about 1 second!
		int sequenceLengthCubic = 100;
		int sequenceLengthQuadratic = 100;
		int sequenceLengthLinear = 100;
		StopWatch timer = new StopWatch();

		
		// Generate 'random' sequences for testing all 3 implementations of MCSS
		int[] sequenceCubic = getRandomSequence(sequenceLengthCubic);
		int[] sequenceQuadratic = getRandomSequence(sequenceLengthQuadratic);
		int[] sequenceLinear = getRandomSequence(sequenceLengthLinear);
		
		// ================
		// Start the races!
		// ================

		// =========================
		// Time the linear algorithm
		// =========================
		timer.reset();
		timer.start();
		result = mcssLinear(sequenceLinear);
		timer.stop();
		System.out.print("MCSS O(N^1): ");
		System.out.println(MessageFormat.format(MESSAGE_FORMAT, result.sum,
				result.startIndex, result.endIndex, timer.getElapsedTime(),
				sequenceLengthLinear));
		
		// ============================
		// Time the quadratic algorithm
		// ============================
		timer.reset();
		timer.start();
		result = mcssQuadratic(sequenceQuadratic);
		timer.stop();
		System.out.print("MCSS O(N^2): ");
		System.out.println(MessageFormat.format(MESSAGE_FORMAT, result.sum,
				result.startIndex, result.endIndex, timer.getElapsedTime(),
				sequenceLengthQuadratic));

		// ========================
		// Time the cubic algorithm
		// ========================
		timer.reset();
		timer.start();
		result = mcssCubic(sequenceCubic);
		timer.stop();
		System.out.print("MCSS O(N^3): ");
		System.out.println(MessageFormat.format(MESSAGE_FORMAT, result.sum,
				result.startIndex, result.endIndex, timer.getElapsedTime(),
				sequenceLengthCubic));
	}

	/**
	 * Implements the cubic MCSS algorithm. See Weiss, figure 5.4.
	 * 
	 * @param seq
	 * @return the MCSS, with start and end indexes
	 */
	public static Result mcssCubic(int[] seq) {
		Result result = new Result();
		result.sum = 0;

		for (int subseqStart = 0; subseqStart < seq.length; subseqStart++) {
			for (int subseqEnd = subseqStart; subseqEnd < seq.length; subseqEnd++) {
				int thisSum = 0;
				for (int k = subseqStart; k <= subseqEnd; k++) {
					thisSum += seq[k];
				}

				if (thisSum > result.sum) {
					result.sum = thisSum;
					result.startIndex = subseqStart;
					result.endIndex = subseqEnd;
				}
			}
		}
		return result;
	}

	/**
	 * Implements the quadratic MCSS algorithm. See Weiss, figure 5.5.
	 * 
	 * @param seq
	 * @return the MCSS, with start and end indexes
	 */
	public static Result mcssQuadratic(int[] seq) {
		Result result = new Result();
		result.sum = 0;

		for (int subseqStart = 0; subseqStart < seq.length; subseqStart++) {
			int thisSum = 0;
			for (int subseqEnd = subseqStart; subseqEnd < seq.length; subseqEnd++) {
				thisSum += seq[subseqEnd];

				if (thisSum > result.sum) {
					result.sum = thisSum;
					result.startIndex = subseqStart;
					result.endIndex = subseqEnd;
				}
			}
		}
		return result;
	}

	/**
	 * Implements the linear MCSS algorithm. See Weiss, figure 5.8.
	 * 
	 * @param seq
	 * @return the MCSS, with start and end indexes
	 */
	public static Result mcssLinear(int[] seq) {
		Result result = new Result();
		result.sum = 0;
		int thisSum = 0;

		int subseqStart = 0;
		for (int subseqEnd = 0; subseqEnd < seq.length; subseqEnd++) {
			thisSum += seq[subseqEnd];

			if (thisSum > result.sum) {
				result.sum = thisSum;
				result.startIndex = subseqStart;
				result.endIndex = subseqEnd;
			} else if (thisSum < 0) {
				// advances start to where end will be on NEXT iteration
				subseqStart = subseqEnd + 1;
				thisSum = 0;
			}
		}
		return result;
	}

	/**
	 * @param sequenceLength
	 * @return a sequence of random integers of the given length
	 */
	private static int[] getRandomSequence(int sequenceLength) {
		int[] result = new int[sequenceLength];
		for (int i = 0; i < result.length; i++) {
			result[i] = RANDOM_GENERATOR.nextInt(MAX_ABSOLUTE_VALUE * 2)
					- MAX_ABSOLUTE_VALUE;
		}
		return result;
	}
}
