package coo.apps.meteoray.network.request

class LimitRequest : BaseRequest() {
    override var method: Method
        get() = Method.GET
        set(value) {}

    override var path: String = "limits.php"
}