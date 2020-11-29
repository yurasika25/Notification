package ru.startandroid.develop.testtask

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.RemoteViews
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener

class MainActivity : FragmentActivity() {
    lateinit var notificationManager : NotificationManager
    lateinit var notificationChannel : NotificationChannel
    lateinit var builder : Notification.Builder
    private val channelId = "i.apps.notifications"
    private val description = "Test notification"

    var pager: ViewPager? = null
    var pagerAdapter: PagerAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn = findViewById<ImageButton>(R.id.createBT)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        btn.setOnClickListener {

            val intent = Intent(this,AfterNotification::class.java)
            val pendingIntent = PendingIntent.getActivity(this,
                0,intent, PendingIntent.FLAG_UPDATE_CURRENT)

            val contentView = RemoteViews(packageName,R.layout.activity_after_notification)
            contentView.setTextViewText(R.id.tv_title,"You create a notification")
            contentView.setTextViewText(R.id.tv_content,"Notification 1")

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationChannel = NotificationChannel(channelId,description,NotificationManager.IMPORTANCE_HIGH)
                notificationChannel.enableLights(true)
                notificationChannel.lightColor = Color.GREEN
                notificationChannel.enableVibration(false)
                notificationManager.createNotificationChannel(notificationChannel)

                builder = Notification.Builder(this,channelId)
                    .setContent(contentView)
                    .setSmallIcon(R.drawable.notifications)
                    .setLargeIcon(BitmapFactory.decodeResource(this.resources,R.drawable.notifications))
                    .setContentIntent(pendingIntent)
            }else{

                builder = Notification.Builder(this)
                    .setContent(contentView)
                    .setSmallIcon(R.drawable.notifications)
                    .setLargeIcon(BitmapFactory.decodeResource(this.resources,R.drawable.notifications))
                    .setContentIntent(pendingIntent)
            }
            notificationManager.notify(1234,builder.build())
        }

        pager = findViewById<View>(R.id.pager) as ViewPager
        pagerAdapter = MyFragmentPagerAdapter(supportFragmentManager)
        pager!!.adapter = pagerAdapter
        pager!!.setOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                Log.d(
                    TAG,
                    "onPageSelected, position = $position")
            }

            override fun onPageScrolled(
                position: Int, positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    private inner class MyFragmentPagerAdapter(fm: FragmentManager?) :

        FragmentPagerAdapter(fm!!) {
        override fun getItem(position: Int): Fragment {
            return PageFragment.newInstance(position )
        }

        override fun getPageTitle(position: Int): CharSequence {
            return "Title $position"
        }

        override fun getCount(): Int {
            return PAGE_COUNT
        }
    }

    companion object {
        const val TAG = "myLogs"
        const val PAGE_COUNT = 100
    }
}

