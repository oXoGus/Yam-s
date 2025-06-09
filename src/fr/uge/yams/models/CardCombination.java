package fr.uge.yams.models;

public interface CardCombination extends Combination {

    @Override
    default double probability (int numGameElementMissing) {
		if (numGameElementMissing < 0 || numGameElementMissing > 5){
			throw new IllegalArgumentException();
		}
	
		double pow = 1.0;
		
		for (int i = 0; i < numGameElementMissing; i++){
			pow = pow * 1/13;
		}
		
		return pow;
	}
} 