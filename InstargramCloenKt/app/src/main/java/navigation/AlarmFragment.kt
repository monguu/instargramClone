package navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.monggu.instargramcloenkt.R
import com.monggu.instargramcloenkt.databinding.FragmentAlramBinding

class AlarmFragment : Fragment() {

    //바인딩 널 초기화
    var fragmentAlarmBinding : FragmentAlramBinding? =null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAlramBinding.inflate(inflater,container,false)

        fragmentAlarmBinding = binding
        return binding.root

    }
        //종료될때 바인딩 파괴
    
}