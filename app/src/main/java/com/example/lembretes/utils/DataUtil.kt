package com.example.lembretes.utils

import com.example.lembretes.domain.model.StickyNoteDomain
import kotlinx.datetime.Clock

object DataUtil {
    /// Data util
   fun  validateDate(stickNoteUp : StickyNoteDomain,onInfoMessage :()->Unit){
       if (stickNoteUp.id == null || Clock.System.getDateFromLongOfCurrentSystemDate(stickNoteUp.dateTime)  <=
           Clock.System.getDateCurrentSystemDefaultInLocalDateTime()
       ) {
           onInfoMessage()
           return
       }
   }
}