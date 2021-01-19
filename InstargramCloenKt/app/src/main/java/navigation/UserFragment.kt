package navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.monggu.instargramcloenkt.R
import com.monggu.instargramcloenkt.databinding.FragmentAlramBinding
import com.monggu.instargramcloenkt.databinding.FragmentUserBinding

class UserFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentUserBinding.inflate(inflater,container,false)
        return binding.root
    }
}