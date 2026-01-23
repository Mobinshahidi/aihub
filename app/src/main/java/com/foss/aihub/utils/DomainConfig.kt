package com.foss.aihub.utils

import kotlinx.serialization.Serializable

@Serializable
data class RemoteDomainConfig(
    val version: String,
    val service_domains: Map<String, List<String>>,
    val always_blocked_domains: Map<String, List<String>>,
    val common_auth_domains: List<String>,
    val tracking_params: List<String>
)