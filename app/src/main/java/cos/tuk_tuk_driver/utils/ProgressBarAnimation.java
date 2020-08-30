package cos.tuk_tuk_driver.utils;

import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ProgressBar;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

public class ProgressBarAnimation extends Animation {
    private ProgressBar progressBar;
    private CircularProgressBar progressBar2;
    private float from;
    private float  to;

    public ProgressBarAnimation(ProgressBar progressBar, float from, float to) {
        super();
        this.progressBar = progressBar;
        this.from = from;
        this.to = to;
    }

    public ProgressBarAnimation(CircularProgressBar progressBar,float from,float to) {
        super();
        this.progressBar2 = progressBar;
        this.from = from;
        this.to = to;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        float value = from + (to - from) * interpolatedTime;
        if(progressBar!=null)
            progressBar.setProgress((int) value);
        else if(progressBar2!= null)
            progressBar2.setProgress(value);
    }

}