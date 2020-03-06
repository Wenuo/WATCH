package com.smartwatch.ywatch;
import android.widget.*;
import android.os.*;
import android.view.*;
import android.app.*;
import android.graphics.drawable.*;

public class WhiteActivity extends Activity
{private ImageView mAnimatorView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_animator);
		mAnimatorView=(ImageView)findViewById(R.id.mode_change_animator_view);
		mAnimatorView.setImageResource(R.drawable.custom_drawable_mode_translation_turn_night_v16);
		sendEmptyMessageDelayed(KeyStore.KEY_TAG_ANIMATOR_START, 300);
	}


    private InternalHandler mHandler;
    private class InternalHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg == null) return ;
            switch (msg.what) {
                case KeyStore.KEY_SKIP_ANIMATOR_FINISH:
                    finish();
                    break;
                case KeyStore.KEY_TAG_ANIMATOR_START:
                    startAnimator();
                    break;
                case KeyStore.KEY_TAG_ANIMATOR_STOP:
                    stopAnimator();
                    break;
                default:
                    break;
            }
        }

        /**
         *
         * */
        private void startAnimator() {
            Drawable drawable = mAnimatorView.getDrawable();
            if (drawable == null || !(drawable instanceof AnimationDrawable)) return ;
            ((AnimationDrawable) drawable).start();
            sendEmptyMessageDelayed(KeyStore.KEY_TAG_ANIMATOR_STOP, 1760);
        }

        /**
         *
         * */
        private void stopAnimator() {
            sendEmptyMessageDelayed(KeyStore.KEY_SKIP_ANIMATOR_FINISH, 360);

        }
    }

    /**
     * */
    private void sendEmptyMessageDelayed(int what, long delay) {
        if (mHandler == null) mHandler = new InternalHandler();
        mHandler.sendEmptyMessageDelayed(what, delay);
    }

	@Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }
	
}
