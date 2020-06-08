package cos.tuk_tuk_driver.utils

object URLHelper {

    //testing
//    const val BaseUrlImage = "http://server.visionvivante.com:8080/tuktuk/storage/app/"
//    const val BaseUrl = "http://server.visionvivante.com:8080/tuktuk/public/"

    const val BaseUrlImage = "http://server.visionvivante.com:8080/tuktuk_new/storage/app/"
    const val BaseUrl = "http://122.160.138.253:8080/tuktuk_new/public/"

    const val RegisterMobile = "api/provider/register-mobile"
    const val doLogin = "api/provider/oauth/token"
    const val VerifyOtp = "api/provider/verify-mobile"
    const val Resend = "api/provider/resend"
    const val MobileLogin = "api/provider/mobile-login"
    const val UpdateDriverData = "api/provider/driver-update"
    const val Logout = "api/provider/logout"
    const val UploadDocs = "api/provider/profile/document"
    const val GetUploadDocs = "api/provider/user-document-list"
    const val AddVehicle = "api/provider/vehicle/store"
    const val GetVehicle = "api/provider/vehicle/list"
    const val MakePrime = "api/provider/vehicle/"
    const val DeleteVehicle = "api/provider/vehicle/delete/"
    const val UpdateVehicle = "api/provider/vehicle/update/{id}"
    const val Services = "api/provider/services"
    const val PaymentsList = "api/provider/payment-methods/list"
    const val ChangePayments = "api/provider/change/payment-methods"
    const val ReportIssue = "api/provider/report-issue"
    const val Available = "api/provider/profile/available"


}
