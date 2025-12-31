package org.lifetrack.ltapp.core.utils

import org.lifetrack.ltapp.model.data.dclass.LoginInfo
import org.lifetrack.ltapp.model.data.dclass.ProfileInfo
import org.lifetrack.ltapp.model.data.dclass.SignUpInfo
import org.lifetrack.ltapp.model.data.dto.LoginRequest
import org.lifetrack.ltapp.model.data.dto.Message
import org.lifetrack.ltapp.model.data.dto.SignUpRequest
import org.lifetrack.ltapp.model.data.dto.UserDataResponse
import org.lifetrack.ltapp.model.roomdb.MessageEntity

fun Message.toEntity(): MessageEntity{
    return MessageEntity(
        id = this.id,
        text = this.text,
        isFromPatient = this.isFromPatient,
        timestamp = this.timestamp,
        type = this.type
    )
}

fun LoginInfo.toLoginRequest(): LoginRequest{
    return LoginRequest(
        emailAddress = this.emailAddress,
        password = this.password
    )
}

fun SignUpInfo.toSignUpRequest(): SignUpRequest{
    return SignUpRequest(
        fullName = this.fullName,
        userName = this.userName,
        password = this.password,
        emailAddress = this.emailAddress,
        phoneNumber = this.phoneNumber.toLong(),
    )
}


fun UserDataResponse.toUserProfileInformation(): ProfileInfo{
    val displayName = this.fullName ?: "N/A"
    return ProfileInfo(
        userName = this.userName.replaceFirstChar { it.uppercase() },
        userEmail = this.emailAddress,
        userFullName = displayName,
        userPhoneNumber = this.phoneNumber.toString(),
        userInitials = ZetuZetuUtil.generateInitials(displayName),
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}

