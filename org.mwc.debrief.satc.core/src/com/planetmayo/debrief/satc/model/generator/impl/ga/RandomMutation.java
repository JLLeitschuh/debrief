
package com.planetmayo.debrief.satc.model.generator.impl.ga;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.uncommons.maths.random.Probability;

import com.planetmayo.debrief.satc.model.legs.StraightLeg;
import com.planetmayo.debrief.satc.model.legs.StraightRoute;

public class RandomMutation extends AbstractMutation
{
	private final RoutesCandidateFactory candidateFactory;

  public RandomMutation(List<StraightLeg> legs, Probability mutationProbability, RoutesCandidateFactory candidateFactory)
	{
		super(legs, mutationProbability);
		this.candidateFactory = candidateFactory;
	}

	@Override
	protected List<StraightRoute> mutate(List<StraightRoute> candidate, Random rng)
	{
		List<StraightRoute> random = candidateFactory.generateRandomCandidate(rng);
		List<StraightRoute> mutated = new ArrayList<StraightRoute>();
		for (int i = 0; i < candidate.size(); i++) 
		{
			StraightRoute res = candidate.get(i);
			if (! mutationProbability.nextValue().nextEvent(rng)) 
			{
				for (int repeat = 0; repeat < 5; repeat++)
				{
					res = random.get(i);
					legs.get(i).decideAchievableRoute(res);
					if (res.isPossible())
					{
						break;
					}
					random = candidateFactory.generateRandomCandidate(rng);
				}
				if (! res.isPossible()) 
				{
					res = candidate.get(i);
				}
			}
			mutated.add(res);			
		}
		return mutated;
	}
}
