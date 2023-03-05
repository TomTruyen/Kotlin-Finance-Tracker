package com.tomtruyen.security.token

import java.util.UUID

interface TokenService {
    suspend fun generate(
        config: TokenConfig,
        userId: UUID,
        isUpdate: Boolean = false
    ): TokenPair
}