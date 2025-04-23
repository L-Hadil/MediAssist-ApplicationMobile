package com.mediassist.network



import com.mediassist.model.Patient
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Body

interface ApiService {
    @GET("patients")
    suspend fun getPatients(): List<Patient>

    @POST("patients")
    suspend fun addPatient(@Body patient: Patient): Patient
}
