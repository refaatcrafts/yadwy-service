package yadwy.app.yadwyservice

import org.junit.jupiter.api.Test
import org.springframework.modulith.core.ApplicationModules

class ModulithArchitectureTest {

    @Test
    fun `verify module boundaries`() {
        val modules = ApplicationModules.of(Application::class.java)
        modules.verify()
    }
}
