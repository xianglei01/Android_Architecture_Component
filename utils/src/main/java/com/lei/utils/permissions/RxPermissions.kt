package com.lei.utils.permissions

import android.annotation.TargetApi
import android.app.Activity
import android.os.Build
import android.text.TextUtils
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.annotations.NonNull
import io.reactivex.functions.Function
import io.reactivex.subjects.PublishSubject


/**
 * created by xianglei
 * 2019/11/5 14:10
 */
class RxPermissions {

    companion object {

        val TAG = RxPermissions::class.java.simpleName
        val TRIGGER = Any()
    }

    @VisibleForTesting
    var mRxPermissionsFragment: Lazy<RxPermissionsFragment>

    constructor(@NonNull activity: FragmentActivity) {
        mRxPermissionsFragment = getLazySingleton(activity.supportFragmentManager)
    }

    constructor(@NonNull fragment: Fragment) {
        mRxPermissionsFragment = getLazySingleton(fragment.childFragmentManager)
    }

    @NonNull
    private fun getLazySingleton(@NonNull fragmentManager: FragmentManager): Lazy<RxPermissionsFragment> {
        return object : Lazy<RxPermissionsFragment> {

            private var rxPermissionsFragment: RxPermissionsFragment? = null

            @Synchronized
            override fun get(): RxPermissionsFragment {
                if (rxPermissionsFragment == null) {
                    rxPermissionsFragment = getRxPermissionsFragment(fragmentManager)
                }
                return rxPermissionsFragment!!
            }

        }
    }

    private fun getRxPermissionsFragment(@NonNull fragmentManager: FragmentManager): RxPermissionsFragment {
        var rxPermissionsFragment: RxPermissionsFragment? = findRxPermissionsFragment(fragmentManager)
        val isNewInstance = rxPermissionsFragment == null
        if (isNewInstance) {
            rxPermissionsFragment = RxPermissionsFragment()
            fragmentManager
                    .beginTransaction()
                    .add(rxPermissionsFragment, TAG)
                    .commitNow()
        }
        return rxPermissionsFragment!!
    }

    private fun findRxPermissionsFragment(@NonNull fragmentManager: FragmentManager): RxPermissionsFragment? {
        return fragmentManager.findFragmentByTag(TAG) as RxPermissionsFragment?
    }

    fun setLogging(logging: Boolean) {
        mRxPermissionsFragment.get().setLogging(logging)
    }

    /**
     * Map emitted items from the source observable into `true` if permissions in parameters
     * are granted, or `false` if not.
     *
     *
     * If one or several permissions have never been requested, invoke the related framework method
     * to ask the user if he allows the permissions.
     */
    private fun <T> ensure(vararg permissions: String): ObservableTransformer<T, Boolean> {
        return object : ObservableTransformer<T, Boolean> {
            override fun apply(o: Observable<T>): ObservableSource<Boolean> {
                return request(o, *permissions)
                        // Transform Observable<Permission> to Observable<Boolean>
                        .buffer(permissions.size)
                        .flatMap(object : Function<List<Permission>, ObservableSource<Boolean>> {
                            override fun apply(t: List<Permission>): ObservableSource<Boolean> {
                                if (t.isEmpty()) {
                                    // Occurs during orientation change, when the subject receives onComplete.
                                    // In that case we don't want to propagate that empty list to the
                                    // subscriber, only the onComplete.
                                    return Observable.empty()
                                }
                                // Return true if all permissions are granted.
                                t.forEach {
                                    if (!it.granted) {
                                        return Observable.just(false)
                                    }
                                }
                                return Observable.just(true)
                            }
                        })
            }
        }
    }

    /**
     * Map emitted items from the source observable into [Permission] objects for each
     * permission in parameters.
     *
     *
     * If one or several permissions have never been requested, invoke the related framework method
     * to ask the user if he allows the permissions.
     */
    fun <T> ensureEach(vararg permissions: String): ObservableTransformer<T, Permission> {
        return ObservableTransformer { o -> request(o, *permissions) }
    }

    /**
     * Map emitted items from the source observable into one combined [Permission] object. Only if all permissions are granted,
     * permission also will be granted. If any permission has `shouldShowRationale` checked, than result also has it checked.
     *
     *
     * If one or several permissions have never been requested, invoke the related framework method
     * to ask the user if he allows the permissions.
     */
    fun <T> ensureEachCombined(vararg permissions: String): ObservableTransformer<T, Permission> {
        return ObservableTransformer { o ->
            request(o, *permissions)
                    .buffer(permissions.size)
                    .flatMap { permissions ->
                        if (permissions.isEmpty()) {
                            Observable.empty()
                        } else Observable.just(Permission(permissions))
                    }
        }
    }

    /**
     * Request permissions immediately, **must be invoked during initialization phase
     * of your application**.
     * 返回是否授权 grant
     * 授权结果合并返回，全部都授权返回true，有一个未授权返回false
     */
    fun request(vararg permissions: String): Observable<Boolean> {
        return Observable.just(TRIGGER).compose(ensure<Any>(*permissions))
    }

    /**
     * Request permissions immediately, **must be invoked during initialization phase
     * of your application**.
     * 返回permission对象
     * 每个权限申请都单独返回授权结果
     */
    fun requestEach(vararg permissions: String): Observable<Permission> {
        return Observable.just(TRIGGER).compose(ensureEach<Any>(*permissions))
    }

    /**
     * Request permissions immediately, **must be invoked during initialization phase
     * of your application**.
     * 返回permission对象
     * 授权结果合并返回，全部授权返回成功，有一个拒绝则返回拒绝
     */
    fun requestEachCombined(vararg permissions: String): Observable<Permission> {
        return Observable.just(TRIGGER).compose(ensureEachCombined<Any>(*permissions))
    }

    private fun request(trigger: Observable<*>, vararg permissions: String): Observable<Permission> {
        require(!(permissions == null || permissions.isEmpty())) { "RxPermissions.request/requestEach requires at least one input permission" }
        return oneOf(trigger, pending(*permissions))
                .flatMap { requestImplementation(*permissions) }
    }

    private fun pending(vararg permissions: String): Observable<Any> {
        for (p in permissions) {
            if (!mRxPermissionsFragment.get().containsByPermission(p)) {
                return Observable.empty()
            }
        }
        return Observable.just(TRIGGER)
    }

    private fun oneOf(trigger: Observable<*>?, pending: Observable<*>): Observable<*> {
        return if (trigger == null) {
            Observable.just(TRIGGER)
        } else Observable.merge(trigger, pending)
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun requestImplementation(vararg permissions: String): Observable<Permission> {
        val list = ArrayList<Observable<Permission>?>(permissions.size)
        val unrequestedPermissions = ArrayList<String>()

        // In case of multiple permissions, we create an Observable for each of them.
        // At the end, the observables are combined to have a unique response.
        for (permission in permissions) {
            mRxPermissionsFragment.get().log("Requesting permission $permission")
            if (isGranted(permission)) {
                // Already granted, or not Android M
                // Return a granted Permission object.
                list.add(Observable.just(Permission(permission, true, false)))
                continue
            }

            if (isRevoked(permission)) {
                // Revoked by a policy, return a denied Permission object.
                list.add(Observable.just(Permission(permission, false, false)))
                continue
            }

            var subject = mRxPermissionsFragment.get().getSubjectByPermission(permission)
            // Create a new subject if not exists
            if (subject == null) {
                unrequestedPermissions.add(permission)
                subject = PublishSubject.create()
                mRxPermissionsFragment.get().setSubjectForPermission(permission, subject)
            }

            list.add(subject)
        }

        if (unrequestedPermissions.isNotEmpty()) {
            val unrequestedPermissionsArray = unrequestedPermissions.toTypedArray()
            requestPermissionsFromFragment(unrequestedPermissionsArray)
        }
        return Observable.concat(Observable.fromIterable(list))
    }

    /**
     * Invokes Activity.shouldShowRequestPermissionRationale and wraps
     * the returned value in an observable.
     *
     *
     * In case of multiple permissions, only emits true if
     * Activity.shouldShowRequestPermissionRationale returned true for
     * all revoked permissions.
     *
     *
     * You shouldn't call this method if all permissions have been granted.
     *
     *
     * For SDK &lt; 23, the observable will always emit false.
     */
    fun shouldShowRequestPermissionRationale(activity: Activity, vararg permissions: String): Observable<Boolean> {
        return if (!isMarshmallow()) {
            Observable.just(false)
        } else Observable.just(shouldShowRequestPermissionRationaleImplementation(activity, *permissions))
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun shouldShowRequestPermissionRationaleImplementation(activity: Activity, vararg permissions: String): Boolean {
        for (p in permissions) {
            if (!isGranted(p) && !activity.shouldShowRequestPermissionRationale(p)) {
                return false
            }
        }
        return true
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun requestPermissionsFromFragment(permissions: Array<String>) {
        mRxPermissionsFragment.get().log("requestPermissionsFromFragment " + TextUtils.join(", ", permissions))
        mRxPermissionsFragment.get().requestPermissions(permissions)
    }

    /**
     * Returns true if the permission is already granted.
     *
     *
     * Always true if SDK &lt; 23.
     */
    fun isGranted(permission: String): Boolean {
        return !isMarshmallow() || mRxPermissionsFragment.get().isGranted(permission)
    }

    /**
     * Returns true if the permission has been revoked by a policy.
     *
     *
     * Always false if SDK &lt; 23.
     */
    fun isRevoked(permission: String): Boolean {
        return isMarshmallow() && mRxPermissionsFragment.get().isRevoked(permission)
    }

    fun isMarshmallow(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }

    fun onRequestPermissionsResult(permissions: Array<String>, grantResults: IntArray) {
        mRxPermissionsFragment.get().onRequestPermissionsResult(permissions, grantResults, BooleanArray(permissions.size))
    }

    @FunctionalInterface
    interface Lazy<V> {
        fun get(): V
    }
}