package com.example.intellilearnteacherapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.intellilearnteacherapp.adapters.ChatAdapter
import kotlinx.android.synthetic.main.activity_ask_chat_gpt.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AskChatGPT : AppCompatActivity() {

    private lateinit var chatAdapter: ChatAdapter
    private val chatMessages = mutableListOf<ChatMessage>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ask_chat_gpt)

        initRecyclerView()

        sendButton.setOnClickListener {
            val question = userInputEditText.text.toString().trim()

            if (question.isNotEmpty()) {
                chatMessages.add(ChatMessage(question, true))
                chatAdapter.notifyItemInserted(chatMessages.size - 1)
                recyclerView.scrollToPosition(chatMessages.size - 1)

                userInputEditText.setText("")

                MyApp.getInstance().getApiServices().askChatGPT(question)
                    .enqueue(object : Callback<String> {
                        override fun onResponse(
                            call: Call<String>,
                            response: Response<String>
                        ) {
                            if (response.isSuccessful && response.body()?.toString() != "Error") {
                                val reply = response.body().toString()
                                chatMessages.add(ChatMessage(reply, false))
                                chatAdapter.notifyItemInserted(chatMessages.size - 1)
                                recyclerView.scrollToPosition(chatMessages.size - 1)
                            } else {
                                chatMessages.add(ChatMessage("Error occurred.", false))
                                chatAdapter.notifyItemInserted(chatMessages.size - 1)
                                recyclerView.scrollToPosition(chatMessages.size - 1)
                            }
                        }

                        override fun onFailure(call: Call<String>, t: Throwable) {
                            chatMessages.add(ChatMessage("Error occurred.", false))
                            chatAdapter.notifyItemInserted(chatMessages.size - 1)
                            recyclerView.scrollToPosition(chatMessages.size - 1)
                        }
                    })
            }
        }
    }

    private fun initRecyclerView() {
        chatAdapter = ChatAdapter(chatMessages)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@AskChatGPT)
            adapter = chatAdapter
        }
    }
}
