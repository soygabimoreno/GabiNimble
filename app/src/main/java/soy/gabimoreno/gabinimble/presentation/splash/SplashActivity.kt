package soy.gabimoreno.gabinimble.presentation.splash

import android.animation.Animator
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_splash.*
import soy.gabimoreno.gabinimble.R
import soy.gabimoreno.gabinimble.presentation.main.MainActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        lav?.addAnimatorListener(
            object : Animator.AnimatorListener {
                val initialFadeInDuration = 500L

                override fun onAnimationStart(animation: Animator?) {
                    lav
                        .animate()
                        .alpha(1f)
                        .setDuration(initialFadeInDuration)
                        .start()
                }

                override fun onAnimationEnd(animation: Animator?) {
                    navigateToMain()
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                }

                override fun onAnimationRepeat(animation: Animator?) {}

                override fun onAnimationCancel(animation: Animator?) {}
            })
    }

    private fun navigateToMain() {
        MainActivity.launch(this)
        finish()
    }
}
