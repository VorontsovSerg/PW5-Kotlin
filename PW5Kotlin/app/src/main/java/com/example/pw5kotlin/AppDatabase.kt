package com.example.pw5kotlin

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.pw5kotlin.data.ProductDao
import com.example.pw5kotlin.data.ProductEntity

@Database(entities = [ProductEntity::class], version = 2) // Обновлено до версии 2
abstract class AppDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "product_database"
                )
                    .addMigrations(MIGRATION_1_2) // Добавление миграции
                    .build()
                INSTANCE = instance
                instance
            }
        }

        // Миграция с версии 1 на версию 2: изменение типа столбца и добавление нового поля
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Шаг 1: Создание новой таблицы с обновленной схемой
                database.execSQL("""
                    CREATE TABLE products_new (
                        id INTEGER PRIMARY KEY NOT NULL,
                        title TEXT NOT NULL,
                        description TEXT NOT NULL,
                        price REAL NOT NULL,  -- Изменен на REAL
                        discountPercentage REAL NOT NULL,
                        rating REAL NOT NULL,
                        stock INTEGER NOT NULL,
                        brand TEXT NOT NULL,
                        category TEXT NOT NULL,
                        thumbnail TEXT NOT NULL,
                        discountAmount REAL NOT NULL DEFAULT 0.0  -- Новое поле
                    )
                """.trimIndent())

                // Шаг 2: Копирование данных из старой таблицы в новую
                database.execSQL("""
                    INSERT INTO products_new (id, title, description, price, discountPercentage, rating, stock, brand, category, thumbnail, discountAmount)
                    SELECT id, title, description, price, discountPercentage, rating, stock, brand, category, thumbnail, 0.0
                    FROM products
                """.trimIndent())

                // Шаг 3: Удаление старой таблицы
                database.execSQL("DROP TABLE products")

                // Шаг 4: Переименование новой таблицы в оригинальное имя
                database.execSQL("ALTER TABLE products_new RENAME TO products")
            }
        }
    }
}
