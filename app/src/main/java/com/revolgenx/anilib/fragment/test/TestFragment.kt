package com.revolgenx.anilib.fragment.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.revolgenx.anilib.R
import com.revolgenx.anilib.activity.TestActivity
import kotlinx.android.synthetic.main.test_fragment_layout.*

class TestFragment : Fragment() {

    companion object {
        fun newInstance() = TestFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return layoutInflater.inflate(R.layout.test_fragment_layout, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        createVideoBt.setOnClickListener {
            (activity as TestActivity).prepare()
        }
    }
}
