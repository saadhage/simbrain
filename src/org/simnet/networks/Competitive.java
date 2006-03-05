package org.simnet.networks;

import java.util.Iterator;

import org.simnet.interfaces.Network;
import org.simnet.interfaces.Neuron;
import org.simnet.interfaces.Synapse;
import org.simnet.layouts.Layout;
import org.simnet.neurons.LinearNeuron;
import org.simnet.neurons.SigmoidalNeuron;

/**
 * <b>Competitive</b> implements a Competitive network.
 *
 * @author Jeff Yoshimi
 */
public class Competitive extends Network {

    /** Learning rate. */
    private double epsilon = .1;

    /** Winner value. */
    private double winValue = 1;

    /** loser value. */
    private double loseValue = 0;

    /** Number of neurons. */
    private int numNeurons = 3;

    /**
     * Default constructor used by Castor.
     */
    public Competitive() {
    }

    /**
     * Constructs a competitive network with specified number of neurons.
     *
     * @param numNeurons size of this network in neurons
     * @param layout Defines how neurons are to be layed out
     */
    public Competitive(final int numNeurons, final Layout layout) {
        super();
        for (int i = 0; i < numNeurons; i++) {
            this.addNeuron(new LinearNeuron());
        }
        layout.layoutNeurons(this);
    }

    private boolean normalizeInputs = true;
    private boolean useLeakyLearning = false;
    private double leakyEpsilon = epsilon / 4;
    double max, val, activation;
    int winner;
    
    /**
     * Update the network.
     */
    public void update() {

        updateAllNeurons();
        max = 0;
        winner = 0;

        // Determine Winner
        for (int i = 0; i < neuronList.size(); i++) {
            Neuron n = (Neuron) neuronList.get(i);
            if (n.getActivation() > max) {
                max = n.getActivation();
                winner = i;
            }
        }

        // Update weights on winning neuron
        for (int i = 0; i < neuronList.size(); i++) {
            Neuron neuron = ((Neuron) neuronList.get(i));
            // Don't update weights if no incoming lines have greater than zero activation
            if (neuron.getNumberOfActiveInputs(0) == 0) {
                return;
            }
            if (i == winner) {
                neuron.setActivation(winValue);

                // Apply learning rule
                for (Iterator j = neuron.getFanIn().iterator(); j.hasNext();) {
                  Synapse incoming = (Synapse) j.next();
                  activation = incoming.getSource().getActivation();

                  if (normalizeInputs) {
                      activation /= neuron.getTotalInput();
                  }

                  val =  incoming.getStrength() + epsilon * (activation - incoming.getStrength());
                  incoming.setStrength(val);
              }
            } else {
                neuron.setActivation(loseValue);
                if (useLeakyLearning) {
                    for (Iterator j = neuron.getFanIn().iterator(); j.hasNext();) {
                      Synapse incoming = (Synapse) j.next();
                      activation = incoming.getSource().getActivation();
                      if (normalizeInputs) {
                          activation /= neuron.getTotalInput();
                      }
                      val = incoming.getStrength() + leakyEpsilon * (activation - incoming.getStrength());
                      incoming.setStrength(val);
                    }
                }
            }
        }
        //normalizeIncomingWeights();
    }

    /**
     * Normalize  weights coming in to this network, separtely for each neuron.
     */
    public void normalizeIncomingWeights() {

        for (Iterator i = neuronList.iterator(); i.hasNext();) {
            Neuron n = (Neuron) i.next();
            double normFactor = n.getSummedIncomingWeights();
            for (Iterator j = n.getFanIn().iterator(); j.hasNext();) {
                Synapse s = (Synapse) j.next();
                s.setStrength(s.getStrength() / normFactor);
            }
        }
    }

    /**
     * Normalize all weights coming in to this network.
     */
    public void normalizeAllIncomingWeights() {

        double normFactor = getSummedIncomingWeights();
        for (Iterator i = neuronList.iterator(); i.hasNext();) {
            Neuron n = (Neuron) i.next();
            for (Iterator j = n.getFanIn().iterator(); j.hasNext();) {
                Synapse s = (Synapse) j.next();
                s.setStrength(s.getStrength() / normFactor);
            }
        }
    }

    /**
     * Returns the sum of all incoming weights to this network.
     *
     * @return the sum of all incoming weights to this network.
     */
    private double getSummedIncomingWeights() {
        double ret = 0;
        for (Iterator i = neuronList.iterator(); i.hasNext();) {
            Neuron n = (Neuron) i.next();
            ret += n.getSummedIncomingWeights();
        }
        return ret;
    }

    /**
     * Return the epsilon.
     *
     * @return the epsilon value.
     */
    public double getEpsilon() {
        return epsilon;
    }

    /**
     * Sets epsilon.
     *
     * @param epsilon The new epsilon value.
     */
    public void setEpsilon(final double epsilon) {
        this.epsilon = epsilon;
    }

    /**
     * Return the loser value.
     *
     * @return the loser Value
     */
    public final double getLoseValue() {
        return loseValue;
    }

    /**
     * Sets the loser value.
     *
     * @param loseValue The new loser value
     */
    public final void setLoseValue(final double loseValue) {
        this.loseValue = loseValue;
    }

    /**
     * Return the winner value.
     *
     * @return the winner value
     */
    public final double getWinValue() {
        return winValue;
    }

    /**
     * Sets the winner value.
     *
     * @param winValue The new winner value
     */
    public final void setWinValue(final double winValue) {
        this.winValue = winValue;
    }

    /**
     * @return The initial number of neurons.
     */
    public int getNumNeurons() {
        return numNeurons;
    }

}
