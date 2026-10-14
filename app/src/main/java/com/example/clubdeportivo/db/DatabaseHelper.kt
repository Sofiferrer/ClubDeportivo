package com.example.clubdeportivo.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.clubdeportivo.models.Alumno
import com.example.clubdeportivo.models.Pago
import com.example.clubdeportivo.models.Usuario
import java.io.BufferedReader
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DatabaseHelper(private val appContext: Context) :
    SQLiteOpenHelper(appContext.applicationContext, DATABASE_NAME, null, 1) {

    companion object {
        const val DATABASE_NAME = "ClubDeportivo.db"

        // Singleton
        @Volatile
        private var instance: DatabaseHelper? = null

        fun getInstance(context: Context): DatabaseHelper {
            return instance ?: synchronized(this) {
                instance ?: DatabaseHelper(context.applicationContext).also { instance = it }
            }
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        executeSqlScript(db, "init_db.sql")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS pagos")
        db.execSQL("DROP TABLE IF EXISTS alumnos")
        db.execSQL("DROP TABLE IF EXISTS usuarios")
        onCreate(db)
    }

    override fun onConfigure(db: SQLiteDatabase) {
        super.onConfigure(db)
        db.setForeignKeyConstraintsEnabled(true)
    }

    override fun onOpen(db: SQLiteDatabase) {
        super.onOpen(db)
        ensureHabilitadoColumnExists(db)
    }

    private fun ensureHabilitadoColumnExists(db: SQLiteDatabase) {
        try {
            val cursor = db.rawQuery("PRAGMA table_info(alumnos)", null)
            var hasHabilitado = false
            if (cursor.moveToFirst()) {
                val nameIndex = cursor.getColumnIndex("name")
                do {
                    if (nameIndex != -1 && cursor.getString(nameIndex) == "habilitado") {
                        hasHabilitado = true
                        break
                    }
                } while (cursor.moveToNext())
            }
            cursor.close()

            if (!hasHabilitado) {
                db.execSQL("ALTER TABLE alumnos ADD COLUMN habilitado INTEGER NOT NULL DEFAULT 1")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun executeSqlScript(db: SQLiteDatabase, scriptName: String) {
        try {
            val inputStream = appContext.assets.open(scriptName)
            val reader = BufferedReader(InputStreamReader(inputStream))
            val statement = StringBuilder()
            var line: String?

            while (reader.readLine().also { line = it } != null) {
                val trimmed = line!!.trim()
                if (trimmed.isEmpty() || trimmed.startsWith("--")) {
                    continue
                }
                statement.append(trimmed).append(" ")
                if (trimmed.endsWith(";")) {
                    val sql = statement.toString().trim()
                    if (sql.isNotEmpty()) {
                        db.execSQL(sql)
                    }
                    statement.setLength(0)
                }
            }
            reader.close()
        } catch (e: Exception) {
            e.printStackTrace()
            createDefaultTablesFallback(db)
        }
    }

    private fun createDefaultTablesFallback(db: SQLiteDatabase) {
        db.execSQL("DROP TABLE IF EXISTS pagos")
        db.execSQL("DROP TABLE IF EXISTS alumnos")
        db.execSQL("DROP TABLE IF EXISTS usuarios")

        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS usuarios (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT NOT NULL UNIQUE,
                password TEXT NOT NULL,
                nombre TEXT NOT NULL,
                apellido TEXT NOT NULL
            );
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS alumnos (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                apellido TEXT NOT NULL,
                dni TEXT NOT NULL UNIQUE,
                es_socio INTEGER NOT NULL DEFAULT 0,
                apto_fisico INTEGER NOT NULL DEFAULT 0,
                fecha_alta TEXT NOT NULL DEFAULT (date('now')),
                fecha_vencimiento TEXT,
                habilitado INTEGER NOT NULL DEFAULT 1
            );
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS pagos (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                alumno_id INTEGER NOT NULL,
                monto REAL NOT NULL,
                metodo_pago TEXT NOT NULL,
                cuotas INTEGER NOT NULL DEFAULT 1,
                fecha_pago TEXT NOT NULL DEFAULT (datetime('now', 'localtime')),
                periodo_desde TEXT NOT NULL,
                periodo_hasta TEXT NOT NULL,
                FOREIGN KEY (alumno_id) REFERENCES alumnos(id) ON DELETE CASCADE
            );
            """.trimIndent()
        )

        db.execSQL(
            "INSERT OR IGNORE INTO usuarios (username, password, nombre, apellido) VALUES ('admin', 'admin123', 'Carlos', 'Mendoza')"
        )
        db.execSQL(
            "INSERT OR IGNORE INTO alumnos (nombre, apellido, dni, es_socio, apto_fisico, fecha_alta, fecha_vencimiento, habilitado) VALUES ('Carlos', 'Mendoza', '33222111', 1, 1, '2025-03-01', '2026-10-31', 1)"
        )
    }

    // ==========================================
    // USUARIOS (Autenticación)
    // ==========================================

    fun validateUser(usernameInput: String, passwordInput: String): Usuario? {
        val db = readableDatabase
        var usuario: Usuario? = null
        try {
            val cursor: Cursor = db.query(
                "usuarios",
                arrayOf("id", "username", "password", "nombre", "apellido"),
                "username = ? AND password = ?",
                arrayOf(usernameInput.trim(), passwordInput.trim()),
                null,
                null,
                null
            )

            if (cursor.moveToFirst()) {
                usuario = Usuario(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    username = cursor.getString(cursor.getColumnIndexOrThrow("username")),
                    password = cursor.getString(cursor.getColumnIndexOrThrow("password")),
                    nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                    apellido = cursor.getString(cursor.getColumnIndexOrThrow("apellido"))
                )
            }
            cursor.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return usuario
    }

    fun getUserById(id: Int): Usuario? {
        val db = readableDatabase
        var usuario: Usuario? = null
        try {
            val cursor = db.query(
                "usuarios",
                arrayOf("id", "username", "password", "nombre", "apellido"),
                "id = ?",
                arrayOf(id.toString()),
                null, null, null
            )
            if (cursor.moveToFirst()) {
                usuario = Usuario(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    username = cursor.getString(cursor.getColumnIndexOrThrow("username")),
                    password = cursor.getString(cursor.getColumnIndexOrThrow("password")),
                    nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                    apellido = cursor.getString(cursor.getColumnIndexOrThrow("apellido"))
                )
            }
            cursor.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return usuario
    }

    // ==========================================
    // ALUMNOS
    // ==========================================

    fun insertAlumno(alumno: Alumno): Long {
        val db = writableDatabase
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val values = ContentValues().apply {
            put("nombre", alumno.nombre.trim())
            put("apellido", alumno.apellido.trim())
            put("dni", alumno.dni.trim())
            put("es_socio", if (alumno.esSocio) 1 else 0)
            put("apto_fisico", if (alumno.aptoFisico) 1 else 0)
            put("fecha_alta", if (alumno.fechaAlta.isNotEmpty()) alumno.fechaAlta else currentDate)
            put("fecha_vencimiento", alumno.fechaVencimiento)
            put("habilitado", if (alumno.habilitado) 1 else 0)
        }
        return db.insert("alumnos", null, values)
    }

    fun setAlumnoHabilitado(alumnoId: Int, habilitado: Boolean): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("habilitado", if (habilitado) 1 else 0)
        }
        val rows = db.update("alumnos", values, "id = ?", arrayOf(alumnoId.toString()))
        return rows > 0
    }

    fun getAllAlumnos(): List<Alumno> {
        val list = mutableListOf<Alumno>()
        val db = readableDatabase
        try {
            val cursor = db.rawQuery("SELECT * FROM alumnos ORDER BY apellido ASC, nombre ASC", null)
            if (cursor.moveToFirst()) {
                do {
                    list.add(cursorToAlumno(cursor))
                } while (cursor.moveToNext())
            }
            cursor.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    fun getAlumnoById(id: Int): Alumno? {
        val db = readableDatabase
        var alumno: Alumno? = null
        try {
            val cursor = db.query(
                "alumnos",
                null,
                "id = ?",
                arrayOf(id.toString()),
                null, null, null
            )
            if (cursor.moveToFirst()) {
                alumno = cursorToAlumno(cursor)
            }
            cursor.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return alumno
    }

    fun getAlumnoByDni(dni: String): Alumno? {
        val db = readableDatabase
        var alumno: Alumno? = null
        try {
            val cursor = db.query(
                "alumnos",
                null,
                "dni = ?",
                arrayOf(dni.trim()),
                null, null, null
            )
            if (cursor.moveToFirst()) {
                alumno = cursorToAlumno(cursor)
            }
            cursor.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return alumno
    }

    fun getOverdueAlumnos(todayDateString: String? = null): List<Alumno> {
        val list = mutableListOf<Alumno>()
        val db = readableDatabase
        val today = todayDateString ?: SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        try {
            val cursor = db.rawQuery(
                "SELECT * FROM alumnos WHERE (habilitado = 1 OR habilitado IS NULL) AND (fecha_vencimiento IS NULL OR fecha_vencimiento < ?) ORDER BY fecha_vencimiento ASC",
                arrayOf(today)
            )
            if (cursor.moveToFirst()) {
                do {
                    list.add(cursorToAlumno(cursor))
                } while (cursor.moveToNext())
            }
            cursor.close()
        } catch (e: Exception) {
            e.printStackTrace()
            // Fallback query if habilitado column doesn't exist in older table
            try {
                val cursor = db.rawQuery(
                    "SELECT * FROM alumnos WHERE fecha_vencimiento IS NULL OR fecha_vencimiento < ? ORDER BY fecha_vencimiento ASC",
                    arrayOf(today)
                )
                if (cursor.moveToFirst()) {
                    do {
                        list.add(cursorToAlumno(cursor))
                    } while (cursor.moveToNext())
                }
                cursor.close()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
        return list
    }

    fun getAlumnosVencimientoHoy(todayDateString: String? = null): List<Alumno> {
        val list = mutableListOf<Alumno>()
        val db = readableDatabase
        val today = todayDateString ?: SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        try {
            val cursor = db.rawQuery(
                "SELECT * FROM alumnos WHERE (habilitado = 1 OR habilitado IS NULL) AND fecha_vencimiento = ? ORDER BY apellido ASC, nombre ASC",
                arrayOf(today)
            )
            if (cursor.moveToFirst()) {
                do {
                    list.add(cursorToAlumno(cursor))
                } while (cursor.moveToNext())
            }
            cursor.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    fun getCountVencimientoHoy(todayDateString: String? = null): Int {
        val db = readableDatabase
        val today = todayDateString ?: SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        var count = 0
        try {
            val cursor = db.rawQuery(
                "SELECT COUNT(*) FROM alumnos WHERE (habilitado = 1 OR habilitado IS NULL) AND fecha_vencimiento = ?",
                arrayOf(today)
            )
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0)
            }
            cursor.close()
        } catch (e: Exception) {
            e.printStackTrace()
            try {
                val cursor = db.rawQuery(
                    "SELECT COUNT(*) FROM alumnos WHERE fecha_vencimiento = ?",
                    arrayOf(today)
                )
                if (cursor.moveToFirst()) {
                    count = cursor.getInt(0)
                }
                cursor.close()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
        return count
    }

    fun updateFechaVencimiento(alumnoId: Int, nuevaFechaVencimiento: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("fecha_vencimiento", nuevaFechaVencimiento)
        }
        val rows = db.update("alumnos", values, "id = ?", arrayOf(alumnoId.toString()))
        return rows > 0
    }

    private fun cursorToAlumno(cursor: Cursor): Alumno {
        val idxHabilitado = cursor.getColumnIndex("habilitado")
        val isHabilitado = if (idxHabilitado != -1) cursor.getInt(idxHabilitado) == 1 else true

        return Alumno(
            id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
            nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
            apellido = cursor.getString(cursor.getColumnIndexOrThrow("apellido")),
            dni = cursor.getString(cursor.getColumnIndexOrThrow("dni")),
            esSocio = cursor.getInt(cursor.getColumnIndexOrThrow("es_socio")) == 1,
            aptoFisico = cursor.getInt(cursor.getColumnIndexOrThrow("apto_fisico")) == 1,
            fechaAlta = cursor.getString(cursor.getColumnIndexOrThrow("fecha_alta")),
            fechaVencimiento = cursor.getString(cursor.getColumnIndexOrThrow("fecha_vencimiento")),
            habilitado = isHabilitado
        )
    }

    // ==========================================
    // PAGOS
    // ==========================================

    fun insertPago(pago: Pago): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("alumno_id", pago.alumnoId)
            put("monto", pago.monto)
            put("metodo_pago", pago.metodoPago)
            put("cuotas", pago.cuotas)
            put("periodo_desde", pago.periodoDesde)
            put("periodo_hasta", pago.periodoHasta)
        }
        return db.insert("pagos", null, values)
    }

    fun getLastPagoForAlumno(alumnoId: Int): Pago? {
        val db = readableDatabase
        var pago: Pago? = null
        try {
            val cursor = db.query(
                "pagos",
                null,
                "alumno_id = ?",
                arrayOf(alumnoId.toString()),
                null, null,
                "id DESC",
                "1"
            )
            if (cursor.moveToFirst()) {
                pago = Pago(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    alumnoId = cursor.getInt(cursor.getColumnIndexOrThrow("alumno_id")),
                    monto = cursor.getDouble(cursor.getColumnIndexOrThrow("monto")),
                    metodoPago = cursor.getString(cursor.getColumnIndexOrThrow("metodo_pago")),
                    cuotas = cursor.getInt(cursor.getColumnIndexOrThrow("cuotas")),
                    fechaPago = cursor.getString(cursor.getColumnIndexOrThrow("fecha_pago")),
                    periodoDesde = cursor.getString(cursor.getColumnIndexOrThrow("periodo_desde")),
                    periodoHasta = cursor.getString(cursor.getColumnIndexOrThrow("periodo_hasta"))
                )
            }
            cursor.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return pago
    }
}
