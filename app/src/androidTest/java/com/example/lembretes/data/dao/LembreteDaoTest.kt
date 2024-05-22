package com.example.lembretes.data.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.example.lembretes.data.LembreteDatabase
import com.example.lembretes.data.entity.LembreteEntity
import com.example.lembretes.utils.TestDispatcherRule
import com.example.lembretes.utils.convertDateStringToLong
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import java.util.Date


@RunWith(AndroidJUnit4::class)
class LembreteDaoTest {

    @get:Rule
    val dispatcherRule = TestDispatcherRule()


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
    fun insertPostagem_insert_lembrete_and_return_created() = runTest {
        val lembrete = LembreteEntity(
            id = null,
            name = "fazer caminhada",
            description = "caminhas duas horas",
            dateTime = Date().convertDateStringToLong("10/05/2024")!!,
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

         lembreteDao.findAll().test {
             val resul = awaitItem()
             assertThat(resul).hasSize(3)
             assertThat(resul[0].name).isEqualTo("fazer caminhada")
             cancel()
         }

    }
    @Test
    fun findStickNoteByDate_must_get_dates_on_daterminate_periodic()= runTest{
        listPostagemDto().forEach {
            lembreteDao.insertLembrete(it)
        }

        lembreteDao.findAll().test {
            val resul = awaitItem()
            assertThat(resul).hasSize(3)
            assertThat(resul[0].name).isEqualTo("fazer caminhada")
            resul.forEach {
                println(" result date ${it.dateTime}.")
            }
            cancel()
        }

         val period1  = Date().convertDateStringToLong("09/05/2024")
        val periodic2 = Date().convertDateStringToLong("09/05/2024")

        println( "P1 = $period1   p2 - $periodic2" )
        lembreteDao.findStickNoteByDate(firstDate = period1!!, secondDate = periodic2!!).test {
             val result = awaitItem()
              assertThat(result.size).isEqualTo(1)
            cancel()
        }
    }


    @Test
    fun delete_must_receive_a_lembrete_delete_an_return_int()= runTest {
        listPostagemDto().forEach {
            lembreteDao.insertLembrete(it)
        }
        val lines = lembreteDao.deleteLembrete(listPostagemDto()[1])
       assertThat(lines).isEqualTo(1)
//        lembreteDao.findAll().test{
//            val result = awaitItem()
//
//
//            assertThat(result).hasSize(2)
//             cancel()
//        }

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
            dateTime = Date().convertDateStringToLong("10/05/2024")!!,
            isRemember = false
        )

        val update = lembreteDao.update(lembreteUpdate)
        assertThat(update).isEqualTo(1)

    }

    @After
    fun tearDown() {
      database.close()
    }

    private fun listPostagemDto() = listOf(
        LembreteEntity(
            id = null,
            name = "fazer caminhada",
            description = "caminhas duas horas",
            dateTime = Date().convertDateStringToLong("10/05/2024")!!,
            isRemember = false
        ),
        LembreteEntity(
            id = null,
            name = "Almocar",
            description = "alimentação",
            dateTime = Date().convertDateStringToLong("10/05/2024")!!,
            isRemember = false
        ),
        LembreteEntity(
            id = null,
            name = "estudar",
            description = "estudar algo novo",
            dateTime =Date().convertDateStringToLong("09/05/2024")!!,
            isRemember = false
        )
    )
}