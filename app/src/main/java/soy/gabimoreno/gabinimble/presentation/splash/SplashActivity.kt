package soy.gabimoreno.gabinimble.presentation.splash

import android.animation.Animator
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import soy.gabimoreno.gabinimble.R
import soy.gabimoreno.gabinimble.databinding.ActivitySplashBinding
import soy.gabimoreno.gabinimble.presentation.main.MainActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.lav.addAnimatorListener(
            object : Animator.AnimatorListener {
                val initialFadeInDuration = 500L

                override fun onAnimationStart(animation: Animator?) {
                    binding.lav
                        .animate()
                        .alpha(1f)
                        .setDuration(initialFadeInDuration)
                        .start()
                }

                override fun onAnimationEnd(animation: Animator?) {
                    navigateToMain()
                    overridePendingTransition(
                        R.anim.fade_in,
                        R.anim.fade_out
                    )
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
