package cos.tuk_tuk_driver.models

import com.google.gson.annotations.SerializedName

data class UploadDocsModal(

    @SerializedName("status") val status: Boolean,
    @SerializedName("documents") val documents: Documents,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: Data,
    @SerializedName("methods") val methods: List<String>

)

data class Documents(

    @SerializedName("driverDocuments") val driverDocuments: List<DocumentsData>,
    @SerializedName("vehicleDocuments") val vehicleDocuments: List<DocumentsData>

)

data class DocumentsData(

    @SerializedName("id") val id: Int,
    @SerializedName("provider_id") val provider_id: Int,
    @SerializedName("document_id") val document_id: String,
    @SerializedName("url") val url: String,
    @SerializedName("additonal_doc") val additonal_doc: String,
    @SerializedName("unique_id") val unique_id: String,
    @SerializedName("expiry") val expiry: String,
    @SerializedName("status") val status: String,
    @SerializedName("expires_at") val expires_at: String,
    @SerializedName("created_at") val created_at: String,
    @SerializedName("updated_at") val updated_at: String

)
