
public class DynProgChoice {
	
	public static int[] greedyChoice(int C[], int U[], int N) {
		int k = U.length;
		int[] Q = new int[k];
		int bestUtilityIndex = 0;
		for (int i=0; i<k; i++) {
			Q[i] = 0;
		}
		int minCost = C[0];
		for (int i=0; i<k; i++) {
			if (C[i] < minCost) {
				minCost = C[i];
			}
		}
		while (N > minCost) {
			int bestUtility = 0;
			for (int i=0; i<k; i++) {
				if (U[i] > bestUtility && C[i] < N) {
					bestUtility = U[i];
					bestUtilityIndex = i;
				}
			}
			Q[bestUtilityIndex] = N/C[bestUtilityIndex];
			N = N - (Q[bestUtilityIndex] * C[bestUtilityIndex]);
		}
		return Q;
	}
	
	public static int maxUtility(int C[], int U[], int N) {
		int k = U.length;
		int minCost = C[0];
		for (int i = 0; i < k; i++) {
			if (C[i] < minCost) {
				minCost = C[i];
			}
		}
		int best = 0;
		if (N < minCost) {
			return 0;
		} else {
			for (int i=0; i < k; i++) {
				if ((U[i] + maxUtility(C, U, N-C[i])) > best && N >= C[i]) {
					best = U[i] + maxUtility(C, U, (N-C[i]));
				}
			}
		}
		return best;
		
	}
	
	
    public static void main(String[] args){	
    	int[] C = {2, 6, 8, 10};
    	int[] U = {1, 5, 8, 9};
    	int N = 26;
    	
    	int best = maxUtility(C, U, N);
    	System.out.println(best);
    	
    	int[] greedyChoiceResult = greedyChoice(C, U, N);
    	for (int i=0; i<greedyChoiceResult.length; i++) {
    		System.out.println(greedyChoiceResult[i]);
    	}
    	
    	
    }

}
