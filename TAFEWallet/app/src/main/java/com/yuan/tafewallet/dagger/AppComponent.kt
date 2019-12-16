package com.yuan.tafewallet.dagger

import com.yuan.tafewallet.refund.RefundConfirmFragment
import com.yuan.tafewallet.refund.RefundFragment
import com.yuan.tafewallet.topup.TopupConfirmFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, PresenterModule::class])
interface AppComponent {
    fun injectTopupConfirmFragment(target: TopupConfirmFragment)

    fun injectRefundConfirmFragment(target: RefundConfirmFragment)

    fun injectRefundFragment(target: RefundFragment)
}