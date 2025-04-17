package com.wzh.rewriteplay02.official

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.wzh.base.util.getStatusBarHeight
import com.wzh.base.view.custom.FragmentVp2Adapter
import com.wzh.rewriteplay02.base.BaseTabFragment
import com.wzh.rewriteplay02.databinding.FragmentOfficialAccountsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OfficialAccountFragment : BaseTabFragment() {
    private val viewModel by viewModels<OfficialViewModel>()
    private var binding: FragmentOfficialAccountsBinding? = null
    override fun getRealLayoutView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ): View {
        binding = FragmentOfficialAccountsBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    private lateinit var adapter: FragmentVp2Adapter

    override fun initView() {
        adapter = FragmentVp2Adapter(requireActivity().supportFragmentManager, lifecycle)
        binding?.apply {
            officialViewPager2.adapter = adapter
            tabOfficialAccount.addOnTabSelectedListener(this@OfficialAccountFragment)
            TabLayoutMediator(tabOfficialAccount, officialViewPager2) { tab, position ->
                tab.text = adapter.title(position)
            }.attach()
            tabOfficialAccount.setPadding(0, context.getStatusBarHeight(), 0, 0)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initData() {
        startLoading()
        setDataStatus(viewModel.dataLiveData) {
            val nameList = mutableListOf<String>()
            val viewList = mutableListOf<Fragment>()
            it.forEach { project ->
                nameList.add(project.name)
                viewList.add(OfficialListFragment.newInstance(project.id))
            }
            adapter.apply {
                reset(nameList.toTypedArray())
                reset(viewList)
//                notifyDataSetChanged()
                notifyItemRangeChanged(0, nameList.size)
            }
            binding?.officialViewPager2?.currentItem = viewModel.position
        }
    }

    override fun onTabPageSelected(position: Int) {
        viewModel.position = position
    }

    companion object {
        @JvmStatic
        fun newInstance() = OfficialAccountFragment()
    }
}