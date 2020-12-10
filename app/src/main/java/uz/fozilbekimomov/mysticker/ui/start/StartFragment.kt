package uz.fozilbekimomov.mysticker.ui.start

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import uz.fozilbekimomov.mysticker.R
import uz.fozilbekimomov.mysticker.core.utils.PREF_NAME
import uz.fozilbekimomov.mysticker.core.utils.USER_NAME
import uz.fozilbekimomov.mysticker.core.utils.USER_NUMBER
import uz.fozilbekimomov.mysticker.databinding.FragmentStartBinding


/**
 * Created by <a href="mailto: fozilbekimomov@gmail.com" >Fozilbek Imomov</a>
 *
 * @author fozilbekimomov
 * @version 1.0
 * @date 12/9/20
 * @project MySticker
 */


class StartFragment : Fragment() {

    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStartBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sp = requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)


        if (sp.getString(USER_NAME, "")!!.isNotEmpty() && sp.getString(USER_NUMBER, "")!!
                .isNotEmpty()
        ) {
            findNavController().navigate(R.id.action_startFragment_to_homeFragment)
        } else {
            findNavController().navigate(R.id.action_startFragment_to_registerFragment)
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}