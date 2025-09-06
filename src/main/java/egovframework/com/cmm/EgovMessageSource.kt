package egovframework.com.cmm

import org.springframework.context.MessageSource
import org.springframework.context.support.ReloadableResourceBundleMessageSource
import java.util.*

class EgovMessageSource : ReloadableResourceBundleMessageSource(), MessageSource {

    private lateinit var reloadableResourceBundleMessageSource: ReloadableResourceBundleMessageSource

    /** setter (Java의 setReloadableResourceBundleMessageSource 대응) */
    fun setReloadableResourceBundleMessageSource(
        delegate: ReloadableResourceBundleMessageSource
    ) {
        this.reloadableResourceBundleMessageSource = delegate
    }

    /** getter (Java의 getReloadableResourceBundleMessageSource 대응) */
    fun getReloadableResourceBundleMessageSource(): ReloadableResourceBundleMessageSource =
        reloadableResourceBundleMessageSource

    /** Default Locale로 메시지 조회 */
    fun getMessage(code: String): String =
        getMessage(code, Locale.getDefault())

    /** 지정 Locale로 메시지 조회 */
    fun getMessage(code: String, locale: Locale): String =
        reloadableResourceBundleMessageSource.getMessage(code, null, locale)


}