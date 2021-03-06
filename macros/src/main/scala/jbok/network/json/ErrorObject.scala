package jbok.network.json

import io.circe.Json
import io.circe.generic.JsonCodec

@JsonCodec
case class ErrorObject(
    code: ErrorCode,
    message: String,
    data: Option[Json]
)
