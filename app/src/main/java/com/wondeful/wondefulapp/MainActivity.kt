package com.wondeful.wondefulapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.wondeful.wondefulapp.adapters.CategoryAdapter
import com.wondeful.wondefulapp.adapters.ContentManager
import com.wondeful.wondefulapp.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity(), CategoryAdapter.Listener, Animation.AnimationListener {

    private lateinit var binding: ActivityMainBinding
    private var adapter: CategoryAdapter? = null
    private var interAd: InterstitialAd? = null
    // var timer: CountDownTimer? = null
    private var posMain: Int = 0
    private lateinit var inAnimation:Animation
    private lateinit var outAnimation:Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inAnimation = AnimationUtils.loadAnimation(this, R.anim.alpha_in)
        outAnimation = AnimationUtils.loadAnimation(this, R.anim.alpha_out)
        outAnimation.setAnimationListener(this)

        initAdMob()
        //запускаем рекламу
        (application as AppMainState).showAdIfAvailable(this){}
        initRcView()

        binding.imageBg.setOnClickListener {
            //getResult()
        }
    }

    private fun initRcView() = with(binding){
        adapter = CategoryAdapter(this@MainActivity)
        rcViewCat.layoutManager = LinearLayoutManager(
            this@MainActivity,
            LinearLayoutManager.HORIZONTAL,
            false)
        rcViewCat.adapter = adapter
        adapter?.submitList(ContentManager.list)
    }

    override fun onResume() {
        super.onResume()
        binding.adView.resume()
        loadInterAd()
    }

    override fun onPause() {
        super.onPause()
        binding.adView.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.adView.destroy()
    }

//    private fun getResult(){
//        var counter = 0
//        timer?.cancel()
//        timer = object : CountDownTimer(3000, 200){
//            override fun onTick(millisUntilFinished: Long) {
//                counter++
//                if (counter > 12) counter = 0
//                binding.imageBg.setImageResource(MainConst.imageList[counter])
//            }
//
//            override fun onFinish() {
//                getMessage()
//            }
//        }.start()
//    }

    private fun getMessage() = with(binding){
        tvMessage.startAnimation(inAnimation)
        tvName.startAnimation(inAnimation)
        imageBg.startAnimation(inAnimation)
        val currentArray = resources.getStringArray(MainConst.arrayList[posMain])
        val message = currentArray[Random.nextInt(currentArray.size)]
        val messageList = message.split("|") // разделение на текст и автора, выдает массив из 2 элементов
        tvMessage.text = messageList[0]
        tvName.text = messageList[1]
        //генерируем случайную картинку
        imageBg.setImageResource(MainConst.imageList[Random.nextInt(13)])
    }

    private fun initAdMob(){
        MobileAds.initialize(this)
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
    }

    private fun loadInterAd(){
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712",
            adRequest, object : InterstitialAdLoadCallback(){
                override fun onAdFailedToLoad(ad: LoadAdError) {
                    interAd = null
                }

                override fun onAdLoaded(ad: InterstitialAd) {
                    interAd = ad
                }
            })
    }

    private fun showInterAd(){
        if (interAd != null){
            interAd?.fullScreenContentCallback = object: FullScreenContentCallback(){
                override fun onAdDismissedFullScreenContent() {
                    showContent()
                    interAd = null
                    loadInterAd()
                }

                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    showContent()
                    interAd = null
                    loadInterAd()
                }

                override fun onAdShowedFullScreenContent() {
                    interAd = null
                    loadInterAd()
                }
            }
        } else {
            showContent()
        }
        interAd?.show(this)
    }

    private fun showContent(){
        Toast.makeText(this, "Запуск контента", Toast.LENGTH_LONG).show()
    }

    override fun onClick(position: Int) {
        binding.apply {
            tvMessage.startAnimation(outAnimation)
            tvName.startAnimation(outAnimation)
            imageBg.startAnimation(outAnimation)
        }
        posMain = position
        //getResult()

    }

    override fun onAnimationStart(animation: Animation?) {

    }

    override fun onAnimationEnd(animation: Animation?) {
        getMessage()
    }

    override fun onAnimationRepeat(animation: Animation?) {

    }
}