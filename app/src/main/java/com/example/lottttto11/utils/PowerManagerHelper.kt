package com.example.lottttto11.utils

import android.os.Build
import java.io.File

/**
 * Утилита для получения системных метрик устройства во время майнинга.
 *
 * Используется в:
 *  - MergedMiningEngine.updateStats() → getCpuTemperature()
 *
 * Важно: прямого API для температуры CPU в Android нет.
 * Читаем из системных файлов термальных зон (/sys/class/thermal/).
 * На разных устройствах пути отличаются — используем перебор
 * известных путей с fallback на 0f если ни один не доступен.
 */
object PowerManagerHelper {

    // Список известных путей к файлам температуры на разных Android-устройствах
    private val THERMAL_PATHS = listOf(
        "/sys/class/thermal/thermal_zone0/temp",
        "/sys/class/thermal/thermal_zone1/temp",
        "/sys/class/thermal/thermal_zone2/temp",
        "/sys/devices/system/cpu/cpu0/cpufreq/cpu_temp",
        "/sys/devices/virtual/thermal/thermal_zone0/temp",
        "/sys/kernel/debug/tegra_thermal/temp_tj"
    )

    /**
     * Возвращает температуру CPU в градусах Цельсия (Float).
     *
     * Большинство Android-устройств хранят температуру в миллиградусах
     * (например 45000 = 45.0°C), но некоторые — уже в градусах (например 45).
     * Автоматически определяем формат и нормализуем.
     *
     * @return температура в °C, или 0f если прочитать не удалось
     */
    fun getCpuTemperature(): Float {
        for (path in THERMAL_PATHS) {
            try {
                val file = File(path)
                if (!file.exists() || !file.canRead()) continue

                val raw = file.readText().trim().toFloatOrNull() ?: continue

                // Значения > 1000 — в миллиградусах, делим на 1000
                return if (raw > 1000f) raw / 1000f else raw

            } catch (e: Exception) {
                // Файл недоступен или нет прав — пробуем следующий путь
                continue
            }
        }

        // Fallback: температура недоступна на этом устройстве
        return 0f
    }

    /**
     * Возвращает уровень заряда батареи в процентах (0–100).
     * Зарезервировано для будущего использования в UI статистики.
     *
     * @return уровень заряда или -1 если недоступно
     */
    fun getBatteryLevel(): Int {
        return try {
            val file = File("/sys/class/power_supply/battery/capacity")
            if (file.exists() && file.canRead()) {
                file.readText().trim().toIntOrNull() ?: -1
            } else {
                -1
            }
        } catch (e: Exception) {
            -1
        }
    }

    /**
     * Проверяет, подключено ли зарядное устройство.
     * Зарезервировано для будущего использования.
     *
     * @return true если подключено, false если нет или недоступно
     */
    fun isCharging(): Boolean {
        return try {
            val file = File("/sys/class/power_supply/battery/status")
            if (file.exists() && file.canRead()) {
                val status = file.readText().trim()
                status == "Charging" || status == "Full"
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }
}
