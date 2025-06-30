package com.example.game_zone.Services.GamesServices;

import com.example.game_zone.Entities.GameSession;
import com.example.game_zone.Entities.GamesEntities.TicTacToeState;
import com.example.game_zone.Entities.User;
import com.example.game_zone.Repositories.GameSessionRepository;
import com.example.game_zone.Repositories.GamesRepositories.TicTacToeStateRepository;
import com.example.game_zone.Services.GameService;
import com.example.game_zone.dto.TicTacToeResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
// This class provides services for the Tic Tac Toe game.
public class TicTacToeService {

    @Autowired
    private TicTacToeStateRepository stateRepository;

    @Autowired
    private GameSessionRepository sessionRepository;

    @Autowired
    private GameService gameService;

    // this method is used to make a move in the tic tac toe game
    public TicTacToeResponseDto makeMove(Long sessionId, Long playerId, int row, int col) {
        Optional<TicTacToeState> optionalState = stateRepository.findBySessionId(sessionId);

        if (optionalState.isEmpty()) {
            throw new IllegalArgumentException("Game not found");
        }

        TicTacToeState state = optionalState.get();

        if (!state.getCurrentTurn().equals(playerId)) {
            throw new IllegalStateException("Not your turn");
        }

        int index = row * 3 + col;
        if (state.getBoard().charAt(index) != '-') {
            throw new IllegalArgumentException("Cell already occupied");
        }
        // Update the board
        char symbol = (playerId.equals(state.getSession().getPlayer1().getUserId())) ? 'X' : 'O';
        char[] boardArr = state.getBoard().toCharArray();
        boardArr[index] = symbol;
        state.setBoard(new String(boardArr));

        // Check for a winner
        int[] winningLine = checkWinner(boardArr, symbol);
        if (winningLine != null) {
            state.setStatus("FINISHED");
            state.setWinningCells(new ArrayList<>(Arrays.asList(winningLine[0], winningLine[1], winningLine[2])));
            gameService.clearActiveSession(sessionId);

            // Set the winner
            User winner = playerId.equals(state.getSession().getPlayer1().getUserId())
                    ? state.getSession().getPlayer1()
                    : state.getSession().getPlayer2();
            state.getSession().setWinner(winner);


            GameSession session = state.getSession();
            session.setEndTime(new Date());
            sessionRepository.save(session);


        } else if (new String(boardArr).chars().noneMatch(c -> c == '-')) {
            state.setStatus("DRAW");
            gameService.clearActiveSession(sessionId);

            GameSession session = state.getSession();
            session.setEndTime(new Date());
            sessionRepository.save(session);
        } else {
            Long nextTurn = playerId.equals(state.getSession().getPlayer1().getUserId())
                    ? state.getSession().getPlayer2().getUserId()
                    : state.getSession().getPlayer1().getUserId();
            state.setCurrentTurn(nextTurn);
        }
        stateRepository.save(state);

        return toDto(state);
    }

    // this method is used to check if there is a winner in the game
    private int[] checkWinner(char[] board, char symbol) {
        int[][] wins = {
                {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
                {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
                {0, 4, 8}, {2, 4, 6}
        };

        for (int[] win : wins) {
            if (board[win[0]] == symbol && board[win[1]] == symbol && board[win[2]] == symbol) {

                return win;
            }
        }
        return null;
    }
    // this method is used to convert the game state to a response dto
    public TicTacToeResponseDto toDto(TicTacToeState state) {
        return new TicTacToeResponseDto(
                state.getBoard().split(""),
                state.getCurrentTurn(),
                state.getSession().getWinner() != null ? state.getSession().getWinner().getUsername() : null,
                state.getWinningCells(),
                state.getStatus()
        );
    }
    // this method is used to get the game state by session id
    public Optional<TicTacToeState> getStateBySessionId(Long sessionId) {
        return stateRepository.findBySessionId(sessionId);
    }
}
