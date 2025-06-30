package com.example.game_zone.Services.GamesServices;

import com.example.game_zone.Entities.GamesEntities.FourInRowState;
import com.example.game_zone.Entities.GameSession;
import com.example.game_zone.Entities.User;
import com.example.game_zone.Repositories.GameSessionRepository;
import com.example.game_zone.Repositories.GamesRepositories.FourInRowRepository;
import com.example.game_zone.Repositories.UserRepository;
import com.example.game_zone.dto.FourInRowResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
// This class provides services for the Four In a Row game.
public class FourInRowService {

    @Autowired
    private FourInRowRepository stateRepository;

    @Autowired
    private GameSessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    public Optional<FourInRowState> getStateBySessionId(Long sessionId) {
        return stateRepository.findBySessionId(sessionId);
    }

    public FourInRowResponseDto toDto(FourInRowState state) {
        return new FourInRowResponseDto(
                state.getBoard().split(""),
                state.getCurrentTurn(),
                state.getSession().getWinner() != null ? state.getSession().getWinner().getUsername() : null,
                state.getWinningCells(),
                state.getStatus()
        );
    }
    // This method is used to make a move in the Four In a Row game.
    public FourInRowResponseDto makeMove(Long sessionId, Long playerId, int column) {
        FourInRowState state = stateRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Game not found"));

        if (!state.getCurrentTurn().equals(playerId)) {
            throw new IllegalStateException("Not your turn");
        }
        // check which player is making the move using X and O to represent the players
        char symbol = playerId.equals(state.getPlayer1Id()) ? 'X' : 'O';
        char[] boardArr = state.getBoard().toCharArray();
        int placedIndex = -1;
        // find the lowest empty cell in the selected column
        for (int row = 5; row >= 0; row--) {
            int index = row * 7 + column;
            if (boardArr[index] == '-') {
                boardArr[index] = symbol;
                placedIndex = index;
                break;
            }
        }
        // if no empty cell is found, throw an exception
        if (placedIndex == -1) {
            throw new IllegalArgumentException("Column is full");
        }

        // Check win condition
        int[] winLine = checkWinner(boardArr, placedIndex, symbol);
        if (winLine != null) {
            state.setStatus("FINISHED");
            state.setWinningCells(Arrays.asList(winLine[0], winLine[1], winLine[2], winLine[3]));
            GameSession session = state.getSession();
            session.setWinner(userRepository.findById(playerId).orElse(null));
            session.setEndTime(new Date());
            sessionRepository.save(session);
        } else if (new String(boardArr).chars().noneMatch(c -> c == '-')) {
            state.setStatus("DRAW");
            GameSession session = state.getSession();
            session.setEndTime(new Date());
            sessionRepository.save(session);
        } else {
            Long nextTurn = playerId.equals(state.getPlayer1Id()) ? state.getPlayer2Id() : state.getPlayer1Id();
            state.setCurrentTurn(nextTurn);
        }

        state.setBoard(new String(boardArr));
        stateRepository.save(state);

        return toDto(state);
    }
    // this method checks if there is a winner after a move is made.
    private int[] checkWinner(char[] board, int index, char symbol) {
        int row = index / 7;
        int col = index % 7;
        // Check all four possible directions for a win
        int[][] directions = {
                {0, 1}, // horizontal
                {1, 0}, // vertical
                {1, 1}, // diagonal down
                {1, -1} // diagonal up
        };
        // Check in both directions for each direction
        for (int[] dir : directions) {
            int count = 1;
            List<Integer> cells = new ArrayList<>(List.of(index));
            // Check in negative and positive directions
            for (int d = -1; d <= 1; d += 2) {
                int r = row + d * dir[0];
                int c = col + d * dir[1];
                // Check if the cell is within bounds and matches the symbol
                while (r >= 0 && r < 6 && c >= 0 && c < 7 && board[r * 7 + c] == symbol) {
                    cells.add(r * 7 + c);
                    count++;
                    r += d * dir[0];
                    c += d * dir[1];
                }
            }
            // If four or more in a row, return the winning cells
            if (count >= 4) {
                return cells.stream().sorted().limit(4).mapToInt(i -> i).toArray();
            }
        }

        return null;
    }
}
