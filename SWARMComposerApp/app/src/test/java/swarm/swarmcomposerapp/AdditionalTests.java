package swarm.swarmcomposerapp;

import org.junit.Assert;
import org.junit.Test;

import swarm.swarmcomposerapp.ActivitiesAndViews.CompositionView;

public class AdditionalTests {
    @Test
    public void hasPNGEnding(){
        String testString ="blabla.png";
        Assert.assertFalse(CompositionView.removePNGEnding(testString).endsWith(".png"));
    }

    @Test
    public void hasNoPNGEnding(){
        String testString ="blabla";
        Assert.assertTrue(CompositionView.removePNGEnding(testString) == testString);
    }


}
