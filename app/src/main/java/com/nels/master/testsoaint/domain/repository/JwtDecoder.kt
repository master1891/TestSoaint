package com.nels.master.testsoaint.domain.repository

import com.nels.master.testsoaint.domain.model.Usuario

interface JwtDecoder {
    fun decodificarJwt(token: String): Usuario
}
