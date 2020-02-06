
package com.planetmayo.debrief.satc.model.generator.impl.sa;

import java.util.Random;

public interface SAFunctions
{
	
	/**
	 * Function to change temperature based on saParameters, current temperature and current step
	 * Temperature must decrease from step to step
	 * @param parameters - parameters of current algorithm
	 * @param T - current temperature
	 * @param step - current step
	 * @return new temperature
	 */
	double changeTemprature(SAParameters parameters, double T, int step);
	
	/**
	 * returns random distance for new neighbor point based on current temperature 
	 * and choosen random algorithm
	 * @param parameters
	 * @param T - current temperature
	 * @return
	 */
	double neighborDistance(SAParameters parameters, Random rnd, double T);
	
	/**
	 * returns probability to accept worse solution based on 
	 * current score, new score and current temperature
	 * @param eCur - current score
	 * @param eNew - new score
	 * @param T - current temperature
	 * @return probability [0, 1]	 
	 */
	double probabilityToAcceptWorse(SAParameters parameters, double T, double eCur, double eNew);
}
