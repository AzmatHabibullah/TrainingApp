package com.azmat.testdrivendevelopment.ui.auth.register

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.azmat.testdrivendevelopment.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterFragment : Fragment() {

    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        val toLoginTextView = view.findViewById<TextView>(R.id.already_account_textview_registerFragment)

        toLoginTextView.setOnClickListener{
            view.findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerViewModel = ViewModelProvider(this,
            RegisterViewModel.RegisterViewModelFactory()
        )[RegisterViewModel::class.java]

        val usernameEditText = view.findViewById<EditText>(R.id.username_edittext_registerFragment)
        val emailEditText = view.findViewById<EditText>(R.id.email_edittext_registerFragment)
        val passwordEditText = view.findViewById<EditText>(R.id.password_edittext_registerFragment)
        val registerButton = view.findViewById<Button>(R.id.register_button_registerFragment)

        registerViewModel.registerFormState.observe(viewLifecycleOwner,
        Observer { registerFormState ->
            if (registerFormState == null) {
                return@Observer
            }
            registerButton.isEnabled = registerFormState.isDataValid
            registerFormState.emailError?.let {
                emailEditText.error = getString(it)
            }
            registerFormState.usernameError?.let {
                usernameEditText.error = getString(it)
            }
            registerFormState.passwordError?.let {
                passwordEditText.error = getString(it)
            }
        })

        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(s: Editable) {
                registerViewModel.registerDataChanged(
                    emailEditText.text.toString(),
                    usernameEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            }
        }

        emailEditText.addTextChangedListener(afterTextChangedListener)
        usernameEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.addTextChangedListener(afterTextChangedListener)

        passwordEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                registerViewModel.register(
                    usernameEditText.text.toString(),
                    emailEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            }
            false
        }

        registerButton.setOnClickListener {
            registerViewModel.register(
                usernameEditText.text.toString(),
                emailEditText.text.toString(),
                passwordEditText.text.toString()
            )
        }

    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            RegisterFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}