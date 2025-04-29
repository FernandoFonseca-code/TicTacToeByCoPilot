package com.example.tictactoebycopilot

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tictactoebycopilot.ui.theme.TicTacToeByCoPilotTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TicTacToeByCoPilotTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TicTacToeGame()
                }
            }
        }
    }
}

@Composable
fun TicTacToeGame() {
    // Game state
    var board by remember { mutableStateOf(List(3) { MutableList(3) { "" } }) }
    var currentPlayer by remember { mutableStateOf("X") }
    var gameOver by remember { mutableStateOf(false) }
    var winner by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current

    // Check for win
    fun checkWinner(): Boolean {
        // Check rows
        for (i in 0..2) {
            if (board[i][0] != "" && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                winner = board[i][0]
                return true
            }
        }

        // Check columns
        for (i in 0..2) {
            if (board[0][i] != "" && board[0][i] == board[1][i] && board[1][i] == board[2][i]) {
                winner = board[0][i]
                return true
            }
        }

        // Check diagonals
        if (board[0][0] != "" && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            winner = board[0][0]
            return true
        }

        if (board[0][2] != "" && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            winner = board[0][2]
            return true
        }

        return false
    }

    // Check for cat's game (draw)
    fun isBoardFull(): Boolean {
        for (row in board) {
            for (cell in row) {
                if (cell == "") return false
            }
        }
        return true
    }

    // Reset game
    fun resetGame() {
        board = List(3) { MutableList(3) { "" } }
        currentPlayer = "X"
        gameOver = false
        winner = null
    }

    // Show result and update game state
    fun showResult() {
        if (checkWinner()) {
            gameOver = true
            Toast.makeText(context, "Player $winner wins!", Toast.LENGTH_SHORT).show()
        } else if (isBoardFull()) {
            gameOver = true
            Toast.makeText(context, "Cat's Game", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Game board
        for (i in 0..2) {
            Row(
                modifier = Modifier.padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                for (j in 0..2) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .padding(4.dp)
                            .border(2.dp, Color.Gray, RoundedCornerShape(4.dp))
                            .clickable {
                                if (board[i][j] == "" && !gameOver) {
                                    val newBoard = board.map { it.toMutableList() }
                                    newBoard[i][j] = currentPlayer
                                    board = newBoard

                                    showResult()

                                    if (!gameOver) {
                                        currentPlayer = if (currentPlayer == "X") "O" else "X"
                                    }
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = board[i][j],
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Current player indicator
        Text(
            text = if (!gameOver) "Player $currentPlayer's turn" else if (winner != null) "Player $winner wins!" else "Cat's Game",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // New Game button
        Button(
            onClick = { resetGame() },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("New Game", fontSize = 16.sp)
        }
    }
}