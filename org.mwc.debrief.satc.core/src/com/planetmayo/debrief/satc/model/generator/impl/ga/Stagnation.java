
package com.planetmayo.debrief.satc.model.generator.impl.ga;

import org.uncommons.watchmaker.framework.PopulationData;
import org.uncommons.watchmaker.framework.TerminationCondition;

public class Stagnation implements TerminationCondition
{
	private final int generationLimit;
	private double bestFitness;
	private int fittestGeneration;

	public Stagnation(int generationLimit)
	{
		this.generationLimit = generationLimit;
	}

	public boolean shouldTerminate(PopulationData<?> populationData)
	{
		double fitness = populationData.getBestCandidateFitness();
		if (populationData.getGenerationNumber() == 0
				|| hasFitnessImproved(fitness))
		{
			bestFitness = fitness;
			fittestGeneration = populationData.getGenerationNumber();
		}

		return populationData.getGenerationNumber() - fittestGeneration >= generationLimit;
	}

	private boolean hasFitnessImproved(double fitness)
	{
		if (Math.abs(fitness - bestFitness) < 0.01) 
		{
			return false;
		}
		return fitness < bestFitness;
	}
}
