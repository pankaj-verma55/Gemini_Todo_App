package com.example.todoapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.todoapp.databinding.ActivityCreateTaskBinding
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.Data.DataClass.ChoseItem
import com.example.todoapp.Data.GeminiRepositoryImp
import com.example.todoapp.Domain.AskGeminiUseCase
import com.example.todoapp.Ui.Adapter.ChoseItemAdapter
import com.example.todoapp.Ui.Adapter.Viewmodel.GeminiViewModel
import com.example.todoapp.Ui.Adapter.Viewmodel.GeminiViewModelFactory
import com.example.todoapp.Ui.Adapter.Viewmodel.TodoViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import kotlinx.coroutines.launch
import java.util.Calendar

class CreateTaskActivity : AppCompatActivity() {
    private lateinit var viewModel: TodoViewModel
    private var isVoiceRequest = false
    private lateinit var binding: ActivityCreateTaskBinding
    private lateinit var geminiViewModel: GeminiViewModel
    private lateinit var speechRecognizer: SpeechRecognizer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateTaskBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[TodoViewModel::class.java]

        FirebaseApp.initializeApp(this)
        val firebaseAppCheck = FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory(
            DebugAppCheckProviderFactory.getInstance()
        )

        val repository = GeminiRepositoryImp()
        val useCase = AskGeminiUseCase(repository)
        val factory = GeminiViewModelFactory(useCase)

        geminiViewModel = ViewModelProvider(this, factory)[GeminiViewModel::class.java]

        // Fix: Set click listener outside of flow collection
        binding.geminiAiBtn.setOnClickListener {
            isVoiceRequest = false
            val prompt = binding.taskTxt.text.toString()
            Log.d("CreateTaskActivity", "AI Button clicked. Prompt: $prompt")
            if (prompt.isNotBlank()) {
                Snackbar.make(
                    binding.root,
                    "Generating description...",
                    Snackbar.LENGTH_SHORT
                ).show()
                geminiViewModel.askGemini(prompt)
            } else {
                Snackbar.make(
                    binding.root,
                    "Please write a task title first",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    geminiViewModel.response.collect { response ->

                        if (response.isNotEmpty()) {

                            Log.d(
                                "CreateTaskActivity",
                                "Received AI Response: $response"
                            )

                            if (isVoiceRequest) {

                                parseTodoResponse(response)

                                isVoiceRequest = false

                            } else {

                                binding.taskDescriptionTxt.setText(response)
                            }
                        }
                    }
                }

                launch {
                    geminiViewModel.isLoading.collect { loading ->

                        binding.progressBar.isVisible = loading

                        if (loading) {
                            binding.progressBar.isVisible = true
//                            binding.generatingText.text = "Text is generating..."
                        } else {
                            binding.progressBar.isVisible = false
                        }
                    }
                }
            }
        }
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)

        speechRecognizer.setRecognitionListener(object : RecognitionListener {

            override fun onResults(results: Bundle?) {

                val matches =
                    results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)

                val spokenText = matches?.firstOrNull()

                if (!spokenText.isNullOrEmpty()) {

                    Log.d("Speech", "User said: $spokenText")

                    generateTodoFromVoice(spokenText)
                }
            }

            override fun onError(error: Int) {
                Log.e("Speech", "Speech recognition error: $error")
                Snackbar.make(
                    binding.root,
                    "Could not understand speech",
                    Snackbar.LENGTH_SHORT
                ).show()
            }

            override fun onReadyForSpeech(params: Bundle?) {
                Snackbar.make(
                    binding.root,
                    "Ready to speak...",
                    Snackbar.LENGTH_SHORT
                ).show()
            }

            override fun onBeginningOfSpeech() {}

            override fun onRmsChanged(rmsdB: Float) {}

            override fun onBufferReceived(buffer: ByteArray?) {}

            override fun onEndOfSpeech() {
                Log.d("Speech", "Speech ended")

                binding.progressBar.isVisible = true
                Snackbar.make(
                    binding.root,
                    "Text is generating...",
                    Snackbar.LENGTH_SHORT
                ).show()
            }

            override fun onPartialResults(partialResults: Bundle?) {}

            override fun onEvent(eventType: Int, params: Bundle?) {

            }
        })

        val choseList = listOf(
            ChoseItem(title = "Idea"),
            ChoseItem(title = "Food"),
            ChoseItem(title = "Work"),
            ChoseItem(title = "Box"),
            ChoseItem(title = "Music")
        )
        val adapter = ChoseItemAdapter(choseList, onItemClick = { selectedItem ->
            binding.titleTxt.text = selectedItem.title
            binding.rvItem.visibility = View.GONE
        })
        binding.rvItem.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvItem.adapter = adapter

        binding.titleBar.backBtn.setOnClickListener {
            finish()
        }

        binding.titleBar.clockBtn.setOnClickListener {
            binding.calenderView.visibility =
                if (binding.calenderView.isVisible) {
                    View.GONE
                } else {
                    View.VISIBLE
                }
        }

        binding.layoutItem.setOnClickListener {
            binding.rvItem.visibility =
                if (binding.rvItem.isVisible) {
                    View.GONE
                } else {
                    View.VISIBLE
                }
            binding.arrowDown.rotation = if (binding.arrowDown.rotation == 0f) 180f else 0f
        }
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = binding.calenderView.date

        var date = "${calendar.get(Calendar.DAY_OF_MONTH)}/" +
                "${calendar.get(Calendar.MONTH) + 1}/" +
                "${calendar.get(Calendar.YEAR)}"
        binding.calenderView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            date = "$dayOfMonth/${month + 1}/$year"
            println(date)
        }
        binding.createTaskBtn.setOnClickListener {
            val selectedItem = binding.titleTxt.text.toString()
            val title = binding.taskTxt.text.toString()
            val description = binding.taskDescriptionTxt.text.toString()

            if (title.isNotBlank()) {
                viewModel.addTodo(
                    date,
                    selectedItem,
                    title = title,
                    description = description,
                    done = false
                )
                finish()
            }
        }

        binding.micBtn.setOnClickListener {


            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.RECORD_AUDIO
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.RECORD_AUDIO),
                    100
                )

            } else {
                startVoiceRecognition()
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun startVoiceRecognition() {

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {

            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )

            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE,
                "en-IN"
            )

            putExtra(
                RecognizerIntent.EXTRA_PROMPT,
                "Speak your task..."
            )
        }

        speechRecognizer.startListening(intent)
    }

    private fun generateTodoFromVoice(spokenText: String) {

        isVoiceRequest = true

        val prompt = """
        Convert the following spoken Todo request into a Todo.

        User said:
        "$spokenText"

        Create:
        1. A short, clear Todo title.
        2. A useful Todo description.

        Return ONLY this format:

        TITLE:
        <title>

        DESCRIPTION:
        <description>
    """.trimIndent()

        Log.d("CreateTaskActivity", "Voice prompt: $spokenText")

        geminiViewModel.askGemini(prompt)
    }

    private fun parseTodoResponse(response: String) {

        val titleStart = response.indexOf("TITLE:")
        val descriptionStart = response.indexOf("DESCRIPTION:")

        if (titleStart == -1 || descriptionStart == -1) {
            return
        }

        val title = response
            .substring(
                titleStart + "TITLE:".length,
                descriptionStart
            )
            .trim()

        val description = response
            .substring(
                descriptionStart + "DESCRIPTION:".length
            )
            .trim()

        binding.titleTxt.setText(title)
        binding.taskDescriptionTxt.setText(description)
    }
}