package swarm.swarmcomposerapp;


import android.graphics.drawable.Drawable;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import swarm.swarmcomposerapp.ActivitiesAndViews.DetailActivity;

@RunWith(RobolectricTestRunner.class)
public class DetailActivityTest {

    @Test
    public void resourceIDTest(){
        DetailActivity detail = Robolectric.setupActivity(DetailActivity.class);
        int id = detail.getResources().getIdentifier("TP_Modeller_10".toLowerCase(),
                "drawable",detail.getPackageName());
        Assert.assertTrue(id != 0);
        Drawable drawable = detail.getResources().getDrawable(id);
        Assert.assertNotNull(drawable);
    }



}
