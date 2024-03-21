package com.raihanresa.githubuser.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.raihanresa.githubuser.data.response.ItemsItem
import com.raihanresa.githubuser.databinding.FragmentFollowBinding

class FollowFragment : Fragment() {

    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
    }

    private lateinit var binding: FragmentFollowBinding
    private val followViewModel by viewModels<FollowViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val index = arguments?.getInt(ARG_SECTION_NUMBER, 0)
        val username = requireActivity().intent.getStringExtra(DetailActivity.EXTRA_LOGIN)

        val layoutManager = LinearLayoutManager(context)
        binding.recycleView.layoutManager = layoutManager

        followViewModel.isLoading.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = it
        }

        followViewModel.isEmpty.observe(viewLifecycleOwner) {
            binding.empty.isVisible = it
        }

        if(index==1) {
            if(followViewModel.listFollowers.value==null) followViewModel.getFollowers(username.toString())
            followViewModel.listFollowers.observe(viewLifecycleOwner) {
                it?.let{ setAdapter(it) }
            }
        } else {
            if(followViewModel.listFollowings.value==null) followViewModel.getFollowings(username.toString())
            followViewModel.listFollowings.observe(viewLifecycleOwner) {
                it?.let{ setAdapter(it) }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }

    private fun setAdapter(user: List<ItemsItem>) {
        val adapter = ItemUserAdapter()
        adapter.submitList(user)
        binding.recycleView.adapter = adapter
    }
}