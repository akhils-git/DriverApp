package com.girfalco.driverapp.network.model

import kotlinx.serialization.Serializable

@Serializable
data class PersonResult(
    val UserTypeName: String? = null,
    val ID: Int? = null,
    val FirstName: String? = null,
    val LastName: String? = null,
    val Email: String? = null,
    val CountryCode: String? = null,
    val Mobile: String? = null,
    val Country: String? = null,
    val ZipCode: String? = null,
    val Address: String? = null,
    val BirthDate: String? = null,
    val StaffCode: String? = null,
    val JobID: Int? = null,
    val UserType: Int? = null,
    val ReportingID: Int? = null,
    val AuthenticationType: String? = null,
    val AuthenticationValue: String? = null,
    val HiredDate: String? = null,
    val TerminationDate: String? = null,
    val LicenseID: String? = null,
    val ValidTill: String? = null,
    val DepartmentID: Int? = null,
    val Status: String? = null,
    val CreatedBy: Int? = null,
    val CreatedDate: String? = null,
    val LastModifiedBy: Int? = null,
    val LastModifiedDate: String? = null,
    val Image: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val UserName: String? = null,
    val VGroup: String? = null,
    val Role: String? = null,
    val Appuser: Int? = null,
    val CompanyID: Int? = null,
    val bySuperadmin: String? = null,
    val WaslIdentityNumber: String? = null,
    val IdentityNumber: String? = null,
    val iButtonValue: String? = null,
    val waslStatus: String? = null,
    val PasswordUpdatedAt: String? = null,
    val passwordTokenID: String? = null,
    val GirfalcoUser: String? = null,
    val DefaultHomePage: String? = null,
    val FCMData: String? = null
)

@Serializable
data class PersonResponse(
    val status: Int = 0,
    val results: List<PersonResult> = emptyList(),
    val message: String? = null
)
