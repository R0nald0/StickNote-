package com.example.lembretes.data.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.lembretes.data.LembreteDatabase
import com.example.lembretes.data.entity.LembreteEntity
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import java.util.Date


@RunWith(AndroidJUnit4::class)
class LembreteDaoTest {

    private lateinit var database : LembreteDatabase
    private lateinit var lembreteDao: LembreteDao
    @Before
    fun setUp() {
           database = Room.inMemoryDatabaseBuilder(
               ApplicationProvider.getApplicationContext(),
               LembreteDatabase::class.java,
           ).allowMainThreadQueries().build()
        lembreteDao = database.lembreteDao()
    }

    @Test
    fun insertPostagem_insert_lembrete_and_return_created() {
        val lembrete = LembreteEntity(
            id = 0,
            name = "fazer caminhada",
            description = "caminhas duas horas",
            dateTime = Date().time,
            isRemember = false
            )

      val resultLembrete = lembreteDao.insertLembrete(lembrete)
        assertThat(resultLembrete).isEqualTo(1)
    }

    @Test
    fun findAll_must_return_a_list_of_lembretes() = runTest {
         listPostagemDto().forEach {
             lembreteDao.insertLembrete(it)
         }

        val result = lembreteDao.findAll()

        assertThat(result).hasSize(3)
        assertThat(result).isNotEmpty()
        assertThat(result[0].name).isEqualTo("fazer caminhada")
    }


    @Test
    fun delete_must_receive_a_lembrete_delete_an_return_int()= runTest {
        listPostagemDto().forEach {
            lembreteDao.insertLembrete(it)
        }

        val result = lembreteDao.findAll()

       val intLine = lembreteDao.deleteLembrete(lembrete = arrayOf(result[2]))

        assertThat(intLine).isEqualTo(1)

    }

    @Test
    fun update_must_update_a_lembrete_and_retun_int() = runTest {
        listPostagemDto().forEach {
            lembreteDao.insertLembrete(it)
        }

        val lembreteUpdate = LembreteEntity(
            id = 1,
            name = "ler e caminhar",
            description = "caminhar duas horas e ler",
            dateTime = Date().time,
            isRemember = false
        )

        val update = lembreteDao.update(lembreteUpdate)

        assertThat(update).isEqualTo(1)
    }

    @After
    fun tearDown() {
      database.close()
    }

    fun listPostagemDto() = listOf(
        LembreteEntity(
            id = 0,
            name = "fazer caminhada",
            description = "caminhas duas horas",
            dateTime = Date().time,
            isRemember = false
        ),
        LembreteEntity(
            id = 0,
            name = "Almocar",
            description = "alimentação",
            dateTime = Date().time,
            isRemember = false
        ),
        LembreteEntity(
            id = 0,
            name = "estudar",
            description = "estudar algo novo",
            dateTime = Date().time,
            isRemember = false
        )
    )
}