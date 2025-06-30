//package com.example.game_zone.dto;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//public class OpponentResponseDto {
//    private boolean waiting; // האם מחכה ליריב
//    private GameSessionDto session; // אם נמצא יריב - שולחים את ה-Session
//}
package com.example.game_zone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
//dto for the opponent response
public class OpponentResponseDto {
    private Long opponentId;
    private Long sessionId;
    private String player1Name;
    private String player2Name;
}