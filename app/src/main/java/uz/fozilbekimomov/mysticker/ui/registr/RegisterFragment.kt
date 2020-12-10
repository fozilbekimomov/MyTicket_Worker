package uz.fozilbekimomov.mysticker.ui.registr

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import uz.fozilbekimomov.mysticker.R
import uz.fozilbekimomov.mysticker.core.utils.PREF_NAME
import uz.fozilbekimomov.mysticker.core.utils.USER_NAME
import uz.fozilbekimomov.mysticker.core.utils.USER_NUMBER
import uz.fozilbekimomov.mysticker.databinding.FragmentRegisterBinding


/**
 * Created by <a href="mailto: fozilbekimomov@gmail.com" >Fozilbek Imomov</a>
 *
 * @author fozilbekimomov
 * @version 1.0
 * @date 12/9/20
 * @project MySticker
 */


class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.saveRegister.setOnClickListener {

            val name = binding.nameInsert.text.toString()
            val number = binding.phoneInsert.text.toString()

            if (name.isNotEmpty() && number.isNotEmpty()) {
                val sp = requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                val edit = sp.edit()

                edit.putString(USER_NUMBER, number).apply()
                edit.putString(USER_NAME, name).apply()

                findNavController().navigate(R.id.action_registerFragment_to_homeFragment)

            } else {
                Snackbar.make(
                    requireView(),
                    getString(R.string.register_error),
                    Snackbar.LENGTH_LONG
                ).show()
            }

        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}