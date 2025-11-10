package com.example.jetpackbasics.data.repository

import Pessoa
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "android_imc"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "pessoa"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "nome"
        private const val COLUMN_AGE = "idade"
        private const val COLUMN_HEIGHT = "altura"
        private const val COLUMN_WEIGHT = "peso"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val sql =
            """CREATE TABLE IF NOT EXISTS $TABLE_NAME(
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME VARCHAR(200),
                $COLUMN_AGE INT,
                $COLUMN_HEIGHT REAL,
                $COLUMN_WEIGHT REAL
            )
        """
        db?.execSQL(sql)
    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
    }

    fun createPessoa(pessoa: Pessoa) {
        val db = writableDatabase
        val sql =
            """INSERT INTO $TABLE_NAME($COLUMN_NAME, $COLUMN_AGE, $COLUMN_HEIGHT, $COLUMN_WEIGHT) 
                VALUES('${pessoa.nome}', '${pessoa.idade}', '${pessoa.altura}', '${pessoa.peso}')"""

        db?.execSQL(sql)
        db.close()
    }

    fun readPessoas(): List<Pessoa> {
        val pessoaList = ArrayList<Pessoa>()
        val db = readableDatabase
        val sql =
            "SELECT $COLUMN_ID, $COLUMN_NAME, $COLUMN_AGE, $COLUMN_HEIGHT, $COLUMN_WEIGHT FROM $TABLE_NAME"

        val cursor = db.rawQuery(sql, null)

        if (cursor.moveToFirst()) {
            do {
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val nome = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
                val idade = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AGE))
                val altura = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_HEIGHT))
                val peso = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_WEIGHT))

                val pessoa = Pessoa(nome, idade, altura, peso)
                pessoaList.add(pessoa)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return pessoaList
    }
}