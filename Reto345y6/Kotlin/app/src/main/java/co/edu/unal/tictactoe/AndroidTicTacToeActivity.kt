package co.edu.unal.tictactoe

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AndroidTicTacToeActivity : AppCompatActivity() {
    private lateinit var mBoardButtons: Array<Button?>
    private var mGame: TicTacToeGame? = null
    private var mInfoTextView: TextView? = null
    private var newgame: Button? = null
    private var mGameOver = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mBoardButtons = arrayOfNulls(TicTacToeGame.BOARD_SIZE)
        mBoardButtons[0] = findViewById<View>(R.id.one) as Button
        mBoardButtons[1] = findViewById<View>(R.id.two) as Button
        mBoardButtons[2] = findViewById<View>(R.id.three) as Button
        mBoardButtons[3] = findViewById<View>(R.id.four) as Button
        mBoardButtons[4] = findViewById<View>(R.id.five) as Button
        mBoardButtons[5] = findViewById<View>(R.id.six) as Button
        mBoardButtons[6] = findViewById<View>(R.id.seven) as Button
        mBoardButtons[7] = findViewById<View>(R.id.eight) as Button
        mBoardButtons[8] = findViewById<View>(R.id.nine) as Button
        mInfoTextView = findViewById<View>(R.id.information) as TextView
        newgame = findViewById(R.id.ten) as Button
        newgame?.setOnClickListener {
            startNewGame()
        }
        mGame = TicTacToeGame()
        startNewGame()
    }

    private fun startNewGame() {
        mGameOver = false
        mGame!!.clearBoard()
        // Reset all buttons
        for (i in mBoardButtons.indices) {
            mBoardButtons[i]!!.text = ""
            mBoardButtons[i]!!.isEnabled = true
            mBoardButtons[i]!!.setOnClickListener(ButtonClickListener(i))
        }
    }

    // Handles clicks on the game board buttons
    private inner class ButtonClickListener(var location: Int) : View.OnClickListener {
        override fun onClick(view: View) {
            if (mBoardButtons[location]!!.isEnabled) {
                setMove(TicTacToeGame.HUMAN_PLAYER, location)
                var winner = mGame!!.checkForWinner()
                if (winner == 0) {
                    mInfoTextView!!.setText(R.string.turn_computer)
                    val move = mGame!!.computerMove
                    setMove(TicTacToeGame.COMPUTER_PLAYER, move)
                    winner = mGame!!.checkForWinner()
                }
                if (winner == 0) {
                    mInfoTextView!!.setText(R.string.turn_human)
                } else if (winner == 1) {
                    mInfoTextView!!.setText(R.string.result_tie)
                    mGameOver = true
                } else if (winner == 2) {
                    mInfoTextView!!.setText(R.string.result_human_wins)
                    mGameOver = true
                } else {
                    mInfoTextView!!.setText(R.string.result_computer_wins)
                    mGameOver = true
                }
                if (mGameOver) {
                    for (i in mBoardButtons) {
                        i!!.isEnabled = false
                    }
                }
            }
        }
    }

    private fun setMove(player: Char, location: Int) {
        mGame!!.setMove(player, location)
        mBoardButtons[location]!!.isEnabled = false
        mBoardButtons[location]!!.text = player.toString()
        if (player == TicTacToeGame.HUMAN_PLAYER) mBoardButtons[location]!!.setTextColor(Color.rgb(0, 200, 0)) else mBoardButtons[location]!!.setTextColor(Color.rgb(200, 0, 0))
    }
}