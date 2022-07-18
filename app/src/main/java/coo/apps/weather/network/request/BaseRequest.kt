package coo.apps.weather.network.request


abstract class BaseRequest {

    // ===========================================================
    // Constants
    // ===========================================================

    enum class Method {
        GET,
        POST
    }


    // ===========================================================
    // Fields
    // ===========================================================

    val defaultHeaders: Map<String, String> = mutableMapOf("Content-Type" to "application/json",
                                                            "charset" to "utf-8")

    val defaultUrlParams: HashMap<String, Any> = hashMapOf()

    abstract var method: Method
    open var path: String = ""
    open var baseUrl: String = "http://api.wassf.net/"
    open var header: Map<String, String>? = null
    open var body: String? = null
    open var queryParameter: MutableMap<String, Any>? = null

    // ===========================================================
    // Constructors
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

}