package ru.kuchanov.huaweiandgoogleservices.analytics

import android.content.Context
import com.huawei.hms.analytics.HiAnalytics

class AnalyticsImpl(context: Context) : Analytics {

    private val huaweiAnalytics = HiAnalytics.getInstance(context)

    override fun send(event: AnalyticsEvent) {
        huaweiAnalytics.onEvent(event.key, event.data.toBundle())
    }
}