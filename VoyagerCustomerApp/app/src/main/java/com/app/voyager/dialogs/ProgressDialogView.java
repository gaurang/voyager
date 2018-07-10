package com.app.voyager.dialogs;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import com.app.voyager.R;

import java.util.ArrayList;

public class ProgressDialogView extends Dialog implements OnClickListener {
    String Message;
    int id;
    Context context;

    public ProgressDialogView(Context context, String Message) {
        super(context, R.style.Progressdialogthem);
        this.Message = Message;
        this.context = context;
        this.id = id;
    }

    View view1, view2, view3, view4, view5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = getLayoutInflater();
        View dialogview = inflater.inflate(R.layout.progressdialog, null);

        getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        TextView message = (TextView) dialogview.findViewById(R.id.message);
        message.setText(Message);

        view1 = dialogview.findViewById(R.id.view1);
        view2 = dialogview.findViewById(R.id.view2);
        view3 = dialogview.findViewById(R.id.view3);
        view4 = dialogview.findViewById(R.id.view4);
        view5 = dialogview.findViewById(R.id.view5);

        setContentView(dialogview);
        SetBouncingEffect();

    }

    @Override
    public void onBackPressed() {

    }

    public void SetDissmiss() {
        dismiss();
    }

    @Override
    public void onClick(View v) {

    }

    int animationcount = 0;

    private void SetBouncingEffect() {
        // TODO Auto-generated method stub
        BouncingAnimation(view1, 500, 0);
        BouncingAnimation(view2, 500, 100);
        BouncingAnimation(view3, 500, 200);
        BouncingAnimation(view4, 500, 300);
        BouncingAnimation(view5, 500, 400);
    }

    public void BouncingAnimation(View foundDevice, int animtime, int delay) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(animtime);
        animatorSet.setStartDelay(delay);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        ArrayList<Animator> animatorList = new ArrayList<Animator>();
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(foundDevice,
                "ScaleX", 0f, 1.2f, 1f);
        animatorList.add(scaleXAnimator);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(foundDevice,
                "ScaleY", 0f, 1.2f, 1f);
        animatorList.add(scaleYAnimator);
        animatorSet.playTogether(animatorList);
        foundDevice.setVisibility(View.VISIBLE);
        animatorSet.start();
        animatorSet.addListener(new AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // TODO Auto-generated method stub
                animationcount++;
                if (animationcount >= 5) {
                    animationcount = 0;
                    SetBouncingEffect();
                }

            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // TODO Auto-generated method stub

            }
        });
    }
    public  static int SUCCESS=1,ERROR=0;
    public void dismissanimation(int requestcode){
        dismiss();
    }
}
