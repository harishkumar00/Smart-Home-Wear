package rocks.keyless.app.android.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import rocks.keyless.app.android.data.GetLockDetailsResponse
import rocks.keyless.app.android.data.UpdateDeviceRequest

data class UpdateDeviceResponse(
    val success: Boolean,
    val message: String
)

interface ApiService {

    @PUT("api/devices/{deviceId}")
    suspend fun updateDeviceDetails(
        @Path("deviceId") deviceId: String,
        @Body deviceDetails: UpdateDeviceRequest
    ): UpdateDeviceResponse

    @GET("api/devices/{deviceId}")
    suspend fun getDeviceDetails(
        @Path("deviceId") deviceId: String,
    ): GetLockDetailsResponse
}

