package com.lei.utils.permissions

import io.reactivex.Observable

/**
 * created by xianglei
 * 2019/11/5 14:12
 */
class Permission {

    /**
     * 权限名称
     */
    var name: String
    /**
     * 是否授权
     */
    var granted: Boolean
    /**
     * 用户是否勾选禁止询问
     */
    var shouldShowRequestPermissionRationale: Boolean

    constructor(name: String, granted: Boolean) : this(name, granted, false)

    constructor(name: String, granted: Boolean, shouldShowRequestPermissionRationale: Boolean) {
        this.name = name
        this.granted = granted
        this.shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale
    }

    constructor(permissions: List<Permission>) {
        name = combineName(permissions)
        granted = combineGranted(permissions)
        shouldShowRequestPermissionRationale = combineShouldShowRequestPermissionRationale(permissions)
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false

        val that = o as Permission?

        if (granted != that?.granted) return false
        return if (shouldShowRequestPermissionRationale != that.shouldShowRequestPermissionRationale) false else name == that.name
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + if (granted) 1 else 0
        result = 31 * result + if (shouldShowRequestPermissionRationale) 1 else 0
        return result
    }

    override fun toString(): String {
        return "Permission{" +
                "name='" + name + '\''.toString() +
                ", granted=" + granted +
                ", shouldShowRequestPermissionRationale=" + shouldShowRequestPermissionRationale +
                '}'.toString()
    }

    private fun combineName(permissions: List<Permission>): String {
        return Observable.fromIterable(permissions)
                .map { t -> t.name }.collectInto(StringBuilder(), { s, s2 ->
                    if (s.isEmpty()) {
                        s.append(s2)
                    } else {
                        s.append(", ").append(s2)
                    }
                }).blockingGet().toString()
    }

    private fun combineGranted(permissions: List<Permission>): Boolean {
        return Observable.fromIterable(permissions)
                .all { permission -> permission.granted }.blockingGet()
    }

    private fun combineShouldShowRequestPermissionRationale(permissions: List<Permission>): Boolean {
        return Observable.fromIterable(permissions)
                .any { permission -> permission.shouldShowRequestPermissionRationale }.blockingGet()
    }
}