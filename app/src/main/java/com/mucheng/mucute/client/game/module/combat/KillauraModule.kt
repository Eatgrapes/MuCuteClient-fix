package com.mucheng.mucute.client.game.module.combat

import com.mucheng.mucute.client.game.Module
import com.mucheng.mucute.client.game.ModuleCategory
import com.mucheng.mucute.client.game.entity.Entity
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
import org.cloudburstmc.protocol.bedrock.packet.PlayerAuthInputPacket

class KillauraModule : Module("killaura", ModuleCategory.Combat) {

    private var rangeValue by floatValue("Range", 3.7f, 2f..7f)

    private var attackInterval by intValue("Delay", 5, 1..20)

    override fun beforePacketBound(packet: BedrockPacket): Boolean {
        if (!isEnabled) {
            return false
        }

        if (packet is PlayerAuthInputPacket && packet.tick % attackInterval == 0L) {
            val closestEntities = searchForClosestEntities()
            if (closestEntities.isEmpty()) {
                return false
            }

            closestEntities.forEach { entity ->
                session.localPlayer.attack(entity)
            }
        }
        return false
    }

    private fun searchForClosestEntities(): List<Entity> {
        return session.level.entityMap.values
            .filter { it.distance(session.localPlayer) < rangeValue }
    }

}