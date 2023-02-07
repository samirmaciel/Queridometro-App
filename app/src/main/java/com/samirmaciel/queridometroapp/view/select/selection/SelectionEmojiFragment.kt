package com.samirmaciel.queridometroapp.view.select.selection

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.samirmaciel.queridometroapp.R
import com.samirmaciel.queridometroapp.databinding.FragmentSelectEmojiBinding
import com.samirmaciel.queridometroapp.model.EmojiSelectionItem
import com.samirmaciel.queridometroapp.view.select.adapter.EmojiSelectAdapter

class SelectionEmojiFragment(val onSelectedEmoji: (EmojiSelectionItem) -> Unit) : DialogFragment(R.layout.fragment_select_emoji) {

    private var mBinding : FragmentSelectEmojiBinding? = null
    private var selectedEmojiItem: EmojiSelectionItem? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBinding(view)
        setupListeners()


        val list = listOf<Int>(
            R.drawable.emojibanana,
            R.drawable.emojibomb,
            R.drawable.emojibrokenheart,
            R.drawable.emojidisgust,
            R.drawable.emojifurious,
            R.drawable.emojihappy,
            R.drawable.emojisnake,
            R.drawable.emojiplant,
            R.drawable.emojiheart
        )

        setupAdapter(list)
    }

    private fun setupListeners() {
        mBinding?.btnSelectionConfirm?.setOnClickListener {
            if(selectedEmojiItem != null){
                onSelectedEmoji(selectedEmojiItem!!)
                dismiss()
            }else{
                Toast.makeText(requireContext(),"Selecione um emoji", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupAdapter(list: List<Int>){
        val emojiSelectionAdapter = EmojiSelectAdapter{
            selectedEmojiItem = it
        }

        emojiSelectionAdapter.emojiSelectionItemList = list.map { EmojiSelectionItem(it) }.toMutableList()

        mBinding?.rvSelectionEmoji?.apply {
            adapter = emojiSelectionAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
        }

        emojiSelectionAdapter.notifyDataSetChanged()
    }

    private fun setupBinding(view: View) {
        mBinding = FragmentSelectEmojiBinding.bind(view)
    }
}