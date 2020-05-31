package com.todo

import com.todo.auth.JwtService
import com.todo.auth.MySession
import com.todo.auth.hash
import com.todo.repository.DatabaseFactory
import com.todo.repository.TodoRepository
import com.todo.routes.todos
import com.todo.routes.users
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.jwt.jwt
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.locations.Location
import io.ktor.locations.Locations
import io.ktor.routing.routing
import io.ktor.sessions.Sessions
import io.ktor.sessions.cookie
import kotlin.collections.set

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(Locations) {
    }

    install(Sessions) {
        cookie<MySession>("MY_SESSION") {
            cookie.extensions["SameSite"] = "lax"
        }
    }

    // 1
    DatabaseFactory.init()
    val db = TodoRepository()
    val jwtService = JwtService()
    val hashFunction = { s: String -> hash(s) }

    install(Authentication) {
        jwt("jwt"){
            verifier(jwtService.verifier)
            realm = "Todo Server"
            validate {
                val payload = it.payload
                val claim = payload.getClaim("id")
                val claimString = claim.asInt()
                val user = db.findUser(claimString)
                user
            }
        }
    }

    install(ContentNegotiation) {
        gson {
        }
    }

    routing {
        users(db, jwtService, hashFunction)
        todos(db)
    }
}

const val API_VERSION = "/v1"
