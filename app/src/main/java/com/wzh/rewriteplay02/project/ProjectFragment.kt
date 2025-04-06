package com.wzh.rewriteplay02.project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.wzh.base.util.getStatusBarHeight
import com.wzh.base.view.custom.FragmentVp2Adapter
import com.wzh.rewriteplay02.base.BaseTabFragment
import com.wzh.rewriteplay02.databinding.FragmentProjectBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProjectFragment : BaseTabFragment() {
    private val viewModel by viewModels<ProjectViewModel>()
    private lateinit var adapter: FragmentVp2Adapter
    private var binding: FragmentProjectBinding? = null

    companion object {
        @JvmStatic
        fun newInstance() = ProjectFragment()
    }

    override fun onTabPageSelected(position: Int) {
        viewModel.position = position
    }

    override fun getRealLayoutView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ): View {
        binding = FragmentProjectBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun initData() {
        startLoading()
        setDataStatus(viewModel.dataLiveData) {
            val nameList = mutableListOf<String>()
            val viewList = mutableListOf<Fragment>()
            //数据没错
            it.forEach { projectTab ->
                nameList.add(projectTab.name)
                viewList.add(ProjectListFragment.newInstance(projectTab.id))
            }
            adapter.apply {
                reset(nameList.toTypedArray())
                reset(viewList)
                notifyItemRangeChanged(0, nameList.size)
            }
            binding?.vp2Project?.currentItem = viewModel.position
        }
    }

    override fun initView() {
        adapter = FragmentVp2Adapter(requireActivity().supportFragmentManager, lifecycle)
        binding?.apply {
            vp2Project.adapter = adapter
            tbProject.addOnTabSelectedListener(this@ProjectFragment)
            TabLayoutMediator(tbProject,vp2Project){tab,position->
                tab.text=adapter.title(position)
            }.attach()
            tbProject.setPadding(0,context.getStatusBarHeight(),0,0)
        }
    }
}